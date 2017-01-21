package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class HuiChangMap implements Serializable{
	@Expose
	public String position = "";
	@Expose
	public String time = "";
	@Expose
	public String name = "";
	@Expose
	public String imgurl = "";
}
