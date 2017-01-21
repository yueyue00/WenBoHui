package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class CommonVipBeanForFixedTask {
	@Expose
	private String id;
	@Expose
	private String name;
	@Expose
	private String imgPath;
	@Expose
	private boolean checkState;
	@Expose 
	private boolean haveSelected;//用来控制后台返回的人选选中状态，如果为true则复选框不允许再次点击
	
	
	
	public boolean isHaveSelected() {
		return haveSelected;
	}
	public void setHaveSelected(boolean haveSelected) {
		this.haveSelected = haveSelected;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public boolean isCheckState() {
		return checkState;
	}
	public void setCheckState(boolean checkState) {
		this.checkState = checkState;
	}
	
	
	

}
