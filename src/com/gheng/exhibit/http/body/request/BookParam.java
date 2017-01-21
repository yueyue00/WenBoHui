package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;

/**
 * 预定
 * @author lileixing
 */
public class BookParam {

	public String eid = SharedData.getBatchId()+"";
	
	public String zoneid = Constant.zoneid+"";
	
	public String companyid;
	
	public String code;
	
	public String floor;
	
}
