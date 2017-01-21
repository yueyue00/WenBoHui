package com.hebg3.mxy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.co;
import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.http.body.response.DaHuiInfo_forsearch;
import com.gheng.exhibit.http.body.response.VipBaseInfoDynamic;
import com.gheng.exhibit.http.body.response.VipBaseInfoDynamic.FwInfoBean;
import com.gheng.exhibit.http.body.response.VipBaseInfoDynamic.JbjbInfoBean;
import com.gheng.exhibit.http.body.response.VipInfoBean;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class VipInfoTask extends AsyncTask<Integer, Integer, Integer> {

	Message m;
	Gson g = new Gson();

	String userid;
	Context context;
	String vipid;

	public VipInfoTask(Message m, String userid, String vipid,Context context) {
		this.m = m;
		this.userid = userid;
		this.context = context;
		this.vipid = vipid;
		Log.d("tag", "=======调用了VipInfoTask的构造方法");
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		Log.d("tag", "=======调用了VipInfoTask的doInBackground方法");
		try {
			String sss = Constant.DOMAIN + "/vipmembers.do";
			Log.d("tag", "=======调用了VipInfoTask的doInBackground方法-->sss:"+sss);
			URL url = new URL(sss);
			System.out.println("method=fwMemberDetails&lg="
					+ SharedData.getInt("i18n", Language.ZH)
					+ "&userid="
					+ URLEncoder.encode(Constant.encode(Constant.key, userid),
							"UTF-8")+"&vipid="
							+vipid);
//			Log.d("tag", "=======调用了VipInfoTask的doInBackground方法-->sss:"+"method=fwMemberDetails&lg="
//					+ SharedData.getInt("i18n", Language.ZH)
//					+ "&userid="
//					+ userid);
			//userid---->  URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			InputStream is = HttpUrlConnectionutil.getHttpUrlConnectionisPOST(
					url,
					"method=fwMemberDetails&lg="
							+ SharedData.getInt("i18n", Language.ZH)
							+ "&userid="
							+ userid
							+"&vipid="
							+vipid,
					        this.context);// 传入url和请求方式
			if (is != null) {
				String json = HttpUrlConnectionutil
						.convertStreamToStringUTF8(is);// 输入流转换成String
				Log.d("tag", "=======调用了VipInfoTask的doInBackground方法-->" + json);
				System.out.println("===嘉宾信息的数据==:" + json);// 输出json
//				m.obj = json;
//				return 1;
				JSONObject dataJsonObject;
				try {
					dataJsonObject = new JSONObject(json);
					String code = dataJsonObject.getString("code");
					if (code.equals("300")) {
						return 300;
					}
					if (code.equals("400")) {
						return 400;
					}
					if (code.equals("500")) {
						return 500;
					}
					if (code.equals("200")) {
						VipBaseInfoDynamic vipBaseInfoDynamic = new VipBaseInfoDynamic();
						vipBaseInfoDynamic.setCode(code);
						vipBaseInfoDynamic.setMessage(dataJsonObject.getString("message"));
						
						JSONObject fwInfo = dataJsonObject.getJSONObject("fwInfo");
						FwInfoBean fwInfoBean = new FwInfoBean();//创建服务人员对象
						fwInfoBean.setMobile(fwInfo.getString("mobile"));
						fwInfoBean.setName(fwInfo.getString("name"));
						fwInfoBean.setRy_imgUrl(fwInfo.getString("ry_imgUrl"));
						fwInfoBean.setRy_name(fwInfo.getString("ry_name"));
						fwInfoBean.setRy_token(fwInfo.getString("ry_token"));
						fwInfoBean.setRy_userId(fwInfo.getString("ry_userId"));
						fwInfoBean.setUserid(fwInfo.getString("userid"));
						fwInfoBean.setUserjuese(fwInfo.getString("userjuese"));
						fwInfoBean.setWorkplace(fwInfo.getString("workplace"));
						vipBaseInfoDynamic.setFwInfoBean(fwInfoBean);//set服务人员数据
						
						JSONObject jbjbInfo = dataJsonObject.getJSONObject("jbjbInfo");
						JbjbInfoBean jbjbInfoBean = new JbjbInfoBean();
						jbjbInfoBean.setUserid(jbjbInfo.getString("userid"));
						jbjbInfoBean.setUserjuese(jbjbInfo.getString("userjuese"));
						jbjbInfoBean.setName(jbjbInfo.getString("name"));
						jbjbInfoBean.setWorkplace(jbjbInfo.getString("workplace"));
						jbjbInfoBean.setJob(jbjbInfo.getString("job"));
						jbjbInfoBean.setMobile(jbjbInfo.getString("mobile"));
						jbjbInfoBean.setPhotourl(jbjbInfo.getString("photourl"));
						jbjbInfoBean.setPhotourlbig(jbjbInfo.getString("photourlbig"));
						jbjbInfoBean.setRy_userId(jbjbInfo.getString("ry_userId"));
						jbjbInfoBean.setRy_name(jbjbInfo.getString("ry_name"));
						jbjbInfoBean.setRy_imgUrl(jbjbInfo.getString("ry_imgUrl"));
						jbjbInfoBean.setRy_token(jbjbInfo.getString("ry_token"));
						jbjbInfoBean.setIsScan(jbjbInfo.getString("isScan"));
						vipBaseInfoDynamic.setJbjbInfoBean(jbjbInfoBean);
						
						JSONObject jbxxInfo = dataJsonObject.getJSONObject("jbxxInfo");
						String jbxxInfoString = jbxxInfo.toString();
						Map<String, Object> jbxxInfoMap = JSON2Map(jbxxInfoString);
						vipBaseInfoDynamic.setJbxxInfoMap(jbxxInfoMap);
						
						JSONObject kvInfo = dataJsonObject.getJSONObject("kvInfo");
						String kvInfoString = kvInfo.toString();
						Map<String, Object> kvInfoMap = JSON2Map(kvInfoString);
						vipBaseInfoDynamic.setKvInfoMap(kvInfoMap);
						
						System.out.println("======jbxxInfoString==>"+jbxxInfoString);
						System.out.println("======kvInfoString==>"+kvInfoString);
						
						m.obj = vipBaseInfoDynamic;
						
						return 1;
					}else {
						return -1;
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					return -1;
				}
				
				
				
				
//				if (json != null && !json.equals("")) {
//				
//				
//				VipInfoBean bean = g.fromJson(json, VipInfoBean.class);
//				if (bean.getCode().equals("300")) {
//					return 300;
//				}
//                if (bean.getCode().equals("500")) {
//					return 500;
//				}
//                if (bean.getCode().equals("400")) {
//					return 400;
//				}
//                if (bean.getCode().equals("200")) {//请求成功
//					m.obj = bean;
//					return 1;
//				}else {
//					return -1;
//				}
//            	
//				}else {
//					return -2;//json数据为空
//				}
			} else {
				Log.d("tag", "======is为空");
				return -1;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	/**
     * 将json格式封装的字符串数据转换成java中的Map数据
     *
     * @return
     */
    public  Map<String, Object> JSON2Map(String json) {
        Map<String, Object> map = null;
        
        try {
            map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(json);
            String s = jsonObject.toString();
            Log.d("tag","======json2map==>s--->"+s);
            JSONObject jsonMap = new JSONObject(s);
            Iterator<String> it = jsonMap.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (TextUtils.isEmpty(key)) continue;
                Object value =  jsonMap.get(key);
                if (value == null) continue;
                map.put(key,value);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return  null;
    }
	@Override
	protected void onPostExecute(Integer result) {
		m.what = result;
		m.sendToTarget();
	}

}
