package com.dinhduc_company.stockmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import DataBase.DBUsers;

/**
 * Created by duy on 4/2/2015.
 */
public class Loading extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading);

        new RetrieveDataTask().execute();
    }

    private class RetrieveDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DBUsers dbUsers = new DBUsers(getBaseContext());
            dbUsers.open();
            if (!dbUsers.getStocks("FBRC").moveToFirst()) {
                String[] stockList = getResources().getStringArray(R.array.list_stock);
                for (String stock : stockList) {
                    dbUsers.insertStock(stock);
                }
            }
            dbUsers.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(getBaseContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }
}
