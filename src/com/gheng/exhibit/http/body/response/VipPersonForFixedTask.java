package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class VipPersonForFixedTask implements Serializable{

	@Expose 
	private String vipid;
	@Expose 
	private String name;
	@Expose 
	private String imgPath;
	public String getVipid() {
		return vipid;
	}
	public void setVipid(String vipid) {
		this.vipid = vipid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	
	
}
