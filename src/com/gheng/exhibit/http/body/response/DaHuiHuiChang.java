package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

/**
 * 大会日期分类实体类
 */
public class DaHuiHuiChang {
	@Expose
    public String meettingroomid;//会场id
	@Expose
    public String meettingname;//会场名称
	@Expose
	public ArrayList<DaHuiInfo> meetting;//当天大会数据集合
	
	public ArrayList<DaHuiInfo> getMeetting() {
		return meetting;
	}
	public void setMeetting(ArrayList<DaHuiInfo> meetting) {
		this.meetting = meetting;
	}
	
	
}
