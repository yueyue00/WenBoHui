package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class VipXingChengGuest implements Serializable{

	@Expose
	public String time;
	@Expose
	public String location;
	@Expose
	public String description;
	@Expose
	public String date;
	@Expose
	public String meetingId;
	@Expose
	public String typename;
	@Expose
	public String type;
	@Expose
	public String title;
	
	
	
}
