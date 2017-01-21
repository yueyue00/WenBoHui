package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;

/**
 *
 * @author lileixing
 */
public class SaleDetailParam {

	public String eid = SharedData.getBatchId()+"";
	
	public String zoneid = Constant.zoneid+"";

	public String floor;
	
	public String code;
	
}
