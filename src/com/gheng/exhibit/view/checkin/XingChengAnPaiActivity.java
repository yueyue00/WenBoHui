package com.gheng.exhibit.view.checkin;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.ScheduleListData;
import com.gheng.exhibit.http.body.response.ScheduleListSupportData;
import com.gheng.exhibit.http.body.response.VipXingCheng;
import com.gheng.exhibit.http.body.response.VipXingChengGuest;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.GuiBinXingChengAdapter;
import com.gheng.exhibit.view.adapter.StickHeaderListViewAdapterForXCAP;
import com.gheng.exhibit.view.adapter.StickyHeaderListViewForVip;
import com.gheng.exhibit.view.checkin.checkin.HuiYiRiChengSearchActivity;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.iadiae.acquaintancehelp.view.stickylistheaders.StickyListHeadersListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smartdot.wenbo.huiyi.R;

/**
 * 行程安排
 * 
 * @author wanglei
 */
public class XingChengAnPaiActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener {

//	@ViewInject(R.id.lv)
//	private PinnedHeaderExpandableListView lv;
	@ViewInject(R.id.xcap_stickyHeaderview)
	private StickyListHeadersListView stickyListHeadersListView;

	@ViewInject(R.id.tv_time)
	private TextView tv_time;

	/** 下拉刷新空间 */
	@ViewInject(R.id.swipe_guibinxingcheng)
	SwipeRefreshLayout swipe_guibinxingcheng;

	private GuiBinXingChengAdapter adapter;
	private StickHeaderListViewAdapterForXCAP stickAdapter;

	private Selector selector = null;

	@ViewInject(R.id.emptyView)
	private TextView emptyView;

	private Boolean isActive = true;

	User parent;

	@ViewInject(R.id.in_title)
	TextView in_title;

	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;

	ProgressDialog pro;
    String title;
	String tripid;
	String vipid = null;
	String vipxingchengname = "vipxingcheng";
	public SharedPreferences sp;
	public Gson g = new Gson();

