package com.smartwardrobe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartwardrobe.R;
import com.smartwardrobe.UpdateClothDetailsActivity;
import com.smartwardrobe.Values;
import com.smartwardrobe.object.Cloth;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by leechunhoe on 12/9/15.
 */
public class ClothAdapter extends ArrayAdapter<Cloth>
{
    Context context;

    public ClothAdapter(Context context)
    {
        super(context, R.layout.lv_item_cloth);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ClothViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_cloth, parent, false);
            viewHolder = new ClothViewHolder();

            viewHolder.tvFrequency = (TextView) convertView.findViewById(R.id.tv_frequency);
            viewHolder.tvLastWear = (TextView) convertView.findViewById(R.id.tv_last_wear);
            viewHolder.ivCloth = (ImageView) convertView.findViewById(R.id.iv_cloth);
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tv_message);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ClothViewHolder) convertView.getTag();
        }

        final Cloth cloth = (Cloth) getItem(position);

        if (!cloth.getLastUpdatedAt().equals(cloth.getCreatedAt()))
        {
            viewHolder.tvMessage.setVisibility(View.GONE);
            viewHolder.tvFrequency.setVisibility(View.VISIBLE);
        } else
        {
            viewHolder.tvMessage.setVisibility(View.VISIBLE);
            viewHolder.tvFrequency.setVisibility(View.GONE);
        }

        viewHolder.tvFrequency.setText(Integer.toString(cloth.getFrequency()) + " times");
        viewHolder.tvLastWear.setText("Last wear at " + cloth.getLastUpdatedAt().toString());

        String clothImagePath = Values.APP_ROOT + cloth.getRfid() + ".jpg";
        cloth.setImagePath(clothImagePath);

        Picasso.with(context).load(new File(cloth.getImagePath())).into(viewHolder.ivCloth);

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, UpdateClothDetailsActivity.class);
                intent.putExtra(Cloth.KEY_SELF, cloth);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ClothViewHolder
    {
        TextView tvLastWear;
        ImageView ivCloth;
        TextView tvFrequency;
        TextView tvMessage;
    }

}
