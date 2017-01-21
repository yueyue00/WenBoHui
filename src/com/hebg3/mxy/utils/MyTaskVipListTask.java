package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import com.gheng.exhibit.http.body.response.EmptyInfo;
import com.gheng.exhibit.http.body.response.VipBeanForVipList;
import com.gheng.exhibit.http.body.response.VipInfoBean;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class MyTaskVipListTask extends AsyncTask<Integer, Integer, Integer>{

	Message m;
	Gson g = new Gson();

	String userid;
	Context context;
	String vipid;

	public MyTaskVipListTask(Message m, String userid, Context context) {
		this.m = m;
		this.userid = userid;
		this.context = context;
		this.vipid = vipid;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			String sss = Constant.DOMAIN + "/taskSign.do";
//			String sss = "http://172.20.15.16:8080/wenbo2"+ "/taskSign.do";
			Log.d("tag", "=======调用了MyTaskVipListTask的doInBackground方法-->sss:"+sss);
			System.out.println("=======调用了MyTaskVipListTask的doInBackground方法-->sss:"+sss);
			URL url = new URL(sss);
			System.out.println("method=showVipMember&userId="
					+ URLEncoder.encode(Constant.encode(Constant.key, userid),
							"UTF-8"));
			//userid---->  URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=showVipMember&userId="
							+ userid,
					        this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				Log.d("tag", "=======调用了MyTaskVipListTask的doInBackground方法-->" + json);
				System.out.println("===***vip列表的数据==:" + json);// 输出json
//				m.obj = json;
//				return 1;
				String infoString =  "\"info\":\"\"";
				System.out.println("------->"+infoString);
				if (json.contains("\"info\":\"\"")) {
					EmptyInfo emptyInfo = g.fromJson(json, EmptyInfo.class);
					if (emptyInfo.getCode().equals("300")) {
						return 300;
					}
					if (emptyInfo.getCode().equals("400")) {
						return 400;
					}
					if (emptyInfo.getCode().equals("500")) {
						return 500;
					}else {
						return -2;
					}
                    
					
				} else {

					VipBeanForVipList bean = g.fromJson(json,
							VipBeanForVipList.class);
					if (bean.getCode().equals("300")) {
						return 300;
					}
					if (bean.getCode().equals("500")) {
						return 500;
					}
					if (bean.getCode().equals("400")) {
						return 400;
					}
					if (bean.getCode().equals("200")) {// 请求成功
						m.obj = bean;
						return 1;
					} else {
						return -1;
					}
				}
				
			} else {
				Log.d("tag", "======is为空");
				return -1;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		m.what = result;
		m.sendToTarget();
	}

}
