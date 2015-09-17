package com.smartwardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.gson.Gson;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.smartwardrobe.adapter.ClothAdapter;
import com.smartwardrobe.object.Cloth;

import java.net.MalformedURLException;

public class MainActivity extends BaseActivity implements WebServiceListener
{
    GridView list;
    ClothAdapter adapter;
    public static MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (GridView) findViewById(android.R.id.list);
        adapter = new ClothAdapter(this);
        list.setAdapter(adapter);

        //GcmMessageHandler.sendRegisterNotification(this);
        GcmMessageHandler.sendReminderNotification(this);

        try
        {
            mClient = new MobileServiceClient(
                    "smartwardrob.azure-mobile.net",
                    "AIzaSyArNP8RjbBWdjWfHPk9ZV2_iVEDYlKsyh8",
                    this);//.withFilter(new ProgressFilter());
//            // Get the Mobile Service Table instance to use
//            mToDoTable = mClient.getTable(ToDoItem.class);
        } catch (MalformedURLException e)
        {
            Log.d(Values.TAG_DEBUG, e.getMessage() + "");
        }

        CommonUtil.sendWebRequest(Values.WEB_REQUEST_GET_ALL_CLOTHES, null, null, this);

        CommonUtil.getGcmRegId(getApplicationContext());

        GcmMessageHandler handler = new GcmMessageHandler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mi_add)
        {
            Intent intent = new Intent(this, UpdateClothDetailsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWebResponse(int responseCode, String content)
    {
        try
        {
            Gson gson = new Gson();

            Cloth[] cloths = gson.fromJson(content, Cloth[].class);
            adapter.addAll(cloths);
            adapter.notifyDataSetChanged();
        } catch (Exception e)
        {
            Log.d(Values.TAG_DEBUG, e.getMessage() + "");
        }
    }
}
