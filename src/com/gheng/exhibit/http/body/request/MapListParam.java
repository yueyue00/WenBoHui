package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;

/**
 * 地图数据列表
 * @author lileixing
 */
public class MapListParam {
	public String eid = SharedData.getBatchId()+"";
	
	public String zoneid = Constant.zoneid+"";
	
	public String floor;
}
