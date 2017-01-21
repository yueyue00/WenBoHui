package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.a.a.be;

import com.gheng.exhibit.http.body.response.VipInfoBean;
import com.gheng.exhibit.http.body.response.VipSchduleBean;
import com.gheng.exhibit.http.body.response.VipSchduleNewBean;
import com.gheng.exhibit.http.body.response.VipScheduleInfoBean;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class VipScheduleTask extends AsyncTask<Integer, Integer, Integer>{

	Message m;
	Gson g = new Gson();

	String userid;
	String vipid;
	Context context;

	public VipScheduleTask(Message m, String userid,String vipid,Context context) {
		this.m = m;
		this.userid = userid;
		this.vipid = vipid;
		this.context = context;
		Log.d("tag", "=======调用了VipScheduleTask的构造方法");
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		Log.d("tag", "=======调用了VipScheduleTask的doInBackground方法");
		try {
			String sss = Constant.DOMAIN + "/viptrips.do";
//			String sss = "http://172.20.30.16:8080/wenbo/viptrips.do";
			Log.d("tag", "=======调用了VipScheduleTask的doInBackground方法-->sss:"+sss);
			URL url = new URL(sss);
			System.out.println("method=hyList&lg="
					+ SharedData.getInt("i18n", Language.ZH)
					+ "&userid="
					+ URLEncoder.encode(Constant.encode(Constant.key, userid),
							"UTF-8")+"&vipid="+vipid);
//			Log.d("tag", "=======调用了VipScheduleTask的doInBackground方法-->sss:"+"method=hyList&lg="
//					+ SharedData.getInt("i18n", Language.ZH)
//					+ "&userid="
//					+ userid+"&vipid="+Constant.VIPID);
			//userid---->  URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=hyList&lg="
							+ SharedData.getInt("i18n", Language.ZH)
							+ "&userid="
							+ userid+"&vipid="+vipid,
					        this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				Log.d("tag", "=======调用了VipScheduleTask的doInBackground方法-->" + json);
				System.out.println("===嘉宾行程的数据==:" + json);// 输出json
				
//				if (json != null && !json.equals("")) {
//					
//					
//					VipSchduleBean bean = g.fromJson(json, VipSchduleBean.class);
//					if (bean.getCode().equals("300")) {
//						return 300;
//					}
//	                if (bean.getCode().equals("500")) {
//						return 500;
//					}
//	                if (bean.getCode().equals("400")) {
//						return 400;
//					}
//	                if (bean.getCode().equals("200")) {//请求成功
//						m.obj = bean;
//						return 1;
//					}else {
//						return -1;
//					}
//	            	
//					}else {
//						return -2;//json数据为空
//					}
				
				//当info的数据为空的时候
				VipSchduleNewBean vipSchduleNewBean = null;
				vipSchduleNewBean = g.fromJson(json, VipSchduleNewBean.class);
				if (vipSchduleNewBean.getCode().equals("300")) {
					m.obj = vipSchduleNewBean.getMessage();
					return 300;
				}
                if (vipSchduleNewBean.getCode().equals("500")) {
                	m.obj = vipSchduleNewBean.getMessage();
					return 500;
				}
                if (vipSchduleNewBean.getCode().equals("400")) {
                	m.obj = vipSchduleNewBean.getMessage();
					return 400;
				}
				if (vipSchduleNewBean.getCode().equals("200")) {// 请求成功
					List<VipScheduleInfoBean> result = new ArrayList<>();
					result.clear();
					JsonArray info = (JsonArray) vipSchduleNewBean.getInfo();
					if (info != null) {

						for (int i = 0; i < info.size(); i++) {
							JsonObject object = (JsonObject) info.get(i);
							VipScheduleInfoBean bean = g.fromJson(object,
									VipScheduleInfoBean.class);
							result.add(bean);
						}
						m.obj = result;
						return 1;

					} else {
						return -2;//info为空
					}
				} else {
					return -1;
				}

			} else {
				Log.d("tag", "======is为空");
				Log.d("tag", "=======调用了VipScheduleTask的doInBackground方法-->else-1");
				return -1;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("tag", "=======调用了VipScheduleTask的doInBackground方法-->catch-1");
			return -1;
		} 
	}

	@Override
	protected void onPostExecute(Integer result) {
		Log.d("tag", "=======调用了VipScheduleTask的onPostExecute方法");
		m.what = result;
		m.sendToTarget();
	}

}
