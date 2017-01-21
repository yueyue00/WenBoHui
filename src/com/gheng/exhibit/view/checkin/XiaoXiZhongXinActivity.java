package com.gheng.exhibit.view.checkin;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForXiaoXiZhongXin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.AsyncTaskForGetXiaoXin;
import com.hebg3.mxy.utils.AsyncTaskForSwispLayoutRefreshStop;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.XiaoXiPojo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class XiaoXiZhongXinActivity extends BaseActivity implements
		OnRefreshListener, OnClickListener {

	@ViewInject(R.id.but_fanhui)
	public Button but_fanhui;
	@ViewInject(R.id.xiaoxizhongxinlistswiperefreshlayout)
	public SwipeRefreshLayout swipe;
	@ViewInject(R.id.xiaoxizhongxinlistrecyclerview)
	public RecyclerView rv;
	@ViewInject(R.id.nodatafound)
	TextView nodatafound;
	@ViewInject(R.id.in_title)
	TextView in_title;

	private Boolean isActive = true;
	public Context context = this;

	public LinearLayoutManager llm;
	public AdapterForXiaoXiZhongXin adapter;
	public ArrayList<XiaoXiPojo> list = new ArrayList<XiaoXiPojo>();// 消息提醒数据集合
	public int pagecount = 1;// 总页数
	public int pagenum = 1;// 当前页
	public ProgressDialog pd;
	public int loadmode = 0;// 0 下拉刷新 1 上提分页
	public int isloading = 0;// 是否正在加载数据 0未加载 1正在加载
	public String userid = "";
	public boolean isfirsttimereuest = true;

	public String xiaoxicachename = "tongzhicache";
	public SharedPreferences sp;
	public Gson g = new Gson();

	Handler h = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			isloading = 0;
			swipe.setRefreshing(false);
			swipe.setEnabled(true);
			if (pd != null) {
				pd.dismiss();
			}
			if (msg.what == 500) {// cookie超时
				BaseActivity.gotoLoginPage(XiaoXiZhongXinActivity.this);
			}
			if (msg.what == -1) {// 查询失败
				if (loadmode == 1) {
					pagenum--;
				}
				Toast.makeText(XiaoXiZhongXinActivity.this,
						getLanguageString("请求失败"), Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 0) {// 没数据
				pagecount = msg.arg1;
				if (loadmode == 1) {
					pagenum--;
				}
				if (list.size() < 1 && loadmode == 0) {// 下拉刷新没有数据才显示没有数据tv
					nodatafound.setVisibility(View.VISIBLE);
					list.clear();
					adapter.notifyDataSetChanged();
					if (!isfirsttimereuest) {
						Toast.makeText(XiaoXiZhongXinActivity.this,
								getLanguageString("暂时没有数据"), Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
			if (msg.what == 1) {// 有数据
				pagecount = msg.arg1;
				if (loadmode == 0) {
					list.clear();
				}
				list.addAll((ArrayList<XiaoXiPojo>) msg.obj);
				nodatafound.setVisibility(View.INVISIBLE);
				adapter.notifyDataSetChanged();
			}
			isfirsttimereuest = false;// 第一次请求结束，变成false
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaoxizhongxin);
		registerBoradcastReceiver();// 注册广播
		nodatafound.setText(getLanguageString("暂时没有通知"));

		sp = context.getSharedPreferences(xiaoxicachename,
				Activity.MODE_PRIVATE);

		// 查找
		User Parent = null;
		try {
			DbUtils db = DbUtils.create(context);
			Parent = db.findFirst(Selector.from(User.class).where("id", "=","1"));
			userid = Constant.decode(Constant.key,Parent.getUserId());
			if (userid == null) {
				userid = "";
			}
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		but_fanhui = (Button) findViewById(R.id.but_fanhui);
		but_fanhui.setOnClickListener(this);

		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));
		nodatafound.setVisibility(View.INVISIBLE);

		rv.setHasFixedSize(true);// 强制item高度一致，加强加载效率
		rv.addItemDecoration(// 为RecyclerView添加divider
		new HorizontalDividerItemDecoration.Builder(this)
				.color(this.getResources().getColor(
						R.color.xiaoxizhongxinrecyclerviewdivider))
				.size(getResources().getDimensionPixelSize(
						R.dimen.shineidingweichangguanlistdivider1))
				.margin(getResources().getDimensionPixelSize(
						R.dimen.title_padding),
						getResources().getDimensionPixelSize(
								R.dimen.title_padding)).build());

		llm = new LinearLayoutManager(this);// 设置RecyclerView的布局管理器（必须设置，否则RecyclerView会崩溃）
		llm.setOrientation(LinearLayoutManager.VERTICAL);// 设置横向或纵向展示item
		rv.setLayoutManager(llm);// 为RecyclerView配置布局管理器 必须手动配置！
		rv.addOnScrollListener(new rvonscrolllistener());
		adapter = new AdapterForXiaoXiZhongXin(this, list);// 初始化用户自定义适配器
		rv.setAdapter(adapter);// 为recyclerView设置适配器

		goRequest(pagenum);
	}

	/**
	 * 发起请求获取数据 pagenum:请求的页码 或者是1 或者是当前页的下一页，传递参数前要判断
	 */
	public synchronized void goRequest(int pagenum) {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			if (getDataFromCache(pagenum) != null) {
				if (loadmode == 1) {
					list.addAll(getDataFromCache(pagenum));
					adapter.notifyDataSetChanged();
				} else {
					list.clear();
					list.addAll(getDataFromCache(pagenum));
					adapter.notifyDataSetChanged();
				}
			} else {
				if (loadmode == 1) {
					this.pagenum--;
				}
				Toast.makeText(this, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			}
			return;
		} else {
			isloading = 1;
			pd = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			AsyncTaskForGetXiaoXin at = new AsyncTaskForGetXiaoXin(
					h.obtainMessage(), pagenum, userid,
					this.getApplicationContext(),30);
			at.execute(1);
		}
	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		in_title.setText(getLanguageString("通知中心"));
	}

	@Override
	// swipelayout的下拉刷新监听方法
	public void onRefresh() {
		// TODO Auto-generated method stub
		swipe.setEnabled(false);
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			AsyncTaskForSwispLayoutRefreshStop at = new AsyncTaskForSwispLayoutRefreshStop(
					this.h.obtainMessage());
			at.execute(1);
			Toast.makeText(this, getLanguageString("网络不给力"), Toast.LENGTH_SHORT)
					.show();
		} else {
			loadmode = 0;
			pagenum = 1;
			goRequest(pagenum);
		}
	}

	/**
	 * RecyclerView 的滑动监听事件，用来判断是否拉倒最后一项，自动加载下一页
	 */
	class rvonscrolllistener extends RecyclerView.OnScrollListener {

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			if (!IsWebCanBeUse.isWebCanBeUse(getApplicationContext())) {
				if (llm.findLastCompletelyVisibleItemPosition() == list.size() - 1) {
					loadmode = 1;
					pagenum++;
					goRequest(pagenum);
				}
			} else {
				if (llm.findLastCompletelyVisibleItemPosition() == list.size() - 1) {// 显示到最后一行启动线程加载下一页
					if (pagenum + 1 <= pagecount && isloading == 0) {
						loadmode = 1;
						pagenum++;
						goRequest(pagenum);
						System.out.println("请求下一页");
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == but_fanhui) {
			this.finish();
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

	/**
	 * 从缓存获取json解析成对象并返回
	 * 
	 * @param pagenum
	 *            页码
	 * @return 解析后的数据集合
	 */
	public ArrayList<XiaoXiPojo> getDataFromCache(int pagenum) {
		if (!sp.getString("pagenum" + pagenum, "").equals("")) {
			Type type = new TypeToken<ArrayList<XiaoXiPojo>>() {
			}.getType();// 设置集合type
			ArrayList<XiaoXiPojo> infos;
			try {
				infos = g.fromJson(Constant.decode(Constant.key,sp.getString("pagenum" + pagenum,"")), type);
				return infos;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
}
