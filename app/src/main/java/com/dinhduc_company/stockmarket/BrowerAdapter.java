package com.dinhduc_company.stockmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by NguyenDinh on 4/6/2015.
 */
public class BrowerAdapter extends ArrayAdapter {
    Context mContext;
    int layoutID;
    Integer[] listImg;

    public BrowerAdapter(Context context, int resource, Integer[] objects) {
        super(context, resource, objects);
        mContext = context;
        layoutID = resource;
        listImg = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutID, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.brower_image);
        imageView.setImageResource(listImg[position]);
        return convertView;
    }
}
