package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.Contacts;

public class AsyncTaskForModifyPassword extends AsyncTask<Integer, Integer, Integer>{

	public Message m;
	public Gson g=new Gson();
	public Context cont;
	public String userid;
	public String oldpass;
	public String newpass;
	
	public AsyncTaskForModifyPassword(Message m,String userid,String oldpass,String newpass,Context cont){
		this.m=m;
		this.cont=cont;
		this.userid=userid;
		this.oldpass=oldpass;
		this.newpass=newpass;
		
	}
	
	
	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		try {
			URL url=new URL(Constant.DOMAIN+"/vipmembers.do");
			String requestContent="method=editPassword&"+"userid="+URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")+"&oldpassword="+URLEncoder.encode(Constant.encode(Constant.key, oldpass),"UTF-8")+"&newpassword="+URLEncoder.encode(Constant.encode(Constant.key, Constant.getMD5(newpass)),"UTF-8");
			InputStream is=HttpUrlConnectionutil.getHttpUrlConnectionisPOST(url,requestContent,cont);//传入url和请求方式
			if(is!=null){
			    String json=HttpUrlConnectionutil.convertStreamToStringUTF8(is);//输入流转换成String
			    System.out.println(json);//输出json
			    BasePojo bpojo=g.fromJson(json,BasePojo.class);//解析成basepojo
			    System.out.println(bpojo.code+"===");
				if(bpojo.code.equals("500")){
					return 500;
				}
			    if(bpojo.code.equals("200")){//请求成功
			    	return 200;
			    }
			    if(bpojo.code.equals("300")){//请求成功
			    	return 300;
			    }else{
			    	return 400;
			    }
			}else{
				return -1000;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1000;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		m.what=(Integer)result;
		m.sendToTarget();
	}

}
