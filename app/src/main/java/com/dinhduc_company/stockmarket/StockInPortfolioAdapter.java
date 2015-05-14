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
public class StockInPortfolioAdapter extends ArrayAdapter {
    Context mContext;
    int layoutID;
    ArrayList<StockInPortfolio> stockInPortfolios;

    public StockInPortfolioAdapter(Context context, int resource, ArrayList<StockInPortfolio> objects) {
        super(context, resource, objects);
        mContext = context;
        layoutID = resource;
        stockInPortfolios = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.stock_in_portfolio_item, null);
        }
        StockInPortfolio stockInPortfolio = stockInPortfolios.get(position);
        TextView symbol = (TextView) convertView.findViewById(R.id.symbolPortfolio);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantityPortfolio);
        TextView average = (TextView) convertView.findViewById(R.id.averagePortfolio);
        TextView cost = (TextView) convertView.findViewById(R.id.costPortfolio);
        TextView interest = (TextView) convertView.findViewById(R.id.interestPortfolio);
        symbol.setText(stockInPortfolio.getSymbol());
        quantity.setText(stockInPortfolio.getQuantity() + "");
        average.setText(stockInPortfolio.getAveragePurchase() + "");
        cost.setText(stockInPortfolio.getCost() + "");
        interest.setText(stockInPortfolio.getInterest() + "%");
        return convertView;
    }
}
