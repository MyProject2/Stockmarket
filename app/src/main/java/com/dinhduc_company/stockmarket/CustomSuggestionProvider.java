package com.dinhduc_company.stockmarket;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import DataBase.DBUsers;


/**
 * Created by NguyenDinh on 4/13/2015.
 */
public class CustomSuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.dinhduc_company.stockmarket.CustomSuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;
    public static final String[] COLUMNS = {
            SearchManager.SUGGEST_COLUMN_FORMAT,
            SearchManager.SUGGEST_COLUMN_ICON_1,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA,
            BaseColumns._ID
    };

    public CustomSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        DBUsers dbUsers = new DBUsers(getContext());
        dbUsers.open();
        String query = selectionArgs[0];
        if (query == null || query.length() == 0)
            return null;
        Cursor c = dbUsers.getStocks(query);
        Cursor recentCursor = super.query(uri, projection, selection, selectionArgs, sortOrder);
        MatrixCursor customCursor = new MatrixCursor(COLUMNS);

        if (c.moveToFirst()) {
            int i = 0;
            do {
                customCursor.addRow(new Object[]{
                        null,
                        R.drawable.ic_action_search1,
                        c.getString(c.getColumnIndex(DBUsers.KEY_SYMBOL)),
                        c.getString(c.getColumnIndex(DBUsers.KEY_SYMBOL)),
                        i++
                });
            } while (c.moveToNext());
        }
        dbUsers.close();
        return new MergeCursor(new Cursor[]{recentCursor, customCursor});
    }
}
