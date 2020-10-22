package com.example.newmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDbHelper extends SQLiteOpenHelper {
    private static MemoDbHelper sInstance;


    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "Memo.db";

    private static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)",
                    MemoContract.MemoEntry.TABLE_NAME,
                    MemoContract.MemoEntry._ID,
                    MemoContract.MemoEntry.COLUMN_NAME_TITLE,
                    MemoContract.MemoEntry.COLUMN_NAME_LAT,
                    MemoContract.MemoEntry.COLUMN_NAME_LNG
            );


    public static MemoDbHelper getInstance(Context context){
        if (sInstance == null){
            sInstance = new MemoDbHelper(context);
        }
        return sInstance;
    }

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS" + MemoContract.MemoEntry.TABLE_NAME;

    public MemoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
    }
}
