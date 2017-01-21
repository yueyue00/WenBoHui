package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import com.gheng.exhibit.http.body.response.EmptyInfo;
import com.gheng.exhibit.http.body.response.EmptyJiabinBean;
import com.gheng.exhibit.http.body.response.FixedTaskCommonVipBean;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean;
import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class FixedTaskAsyncTask extends AsyncTask<Integer, Integer, Integer>{

	Message m;
	Gson g = new Gson();

	String userid;
	Context context;
	String taskid;//如果是接机等固定任务  则  taskid是对应的唯一标识
//	String taskuniquecode;

	public FixedTaskAsyncTask(Message m, String userid, String taskid,Context context) {
		this.m = m;
		this.userid = userid;
		this.context = context;
		this.taskid = taskid;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			
			String sss = Constant.DOMAIN + "/taskSign.do";
//			String sss = "http://172.20.15.16:8080/wenbo2"+ "/taskSign.do";
			Log.d("tag", "=======调用了FixedTaskAsyncTask的doInBackground方法-->sss:"+sss);
			System.out.println("=======调用了FixedTaskAsyncTask的doInBackground方法-->sss:"+sss);
			
			URL url = new URL(sss);
			System.out.println("method=showNotFinishMember&userId="
					+ URLEncoder.encode(Constant.encode(Constant.key, userid),
							"UTF-8")+"&taskId="+taskid);
			
			//userid---->  URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=showNotFinishMember&userId="
							+ userid+"&taskId="+taskid,
					        this.context);// 传入url和请求方式
			
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				Log.d("tag", "=======调用了FixedTaskAsyncTask的doInBackground方法-->" + json);
				System.out.println("===未完成固定任务的嘉宾列表==:" + json);// 输出json
//				m.obj = json;
//				return 1;
				
				String jiabinString =  "\"info\":\"\"";
				System.out.println("------->"+jiabinString);
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

					FixedTaskCommonVipBean bean = g.fromJson(json,
							FixedTaskCommonVipBean.class);
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
