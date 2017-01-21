package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class YiTiDetailsRespnseData {
	@Expose
	public String yitititle;
	@Expose
	public String yititime;
	@Expose
	public String yititalkman;
	@Expose
	public String yiticontent;

	@Expose
	public ArrayList<HuiYiJiaBin> guests;// 议题嘉宾列表

	public ArrayList<HuiYiJiaBin> getGuests() {
		return guests;
	}

	public void setGuests(ArrayList<HuiYiJiaBin> guests) {
		this.guests = guests;
	}
}
