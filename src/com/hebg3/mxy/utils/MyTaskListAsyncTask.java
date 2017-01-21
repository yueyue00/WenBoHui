package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import com.gheng.exhibit.http.body.response.EmptyInfo;
import com.gheng.exhibit.http.body.response.EmptyJiabinBean;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean;
import com.gheng.exhibit.http.body.response.VipBeanForVipList;
import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class MyTaskListAsyncTask extends AsyncTask<Integer, Integer, Integer>{

	Message m;
	Gson g = new Gson();

	String userid;
	Context context;

	public MyTaskListAsyncTask(Message m, String userid, Context context) {
		this.m = m;
		this.userid = userid;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			
			String sss = Constant.DOMAIN + "/taskSign.do";
			
			Log.d("tag", "=======调用了MyTaskListAsyncTask的doInBackground方法-->sss:"+sss);
			System.out.println("=======调用了MyTaskListAsyncTask的doInBackground方法-->sss:"+sss);
			
			URL url = new URL(sss);
			
			System.out.println("method=showMember&userId="
					+ URLEncoder.encode(Constant.encode(Constant.key, userid),
							"UTF-8")+"&uniqueCode=\"\"");
			
			//userid---->  URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=showMember&userId="
							+ userid+"&uniqueCode=\"\"",
					        this.context);// 传入url和请求方式
			
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				Log.d("tag", "=======调用了MyTaskListAsyncTask的doInBackground方法-->" + json);
				System.out.println("===普通嘉宾列表的数据==:" + json);// 输出json
//				m.obj = json;
//				return 1;
				String infoString =  "\"info\":\"\"";
				System.out.println("------->"+infoString);
				if (json.contains("\"info\":\"\"")) {
					EmptyInfo emptyInfo = g.fromJson(json, EmptyInfo.class);
					if (emptyInfo.getCode().equals("300")) {
						m.obj = emptyInfo.getMessage();
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
                    
				}else {
					String jiabinString =  "\"jiabin\":\"\"";
					System.out.println("------->"+jiabinString);
					if (json.contains("\"jiabin\":\"\"")) {
						EmptyJiabinBean emptyJiabinBean = g.fromJson(json, EmptyJiabinBean.class);
						if (emptyJiabinBean.getCode().equals("300")) {
							return 300;
						}
						if (emptyJiabinBean.getCode().equals("400")) {
							return 400;
						}
						if (emptyJiabinBean.getCode().equals("500")) {
							return 500;
						}
						if (emptyJiabinBean.getCode().equals("200")) {
							m.obj = emptyJiabinBean;
							return 3;//普通嘉宾列表为空
						}else {
							return -2;
						}
	                    
						
					} else {

						MyTaskListDataBean bean = g.fromJson(json,
								MyTaskListDataBean.class);
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
				}
				
				
			} else {
				Log.d("tag", "======is为空");
				return -1;
			}
		} catch (IOException e) {
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
