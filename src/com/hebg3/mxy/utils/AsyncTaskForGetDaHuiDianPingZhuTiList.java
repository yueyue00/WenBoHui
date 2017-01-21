package com.hebg3.mxy.utils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.checkin.DaHuiDianPingActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Message;

public class AsyncTaskForGetDaHuiDianPingZhuTiList extends
		AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	int pagenum;
	String userid;
	Context context;
	
	public String cachename="dahuidianpingzhuticache";
	public SharedPreferences sp;
	public Editor e;
	
	public AsyncTaskForGetDaHuiDianPingZhuTiList(Message m, int pagenum, String userid,Context context) {
		this.m = m;
		this.pagenum = pagenum;
		this.userid = userid;
		this.context=context;
		sp=context.getSharedPreferences(cachename,Activity.MODE_PRIVATE);
		e=sp.edit();
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			//-----------lyy 更换接口为subjectPage2
//			URL url = new URL(Constant.DOMAIN+"/subjects.do?pagesize=10&method=subjectPage&pagenum="+pagenum+"&userid="+userid);
			URL url = new URL(Constant.DOMAIN+"/subjects.do?pagesize=10&method=subjectPage2&pagenum="+pagenum+"&userid="+userid);
			
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(url,this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println(json);// 输出json
				BasePojo bpojo = g.fromJson(json, BasePojo.class);// 解析成basepojo
				if(bpojo.code.equals("300")){
					m.arg1 = bpojo.pagecount;
					return 0;
				}
				if(bpojo.code.equals("500")){
					return 500;
				}
				if (bpojo.code.equals("200")) {// 请求成功 300是没数据
					System.out.println("aaa:AsyncTaskForGetDaHuiDianPingZhuTiList:获取成功！");
					Type type = new TypeToken<ArrayList<DaHuiDianPingZhuTiListPojo>>() {
					}.getType();// 设置集合type
					ArrayList<DaHuiDianPingZhuTiListPojo> infos = g.fromJson(bpojo.info, type);// 解析多层jsonelement数据
					m.obj = infos;
					m.arg1 = bpojo.pagecount;
					
					if(context instanceof DaHuiDianPingActivity){//保存大会点评列表缓存
						//保存json
						if(pagenum<6){//保存5页
							e.putString("pagenum"+pagenum, Constant.encode(Constant.key, bpojo.info.toString()));
							e.apply();
						}
					}

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
