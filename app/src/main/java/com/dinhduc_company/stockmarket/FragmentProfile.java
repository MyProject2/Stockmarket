package com.dinhduc_company.stockmarket;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import DataBase.DBStock;
import DataBase.DBUsers;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView userImg;
    TextView user_name, gender, email, investingMoney, totalMoney, interest;
    public static final int REQUEST_CODE = 1;
    public static String userName;
    DBUsers dbUsers;
    DBStock dbStock;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentProfile() {
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
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(rootView);
        getControl();
        getData();
        return rootView;
    }

    public void initView(View rootView) {
        userImg = (ImageView) rootView.findViewById(R.id.userImg);
        user_name = (TextView) rootView.findViewById(R.id.userName);
        gender = (TextView) rootView.findViewById(R.id.gender);
        email = (TextView) rootView.findViewById(R.id.email);
        investingMoney = (TextView) rootView.findViewById(R.id.investingMoney);
        totalMoney = (TextView) rootView.findViewById(R.id.totalMoney);
        interest = (TextView) rootView.findViewById(R.id.profit);
    }

    public void getControl() {
        userImg.setImageResource(R.drawable.a);
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BrowerImage.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    public void getData() {
        dbStock = new DBStock(getActivity(), Login.username);
        dbStock.open();
        dbUsers = new DBUsers(getActivity());
        dbUsers.open();
        Intent intent = getActivity().getIntent();
        userName = intent.getStringExtra("user");
        Cursor c = dbUsers.getUser(userName);
        user_name.setText(userName);
        int imgRes = c.getInt(c.getColumnIndex(DBUsers.KEY_IDAVA));
        if (imgRes != 0) {
            userImg.setImageResource(imgRes);
        }
        gender.setText(c.getString(c.getColumnIndex(DBUsers.KEY_GENDER)));
        email.setText(c.getString(c.getColumnIndex(DBUsers.KEY_EMAIL)));

        Cursor cursor = dbStock.getAllPortfolioManaging();
        double investingMoney = 0;
        double totalMoney = 0;
        if (cursor.moveToFirst()) {
            do {
                investingMoney += cursor.getDouble(cursor.getColumnIndex(DBStock.KEY_INVESTING_MONEY));
                totalMoney += cursor.getDouble(cursor.getColumnIndex(DBStock.KEY_TOTAL_MONEY));
            } while (cursor.moveToNext());
            double interest = (double) Math.round(((totalMoney - investingMoney) / investingMoney * 100) * 100) / 100;
            this.investingMoney.setText(investingMoney + "$");
            this.totalMoney.setText(totalMoney + "$");
            this.interest.setText(interest + "%");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                int imgRes = data.getIntExtra("imageRes", 0);
                userImg.setImageResource(imgRes);
                dbUsers.updateAva(userName, imgRes);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbUsers.close();
        dbStock.close();
    }
}
