package com.gheng.exhibit.http.body.response;

import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.Objectives;
import com.gheng.exhibit.model.databases.Survey;
import com.gheng.exhibit.model.databases.SurveyOptions;
import com.gheng.exhibit.model.databases.SurveyQuestions;

/**
 *
 * @author lileixing
 */
public class SurveyUpdataBody {

	public PageBody<Survey> surveys;
	
	public PageBody<Objectives> objectives;
	
	public PageBody<SurveyQuestions> questions;
	
	public PageBody<SurveyOptions> options;
	
}
