package com.gheng.exhibit.model.databases;

import com.gheng.exhibit.model.databases.BaseModel;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 
 * @author lileixing
 */
@Table(name = "Product")
public class Product extends BaseModel {

	@Column(column = "name")
	private String name;

	@Column(column = "enname")
	private String enname;

	@Column(column = "typeid")
	private Long typeid;

	@Column(column = "companyid")
	private Long companyid;

	@Column(column = "remark")
	private String remark;

	@Column(column = "enremark")
	private String enremark;

	@Column(column = "logo")
	private String logo;

	@Column(column = "isfav")
	private int isfav;

	@Transient
	public Company company;

	@Column(column = "commentcount")
	private Integer commentcount;
	
	@Column(column = "searchtxt")
	private String searchtxt;
	
	@Column(column = "sorting")
	private Integer sorting;
	
	@Column(column = "batchid")
	private Integer batchid;

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
	 * @return the typeid
	 */
	public Long getTypeid() {
		return typeid;
	}

	/**
	 * @param typeid
	 *            the typeid to set
	 */
	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}

	/**
	 * @return the companyid
	 */
	public Long getCompanyid() {
		return companyid;
	}

	/**
	 * @param companyid
	 *            the companyid to set
	 */
	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
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

	public String getSearchtxt() {
		return searchtxt;
	}

	public void setSearchtxt(String searchtxt) {
		this.searchtxt = searchtxt;
	}

	public Integer getSorting() {
		return sorting;
	}

	public void setSorting(Integer sorting) {
		this.sorting = sorting;
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
