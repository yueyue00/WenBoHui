package com.gheng.exhibit.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.BaseResponse;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.HttpWrapper;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.http.body.request.ScheduleUpdateParam;
import com.gheng.exhibit.http.body.response.BatchUpdateBody;
import com.gheng.exhibit.http.body.response.CompanyUpdateBody;
import com.gheng.exhibit.http.body.response.ProductUpdateBody;
import com.gheng.exhibit.http.body.response.ScheduleInfoResult;
import com.gheng.exhibit.http.body.response.ScheduleResult;
import com.gheng.exhibit.http.body.response.ScheduleTypeResult;
import com.gheng.exhibit.http.body.response.ScheduleUpdateBody;
import com.gheng.exhibit.http.body.response.SpeakerResult;
import com.gheng.exhibit.http.body.response.SurveyUpdataBody;
import com.gheng.exhibit.http.response.BatchUpdateResponse;
import com.gheng.exhibit.http.response.CompanyUpdateResponse;
import com.gheng.exhibit.http.response.MessageResponse;
import com.gheng.exhibit.http.response.ProductUpdateResponse;
import com.gheng.exhibit.http.response.ScheduleUpdateResponse;
import com.gheng.exhibit.http.response.SurveyUpdateResponse;
import com.gheng.exhibit.model.databases.AreaBatch;
import com.gheng.exhibit.model.databases.Batch;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.CompanyType;
import com.gheng.exhibit.model.databases.Objectives;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.model.databases.ProductType;
import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.model.databases.ScheduleInfo;
import com.gheng.exhibit.model.databases.ScheduleType;
import com.gheng.exhibit.model.databases.Speakers;
import com.gheng.exhibit.model.databases.Survey;
import com.gheng.exhibit.model.databases.SurveyOptions;
import com.gheng.exhibit.model.databases.SurveyQuestions;
import com.gheng.exhibit.model.databases.TimeRecord;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.support.SearchData;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;

/**
 *	http请求工具
 * @author lileixing
 */
public class ApiUtil {

