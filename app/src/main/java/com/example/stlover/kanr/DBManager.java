package com.example.stlover.kanr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Created by StLover on 2016/4/27.
 */
public class DBManager {
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "database/KanR.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.example.stlover.kanr";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;

    private SQLiteDatabase database;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void openDatabase() {
        System.out.println(DB_PATH + "/" + DB_NAME);
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            return db;
    }

    public void closeDatabase() {
        this.database.close();
    }
}
