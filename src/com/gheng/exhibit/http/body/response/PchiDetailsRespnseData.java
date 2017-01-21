package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class PchiDetailsRespnseData {

	// 会议详情接口参数
	@Expose
	public String meettingtitle;// 会议名称
	@Expose
	public String meettingtime;// 会议时间
	@Expose
	public String meettingroom;// 会议地点
	@Expose
	public String meettingtalkman;// 会议主讲人
	@Expose
	public String mettingcontent;// 会议简介
	@Expose
	public String venceLatitude;// 会场经纬度
	@Expose
	public String venceLongitude;// 会场经纬度
	// @Expose
	// public String meettingyiti; // 会议议题
	// @Expose
	// public int isguanzhu;// 0未关注 1已关注
	@Expose
	public ArrayList<HuiYiJiaBin> guests;// 会议嘉宾列表
	@Expose
	public ArrayList<HuiYiYiTi> meettingyiti;

	public ArrayList<HuiYiYiTi> getMeettingyiti() {
		return meettingyiti;
	}

	public void setMeettingyiti(ArrayList<HuiYiYiTi> meettingyiti) {
		this.meettingyiti = meettingyiti;
	}

	public void setGuests(ArrayList<HuiYiJiaBin> guests) {
		this.guests = guests;
	}

	public ArrayList<HuiYiJiaBin> getGuests() {
		return guests;
	}

}
