package com.gheng.exhibit.view.checkin.checkin;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.JiuDianList;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.JiuDianAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class JiuDianActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.swipe_jiudian)
	SwipeRefreshLayout swipe_jiudian;
	@ViewInject(R.id.emptyView)
	private TextView emptyView;
	@ViewInject(R.id.in_title)
	TextView in_title;
	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;
	ProgressDialog pro;
	
	public LinearLayoutManager llm;
	@ViewInject(R.id.jiudian_rv)
	private RecyclerView jiudian_rv;
	ArrayList<JiuDianList> datas = new ArrayList<JiuDianList>();
	private JiuDianAdapter adapter;
	private Boolean isActive = true;
	User parent;
	public SharedPreferences sp;
	String vipid = null;
	public Gson g = new Gson();
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			swipe_jiudian.setRefreshing(false);
			swipe_jiudian.setEnabled(true);
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				System.out.println("aaa:JiuDianActivity:" + "进入handler");
				ResponseBody<JiuDianList> res = (ResponseBody<JiuDianList>) msg.obj; // 首先创建接收方法
				// 其实就是拿到ResponseBody<HuiYiRiChengRiQiHuoQu>里面的list对象
				datas.clear();
				datas.addAll(res.list);
				adapter.notifyDataSetChanged();
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				Toast.makeText(context, BaseActivity.getLanguageString("错误"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 3) { // 表示没有拿到列表数据
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(JiuDianActivity.this);
			}
			pro.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jiudian);
		ViewUtils.inject(this);
		registerBoradcastReceiver();// 注册广播
		context = JiuDianActivity.this;
		vipid = getIntent().getStringExtra("vipid");
		process();
		loadData();
		setAllClickListener();
	}

	private void process() {
		llm = new LinearLayoutManager(this);// 设置RecyclerView的布局管理器（必须设置，否则RecyclerView会崩溃）
		llm.setOrientation(LinearLayoutManager.VERTICAL);// 设置横向或纵向展示item
		jiudian_rv.setLayoutManager(llm);// 为RecyclerView配置布局管理器 必须手动配置！
		adapter = new JiuDianAdapter(datas, context);// 初始化用户自定义适配器
		jiudian_rv.setAdapter(adapter);// 为recyclerView设置适配器
		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失

		swipe_jiudian.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		but_fanhui.setOnClickListener(this);
		emptyView.setVisibility(View.GONE);
	}

	private void loadData() {

		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			pro.dismiss();
			Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 查找
		try {
			DbUtils db = DbUtils.create(this);
			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		try {
			ClientParams client = new ClientParams();
			client.url = "/vipmembers.do";
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=vipJdsDetails&vipid=");
			if (vipid != null) {
				strbuf.append(vipid);
			} else {
				if (parent.getUserjuese().equals("3")) {
					strbuf.append(parent.getUserjuese().equals("3") ? Constant
							.decode(Constant.key, parent.getVipid()) : Constant
							.decode(Constant.key, parent.getUserId()));
				} else {
					strbuf.append(Constant.decode(Constant.key,
							parent.getUserId()));
				}
			}

			strbuf.append("&userid=");
			strbuf.append(Constant.decode(Constant.key, parent.getUserId()));
			strbuf.append("&lg=");
			if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				strbuf.append("2");
			} else {
				strbuf.append("1");
			}
			String str = strbuf.toString();
			client.params = str;
			// (Message msg, ClientParams params, Type typeToken,
			// Context context, int cache)
			Type type = new TypeToken<ArrayList<JiuDianList>>() {
			}.getType();
			NetTask<JiuDianList> net = new NetTask<JiuDianList>(
					hand.obtainMessage(), client, type, context, 5);
			net.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setAllClickListener() {
		swipe_jiudian.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData();
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (but_fanhui.getId() == v.getId()) {
			JiuDianActivity.this.finish();
		}
	}

	@Override
	protected void setI18nValue() {
		emptyView.setText(BaseActivity.getLanguageString("暂时没有数据"));
		in_title.setText(BaseActivity.getLanguageString("酒店信息"));
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
