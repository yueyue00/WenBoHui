package com.gheng.exhibit.http.body.request;

import android.app.Activity;

import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.PhoneUtils;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;

public class LocationBody
{
	/**
	 * 定位类型：0、WIFI终端侧定位；1、蓝牙终端侧定位； 2、网络层定位
	 */
	public int type = 1;
	public int floorNum = 1;
	public double x;
	public double y;
	public long timestamp;
	private Activity activity;

	public LocationBody(Activity activity, int type, int floorNum,double x, double y)
	{
		this.type = type;
		this.activity = activity;
		this.x = x;
		this.y = y;
		this.floorNum = floorNum;
	}

	@Override
	public String toString()
	{
		return type + "," + AppTools.getLocalMacAddress(activity).replace(":", "") + "," + x + "," + y + "," + "310001_" + floorNum + "_0" + ","
				+getPhoneNo()+","+PhoneUtils.getLocalIpAddress()+","
				+ SharedData.getString("snum");
	}
	
	private String getPhoneNo(){
		String s = PhoneUtils.getPhoneNo();
		if(StringTools.isBlank(s)){
			s = SharedData.getString(SharedData.MOBILE);
			if(StringTools.isBlank(s))
				s = "";
		}
		return s;
	}
	
}
