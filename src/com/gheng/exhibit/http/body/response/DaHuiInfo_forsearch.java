package com.gheng.exhibit.http.body.response;


import com.google.gson.annotations.Expose;

/**
 * 大会概要信息实体类
 */
public class DaHuiInfo_forsearch {
	@Expose
   public String meettingroom;//会场
	@Expose
   public String meettingtime;//会议时间安排
	@Expose
   public String meettingid;//会议id
	@Expose
   public int isguanzhu;//是否已关注
	@Expose
   public String meettingtalkman;//主持人
	@Expose
   public String meettingtitle;//会议标题
}
