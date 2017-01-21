package com.hebg3.mxy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class IsWebCanBeUse {

	/**
	 * 判断移动网络是否打开
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isMobileCanBeUse(Context context) {
		// 获取连接管理器
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取网络连接状态信息
		if(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)==null){
			System.out.println("ConnectivityManager.TYPE_MOBILE是null 这个是个平板电脑！！！");
			return false;
		}
		State Mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		// 返回移动网络是否可用
		if (Mobile == State.CONNECTED || Mobile == State.CONNECTING) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断WIFI是否可用
	 * 
	 * @param cont
	 * @return
	 */
	private static boolean isWifiCanBeUse(Context context) {
		// 获取连接管理器
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取状态
		State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		// 判断wifi当前状态信息
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {// wifi处于可用状态
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断网络是否可用
	 */
	public static boolean isWebCanBeUse(Context context){
		if(IsWebCanBeUse.isMobileCanBeUse(context)||IsWebCanBeUse.isWifiCanBeUse(context)){
			return true;
		}else{
			return false;
		}
	}
}
