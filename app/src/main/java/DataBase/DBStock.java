package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by duy on 4/2/2015.
 */
public class DBStock {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SYMBOL = "symbol";
    public static final String KEY_TYPE = "type";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_COST = "cost";
    public static final String KEY_TIME = "time";
    public static final String KEY_AVERAGEPURCHASE = "averagePurchase";
    public static final String KEY_INTEREST = "interest";
    public static final String KEY_PORTFOLIO_NAME = "portfolioName";
    public static final String KEY_INVESTING_MONEY = "investingMoney";
    public static final String KEY_TOTAL_MONEY = "totalMoney";

    public static final String TAG = "DBStock";
    public static String DATABASE_NAME;
    public static final String DATABASE_TABLE_HISTORY = "History";
    public static final String DATABASE_TABLE_PORTFOLIO_MANAGING = "Portfolio_Managing";

    public static final int DATABASE_VERSION = 1;

    public static final String PORTFOLIO_MANAGING_DATABASE_CREATE = "create table Portfolio_Managing (_id integer primary key " +
            "autoincrement, portfolioName text not null, interest double not null, investingMoney double not null," +
            "totalMoney double not null)";
    static final String HISTORY_DATABASE_CREATE = "create table History (_id integer primary key autoincrement, " +
            "symbol text not null, type text not null, portfolioName text not null, quantity integer not null," +
            " cost double not null, time text not null);";

    final Context context;
    DataBaseHelper DBHelper;
    SQLiteDatabase db;

    public DBStock(Context ct, String username) {
        this.context = ct;
        DATABASE_NAME = username;
        DBHelper = new DataBaseHelper(context);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(PORTFOLIO_MANAGING_DATABASE_CREATE);
                db.execSQL(HISTORY_DATABASE_CREATE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Porfolio");
            db.execSQL("DROP TABLE IF EXISTS History");
            onCreate(db);
        }
    }

    //===========  open the database =================
    public DBStock open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //=============  close the database =============
    public void close() {
        DBHelper.close();
    }

    //============create Portfolio  ===========
    public void createPortfolioTable(String portfolioName) {
        String sql = "create table '" + portfolioName.replace(" ", "") + "' (_id integer primary key autoincrement, symbol text not null," +
                " quantity integer not null, averagePurchase double not null, cost double not null, interest double not null )";
        db.execSQL(sql);
    }

    public void dropPortfolioTable(String portfolioName) {
        String sqlite = "drop table if exists " + portfolioName.replace(" ","");
        db.execSQL(sqlite);
    }


