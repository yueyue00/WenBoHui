package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.SharedData;


/**
 *	初始化方法请求参数
 * @author lileixing
 */
public class InitParam {

	public String snum = SharedData.getString("snum");
	
	public String eid = SharedData.getBatchId()+"";
	
	public String mac;
	
	public String ip;
	
	public String mobile;
	
	public String appversion = "0";
	
	public String mapversion = "0";
	
	public String wifiversion = "0";
	
	public String bconversion = "0";
	
	public String type = "1";
	
	public String mobiletype;
	
	public String ostype;
	
	public String teltype;
	
}
