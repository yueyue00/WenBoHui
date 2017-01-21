package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class VipXingCheng {

	private String smallPorurl;
	@Expose
	public String date;
	@Expose
	public String week;
	@Expose
	public List<VipXingChengGuest> journey = new ArrayList<VipXingChengGuest>();

	public String getSmallPorurl() {
		return smallPorurl;
	}
	public void setSmallPorurl(String smallPorurl) {
		this.smallPorurl = smallPorurl;
	}
	
	
}
