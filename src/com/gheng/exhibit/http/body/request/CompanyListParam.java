package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.SharedData;

/**
 *
 * @author lileixing
 */
public class CompanyListParam {

	public String eid = SharedData.getBatchId()+"";
	
	public String time = SharedData.getLong("time", 0) +"";
	
}
