package com.hebg3.mxy.utils;

import java.io.InputStream;
import java.net.URL;

import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.http.body.response.YongHuMingDengLu;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

public class AsyncTaskForUpdateUserInfo extends
		AsyncTask<Integer, Integer, Integer> {

	public Message m;
	public String userid;
	public Gson g = new Gson();
	public Context cont;

	public AsyncTaskForUpdateUserInfo(Context cont, String userid, Message m) {
		this.cont = cont;
		this.userid = userid;
		this.m = m;

	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		try {
			System.out.println(Constant.DOMAIN
					+ "/hylogin.do?method=getMemberDetails&userid=" + userid
					+ "&lg=" + SharedData.getInt("i18n", Language.ZH));

			URL url = new URL(Constant.DOMAIN + "/hylogin.do");
			String requestContent = "method=getMemberDetails&userid=" + userid
					+ "&lg=" + SharedData.getInt("i18n", Language.ZH);

			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url, requestContent, cont);// 传入url和请求方式

			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("AsyncTaskForUpdateUserInfo:doInBackground"
						+ json);// 输出json
				BasePojo bpojo = g.fromJson(json, BasePojo.class);// 解析成basepojo

				YongHuMingDengLu userinfo = g.fromJson(bpojo.info,
						YongHuMingDengLu.class);
				m.obj = userinfo;
				return 200;
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		m.what = result;
		m.sendToTarget();
	}
}
