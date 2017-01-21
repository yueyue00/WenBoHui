package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 演讲题目
 * 
 * @author lileixing
 */
@Table(name = "ScheduleInfo")
public class ScheduleInfo extends BaseModel {

	@Column(column = "title")
	private String title;

	@Column(column = "time")
	private String time;

	@Column(column = "speakerid")
	private String speakerid;

	@Column(column = "sorting")
	private int sorting;

	@Column(column = "scheduleid")
	private long scheduleid;

	@Column(column = "host")
	private String host;

	@Column(column = "enhost")
	private String enhost;

	@Column(column = "commentcount")
	private Integer commentcount;

	@Column(column = "starttime")
	private String starttime;

	@Column(column = "endtime")
	private String endtime;

	@Column(column = "isfav")
	private int isfav;

	@Column(column = "ispraise")
	private int ispraise;

	@Column(column = "praisecount")
	private int praisecount;

	@Column(column = "batchid")
	private Integer batchid;

	@Transient
	public Schedule schedule;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the speakerid
	 */
	public String getSpeakerid() {
		return speakerid;
	}

	/**
	 * @param speakerid
	 *            the speakerid to set
	 */
	public void setSpeakerid(String speakerid) {
		this.speakerid = speakerid;
	}

	/**
	 * @return the sorting
	 */
	public int getSorting() {
		return sorting;
	}

	/**
	 * @param sorting
	 *            the sorting to set
	 */
	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	/**
	 * @return the scheduleid
	 */
	public long getScheduleid() {
		return scheduleid;
	}

	/**
	 * @param scheduleid
	 *            the scheduleid to set
	 */
	public void setScheduleid(long scheduleid) {
		this.scheduleid = scheduleid;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the enhost
	 */
	public String getEnhost() {
		return enhost;
	}

	/**
	 * @param enhost
	 *            the enhost to set
	 */
	public void setEnhost(String enhost) {
		this.enhost = enhost;
	}

	/**
	 * @return the commentcount
	 */
	public Integer getCommentcount() {
		return commentcount;
	}

	/**
	 * @param commentcount
	 *            the commentcount to set
	 */
	public void setCommentcount(Integer commentcount) {
		this.commentcount = commentcount;
	}

	/**
	 * @return the starttime
	 */
	public String getStarttime() {
		return starttime;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime() {
		return endtime;
	}

	/**
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getIsfav() {
		return isfav;
	}

	public void setIsfav(int isfav) {
		this.isfav = isfav;
	}

	public int getIspraise() {
		return ispraise;
	}

	public void setIspraise(int ispraise) {
		this.ispraise = ispraise;
	}

	public int getPraisecount() {
		return praisecount;
	}

	public void setPraisecount(int praisecount) {
		this.praisecount = praisecount;
	}

	/**
	 * @return the batchid
	 */
	public Integer getBatchid() {
		return batchid;
	}

	/**
	 * @param batchid
	 *            the batchid to set
	 */
	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}

}
