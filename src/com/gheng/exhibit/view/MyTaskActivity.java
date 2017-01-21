package com.gheng.exhibit.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import cn.jpush.a.a.i;
import cn.jpush.a.a.v;

import com.alibaba.fastjson.JSONObject;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.InitData;
import com.gheng.exhibit.http.body.response.MyTaskContent;
import com.gheng.exhibit.http.body.response.MyTaskContent.GroupcontentBean;
import com.gheng.exhibit.http.body.response.MyTaskContentNew;
import com.gheng.exhibit.http.body.response.MytaskGridViewBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.MyTaskGridViewAdapter;
import com.gheng.exhibit.view.adapter.MyTaskListViewAdapter;
import com.gheng.exhibit.view.adapter.StickyListViewAdapter;
import com.gheng.exhibit.view.checkin.VIPInfoActivity;
import com.gheng.exhibit.widget.CustomGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.hebg3.mxy.utils.MytaskAsynctask;
import com.hebg3.mxy.utils.VipScheduleTask;
import com.iadiae.acquaintancehelp.view.stickylistheaders.StickyListHeadersListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyTaskActivity extends BaseActivity implements OnClickListener {
	private StickyListHeadersListView stickyListHeadersListView;
	private StickyListViewAdapter adapter;

	private LinearLayout mytask_title_layout;
	private View mytask_view;
	private RelativeLayout mytask_titlelayout;
	private Button mytask_goback;
	private TextView mytask_titletv;
	private TextView mytask_emptyTV;
	
//	private ListView mytask_cusgridView;
	

	private Context mContext;
	private List<MyTaskContent> initListData;
	private String title = "";
	private String vipId = "";
    private User user;
    private String userid = "";
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case 1:
//				String data = (String) msg.obj;
//				System.out.println("======>固定任务嘉宾对应的任务-----》"+data);
				
				MyTaskContentNew data = (MyTaskContentNew) msg.obj;
				adapter = new StickyListViewAdapter(mContext, data.getInfo(),vipId);
				stickyListHeadersListView.setAdapter(adapter);
				
				break;
			case -1:
		    	   Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
		    	   break;
			case -2:
		    	   Toast.makeText(mContext, "请求数据info为空", Toast.LENGTH_LONG).show();
		    	   break;
			case 300:
//		    	   String resultThree = (String) msg.obj;
		    	   Toast.makeText(mContext, "请求数据为空", Toast.LENGTH_LONG).show();
		    	   stickyListHeadersListView.setVisibility(View.GONE);
		    	   mytask_emptyTV.setVisibility(View.VISIBLE);
		    	   break;
		       case 500:
		    	   String resultFive = (String) msg.obj;
		    	   Toast.makeText(mContext, "请求超时", Toast.LENGTH_LONG).show();
		    	   break;
		       case 400:
		    	   String resultFour = (String) msg.obj;
		    	   Toast.makeText(mContext, "请求失败", Toast.LENGTH_LONG).show();
		    	   break;
			default:
				break;
			}
    	};
    };
	

	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			MyApplication.add(this);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  
	            Window window = getWindow();  
	            // Translucent status bar  
	            window.setFlags(  
	                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  
	                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
	        } 
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_my_task);
			mContext = MyTaskActivity.this;
			Intent intent = getIntent();
			title = intent.getStringExtra("title");
			vipId = intent.getStringExtra("vipid");
			System.out.println("-----------嘉宾-vipid->"+vipId);
			initView(); 
