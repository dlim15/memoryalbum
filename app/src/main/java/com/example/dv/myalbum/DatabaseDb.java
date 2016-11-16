package com.example.dv.myalbum;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by DV on 11/6/2016.
 */

public class DatabaseDb{
    private SQLStatements st;
    private Cursor result;
    private SQLiteDatabase database;
    public DatabaseDb(SQLiteDatabase db){
        setDb(db);
    }
    public void setDb(SQLiteDatabase db){
        database = db;
    }

    private void insertExec(String tableName, String columns, String values){
        if(database != null){
            database.execSQL("INSERT INTO " + tableName + " (" + columns + ") VALUES (" +
                    values + ");");
        }
    }
    private String qu(String val){
        return "'"+val+"'";
    }
    public void insertAlbum(String name, long firstDay){
        name = name.contains("'") ? DatabaseUtils.sqlEscapeString(name) : qu(name);
        insertExec(st.T_ALBUM, st.C_ALBUM_NAME + "," + st.C_ALBUM_FIRST_DAY, name+","+firstDay);
    }
    public void insertFile(int id, int day, String location){
        insertExec(st.T_FILE, st.C_FILE_ID + "," + st.C_FILE_DAY + "," + st.C_FILE_LOCATION,id + "," + day+",'"+location+"'");
    }
    public int getSelectRowNum(String selectCol, String tableName, String where){
        return sqlSelect(selectCol,tableName,where).getCount();
    }
    public Cursor sqlSelect(String selectCol, String tableName, String where){
        return database.rawQuery("SELECT " + selectCol +
                " FROM " + tableName +
                " WHERE " + where +";", null);
    }
    public int selectAlbumNo(){
        if(database != null){
            result = sqlSelect("MAX("+ st.C_ALBUM_ID + ")", st.T_ALBUM, "1");
            result.moveToNext();
            return result.getInt(0);
        }
        return -1;
    }
    public Cursor getAlbumInfo(){
        if(database!=null)
            return sqlSelect(st.C_ALBUM_ID+","+st.C_ALBUM_NAME,st.T_ALBUM,"1");
        return null;
    }


}
