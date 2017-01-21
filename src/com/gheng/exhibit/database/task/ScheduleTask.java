package com.gheng.exhibit.database.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.response.ScheduleListData;
import com.gheng.exhibit.http.body.response.ScheduleListSupportData;
import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.model.databases.ScheduleType;
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
public class ScheduleTask extends AsyncTask<Void, Void, List<ScheduleListData> > {

	private CallBack<List<ScheduleListData>> callBack;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private boolean isMine = false;
	
	private Selector tSelect;
	
	public ScheduleTask(boolean isMine,Selector selector,CallBack<List<ScheduleListData>> callBack){
		this.callBack = callBack;
		this.isMine = isMine;
		tSelect = selector;
	}
	
	@Override
	protected List<ScheduleListData> doInBackground(Void... params) {
		if(callBack == null)
			return null;
		Selector selector = null;
		if(tSelect == null){
			selector = Selector.from(Schedule.class);
		}else{
			selector = tSelect;
		}
		if(isMine){
			selector.where("issign", "=", 1);
		}
		ApiUtil.changeSelector(selector);
		List<ScheduleListData> result = new ArrayList<ScheduleListData>();
		try {
			List<Schedule> scheduleList = BaseActivity.getDbUtils().findAll(selector);
			selector = Selector.from(ScheduleType.class);
			ApiUtil.changeSelector(selector);
			List<ScheduleType> typeList = BaseActivity.getDbUtils().findAll(selector);
			List<Date> dateList = new ArrayList<Date>();
			Map<String, List<Schedule>> map = new HashMap<String, List<Schedule>>();
			for (Schedule model : scheduleList) {
				String date = model.getDate();
				if(StringTools.isNotBlank(date)){
					List<Schedule> list = map.get(date);
					if(list == null){
						try {
							dateList.add(dateFormat.parse(date));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						list = new ArrayList<Schedule>();
						map.put(date, list);
					}
					list.add(model);
				}
			}
			//日期排序
			Collections.sort(dateList, new Comparator<Date>() {
				@Override
				public int compare(Date lhs, Date rhs) {
					return lhs.compareTo(rhs);
				}
			});
			String today = DateTools.format(new Date(System.currentTimeMillis()));
			for (Date date : dateList) {
				ScheduleListData data = new ScheduleListData();
				result.add(data);
				String key = dateFormat.format(date);
				if(key.equals(today)){
					data.isToday = true;
				}
				data.title = DateTools.formatNoYear(date)+" "+DateTools.formatWeek(date);
				List<ScheduleListSupportData> rlist = new ArrayList<ScheduleListSupportData>();
				data.rlist = rlist;
				
				List<Schedule> list = map.get(key);
				for (Schedule schedule : list) {
					ScheduleListSupportData d = new ScheduleListSupportData();
					d.address = (String) I18NUtils.getValue(schedule, "address");
					d.id = schedule.getId();
					d.name = (String) I18NUtils.getValue(schedule, "name");
					d.time = (String) I18NUtils.getValue(schedule, "time");
					d.closeflag = schedule.getCloseflag();
					d.feeflag = schedule.getFeeflag();
					d.syncflag = schedule.getSyncflag();
					d.isfav = schedule.getIsfav();
					d.ispraise = schedule.getIspraise();
					d.issign = schedule.getIssign();
					d.praisecount = schedule.getPraisecount();
//					d.type = (Integer) I18NUtils.getValue(schedule, "type");
					ScheduleType findById = findById(schedule.getTypeid(),typeList);
					if(findById != null){
						d.typelogo = (String) I18NUtils.getValue(findById, "logo");
					}
					rlist.add(d);
				}
				//会议名称排序
				Collections.sort(rlist, new Comparator<ScheduleListSupportData>() {
					@Override
					public int compare(ScheduleListSupportData lhs, ScheduleListSupportData rhs) {
						if(StringTools.isBlank(lhs.time) || StringTools.isBlank(rhs.time))
							return 0;
						return lhs.time.compareTo(rhs.time);
					}
				});
			}
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return result;
		
	}
	
	private ScheduleType findById(long id,List<ScheduleType> list){
		for (ScheduleType scheduleType : list) {
			if(id == scheduleType.getId()){
				return scheduleType;
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(List<ScheduleListData> result) {
		if(result == null){
			callBack.onFailure(new HttpException("数据库异常"), "数据库异常");
		}else {
			callBack.onSuccess(result);
		}
	}

}
