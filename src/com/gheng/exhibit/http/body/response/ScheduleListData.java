package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lileixing
 */
public class ScheduleListData {

	public String title;
	
	public List<ScheduleListSupportData> rlist =new ArrayList<ScheduleListSupportData>();
	
	public boolean isToday = false;//是否是今天
}
