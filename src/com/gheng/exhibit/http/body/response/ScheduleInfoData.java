package com.gheng.exhibit.http.body.response;

import java.util.List;

import com.gheng.exhibit.model.databases.Schedule;

/**
 *
 * @author lileixing
 */
public class ScheduleInfoData {

	public String name;
	public String address;
	public String time;
	public String host;
	public String partner;
	public String remark;
	public String linkname;
	public String linktel;
	public String sponsor ;
	public String date;
	public String startime;
	public String endtime;
	public Schedule model;
	public List<ScheduleInfoSupportData> rdata;
	
}
