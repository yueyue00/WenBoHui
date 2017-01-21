package com.gheng.exhibit.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdot.wenbo.huiyi.R;

/**
 * 进度条工具
 * 
 * @author Administrator
 * 
 */
public class ProgressTools {

	private static Dialog progressDialog;

	private ProgressTools() {
	}

	public static void showDialog(final Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		if (progressDialog != null && progressDialog.isShowing())
			return;
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局

		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loding_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		if(message == null || StringTools.isBlank(message.toString())) {
			message = "Loading...";
		}
		tipTextView.setText(message);// 设置加载信息
		progressDialog = new Dialog(context, R.style.dialog);// 创建自定义样式dialog
		progressDialog.setCancelable(false);// 不可以用“返回键”取消
		progressDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		progressDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					((Activity) context).finish();
				}
				return false;
			}
		});
		progressDialog.show();
	}

	public static void showDialog(Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable) {
		showDialog(context, title, message, indeterminate, cancelable, null);
	}

	public static void showDialog(Context context, CharSequence title,
			CharSequence message) {
		showDialog(context, title, message, false, false);
	}

	public static void showDialog(Context context, CharSequence message) {
		showDialog(context, "", message);
	}

	public static void showDialog(Context context) {
		showDialog(context, "", "");
	}

	public static void hide() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}
}
