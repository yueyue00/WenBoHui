package com.hebg3.wl.net;

import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
public class NetTask<T> extends AsyncTask<Void, Void, Void> {
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
	private int cache = 0;// 判断是否缓存json 0为不缓存，1为缓存大会新闻栏目，2位缓存大会新闻栏目下的内容，3为缓存大会日程
	public String newslanmucachename = "xinwenlanmu";
	public String newsliebiaocachename = "xinwenliebiao";
	public String vipxingchengname = "vipxingcheng";
	public String shouyelunboname = "shouyelunbo";
	public String jiudianxinxiname = "jiudianxinxi";
	public String zhuanshufuwuname = "zhuanshufuwu";
	public SharedPreferences sp;
	public Editor e;
	public String cat_id;
	public int pagenum;

	/**
	 * 解析info中为数组对象的构造方法，并返回数据
	 * 
	 * @param msg
	 * @param params
	 * @param typeToken
	 */
	public NetTask(Message msg, ClientParams params, Type typeToken,
			Context context) {// 解析数组json字符串调用的构造方法
		this.context = context;
		this.params = params;
		this.msg = msg;
		this.typeToken = typeToken;
	}

	/**
	 * 调用这个构造方法用于返回成功值 《没有数据，不需要解析》 只需要判断是否返回成功值
	 * 
	 * @param msg
	 * @param params
	 * @param isNull
	 */
	public NetTask(Message msg, ClientParams params, int isNull, Context context) {
		this.context = context;
		this.msg = msg;
		this.isNull = isNull;
		this.params = params;
	}

	/**
	 * 解析info中为对象的构造方法，并返回数据
	 * 
	 * @param msg
	 * @param params
	 * @param clazz
	 * @param obj
	 */
	public NetTask(Message msg, ClientParams params, Class clazz, Object obj,
			Context context) {// 解析json对象调用的构造方法
		this.context = context;
		this.msg = msg;
		this.params = params;
		this.clazz = clazz;
		this.obj = obj;
	}

	/**
	 * 解析info中为数组对象的构造方法,传递cache代表改接口数据需要缓存.《获取所有数据》
	 * 
	 * @param msg
	 * @param params
	 * @param typeToken
	 */
	public NetTask(Message msg, ClientParams params, Type typeToken,
			Context context, int cache) {// 解析数组json字符串调用的构造方法
		this.context = context;
		this.params = params;
		this.msg = msg;
		this.typeToken = typeToken;
		this.cache = cache;
		if (cache == 1) {// 缓存大会新闻
			sp = context.getSharedPreferences(newslanmucachename,
					Activity.MODE_PRIVATE);
			e = sp.edit();
		} else if (cache == 3) {// 缓存vip行程
			sp = context.getSharedPreferences(vipxingchengname,
					Activity.MODE_PRIVATE);
			e = sp.edit();
		} else if (cache == 4) {// 缓存首页轮播图
			sp = context.getSharedPreferences(shouyelunboname,
					Activity.MODE_PRIVATE);
			e = sp.edit();
		} else if (cache == 6) {// 缓存专属服务
			sp = context.getSharedPreferences(zhuanshufuwuname,
					Activity.MODE_PRIVATE);
			e = sp.edit();
		} else if (cache == 5) {
			sp = context.getSharedPreferences(jiudianxinxiname,
					Activity.MODE_PRIVATE);
			e = sp.edit();
		}
	}

	/**
	 * 解析info中为对象的构造方法《带有pagenum的参数》
	 * 
	 * @param msg
	 * @param params
	 * @param clazz
	 * @param obj
	 */
	public NetTask(Message msg, ClientParams params, Class clazz, Object obj,
			Context context, int pagenum) {// 解析json对象调用的构造方法
		this.context = context;
		this.msg = msg;
		this.params = params;
		this.clazz = clazz;
		this.obj = obj;
		this.pagenum = pagenum;
		if (pagenum == 5) {
			sp = context.getSharedPreferences(jiudianxinxiname,
					Activity.MODE_PRIVATE);
			e = sp.edit();
		}
	}

	/**
	 * 解析info中为对象的构造方法，需要《缓存序号，cat_id，页数》参数。
	 * 
	 * @param msg
	 * @param params
	 * @param clazz
	 * @param obj
	 */
	public NetTask(Message msg, ClientParams params, Class clazz, Object obj,
			Context context, int cache, String cat_id, int pagenum) {// 解析json对象调用的构造方法
		this.context = context;
		this.msg = msg;
		this.params = params;
		this.clazz = clazz;
		this.obj = obj;
		this.cache = cache;
		this.cat_id = cat_id;
		this.pagenum = pagenum;
		if (cache == 2) {
			sp = context.getSharedPreferences(newsliebiaocachename,
					Activity.MODE_PRIVATE);
			e = sp.edit();
		}
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		System.out.println("aaa:NetTask:doInBackground方法");
		HttpUrlConnectionLoader con = new HttpUrlConnectionLoader();
		System.out.println(params.http_method);
		System.out.println(params.domain + params.url);
		System.out.println(params.params);
		con.postDataFromSelf(params, listener, context);
		return null;
	}

