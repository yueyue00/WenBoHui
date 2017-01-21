package com.gheng.exhibit.rongyun.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.ContactBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForContactSearchResult;
import com.gheng.exhibit.view.adapter.AdapterForDaHuiRiChengSearchRecyclerView;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class ContactSearchActivity extends BaseActivity implements
		OnClickListener {

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
	RecyclerView searchresultrv;// 搜搜结果rv

	@ViewInject(R.id.searchhistorylistrv)
	RecyclerView searchhistorylistrv;// 搜索历史rv
	@ViewInject(R.id.zuijinlayout)
	RelativeLayout zuijinlayout;// 最近搜索布局 点击搜索或点击搜索历史item，隐藏该布局

	@ViewInject(R.id.nodatafound)
	TextView nodatafound;

	ProgressDialog pd;
	public Context context = this;

	private Boolean isActive = true;
	// 搜索历史记录rv布局管理器-----------------------------------------
	LinearLayoutManager llm_historysearchlist;
	// 搜索历结果rv布局管理器
	LinearLayoutManager llm_search_result;
	// 搜索历史记录=================================================
	ArrayList<String> historysearchlist = new ArrayList<String>();
	// 搜索结果
	ArrayList<ContactBean> search_result = new ArrayList<ContactBean>();
	// 搜索历史记录 rv adapter---------------------------------------
	AdapterForDaHuiRiChengSearchRecyclerView adapter_historysearchlist;
	// 搜索结果 rv adapter
	AdapterForContactSearchResult adapter_search_result;

	SharedPreferences sp;
	Editor e;
	String spname = "contactsearchactivity";
	String keyname = "contactsearchhistory";
	User parent = null;

	Handler search_handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			searchbutton.setOnClickListener(ContactSearchActivity.this);
			try {
				if (pd != null) {
					pd.dismiss();
				}
				switch (msg.what) {
				case 0:// 融云获取token时返回内容
					ResponseBody<ContactBean> res = (ResponseBody<ContactBean>) msg.obj; // 首先创建接收方法
					search_result.clear();
					search_result.addAll(res.list);

					nodatafound.setVisibility(View.GONE);
					searchresultrv.setVisibility(View.VISIBLE);
					adapter_search_result.notifyDataSetChanged();
					break;
				case 1:
					Toast.makeText(context, "请求错误！", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					nodatafound.setVisibility(View.VISIBLE);
					searchresultrv.setVisibility(View.GONE);
					Toast.makeText(context, "数据为空！", Toast.LENGTH_SHORT).show();
					break;
				case 4:
					Toast.makeText(context, "cookie失效，请求超时！",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MyApplication.add(this);
		 //透明状态栏  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  
            Window window = getWindow();  
            // Translucent status bar  
            window.setFlags(  
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
        }  
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_huiyirichengsearch);
		registerBoradcastReceiver();// 注册广播
		nodatafound.setText(getLanguageString("暂时没有数据"));

		// 查找
		try {
			DbUtils db = DbUtils.create(context);
			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}

		sp = this.getSharedPreferences(spname, Activity.MODE_PRIVATE);
		e = sp.edit();
		edittext.setOnClickListener(this);

		searchbutton.setOnClickListener(this);
		goback.setOnClickListener(this);
		searchresultrv.setVisibility(View.GONE);
		zuijinlayout.setVisibility(View.GONE);
		nodatafound.setVisibility(View.GONE);

		// 初始化搜索历史记录rv
		searchhistorylistrv.setHasFixedSize(true);
		llm_historysearchlist = new LinearLayoutManager(this);
		llm_historysearchlist.setOrientation(LinearLayoutManager.VERTICAL);
		searchhistorylistrv.setLayoutManager(llm_historysearchlist);
		searchhistorylistrv.setHasFixedSize(true);// 强制item高度一致，加强加载效率

		searchhistorylistrv.addItemDecoration(// 为RecyclerView添加divider
				new HorizontalDividerItemDecoration.Builder(this)
						.color(this.getResources().getColor(
								R.color.searchhuiyirvfengexian))
						.size(getResources().getDimensionPixelSize(
								R.dimen.half1dp))
						.margin(getResources().getDimensionPixelSize(
								R.dimen.recylerviewitemdivider_pchi),
								getResources().getDimensionPixelSize(
										R.dimen.recylerviewitemdivider_pchi))
						.build());

		adapter_historysearchlist = new AdapterForDaHuiRiChengSearchRecyclerView(
				this, historysearchlist);
		searchhistorylistrv.setAdapter(adapter_historysearchlist);

		// 初始化搜索结果rv
		searchresultrv.setHasFixedSize(true);
		llm_search_result = new LinearLayoutManager(this);
		llm_search_result.setOrientation(LinearLayoutManager.VERTICAL);
		searchresultrv.setLayoutManager(llm_search_result);
		searchresultrv.setHasFixedSize(true);// 强制item高度一致，加强加载效率

		searchresultrv.addItemDecoration(// 为RecyclerView添加divider
				new HorizontalDividerItemDecoration.Builder(this)
						.color(this.getResources().getColor(R.color.xiaoguo_bg_search_contact))
						.size(getResources().getDimensionPixelSize(
								R.dimen.radio_button_stroke_border))
						.margin(getResources().getDimensionPixelSize(
								R.dimen.title_padding),
								getResources().getDimensionPixelSize(
										R.dimen.title_padding)).build());

		adapter_search_result = new AdapterForContactSearchResult(this,
				search_result);
		searchresultrv.setAdapter(adapter_search_result);
		// 检查搜索历史记录
		try {
			showHistroyRecords();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 点击编辑框，展示历史记录
	 * 
	 * @throws Exception
	 */
	public void showHistroyRecords() throws Exception {
		String[] records = null;
		String record = Constant.decode(Constant.key, sp.getString(keyname, "")
				.trim());
		if (!record.equals("")) {
			records = record.split(",");
		} else {
			return;
		}
		if (records.length > 0) {// 有历史搜索记录
			historysearchlist.clear();
			for (int i = records.length - 1; i >= 0; i--) {
				historysearchlist.add(records[i]);
			}
			historysearchlist.add(getLanguageString("清空历史数据"));
			// 显示历史搜索记录列表
			adapter_historysearchlist.notifyDataSetChanged();
			zuijinlayout.setVisibility(View.VISIBLE);
		} else {// 没有历史搜索记录，隐藏搜索记录层
			zuijinlayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 历史搜索记录被点击后，在adapter中执行本方法
	 * 
	 * @throws Exception
	 */
	public void itemclicked(int position) throws Exception {
		if (position == historysearchlist.size() - 1) {// 用户点击"清空历史数据"
			e.putString(keyname, "");
			e.apply();
			zuijinlayout.setVisibility(View.GONE);
		} else {
			String clickcontent = historysearchlist.get(position) + ",";
			String record = Constant.decode(Constant.key,
					sp.getString(keyname, "").trim());
			record = record.replace(historysearchlist.get(position) + ",", "");
			e.putString(keyname,
					Constant.encode(Constant.key, record + clickcontent));
			e.apply();
			zuijinlayout.setVisibility(View.GONE);
			edittext.setText(historysearchlist.get(position));
			goSearch(edittext.getEditableText().toString().trim());
		}
	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		titletv.setText(getLanguageString("服务人员搜索"));
		searchbutton.setText(getLanguageString("搜索"));
		zuijinsousuotv.setText(getLanguageString("最近搜索"));
		edittext.setHint(getLanguageString("请输入用户名称"));

	}

	/**
	 * 根据内容请求服务器
	 * 
	 * @throws Exception
	 */
	public void goSearch(String content) throws Exception {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, getLanguageString("网络不可用"), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		searchbutton.setOnClickListener(null);
		pd = ProgressDialog.show(this, "",BaseActivity.getLanguageString("加载中..."));
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		// 下面是网络请求
		NetTask<ContactBean> aa;
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=getUserinfo&userId=");
		strbuf.append(Constant.decode(Constant.key, parent.getUserId()));
		strbuf.append("&userName=");
		strbuf.append(content);

		String str = strbuf.toString();
		client.params = str;

		Type type = new TypeToken<ArrayList<ContactBean>>() {
		}.getType();
		NetTask<ContactBean> net = new NetTask<ContactBean>(
				search_handler.obtainMessage(), client, type, context);
		net.execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == goback) {
			this.finish();
		}
		if (v == searchbutton) {// 点击搜索按钮
			String searchcontent = edittext.getEditableText().toString().trim();// 去掉前后空格
			if (null == searchcontent || searchcontent.equals("")) {
				Toast.makeText(this, getLanguageString("请输入检索内容"),
						Toast.LENGTH_SHORT).show();
				return;
			} else {// 将搜索记录保存到ShardPreference中
				String history;
				try {
					history = Constant.decode(Constant.key,
							sp.getString(keyname, "").trim());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					history = "";
				}// 用，隔开
				if (!history.contains(searchcontent)) {// 去重复
					history = history + searchcontent + ",";
					e.putString(keyname, Constant.encode(Constant.key, history));
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
		if (v == edittext) {// 点击输入框，弹出历史搜索记录
			if (zuijinlayout.getVisibility() == View.VISIBLE) {
				return;
			} else {
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
		if (zuijinlayout.getVisibility() == View.VISIBLE) {
			zuijinlayout.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
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
}
