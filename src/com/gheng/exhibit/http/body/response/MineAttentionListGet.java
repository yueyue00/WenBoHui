package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class MineAttentionListGet {
	// 我的关注列表字段
	// 服务器返回的字段名 要和接口文档一直 必须加这个@Expose
	@Expose
	public String meettingid;
	@Expose
	public String meettingtitle;
	@Expose
	public String meettingtime;

}
