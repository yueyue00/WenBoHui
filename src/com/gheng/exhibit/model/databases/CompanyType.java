package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 展商类型
 * 
 * @author lileixing
 */
@Table(name = "CompanyType")
public class CompanyType extends BaseModel {

	@Column(column = "name")
	private String name;

	@Column(column = "enname")
	private String enname;
	
	@Column(column = "tuantype")
	private String tuantype;
	
	@Column(column = "typecount")
	private Integer typecount = 0;
	
	@Column(column = "sorting")
	private Integer sorting = 99999;
	
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

	public String getTuantype() {
		return tuantype;
	}

	public void setTuantype(String tuantype) {
		this.tuantype = tuantype;
	}

	public Integer getTypecount() {
		return typecount;
	}

	public void setTypecount(Integer typecount) {
		this.typecount = typecount;
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
