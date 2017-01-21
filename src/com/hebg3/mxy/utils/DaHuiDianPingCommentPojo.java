package com.hebg3.mxy.utils;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class DaHuiDianPingCommentPojo implements Serializable{

	@Expose 
	public String commentusername="";
	@Expose 
	public String commentuserid="";
	@Expose 
	public String commentuserphoto="";
	@Expose 
	public String replyuserid="";
	@Expose 
	public String replyusername="";
	@Expose 
	public String content="";
	@Expose 
	public String uploadtime="";
}
