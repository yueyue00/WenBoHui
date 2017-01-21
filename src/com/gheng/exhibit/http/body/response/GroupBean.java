package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
/**
 * 群组列表的bean
 * @author Administrator
 *
 */
public class GroupBean implements Serializable{
	@Expose
	public int id;
	@Expose
	public String groupId;
	@Expose
	public String groupName;
	@Expose
	public String status;
	@Expose
	public String userId;
	@Expose
	public String isManager;
	@Expose
	public String createDate;
}
