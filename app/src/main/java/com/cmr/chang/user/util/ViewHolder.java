package com.cmr.chang.user.util;


import android.util.SparseArray;
import android.view.View;

/**
 * listview 加载 adater 时
 * 为了减少view每次创建的内存消耗，来使用viewholder类。
 */
public class ViewHolder {
	// I added a generic return type to reduce the casting noise in client code
	//用于 adapter 重复执行 viewholder 
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}
