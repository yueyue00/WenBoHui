package com.gheng.exhibit.view.checkin.checkin;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.DaHuiInfo_forsearch;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForDaHuiRiChengSearchRecyclerView;
import com.gheng.exhibit.view.adapter.AdapterForDaHuiRiChengSearchResultRecyclerView;
import com.hebg3.mxy.utils.AsyncTaskForSearchDaHuiInfos;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;

public class HuiYiRiChengSearchActivity extends BaseActivity implements OnClickListener{
    
	@ViewInject(R.id.in_title)
	TextView titletv;
	
	@ViewInject(R.id.searchbutton)
	Button searchbutton;
	
	@ViewInject(R.id.zuijinsousuotv)
	TextView zuijinsousuotv;
	
	@ViewInject(R.id.edittext)
	EditText edittext;

	@ViewInject(R.id.but_fanhui)
	Button goback;
	
	@ViewInject(R.id.searchresultrv)
	RecyclerView searchresultrv;//搜搜结果rv
	
	@ViewInject(R.id.searchhistorylistrv)
	RecyclerView searchhistorylistrv;//搜索历史rv
	@ViewInject(R.id.zuijinlayout)
	RelativeLayout zuijinlayout;//最近搜索布局 点击搜索或点击搜索历史item，隐藏该布局
	
	@ViewInject(R.id.nodatafound)
	TextView nodatafound; 
	
	ProgressDialog pd;
	public Context context = this;
	
	private Boolean isActive = true;
	
	//搜索历史记录
	ArrayList<String> historysearchlist=new ArrayList<String>();
	//搜索历史记录rv布局管理器
	LinearLayoutManager llm_historysearchlist;
	//搜索历史记录 rv   adapter
	AdapterForDaHuiRiChengSearchRecyclerView adapter_historysearchlist;
	//搜索结果
	ArrayList<DaHuiInfo_forsearch> search_result=new ArrayList<DaHuiInfo_forsearch>();
	//搜索历结果rv布局管理器
	LinearLayoutManager llm_search_result;
	//搜索结果 rv  adapter
	AdapterForDaHuiRiChengSearchResultRecyclerView adapter_search_result;
	
