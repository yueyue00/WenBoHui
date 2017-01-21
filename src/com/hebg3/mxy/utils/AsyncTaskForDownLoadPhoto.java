package com.hebg3.mxy.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;

public class AsyncTaskForDownLoadPhoto extends
		AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	String downloadurl;
	Context context;

	public AsyncTaskForDownLoadPhoto(Message m, String downloadurl,
			Context context) {
		this.m = m;
		this.downloadurl = downloadurl;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			URL url = new URL(downloadurl);

			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(
					url, this.context);// 传入url和请求方式
			int size = 0;
			if (is != null) {

				BufferedInputStream bis = new BufferedInputStream(is);
				// File photofile=new
				// File(Environment.getExternalStorageDirectory().getPath()+"/WICPhotos/"+downloadurl.substring(downloadurl.lastIndexOf("/")+1,
				// downloadurl.length()));
				File photofile = new File(Constant.dianpingTarget
						+ downloadurl.substring(
								downloadurl.lastIndexOf("/") + 1,
								downloadurl.length()));
				if (photofile.exists()) {// 如果存在，先删除再创建
					photofile.delete();
					photofile.createNewFile();
				} else {// 不存在，直接创建空文件
					photofile.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(photofile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				byte[] buffer = new byte[100 * 1024];
				while ((size = bis.read(buffer)) != -1) {// 边读边写
					bos.write(buffer, 0, size);
				}
				bos.close();
				fos.close();
				bis.close();
				return 1;
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
