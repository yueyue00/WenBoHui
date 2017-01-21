package com.gheng.exhibit.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.view.BaseActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * 设置View国际化
 * @author lileixing
 */
public class I18NUtils {

	private I18NUtils(){}
	
	public static void setTextView(TextView tv,String text,String hint){
		tv.setText(text);
		tv.setHint(hint);
	}
	
	public static void setTextView(TextView tv,String text){
		setTextView(tv, text,null);
	}
	
	public static void setTextView(TextView tv,int textCode,int hintCode){
		BaseActivity a = (BaseActivity)tv.getContext();
		setTextView(tv, a.getLanguageString(textCode),a.getLanguageString(hintCode));
	}
	
	public static void setTextView(TextView tv,int textCode){
		BaseActivity a = (BaseActivity)tv.getContext();
		setTextView(tv, a.getLanguageString(textCode),null);
	}
	
	public static void setPullView(PullToRefreshBase<? extends View> pullView,
			String headPullLabel,String footPullLabel,String refreshingLabel,String releaseLabel){
		
		pullView.getHeaderLayout().setPullLabel(headPullLabel);
		pullView.getHeaderLayout().setRefreshingLabel(refreshingLabel);
		pullView.getHeaderLayout().setReleaseLabel(releaseLabel);
		
		pullView.getFooterLayout().setPullLabel(footPullLabel);
		pullView.getFooterLayout().setRefreshingLabel(refreshingLabel);
		pullView.getFooterLayout().setReleaseLabel(releaseLabel);
		
//		pullView.getLoadingLayoutProxy().setPullLabel(headPullLabel);
		pullView.getLoadingLayoutProxy().setRefreshingLabel(refreshingLabel); 
//		pullView.getLoadingLayoutProxy().setReleaseLabel(refreshingLabel); 
	}
	
	public static void setPullView(PullToRefreshBase<? extends View> pullView,BaseActivity activity){
		I18NUtils.setPullView(pullView, activity.getLanguageString("下拉刷新…"), activity.getLanguageString("上拉加载更多…"), activity.getLanguageString("正在载入…"), activity.getLanguageString("放开加载更多..."));
	}
	
	/**
	 * 获取I18N的值
	 * @param obj
	 * 		获取值
	 * @param chFieldName
	 * 		中文字段名
	 * @param lang
	 * 		语言，指定获取语言的优先级
	 * @return
	 * 	
	 */
	public static Object getValue(Object obj,String zhFieldName){
		if(obj == null)
			return null;
		int lang = SharedData.getInt("i18n", Language.ZH);
		String enFieldName = "en" + StringTools.capitalize(zhFieldName);
		String enFieldName2 = "en" + zhFieldName;
		System.out.println(enFieldName+"========"+enFieldName2);
		//查询优先级
		List<String> list = new ArrayList<String>();
		if(lang == Language.ZH){
			list.add(zhFieldName);
			list.add(enFieldName);
			list.add(enFieldName2);
		}else{
			list.add(enFieldName);
			list.add(enFieldName2);
			list.add(zhFieldName);
		}
		Object value = null;
		for (String fieldName : list) {
			System.out.println(fieldName+"!!!!!!!!!!!!!!!!!!!");
			value = getV(obj, fieldName);
			if(value != null){
				if(value instanceof String){
					if(!"".equals(((String) value).trim())){
						break;
					}
				}else{
					break;
				}
			}
		}
		return value;
	}
	
	public static Object getV(Object obj,String fieldName){
		if(obj == null)
			return null;
		Field field = getField(obj.getClass(), fieldName);
		if(field == null)
			return null;
		field.setAccessible(true);
		try {
			return field.get(obj);
		} catch (Exception e) {
		} 
		return null;
	}
	
	private static Field getField(Class<?> clazz,String fieldName){
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
		} 
		return field;
	}
}
