package com.dinhduc_company.stockmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDinh on 3/29/2015.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem> {
    Context mContext;
    int layoutId;
    ArrayList<NavigationDrawerItem> list;

    public NavigationDrawerAdapter(Context context, int resource, ArrayList<NavigationDrawerItem> objects) {
        super(context, resource, objects);
        mContext = context;
        layoutId = resource;
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, null);
        }
            NavigationDrawerItem item = list.get(position);
            ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
            itemIcon.setImageResource(item.getItemIcon());
            TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
            itemName.setText(mContext.getString(item.getItemName()));
        return convertView;
    }
}
