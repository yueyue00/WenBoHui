package com.gheng.exhibit.database.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.AsyncTask;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.response.ScheduleInfoData;
import com.gheng.exhibit.http.body.response.ScheduleInfoSupportData;
import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.model.databases.ScheduleInfo;
import com.gheng.exhibit.model.databases.Speakers;
import com.gheng.exhibit.utils.ApiUtil;
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
public class ScheduleInfoTask extends AsyncTask<Void, Void, ScheduleInfoData > {

	private CallBack<ScheduleInfoData> callBack;
	
	private long id;
	
	public ScheduleInfoTask(long id,CallBack<ScheduleInfoData> callBack){
		this.callBack = callBack;
		this.id = id;
	}
	
	@Override
	protected ScheduleInfoData doInBackground(Void... params) {
		if(callBack == null)
			return null;
		List<Speakers> speakers = new ArrayList<Speakers>();
		Selector selector = Selector.from(Schedule.class);
		selector.where("id", "=", id);
		ApiUtil.changeSelector(selector);
		ScheduleInfoData data = null;
		try {
			Schedule model = BaseActivity.getDbUtils().findFirst(selector);
			if(model != null){
				data = new ScheduleInfoData();
				data.address = (String) I18NUtils.getValue(model, "address");
				data.name = (String) I18NUtils.getValue(model, "name");
				data.partner = (String) I18NUtils.getValue(model, "partner");
				data.remark = (String) I18NUtils.getValue(model, "remark");
				data.sponsor = (String) I18NUtils.getValue(model, "sponsor");
				data.time = (String) I18NUtils.getValue(model, "time");
				data.date = model.getDate();
				data.startime = model.getStarttime();
				data.endtime = model.getEndtime();
				
				data.model = model;
				
				List<ScheduleInfoSupportData> rlist = new ArrayList<ScheduleInfoSupportData>();
				data.rdata = rlist;
				selector = Selector.from(ScheduleInfo.class);
				selector.where("scheduleid", "=", model.getId());
				ApiUtil.changeSelector(selector);
				
				//查询所有的会议详情
				List<ScheduleInfo> findAll = BaseActivity.getDbUtils().findAll(selector);
				//排序会议
				Collections.sort(findAll, new Comparator<ScheduleInfo>() {

					@Override
					public int compare(ScheduleInfo lhs, ScheduleInfo rhs) {
						if(lhs.getSorting() == rhs.getSorting()){
							return lhs.getTime().compareTo(rhs.getTime());
						}
						return lhs.getSorting() - rhs.getSorting();
					}
				});
				String lastHost = "";
				for (ScheduleInfo scheduleInfo : findAll) {
					//添加新主持人
					if(!lastHost.equals(scheduleInfo.getHost())){
						lastHost = scheduleInfo.getHost();
						ScheduleInfoSupportData d = new ScheduleInfoSupportData();
						d.name = lastHost;
						if(StringTools.isNotBlank(d.name)){
							if(d.name.endsWith(",")){
								d.name = d.name.substring(0, d.name.length()-1);
							}
							String[] split = d.name.split(",");
							StringBuffer sb = new StringBuffer();
							for (String s: split) {
								Speakers findById = findById(Long.valueOf(s), speakers);
								sb.append(I18NUtils.getV(findById, "name"));
								sb.append(",");
							}
							if(sb.length() > 0)
								sb.deleteCharAt(sb.length()-1);
							d.name = sb.toString();
							if(StringTools.isNotBlank(d.name)){
								d.name = BaseActivity.getLanguageString("主持人")+": "+d.name;
							}
						}
						d.type = 1;
						rlist.add(d);
					}
					
					ScheduleInfoSupportData d = new ScheduleInfoSupportData();
					d.time = (String) I18NUtils.getValue(scheduleInfo, "time");
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
			}
		} catch (DbException e) {
			e.printStackTrace();
			data = null;
		}
		
		return data;
		
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
	protected void onPostExecute(ScheduleInfoData result) {
		if(result == null){
			callBack.onFailure(new HttpException("数据库异常"), "数据库异常");
		}else {
			callBack.onSuccess(result);
		}
	}

}
