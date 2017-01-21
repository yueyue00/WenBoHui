package com.gheng.exhibit.view.map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gheng.exhibit.view.BaseActivity;
import com.smartdot.wenbo.huiyi.R;

/**
 * 信息Dialog
 * 
 * @author zhaofangfang
 * 
 */
public class InfoDialog {

	private static Dialog dialog;

	public static void showDialog(Context context) {
		if (dialog != null && dialog.isShowing())
			return;
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_map, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		BaseActivity b = (BaseActivity)context;
		TextView tv1 = (TextView) v.findViewById(R.id.tv1);
		tv1.setText(b.getLanguageString("可按展商、展品、展区查询本馆数据"));
		TextView tv2 = (TextView) v.findViewById(R.id.tv2);
		tv2.setText(b.getLanguageString("可设置起点、终点进行路径规划"));
		TextView tv3 = (TextView) v.findViewById(R.id.tv3);
		tv3.setText(b.getLanguageString("可重置地图"));
		TextView tv4 = (TextView) v.findViewById(R.id.tv4);
		tv4.setText(b.getLanguageString("当前位置跟随"));
		
		
		dialog = new Dialog(context, R.style.dialog);// 创建自定义样式dialog
		dialog.setCancelable(false);// 不可以用“返回键”取消
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
				}
				return false;
			}
		});
		dialog.show();
	}

}
