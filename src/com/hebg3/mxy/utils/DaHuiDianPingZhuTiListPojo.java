package com.hebg3.mxy.utils;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;

public class DaHuiDianPingZhuTiListPojo implements Serializable{

	@Expose 
	public String actionid="";// 主题id

	@Expose 
	public String content="";// 内容

	@Expose 
	public int state;//主题当前状态 1 已发布 2 发布中 3 未通过
	
	@Expose 
	public ArrayList<PhotoInfo> photoarray;// 图片信息集合

	@Expose 
	public String uploadtime;// 上传时间
	
	@Expose 
	public String uploadusername;// 发起人
	
	@Expose 
	public String uploaduserid;// 发起人id
	
	@Expose
	public String uploaduserphoto;//上传着头像地址
	
	@Expose
	public String comments;//被评论次数
	
	@Expose
	public ArrayList<DaHuiDianPingCommentPojo> commentarray;//评论集合
	

	public ArrayList<PhotoInfo> getPhotoarray() {
		return photoarray;
	}

	public void setPhotoarray(ArrayList<PhotoInfo> photoarray) {
		this.photoarray = photoarray;
	}

	public ArrayList<DaHuiDianPingCommentPojo> getCommentarray() {
		return commentarray;
	}

	public void setCommentarray(ArrayList<DaHuiDianPingCommentPojo> commentarray) {
		this.commentarray = commentarray;
	}
	
	
}
	


