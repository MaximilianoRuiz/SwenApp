package com.example.maxi.swenapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maxi.swenapp.VOs.LocalPostLiked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseLikeHandler {

    public static final String ID= "id";
    public static final String POST_ID= "post";
    public static final String CHECKED = "checked";

    public static final String DATA_BASE_NAME= "likesDataBase";

    public static final String TABLE = "likesTable";

    public static final int DATABASE_VERSION= 1;

    public static final String DATABASE_CREATE_TABLE = "create table likesTable (id integer primary key autoincrement, post text, checked boolean);";

    DataBaseHelper baseHelper;
    Context cxt;
    SQLiteDatabase db;

    public DataBaseLikeHandler(Context cxt) {
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

    public DataBaseLikeHandler open(){
        this.db = baseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        baseHelper.close();
    }

    public long insertData(String postID, boolean checked) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, postID);
        contentValues.put(CHECKED, checked);

        try {
            return db.insert(TABLE, null, contentValues);
        } catch (Exception e){
            return 0;
        }
    }

    public Cursor returnData() {
        try {
            return db.query(TABLE, new String[]{ID, POST_ID, CHECKED}, null, null, null, null, null);
        }catch (Exception e){
            Log.d("POTTER",e.getMessage());
            return null;
        }
    }

    public int upDate(int id, String postId, boolean checked){
        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, postId);
        contentValues.put(CHECKED, checked);
        contentValues.put(ID, id);
        String[] whereArgs = {Integer.toString(id)};
        String where;

        where = "likesTable.id LIKE ?";
        return db.update(TABLE, contentValues, where, whereArgs);
    }

    public Map<String,LocalPostLiked> returnValue() {
        Cursor c = returnData();
        List<LocalPostLiked> fanPageVOs = new ArrayList<>();
        Map<String,LocalPostLiked> stringListMap = new HashMap<>();

        if (c.moveToFirst()){
            do{
                LocalPostLiked fanPageVO = new LocalPostLiked();
                fanPageVO.setId(c.getInt(0));
                fanPageVO.setPostID(c.getString(1));
                fanPageVO.setLiked("0".equals(c.getString(2)) ? false : true);

                stringListMap.put(fanPageVO.getPostID(), fanPageVO);

                fanPageVOs.add(fanPageVO);
            }while(c.moveToNext());
        }
        return stringListMap;
    }

}
