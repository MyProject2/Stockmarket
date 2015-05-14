package com.dinhduc_company.stockmarket;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.views.ButtonFloat;

import DataBase.DBStock;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    ButtonFloat buttonFloat;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getResources().getString(R.string.portfolio);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        buttonFloat = (ButtonFloat) findViewById(R.id.buttonFloat);
        buttonFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNavigationDrawerItemSelected(2);
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(mTitle);
                mNavigationDrawerFragment.setItemCheckedDrawr(2);

            }
        });
        onNavigationDrawerItemSelected(1);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        if (position == 4) {
            showDialog();
        } else if (position == 5) {
            signOut();
        } else {
            onSectionAttached(position);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment = new Fragment();
            switch (position) {
                case 0:
                    fragment = new FragmentProfile();
                    break;
                case 1:
                    fragment = new FragmentPortfolioManagement();
                    break;
                case 2:
                    fragment = new FragmentTransactions();
                    break;
                case 3:
                    fragment = new FragmentHistory();
                    break;
            }
            transaction.replace(R.id.container, fragment).commit();
        }
    }

    //==================display dialog signOut application  =======================
    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm!")
                .setMessage("Do you want to sign out?")
                .setIcon(getResources().getDrawable(R.drawable.exit))
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getBaseContext(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        builder.show();

    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(mTitle);
        builder.setMessage("Copyright by V.I.P\nNguyen Dinh Duc\nVu Xuan Huy\nTran Quang Duy");
        builder.setIcon(R.drawable.about_us);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.profile);
                break;
            case 1:
                mTitle = getString(R.string.portfolio);
                break;
            case 2:
                mTitle = getString(R.string.transactions);
                break;
            case 3:
                mTitle = getString(R.string.history);
                break;
            case 4:
                mTitle = getString(R.string.about_us);
                break;
            case 5:
                mTitle = getString(R.string.sign_out);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        buttonFloat.setVisibility(View.VISIBLE);
    }

    public double getTwoDigit(double s) {
        return (double) Math.round(s * 100) / 100;
    }

    public void update() {
        GetStockData stockData = new GetStockData();
        DBStock dbStock = new DBStock(this, Login.username);
        dbStock.open();
        Cursor c = dbStock.getAllPortfolioManaging();
        if (c.moveToFirst()) {
            do {
                String portfolioName = c.getString(c.getColumnIndex(DBStock.KEY_PORTFOLIO_NAME));
                double investingMoney = 0;
                double totalMoney = 0;
                Cursor c1 = dbStock.getAllPorfolio(portfolioName);
                if (c1.moveToFirst()) {
                    do {
                        String symbol = c1.getString(c1.getColumnIndex(DBStock.KEY_SYMBOL));
                        int quantity = c1.getInt(c1.getColumnIndex(DBStock.KEY_QUANTITY));
                        double average = c1.getDouble(c1.getColumnIndex(DBStock.KEY_AVERAGEPURCHASE));
                        double priceMarket = Double.parseDouble(stockData.getStockData(symbol).getLastTradePrice());
                        double interest = getTwoDigit((priceMarket / average - 1) * 100);
                        investingMoney += quantity * average;
                        totalMoney += quantity * priceMarket;
                        dbStock.updatePorfolio(portfolioName, symbol, quantity, average, priceMarket, interest);
                    } while (c1.moveToNext());
                }
                double totalInterest = getTwoDigit((totalMoney / investingMoney - 1) * 100);
                dbStock.updatePortfolioManaging(portfolioName, totalInterest, investingMoney, totalMoney);
            } while (c.moveToNext());
        }
        dbStock.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            return true;
        } else {
            buttonFloat.setVisibility(View.GONE);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.clear_history_main) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, CustomSuggestionProvider.AUTHORITY, CustomSuggestionProvider.MODE);
            suggestions.clearHistory();
        } else if (id == R.id.update) {
            CheckInternet checkInternet = new CheckInternet(this);
            if (checkInternet.hasInternet()) {
                progressDialog = ProgressDialog.show(this, "Please wait...", "Updating...", true, true);
                new UpdateTask().execute();
            } else
                checkInternet.showInternetDialog();

        } else if (id == R.id.clear_transaction_history) {
            DBStock dbStock = new DBStock(this, Login.username);
            dbStock.open();
            dbStock.deleateAllHistory();
            dbStock.close();
        }

        return super.onOptionsItemSelected(item);
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            update();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Fragment fragment = new Fragment();
            if (mTitle.toString().equals("Profile")) {
                fragment = new FragmentProfile();
            } else if (mTitle.toString().equals("Portfolio")) {
                fragment = new FragmentPortfolioManagement();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, fragment).commit();
        }
    }
}
