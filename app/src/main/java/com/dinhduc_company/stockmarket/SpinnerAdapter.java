package com.dinhduc_company.stockmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NguyenDinh on 4/8/2015.
 */
public class SpinnerAdapter extends ArrayAdapter {
    Context mContext;
    int layoutId;
    ArrayList<String> portfolioNames;

    public SpinnerAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        layoutId = resource;
        portfolioNames = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, null);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(portfolioNames.get(position));
        return convertView;
    }
}
