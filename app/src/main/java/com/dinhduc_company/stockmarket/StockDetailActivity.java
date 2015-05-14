package com.dinhduc_company.stockmarket;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by NguyenDinh on 4/12/2015.
 */
public class StockDetailActivity extends ActionBarActivity {
    TextView lastTradePrice, price_percentChange, date, previousClose, daysRange, open, yearRange;
    TextView bid, volume, avgVolume, ask, oneYearTarget, marketCap, pe, eps;
    ImageView arrow;
    Stock stock = new Stock();
    String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            symbol = intent.getStringExtra(SearchManager.QUERY);
            if (symbol != null) {
                Log.w("debug", "hello");
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, CustomSuggestionProvider.AUTHORITY, CustomSuggestionProvider.MODE);
                suggestions.saveRecentQuery(symbol.toUpperCase(), null);
            } else {
                symbol = intent.getData().getLastPathSegment();
            }
        } else
            symbol = getIntent().getStringExtra("stock");
        actionBar.setTitle(symbol.toUpperCase());
        CheckInternet checkInternet = new CheckInternet(this);
        if (checkInternet.hasInternet()) {
            new GetStockDataTask().execute(symbol);
        } else
            checkInternet.showInternetDialog();
    }


    private void initView() {
        lastTradePrice = (TextView) findViewById(R.id.lastTrade);
        arrow = (ImageView) findViewById(R.id.arrow);
        price_percentChange = (TextView) findViewById(R.id.price_percent_change);
        String percentChange = stock.getPercentChange().replace("-", "");
        double priceChange = Double.parseDouble(stock.getPriceChange());
        if (priceChange > 0) {
            arrow.setImageResource(R.drawable.up_arrow);
            price_percentChange.setTextColor(getResources().getColor(R.color.up_arrow));
        } else {
            arrow.setImageResource(R.drawable.down_arrow);
            price_percentChange.setTextColor(getResources().getColor(R.color.down_arrow));
        }
        String str = Math.abs(priceChange) + "(" + percentChange + ")";
        price_percentChange.setText(str);
        date = (TextView) findViewById(R.id.date);
        previousClose = (TextView) findViewById(R.id.previosClose);
        daysRange = (TextView) findViewById(R.id.dayRange);
        open = (TextView) findViewById(R.id.open);
        yearRange = (TextView) findViewById(R.id.yearRange);
        bid = (TextView) findViewById(R.id.bid);
        volume = (TextView) findViewById(R.id.volume);
        avgVolume = (TextView) findViewById(R.id.avgVolume);
        ask = (TextView) findViewById(R.id.ask);
        oneYearTarget = (TextView) findViewById(R.id.oneYear);
        marketCap = (TextView) findViewById(R.id.marketCap);
        pe = (TextView) findViewById(R.id.pe);
        eps = (TextView) findViewById(R.id.eps);

        lastTradePrice.setText(stock.getLastTradePrice());
        date.setText(stock.getDate());
        previousClose.setText(stock.getPreviousClose());
        daysRange.setText(stock.getDaysRange());
        open.setText(stock.getOpen());
        yearRange.setText(stock.getYearRange());
        bid.setText(stock.getBid());
        volume.setText(stock.getVolume());
        avgVolume.setText(stock.getAvgVolume());
        ask.setText(stock.getAsk());
        oneYearTarget.setText(stock.getOneYearTarget());
        marketCap.setText(stock.getMarketCap());
        pe.setText(stock.getPe());
        eps.setText(stock.getEps());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private class GetStockDataTask extends AsyncTask<String, Void, Stock> {

        @Override
        protected Stock doInBackground(String... strings) {
            GetStockData getStockData = new GetStockData();
            return getStockData.getStockData(strings[0]);
        }

        @Override
        protected void onPostExecute(Stock result) {
            super.onPostExecute(result);
            stock = result;
            initView();
        }
    }
}
