package com.gheng.exhibit.database.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.AsyncTask;
import android.provider.ContactsContract.Contacts.Data;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.response.ScheduleInfoSupportData;
import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.model.databases.ScheduleInfo;
import com.gheng.exhibit.model.databases.Speakers;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.DateTools;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;

/**
 *	数据库查询任务
 * @author lileixing
 */
public class MineScheduleFavListTask extends AsyncTask<Void, Void, List<ScheduleInfoSupportData> > {

	private CallBack<List<ScheduleInfoSupportData> > callBack;
	
	public MineScheduleFavListTask(CallBack<List<ScheduleInfoSupportData> > callBack){
		this.callBack = callBack;
	}
	
	@Override
	protected List<ScheduleInfoSupportData>  doInBackground(Void... params) {
		if(callBack == null)
			return null;
		Selector selector = Selector.from(ScheduleInfo.class);
		ApiUtil.changeSelector(selector);
		selector.and("isfav", "=", 1);
		List<ScheduleInfo> scheduleList = null;
		try {
			scheduleList = BaseActivity.getDbUtils().findAll(selector);
		} catch (DbException e) {
			e.printStackTrace();
		}
		
		List<Long> scheduleIds = new ArrayList<Long>();
		if(scheduleList == null)
			return null;
		for (ScheduleInfo scheduleInfo : scheduleList) {
			scheduleIds.add(scheduleInfo.getScheduleid());
		}
		selector = Selector.from(Schedule.class);
		ApiUtil.changeSelector(selector);
		selector.and("id", "in", scheduleIds);
		
		List<Schedule> findAll = null;
		try {
			findAll = BaseActivity.getDbUtils().findAll(selector);
		} catch (DbException e) {
			e.printStackTrace();
		}
		for (ScheduleInfo scheduleInfo : scheduleList) {
			Schedule findScheduleById = findScheduleById(scheduleInfo.getScheduleid(), findAll);
			if(findScheduleById != null)
				scheduleInfo.schedule = findScheduleById;
		}
		Collections.sort(scheduleList, new Comparator<ScheduleInfo>() {

			@Override
			public int compare(ScheduleInfo lhs, ScheduleInfo rhs) {
				if(lhs.schedule != null && StringTools.isNotBlank(lhs.schedule.getDate()) 
						&& rhs.schedule != null && StringTools.isNotBlank(rhs.schedule.getDate())){
					int cp = lhs.schedule.getDate().compareTo(rhs.schedule.getDate());
					if(cp == 0){
						return lhs.getStarttime().compareTo(rhs.getStarttime());
					}else{
						return cp;
					}
				}
				return 0;
			}
		});
		List<Speakers> speakers = new ArrayList<Speakers>();
		List<ScheduleInfoSupportData> rlist = new ArrayList<ScheduleInfoSupportData>();
		
		for (ScheduleInfo scheduleInfo : scheduleList) {
			
			ScheduleInfoSupportData d = new ScheduleInfoSupportData();
			d.time = DateTools.formatNoYear(DateTools.parse(scheduleInfo.schedule.getDate(), "yyyy-MM-dd"))+" "+(String) I18NUtils.getValue(scheduleInfo, "time");
			d.name = (String) I18NUtils.getValue(scheduleInfo, "title");
			d.commentcount = scheduleInfo.getCommentcount();
			d.speakerid = scheduleInfo.getSpeakerid();
			d.isfav = scheduleInfo.getIsfav();
			d.ispraise = scheduleInfo.getIspraise();
			d.praisecount = scheduleInfo.getPraisecount();
			d.setId(scheduleInfo.getId());
			String speaker = "";
			String workplace = "";
			if(StringTools.isNotBlank(d.speakerid)){
				String[] split = d.speakerid.split(",");
				for (int i = 0 ; i < split.length; i ++) {
					if(StringTools.isBlank(split[i]))
						continue;
					long id = Long.valueOf(split[i]);
					Speakers findById = findById(id, speakers);
					if(findById != null){
						if(speaker.length() > 0){
							speaker += " , ";
							workplace += " , ";
						}
						speaker += I18NUtils.getValue(findById, "name");
						workplace += I18NUtils.getValue(findById, "workplace");
					}
				}
			}
			d.speaker = speaker;
			d.companyname = workplace;
			rlist.add(d);
		}
		return rlist;
	}
	
	//根据schedule查询scheduleId
	private Schedule findScheduleById(long id,List<Schedule> schedules){
		if(schedules == null)
			return null;
		for (Schedule schedule : schedules) {
			if(id == schedule.getId())
				return schedule;
		}
		return null;
	}
	
	private Speakers findById(long id,List<Speakers> list){
		Speakers model = null;
		for (Speakers speakers : list) {
			if(id == speakers.getId()){
				model = speakers;
				break;
			}
		}
		if(model == null){
			try {
				model = BaseActivity.getDbUtils().findById(Speakers.class, id);
				if(model != null){
					list.add(model);
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		return model;
	}
	
	@Override
	protected void onPostExecute(List<ScheduleInfoSupportData> result) {
		if(result == null){
			callBack.onFailure(new HttpException("数据库异常"), "数据库异常");
		}else {
			callBack.onSuccess(result);
		}
	}

}
