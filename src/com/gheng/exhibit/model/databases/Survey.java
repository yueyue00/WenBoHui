package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 *
 * @author lileixing
 */
@Table(name = "survey")
public class Survey extends BaseModel{

	@Column(column = "name")
	private String name;
	
	@Column(column = "enname")
	private String enname;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
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
	 * @param enname the enname to set
	 */
	public void setEnname(String enname) {
		this.enname = enname;
	}
	
	
}
