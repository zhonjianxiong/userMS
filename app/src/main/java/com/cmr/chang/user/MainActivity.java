package com.cmr.chang.user;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.cmr.chang.user.adapter.UserListAdapter;
import com.cmr.chang.user.entity.UserEntity;
import com.cmr.chang.user.util.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends BaseActivity{
    private String TAG = MainActivity.class.getSimpleName();

    //手机权限
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 1;
    protected String[] needPermissions = {
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
    };

    private ListView lv_user;
    private UserListAdapter userListAdapter;//用户列表adapter
    private List<UserEntity> userList;  //用户列表data

    private Button b_user_add;
    private Button b_user_delete;
    private Button b_group_sending;
    private Button b_user_clear;

    private SQLiteDatabase dbWrite ;
    private List<String> isCheckList;  //选择删除列表

    private  Toast toast;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_userlist;
    }

    @Override
    protected void initView() {
        lv_user = findViewById(R.id.lv_user);
        b_user_add = findViewById(R.id.b_user_add);
        b_user_delete = findViewById(R.id.b_user_delete);
        b_group_sending = findViewById(R.id.b_group_sending);
        b_user_clear = findViewById(R.id.b_user_clear);

    }

    @Override
    protected void setListener() {
        //添加用户
        b_user_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserAddActivity.class);
                startActivityForResult(intent, application.request_add_user);
            }
        });
        //删除用户
        b_user_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckList = new ArrayList<String>();
                //获取删除列表
                for(UserEntity user : userList){
                    //Log.e(TAG,""+user.isCheck());
                    if(user.isCheck()){
                        isCheckList.add(""+user.getId());
                    }
                }
                String arrayStr =
                        isCheckList.toString().substring(1,isCheckList.toString().length()-1);
                deleteUser(arrayStr);
            }
        });
        //群发
        b_group_sending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGroupSendingDialog();
            }
        });
        //清除已选择的信息
        b_user_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllUser();
            }
        });
    }

    /**
     * 获取数据连接
     */
    @Override
    protected void initData() {
        //请求手机权限
        requestPermission();
        //读取数据库请求接口
        dbWrite = application.dbHelper.getWritableDatabase();
        //插入默认用户信息
        insertUsers();
        //查询用户数据
        selectUser();
        userListAdapter = new UserListAdapter(this, userList);
        lv_user.setAdapter(userListAdapter);
    }

    /**
     * 查询用户
     */
    public void selectUser(){
        SQLiteDatabase dbRead = application.dbHelper.getReadableDatabase();
        userList =  application.dbHelper.searchAllFile(dbRead);
    }

    /**
     * 删除指定 的 user用户
     * @param userId
     */
    public void deleteUser(String userId){
        if(userId.length()>0){
            application.dbHelper.deleteTableRow(dbWrite,userId);
            toast = Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT);
            toast.setText("删除成功");
            toast.show();
        }else{
            toast = Toast.makeText(MainActivity.this,"请选择删除用户",Toast.LENGTH_SHORT);
            toast.setText("请选择删除用户");
            toast.show();
        }
        updateListView();
    }

    /**
     * 清除所有用户
     */
    public void clearAllUser(){
        for(UserEntity user : userListAdapter.list){
            user.setCheck(false);
        }
        userListAdapter.notifyDataSetChanged();
    }


    /**
     * 群发短信功能
     */
    public void sendGroupSendingDialog(){
        final EditText et_sms = new EditText(this);
        et_sms.setHint("请输入短信内容");
        new AlertDialog.Builder(this)
                .setTitle("输入短信")
                .setView(et_sms)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sms = et_sms.getText().toString().trim();
                        if (TextUtils.isEmpty(sms)) {
                            Toast.makeText(MainActivity.this, "短信内容不能为空", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                groupSending(et_sms.getText().toString().trim());
                            } catch (SecurityException e){
                                Toast.makeText(MainActivity.this, "发送短信权限被禁止", Toast.LENGTH_LONG).show();
                            }
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
    /**
     *  短信群发
     * @param content   发送内容
     */
    public void groupSending(String content) throws SecurityException{
        SmsManager smsManager=SmsManager.getDefault();
        for (UserEntity user : userList){
            // 创建一个PendingIntent对象
            PendingIntent pi = PendingIntent.getActivity(
                    MainActivity.this, 0, new Intent(), 0);
            // 发送短信
            smsManager.sendTextMessage(user.getPhoneNumber(),
                    null, content, pi, null);
        }
        // 提示短信群发完成
        toast = Toast.makeText(MainActivity.this,"短信群发成功", Toast.LENGTH_SHORT);
        toast.setText("短信群发成功");
        toast.show();
    }

    //更新listview 用户信息
    public void updateListView(){
        selectUser();
        userListAdapter.list = userList;
        userListAdapter.notifyDataSetChanged();
    }

    /**
     * 系统回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == application.result_add_user) {
            toast = Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT);
            toast.setText("添加成功");
            toast.show();
            updateListView();
        }
    }


    /**
     * 执行 权限  ---------------------------------
     */
    private void requestPermission() {
        /**
         * android 6.0权限
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //读取短信
            int read_sms = checkSelfPermission(Manifest.permission.READ_SMS);
            //发送短信
            int send_sms = checkSelfPermission(Manifest.permission.SEND_SMS);
            //接收短信
            int receive_sms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);

            //保存 权限列表
            List<String> permissions = new ArrayList<String>();

            if (read_sms != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (send_sms != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.SEND_SMS);
            }
            if (receive_sms != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]),
                        100);
            }
        } else {// 小于6.0
            // AbSharedUtil.putString(this,"storage", "true");
        }
    }

    /**
     * 回调函数
     * 执行权限方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA_REQUEST_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.e("TTT", "Permissions --> " + "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.e("TTT", "Permissions --> " + "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    /**
     * 插入用户信息
     */
    public void insertUsers(){
        AppData app = (AppData) getApplication();
        SQLiteDatabase db = app.dbHelper.getWritableDatabase();
        for (int i = 0; i < 12; i++) {
            app.dbHelper.insertFileMessage(db,"张" + i, i + "1112122211");
        }
    }


}
