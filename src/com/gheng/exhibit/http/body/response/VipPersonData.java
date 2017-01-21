package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 
 * @author Dean vip 人员的bean
 */
public class VipPersonData implements Serializable{
	@Expose
	public String sex;
	@Expose
	public String id;
	@Expose
	public String job;
	@Expose
	public String workplace;
	@Expose
	public String username;
	@Expose
	public String photourl;
	@Expose
	public String photourlbig;
	@Expose
	public String groupname;
	@Expose
	public String groupid;

	@Override
	public String toString() {
		return "VipPersonData [sex=" + sex + ", id=" + id + ", job=" + job
				+ ", workplace=" + workplace + ", username=" + username
				+ ", photourl=" + photourl + ", photourlbig=" + photourlbig
				+ ", groupname=" + groupname + ", groupid=" + groupid + "]";
	}

}
