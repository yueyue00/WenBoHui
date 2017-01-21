package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 时间类型记录
 * 
 * @author lileixing
 */
@Table(name = "TimeRecord")
public class TimeRecord extends BaseModel {

	@Column(column = "name")
	private String name;

	@Column(column = "time")
	private long time;
	
	@Column(column = "batchid")
	private int batchid;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the batchid
	 */
	public int getBatchid() {
		return batchid;
	}

	/**
	 * @param batchid the batchid to set
	 */
	public void setBatchid(int batchid) {
		this.batchid = batchid;
	}

}
