package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 区域会
 * 
 * @author lileixing
 */
@Table(name = "areabatch")
public class AreaBatch extends BaseModel {

	@Column(column = "code")
	private String code;

	@Column(column = "name")
	private String name;

	@Column(column = "enname")
	private String enname;

	@Column(column = "batch_id")
	private Integer batch_id;

	@Column(column = "survey_id")
	private Integer survey_id;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the batch_id
	 */
	public Integer getBatch_id() {
		return batch_id;
	}

	/**
	 * @param batch_id
	 *            the batch_id to set
	 */
	public void setBatch_id(Integer batch_id) {
		this.batch_id = batch_id;
	}

	/**
	 * @return the survey_id
	 */
	public Integer getSurvey_id() {
		return survey_id;
	}

	/**
	 * @param survey_id
	 *            the survey_id to set
	 */
	public void setSurvey_id(Integer survey_id) {
		this.survey_id = survey_id;
	}

}
