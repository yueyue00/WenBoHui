package com.hebg3.mxy.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.model.databases.RongUserInfo;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.model.databases.zRongUserInfo;
import com.gheng.exhibit.rongyun.utils.MSharePreferenceUtils;
import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class zAsyncTaskForRongGlobalUser extends
		AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	Context context;
	String userid;
	DbUtils db;

	public zAsyncTaskForRongGlobalUser(Message m, Context context,
			String userid, DbUtils db) {
		this.m = m;
		this.context = context;
		this.userid = userid;
		this.db = db;
		MSharePreferenceUtils.getAppConfig(context);
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			// URL url = new URL(Constant.DOMAIN
			// + "/agenda.do?method=getUserRongList&userid=" + userid);
			// String client = "/agenda.do?method=getUserRongList&userid="
			// + userid;
			URL url = new URL(Constant.DOMAIN + "/atAgenda.do");
			String requestContent = "method=getUserRongList&userid=" + userid;
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url, requestContent, this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("zAsyncTaskForRongGlobalUser" + json);
				BasePojo bpojo = g.fromJson(json, BasePojo.class);// 解析成basepojo
				if (bpojo.code.equals("300")) {
					m.obj = bpojo.message;
					return 3;
				}
				if (bpojo.code.equals("500")) {
					m.obj = bpojo.message;
					return 4;
				}
				if (bpojo.code.equals("200")) {// 请求成功 300是没数据
					JsonArray array = (JsonArray) bpojo.info;
					// 遍历对象
					zRongUserInfo zrongUserInfo = null;
					RongUserInfo rongUserInfo = null;
					ArrayList<zRongUserInfo> zlist = new ArrayList<>();
					ArrayList<RongUserInfo> list = new ArrayList<>();
					Iterator it = array.iterator();
					while (it.hasNext()) {
						JsonElement e = (JsonElement) it.next();
						// JsonElement转换为JavaBean对象
						zrongUserInfo = (zRongUserInfo) Constant.gson.fromJson(
								e, zRongUserInfo.class);
						rongUserInfo = new RongUserInfo();
						rongUserInfo.setName(zrongUserInfo.name);
						rongUserInfo.setUserId(zrongUserInfo.userId);
						zlist.add(zrongUserInfo);
						list.add(rongUserInfo);
						if (zrongUserInfo.userId != null
								&& zrongUserInfo.name != null)
							MSharePreferenceUtils.setParam(
									zrongUserInfo.userId, zrongUserInfo.name);
					}
					// try {
					// db.dropTable(RongUserInfo.class);
					// db.saveAll(list);
					// } catch (DbException e) {
					// e.printStackTrace();
					// }
					//
					// List<RongUserInfo> girl8 =
					// db.findAll(RongUserInfo.class);
					// System.out.println("zAsyncTaskForRongGlobalUser:girl8"
					// + girl8.toString());
					m.obj = bpojo.message;
					return 0;
				} else {
					m.obj = bpojo.message;
					return 1;
				}
			} else {
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		m.what = result;
		m.sendToTarget();
	}
}
