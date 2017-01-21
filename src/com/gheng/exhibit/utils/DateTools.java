package com.gheng.exhibit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.gheng.exhibit.model.databases.Language;

/**
 * 日期工具类
 * 
 * @author lileixing
 */
public class DateTools
{

	private static SimpleDateFormat format = new SimpleDateFormat();
	private static SimpleDateFormat enFormat = new SimpleDateFormat("d MMMM yyyyy", Locale.ENGLISH);

	public static String format(Date date, String pattern)
	{
		format.applyPattern(pattern);
		return format.format(date);
	}

	public static String format(Date date)
	{
		return format(date, getPattern());
	}

	public static String formatNoYear(Date date)
	{
		String pattern = "M月d日";
		if (SharedData.getInt("i18n", Language.ZH) == Language.EN)
		{
			pattern = "d MMMM";
		}
		getFormat().applyPattern(pattern);
		return getFormat().format(date);
	}

	public static String formatWeek(Date date)
	{
		String pattern = "EEEE";
		getFormat().applyPattern(pattern);
		return getFormat().format(date);
	}

	public static Date parse(String value, String pattern)
	{
		getFormat().applyPattern(pattern);
		try
		{
			return getFormat().parse(value);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Date parse(String value)
	{
		return parse(value, getPattern());
	}

	private static SimpleDateFormat getFormat()
	{
		int lang = SharedData.getInt("i18n", Language.ZH);
		if (lang == Language.ZH)
		{
			return format;
		}
		return enFormat;
	}

	private static String getPattern()
	{
		int lang = SharedData.getInt("i18n", Language.ZH);
		if (lang == Language.ZH)
		{
			return "yyyy-MM-dd";
		}
		return "d MMMM yyyy";
	}
}
