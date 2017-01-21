package com.hebg3.mxy.utils;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.model.databases.RongGroupInfo;
import com.gheng.exhibit.model.databases.zRongGroupInfo;
import com.gheng.exhibit.rongyun.utils.MSharePreferenceUtils;
import com.gheng.exhibit.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class zAsyncTaskForRongGlobalGroup extends
		AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();
	Context context;
	String userid;
	DbUtils db;

	public zAsyncTaskForRongGlobalGroup(Message m, Context context,
			String userid, DbUtils db) {
		this.m = m;
		this.context = context;
		this.userid = userid;
		this.db = db;
		MSharePreferenceUtils.getAppConfig(context);
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		try {
			// method=groupUsers&userId=%s&groupId=%s&groupName=%s
			URL url = new URL(Constant.DOMAIN + "/atAgenda.do");
			String requestContent = "method=getGroupRongList&userid=" + userid;
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url, requestContent, this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				System.out.println("zAsyncTaskForRongGlobalGroup" + json);
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
					JsonArray array = (JsonArray) bpojo.info;
					// 遍历对象
					zRongGroupInfo zRongGroupInfo = null;
					RongGroupInfo rongGroupInfo = null;
					ArrayList<zRongGroupInfo> zlist = new ArrayList<>();
					ArrayList<RongGroupInfo> list = new ArrayList<>();
					Iterator it = array.iterator();
					while (it.hasNext()) {
						JsonElement e = (JsonElement) it.next();
						// JsonElement转换为JavaBean对象
						zRongGroupInfo = (zRongGroupInfo) Constant.gson
								.fromJson(e, zRongGroupInfo.class);
						rongGroupInfo = new RongGroupInfo();
						rongGroupInfo.setName(zRongGroupInfo.name);
						rongGroupInfo.setGroupId(zRongGroupInfo.groupId);
						zlist.add(zRongGroupInfo);
						list.add(rongGroupInfo);
						Group group = new Group(zRongGroupInfo.groupId,
								zRongGroupInfo.name, null);
						RongIM.getInstance().refreshGroupInfoCache(group);
						if (zRongGroupInfo.groupId != null
								&& zRongGroupInfo.name != null)
							MSharePreferenceUtils
									.setParam(zRongGroupInfo.groupId,
											zRongGroupInfo.name);
					}
					// try {
					// db.dropTable(RongGroupInfo.class);
					// db.saveAll(list);
					// } catch (DbException e) {
					// e.printStackTrace();
					// }
					// List<RongGroupInfo> girl8 =
					// db.findAll(RongGroupInfo.class);
					// System.out.println("zAsyncTaskForRongGlobalGroup:girl8"
					// + girl8.toString());
					m.obj = bpojo.message;
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
