package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 演讲者
 * 
 * @author lileixing
 */
@Table(name = "Speakers")
public class Speakers extends BaseModel {

	@Column(column = "name")
	private String name;

	@Column(column = "enname")
	private String enname;

	@Column(column = "office")
	private String office;

	@Column(column = "enoffice")
	private String enoffice;

	@Column(column = "workplace")
	private String workplace;

	@Column(column = "enworkplace")
	private String enworkplace;

	@Column(column = "remark")
	private String remark;

	@Column(column = "enremark")
	private String enremark;

	@Column(column = "logo")
	private String logo;

	@Column(column = "sorting")
	private int sorting;

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
	 * @return the office
	 */
	public String getOffice() {
		return office;
	}

	/**
	 * @param office
	 *            the office to set
	 */
	public void setOffice(String office) {
		this.office = office;
	}

	/**
	 * @return the enoffice
	 */
	public String getEnoffice() {
		return enoffice;
	}

	/**
	 * @param enoffice
	 *            the enoffice to set
	 */
	public void setEnoffice(String enoffice) {
		this.enoffice = enoffice;
	}

	/**
	 * @return the workplace
	 */
	public String getWorkplace() {
		return workplace;
	}

	/**
	 * @param workplace
	 *            the workplace to set
	 */
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	/**
	 * @return the enworkplace
	 */
	public String getEnworkplace() {
		return enworkplace;
	}

	/**
	 * @param enworkplace
	 *            the enworkplace to set
	 */
	public void setEnworkplace(String enworkplace) {
		this.enworkplace = enworkplace;
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
