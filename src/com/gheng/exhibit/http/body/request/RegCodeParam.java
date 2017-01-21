package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.SharedData;
/**
 * 绑定手机-验证手机号
 * @author zhaofangfang
 *
 */
public class RegCodeParam {

	public String eid = SharedData.getBatchId()+"";
	
	public String mobile;
	
	public String code;
	
	public String type;

}
