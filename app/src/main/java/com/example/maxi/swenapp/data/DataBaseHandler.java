package com.example.maxi.swenapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maxi.swenapp.VOs.FanPageVO;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler {

    public static final String ID= "id";
    public static final String NOMBRE= "nombre";
    public static final String CATEGORIA= "categoria";
    public static final String PICTURE= "picture";
    public static final String CHECKED = "checked";
    public static final String URL = "url";

    public static final String DATA_BASE_NAME= "myDataBase";

    public static final String TABLE = "fanPages";

    public static final int DATABASE_VERSION= 1;

    public static final String DATABASE_CREATE_TABLE = "create table fanPages (id integer primary key autoincrement, nombre text, categoria text, picture text, checked boolean, url text);";

    DataBaseHelper baseHelper;
    Context cxt;
    SQLiteDatabase db;

    public DataBaseHandler(Context cxt) {
        this.cxt = cxt;
        baseHelper = new DataBaseHelper(cxt);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE_TABLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST "+ TABLE);
            onCreate(db);
        }
    }

    public DataBaseHandler open(){
        this.db = baseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        baseHelper.close();
    }

    public long insertData(String nombre, String categoria, String picture, boolean checked, String url) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOMBRE, nombre);
        contentValues.put(CATEGORIA, categoria);
        contentValues.put(PICTURE, picture);
        contentValues.put(CHECKED, checked);
        contentValues.put(URL, url);

        try {
            return db.insert(TABLE, null, contentValues);
        } catch (Exception e){
            return 0;
        }
    }

    public Cursor returnData() {
        try {
            return db.query(TABLE, new String[]{ID, NOMBRE, CATEGORIA, PICTURE, CHECKED, URL}, null, null, null, null, null);
        }catch (Exception e){
            Log.d("POTTER",e.getMessage());
            return null;
        }
    }

    public int upDate(int id, String nombre, String categoria, String picture, boolean checked, String url){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOMBRE, nombre);
        contentValues.put(CATEGORIA, categoria);
        contentValues.put(PICTURE, picture);
        contentValues.put(CHECKED, checked);
        contentValues.put(ID, id);
        contentValues.put(URL, url);
        String[] whereArgs = {Integer.toString(id)};
        String where;

        where = "fanPages.id LIKE ?";
        return db.update(TABLE, contentValues, where, whereArgs);
    }

    public List<FanPageVO> returnValue() {
        Cursor c = returnData();
        List<FanPageVO> fanPageVOs = new ArrayList<>();

        if (c.moveToFirst()){
            do{
                FanPageVO fanPageVO = new FanPageVO();
                fanPageVO.setId(c.getInt(0));
                fanPageVO.setName(c.getString(1));
                fanPageVO.setCategori(c.getString(2));
                fanPageVO.setPicture(c.getString(3));
                fanPageVO.setChecked("0".equals(c.getString(4)) ? false : true);
                fanPageVO.setUrl(c.getString(5));

                fanPageVOs.add(fanPageVO);
            }while(c.moveToNext());
        }
        return fanPageVOs;
    }

}
