package com.gheng.exhibit.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.gheng.exhibit.application.MyApplication;

/**
 * 手机信息工具类
 * 
 * @author lileixing
 */
public class PhoneUtils {

	private static TelephonyManager telephonyManager;
	private static WifiManager wifiManager;

	static {
		telephonyManager = (TelephonyManager) MyApplication.getInstance()
				.getSystemService(Context.TELEPHONY_SERVICE);
		wifiManager = (WifiManager) MyApplication.getInstance()
				.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 获取设备ID
	 */
	public static String getDeviceId() {
		if (telephonyManager == null)
			return "";
		return telephonyManager.getSubscriberId();
	}

	/**
	 * 获取手机号
	 */
	public static String getPhoneNo() {
		if (telephonyManager == null)
			return "";
		return telephonyManager.getLine1Number();
	}

	/**
	 * 获取MAC地址
	 */
	public static String getLocalMacAddress() {
		WifiInfo info = wifiManager.getConnectionInfo();
		String mac = info.getMacAddress();
		if (StringTools.isNotBlank(mac)) {
			mac = mac.replace(":", "");
		}
		return mac;
	}

	/**
	 * 获取IP地址
	 */
	public static String getLocalIpAddress() {
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return intToIp(ipAddress);
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	/**
	 * 获取手机型号
	 */
	public static String getPhoneType() {
		return Build.MODEL;
	}

	/**
	 * 获取SDK
	 */
	public static int getSDK() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 获取版本号
	 */
	public static String getBuildVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机通讯制式 -1未知 0 移动 1联通 2电信
	 * 
	 * @return
	 */
	public static String getTelType() {
		String deviceId = getDeviceId();
		
		if(deviceId == null)
			return "-1";
		
		if (deviceId.startsWith("46000") || deviceId.startsWith("46002")) {
			return "0";
		} else if (deviceId.startsWith("46001")) {
			return "1";
		} else if (deviceId.startsWith("46003")) {
			return "2";
		} else {
			return "-1";
		}
	}
}
