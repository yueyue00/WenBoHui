package com.gheng.exhibit.utils;

import android.os.AsyncTask;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;

/**
 *	数据库查询任务
 * @author lileixing
 */
public class DataBaseInfoTask<T> extends AsyncTask<Void, Void, T> {

	private Selector selector;
	
	private CallBack<T> callBack;
	
	public DataBaseInfoTask(final Selector selector,CallBack<T> callBack){
		this.selector = selector;
		this.callBack = callBack;
	}
	
	@Override
	protected T doInBackground(Void... params) {
		if(callBack == null)
			return null;
		try {
			return BaseActivity.getDbUtils().findFirst(selector);
		} catch (DbException e) {
			return null;
		}
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
