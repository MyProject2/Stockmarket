package com.dinhduc_company.stockmarket;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;

import DataBase.DBStock;

/**
 * Created by NguyenDinh on 4/7/2015.
 */
public class PortfolioActivity extends ActionBarActivity {
    DBStock dbStock;
    ListView listInterest;
    ArrayList<StockInPortfolio> stockInPortfolios = new ArrayList<>();
    StockInPortfolioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        getControl();
    }

    public void getData() {
        dbStock = new DBStock(this, Login.username);
        dbStock.open();
        Intent intent = getIntent();
        String tableName = intent.getStringExtra("portfolio_name");
        Cursor c = dbStock.getAllPorfolio(tableName);
        if (c.moveToFirst()) {
            do {
                StockInPortfolio stockInPortfolio = new StockInPortfolio();
                stockInPortfolio.setId(c.getInt(c.getColumnIndex(DBStock.KEY_ROWID)));
                stockInPortfolio.setSymbol(c.getString(c.getColumnIndex(DBStock.KEY_SYMBOL)));
                stockInPortfolio.setQuantity(c.getDouble(c.getColumnIndex(DBStock.KEY_QUANTITY)));
                stockInPortfolio.setAveragePurchase(c.getDouble(c.getColumnIndex(DBStock.KEY_AVERAGEPURCHASE)));
                stockInPortfolio.setCost(c.getDouble(c.getColumnIndex(DBStock.KEY_COST)));
                stockInPortfolio.setInterest(c.getDouble(c.getColumnIndex(DBStock.KEY_INTEREST)));
                stockInPortfolios.add(stockInPortfolio);
            } while (c.moveToNext());
        }
        dbStock.close();

    }

    public void initView() {
        getData();
        listInterest = (ListView) findViewById(R.id.interestList);
        adapter = new StockInPortfolioAdapter(this, R.layout.stock_in_portfolio_item, stockInPortfolios);
        listInterest.setAdapter(adapter);
    }

    public void getControl() {
        listInterest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), StockDetailActivity.class);
                intent.putExtra("stock", stockInPortfolios.get(i).getSymbol());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }


}
