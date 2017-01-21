package com.gheng.exhibit.model.survey;

import java.util.ArrayList;
import java.util.List;

import com.gheng.exhibit.model.databases.SurveyOptions;

public class VoteSubmitItem
{

	public long itemId; // 标题栏之下的新闻内容id
	public String voteQuestion; // 调查问题
	public List<SurveyOptions> voteAnswers = new ArrayList<SurveyOptions>(); // 问卷调查选项
}
