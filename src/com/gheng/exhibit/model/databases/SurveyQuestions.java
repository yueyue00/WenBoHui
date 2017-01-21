package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "SurveyQuestions")
public class SurveyQuestions extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2718915711878982325L;

	/*
	 * 中文问题
	 */
	@Column(column = "SurveyQuestionCH")
	private String qtitle;

	@Column(column = "No")
	private String qno;

	/*
	 * 英文问题
	 */
	@Column(column = "SurveyQuestionEN")
	private String qentitle;

	@Column(column = "SurveyType")
	private Integer qtype;

	@Column(column = "ObjectiveID")
	private Integer soid;

	@Column(column = "qmax")
	private Integer qmax = 999;

	@Column(column = "qminscore")
	private Integer qminscore;

	@Column(column = "qmaxscore")
	private Integer qmaxscore;

	/**
	 * @return the qtitle
	 */
	public String getQtitle() {
		return qtitle;
	}

	/**
	 * @param qtitle
	 *            the qtitle to set
	 */
	public void setQtitle(String qtitle) {
		this.qtitle = qtitle;
	}

	/**
	 * @return the qno
	 */
	public String getQno() {
		return qno;
	}

	/**
	 * @param qno
	 *            the qno to set
	 */
	public void setQno(String qno) {
		this.qno = qno;
	}

	/**
	 * @return the qentitle
	 */
	public String getQentitle() {
		return qentitle;
	}

	/**
	 * @param qentitle
	 *            the qentitle to set
	 */
	public void setQentitle(String qentitle) {
		this.qentitle = qentitle;
	}

	/**
	 * @return the qtype
	 */
	public Integer getQtype() {
		return qtype;
	}

	/**
	 * @param qtype
	 *            the qtype to set
	 */
	public void setQtype(Integer qtype) {
		this.qtype = qtype;
	}

	/**
	 * @return the soid
	 */
	public Integer getSoid() {
		return soid;
	}

	/**
	 * @param soid
	 *            the soid to set
	 */
	public void setSoid(Integer soid) {
		this.soid = soid;
	}

	/**
	 * @return the qmax
	 */
	public Integer getQmax() {
		return qmax;
	}

	/**
	 * @param qmax
	 *            the qmax to set
	 */
	public void setQmax(Integer qmax) {
		this.qmax = qmax;
	}

	/**
	 * @return the qminscore
	 */
	public Integer getQminscore() {
		return qminscore;
	}

	/**
	 * @param qminscore
	 *            the qminscore to set
	 */
	public void setQminscore(Integer qminscore) {
		this.qminscore = qminscore;
	}

	/**
	 * @return the qmaxscore
	 */
	public Integer getQmaxscore() {
		return qmaxscore;
	}

	/**
	 * @param qmaxscore
	 *            the qmaxscore to set
	 */
	public void setQmaxscore(Integer qmaxscore) {
		this.qmaxscore = qmaxscore;
	}

}
