package com.gheng.exhibit.http.body.response;

import com.gheng.exhibit.model.databases.BaseModel;

/**
 *
 * @author lileixing
 */
public class ScheduleInfoSupportData extends BaseModel{

	public String time;
	public String name;
	public String speaker;
	public String speakerid;
	
	public String companystand;
	public long companyid;
	public String companyname;
	public String logo;
	
	//0演讲者 1主持人
	public int type = 0;
	
	public int commentcount;//评论数据
	public int praisecount;//点赞数量
	public int isfav;//是否收藏
	public int ispraise;//是否点赞
}
