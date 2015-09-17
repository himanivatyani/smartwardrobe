package com.smartwardrobe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartwardrobe.object.Cloth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Update cloth details
 */
public class UpdateClothDetailsActivity extends BaseActivity implements View.OnClickListener, WebServiceListener
{
    private Cloth cloth;
    //private String clothRfid;
    private Spinner spClothType;
    private ImageView ivCloth;
    private Button btnTakePhoto;
    private EditText etClothName;
    private ArrayAdapter<String> clothTypeAdapter;

    private static final int REQUEST_CODE_TAKE_PHOTO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cloth_details);

        btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        ivCloth = (ImageView) findViewById(R.id.iv_cloth);
        spClothType = (Spinner) findViewById(R.id.sp_cloth_type);
        etClothName = (EditText) findViewById(R.id.et_cloth_name);

        String currentTimeInMillis = Long.toString(Calendar.getInstance().getTimeInMillis());
        etClothName.setText("cloth_" + currentTimeInMillis);

        clothTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Cloth.CLOTH_TYPES);
        spClothType.setAdapter(clothTypeAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnTakePhoto.setOnClickListener(this);

        cloth = (Cloth) getIntent().getSerializableExtra(Cloth.KEY_SELF);

        if (cloth != null)
        {
            etClothName.setText(cloth.getName());

            String clothImagePath = Values.APP_ROOT + cloth.getRfid() + ".jpg";
            cloth.setImagePath(clothImagePath);
            Picasso.with(this).load(new File(cloth.getImagePath())).error(R.drawable.hanger).into(ivCloth);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_cloth_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.mi_done)
        {
//            if (clothRfid != null && clothRfid.length() > 0)
            if (cloth != null)
            {
                //cloth.setRfid(clothRfid);
                String clothName = etClothName.getText().toString();
                String clothType = spClothType.getSelectedItem().toString();
                cloth.setName(clothName);
                cloth.setType(clothType);

                Bitmap bitmap = ((BitmapDrawable) ivCloth.getDrawable()).getBitmap();

                // This is for actually upload image to server, skip this for demo and save to local instead
                //            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                //            byte[] clothImage = stream.toByteArray();
                //            cloth.setImage(clothImage);

                // Save into local folder
                CommonUtil.saveBitmap(bitmap, "/SmartWardrobe/", cloth.getRfid());

                // TODO: Send web request
                // PUT /api/cloth/, body: Cloth

                HashMap<String, String> urlParams = new HashMap<String, String>();

                // TODO: Change to rfid later
                urlParams.put("rfid", cloth.getRfid());

                CommonUtil.sendWebRequest(Values.WEB_REQUEST_UPDATE_CLOTH_INFO, urlParams, cloth, this);
            } else
            {
                Toast.makeText(this, "No rfid", Toast.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE_PHOTO)
        {
            switch (resultCode)
            {
                case RESULT_OK:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ivCloth.setImageBitmap(imageBitmap);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_take_photo:
                // Take photo from camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
                break;
        }
    }

    @Override
    public void onWebResponse(int responseCode, String content)
    {
        if (responseCode == 201)
        {
            Toast.makeText(this, "Register cloth success!", Toast.LENGTH_LONG).show();
            finish();
        } else
        {
            Toast.makeText(this, "Register cloth failed. Response code: " + responseCode, Toast.LENGTH_LONG).show();
        }
    }
}
