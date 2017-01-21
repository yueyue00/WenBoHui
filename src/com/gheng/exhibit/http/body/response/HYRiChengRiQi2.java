package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class HYRiChengRiQi2 {
	private String date;
	private String week;
	private String meettingtime;
	private String meettingid;
	private String meettingtalkman;
	private String meettingtitle;
	public boolean headflag = false;

	private int iscolor;
	private int iskz;

	public int getIscolor() {
		return iscolor;
	}

	public void setIscolor(int iscolor) {
		this.iscolor = iscolor;
	}

	public boolean getHeadflag() {
		return headflag;
	}

	public void setHeadflag(boolean headflag) {
		this.headflag = headflag;
	}

	public String getdate() {
		return date;
	}

	public void setdate(String date) {
		this.date = date;
	}

	public String getweek() {
		return week;
	}

	public void setweek(String week) {
		this.week = week;
	}

	public String getMeettingtime() {
		return meettingtime;
	}

	public void setMeettingtime(String meettingtime) {
		this.meettingtime = meettingtime;
	}

	public String getMeettingid() {
		return meettingid;
	}

	public void setMeettingid(String meettingid) {
		this.meettingid = meettingid;
	}

	public String getMeettingtalkman() {
		return meettingtalkman;
	}

	public void setMeettingtalkman(String meettingtalkman) {
		this.meettingtalkman = meettingtalkman;
	}


	public String getMeettingtitle() {
		return meettingtitle;
	}

	public void setMeettingtitle(String meettingtitle) {
		this.meettingtitle = meettingtitle;
	}


	public int getIskz() {
		return iskz;
	}

	public void setIskz(int iskz) {
		this.iskz = iskz;
	}

	@Override
	public String toString() {
		return "HYRiChengRiQi [date=" + date + ", week=" + week
				+ ", meettingtime=" + meettingtime + ", meettingid="
				+ meettingid + ", meettingtalkman=" + meettingtalkman
				+ ", meettingtitle=" + meettingtitle + ", headflag=" + headflag
				+ ", iscolor=" + iscolor + ", iskz=" + iskz + "]";
	}

	
	
}
