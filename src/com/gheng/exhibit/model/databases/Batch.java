package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 
 * @author lileixing
 */
@Table(name="batch")
public class Batch extends BaseModel {

	@Column(column = "name")
	private String name;

	@Column(column = "enname")
	private String enname;

	@Column(column = "name")
	private String startdatetxt;

	@Column(column = "enstartdatetxt")
	private String enstartdatetxt;

	@Column(column = "batchtimetxt")
	private String batchtimetxt;

	@Column(column = "enbatchtimetxt")
	private String enbatchtimetxt;
	
	@Column(column = "remark")
	private String remark;
	
	@Column(column = "enremark")
	private String enremark;
	
	@Column(column = "logo")
	private String logo;
	
	@Column(column = "enlogo")
	private String enlogo;
	
	@Column(column = "address")
	private String address;
	
	@Column(column = "enaddress")
	private String enaddress;
	
	@Column(column = "videourl")
	private String videourl;

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
	 * @return the startdatetxt
	 */
	public String getStartdatetxt() {
		return startdatetxt;
	}

	/**
	 * @param startdatetxt
	 *            the startdatetxt to set
	 */
	public void setStartdatetxt(String startdatetxt) {
		this.startdatetxt = startdatetxt;
	}

	/**
	 * @return the enstartdatetxt
	 */
	public String getEnstartdatetxt() {
		return enstartdatetxt;
	}

	/**
	 * @param enstartdatetxt
	 *            the enstartdatetxt to set
	 */
	public void setEnstartdatetxt(String enstartdatetxt) {
		this.enstartdatetxt = enstartdatetxt;
	}

	/**
	 * @return the batchtimetxt
	 */
	public String getBatchtimetxt() {
		return batchtimetxt;
	}

	/**
	 * @param batchtimetxt
	 *            the batchtimetxt to set
	 */
	public void setBatchtimetxt(String batchtimetxt) {
		this.batchtimetxt = batchtimetxt;
	}

	/**
	 * @return the enbatchtimetxt
	 */
	public String getEnbatchtimetxt() {
		return enbatchtimetxt;
	}

	/**
	 * @param enbatchtimetxt
	 *            the enbatchtimetxt to set
	 */
	public void setEnbatchtimetxt(String enbatchtimetxt) {
		this.enbatchtimetxt = enbatchtimetxt;
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
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
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
	 * @return the enaddress
	 */
	public String getEnaddress() {
		return enaddress;
	}

	/**
	 * @param enaddress
	 *            the enaddress to set
	 */
	public void setEnaddress(String enaddress) {
		this.enaddress = enaddress;
	}

	/**
	 * @return the videourl
	 */
	public String getVideourl() {
		return videourl;
	}

	/**
	 * @param videourl
	 *            the videourl to set
	 */
	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	/**
	 * @return the enlogo
	 */
	public String getEnlogo() {
		return enlogo;
	}

	/**
	 * @param enlogo the enlogo to set
	 */
	public void setEnlogo(String enlogo) {
		this.enlogo = enlogo;
	}

}
