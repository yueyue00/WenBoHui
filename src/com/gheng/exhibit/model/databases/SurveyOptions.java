package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

@Table(name = "SurveyOptions")
public class SurveyOptions extends BaseModel {

	@Column(column = "No")
	private String no;

	/**
	 * 问题选项
	 */
	@Column(column = "SurveyOptionCH")
	private String otitle;

	/**
	 * 问题英文选项
	 */
	@Column(column = "SurveyOptionEN")
	private String oentitle;

	@Column(column = "OptionType")
	private int otype;

	@Transient
	private String remark;

	@Transient
	private boolean isSelected;
	
	@Column(column = "SurveyQuestionID")
	private Integer qid;

	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}

	/**
	 * @param no
	 *            the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}

	/**
	 * @return the otitle
	 */
	public String getOtitle() {
		return otitle;
	}

	/**
	 * @param otitle
	 *            the otitle to set
	 */
	public void setOtitle(String otitle) {
		this.otitle = otitle;
	}

	/**
	 * @return the oentitle
	 */
	public String getOentitle() {
		return oentitle;
	}

	/**
	 * @param oentitle
	 *            the oentitle to set
	 */
	public void setOentitle(String oentitle) {
		this.oentitle = oentitle;
	}

	/**
	 * @return the otype
	 */
	public int getOtype() {
		return otype;
	}

	/**
	 * @param otype
	 *            the otype to set
	 */
	public void setOtype(int otype) {
		this.otype = otype;
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
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the qid
	 */
	public Integer getQid() {
		return qid;
	}

	/**
	 * @param qid the qid to set
	 */
	public void setQid(Integer qid) {
		this.qid = qid;
	}

}
