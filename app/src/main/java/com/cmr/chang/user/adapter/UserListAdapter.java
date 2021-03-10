package com.cmr.chang.user.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cmr.chang.user.R;
import com.cmr.chang.user.entity.UserEntity;
import com.cmr.chang.user.util.BaseAdapter;
import com.cmr.chang.user.util.ViewHolder;

import java.util.List;

/**
 * 用户列表信息adapter
 */
public class UserListAdapter extends BaseAdapter<UserEntity> {
    private String TAG = UserListAdapter.class.getSimpleName();

    private TextView tv_name;
    private TextView tv_phone;
    private CheckBox cb_choice;

    public UserListAdapter(Activity activity, List<UserEntity> list) {
        super(activity, list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_user;
    }

    @Override
    public void initView(int position, View convertView) {
        //用户名
        tv_name = ViewHolder.get(convertView,R.id.tv_name);
        //电话号码
        tv_phone = ViewHolder.get(convertView,R.id.tv_phone);
        //选择
        cb_choice = ViewHolder.get(convertView,R.id.cb_choice);
    }

    @Override
    public void setListener(final int position, View convertView) {
        cb_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //设置check状态
                list.get(position).setCheck(isChecked);
            }
        });
    }

    @Override
    public void initData(int position, View convertView) {
        tv_name.setText(list.get(position).getUserName());
        tv_phone.setText(list.get(position).getPhoneNumber());
        cb_choice.setTag(list.get(position).getId());
        cb_choice.setChecked(list.get(position).isCheck());
    }
}
