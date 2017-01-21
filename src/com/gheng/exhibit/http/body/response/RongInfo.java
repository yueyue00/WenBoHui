package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class RongInfo implements Serializable {
	@Expose
	public String ry_userId;
	@Expose
	public String ry_token;
	@Expose
	public String ry_imgUrl;
	@Expose
	public String ry_name;

}
