package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.SharedData;

/**
 *
 * @author lileixing
 */
public class SpeakerListParam {
	
	public String eid = SharedData.getBatchId()+"";
	
	public String keyword = "";
	
	public String pno = 1+"";
	
}
