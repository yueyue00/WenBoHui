package com.gheng.exhibit.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.gheng.exhibit.application.MyApplication;

/**
 * SharedPreferences工具类
 * 
 * @author Administrator
 * 
 */
public class SharedData {

	private SharedData() {
	}

	private static final String SP_NAME = "exhibit-this";
	private static SharedPreferences sharedPreferences = null;
	private static Context context;

	private static final String USERNAME = "uname";
	// public static final String REGISTER_USER = "register_user";
	// 姓名
	public static final String SURNAME = "firstname";
	public static final String NAME = "lastname";
	public static final String EMAIL = "email";
	public static final String POSITION = "position";
	public static final String WORKPLACE = "workPlace";

	public static final String REGISTER = "register";
	public static final String COMMIT = "commit";
	public static final String MOBILE = "mobile";
	
	private static final String EID = "eid";

	public static final String M_VERSION = "m_version";
	public static final String B_VERSION = "b_version";
	public static final String R_VERSION = "r_version";

	public static void init(Context context) {
		if (sharedPreferences == null) {
			SharedData.context = context;
			sharedPreferences = context.getSharedPreferences(SP_NAME,
					Context.MODE_PRIVATE);
		}
	}

	public static void reinit() {
		try {
			if (sharedPreferences == null) {
				if (context == null) {
					context = MyApplication.getInstance();
				}
				sharedPreferences = context.getSharedPreferences(SP_NAME,
						Context.MODE_PRIVATE);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException("SP gen error");
		}
		if (sharedPreferences == null) {
			throw new RuntimeException("SP cant be init");
		}
	}

	public static void saveUser(String uname) {
		commit(USERNAME, uname);
	}

	public static void clearUser() {
		clear(USERNAME);
	}

	public static String getUser() {
		return getString(USERNAME);
	}

	public static void commit(String name, boolean value) {
		reinit();
		if (sharedPreferences == null)
			return;
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(name, value);
		edit.apply();
	}

	public static void commit(String name, float value) {
		reinit();
		if (sharedPreferences == null)
			return;
		Editor edit = sharedPreferences.edit();
		edit.putFloat(name, value);
		edit.apply();
	}

	public static void commit(String name, int value) {
		reinit();
		if (sharedPreferences == null)
			return;
		Editor edit = sharedPreferences.edit();
		edit.putInt(name, value);
		edit.apply();
	}

	public static void commit(String name, long value) {
		reinit();
		if (sharedPreferences == null)
			return;
		Editor edit = sharedPreferences.edit();
		edit.putLong(name, value);
		edit.apply();
	}

	public static void commit(String name, String value) {
		reinit();
		if (sharedPreferences == null)
			return;
		Editor edit = sharedPreferences.edit();
		edit.putString(name, value);
		edit.apply();
	}

	public static void commit(Map<String, Object> map) {
		reinit();
		if (sharedPreferences == null || map == null)
			return;
		Editor edit = sharedPreferences.edit();
		Set<Entry<String, Object>> entrySet = map.entrySet();
		for (Entry<String, Object> en : entrySet) {
			String key = en.getKey();
			Object value = en.getValue();
			Class<?> cls = value.getClass();
			if (Integer.class.isAssignableFrom(cls)) {
				edit.putInt(key, (Integer) value);
			} else if (Boolean.class.isAssignableFrom(cls)) {
				edit.putBoolean(key, (Boolean) value);
			} else if (Float.class.isAssignableFrom(cls)) {
				edit.putFloat(key, (Float) value);
			} else if (Long.class.isAssignableFrom(cls)) {
				edit.putLong(key, (Long) value);
			} else if (String.class.isAssignableFrom(cls)) {
				edit.putString(key, (String) value);
			}
		}
		edit.apply();
	}

	public static boolean getBoolean(String name, boolean defaultValue) {
		reinit();
		if (sharedPreferences == null)
			return defaultValue;
		return sharedPreferences.getBoolean(name, defaultValue);
	}

	public static boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	public static int getInt(String name, int defaultValue) {
		reinit();
		if (sharedPreferences == null)
			return defaultValue;
		return sharedPreferences.getInt(name, defaultValue);
	}

	public static int getInt(String name) {
		return getInt(name, 0);
	}

	public static long getLong(String name, long defaultValue) {
		reinit();
		if (sharedPreferences == null)
			return defaultValue;
		return sharedPreferences.getLong(name, defaultValue);
	}

	public static long getLong(String name) {
		return getLong(name, 0);
	}

	public static String getString(String name, String defaultValue) {
		reinit();
		if (sharedPreferences == null)
			return defaultValue;
		return sharedPreferences.getString(name, defaultValue);
	}

	public static String getString(String name) {
		return getString(name, "");
	}

	public static float getFloat(String name, float defaultValue) {
		reinit();
		if (sharedPreferences == null)
			return defaultValue;
		return sharedPreferences.getFloat(name, defaultValue);
	}

	public static float getFloat(String name) {
		return getFloat(name, 0);
	}

	public static void clear(String... keys) {
		reinit();
		if (sharedPreferences == null)
			return;
		Editor edit = sharedPreferences.edit();
		for (String key : keys) {
			edit.remove(key);
		}
		edit.apply();
	}
	
	public static void setBatchId(int id){
		commit(EID, id);
	}
	
	public static int getBatchId(){
		return getInt(EID, 660300);
	}

	public static void clear() {
		reinit();
		if (sharedPreferences == null)
			return;
		Editor edit = sharedPreferences.edit();
		edit.clear();
		edit.apply();
	}
}
