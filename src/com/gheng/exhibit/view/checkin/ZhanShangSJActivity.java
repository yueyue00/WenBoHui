package com.gheng.exhibit.view.checkin;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.ZhanGuan;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterZhanShangSJ;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

//展商数据
public class ZhanShangSJActivity extends BaseActivity implements
		View.OnClickListener {

	private Boolean isActive = true;

	@ViewInject(R.id.swipe_zhanshangsj)
	SwipeRefreshLayout swipe_zhanshangsj;
	@ViewInject(R.id.zhanshangsj_rv)
	RecyclerView zhanshangsj_rv;
	@ViewInject(R.id.emptyView)
	private TextView emptyView;
	@ViewInject(R.id.in_title)
	TextView in_title;
	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;

	// ProgressDialog pro;
	Gson g = new Gson();
	private AdapterZhanShangSJ adapter;
	ArrayList<ZhanGuan> list = new ArrayList<ZhanGuan>();
	public LinearLayoutManager llm;
	Context context;
	String title = "";
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			swipe_zhanshangsj.setRefreshing(false);
			swipe_zhanshangsj.setEnabled(true);
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				ResponseBody<ZhanGuan> res = (ResponseBody<ZhanGuan>) msg.obj; // 首先创建接收方法
				// 其实就是拿到ResponseBody<HuiYiRiChengRiQiHuoQu>里面的list对象
				list.clear();
				list.addAll(res.list);
				adapter.notifyDataSetChanged();
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确<对应于200>
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 3) { // 表示没有拿到列表数据<对应300>
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 4) {// cookie失效<对应500>
				BaseActivity.gotoLoginPage(ZhanShangSJActivity.this);
			}
			// pro.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhanshangsj);
		// ViewUtils.inject(this);
		registerBoradcastReceiver();// 注册广播
		context = ZhanShangSJActivity.this;
		process();
		// loadData();
		setAllClickListener();
	}

	private void process() {
		llm = new LinearLayoutManager(this);// 设置RecyclerView的布局管理器（必须设置，否则RecyclerView会崩溃）
		llm.setOrientation(LinearLayoutManager.VERTICAL);// 设置横向或纵向展示item
		zhanshangsj_rv.setLayoutManager(llm);// 为RecyclerView配置布局管理器 必须手动配置！
		adapter = new AdapterZhanShangSJ(list, context);
		zhanshangsj_rv.setAdapter(adapter);
		// pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
		// pro.setCancelable(true);// 点击dialog外空白位置是否消失
		// pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失

		swipe_zhanshangsj.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		but_fanhui.setOnClickListener(this);
		emptyView.setVisibility(View.GONE);
	}

	@Override
	protected void setI18nValue() {
		emptyView.setText(BaseActivity.getLanguageString("暂时没有数据"));
		if (getIntent().getStringExtra("title") != null)
			title = getIntent().getStringExtra("title");
		in_title.setText(title);
		if(title.equals("专属服务")){
			initData1();
		}else{
			initData();
		}
	}

	private void initData1() {
		ZhanGuan z2 = new ZhanGuan();
		z2.setName("王思羽 ");
		ArrayList<ZhanGuan> fuwudiaodu = new ArrayList<ZhanGuan>();
		ZhanGuan z1 = new ZhanGuan();
		z1.setName("蛮爽 ");

		fuwudiaodu.add(z1);
		fuwudiaodu.add(z2);
		list.clear();
		list.addAll(fuwudiaodu);
		adapter.notifyDataSetChanged();
	}

	private void initData() {
		ArrayList<ZhanGuan> fuwudiaodu = new ArrayList<ZhanGuan>();
		ZhanGuan z1 = new ZhanGuan();
		z1.setName("文博会通讯录");
		ZhanGuan z2 = new ZhanGuan();
		z2.setName("会场服务群组");
		ZhanGuan z3 = new ZhanGuan();
		z3.setName("最近通话");
		ZhanGuan z4 = new ZhanGuan();
		z4.setName("指挥中心");
		ZhanGuan z5 = new ZhanGuan();
		z5.setName("服务人员");
		fuwudiaodu.add(z1);
		fuwudiaodu.add(z2);
		fuwudiaodu.add(z3);
		fuwudiaodu.add(z4);
		fuwudiaodu.add(z5);

		list.clear();
		list.addAll(fuwudiaodu);
		adapter.notifyDataSetChanged();
	}

	private void loadData() {

		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			// pro.dismiss();
			Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				ClientParams client = new ClientParams();
				client.url = "/pavilion.do";
				StringBuffer strbuf = new StringBuffer();
				strbuf.append("method=getPavilions");
				strbuf.append("&lg=");
				if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
					strbuf.append("2");
				} else {
					strbuf.append("1");
				}
				String str = strbuf.toString();
				client.params = str;

				Type type = new TypeToken<ArrayList<ZhanGuan>>() {
				}.getType();
				NetTask<ZhanGuan> net = new NetTask<ZhanGuan>(
						hand.obtainMessage(), client, type, context);
				net.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setAllClickListener() {
		// swipe_zhanshangsj.setOnRefreshListener(new OnRefreshListener() {
		//
		// @Override
		// public void onRefresh() {
		// loadData();
		// }
		// });

	}

	@Override
	public void onClick(View v) {
		if (but_fanhui.getId() == v.getId()) {
			ZhanShangSJActivity.this.finish();
		}
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 接收广播
	 **/
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				if (BaseActivity.isAppOnForeground(context)) {
					isActive = false;
					TimeStart = System.currentTimeMillis() / 1000;
				}
			}
		}
	};

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if (mBroadcastReceiver != null) {
			this.unregisterReceiver(mBroadcastReceiver);
		}
		MyApplication.remove(this);
		super.onDestroy();
	}

	/** 记录前台切后台时间 */
	private Long TimeStart;
	/*
	 * * 记录后台切前台时间
	 */
	private Long TimeEnd;

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