//			setStatusBar();
	        InitListData();
            loadData();
		}
		/**
		 * 从网络请求数据
		 */
		private void loadData() {
			// 查找
			try {
				DbUtils db = DbUtils.create(mContext);
				user = db
						.findFirst(Selector.from(User.class).where("id", "=", "1"));
				// user.getUserjuese().equals("3") ?
//				 Constant.decode(Constant.key,user.getVipid());
				// Constant.decode(Constant.key,user.getUserId())
				// URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
				userid = Constant.decode(Constant.key, user.getUserId());
				Log.d("tag", "=======MyTaskActivity--->userid=" + userid);
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
				try {
					new MytaskAsynctask(handler.obtainMessage(), userid,vipId, mContext)
							.execute(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
		}

		/**
		 * 初始化ListView的数据
		 */
		private void InitListData() {
			
			initListData = initListData(getDataFromAssets());
//			addHeaderViewForListView();
//			adapter = new StickyListViewAdapter(mContext, initListData);
//			stickyListHeadersListView.setAdapter(adapter);
	        
			
//			if (initListData != null && initListData.size() > 0) {
//			Toast.makeText(
//						mContext,
//						"==:"
//								+ "size="+initListData.size()+initListData.toString(), Toast.LENGTH_LONG).show();
//			} else {
//				Toast.makeText(mContext, "==:空", Toast.LENGTH_LONG).show();
//			}
			
			
		}
		/**
		 * 给listview添加headerView，且添加的headerview的布局为一个GridView
		 * 这样的话会出现一个问题就是gridview不显示的问题
		 * 解决办法就是自定义一个GridView重写其OnMeasure（）方法
		 */
//		public void addHeaderViewForListView() {
//			View headerView = LayoutInflater.from(mContext).inflate(R.layout.mytask_list_headerview, null);
//			stickyListHeadersListView.addHeaderView(headerView);
//	        mytask_cusgridView = (CustomGridView) headerView.findViewById(R.id.mytask_customgridView);
////	        mytask_cusgridView = (ListView) headerView.findViewById(R.id.mytask_customgridView);
//	        InitGridData();
//	        
//		}
//		
		/**
		 * 透明状态栏 
		 */
//		public void setStatusBar() {
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  
//	            Window window = getWindow();  
//	            // Translucent status bar  
//	            window.setFlags(  
//	                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  
//	                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
//	        } else {
//				mytask_view.setVisibility(View.GONE);
//			}
//		}
	    /**
	     * 初始化view
	     */
		private void initView() {
//			mytask_title_layout = (LinearLayout) findViewById(R.id.mytask_title_layout);
//			mytask_view = (View) findViewById(R.id.mytask_view);
//			mytask_titlelayout = (RelativeLayout) findViewById(R.id.mytask_titlelayout);
			mytask_titletv = (TextView) findViewById(R.id.in_title);
			mytask_titletv.setText(title);
			mytask_goback = (Button) findViewById(R.id.but_fanhui);
			mytask_goback.setOnClickListener(this);
			
			stickyListHeadersListView = (StickyListHeadersListView)findViewById(R.id.stickyHeaderview);
			mytask_emptyTV =  (TextView) findViewById(R.id.mytask_tv_empty);
			
		}

		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.but_fanhui:
				finish();
				break;
//	        case R.id.btn_myt_g:
//	        	int position = 0;
//	        	Object tag = v.getTag();
//	        	if (tag!=null && tag instanceof Integer) {
//					position = (Integer)tag;
//				}
//	        	toastInfo(position);
//	        	break;
			default:
				break;
			}
			
		}
		
		/**
		 * 展示Dialog
		 */
		 public void showCustomDialog(final int position) {
			 //将布局文件转为View
			View customDialogView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog, null);
			//对话框
			final Dialog dialog = new AlertDialog.Builder(mContext).create();
			dialog.show();
			dialog.getWindow().setContentView(customDialogView);
			//确认button
			Button btn_ok = (Button) customDialogView.findViewById(R.id.dialog_ok);
			btn_ok.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					v.setEnabled(false);
					dialog.dismiss();
				}
			});
			//取消Button
			Button btn_cancel = (Button) customDialogView.findViewById(R.id.dialog_cancel);
			btn_cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		}
		 /**
		  * 读取数据---》assets
		  */
		 public String getDataFromAssets() {
			 try {
				InputStream inputStream = getAssets().open("mytask.txt");
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
		 public List<MyTaskContent> initListData(String json) {
			 List<MyTaskContent> listMyTaskContents = new ArrayList<>();
			
//			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
//					.create();
//			bean = gson.fromJson(json, MyTaskContent.class);
			try {
				org.json.JSONObject object = new org.json.JSONObject(json);
				JSONArray array = object.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					MyTaskContent bean = new MyTaskContent();
					org.json.JSONObject jsonObject = array.getJSONObject(i);
					bean.setGroupname(jsonObject.getString("groupname"));
					JSONArray jsonArray = jsonObject.getJSONArray("groupcontent");
					List<GroupcontentBean> listBeans = new ArrayList<>();
					for (int j = 0; j < jsonArray.length(); j++) {
						org.json.JSONObject jsonObject2 = jsonArray.getJSONObject(j);
						GroupcontentBean bean2 = new GroupcontentBean();
						bean2.setId(jsonObject2.getString("id"));
						bean2.setContent(jsonObject2.getString("content"));
						bean2.setChecked(false);
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
		@Override
		protected void setI18nValue() {
			// TODO Auto-generated method stub
			
		}
	
}
