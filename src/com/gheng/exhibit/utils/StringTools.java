package com.gheng.exhibit.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author Administrator
 * 
 */
public class StringTools {

	private StringTools() {
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isBlank(String s) {
		return (s == null || s.trim().length() == 0);
	}

	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}
	
	public static String upper(String s) {
		if(isBlank(s)) {
			return "";
		}
		return s.toUpperCase();
	}
	
	public static String lower(String s) {
		if(isBlank(s)) {
			return "";
		}
		return s.toLowerCase();
	}

	/**
	 * MD5加密后的字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String getMd5Str(String s) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * 验证手机号码
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isMobile(String s) {
		String r = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$";
		return check(s, r);
	}

	/**
	 * 验证中文
	 * 
	 * @param value
	 * @param length
	 * @return
	 */
	public boolean isChineseName(String value) {
		String r = "^[u4e00-u9fa5]+$";
		return check(value, r);
	}

	/**
	 * 验证邮箱
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmail(String s) {
		String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		return check(s, regex);
	}

	/**
	 * 验证电话
	 * 
	 * @param telephone
	 * @return
	 */
	public static boolean isPhone(String telephone) {
		String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
		return check(telephone, regex);
	}

	/**
	 * 检测QQ
	 * 
	 * @param QQ
	 * @return
	 */
	public static boolean isQQ(String QQ) {
		String regex = "^[1-9][0-9]{4,}$";
		return check(QQ, regex);
	}

	/**
	 * 检测URL
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isUrl(String s) {
		String r = "(http://|https://){1}[\\w\\.\\-/:]+";
		return check(s, r);
	}
	
	public static boolean isContains(Object[] objs,Object obj){
		if(objs == null || obj == null)
			return false;
		for (Object object : objs) {
			if(obj.equals(object) || obj == object)
				return true;
		}
		return false;
	}

	public static boolean check(String str, String regex) {
		boolean flag = false;
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 获取最小的字符串
	 */
	public static String getMinString(List<String> strs){
		String result = strs.get(0);
		for(int i = 1; i < strs.size(); i++){
			String s = strs.get(i);
			if(result.length() > s.length()){
				result = s;
			}else if(result.length() == s.length()){
				for(int j = 0; j < s.length(); j++){
					char c1 = result.charAt(j);
					char c2 = s.charAt(j);
					if(c1 > c2){
						result = s;
						break;
					}
				}
			}
		}
		return result;
	}
	
	public static String getMinString(String[] strs){
		String result = strs[0].split("-")[0];
		for(int i = 1; i < strs.length; i++){
			String s = strs[i].split("-")[0];
			if(result.length() > s.length()){
				result = s;
			}else if(result.length() == s.length()){
				if(result.compareTo(s) > 0){
					result = s;
				}
			}
		}
		return result;
	}
	
	public static String capitalize(String str){
		if(StringTools.isNotBlank(str)){
			return Character.toUpperCase(str.charAt(0)) + str.substring(1);
		}
		return str;
	}
	
	public static boolean equals(String str,String other){
		if(str == null && other == null)
			return false;
		if(str == null || other == null)
			return false;
		return str.equals(other);
	}

}
