package com.example.maxi.swenapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maxi.swenapp.VOs.LocalPostComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseCommentsHandler {

    public static final String ID= "id";
    public static final String POST_ID= "post";
    public static final String COMMENT = "comment";

    public static final String DATA_BASE_NAME= "CommentsDataBase";

    public static final String TABLE = "comentsTable";

    public static final int DATABASE_VERSION= 1;

    public static final String DATABASE_CREATE_TABLE = "create table comentsTable (id integer primary key autoincrement, post text, comment text);";

    DataBaseHelper baseHelper;
    Context cxt;
    SQLiteDatabase db;

    public DataBaseCommentsHandler(Context cxt) {
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

    public DataBaseCommentsHandler open(){
        this.db = baseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        baseHelper.close();
    }

    public long insertData(String postID, String comment) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, postID);
        contentValues.put(COMMENT, comment);

        try {
            return db.insert(TABLE, null, contentValues);
        } catch (Exception e){
            return 0;
        }
    }

    public Cursor returnData() {
        try {
            return db.query(TABLE, new String[]{ID, POST_ID, COMMENT}, null, null, null, null, null);
        }catch (Exception e){
            Log.d("POTTER",e.getMessage());
            return null;
        }
    }

    public int upDate(int id, String postId, String comment){
        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, postId);
        contentValues.put(COMMENT, comment);
        contentValues.put(ID, id);
        String[] whereArgs = {Integer.toString(id)};
        String where;

        where = "comentsTable.id LIKE ?";
        return db.update(TABLE, contentValues, where, whereArgs);
    }

    public Map<String,List<LocalPostComment>> returnValue() {
        Cursor c = returnData();
        Map<String,List<LocalPostComment>> fanPageVOMap = new HashMap<>();

        if (c.moveToFirst()){
            do{
                if(!fanPageVOMap.containsKey(c.getString(1))){
                    LocalPostComment localPostComment = new LocalPostComment();
                    List<LocalPostComment> localPostComments = new ArrayList<>();

                    localPostComment.setId(c.getInt(0));
                    localPostComment.setPostID(c.getString(1));
                    localPostComment.setComment(c.getString(2));

                    localPostComments.add(localPostComment);

                    fanPageVOMap.put(localPostComment.getPostID(), localPostComments);
                } else {
                    List<LocalPostComment> comments = fanPageVOMap.get(c.getString(1));
                    LocalPostComment localPostComment = new LocalPostComment();
                    localPostComment.setId(c.getInt(0));
                    localPostComment.setPostID(c.getString(1));
                    localPostComment.setComment(c.getString(2));
                    comments.add(localPostComment);
                }

            }while(c.moveToNext());
        }
        return fanPageVOMap;
    }

}
