package com.hebg3.mxy.utils;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.http.body.response.QianDaoBackInfo;
import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class zAsyncTaskForQianDaoBackInfo extends
		AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	Context context;
	String userid;
	String taskId;
	String type;
	String longitude;
	String latitude;

	public zAsyncTaskForQianDaoBackInfo(Message m, Context context,
			String userid, String taskId, String type, String longitude,
			String latitude) {
		this.m = m;
		this.context = context;
		this.userid = userid;
		this.taskId = taskId;
		this.type = type;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			// method=groupUsers&userId=%s&groupId=%s&groupName=%s
			URL url = new URL(Constant.DOMAIN
					+ "/taskSign.do?method=sjCommTask&userId=" + userid
					+ "&taskId=" + taskId + "&type=" + type + "&longitude="
					+ longitude + "&latitude=" + latitude);
			System.out.println("zAsyncTaskForQianDaoBackInfo:" + url);
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(
					url, this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("zAsyncTaskForQianDaoBackInfo:json" + json);
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

					// m.obj = bpojo.message;
					// return 0;
					JsonArray array = (JsonArray) bpojo.info;
					if (array != null && array.size() != 0) {

						JsonObject object = (JsonObject) array.get(0);
						QianDaoBackInfo qinBackInfo = (QianDaoBackInfo) Constant.gson
								.fromJson(object, QianDaoBackInfo.class);
						m.obj = qinBackInfo;
						return 0;
					} else {
						m.obj = bpojo.message;
						return 1;
					}
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