    //============insert a symbol into the portfolio  ===========
    public long insertPortfolio(String tableName, String symbol, int quantity, double average, double price, double interest) {
        ContentValues values = new ContentValues();
        values.put(KEY_SYMBOL, symbol);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_AVERAGEPURCHASE, average);
        values.put(KEY_COST, price);
        values.put(KEY_INTEREST, interest);
        return db.insert(tableName.replace(" ",""), null, values);
    }

    //============insert a Portfolio into the database  ===========
    public long insertPortfolioManaging(String portfolioName, double interest, double investingMoney, double totalMoney) {
        ContentValues values = new ContentValues();
        values.put(KEY_PORTFOLIO_NAME, portfolioName);
        values.put(KEY_INTEREST, interest);
        values.put(KEY_INVESTING_MONEY, investingMoney);
        values.put(KEY_TOTAL_MONEY, totalMoney);
        return db.insert(DATABASE_TABLE_PORTFOLIO_MANAGING, null, values);
    }

    //============insert a history into the database  ===========
    public long insertHistory(String symbol, String purchaseSell, String portfolioName, int quantity, double cost, String time) {
        ContentValues values = new ContentValues();
        values.put(KEY_SYMBOL, symbol);
        values.put(KEY_TYPE, purchaseSell);
        values.put(KEY_PORTFOLIO_NAME, portfolioName);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_COST, cost);
        values.put(KEY_TIME, time);
        return db.insert(DATABASE_TABLE_HISTORY, null, values);
    }

    //================== delete a particular porfolioManaging  ================
    public boolean deletePortfolioManaging(int id) {
        return db.delete(DATABASE_TABLE_PORTFOLIO_MANAGING, KEY_ROWID + "=" + id, null) > 0;
    }

    //================== delete a particular porfolio  ================
    public boolean deleteStockInPortfolio(String tableName, String symbol) {
        return db.delete(tableName, KEY_SYMBOL + "= '" + symbol + "'", null) > 0;
    }

    //============ delete a particular set of history  ==============
    public boolean deleteHistory(String portfolioName) {
        return db.delete(DATABASE_TABLE_HISTORY, KEY_PORTFOLIO_NAME + " = '" + portfolioName + "'", null) > 0;
    }

    public boolean deleateAllHistory() {
        return db.delete(DATABASE_TABLE_HISTORY, null, null) > 0;
    }

    //===========  retrieves all porfolioManaging ====================
    public Cursor getAllPortfolioManaging() {
        return db.query(DATABASE_TABLE_PORTFOLIO_MANAGING, null, null, null, null, null, null);
    }

    //===========  retrieves all porfolio ====================
    public Cursor getAllPorfolio(String tableName) {
        return db.query(tableName.replace(" ",""), null, null, null, null, null, null);
    }


    //==========  retrieves all history  ===============
    public Cursor getAllHistory() {
        return db.query(DATABASE_TABLE_HISTORY, null, null, null, null, null, null);
    }


    //==============  retrieve a particular symbol ==============
    public Cursor getPorfolio(String tableName, String symbol) {
        return db.query(tableName.replace(" ",""), null,
                KEY_SYMBOL + " = '" + symbol + "'", null, null, null, null);
    }

    //===========  retrieve a particular history ============
    public Cursor getHistory(String symbol) {

        return db.query(true, DATABASE_TABLE_HISTORY, null, KEY_SYMBOL + "=\"" + symbol + "\"", null, null, null, null, null);
    }

    public Cursor getPortfolioManaging(String portfolioName) {
        return db.query(DATABASE_TABLE_PORTFOLIO_MANAGING, null, KEY_PORTFOLIO_NAME + " = '" + portfolioName + "'", null, null, null, null);
    }

    //=============  update a porfolioManaging ====================
    public boolean updatePortfolioManaging(int rowID, String portfolioName) {
        ContentValues values = new ContentValues();
        values.put(KEY_ROWID, rowID);
        values.put(KEY_PORTFOLIO_NAME, portfolioName);
        return db.update(DATABASE_TABLE_PORTFOLIO_MANAGING, values, KEY_ROWID + "=" + rowID, null) > 0;
    }

    public boolean updatePortfolioManaging(String portfolioName, double interest, double investingMoney, double totalMoney) {
        ContentValues values = new ContentValues();
        values.put(KEY_INTEREST, interest);
        values.put(KEY_PORTFOLIO_NAME, portfolioName);
        values.put(KEY_INVESTING_MONEY, investingMoney);
        values.put(KEY_TOTAL_MONEY, totalMoney);
        return db.update(DATABASE_TABLE_PORTFOLIO_MANAGING, values, KEY_PORTFOLIO_NAME + " = '" + portfolioName + "'", null) > 0;
    }

    //=============  update a porfolio ====================
    public boolean updatePorfolio(String tableName, String symbol, int quantity, double average, double price, double interest) {
        ContentValues values = new ContentValues();
        values.put(KEY_SYMBOL, symbol);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_AVERAGEPURCHASE, average);
        values.put(KEY_COST, price);
        values.put(KEY_INTEREST, interest);
        return db.update(tableName.replace(" ",""), values, KEY_SYMBOL + " = '" + symbol + "'", null) > 0;
    }


    //=============  update a set of history ===================
    public boolean updateHistory(String newPortfolioName, String oldPortfolioName) {
        ContentValues values = new ContentValues();
        values.put(KEY_PORTFOLIO_NAME, newPortfolioName);
        return db.update(DATABASE_TABLE_HISTORY, values, KEY_PORTFOLIO_NAME + " = '" + oldPortfolioName + "'", null) > 0;
    }

    public void renameTable(String oldName, String newName) {
        String sqlite = "ALTER TABLE " + oldName.replace(" ","") + " RENAME TO " + newName.replace(" ","");
        db.execSQL(sqlite);
    }

}
