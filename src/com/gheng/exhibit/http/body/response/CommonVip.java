package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class CommonVip {

	@Expose
	private String commonVipId;
	@Expose
	private String name;
	@Expose
	private String imgPath;
	@Expose
	private boolean jiejiState;
	@Expose
	private boolean qiaobaodaoState;
	@Expose
	private boolean banruzhuState;
	@Expose
	private boolean lingziliaoState;
	@Expose
	private boolean kanzhanlanState;
	@Expose
	private boolean songjiState;
	
	public String getCommonVipId() {
		return commonVipId;
	}
	public void setCommonVipId(String commonVipId) {
		this.commonVipId = commonVipId;
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
	public boolean isJiejiState() {
		return jiejiState;
	}
	public void setJiejiState(boolean jiejiState) {
		this.jiejiState = jiejiState;
	}
	public boolean isQiaobaodaoState() {
		return qiaobaodaoState;
	}
	public void setQiaobaodaoState(boolean qiaobaodaoState) {
		this.qiaobaodaoState = qiaobaodaoState;
	}
	public boolean isBanruzhuState() {
		return banruzhuState;
	}
	public void setBanruzhuState(boolean banruzhuState) {
		this.banruzhuState = banruzhuState;
	}
	public boolean isLingziliaoState() {
		return lingziliaoState;
	}
	public void setLingziliaoState(boolean lingziliaoState) {
		this.lingziliaoState = lingziliaoState;
	}
	public boolean isKanzhanlanState() {
		return kanzhanlanState;
	}
	public void setKanzhanlanState(boolean kanzhanlanState) {
		this.kanzhanlanState = kanzhanlanState;
	}
	public boolean isSongjiState() {
		return songjiState;
	}
	public void setSongjiState(boolean songjiState) {
		this.songjiState = songjiState;
	}
	
	

}
