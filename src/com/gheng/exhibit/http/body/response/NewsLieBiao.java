package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class NewsLieBiao {

	@Expose
	public List<News_LunBoTu> lunbo = new ArrayList<News_LunBoTu>();
	@Expose
	public List<News_LieBiao> liebiao = new ArrayList<News_LieBiao>();
}
