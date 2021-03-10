package com.cmr.chang.user.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cmr.chang.user.entity.UserEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * DBHelper
 */
public class DBHelper extends SQLiteOpenHelper {

    // 数据库版本号
    private static final int DATABASE_VERSION = 1;
    // 数据库名
    private static final String DATABASE_NAME = "user.db";
    public static final String CARMERA_FILE = "user";
    // 数据表名: carmera_file
    public static final String TABLE_NAME = CARMERA_FILE;

    public DBHelper(Context context, String name, CursorFactory factory, int version,
                          DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 设置数据表名称
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("CREATE TABLE  if not exists " + TABLE_NAME
                + "  (");
        sBuffer.append("id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("name VARCHAR(20),");
        sBuffer.append("phone VARCHAR(20)" +
                ")");
        db.execSQL(sBuffer.toString());
    }

    /**
     * 更新数据库
     * @param db        数据库
     * @param oldVersion    老版本
     * @param newVersion    新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 搜索 所有用户信息
     * @param db 数据库
     * @return  所有用户信息
     */
    public List<UserEntity> searchAllFile(SQLiteDatabase db ) {
        Cursor cursor = db.rawQuery("select id,name,phone from "+ TABLE_NAME + " where 1=1 ", new String[]{});

        List<UserEntity> list = new LinkedList<UserEntity>();
        UserEntity entity;
        while (cursor.moveToNext()) {
            entity = new UserEntity();
            entity.setId(cursor.getInt(cursor.getColumnIndex("id")) );
            entity.setUserName(cursor.getString(cursor.getColumnIndex("name")) );
            entity.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone")));
            list.add(entity);
        }
        return list;
    }

    /**
     * 搜索
     * @param db 数据库
     * @return   保存的文件列表
     */
    public List<UserEntity> searchFile(SQLiteDatabase db ,String filePath) {
        Cursor cursor = db.rawQuery("select id,name,phone from "+ TABLE_NAME
                + " where phone=? ", new String[]{filePath});

        List<UserEntity> list = new LinkedList<UserEntity>();
        UserEntity bean;
        while (cursor.moveToNext()) {
            bean = new UserEntity();
            bean.setId(cursor.getInt(cursor.getColumnIndex("id")) );
            bean.setUserName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone")));
            list.add(bean);
        }
        return list;
    }

    /**
     * 插入用户名 电话
     * @param db        数据库
     * @param name      用户名
     * @param phone     电话号码
     */
    public void insertFileMessage(SQLiteDatabase db, String name , String phone) {
        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("phone", phone);

        long returnid = db.insert(TABLE_NAME, null, value);
        Log.e("插入用户返回", "" + returnid);

    }

    /**
     * 清除表的所有数据
     */
    public void deletAll(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    /**
     * 清除表中指定多条 内容
     * @param ids     行id
     */
    public void deleteTableRow(SQLiteDatabase db , String ids) throws SQLException {
        db.execSQL("DELETE FROM " + TABLE_NAME +" where id IN ("+ids+") ");
    }


}
