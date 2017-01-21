package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

/**
 * 大会概要信息实体类
 */
public class DaHuiInfo {
	@Expose
	public String meettingtime = "";// 大会时间安排
	@Expose
	public String meettingid = "";// 会议id
	@Expose
	public String meettingtalkman = "";// 主持人
	// @Expose
	// public ArrayList<DaHuiGuest> guests;//嘉宾集合数据
	@Expose
	public String meettingtitle = "";// 大会名称
	@Expose
	public String startTime = "";//开始时间
	@Expose 
	public String endTiime = "";//结束时间

	// public ArrayList<DaHuiGuest> getGuests() {
	// return guests;
	// }
	// public void setGuests(ArrayList<DaHuiGuest> guests) {
	// this.guests = guests;
	// }

}
