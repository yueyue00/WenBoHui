package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

/**
 * 大会嘉宾实体类
 */
public class DaHuiGuest {
   
	@Expose
	public String guestphotourl = "";// 嘉宾头像url地址
	@Expose
	public String guestname = "";// 嘉宾姓名
	@Expose
	public String guestzhiwu = "";// 嘉宾职务描述
}
