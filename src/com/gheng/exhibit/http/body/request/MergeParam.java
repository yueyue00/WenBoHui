package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;

/**
 * 合并参数
 * @author lileixing
 */
public class MergeParam {
	
	public String eid = SharedData.getBatchId()+"";
	
	public String zoneid = Constant.zoneid+"";
	
	public String floor;
	
	public String codes;
	
	public String code;
	
}