	SharedPreferences sp;
	Editor e;
	String spname="dahuirichengsearchhistory";
	String keyname="dahuisearchhistory";
	User parent = null;
	
	
	Handler handler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			searchbutton.setOnClickListener(HuiYiRiChengSearchActivity.this);
			try{
				if(pd!=null){
					pd.dismiss();
				}
				if (msg.what == 500) {// cookie超时
					BaseActivity.gotoLoginPage(HuiYiRiChengSearchActivity.this);
				}
				if(msg.what==-1){//请求失败
					Toast.makeText(HuiYiRiChengSearchActivity.this, getLanguageString("请求失败"), Toast.LENGTH_SHORT).show();
					nodatafound.setVisibility(View.VISIBLE);
					searchresultrv.setVisibility(View.GONE);
					
				}
				if(msg.what==0){//请求成功，没数据
					Toast.makeText(HuiYiRiChengSearchActivity.this, getLanguageString("暂时没有数据"), Toast.LENGTH_SHORT).show();
					nodatafound.setVisibility(View.VISIBLE);
					searchresultrv.setVisibility(View.GONE);
				}
				if(msg.what==1){//请求成功，有数据
					search_result.clear();
					search_result.addAll((ArrayList<DaHuiInfo_forsearch>)msg.obj);
					System.out.println(search_result.get(0).meettingtime);
					
					nodatafound.setVisibility(View.GONE);
					searchresultrv.setVisibility(View.VISIBLE);
					adapter_search_result.notifyDataSetChanged();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_huiyirichengsearch);
		registerBoradcastReceiver();//注册广播
		nodatafound.setText(getLanguageString("暂时没有数据"));
		
		// 查找
		try {
			DbUtils db = DbUtils.create(context);
			parent = db.findFirst(Selector.from(User.class).where("id", "=","1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		
		sp=this.getSharedPreferences(spname,Activity.MODE_PRIVATE);
		e=sp.edit();
		edittext.setOnClickListener(this);
		
		searchbutton.setOnClickListener(this);
		goback.setOnClickListener(this);
		searchresultrv.setVisibility(View.GONE);
		zuijinlayout.setVisibility(View.GONE);
		nodatafound.setVisibility(View.GONE);

		//初始化搜索历史记录rv
		searchhistorylistrv.setHasFixedSize(true);
		llm_historysearchlist=new LinearLayoutManager(this);
		llm_historysearchlist.setOrientation(LinearLayoutManager.VERTICAL);
		searchhistorylistrv.setLayoutManager(llm_historysearchlist);
		searchhistorylistrv.setHasFixedSize(true);//强制item高度一致，加强加载效率
		
		searchhistorylistrv.addItemDecoration(//为RecyclerView添加divider
		new HorizontalDividerItemDecoration.Builder(this)
		.color(this.getResources().getColor(R.color.searchhuiyirvfengexian))
		.size(getResources().getDimensionPixelSize(R.dimen.half1dp))
		.margin(getResources().getDimensionPixelSize(R.dimen.recylerviewitemdivider_pchi),getResources().getDimensionPixelSize(R.dimen.recylerviewitemdivider_pchi))
		.build());
		
		adapter_historysearchlist=new AdapterForDaHuiRiChengSearchRecyclerView(this,historysearchlist);
		searchhistorylistrv.setAdapter(adapter_historysearchlist);
		
		//初始化搜索结果rv
		searchresultrv.setHasFixedSize(true);
		llm_search_result=new LinearLayoutManager(this);
		llm_search_result.setOrientation(LinearLayoutManager.VERTICAL);
		searchresultrv.setLayoutManager(llm_search_result);
		searchresultrv.setHasFixedSize(true);//强制item高度一致，加强加载效率
		
		searchresultrv.addItemDecoration(//为RecyclerView添加divider
		new HorizontalDividerItemDecoration.Builder(this)
		.color(this.getResources().getColor(R.color.xiaoguo_bg_search_contact))
		.size(getResources().getDimensionPixelSize(R.dimen.radio_button_stroke_border))
		.margin(getResources().getDimensionPixelSize(R.dimen.title_padding),getResources().getDimensionPixelSize(R.dimen.title_padding))
		.build());
		
		adapter_search_result=new AdapterForDaHuiRiChengSearchResultRecyclerView(this,search_result);
		searchresultrv.setAdapter(adapter_search_result);
		//检查搜索历史记录
		try {
			showHistroyRecords();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * 点击编辑框，展示历史记录
	 * @throws Exception 
	 */
	public void showHistroyRecords() throws Exception{
		String[] records=null;
		String record=Constant.decode(Constant.key,sp.getString(keyname,"").trim());
		if(!record.equals("")){
			records=record.split(",");
		}else{
			return;
		}
		if(records.length>0){//有历史搜索记录
			historysearchlist.clear();
			for(int i=records.length-1;i>=0;i--){
				historysearchlist.add(records[i]);
			}
			historysearchlist.add(getLanguageString("清空历史数据"));
		    //显示历史搜索记录列表
			adapter_historysearchlist.notifyDataSetChanged();
			zuijinlayout.setVisibility(View.VISIBLE);
		}else{//没有历史搜索记录，隐藏搜索记录层
			zuijinlayout.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 历史搜索记录被点击后，在adapter中执行本方法
	 * @throws Exception 
	 */
	public void itemclicked(int position) throws Exception{
		if(position==historysearchlist.size()-1){//用户点击"清空历史数据"
			e.putString(keyname,"");
			e.apply();
			zuijinlayout.setVisibility(View.GONE);
		}else{
			String clickcontent=historysearchlist.get(position)+",";
			String record=Constant.decode(Constant.key,sp.getString(keyname,"").trim());
			record=record.replace(historysearchlist.get(position)+",","");
			e.putString(keyname,Constant.encode(Constant.key,record+clickcontent));
			e.apply();
			zuijinlayout.setVisibility(View.GONE);
			edittext.setText(historysearchlist.get(position));
			goSearch(edittext.getEditableText().toString().trim());
		}
	}
	
	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		titletv.setText(getLanguageString("搜索会议"));
		searchbutton.setText(getLanguageString("搜索"));
		zuijinsousuotv.setText(getLanguageString("最近搜索"));
		edittext.setHint(getLanguageString("按标题,简介搜索"));
		
	}
	
	/**
	 * 根据内容请求服务器
	 * @throws Exception 
	 */
	public void goSearch(String content) throws Exception{
		if(!IsWebCanBeUse.isWebCanBeUse(this)){
			Toast.makeText(this, getLanguageString("网络不可用"), Toast.LENGTH_SHORT).show();
			return;
		}
		searchbutton.setOnClickListener(null);
		pd = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中...")); 
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		AsyncTaskForSearchDaHuiInfos at=new AsyncTaskForSearchDaHuiInfos(handler.obtainMessage(),content,parent.getUserjuese().equals("3")?Constant.decode(Constant.key,parent.getVipid()):Constant.decode(Constant.key,parent.getUserId()),this.getApplicationContext());
		at.execute(1);
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==goback){
			this.finish();
		}
		if(v==searchbutton){//点击搜索按钮
			String searchcontent=edittext.getEditableText().toString().trim();//去掉前后空格
			if(null==searchcontent||searchcontent.equals("")){
				Toast.makeText(this, getLanguageString("请输入检索内容"), Toast.LENGTH_SHORT).show();
				return;
			}else{//将搜索记录保存到ShardPreference中
				String history;
				try {
					history = Constant.decode(Constant.key,sp.getString(keyname,"").trim());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					history="";
				}//用，隔开
				if(!history.contains(searchcontent)){//去重复
					history=history+searchcontent+",";
					e.putString(keyname,Constant.encode(Constant.key,history));
					e.apply();
				}
				zuijinlayout.setVisibility(View.GONE);
				try {
					goSearch(searchcontent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(v==edittext){//点击输入框，弹出历史搜索记录
			if(zuijinlayout.getVisibility()==View.VISIBLE){
				return;
			}else{
				try {
					showHistroyRecords();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		if(zuijinlayout.getVisibility()==View.VISIBLE){
			zuijinlayout.setVisibility(View.GONE);
		}else{
			super.onBackPressed();
		}
	}
	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if(mBroadcastReceiver!=null){
			this.unregisterReceiver(mBroadcastReceiver);
		}
		MyApplication.remove(this);
		super.onDestroy();
	}

	/** 记录前台切后台时间 */
	private Long TimeStart;
	/** 记录后台切前台时间 */
	private Long TimeEnd;
	/**
	 * 接收广播
	 **/
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
		    if(Intent.ACTION_SCREEN_OFF.equals(action)){
				if (BaseActivity.isAppOnForeground(context)) {
					isActive = false;
					TimeStart = System.currentTimeMillis() / 1000;
				}
			}
		}
	};
	/**
	 * 注册广播
	 **/
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);	
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	/**
	 * 挂起时调用的方法
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (!BaseActivity.isAppOnForeground(this)) {
			isActive = false;
			TimeStart = System.currentTimeMillis() / 1000;
		}
	}

	/**
	 * 唤醒时调用的方法
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!isActive) {
			TimeEnd = System.currentTimeMillis() / 1000;
			Integer time = getResources().getInteger(R.integer.time);
			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
				BaseActivity.loginValidation(context);
			}
		}
	}
}
