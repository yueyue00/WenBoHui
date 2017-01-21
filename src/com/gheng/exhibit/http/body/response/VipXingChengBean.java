package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;
import java.util.List;

public class VipXingChengBean {

	private String date;
	private String week;
	private List<VipXingChengGuestBean> journey = new ArrayList<VipXingChengGuestBean>();
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public List<VipXingChengGuestBean> getJourney() {
		return journey;
	}
	public void setJourney(List<VipXingChengGuestBean> journey) {
		this.journey = journey;
	}
	
}
