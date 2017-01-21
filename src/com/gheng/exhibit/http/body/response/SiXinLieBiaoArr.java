package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class SiXinLieBiaoArr {

	private String createTimes;
	private String messcontent;
	private String messtag;
	public String getCreateTimes() {
		return createTimes;
	}
	public void setCreateTimes(String createTimes) {
		this.createTimes = createTimes;
	}
	public String getMesscontent() {
		return messcontent;
	}
	public void setMesscontent(String messcontent) {
		this.messcontent = messcontent;
	}
	public String getMesstag() {
		return messtag;
	}
	public void setMesstag(String messtag) {
		this.messtag = messtag;
	}
	
}