	private OnEntityLoadCompleteListener<Base> listener = new OnEntityLoadCompleteListener<Base>() {

		@Override
		public void onError() {
			System.out.println("aaa:NetTask:onError方法");
			// Base entity = new Base();
			// if (body == null) {
			// body = new ResponseBody<T>();
			// }
			// body.base = entity;
			// System.out.println("网络不给力");
			msg.what = 1;
			msg.sendToTarget();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onEntityLoadComplete(Base entity) {
			System.out.println("aaa:NetTask:onEntityLoadComplete方法:typeToken");
			try {
				// cookie失效返回500
				if (entity.code.equals("500")) {
					msg.what = 4;// 返回 值为cookie失效
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
				// if (!entity.code.equals("200")) {
				// msg.obj = entity.message;
				// msg.what = 2;
				// msg.sendToTarget();
				// return;
				// }

				body = new ResponseBody<T>();
				if (typeToken != null) {
					System.out
							.println("aaa:NetTask:onEntityLoadComplete:请求成功！");
					try {
						body.list = Constant.gson.fromJson(entity.info, typeToken);
					} catch (Exception e) {
						msg.what = 3;
						msg.sendToTarget();
					}
					System.out
							.println("aaa:NetTask:onEntityLoadComplete:jsonString"
									+ entity.info.toString());
					// 保存json数据
					if (cache == 1) {
						e.putString(
								"newslanmu",
								Constant.encode(Constant.key,
										entity.info.toString()));
						e.apply();
					} else if (cache == 3) {
						e.putString(
								"vipxingcheng",
								Constant.encode(Constant.key,
										entity.info.toString()));
						e.apply();
					} else if (cache == 4) {
						e.putString(
								"shouyelunbo",
								Constant.encode(Constant.key,
										entity.info.toString()));
						e.apply();
					} else if (cache == 5) {
						e.putString(
								"jiudianxinxiname",
								Constant.encode(Constant.key,
										entity.info.toString()));
						e.apply();
					} else if (cache == 6) {
						e.putString(
								"zhuanshufuwu",
								Constant.encode(Constant.key,
										entity.info.toString()));
						e.apply();
					}

					if (!entity.smallPorurl.equals("")) {
						body.base.smallPorurl = entity.smallPorurl;
					}
					if (!entity.baoanphone.equals("")
							|| !entity.fanyiphone.equals("")
							|| !entity.yihurenyuanphone.equals("")
							|| !entity.sixin.equals("")) {
						body.base.baoanphone = entity.baoanphone;
						body.base.fanyiphone = entity.fanyiphone;
						body.base.yihurenyuanphone = entity.yihurenyuanphone;
						body.base.sixin = entity.sixin;
					}
					if (body.list.size() == 0) {
						System.out
								.println("aaa:NetTask:onEntityLoadComplete方法:list.size()=0");
						msg.what = 3;
						msg.sendToTarget();
					} else {
						msg.obj = body;
						msg.sendToTarget();
					}
				} else {
					System.out
							.println("aaa:NetTask:onEntityLoadComplete方法---typeToken= null");
					if (clazz != null) {
						obj = (Object) Constant.gson.fromJson(entity.info,
								clazz);
						// 得到数据传回handler
						msg.obj = obj;
						if (entity.pagecount != 0000) {
							msg.arg1 = entity.pagecount;
						}
						msg.sendToTarget();
						// 保存json
						if (pagenum == 5) {
							e.putString(
									"jiudianxinxi",
									Constant.encode(Constant.key,
											entity.info.toString()));
							e.apply();
						} else if (cache < 3) {
							e.putString(
									"cat_id" + cat_id + "pagenum" + pagenum,
									Constant.encode(Constant.key,
											entity.info.toString()));
							e.apply();
						}
					} else {// 这里的处理就是为了返回message数据
						msg.obj = entity.message;
						msg.sendToTarget();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(Base entity) {
			System.out.println("aaa:NetTask:onError方法:" + entity.message);
			// if (body == null) {
			// body = new ResponseBody<T>();
			// }
			// body.base = entity;
			msg.what = 1;
			msg.sendToTarget();
		}
	};
}
