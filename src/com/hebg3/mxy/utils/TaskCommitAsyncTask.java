package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import com.gheng.exhibit.http.body.response.EmptyInfo;
import com.gheng.exhibit.http.body.response.MyTaskContentNew;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class TaskCommitAsyncTask extends AsyncTask<Integer, Integer, Integer>{


	Message m;
	Gson g = new Gson();

	Context context;
	String userid;
	String vipid;
	String taskid;
	String longtitude;
	String latitude;
	String type;
	

	public TaskCommitAsyncTask(Message m, String userid,String vipid,String taskid,String type,String latitue,String longtitue,Context context) {
		this.m = m;
		this.userid = userid;
		this.vipid = vipid;
		this.taskid = taskid;
		this.type = type;
		this.latitude = latitue;
		this.longtitude = longtitue;
		
		this.context = context;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			String sss = Constant.DOMAIN + "/taskSign.do";
//			String sss = "http://172.20.30.16:8080/wenbo/viptrips.do";
			Log.d("tag", "=======调用了TaskCommitAsyncTask的doInBackground方法-->sss:"+sss);
			URL url = new URL(sss);
			System.out.println("method=commTaskSign&userId="
					+ URLEncoder.encode(Constant.encode(Constant.key, userid),
							"UTF-8")+"&vipId="+vipid+"&taskId="+taskid+"&type="+type+"&latitude="+latitude+"&longitude="+longtitude);
			
		InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=commTaskSign&userId="
							+ userid+"&vipId="+vipid+"&taskId="+taskid+"&type="+type+"&latitude="+latitude+"&longitude="+longtitude,
					        this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("===提交任务后返回的结果==:" + json);// 输出json
				
                 m.obj = json;
                 return 1;
                 
                 
//                String infoString = "\"info\":\"\"";
// 				System.out.println("------->"+infoString);
// 				if (json.contains("\"info\":\"\"")) {
// 					EmptyInfo emptyInfo = g.fromJson(json, EmptyInfo.class);
// 					if (emptyInfo.getCode().equals("300")) {
// 						return 300;
// 					}
// 					if (emptyInfo.getCode().equals("400")) {
// 						return 400;
// 					}
// 					if (emptyInfo.getCode().equals("500")) {
// 						return 500;
// 					}else {
// 						return -2;
// 					}
//                     
// 					
// 				} else {
//
// 					MyTaskContentNew bean = g.fromJson(json, MyTaskContentNew.class);
// 					if (bean.getCode().equals("300")) {
// 						return 300;
// 					}
// 	                if (bean.getCode().equals("500")) {
// 						return 500;
// 					}
// 	                if (bean.getCode().equals("400")) {
// 						return 400;
// 					}
// 	                if (bean.getCode().equals("200")) {//请求成功
// 						m.obj = bean;
// 						return 1;
// 					}else {
// 						return -1;
// 					}
// 				}
// 				

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
