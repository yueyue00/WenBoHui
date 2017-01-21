package com.hebg3.mxy.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.gheng.exhibit.utils.MapHelper;
import com.google.gson.Gson;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;

public class AsyncTaskForDownLoadMapZip extends AsyncTask<Integer, Integer, Integer>{

	Message m;
	Gson g=new Gson();
	String downloadurl;
	Context context;
	
	public AsyncTaskForDownLoadMapZip(Message m,String downloadurl,Context context){
		this.m=m;
		this.downloadurl=downloadurl;
		this.context=context;
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			File f=new File(Environment.getExternalStoragePublicDirectory("ghmap").getPath()+"/tempmap/");
			if(!f.exists()){
				f.mkdirs();
			}
			File zipfile=new File(Environment.getExternalStoragePublicDirectory("ghmap").getPath()+"/660300/");
		    if(!zipfile.exists()){
		    	zipfile.mkdirs();
		    }
			URL url=new URL(downloadurl);
			
			InputStream is=HttpUrlConnectionutil.getHttpUrlConnectionisGET(url,this.context);//传入url和请求方式
			int size=0;
			if(is!=null){
				//先下载，在解压
				BufferedInputStream bis=new BufferedInputStream(is);
				File tempmap=new File(Environment.getExternalStoragePublicDirectory("ghmap").getPath()+"/tempmap/ghmap.zip");
				if(tempmap.exists()){//如果存在，先删除再创建
					tempmap.delete();
					tempmap.createNewFile();
				}else{//不存在，直接创建空文件
					tempmap.createNewFile();
				}
				FileOutputStream fos=new FileOutputStream(tempmap);
				BufferedOutputStream bos=new BufferedOutputStream(fos);
				
				byte[] buffer=new byte[100*1024];
				while((size=bis.read(buffer))!=-1){
					bos.write(buffer,0,size);
				}
				bos.close();
				fos.close();
				bis.close();
				is.close();
				//开始解压				
				FileInputStream fis=new FileInputStream(new File(Environment.getExternalStoragePublicDirectory("ghmap").getPath()+"/tempmap/ghmap.zip"));
				MapHelper.upZipFile(fis,Environment.getExternalStoragePublicDirectory("ghmap").getPath()+"/660300/");
				return 10000;
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
