package com.cmr.chang.user;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cmr.chang.user.util.BaseActivity;

/**
 * 添加用户信息
 */
public class UserAddActivity extends BaseActivity {

    private EditText et_name;
    private EditText et_phone;
    private Button b_submit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_add;
    }

    @Override
    protected void initView() {
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        b_submit = findViewById(R.id.b_submit);
    }

    @Override
    protected void setListener() {
        //添加按钮
        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
                //返回MainActivity用户列表页面
                setResult(application.result_add_user);
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 插入用户
     */
    public void insertUser(){
        AppData app = (AppData) getApplication();
        SQLiteDatabase db = app.dbHelper.getWritableDatabase();
        app.dbHelper.insertFileMessage(db,
                et_name.getText().toString(),
                et_phone.getText().toString());
    }
}
