package com.gheng.exhibit.widget;

import android.util.SparseArray;
import android.view.View;

//泛型的ViewHolder类
public class ViewHolderz {

	// I added a generic return type to reduce the casting noise in client code
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View convertView, int id) {
		/*
		 * 关于SparseArray的应用：可以去百度查阅资料学习。
		 * 对于HashMap<Integer,Object>的使用，Android官方建议是使用SparseArray<E>的。
		 * 参考：http://www.eoeandroid.com/thread-321547-1-1.html
		 * https://github.com/JoanZapata/base-adapter-helper
		 */
		if (null == convertView) {
			throw new NullPointerException("请检查getView中的convertView对象是否初始化");
		}
		SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			convertView.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

}
