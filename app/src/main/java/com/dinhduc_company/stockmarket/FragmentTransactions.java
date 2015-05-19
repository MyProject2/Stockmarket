package com.dinhduc_company.stockmarket;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import DataBase.DBStock;


public class FragmentTransactions extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] symbolsTransaction;
    SpinnerAdapter adapter;
    ArrayAdapter symbolAdapter;
    Spinner portfolioSelection, transactionType;
    EditText editQuantity, editPricePurchase, editPriceMarket, editDate;
    AutoCompleteTextView symbolTransaction;
    DBStock dbStock;
    ArrayList<String> portfolioNames = new ArrayList<>();
    Button confirm, pickDate;
    private String tableName;
    private String symbol;
    private int quantity;
    private double priceMarket;
    private double priceTransaction;
    private double investingMoneyPortfolio;
    private double totalMoneyPortfolio;
    private String type;
    private String date;
    private Calendar calendar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTransactions.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTransactions newInstance(String param1, String param2) {
        FragmentTransactions fragment = new FragmentTransactions();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentTransactions() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);
        initView(rootView);
        getControl();
        return rootView;
    }

    public void getData() {
        dbStock = new DBStock(getActivity(), Login.username);
        dbStock.open();
        Cursor c = dbStock.getAllPortfolioManaging();
        if (c.moveToFirst()) {
            do {
                portfolioNames.add(c.getString(c.getColumnIndex(DBStock.KEY_PORTFOLIO_NAME)));
            } while (c.moveToNext());
        }
        dbStock.close();
    }

    private void initView(View rootView) {
        getData();
        symbolTransaction = (AutoCompleteTextView) rootView.findViewById(R.id.symbolTransaction);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                symbolsTransaction = getResources().getStringArray(R.array.list_stock);
                symbolAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, symbolsTransaction);
                symbolTransaction.setAdapter(symbolAdapter);
            }
        });
        portfolioSelection = (Spinner) rootView.findViewById(R.id.portfolioSelection);
        transactionType = (Spinner) rootView.findViewById(R.id.transactionType);
        editQuantity = (EditText) rootView.findViewById(R.id.editQuantity);
        editPricePurchase = (EditText) rootView.findViewById(R.id.editPricePurchase);
        editPriceMarket = (EditText) rootView.findViewById(R.id.editPriceMarket);
        editDate = (EditText) rootView.findViewById(R.id.editDate);
        adapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, portfolioNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        portfolioSelection.setAdapter(adapter);
        confirm = (Button) rootView.findViewById(R.id.confirm);
        pickDate = (Button) rootView.findViewById(R.id.pickDate);
        calendar = Calendar.getInstance();
    }

    private void getControl() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if every field is filled in
                if (!checkForm()) {
                    Toast.makeText(getActivity(), "Form Missing", Toast.LENGTH_SHORT).show();
                } else {
                    dbStock.open();
                    if (isSuccessTransaction()) {
                        dbStock.insertHistory(symbol, type, tableName, quantity, priceTransaction, date);
                        dbStock.updatePortfolioManaging(tableName, calculateInterestPortfolio(), investingMoneyPortfolio, totalMoneyPortfolio);
                        Toast.makeText(getActivity(), "Successful Transaction", Toast.LENGTH_SHORT).show();
                    }
                    dbStock.close();
                }

            }
        });
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
    }

    public void pickDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());
        String arr[] = date.split("/");
        int day = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]) - 1;
        int year = Integer.parseInt(arr[2]);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                String date = i3 + "/" + (i2 + 1) + "/" + i;
                editDate.setText(date);
                calendar.set(i, i2, i3);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dialog.show();
    }

    public boolean isSuccessTransaction() {
        tableName = (String) portfolioSelection.getSelectedItem();
        type = (String) transactionType.getSelectedItem();
        symbol = symbolTransaction.getText().toString();
        quantity = Integer.parseInt(editQuantity.getText().toString());
        priceMarket = Double.parseDouble(editPriceMarket.getText().toString());

        priceTransaction = Double.parseDouble(editPricePurchase.getText().toString());
        date = editDate.getText().toString();
        double average = priceTransaction;
        double interest = 0;
        Cursor c = dbStock.getPorfolio(tableName, symbol);
        // check if have the symbol in portfolio
        if (!c.moveToFirst()) {
            //check type transaction is buy or sell
            if (type.equals("BUY")) {
                interest = (double) Math.round(((priceMarket - average) / average * 100) * 100) / 100;
                dbStock.insertPortfolio(tableName, symbol, quantity, average, priceMarket, interest);
                return true;
            } else {
                Toast.makeText(getActivity(), "Can't sell the stock you don't have", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (type.equals("BUY")) {
                average = getTwoDigit(calculateBuyAverage(c));
                interest = getTwoDigit((priceMarket - average) / average * 100);
                dbStock.updatePorfolio(tableName, symbol, quantity + c.getInt(c.getColumnIndex(DBStock.KEY_QUANTITY))
                        , average, priceMarket, interest);
                return true;

            } else {
                if (quantity > c.getInt(c.getColumnIndex(DBStock.KEY_QUANTITY))) {
                    Toast.makeText(getActivity(), "Not enough stock quality to sell", Toast.LENGTH_SHORT).show();
                } else if (quantity == c.getInt(c.getColumnIndex(DBStock.KEY_QUANTITY))) {
                    dbStock.deleteStockInPortfolio(tableName, symbol);
                    return true;
                } else {
                    average = calculateSellAverage(c);
                    interest = getTwoDigit((priceMarket - average) / average * 100);
                    dbStock.updatePorfolio(tableName, symbol, c.getInt(c.getColumnIndex(DBStock.KEY_QUANTITY)) - quantity
                            , average, priceMarket, interest);
                    return true;
                }
            }

        }
        return false;
    }

    public double getTwoDigit(double s) {
        return (double) Math.round(s * 100) / 100;
    }

    public boolean checkForm() {
        if (symbolTransaction.getText().toString().equals("") || editQuantity.getText().toString().equals("")
                || editPriceMarket.getText().toString().equals("") || editDate.getText().toString().equals("")
                || editPricePurchase.getText().toString().equals(""))
            return false;
        return true;
    }

    public double calculateBuyAverage(Cursor c) {
        int quantity = c.getInt(c.getColumnIndex(DBStock.KEY_QUANTITY));
        double average = c.getDouble(c.getColumnIndex(DBStock.KEY_AVERAGEPURCHASE));
        return (quantity * average + this.quantity * this.priceTransaction) / (quantity + this.quantity);
    }

    public double calculateSellAverage(Cursor c) {
        return c.getDouble(c.getColumnIndex(DBStock.KEY_AVERAGEPURCHASE));
    }

    public double calculateInterestPortfolio() {
        Cursor c = dbStock.getAllPorfolio(tableName);
        if (c.moveToFirst()) {
            int quantity = 0;
            do {
                quantity = c.getInt(c.getColumnIndex(DBStock.KEY_QUANTITY));
                investingMoneyPortfolio += getTwoDigit(quantity * c.getDouble(c.getColumnIndex(DBStock.KEY_AVERAGEPURCHASE)));
                totalMoneyPortfolio += getTwoDigit(quantity * c.getDouble(c.getColumnIndex(DBStock.KEY_COST)));
            } while (c.moveToNext());
            return getTwoDigit(((totalMoneyPortfolio - investingMoneyPortfolio) / investingMoneyPortfolio * 100));
        }
        return 0;
    }


}