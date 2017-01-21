package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class News_LieBiao {

	@Expose
	public String content;
	@Expose
	public String title;
	@Expose
	public String photourl;
	@Expose
	public String newsurl;
}
