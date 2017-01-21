package com.hebg3.mxy.utils;

import java.io.InputStream;
import java.net.URL;
import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public class AsyncTaskForGetDaHuiDianPingZhuTiPojo extends
		AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	String actionid;
	Context context;
	String userid;
	public AsyncTaskForGetDaHuiDianPingZhuTiPojo(Message m,String actionid,Context context,String userid) {
		this.m = m;
		this.actionid = actionid;
		this.context=context;
		this.userid=userid;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			URL url = new URL(Constant.DOMAIN+"/subjects.do?&method=subjectDetail&actionid="+actionid+"&userid="+userid);
			
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(url,this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println(json);// 输出json
				BasePojo bpojo = g.fromJson(json, BasePojo.class);// 解析成basepojo
				if(bpojo.code.equals("300")){
					return 0;
				}
				if(bpojo.code.equals("500")){
					return 500;
				}
				if (bpojo.code.equals("200")) {// 请求成功 300是没数据
					DaHuiDianPingZhuTiListPojo pojo = g.fromJson(bpojo.info, DaHuiDianPingZhuTiListPojo.class);
					m.obj = pojo;
					return 1;
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		} catch (Exception e) {
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
