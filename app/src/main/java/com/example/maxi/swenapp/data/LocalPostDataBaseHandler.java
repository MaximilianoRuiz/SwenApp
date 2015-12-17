package com.example.maxi.swenapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.mock.MockCursor;
import android.util.Log;

import com.example.maxi.swenapp.VOs.LocalPostComment;
import com.example.maxi.swenapp.VOs.LocalPostLiked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalPostDataBaseHandler {

    public static final String ID= "id";
    public static final String POST_ID= "post_id";
    public static final String COMMENT= "comment";
    public static final String LIKED= "liked";

    public static final String DATA_BASE_NAME= "localPostDataBase";

    public static final String TABLE_LOCAL_POST_LIKED = "localPostLiked";
    public static final String TABLE_LOCAL_POST_COMMENT = "localPostComment";

    public static final int DATABASE_VERSION= 1;

    public static final String DATABASE_CREATE_LOCAL_LIKED = "create table localPostLiked (id integer primary key autoincrement, post_id text, comment text);";
    public static final String DATABASE_CREATE_LOCAL_COMMENT = "create table localPostComment (id integer primary key autoincrement, post_id text, liked boolean);";

    DataBaseHelper baseHelper;
    Context cxt;
    SQLiteDatabase db;

    public LocalPostDataBaseHandler(Context cxt) {
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
                db.execSQL(DATABASE_CREATE_LOCAL_LIKED);
                db.execSQL(DATABASE_CREATE_LOCAL_COMMENT);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST "+ TABLE_LOCAL_POST_LIKED);
            db.execSQL("DROP TABLE IF EXIST "+ TABLE_LOCAL_POST_COMMENT);
            onCreate(db);
        }
    }

    public LocalPostDataBaseHandler open(){
        this.db = baseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        baseHelper.close();
    }

    public long insertComment(String postId, String comment) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, postId);
        contentValues.put(COMMENT, comment);

        try {
            return db.insert(TABLE_LOCAL_POST_COMMENT, null, contentValues);
        } catch (Exception e){
            return 0;
        }
    }

    public Cursor returnComments() {
        try {
            return db.query(TABLE_LOCAL_POST_COMMENT, new String[]{ID, POST_ID, COMMENT}, null, null, null, null, null);
        }catch (Exception e){
            return new MockCursor();
        }
    }

    public int upDateComment(int id, String postId, String comment){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(POST_ID, postId);
        contentValues.put(COMMENT, comment);
        String[] whereArgs = {Integer.toString(id)};
        String where;

        where = "localPostComment.id LIKE ?";
        return db.update(TABLE_LOCAL_POST_COMMENT, contentValues, where, whereArgs);
    }

    public Map<String,List<LocalPostComment>> returnValueComments() {
        Cursor c = returnComments();
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

    public long insertLiked(String postId, boolean liked) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, postId);
        contentValues.put(LIKED, liked);

        try {
            return db.insert(TABLE_LOCAL_POST_LIKED, null, contentValues);
        } catch (Exception e){
            return 0;
        }
    }

    public Cursor returnLikeds() {
        try {
            return db.query(TABLE_LOCAL_POST_LIKED, new String[]{ID, POST_ID, LIKED}, null, null, null, null, null);
        }catch (Exception e){
            Log.d("POTTER",e.getMessage());
            return null;
        }
    }

    public int upDateLiked(int id, String postId, boolean liked){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(POST_ID, postId);
        contentValues.put(LIKED, liked);
        String[] whereArgs = {Integer.toString(id)};
        String where;

        where = "localPostLiked.id LIKE ?";
        return db.update(TABLE_LOCAL_POST_LIKED, contentValues, where, whereArgs);
    }

    public Map<String,List<LocalPostLiked>> returnValueLikeds() {
        Cursor c = returnLikeds();
        Map<String,List<LocalPostLiked>> fanPageVOMap = new HashMap<>();

        if (c.moveToFirst()){
            do{
                if(!fanPageVOMap.containsKey(c.getString(1))){
                    LocalPostLiked localPostLiked = new LocalPostLiked();
                    List<LocalPostLiked> localPostLikeds = new ArrayList<>();

                    localPostLiked.setId(c.getInt(0));
                    localPostLiked.setPostID(c.getString(1));
                    localPostLiked.setLiked("0".equals(c.getString(2)) ? false : true);

                    localPostLikeds.add(localPostLiked);

                    fanPageVOMap.put(localPostLiked.getPostID(), localPostLikeds);
                } else {
                    List<LocalPostLiked> comments = fanPageVOMap.get(c.getString(1));
                    LocalPostLiked localPostLiked = new LocalPostLiked();
                    localPostLiked.setId(c.getInt(0));
                    localPostLiked.setPostID(c.getString(1));
                    localPostLiked.setLiked("0".equals(c.getString(2)) ? false : true);
                    comments.add(localPostLiked);
                }

            }while(c.moveToNext());
        }
        return fanPageVOMap;
    }

}
