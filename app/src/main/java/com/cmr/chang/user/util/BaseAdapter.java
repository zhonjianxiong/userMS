package com.cmr.chang.user.util;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * BaseAdapter 模式
 * @param <T>
 */
public abstract class BaseAdapter<T> extends  android.widget.BaseAdapter{

	public Activity activity;
	public List<T> list ;
	public View convertView ;
	
	public BaseAdapter(Activity activity , List<T> list ){
		this.activity = activity;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(activity.getApplicationContext()).inflate(getLayoutId(), parent, false);
		}
		
		initView(position,convertView);
		setListener(position,convertView);
		initData(position,convertView);
		
		//使用 ViewHoder基类
		
	    //ImageView bananaView = ViewHolder.get(convertView, R.id.banana);  
	    //TextView phoneView = ViewHolder.get(convertView, R.id.phone);  
	   
		//BananaPhone bananaPhone = getItem(position);  
		//phoneView.setText(bananaPhone.getPhone());  
		//bananaView.setImageResource(bananaPhone.getBanana());  
	   
	    return convertView;  
	    
	}

	abstract public int getLayoutId();
	
	abstract public void initView(int position, View convertView);
	
	abstract public void setListener(int position, View convertView);
	
	abstract public void initData(int position, View convertView);
}
