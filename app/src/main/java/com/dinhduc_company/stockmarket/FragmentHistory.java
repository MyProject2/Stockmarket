package com.dinhduc_company.stockmarket;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import DataBase.DBStock;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHistory newInstance(String param1, String param2) {
        FragmentHistory fragment = new FragmentHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentHistory() {
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

    ListView listView;
    ArrayList<HistoryItem> arrayList = new ArrayList<HistoryItem>();
    HistoryAdapter adapter;
    HistoryItem item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_history, container, false);

        // reference into listView
        listView = (ListView)root.findViewById(R.id.listView);

        // get data for arrayList to database
        getData();

        adapter = new HistoryAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);

        return root;
    }

    private void getData() {
        Login login = new Login();
        DBStock dbStock = new DBStock(getActivity(), login.getUsername());
        dbStock.open();
        Cursor cursor = dbStock.getAllHistory();
        if(cursor.moveToFirst()){
            do{
                item = new HistoryItem(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        Integer.parseInt(cursor.getString(4)), Double.parseDouble(cursor.getString(5)), cursor.getString(6));
                arrayList.add(item);
            }while(cursor.moveToNext());
        }
        dbStock.close();

    }


}
