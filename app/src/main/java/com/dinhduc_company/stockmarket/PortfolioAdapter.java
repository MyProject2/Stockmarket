package com.dinhduc_company.stockmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NguyenDinh on 4/7/2015.
 */
public class PortfolioAdapter extends ArrayAdapter {
    Context mContext;
    int layoutID;
    ArrayList<Portfolio> portfolios;

    public PortfolioAdapter(Context context, int resource, ArrayList<Portfolio> objects) {
        super(context, resource, objects);
        mContext = context;
        layoutID = resource;
        portfolios = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutID, null);
        }
        TextView portfolioName = (TextView) convertView.findViewById(R.id.portfolioName);
        TextView interest = (TextView) convertView.findViewById(R.id.interest);
        Portfolio portfolio = portfolios.get(position);
        portfolioName.setText(portfolio.getPortfolioName());
        interest.setText(portfolio.getInterest() + "%");
        return convertView;
    }
}
