package com.gheng.exhibit.view.checkin.checkin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;


import com.gheng.exhibit.http.body.response.InitData;
import com.gheng.exhibit.http.body.response.VipSchdule;
import com.gheng.exhibit.http.body.response.VipSchduleBean;
import com.gheng.exhibit.http.body.response.VipSchdule.GroupcontentBean;
import com.gheng.exhibit.http.body.response.VipScheduleInfoBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.adapter.StickyHeaderListViewForVip;
import com.gheng.exhibit.view.adapter.StickyListViewAdapter;
import com.gheng.exhibit.view.checkin.VIPInfoActivity;
import com.hebg3.mxy.utils.VipInfoTask;
import com.hebg3.mxy.utils.VipScheduleTask;
import com.iadiae.acquaintancehelp.view.stickylistheaders.StickyListHeadersListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class VipScheduleFragment extends Fragment{
    
	private Context mContext;
	private StickyListHeadersListView vipStickyListHeadersListView;
	private TextView emptyView;
	private StickyHeaderListViewForVip adapter;
	private User user = null;
	private String userid = ""; 
	private List<VipSchdule> listData;
   
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				
				
//			    String resultData = (String) msg.obj;
//				Toast.makeText(mContext, "=====shedulefragmentd的数据json======>"+resultData, Toast.LENGTH_LONG).show();
				
//				VipScheduleInfoBean dataBean = (VipScheduleInfoBean) msg.obj;//当info内容为空的时候
//				VipSchduleBean resultData = (VipSchduleBean)msg.obj;
				List<VipScheduleInfoBean> result = (List<VipScheduleInfoBean>) msg.obj;
				adapter = new StickyHeaderListViewForVip(getActivity(), result);
		        vipStickyListHeadersListView.setAdapter(adapter);
                
				break;
			 case -1:
		    	   Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
		    	   break;
			 case -2:
		    	   Toast.makeText(mContext, "请求数据为空", Toast.LENGTH_LONG).show();
		    	   break;
		       case 300:
		    	   String resultThree = (String) msg.obj;
//		    	   Toast.makeText(mContext, resultThree, Toast.LENGTH_LONG).show();
		    	   emptyView.setVisibility(View.VISIBLE);
		    	   vipStickyListHeadersListView.setVisibility(View.GONE);
		    	   break;
		       case 500:
		    	   String resultFive = (String) msg.obj;
//		    	   Toast.makeText(mContext, "code = 500 message = "+resultFive, Toast.LENGTH_LONG).show();
		    	   emptyView.setVisibility(View.VISIBLE);
		    	   vipStickyListHeadersListView.setVisibility(View.GONE);
		    	   break;
		       case 400:
		    	   String resultFour = (String) msg.obj;
//		    	   Toast.makeText(mContext, "code = 400 message = "+resultFour, Toast.LENGTH_LONG).show();
		    	   emptyView.setVisibility(View.VISIBLE);
		    	   vipStickyListHeadersListView.setVisibility(View.GONE);
		    	   break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = getActivity();
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_vip_schedule, container, false);
		initView(view);
//		InitDataLocal();
		initData();//从网络请求数据
		return view;
	}
	/**
	 * 从网络请求数据
	 */
	private void initData() {
		// 查找
		try {
			DbUtils db = DbUtils.create(mContext);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			// user.getUserjuese().equals("3") ?
//			 Constant.decode(Constant.key,user.getVipid());
			// Constant.decode(Constant.key,user.getUserId())
			// URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			userid = Constant.decode(Constant.key, user.getUserId());
			Log.d("tag", "=======VipBaseInfoFragment--->userid=" + userid);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		VIPInfoActivity vipInfoActivity = (VIPInfoActivity) getActivity();
		System.out.println("------------------VipScheduleFragment---->vipid--->"+vipInfoActivity.vIPIDString);
		if (vipInfoActivity.vIPIDString!=null) {
			try {
				new VipScheduleTask(handler.obtainMessage(), userid,vipInfoActivity.vIPIDString, mContext)
						.execute(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			Toast.makeText(getActivity(), "暂时没有对应的嘉宾日程", Toast.LENGTH_LONG).show();
		}
		
	}
	 private void InitDataLocal() {
        listData = new ArrayList<>();
        listData.clear();
        listData.addAll(initListData(getDataFromAssets()));
//        adapter = new StickyHeaderListViewForVip(getActivity(), listData);
//        vipStickyListHeadersListView.setAdapter(adapter);
	}
	private void initView(View view) {
         vipStickyListHeadersListView = (StickyListHeadersListView) view.findViewById(R.id.vip_stickyHeaderview);
         emptyView = (TextView) view.findViewById(R.id.vip_tv_empty);
	}
	/**
	  * 读取数据---》assets
	  */
	 public String getDataFromAssets() {
		 try {
			InputStream inputStream = getActivity().getAssets().open("vipschdule.txt");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte [] buf = new byte[inputStream.available()];
			while ((len = inputStream.read(buf))!=-1) {
				baos.write(buf,0,buf.length);
               return new String(baos.toByteArray());				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	 /**
	  * 获取listview的数据
	  * @param json   json字串
	  * @return
	  */
	 public List<VipSchdule> initListData(String json) {
		 List<VipSchdule> listMyTaskContents = new ArrayList<>();
		
//		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
//				.create();
//		bean = gson.fromJson(json, MyTaskContent.class);
		try {
			org.json.JSONObject object = new org.json.JSONObject(json);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				VipSchdule bean = new VipSchdule();
				org.json.JSONObject jsonObject = array.getJSONObject(i);
				bean.setGroupname(jsonObject.getString("groupname"));
				JSONArray jsonArray = jsonObject.getJSONArray("groupcontent");
				List<GroupcontentBean> listBeans = new ArrayList<>();
				for (int j = 0; j < jsonArray.length(); j++) {
					org.json.JSONObject jsonObject2 = jsonArray.getJSONObject(j);
					GroupcontentBean bean2 = new GroupcontentBean();
//					bean2.setId(jsonObject2.getString("id"));
//					bean2.setContent(jsonObject2.getString("content"));
//					bean2.setChecked(false);
					bean2.setTitle(jsonObject2.getString("title"));
					bean2.setLoccation(jsonObject2.getString("loccation"));
					bean2.setMeetingdate(jsonObject2.getString("meetingdate"));
					bean2.setIsschedule(jsonObject2.getBoolean("isschedule"));
					bean2.setContent(jsonObject2.getString("content"));
					listBeans.add(bean2);
				}
				bean.setGroupcontent(listBeans);
				listMyTaskContents.add(bean);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listMyTaskContents;
	}
}
