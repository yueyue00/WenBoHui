package com.gheng.exhibit.view.noused;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.smartdot.wenbo.huiyi.R;
/**
 * 公用的弹出窗体， 接受View的
 * @author zhaofangfang
 *
 */
public class CommonDialog {
	
	private Context context;
	
	private View v;
	
	private Dialog dialog;
	
	public CommonDialog(Context c, View v) {
		this.context = c;
		this.v = v;
		initDialog();
	}
	/**
	 * 
	 */
	private void initDialog() {
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
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
	}

	/**
	 * 显示dialog
	 * @param context
	 * @param v
	 */
	public void showDialog() {
		if(dialog != null) {
			dialog.show();
		}
	}
	/**
	 * 关闭图标
	 */
	public void closeDialog() {
		if(dialog != null) {
			dialog.dismiss();
		}
	}

}
