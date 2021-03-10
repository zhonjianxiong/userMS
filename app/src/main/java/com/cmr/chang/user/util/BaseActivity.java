package com.cmr.chang.user.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.cmr.chang.user.AppData;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 模板：模块化activity组件
 */
abstract public class BaseActivity extends AppCompatActivity {
	private static final String TAG = BaseActivity.class.getSimpleName();

	public Activity act;
	public AppData application;
	public Context context;

	public String spinner;
	public static int dialog_pos;
	InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		//禁止休眠
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
//				| WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if(getSupportActionBar()!=null){
			getSupportActionBar().hide();
		}

		int layoutid = getLayoutId();
		if (layoutid != 0) {
			setContentView(layoutid);
		}

		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		act = this;
		context = act;

		application = (AppData) getApplication();

		initView();
		setListener();
		initData();
	}

	/**
	 * 获取界面布局id
	 *
	 * @return 界面布局id
	 */
	abstract protected int getLayoutId();

	/**
	 * 初始化界面元
	 */
	abstract protected void initView();

	/**
	 * 监听事件
	 * */
	abstract protected void setListener();

	/**
	 * 初始化数据
	 * */
	abstract protected void initData();



}
