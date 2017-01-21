package com.gheng.exhibit.utils;

import java.util.List;

import android.os.AsyncTask;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.model.databases.DataBaseHelper;
import com.gheng.exhibit.model.databases.OrderBy;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.HttpException;

/**
 *	数据库查询任务
 * @author lileixing
 */
public class DataBaseTask<T> extends AsyncTask<Void, Void, T> {

	private Selector selector;
	
	private int pageSize;
	
	private int pageNo;
	
	private List<OrderBy> orderList;
	
	private CallBack<T> callBack;
	
	public DataBaseTask(final Selector selector,final int pageNo,final int pageSize,final List<OrderBy> orderList,CallBack<T> callBack){
		this.selector = selector;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.orderList = orderList;
		this.callBack = callBack;
	}
	
	@Override
	protected T doInBackground(Void... params) {
		if(callBack == null)
			return null;
		return (T) new DataBaseHelper(BaseActivity.getDbUtils()).pageQuery(selector, pageNo, pageSize, orderList);
		
	}
	
	@Override
	protected void onPostExecute(T result) {
		if(result == null){
			callBack.onFailure(new HttpException("数据库异常"), "数据库异常");
		}else {
			callBack.onSuccess(result);
		}
	}

}
