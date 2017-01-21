package com.gheng.exhibit.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Dip工具,包含像素和dip之间的转换
 * 
 * @author Administrator
 * 
 */
public class DipUtils {

	private DipUtils() {
	}

	/** * 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** * 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/** 获取屏幕的宽高(像素) **/
	public static int[] getWindows(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display.getMetrics(metric);
		return new int[] { metric.widthPixels, metric.heightPixels };
	}
}
