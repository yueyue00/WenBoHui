package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class PchiDetailsData {

	// 会议详细页面需要从服务器获取的数据字段
	@Expose
	public String meettingid;
	
	@Expose
	public int lg;

}
