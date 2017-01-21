package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.http.body.response.DaHuiInfo_forsearch;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public class AsyncTaskForSearchDaHuiInfos extends AsyncTask<Integer, Integer, Integer>{

	Message m;
	Gson g=new Gson();
	String content;
	String userid;
	Context context;
	
	public AsyncTaskForSearchDaHuiInfos(Message m,String content,String userid,Context context){
		this.m=m;
		this.content=content;
		this.userid=userid;
		this.context=context;
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			String sss=Constant.DOMAIN+"/meetings.do";
			URL url=new URL(sss);
			System.out.println("method=getMeetByContent&lg="+SharedData.getInt("i18n", Language.ZH)+"&content="+content+"&userid="+userid);
			InputStream is=HttpUrlConnectionutil.getHttpUrlConnectionisPOST(url,"method=getMeetByContent&lg="+SharedData.getInt("i18n", Language.ZH)+"&content="+content+"&userid="+userid,this.context);//传入url和请求方式
			if(is!=null){
			    String json=HttpUrlConnectionutil.convertStreamToStringUTF8(is);//输入流转换成String
			    System.out.println(json);//输出json
			    BasePojo bpojo=g.fromJson(json,BasePojo.class);//解析成basepojo
				if(bpojo.code.equals("300")){
					return 0;
				}
				if(bpojo.code.equals("500")){
					return 500;
				}
			    if(bpojo.code.equals("200")){//请求成功
			    	Type type=new TypeToken<ArrayList<DaHuiInfo_forsearch>>(){}.getType();//设置集合type
			    	ArrayList<DaHuiInfo_forsearch> infos=g.fromJson(bpojo.info,type);//解析多层jsonelement数据

					m.obj = infos;
					return 1;

			    }else{
			    	return -1;
			    }
			}else{
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
		m.what=result;
		m.sendToTarget();
	}
}
