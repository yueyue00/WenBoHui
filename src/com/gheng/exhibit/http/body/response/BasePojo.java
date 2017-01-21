package com.gheng.exhibit.http.body.response;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

/**
 * 请求响应json基类
 */
public class BasePojo {
	@Expose
	public String code="";
	@Expose
	public String message="";
	@Expose
	public JsonElement info;
	@Expose
	public int pagenum;//请求当前页
	@Expose
	public int pagecount;//共有多少页

}
