package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.http.body.response.DaHuiDate;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Message;

public class AsyncTaskForRequestDaHuiInfos_ByDate extends
		AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	Context context;
	public String huiyirichengname="huiyiricheng";
	public SharedPreferences sp;
	public Editor e;
	public AsyncTaskForRequestDaHuiInfos_ByDate(Message m,Context context) {
		this.m = m;
		this.context=context;
		sp=context.getSharedPreferences(huiyirichengname, Activity.MODE_PRIVATE);
		e=sp.edit();
	}
	
		
	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL(Constant.DOMAIN+"/meetings.do?method=getMeetInfoByDate&lg="+SharedData.getInt("i18n", Language.ZH));
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(url,this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("aaa:AsyncTaskForRequestDaHuiInfos_ByDate:通过日期获取会议信息"+json);// 输出json
				BasePojo bpojo = g.fromJson(json, BasePojo.class);// 解析成basepojo
				if(bpojo.code.equals("300")){
					return 0;
				}
				if(bpojo.code.equals("500")){
					return 500;
				}
				if (bpojo.code.equals("200")) {// 请求成功
					System.out.println("aaa:AsyncTaskForRequestDaHuiInfos_ByDate:通过日期获取会议信息成功");
					Type type = new TypeToken<ArrayList<DaHuiDate>>() {}.getType();// 设置集合type
					ArrayList<DaHuiDate> infos = g.fromJson(bpojo.info, type);// 解析多层jsonelement数据
					m.obj = infos;
					e.putString("huiyirichengbydate", Constant.encode(Constant.key,bpojo.info.toString()));
					e.apply();
					return 1;
				} else {
					return -1;
				}
			} else {
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
