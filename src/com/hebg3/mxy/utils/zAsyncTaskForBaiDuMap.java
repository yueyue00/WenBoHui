package com.hebg3.mxy.utils;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.baidu.mapapi.model.LatLng;
import com.gheng.exhibit.http.body.response.BaiDuLocations;
import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class zAsyncTaskForBaiDuMap extends AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	Context context;
	String userid;

	public zAsyncTaskForBaiDuMap(Message m, Context context, String userid) {
		this.m = m;
		this.context = context;
		this.userid = userid;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			// method=groupUsers&userId=%s&groupId=%s&groupName=%s
			URL url = new URL(Constant.DOMAIN
					+ "/mapNavigation.do?method=mapNavign&userid=" + userid);
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisGET(
					url, this.context);// 传入url和请求方式
			if (is != null) {
				System.out.println("zAsyncTaskForBaiDuMap:inputstream" + "aaa");
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("zAsyncTaskForBaiDuMap:json" + json);
				BasePojo bpojo = g.fromJson(json, BasePojo.class);// 解析成basepojo
				if (bpojo.code.equals("300")) {
					m.obj = bpojo.message;
					return 3;
				}
				if (bpojo.code.equals("500")) {
					m.obj = bpojo.message;
					return 4;
				}
				if (bpojo.code.equals("200")) {// 请求成功 300是没数据
					System.out.println("zAsyncTaskForBaiDuMap:获取成功!");
					JsonArray array = (JsonArray) bpojo.info;
					JsonObject object = (JsonObject) array.get(0);
					BaiDuLocations baidulocations = (BaiDuLocations) Constant.gson
							.fromJson(object, BaiDuLocations.class);
					for (int i = 0; i < baidulocations.hotel.size(); i++) {
						if (baidulocations.hotel.get(i).lat_itude.equals("")
								|| baidulocations.hotel.get(i).long_itude
										.equals("")) {
							baidulocations.hotel.get(i).setLl(null);
						} else {
							baidulocations.hotel.get(i).setLl(
									new LatLng(Double
											.parseDouble(baidulocations.hotel
													.get(i).lat_itude), Double
											.parseDouble(baidulocations.hotel
													.get(i).long_itude)));
						}
					}
					for (int i = 0; i < baidulocations.confHall.size(); i++) {
						baidulocations.confHall.get(i).setLl(
								new LatLng(Double
										.parseDouble(baidulocations.confHall
												.get(i).latitude), Double
										.parseDouble(baidulocations.confHall
												.get(i).longitude)));
					}
					for (int i = 0; i < baidulocations.scenicSpot.size(); i++) {
						baidulocations.scenicSpot.get(i).setLl(
								new LatLng(Double
										.parseDouble(baidulocations.scenicSpot
												.get(i).latitude), Double
										.parseDouble(baidulocations.scenicSpot
												.get(i).longitude)));
					}
					System.out.println("zAsyncTaskForBaiDuMap"
							+ baidulocations.toString());
					m.obj = baidulocations;
					return 0;
				} else {
					m.obj = bpojo.message;
					return 1;
				}
			} else {
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		m.what = result;
		m.sendToTarget();
	}
}
