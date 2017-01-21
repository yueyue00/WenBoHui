package com.gheng.exhibit.http.body.response;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class VipSchduleNewBean {
	@Expose 
	private String code;
	@Expose
	private String message;
	@Expose
	private int pagecount;
	@Expose
	private int pagenum;
	@Expose
	private JsonElement info;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getPagecount() {
		return pagecount;
	}
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
	public int getPagenum() {
		return pagenum;
	}
	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}
	public JsonElement getInfo() {
		return info;
	}
	public void setInfo(JsonElement info) {
		this.info = info;
	}
	
	
}
