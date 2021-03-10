package com.cmr.chang.user;

import android.app.Application;

import com.cmr.chang.user.database.DBHelper;

public class AppData extends Application {
    //用户请求
    public static final int request_add_user = 0x000001;
    public static final int result_add_user = 0x000002;

    public DBHelper dbHelper;

    public AppData(){
        dbHelper = new DBHelper(this);
    }
}
