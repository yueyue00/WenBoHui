package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class IfAttention {
	// 关注或者取消 某会议
	@Expose
	public String userid; // 用户ID
	@Expose
	public String meettingid; // 会议的ID
	@Expose
	public int operation; // 0是取消关注  1是关注该会议

}
