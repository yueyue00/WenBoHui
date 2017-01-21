package com.gheng.exhibit.model.databases;

import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 
 * @author lileixing
 */
@Table(name = "ProductType")
public class ProductType extends BaseModel {

	@Column(column = "name")
	private String name;

	@Column(column = "enname")
	private String enname;

	@Column(column = "sorting")
	private int sorting = 99999;

	@Column(column = "pid")
	private Long pid;
	/**
	 * 数字
	 */
	@Column(column = "typecount")
	private int typecount = 0;

	@Column(column = "batchid")
	private Integer batchid;
	
	@Transient
	public List<ProductType> children = new ArrayList<ProductType>();	
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
	 * @return the pid
	 */
	public Long getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            the pid to set
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}

	public int getTypecount() {
		return typecount;
	}

	public void setTypecount(int typecount) {
		this.typecount = typecount;
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
