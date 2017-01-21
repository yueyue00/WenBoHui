package com.gheng.exhibit.http;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.gheng.exhibit.model.databases.OrderBy;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.DataBaseInfoTask;
import com.gheng.exhibit.utils.DataBaseTask;
import com.gheng.exhibit.utils.JsonUtils;
import com.gheng.exhibit.utils.LogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * Http请求封装成符合APP使用的
 * 
 * @author zhao
 */
public class HttpWrapper {

	private HttpUtils httpUtils = null;
	private final int TIMEOUT = 10000;

	public HttpWrapper() {
		httpUtils = new HttpUtils();
		httpUtils.configSoTimeout(TIMEOUT);
		httpUtils.configTimeout(TIMEOUT);
	}

	public <T> void post(BaseRequestData<?> requestData,
			final CallBack<T> callBack) {

		RequestParams param = new RequestParams(Constant.CHARSET);
		String s = JsonUtils.toJson(requestData);
		LogUtils.d("发送请求", s);
		StringEntity en = null;
		try {
			en = new StringEntity(s, Constant.CHARSET);
		} catch (UnsupportedEncodingException e) {
		}
		param.setBodyEntity(en);
		httpUtils.send(HttpMethod.POST, Constant.API_URL, param,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						callBack.onStart();
					}

					@Override
					public void onCancelled() {
						callBack.onCancelled();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						callBack.onLoading(total, current, isUploading);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						callBack.onFailure(error, msg);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("响应数据", responseInfo.result);
						try {
							Class<T> clazz = callBack.getGenericClass();
							T entity = JsonUtils.fromJson(responseInfo.result,
									clazz);
							LogUtils.d("model", entity.toString());
							callBack.onSuccess(entity);
						} catch (Exception e) {
							e.printStackTrace();
							callBack.onFailure(new HttpException(e), e.getMessage());
						}
					}
				});
	}
	
	public <T> void postToDataBase(final Selector selector,final int pageNo,final int pageSize,final List<OrderBy> orderList,final CallBack<T> callback){
		if(callback == null)
			return;
		new DataBaseTask<T>(selector, pageNo, pageSize, orderList, callback).execute();
	} 
	
	public <T> void postToDataBase(final Selector selector,final int pageNo,final int pageSize,final CallBack<T> callback){
		postToDataBase(selector, pageNo, pageSize, null, callback);
	} 
	
	public <T> void postToDataBase(final Selector selector,final int pageNo,final CallBack<T> callback){
		postToDataBase(selector, pageNo,Constant.PAGE_SIZE, null, callback);
	} 
	
	public <T> void postToDataBase(final Selector selector,final int pageNo,final List<OrderBy> orderList,final CallBack<T> callback){
		new DataBaseTask<T>(selector, pageNo, Constant.PAGE_SIZE, orderList, callback).execute();
	} 
	
	public <T> void getById(long id,final CallBack<T> callback){
		Selector selector = Selector.from(AppTools.getGenericClass(callback.getClass()));
		ApiUtil.changeSelector(selector);
		selector.and("id","=",id);
		new DataBaseInfoTask<T>(selector, callback).execute();
	}
	
	public <T> void getByCode(String floorName, String code,final CallBack<T> callback){
		Selector selector = Selector.from(AppTools.getGenericClass(callback.getClass()));
		ApiUtil.changeSelector(selector);
		selector.and("standreference","like","%" + floorName + "-" + code + "%");
		//selector.or(columnName, op, "%'"+code+"'%");
		//selector.and("zoneid","like","H7.2%");
		new DataBaseInfoTask<T>(selector, callback).execute();
	}
	
	/**
	 * @return the httpUtils
	 */
	public HttpUtils getHttpUtils() {
		return httpUtils;
	}

}