	/**
	 * 贵宾行程数据返回
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			swipe_guibinxingcheng.setRefreshing(false);
			swipe_guibinxingcheng.setEnabled(true);
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据

				ResponseBody<VipXingCheng> res = (ResponseBody<VipXingCheng>) msg.obj; // 首先创建接收方法
				// 其实就是拿到ResponseBody<HuiYiRiChengRiQiHuoQu>里面的list对象
//				adapter.setSmallPorurl(res.base.smallPorurl);
				System.out.println("aaa:GuiBinXingChengActivity:jsonString:"
						+ "res.base.smallPorurl");
//				adapter.getDatas().clear();
//				adapter.setDatas(res.list);
//				adapter.notifyDataSetChanged();
				//stickHeaderListView
				stickAdapter = new StickHeaderListViewAdapterForXCAP(XingChengAnPaiActivity.this, res.list);
				stickyListHeadersListView.setAdapter(stickAdapter);
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				Toast.makeText(context, getLanguageString("您未绑定行程,请联系管理员"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 3) { // 表示没有拿到列表数据
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(XingChengAnPaiActivity.this);
			}
			pro.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guibinxingcheng);
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		registerBoradcastReceiver();// 注册广播

		sp = getSharedPreferences(vipxingchengname, Activity.MODE_PRIVATE);
		/**
		 * 请求已读接口
		 */
		new Thread() {
			public void run() {
				// 以下内容将来会是网络访问获取数据
				User user = null;
				// 查找
				try {
					DbUtils db = DbUtils.create(context);
					user = db.findFirst(Selector.from(User.class).where("id",
							"=", "1"));
					db.close();
				} catch (DbException e) {
					e.printStackTrace();
				}
				ClientParams client = new ClientParams();
				client.url = "/viptrips.do";
				StringBuffer strbuf = new StringBuffer();
				strbuf.append("method=viewTripStatistics&vipid=");
				try {
					strbuf.append(Constant.decode(Constant.key,
							user.getUserId()));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String str = strbuf.toString();
				System.out.println(str);
				client.params = str;

				try {
					HttpURLConnection connection = null;
					OutputStream os = null;
					connection = (HttpURLConnection) new URL(client.domain
							+ client.url).openConnection();
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setUseCaches(false);// 不使用缓存
					connection.setRequestMethod(client.http_method);
					connection.setConnectTimeout(10000);
					connection.setReadTimeout(10000);
					connection.connect();
					os = connection.getOutputStream();
					System.out.println(client.params);
					os.write(client.params.getBytes("UTF-8"));
					os.flush();
					System.out.println("connection.getResponseCode() = "
							+ connection.getResponseCode());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		swipe_guibinxingcheng.setOnRefreshListener(this);
		swipe_guibinxingcheng.setColorSchemeColors(this.getResources()
				.getColor(R.color.swiperefreshlayoutcolor));
		List<ScheduleListData> entity = new ArrayList<ScheduleListData>();
		for (int i = 0; i < 6; i++) {
			ScheduleListData sche = new ScheduleListData();
			sche.title = "hah";

			for (int j = 0; j < 4; j++) {
				ScheduleListSupportData sc = new ScheduleListSupportData();
				sc.id = 1;
				sche.rlist.add(sc);
			}
			entity.add(sche);
		}
		tripid = intent.getStringExtra("tripid");
		vipid = intent.getStringExtra("vipid");
//		adapter = new GuiBinXingChengAdapter(this, lv);
//		lv.setAdapter(adapter);

		emptyView.setVisibility(View.GONE);
		emptyView.setText(getLanguageString("暂时没有数据"));

		// 设置标题
//		in_title.setText(" "+getLanguageString("行程安排"));
		in_title.setText(title);
		// 返回键监听
		but_fanhui.setOnClickListener(this);

		// 加载网络时等待对话框
		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
		try {
			loadData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadData() throws Exception {

		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			if (getDataFromCache(hand.obtainMessage()).equals("")) {

				pro.dismiss();
				Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			}
			return;
		} else {

			// 查找
			try {
				DbUtils db = DbUtils.create(this);
				parent = db.findFirst(Selector.from(User.class).where("id",
						"=", "1"));
				db.close();
			} catch (DbException e) {

				e.printStackTrace();
			}
			ClientParams client = new ClientParams();
			client.url = "/viptrips.do";
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=hyList&vipid=");
			if (vipid != null) {
				strbuf.append(vipid);
				System.out.println("aaa:XingChengAnPaiActivity:vipid" + vipid);
			} else {
				strbuf.append(parent.getUserjuese().equals("3") ? Constant
						.decode(Constant.key, parent.getVipid()) : Constant
						.decode(Constant.key, parent.getUserId()));
			}

			strbuf.append("&userid=");
			strbuf.append(Constant.decode(Constant.key, parent.getUserId()));
			if (tripid != null) {
				strbuf.append("&tripid=");
				strbuf.append(tripid);
			}
			strbuf.append("&lg=");
			if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				strbuf.append("2");
			} else {
				strbuf.append("1");
			}

			String str = strbuf.toString();
			client.params = str;
			System.out.println("aaa:GuiBinXingChengActivity:接口" + str);

			// 调用数组
			Type type = new TypeToken<ArrayList<VipXingCheng>>() { // json返回值为数组时需要创建一个Type对象
				// Json 解析
			}.getType();
			NetTask<VipXingCheng> net = new NetTask<VipXingCheng>(
					hand.obtainMessage(), client, type, this, 3); // Htpp的异步类
			net.execute(); // 相当于线程的Star方法 开始运行
		}
	}

	ResponseBody<VipXingCheng> body;

	/**
	 * 从缓存获取json解析成对象并返回
	 * 
	 * @param pagenum
	 *            页码
	 * @return 解析后的数据集合
	 */
	public String getDataFromCache(Message msg) {
		if (!sp.getString("vipxingcheng]", "").equals("")) {
			body = new ResponseBody<VipXingCheng>();
			Type type = new TypeToken<ArrayList<VipXingCheng>>() {
			}.getType();// 设置集合type
			try {
				body.list = g.fromJson(
						Constant.decode(Constant.key,
								sp.getString("vipxingcheng", "")), type);
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.obj = body;
			msg.sendToTarget();
			return "cache";
		} else {
			return "";
		}
	}

	@Override
	protected void setI18nValue() {
	}

	@OnClick(value = { R.id.edt_name })
	public void clickEdtName(View v) {
		Intent intent = new Intent(context, HuiYiRiChengSearchActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (but_fanhui.getId() == arg0.getId()) {
			XingChengAnPaiActivity.this.finish();
		}
	}

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
	/** 记录后台切前台时间 */
	private Long TimeEnd;

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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		try {
			loadData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
