package com.gheng.exhibit.utils;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.checkin.RegisterNoteActivity;
import com.gheng.exhibit.view.noused.SignUpActivity;

public class UIUtils {
	/**
	 * 回到主页
	 * 
	 * @param activity
	 */
//	public static void goHome() {
//		// Intent i = new Intent(activity, MainActivity.class);
//		// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		// activity.startActivity(i);
//		MyApplication.getInstance().goHome();
//	}

	/**
	 * 禁止弹出软键盘,但是仍然有光标存在
	 * 
	 * @param activity
	 * @param editText
	 */
	public static void hiddenInputMethod(Activity activity, EditText editText) {
		if (android.os.Build.VERSION.SDK_INT <= 10) {// 4.0以下 danielinbiti
			editText.setInputType(InputType.TYPE_NULL);
		} else {
			activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setSoftInputShownOnFocus;
				setSoftInputShownOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
				setSoftInputShownOnFocus.setAccessible(true);
				setSoftInputShownOnFocus.invoke(editText, false);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Class<EditText> cls = EditText.class;
					Method setShowSoftInputOnFocus;
					setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
					setShowSoftInputOnFocus.setAccessible(true);
					setShowSoftInputOnFocus.invoke(editText, false);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
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
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display.getMetrics(metric);
		return new int[] { metric.widthPixels, metric.heightPixels };
	}

	public static void cleartTop(Activity start, Class<?> toActivity) {
		Intent i = new Intent(start, toActivity);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		start.startActivity(i);
	}
//
//	public static void finishToBlockActivity() {
//		MyApplication.getInstance().finishToBlockActivity();
//	}

//	public static void setBlockActivity(Activity a) {
//		MyApplication.getInstance().setBlockActivity(a);
//	}

//	/**
//	 * 到注册页面
//	 */
//	public static void startToRegister(Activity a) {
//		setBlockActivity(a);
//		((BaseActivity)a).toastLong(((BaseActivity)a).getLanguageString("请先注册"));
//		((BaseActivity) a).startTo(RegisterNoteActivity.class);
//	}

	public static void setListViewHeader(BaseActivity obj) {
//		View headerView = obj.getLayoutInflater().inflate(R.layout.view_bar_line, null);
//		View footerView = obj.getLayoutInflater().inflate(R.layout.view_bar_line, null);
//		Field[] fields = obj.getClass().getDeclaredFields();
//		for (Field field : fields) {
//			if (ListView.class.isAssignableFrom(field.getType())) {
//				try {
//					field.setAccessible(true);
//					Object fobj = field.get(obj);
//					Method m = getMethod(fobj.getClass(),"addHeaderView", View.class,Object.class,Boolean.class);
//					if (m != null) {
//						m.setAccessible(true);
//						m.invoke(fobj, headerView, null, true);
//					}
//					m = getMethod(fobj.getClass(),"addFooterView", View.class,Object.class,Boolean.class);
//					if (m != null) {
//						m.setAccessible(true);
//						m.invoke(fobj, footerView, null, true);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

	private static Method getMethod(Class<?> clazz,String method,Class<?>... paramTypes){
		Class<?> cls = clazz;
		Method m = null;
		while (m == null && cls != Object.class) {
			try {
				m =  cls.getDeclaredMethod(method, View.class,Object.class,boolean.class);
			} catch (NoSuchMethodException e) {
				m = null;
			}
			cls = cls.getSuperclass();
		}
		return m;
	}
	
	public static void goSignUpForResult(BaseActivity a,long id){
		Intent i = new Intent(a, SignUpActivity.class);
		i.putExtra("id", id);
		a.startActivityForResult(i, 300);
	}
	
	public static void goSignUpForResult(BaseActivity a,long id,Bundle bd){
		Intent i = new Intent(a, SignUpActivity.class);
		i.putExtra("id", id);
		i.putExtras(bd);
		a.startActivityForResult(i, 300);
	}

	/**
	 * 获取状态栏高度
	 */
    public static int getStatusHeight(Activity activity){  
        int statusHeight = 0;  
        Rect localRect = new Rect();  
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);  
        statusHeight = localRect.top;  
        if (0 == statusHeight){  
            Class<?> localClass;  
            try {  
                localClass = Class.forName("com.android.internal.R$dimen");  
                Object localObject = localClass.newInstance();  
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());  
                statusHeight = activity.getResources().getDimensionPixelSize(i5);  
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
