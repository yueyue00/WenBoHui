package com.hebg3.mxy.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class ChangeTimeToGMT8 {

	/**
	 * timeZoneOffset表示时区，如中国一般使用东八区，因此timeZoneOffset就是8
	 * 
	 * @param timeZoneOffset
	 * @return
	 */
	public static String getFormatedDateString(int timeZoneOffset) {
		if (timeZoneOffset > 13 || timeZoneOffset < -12) {
			timeZoneOffset = 8;
		}
		TimeZone timeZone;
		String[] ids = TimeZone.getAvailableIDs(timeZoneOffset * 60 * 60 * 1000);
		if (ids.length == 0) {
			timeZone = TimeZone.getDefault();
		} else {
			timeZone = new SimpleTimeZone(timeZoneOffset * 60 * 60 * 1000, ids[0]);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这里需要新建format，因为如果重复使用format的话，时区就没有变化了
		format.setTimeZone(timeZone);
		return format.format(new Date());
	}

	/**
	 * 根据转入的nowTime时间以及新的时区，返回新时区的nowTime对应的时间字符串
	 * @param nowTime 传入时间  格式："yyyy-MM-dd HH:mm:ss"
	 * @param newTimeZone
	 * @return
	 * @throws Exception
	 */
	public static String getnewTimeZoneTime(String nowTime,String newTimeZone) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这里需要新建format，因为如果重复使用format的话，时区就没有变化了
		Date date = format.parse(nowTime);
		TimeZone timeZone = TimeZone.getTimeZone(newTimeZone);
		format.setTimeZone(timeZone);
		return format.format(date);
	}
}