	/**
	 * 会议接口调用
	 */
	public static void invokeSchedule(final ApiCallBack apiCallBack){
		BaseRequestData<ScheduleUpdateParam> requestData = new BaseRequestData<ScheduleUpdateParam>("scheduleupdate");
		ScheduleUpdateParam param = new ScheduleUpdateParam();
		requestData.body = param;
		param.scheduleinfotime = getUpdateTime(TimeRecordType.SCHEDULE_INFO)+"";
		param.scheduletime = getUpdateTime(TimeRecordType.SCHEDULE)+"";
		param.scheduletypetime = getUpdateTime(TimeRecordType.SCHEDULE_TYPE)+"";
		param.speakertime = getUpdateTime(TimeRecordType.SPEAKER)+"";
		final int batchId = SharedData.getBatchId();
		new HttpWrapper().post(requestData, new CallBack<ScheduleUpdateResponse>() {
			@Override
			public void onSuccess(ScheduleUpdateResponse entity)  {
				ScheduleUpdateBody body = entity.body;
				//会议题目更新
				Map<TimeRecordType,Integer> map = new HashMap<TimeRecordType, Integer>();
				ScheduleResult schedule = body.schedule;
				if(schedule != null && AppTools.isNotBlack(schedule.rdata)){
					for (Schedule model : schedule.rdata) {
						model.setBatchid(batchId);
					}
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(schedule.rdata);
						map.put(TimeRecordType.SCHEDULE, schedule.rdata.size());
						setUpdateTime(TimeRecordType.SCHEDULE, schedule.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				
				//会议分类更新
				ScheduleTypeResult scheduleType = body.scheduletype;
				if(scheduleType != null && AppTools.isNotBlack(scheduleType.rdata)){
					for (ScheduleType model : scheduleType.rdata) {
						model.setBatchid(batchId);
					}
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(scheduleType.rdata);
						map.put(TimeRecordType.SCHEDULE_TYPE, scheduleType.rdata.size());
						setUpdateTime(TimeRecordType.SCHEDULE_TYPE, scheduleType.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				
				//演讲题目更新
				ScheduleInfoResult scheduleInfo = body.scheduleinfo;
				if(scheduleInfo != null && AppTools.isNotBlack(scheduleInfo.rdata)){
					for (ScheduleInfo model : scheduleInfo.rdata) {
						model.setBatchid(batchId);
					}
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(scheduleInfo.rdata);
						map.put(TimeRecordType.SCHEDULE_INFO, scheduleInfo.rdata.size());
						setUpdateTime(TimeRecordType.SCHEDULE_INFO, scheduleInfo.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				
				//演讲人更新
				SpeakerResult speaker = body.speaker;
				if(speaker != null && AppTools.isNotBlack(speaker.rdata)){
					for (Speakers model : speaker.rdata) {
						model.setBatchid(batchId);
					}
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(speaker.rdata);
						map.put(TimeRecordType.SPEAKER, speaker.rdata.size());
						setUpdateTime(TimeRecordType.SPEAKER, speaker.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				if(apiCallBack != null){
					apiCallBack.callback(map);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				apiCallBack.callback(null);
			}
		});
	}
	
	public static void invokeCompany(final ApiCallBack apiCallBack){
		final int batchId = SharedData.getBatchId();
		BaseRequestData<Map<String,String>> requestData = new BaseRequestData<Map<String,String>>("companyupdate");
		Map<String,String> param = new HashMap<String, String>();
		param.put("typetime", getUpdateTime(TimeRecordType.COMPANY_TYPE)+"");
		param.put("companytime", getUpdateTime(TimeRecordType.COMPANY_INFO)+"");
		param.put("eid", batchId+"");
		requestData.body = param;
		new HttpWrapper().post(requestData, new CallBack<CompanyUpdateResponse>() {

			@Override
			public void onSuccess(CompanyUpdateResponse entity) {
				CompanyUpdateBody body = entity.body;
				//展商分类更新
				Map<TimeRecordType,Integer> map = new HashMap<TimeRecordType, Integer>();
				PageBody<CompanyType> companytype = body.companytype;
				if(companytype != null && AppTools.isNotBlack(companytype.rdata)){
					for (CompanyType type : companytype.rdata) {
						type.setBatchid(batchId);
					}
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(companytype.rdata);
						map.put(TimeRecordType.COMPANY_TYPE, companytype.rdata.size());
						setUpdateTime(TimeRecordType.COMPANY_TYPE, companytype.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				
				//展商更新
				PageBody<Company> company = body.company;
				if(company != null && AppTools.isNotBlack(company.rdata)){
					for (Company model : company.rdata) {
						model.setBatchid(batchId);
					}
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(company.rdata);
						map.put(TimeRecordType.COMPANY_INFO, company.rdata.size());
						setUpdateTime(TimeRecordType.COMPANY_INFO, company.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				if(apiCallBack != null){
					apiCallBack.callback(map);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				apiCallBack.callback(null);
			}
		});
	}
	
	//更新产品
	public static void invokeProduct(final ApiCallBack apiCallBack){
		final int batchId = SharedData.getBatchId();
		BaseRequestData<Map<String,String>> requestData = new BaseRequestData<Map<String,String>>("productupdate");
		Map<String,String> param = new HashMap<String, String>();
		param.put("typetime", getUpdateTime(TimeRecordType.PRODUCT_TYPE)+"");
		param.put("producttime", getUpdateTime(TimeRecordType.PRODUCT_INFO)+"");
		param.put("eid", batchId+"");
		requestData.body = param;
		new HttpWrapper().post(requestData, new CallBack<ProductUpdateResponse>() {

			@Override
			public void onSuccess(ProductUpdateResponse entity) {
				ProductUpdateBody body = entity.body;
				//产品分类更新
				Map<TimeRecordType,Integer> map = new HashMap<TimeRecordType, Integer>();
				PageBody<ProductType> producttype = body.producttype;
				if(producttype != null && AppTools.isNotBlack(producttype.rdata)){
					for (ProductType model : producttype.rdata) {
						model.setBatchid(batchId);
					}
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(producttype.rdata);
						map.put(TimeRecordType.PRODUCT_TYPE, producttype.rdata.size());
						setUpdateTime(TimeRecordType.PRODUCT_TYPE, producttype.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				
				//产品更新
				PageBody<Product> product = body.product;
				if(product != null && AppTools.isNotBlack(product.rdata)){
					for (Product model : product.rdata) {
						model.setBatchid(batchId);
					}
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(product.rdata);
						map.put(TimeRecordType.PRODUCT_INFO, product.rdata.size());
						setUpdateTime(TimeRecordType.PRODUCT_INFO, product.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				if(apiCallBack != null){
					apiCallBack.callback(map);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				apiCallBack.callback(null);
			}
		});
	}
	/**
	 * 获取消息
	 * @param apiCallBack
	 */
	public static void invokeMessage(final ApiCallBack apiCallBack){
		BaseRequestData<Map<String,String>> requestData = new BaseRequestData<Map<String,String>>("getlastmessage");
		Map<String,String> param = new HashMap<String, String>();
		param.put("eid", SharedData.getBatchId()+"");
		requestData.body = param;
		new HttpWrapper().post(requestData, new CallBack<MessageResponse>() {
			@Override
			public void onSuccess(MessageResponse entity) {
				if(apiCallBack != null && entity.body.id != null) {
					apiCallBack.callback(true, Long.valueOf(entity.body.id), 1, 1, entity.body);
				}else{
					apiCallBack.callback(false,0,1,1,entity.body);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				apiCallBack.callback(null);
			}
		});
	}
	/**
	 * 展会更新
	 */
	public static void invokeBatch(final ApiCallBack apiCallBack){
		BaseRequestData<Map<String,String>> requestData = new BaseRequestData<Map<String,String>>("batchupdate");
		Map<String,String> param = new HashMap<String, String>();
		param.put("eid", SharedData.getBatchId()+"");
		param.put("batchtime", getUpdateTime(TimeRecordType.BATCH)+"");
		param.put("areatime", getUpdateTime(TimeRecordType.AEREBATCH)+"");
		requestData.body = param;
		final int batchId = -1;
		new HttpWrapper().post(requestData, new CallBack<BatchUpdateResponse>() {
			@Override
			public void onSuccess(BatchUpdateResponse entity) {
				BatchUpdateBody body = entity.body;
				Map<TimeRecordType,Integer> map = new HashMap<TimeRecordType, Integer>();
				
				//展会更新
				PageBody<Batch> batch = body.batch;
				if(batch != null && AppTools.isNotBlack(batch.rdata)){
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(batch.rdata);
						map.put(TimeRecordType.BATCH, batch.rdata.size());
						setUpdateTime(TimeRecordType.BATCH, batch.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				//区域会更新
				PageBody<AreaBatch> areabatch = body.areabatch;
				if(areabatch != null && AppTools.isNotBlack(areabatch.rdata)){
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(areabatch.rdata);
						map.put(TimeRecordType.AEREBATCH, areabatch.rdata.size());
						setUpdateTime(TimeRecordType.AEREBATCH, areabatch.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				if(apiCallBack != null){
					apiCallBack.callback(map);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if(apiCallBack != null)
					apiCallBack.callback(null);
			}
		});
	}
	/**
	 * 问卷调查数据更新
	 */
	public static void invokeSurveyUpdate(final ApiCallBack apiCallBack){
		final int batchId = SharedData.getBatchId();
		BaseRequestData<Map<String,String>> requestData = new BaseRequestData<Map<String,String>>("surveyupdate");
		Map<String,String> param = new HashMap<String, String>();
		param.put("eid", batchId+"");
		param.put("surveytime", getUpdateTime(TimeRecordType.SURVEY)+"");
		param.put("objectivetime", getUpdateTime(TimeRecordType.OBJECTIVE)+"");
		param.put("questiontime", getUpdateTime(TimeRecordType.QUESTION)+"");
		param.put("optiontime", getUpdateTime(TimeRecordType.OPTION)+"");
		requestData.body = param;
		new HttpWrapper().post(requestData, new CallBack<SurveyUpdateResponse>() {
			@Override
			public void onSuccess(SurveyUpdateResponse entity) {
				SurveyUpdataBody body = entity.body;
				Map<TimeRecordType,Integer> map = new HashMap<TimeRecordType, Integer>();
				final int batchId = SharedData.getBatchId();
				//survey更新
				PageBody<Survey> surveys = body.surveys;
				if(surveys != null && AppTools.isNotBlack(surveys.rdata)){
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(surveys.rdata);
						map.put(TimeRecordType.SURVEY, surveys.rdata.size());
						setUpdateTime(TimeRecordType.SURVEY, surveys.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				//更新Objective
				PageBody<Objectives> objectives = body.objectives;
				if(objectives != null && AppTools.isNotBlack(objectives.rdata)){
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(objectives.rdata);
						map.put(TimeRecordType.OBJECTIVE, objectives.rdata.size());
						setUpdateTime(TimeRecordType.OBJECTIVE, objectives.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				//更新question
				PageBody<SurveyQuestions> questions = body.questions;
				if(questions != null && AppTools.isNotBlack(questions.rdata)){
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(questions.rdata);
						map.put(TimeRecordType.QUESTION, questions.rdata.size());
						setUpdateTime(TimeRecordType.QUESTION, questions.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				//更新option
				PageBody<SurveyOptions> options = body.options;
				if(options != null && AppTools.isNotBlack(options.rdata)){
					try {
						BaseActivity.getDbUtils().saveOrUpdateAll(options.rdata);
						map.put(TimeRecordType.OPTION, options.rdata.size());
						setUpdateTime(TimeRecordType.OPTION, options.rtime,batchId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				if(apiCallBack != null){
					apiCallBack.callback(map);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if(apiCallBack != null)
					apiCallBack.callback(null);
			}
		});
	}
	
	/**
	 * @param browsetype
	 * 		是列表还是详情
	 * @param objid
	 * 		详情对象ID
	 * @param type
	 * 		浏览对象的类型
	 * @param searchtype
	 * 		触发日志上传的类型  是进入页面自动触发 还是点击搜索触发
	 * @param content
	 * 		搜索内容
	 * 
	 */
	public static void postBrowseLog(int browsetype,long objid,int type,int searchtype,SearchData data){
		BaseRequestData<Map<String,Object>> requestData = new BaseRequestData<Map<String,Object>>("browselog");
		Map<String,Object> body = new HashMap<String, Object>();
		requestData.body = body;
		body.put("browsetype", browsetype+"");
		body.put("objid", objid+"");
		body.put("type", type+"");
		body.put("searchtype", searchtype+"");
		body.put("content", data);
		body.put("eid", SharedData.getBatchId()+"");
		new HttpWrapper().post(requestData, new CallBack<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse entity) {
			}
			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}
	
	/**
	 * 获取最后更新时间
	 */
	public static long getUpdateTime(TimeRecordType type){
		DbUtils dbUtils = BaseActivity.getDbUtils();
		Selector selector = Selector.from(TimeRecord.class);
		ApiUtil.changeSelector(selector);
		selector.where("name", "=", type.toString());
		long result = 0;
		try {
			TimeRecord findFirst = dbUtils.findFirst(selector);
			if(findFirst != null){
				result = findFirst.getTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Long.MAX_VALUE;
		}
		return result;
	}
	/**
	 * 设置最后的更新时间
	 */
	public static void setUpdateTime(TimeRecordType type,long time,int batchId){
		Selector selector = Selector.from(TimeRecord.class);
		ApiUtil.changeSelector(selector);
		selector.where("name", "=", type.toString());
		try {
			TimeRecord findFirst = BaseActivity.getDbUtils().findFirst(selector);
			if(findFirst == null){
				SqlInfo sqlInfo = new SqlInfo("select max(id) as maxid from timerecord");
				DbModel findMax = BaseActivity.getDbUtils().findDbModelFirst(sqlInfo);
				long maxId = findMax.getLong("maxid");
				long id = maxId + 1;
				findFirst = new TimeRecord();
				findFirst.setId(id);
				findFirst.setName(type.toString());
				findFirst.setBatchid(batchId);
			}
			findFirst.setState(1);
			findFirst.setTime(time);
			BaseActivity.getDbUtils().saveOrUpdate(findFirst);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 点赞类型
	 * 	id:对象ID
	 *  type:1:展商 2:展品 3:会议  5会议详情
	 *  mode:0:取消 1:点赞
	 *  action: memberpraise memberfav
	 *  content:评论内容
	 *  score:评论打分
	 */
	private static void post(final long id,final int type,final int mode,Map<String,String> data,final String action
			,final ApiCallBack callback){
		if(callback == null)
			return;
		BaseRequestData<Map<String,String>> requestData = new BaseRequestData<Map<String,String>>(action);
		Map<String,String> body = new HashMap<String, String>();
		body.put("id", id+"");
		body.put("snum", SharedData.getString("snum"));
		body.put("type", type+"");
		body.put("mode", mode+"");
		if(data != null)
			body.putAll(data);
		requestData.body = body;
		
		new HttpWrapper().post(requestData, new CallBack<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse entity) {
				System.out.println("onSuccess  onSuccess");
				boolean ex = false;
				switch (type) {
				case Constant.TYPE_COMPANY:
					try {
						Company company = BaseActivity.getDbUtils().findById(Company.class, id);
						if(company != null){
							company.setIsfav(mode);
						}
						BaseActivity.getDbUtils().saveOrUpdate(company);
					} catch (DbException e1) {
						e1.printStackTrace();
						ex = true;
						callback.callback(false, id, type,mode, null);
					}
					if(ex)
						return;
					if(entity.retcode == 200){
						callback.callback(true, id, type,mode, null);
					}else{
						callback.callback(false, id, type,mode, null);
					}
					break;
				case Constant.TYPE_PRODUCT:
					try {
						Product product = BaseActivity.getDbUtils().findById(Product.class, id);
						if(product != null){
							product.setIsfav(mode);
						}
						BaseActivity.getDbUtils().saveOrUpdate(product);
					} catch (DbException e1) {
						e1.printStackTrace();
						ex = true;
						callback.callback(false, id, type,mode, null);
					}
					if(ex)
						return;
					if(entity.retcode == 200){
						callback.callback(true, id, type,mode, null);
					}else{
						callback.callback(false, id, type,mode, null);
					}
					break;
				case Constant.TYPE_SCHEDULE:
					try {
						Schedule findById = BaseActivity.getDbUtils().findById(Schedule.class, id);
						if(findById != null){
							if("memberpraise".equals(action)){
								findById.setIspraise(mode);
								int praisecount = findById.getPraisecount();
								if(mode == 1){
									praisecount ++;
								}else{
									praisecount = praisecount > 0 ? praisecount - 1 : praisecount;
								}
								findById.setPraisecount(praisecount);
							}else if("memberfav".equals(action)){
								findById.setIsfav(mode);
							}else if("membersign".equals(action)){
								findById.setIssign(mode);
							}
							BaseActivity.getDbUtils().saveOrUpdate(findById);
						}
					} catch (DbException e) {
						ex = true;
						e.printStackTrace();
						callback.callback(false, id, type,mode, null);
					}
					if(ex)
						return;
					if(entity.retcode == 200){
						callback.callback(true, id, type,mode, null);
					}else{
						callback.callback(false, id, type,mode, null);
					}
					break;
				case Constant.TYPE_SCHEDULE_INFO:
					try {
						ScheduleInfo findById = BaseActivity.getDbUtils().findById(ScheduleInfo.class, id);
						if(findById != null) {
							if("memberpraise".equals(action)){
								findById.setIspraise(mode);
								int praisecount = findById.getPraisecount();
								if(mode == 1){
									praisecount ++;
								}else{
									praisecount = praisecount > 0 ? praisecount - 1 : praisecount;
								}
								findById.setPraisecount(praisecount);
							}else if("memberfav".equals(action)){
								findById.setIsfav(mode);
							}
							BaseActivity.getDbUtils().saveOrUpdate(findById);
						}
					} catch (DbException e) {
						ex = true;
						e.printStackTrace();
						callback.callback(false, id, type,mode, null);
					}
					if(ex)
						return;
					if(entity.retcode == 200){
						callback.callback(true, id, type,mode, null);
					}else{
						callback.callback(false, id, type,mode, null);
					}
					break;
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				callback.callback(false, id, type,mode, null);
			}
		});
	}
	
	public static void postZan(final long id,final int type,int mode
			,final ApiCallBack callback){
		post(id, type, mode, null, "memberpraise", callback);
	} 
	
	public static void postFav(final long id,final int type,int mode
			,final ApiCallBack callback){
		post(id, type, mode, null, "memberfav", callback);
	} 
	
	public static void postSign(final long id,int mode,Map<String,String> data,
			final ApiCallBack callback){
		post(id, Constant.TYPE_SCHEDULE, mode, data, "membersign", callback);
	} 
	
	public static void changeSelector(Selector selector){
		WhereBuilder where = WhereBuilder.b("state", "=", 1);
//		where.or("state", "=", null);
		try {
			selector.and(where);
		} catch (Exception e) {
			selector.where(where);
		}
		Class<?> entityType = selector.getEntityType();
		try {
			Field field = entityType.getDeclaredField("batchid");
			if(field != null){
				WhereBuilder w = WhereBuilder.b("batchid", "=", -1);
				w.or("batchid", "=", SharedData.getBatchId());
				selector.and(w);
			}
		} catch (NoSuchFieldException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static abstract class ApiCallBack{
		//回调哪个类型更新了多少条记录
		public abstract void callback(Map<TimeRecordType, Integer> map);
		
		/**
		 * 点赞 评论 收藏的回调方法
		 * 	 id:对象ID
		 *   type:1:展商 2:展品 3:会议
		 *   success:是否成功
		 *   data:附加信息
		 */
		public void callback(boolean success,long id,int type,int mode,Object data){};
	}
	
}
