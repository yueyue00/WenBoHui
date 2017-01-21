package com.gheng.exhibit.http;

import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.SharedData;

/**
 * 基本Http请求
 * @author zhao
 */
public class BaseRequestData<T> {
	/**
	 * action
	 */
	public String action;
	/**
	 * SID
	 */
	public String snum = SharedData.getString("snum");  //Constant.IMEI;
	/**
	 * 用户名
	 */
	public String uname = SharedData.getUser();
	/**
	 * body
	 */
	public T body;
	
	public String lg = SharedData.getInt("i18n", Language.ZH)+"";
	
	public BaseRequestData(){}
	
	public BaseRequestData(String action){
		this.action = action;
	}
	
	public BaseRequestData(String action,T body){
		this.action = action;
		this.body = body;
	}
}
