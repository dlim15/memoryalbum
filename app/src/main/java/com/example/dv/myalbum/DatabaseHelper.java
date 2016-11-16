package com.example.dv.myalbum;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DV on 10/23/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;
    private Context cont;
    private SQLStatements st;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        cont = context;
        st = new SQLStatements();
    }

    public static synchronized DatabaseHelper getsInstance(Context context){
        if(sInstance == null){
            sInstance = new DatabaseHelper(context,"albumdb", null, 2);
        }
        return sInstance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    public void close(){

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + st.T_ALBUM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + st.T_FILE);
        createTable(sqLiteDatabase);
    }
    private String createAlbumStatement(){
        return "CREATE TABLE IF not EXISTS " + st.T_ALBUM + " (" +
                st.C_ALBUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                st.C_ALBUM_NAME + " TEXT NOT NULL," +
                st.C_ALBUM_FIRST_DAY + " INTEGER);";
    }
    private String createFilesStatement(){
        return "CREATE TABLE IF NOT EXISTS " + st.T_FILE + " (" +
                st.C_FILE_ID + " INTEGER," +
                st.C_FILE_DAY + " INTEGER," +
                st.C_FILE_LOCATION + " TEXT KEY," +
                st.C_FILE_TIME_ADDED + " TIMESTAMP PRIMARY KEY DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY("+ st.C_FILE_ID + ") REFERENCES "+ st.T_ALBUM + "("+ st.C_ALBUM_ID + "))";
    }
    private void createTable(SQLiteDatabase db){
        if(db != null){

            db.execSQL(createAlbumStatement());
            db.execSQL(createFilesStatement());
        }
    }

}
