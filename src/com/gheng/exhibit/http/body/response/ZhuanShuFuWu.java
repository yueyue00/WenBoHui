package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class ZhuanShuFuWu {
	/**
	 * 专属服务
	 */
	@Expose
	public String serveruserid = "";
	@Expose
	public String userid = "";
	@Expose
	public String juese = "";
	@Expose
	public String servername = "";
	@Expose
	public String serverphone = "";
	@Expose
	public String baoanphone = "";
	@Expose
	public String fanyiphone = "";
	@Expose
	public String yihurenyuanphone = "";
	@Expose
	public String sixin = "";// 马晓勇加 是否可以发私信，1可以 2不可以
	@Expose
	public String mobile;
	@Expose
	public String name;
	@Expose
	public String ry_userId;

}
