package com.dinhduc_company.stockmarket;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import DataBase.DBStock;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPortfolioManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPortfolioManagement extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DBStock dbStock;
    ArrayList<Portfolio> portfolios = new ArrayList<>();
    PortfolioAdapter adapter;
    ListView listPortfolio;
    int position = 0;
    String name;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPortfolioManagement.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPortfolioManagement newInstance(String param1, String param2) {
        FragmentPortfolioManagement fragment = new FragmentPortfolioManagement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentPortfolioManagement() {
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
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_portfolio_management, container, false);
        dbStock = new DBStock(getActivity(), Login.username);
        dbStock.open();
        Cursor c = dbStock.getAllPortfolioManaging();
        if (c.moveToFirst()) {
            do {
                Portfolio portfolio = new Portfolio();
                portfolio.setId(c.getInt(c.getColumnIndex(DBStock.KEY_ROWID)));
                portfolio.setPortfolioName(c.getString(c.getColumnIndex(DBStock.KEY_PORTFOLIO_NAME)));
                portfolio.setInterest(c.getDouble(c.getColumnIndex(DBStock.KEY_INTEREST)));
                portfolios.add(portfolio);
            } while (c.moveToNext());
        }
        listPortfolio = (ListView) rootView.findViewById(R.id.listPortfolio);
        adapter = new PortfolioAdapter(getActivity(), R.layout.portfolio, portfolios);
        listPortfolio.setAdapter(adapter);
        listPortfolio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Portfolio portfolio = portfolios.get(i);
                String portfolioName = portfolio.getPortfolioName();
                Intent intent = new Intent(getActivity(), PortfolioActivity.class);
                intent.putExtra("portfolio_name", portfolioName);
                startActivity(intent);
            }
        });
        registerForContextMenu(listPortfolio);
        return rootView;
    }

    public void showAddDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Add Portfolio");
        dialog.setContentView(R.layout.add_portfolio);
        final EditText editText = (EditText) dialog.findViewById(R.id.addPortfolioET);
        Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
        Button noBtn = (Button) dialog.findViewById(R.id.noBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String portfolioName = editText.getText().toString();
                if (portfolioName.replace(" ","").equals("")){
                    Toast.makeText(getActivity(),"Portfolio Name must have letters", Toast.LENGTH_SHORT).show();
                }else {
                    if (dbStock.getPortfolioManaging(portfolioName.trim()).moveToFirst()) {
                        Toast.makeText(getActivity(), "Can't add an exist Portfolio", Toast.LENGTH_SHORT).show();
                    } else {
                        dbStock.createPortfolioTable(portfolioName.trim());
                        dbStock.insertPortfolioManaging(portfolioName, 0, 0, 0);
                        Cursor c = dbStock.getAllPortfolioManaging();
                        c.moveToLast();
                        Portfolio portfolio = new Portfolio();
                        portfolio.setId(c.getInt(c.getColumnIndex(DBStock.KEY_ROWID)));
                        portfolio.setPortfolioName(portfolioName);
                        portfolio.setInterest(0);
                        portfolios.add(portfolio);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Add Portfolio Successfully", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void showEditDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Edit Portfolio");
        dialog.setContentView(R.layout.add_portfolio);
        final EditText editText = (EditText) dialog.findViewById(R.id.addPortfolioET);
        final Portfolio portfolio = portfolios.get(position);
        final String oldName = portfolio.getPortfolioName();
        editText.setText(oldName);
        Button yesBtn = (Button) dialog.findViewById(R.id.yesBtn);
        Button noBtn = (Button) dialog.findViewById(R.id.noBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String portfolioName = editText.getText().toString();
                if (dbStock.getPortfolioManaging(portfolioName.trim()).moveToFirst()) {
                    Toast.makeText(getActivity(), "Can't change to an exist Portfolio", Toast.LENGTH_SHORT).show();
                } else {
                    dbStock.updatePortfolioManaging(portfolio.getId(), portfolioName);
                    portfolio.setPortfolioName(portfolioName);
                    adapter.notifyDataSetChanged();
                    dbStock.renameTable(oldName, portfolioName);
                    dbStock.updateHistory(portfolioName, oldName);
                    Toast.makeText(getActivity(), "Edit Portfolio Successfully", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void showDeleteDialog() {
        final Portfolio portfolio = portfolios.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this portfolio");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String portfolioName = portfolio.getPortfolioName();
                dbStock.deleteHistory(portfolioName);
                dbStock.dropPortfolioTable(portfolioName);
                dbStock.deletePortfolioManaging(portfolio.getId());
                portfolios.remove(portfolio);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Delete Portfolio Successfully", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.portfolio, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addPortfolio) {
            showAddDialog();
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.portfolio_context_menu, menu);
        if (v.getId() == R.id.listPortfolio) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            position = info.position;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_portfolio) {
            showEditDialog();
        } else {
            showDeleteDialog();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbStock.close();
    }
}
