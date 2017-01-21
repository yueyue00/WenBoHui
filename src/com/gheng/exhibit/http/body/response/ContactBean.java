package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class ContactBean implements Serializable {
	@Expose
	public String id;
	@Expose
	public String pid;
	@Expose
	public String name;
	@Expose
	public String type;
	@Expose
	public String truename;
	@Expose
	public String mobile;
	@Expose
	public String departname;
	@Expose
	public String shortname;
	@Expose
	public RongInfo map;
	@Expose
	public boolean check = false;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	@Override
	public String toString() {
		return "ContactBean [id=" + id + ", pid=" + pid + ", name=" + name
				+ ", type=" + type + ", truename=" + truename + ", mobile="
				+ mobile + ", departname=" + departname + ", shortname="
				+ shortname + ", map=" + map + ", check=" + check + "]";
	}

}
