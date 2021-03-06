package com.murach.tipcalculator;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by 660161986.
 */
public class TipDB {

    public static final String DB_NAME = "TipDB.db";
    public static final int    DB_VERSION = 1;

    public static final String TIP_TABLE = "tip";

    public static final String TIP_ID = "_id"
            , BILL_DATE = "bill_date"
            , BILL_AMOUNT = "bill_amount"
            , TIP_PERCENT = "tip_percent";

    //columns
    public static final int TIP_ID_COL = 0
            , BILL_DATE_COL = 1
            , BILL_AMOUNT_COL = 2
            , TIP_PERCENT_COL = 3;

    public static final String CREATE_TIP_TABLE =
            "CREATE TABLE " + TIP_TABLE + " (" +
                    TIP_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BILL_DATE    + " INTEGER NOT NULL, " +
                    BILL_AMOUNT  + " REAL NOT NULL, " +
                    TIP_PERCENT  + " REAL NOT NULL);";

    public static final String DROP_TIP_TABLE =
            "DROP TABLE IF EXISTS " + TIP_TABLE;


    //Helper class
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create table
            db.execSQL(CREATE_TIP_TABLE);

            // insert default values
            insert(db,1,0,55.62f,.18f);
            insert(db, 2, 0, 3.50f, .25f);
        }

        public void insert(SQLiteDatabase db, int id, int billDate, float billAmount, float tipPercent)
        {
            db.execSQL("INSERT INTO tip VALUES ("
                    + id + ", "
                    + billDate + ", "
                    + billAmount + ", "
                    + tipPercent + ")");
        }

        //insert with incrementing id
        public void insert(SQLiteDatabase db, int billDate, float billAmount, float tipPercent)
        {
            db.execSQL("INSERT INTO tip VALUES ("
                    + billDate + ", "
                    + billAmount + ", "
                    + tipPercent + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.d("Tip Database", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(TipDB.DROP_TIP_TABLE);
            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public TipDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    //get all database contents
    public ArrayList<TipOBJ> getTips() {
        ArrayList<TipOBJ> tips = new ArrayList<TipOBJ>();

        //open the database
        openReadableDB();

        //obtain the cursor
        Cursor cursor = db.query(TIP_TABLE,
                null, null, null, null, null, null);

        //list db entries
        while (cursor.moveToNext()) {
            TipOBJ tip = new TipOBJ();
            tip.setId(cursor.getInt(TIP_ID_COL));
            tip.setDateMillis(cursor.getInt(BILL_DATE_COL));
            tip.setBillAmount(cursor.getFloat(BILL_AMOUNT_COL));
            tip.setTipPercent(TIP_PERCENT_COL);

            tips.add(tip);
        }

        if (cursor != null)
            cursor.close();
        closeDB();

        return tips;
    }

    public long insertTip(TipOBJ tip) {
        ContentValues cv = new ContentValues();
        cv.put(TIP_PERCENT, tip.getTipPercent());
        cv.put(BILL_DATE, tip.getTipPercent());
        cv.put(BILL_AMOUNT, tip.getBillAmount());

        this.openWriteableDB();
        long rowID = db.insert(TIP_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateTip(TipOBJ tip) {
        ContentValues cv = new ContentValues();
        cv.put(TIP_PERCENT, tip.getTipPercent());
        cv.put(BILL_DATE, tip.getTipPercent());
        cv.put(BILL_AMOUNT, tip.getBillAmount());

        String where = TIP_ID + "= ?";
        String[] whereArgs = { String.valueOf(tip.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(TIP_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteTip(long id) {
        String where = TIP_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(TIP_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public float getAverageTip()
    {
        ArrayList<TipOBJ> tips = new ArrayList<TipOBJ>();

        //open the database
        openReadableDB();

        //obtain the cursor
        Cursor cursor = db.query(TIP_TABLE,
                null, null, null, null, null, null);

        //iterate through the database contents
        while (cursor.moveToNext()) {
            TipOBJ tip = new TipOBJ();
            tip.setId(cursor.getInt(TIP_ID_COL));
            tip.setDateMillis(cursor.getInt(BILL_DATE_COL));
            tip.setBillAmount(cursor.getFloat(BILL_AMOUNT_COL));
            tip.setTipPercent(cursor.getFloat(TIP_PERCENT_COL));

            tips.add(tip);
        }

        if (cursor != null)
            cursor.close();
        closeDB();

        float average = 0, count = 0;

        for(TipOBJ tip: tips)
        {
            average += tip.getTipPercent();
            count++;
        }

        return average/count;
    }
}