package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 会议名称
 * 
 * @author lileixing
 */
@Table(name = "Schedule")
public class Schedule extends BaseModel {

	@Column(column = "typeid")
	private long typeid;

	@Column(column = "date")
	private String date;

	@Column(column = "time")
	private String time;

	@Column(column = "entime")
	private String entime;

	@Column(column = "name")
	private String name;

	@Column(column = "enname")
	private String enname;

	@Column(column = "address")
	private String address;

	@Column(column = "host")
	private String host;

	@Column(column = "partner")
	private String partner;

	@Column(column = "enpartner")
	private String enpartner;

	@Column(column = "sponsor")
	private String sponsor;

	@Column(column = "ensponsor")
	private String ensponsor;

	@Column(column = "sorting")
	private int sorting;

	@Column(column = "remark")
	private String remark;

	@Column(column = "enremark")
	private String enremark;

	@Column(column = "isfav")
	private int isfav;
	/**
	 * 是否是闭门会
	 */
	@Column(column = "closeflag")
	private int closeflag;

	/**
	 * 是否是同声传译
	 */
	@Column(column = "syncflag")
	private int syncflag;
	/**
	 * 是否收费 1是 0否
	 */
	@Column(column = "feeflag")
	private int feeflag;

	/**
	 * 是否点赞
	 */
	@Column(column = "ispraise")
	private int ispraise;

	@Column(column = "starttime")
	private String starttime;

	@Column(column = "endtime")
	private String endtime;

	/**
	 * 点赞个数
	 */
	@Column(column = "praisecount")
	private int praisecount;
	/**
	 * 是否报名
	 */
	@Column(column="issign")
	private int issign;
	/**
	 * 是否推荐
	 */
	@Column(column = "keypoint")
	private int keypoint;
	
	/**
	 * 首页的图片显示
	 */
	@Transient
	public int icon;

	@Column(column = "batchid")
	private Integer batchid;
	
	/**
	 * @return the typeid
	 */
	public long getTypeid() {
		return typeid;
	}

	/**
	 * @param typeid
	 *            the typeid to set
	 */
	public void setTypeid(long typeid) {
		this.typeid = typeid;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * @return the entime
	 */
	public String getEntime() {
		return entime;
	}

	/**
	 * @param entime
	 *            the entime to set
	 */
	public void setEntime(String entime) {
		this.entime = entime;
	}

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
	 * @return the enname
	 */
	public String getEnname() {
		return enname;
	}

	/**
	 * @param enname
	 *            the enname to set
	 */
	public void setEnname(String enname) {
		this.enname = enname;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @return the partner
	 */
	public String getPartner() {
		return partner;
	}

	/**
	 * @param partner
	 *            the partner to set
	 */
	public void setPartner(String partner) {
		this.partner = partner;
	}

	/**
	 * @return the enpartner
	 */
	public String getEnpartner() {
		return enpartner;
	}

	/**
	 * @param enpartner
	 *            the enpartner to set
	 */
	public void setEnpartner(String enpartner) {
		this.enpartner = enpartner;
	}

	/**
	 * @return the sponsor
	 */
	public String getSponsor() {
		return sponsor;
	}

	/**
	 * @param sponsor
	 *            the sponsor to set
	 */
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	/**
	 * @return the ensponsor
	 */
	public String getEnsponsor() {
		return ensponsor;
	}

	/**
	 * @param ensponsor
	 *            the ensponsor to set
	 */
	public void setEnsponsor(String ensponsor) {
		this.ensponsor = ensponsor;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the enremark
	 */
	public String getEnremark() {
		return enremark;
	}

	/**
	 * @param enremark
	 *            the enremark to set
	 */
	public void setEnremark(String enremark) {
		this.enremark = enremark;
	}

	/**
	 * @return the isfav
	 */
	public int getIsfav() {
		return isfav;
	}

	/**
	 * @param isfav
	 *            the isfav to set
	 */
	public void setIsfav(int isfav) {
		this.isfav = isfav;
	}

	/**
	 * @return the closeflag
	 */
	public int getCloseflag() {
		return closeflag;
	}

	/**
	 * @param closeflag
	 *            the closeflag to set
	 */
	public void setCloseflag(int closeflag) {
		this.closeflag = closeflag;
	}

	/**
	 * @return the syncflag
	 */
	public int getSyncflag() {
		return syncflag;
	}

	/**
	 * @param syncflag
	 *            the syncflag to set
	 */
	public void setSyncflag(int syncflag) {
		this.syncflag = syncflag;
	}

	/**
	 * @return the feeflag
	 */
	public int getFeeflag() {
		return feeflag;
	}

	/**
	 * @param feeflag
	 *            the feeflag to set
	 */
	public void setFeeflag(int feeflag) {
		this.feeflag = feeflag;
	}

	/**
	 * @return the ispraise
	 */
	public int getIspraise() {
		return ispraise;
	}

	/**
	 * @param ispraise
	 *            the ispraise to set
	 */
	public void setIspraise(int ispraise) {
		this.ispraise = ispraise;
	}

	/**
	 * @return the praisecount
	 */
	public int getPraisecount() {
		return praisecount;
	}

	/**
	 * @param praisecount
	 *            the praisecount to set
	 */
	public void setPraisecount(int praisecount) {
		this.praisecount = praisecount;
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

	/**
	 * @return the issign
	 */
	public int getIssign() {
		return issign;
	}

	/**
	 * @param issign the issign to set
	 */
	public void setIssign(int issign) {
		this.issign = issign;
	}

	/**
	 * @return the keypoint
	 */
	public int getKeypoint() {
		return keypoint;
	}

	/**
	 * @param keypoint the keypoint to set
	 */
	public void setKeypoint(int keypoint) {
		this.keypoint = keypoint;
	}

	/**
	 * @return the batchid
	 */
	public Integer getBatchid() {
		return batchid;
	}

	/**
	 * @param batchid the batchid to set
	 */
	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}

}
