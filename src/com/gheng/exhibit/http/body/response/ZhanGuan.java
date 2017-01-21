package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class ZhanGuan {
	@Expose
	public String name;
	@Expose
	public String id;
	@Expose
	public String newsurl;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNewsurl() {
		return newsurl;
	}
	public void setNewsurl(String newsurl) {
		this.newsurl = newsurl;
	}

}
