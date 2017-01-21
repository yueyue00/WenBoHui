package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import com.gheng.exhibit.http.body.response.CulturalInfoBean;
import com.gheng.exhibit.http.body.response.EmptyInfo;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class CulturalExhibitionDetailAsyncTask extends AsyncTask<Integer,Integer,Integer>{


	Message m;
	Gson g = new Gson();

	String userid;
	String uniqueCode;
	Context context;

	public CulturalExhibitionDetailAsyncTask(Message m, String userid,String uniqueCode, Context context) {
		this.m = m;
		this.userid = userid;
		this.context = context;
		this.uniqueCode = uniqueCode;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			String sss = Constant.DOMAIN + "/cultureHib.do";
//			String sss = "http://192.168.253.9:8080/wenbo2/cultureHib.do";
			Log.d("tag", "=======调用了CulturalExhibitionDetailAsyncTask的doInBackground方法-->sss:"+sss);
			URL url = new URL(sss);
			System.out.println("method=getCultureInfo&language="
					+ SharedData.getInt("i18n", Language.ZH)
					+"&uniqueCode="
					+uniqueCode
					+ "&userid="
					+ URLEncoder.encode(Constant.encode(Constant.key, userid),
							"UTF-8"));
			//userid---->  URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=getCultureInfo&language="
							+ SharedData.getInt("i18n", Language.ZH)
							+"&uniqueCode="
					        +uniqueCode
							+ "&userid="
							+ userid,
					        this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("===文化年展列表详情数据==:" + json);// 输出json
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

					CulturalInfoBean bean = g.fromJson(json, CulturalInfoBean.class);
					if (bean.getCode().equals("300")) {
						return 300;
					}
	                if (bean.getCode().equals("500")) {
						return 500;
					}
	                if (bean.getCode().equals("400")) {
						return 400;
					}
	                if (bean.getCode().equals("200")) {//请求成功
						m.obj = bean;
						return 1;
					}else {
						return -1;
					}
				}
				
				
            	
			} else {
				Log.d("tag", "======is为空");
				Log.d("tag", "=======调用了CulturalExhibitionDetailAsyncTask的doInBackground方法-->else-1");
				return -1;
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("tag", "=======调用了CulturalExhibitionDetailAsyncTask的doInBackground方法-->catch-1");
			return -1;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		m.what = result;
		m.sendToTarget();
	}

}
