package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "Objectives")
public class Objectives extends BaseModel {
	/*
	 * 问卷中文名
	 */
	@Column(column = "ObjectiveCH")
	private String name;

	/*
	 * 问卷英文名
	 */
	@Column(column = "ObjectiveEN")
	private String enname;

	@Column(column = "surveyid")
	private Integer surveyid;

	@Column(column = "sorting")
	private int sorting;

	/**
	 * @return the surveyId
	 */
	public Integer getSurveyid() {
		return surveyid;
	}

	/**
	 * @param surveyId
	 *            the surveyId to set
	 */
	public void setSurveyid(Integer surveyid) {
		this.surveyid = surveyid;
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
