package com.dinhduc_company.stockmarket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by duy on 4/18/2015.
 */
public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HistoryItem> arrayList;

    public HistoryAdapter(){}

    public HistoryAdapter(Context context, ArrayList<HistoryItem> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_list_item, null);
        }

        TextView txt_symbol = (TextView)convertView.findViewById(R.id.symbol);
        TextView txt_type = (TextView)convertView.findViewById(R.id.type);
        TextView txt_portfolioName = (TextView)convertView.findViewById(R.id.portfolioName);
        TextView txt_quantity = (TextView)convertView.findViewById(R.id.quantity);
        TextView txt_cost = (TextView)convertView.findViewById(R.id.cost);
        TextView txt_time = (TextView)convertView.findViewById(R.id.time);

        if(arrayList.size()>0){
            txt_symbol.setText(arrayList.get(position).getSymbol());
            txt_type.setText(arrayList.get(position).getType());
            txt_portfolioName.setText(arrayList.get(position).getPortfolioName());
            txt_quantity.setText(String.valueOf(arrayList.get(position).getQuantity()));
            txt_cost.setText(String.valueOf(arrayList.get(position).getCost()));
            txt_time.setText(arrayList.get(position).getTime());
        }

        return convertView;
    }
}
