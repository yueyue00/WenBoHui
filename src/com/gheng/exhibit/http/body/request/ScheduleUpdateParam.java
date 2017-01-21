package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.SharedData;

/**
 *
 * @author lileixing
 */
public class ScheduleUpdateParam {
	
	public String speakertime;
	
	public String scheduletime;
	
	public String scheduletypetime;
	
	public String scheduleinfotime;
	
	public String eid =SharedData.getBatchId()+"";
	
}
