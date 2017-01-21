package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "SurveyAnswers")
public class SurveyAnswers extends BaseModel
{

	/**
	 * 问题
	 */
	@Column(column = "SurveyQuestionID")
	private long surveyQuestionID;

	/**
	 * 答案
	 */
	@Column(column = "SurveyOptionID")
	private String surveyOptionID;

	/**
	 * 答案补充
	 */
	@Column(column = "SurveyAnswerText")
	private String SurveyAnswerText;

	public long getSurveyQuestionID()
	{
		return surveyQuestionID;
	}

	public void setSurveyQuestionID(long surveyQuestionID)
	{
		this.surveyQuestionID = surveyQuestionID;
	}

	public String getSurveyOptionID()
	{
		return surveyOptionID;
	}

	public void setSurveyOptionID(String surveyOptionID)
	{
		this.surveyOptionID = surveyOptionID;
	}

	public String getSurveyAnswerText()
	{
		return SurveyAnswerText;
	}

	public void setSurveyAnswerText(String surveyAnswerText)
	{
		SurveyAnswerText = surveyAnswerText;
	}

}
