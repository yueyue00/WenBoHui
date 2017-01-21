package com.hebg3.wl.net;

import java.lang.reflect.Type;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.gheng.exhibit.utils.Constant;

/**
 * @author ghost
 * @version 创建时间：2013-7-11 上午10:40:35
 * @param <T>
 * 
 */
@SuppressWarnings("rawtypes")
public class NetSiXinTask<T> extends AsyncTask<Void, Void, Void> {
	// Message是在线程之间传递信息，它可以在内部携带少量的信息，用于在不同线程之间交换数据
	private Message msg;
	// params 是指在执行Task时需要传入的参数，可用于在后台任务中使用
	private ClientParams params;
	private Class clazz;
	private ResponseBody<T> body;
	private Type typeToken;
	public Object obj;
	private int isNull = 0;
	private Context context;

	/**
	 * 解析info中为对象的构造方法
	 * 
	 * @param msg
	 * @param params
	 * @param clazz
	 * @param obj
	 */
	public NetSiXinTask(Message msg, ClientParams params, Class clazz, Object obj,Context context) {// 解析json对象调用的构造方法
		this.context = context;
		this.msg = msg;
		this.params = params;
		this.clazz = clazz;
		this.obj = obj;
	}

	/**
	 * 解析info中为数组对象的构造方法
	 * 
	 * @param msg
	 * @param params
	 * @param typeToken
	 */
	public NetSiXinTask(Message msg, ClientParams params, Type typeToken,Context context) {// 解析数组json字符串调用的构造方法
		this.context = context;
		this.params = params;
		this.msg = msg;
		this.typeToken = typeToken;
	}

	/**
	 * 调用这个构造方法用于返回成功值 不需要解析 只需要判断是否返回成功值
	 * 
	 * @param msg
	 * @param params
	 * @param isNull
	 */
	public NetSiXinTask(Message msg, ClientParams params, int isNull,Context context) {
		this.context = context;
		this.msg = msg;
		this.isNull = isNull;
		this.params = params;
	}

	// @Override
	// protected void onPostExecute(ResponseBody result) {
	// System.out.println(msg+"111111111111"+result+"1111111111"+errorCode);
	// if (msg != null && result != null && errorCode == 1) {
	//
	// }
	// }

	@Override
	protected Void doInBackground(Void... arg0) {
		HttpUrlConnectionLoader con = new HttpUrlConnectionLoader();
//		System.out.println(params.http_method);
//		System.out.println(params.domain + params.url);
//		System.out.println(params.params);
		con.postDataFromSelf(params, listener,context);
		return null;
	}

	private OnEntityLoadCompleteListener<Base> listener = new OnEntityLoadCompleteListener<Base>() {

		@Override
		public void onError() {
			// Base entity = new Base();
			// if (body == null) {
			// body = new ResponseBody<T>();
			// }
			// body.base = entity;
//			System.out.println("网络不给力");
			msg.what = 1;
			msg.sendToTarget();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onEntityLoadComplete(Base entity) {
			try {
				//cookie失效返回500
				if (entity.code.equals("500")) {
					msg.what = 4;//返回 值为cookie失效
					msg.sendToTarget();
					return;
				}
				if (entity.code.equals("300")) {
					msg.what = 3;
					msg.sendToTarget();
					return;
				}
				if (isNull == 1 && entity.code.equals("200")) {
					msg.what = 0;
					msg.sendToTarget();
					return;
				} else if (!entity.code.equals("200")) {
					msg.what = 2;
					msg.obj = entity.message;
					msg.sendToTarget();
					return;
				}
				if (!entity.code.equals("200")) {
					msg.obj = entity.message;
					msg.what = 2;
					msg.sendToTarget();
					return;
				}
				
				body = new ResponseBody<T>();
				if (typeToken != null) {
					body.list = Constant.gson.fromJson(entity.info, typeToken);
					if (body.list.size() == 0) {
						msg.what = 3;
						msg.sendToTarget();
					} else {
						msg.obj = body;
						msg.sendToTarget();
					}
				} else {
					obj = (Object) Constant.gson.fromJson(entity.info, clazz);
					msg.obj = obj;
					if (entity.pagecount!=0000) {
						msg.arg1 = entity.pagecount;
					}
					msg.sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(Base entity) {
			// if (body == null) {
			// body = new ResponseBody<T>();
			// }
			// body.base = entity;
			msg.what = 1;
			msg.sendToTarget();
		}
	};
}
