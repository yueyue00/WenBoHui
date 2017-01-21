package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 融云-群组成员
 */
@SuppressWarnings("serial")
public class GroupInfoBean implements Serializable {
	@Expose
	public String GROUP_ID;
	@Expose
	public String groupName;
	@Expose
	public String USER_ID;
	@Expose
	public String userName;
	@Expose
	public String isManager;
	@Expose
	public String truename;
}
