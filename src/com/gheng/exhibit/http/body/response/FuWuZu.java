package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class FuWuZu {
	@Expose
	public String name;
	@Expose
	public String tell;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTell() {
		return tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

}
