package com.gheng.exhibit.view.checkin.checkin;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.FuWuZu;
import com.gheng.exhibit.http.body.response.ZhuanShuFuWu;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.GridViewAdapterForHuJiaoFW;
import com.gheng.exhibit.view.adapter.ZhuanShuFuWuAdapter;
import com.gheng.exhibit.view.support.PchiData;
import com.gheng.exhibit.view.viplist.FuWuSiXingActivity;
import com.gheng.exhibit.widget.CustomGridView;
import com.gheng.exhibit.widget.MyListView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

//专属服务-现在的呼叫服务
public class VipZhuanShuFWActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	@ViewInject(R.id.in_title)
	TextView in_title;

	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;
	@ViewInject(R.id.vip_vipxiangqing)
	LinearLayout vip_vipxiangqing;

	private Boolean isActive = true;

	/**
	 * 专属服务listview
	 */
	@ViewInject(R.id.list_vipzhuanshufuwu)
	MyListView list_vipzhuanshufuwu;
	@ViewInject(R.id.rv_hujiaofuwu)
	CustomGridView rv_hujiaofuwu;

	// 专属服务adapter
	ZhuanShuFuWuAdapter zhuanshufuwuadapter;
	GridViewAdapterForHuJiaoFW gridAdapter;
	User parent;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	public String zhuanshufuwuname = "zhuanshufuwu";
	public SharedPreferences sp;
	public Gson g = new Gson();
	ProgressDialog pro;
	ArrayList<FuWuZu> fuwuzus = new ArrayList<FuWuZu>();

	public VipZhuanShuFWActivity() {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.color.huise)
				.showImageForEmptyUri(R.color.huise)
				.showImageOnFail(R.color.huise).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.build();
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pro.dismiss();
			if (msg.what == 0) {
				ResponseBody<ZhuanShuFuWu> zhuanshufuwu = (ResponseBody<ZhuanShuFuWu>) msg.obj;
				zhuanshufuwuadapter.setList(zhuanshufuwu.list);
				zhuanshufuwuadapter.notifyDataSetChanged();

			} else if (msg.what == 1) {
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {
				Toast.makeText(context, getLanguageString("您未绑定VIP,请联系管理员"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 3) { // 表示没有拿到列表数据
				Toast.makeText(context,
						BaseActivity.getLanguageString("您未设置任何服务人员,请联系管理员"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(VipZhuanShuFWActivity.this);
			}
		}
	};
	Handler hander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				ResponseBody<FuWuZu> res = (ResponseBody<FuWuZu>) msg.obj; // 首先创建接收方法
				// 其实就是拿到ResponseBody<HuiYiRiChengRiQiHuoQu>里面的list对象
				fuwuzus.clear();
				fuwuzus.addAll(res.list);
				// 给头部添加一条
				FuWuZu fuWuZu = new FuWuZu();
				fuWuZu.setName("找出租");
				fuwuzus.add(0, fuWuZu);
				//
				gridAdapter.notifyDataSetChanged();
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				rv_hujiaofuwu.setVisibility(View.GONE);
//				Toast.makeText(context,
//						BaseActivity.getLanguageString("暂时没有数据"),
//						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确<对应于200>
				rv_hujiaofuwu.setVisibility(View.GONE);
//				Toast.makeText(context,
//						BaseActivity.getLanguageString("暂时没有数据"),
//						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 3) { // 表示没有拿到列表数据<对应300>
				rv_hujiaofuwu.setVisibility(View.GONE);
//				Toast.makeText(context,
//						BaseActivity.getLanguageString("暂时没有数据"),
//						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效<对应500>
				rv_hujiaofuwu.setVisibility(View.GONE);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vipfuwuxiangqing);
		registerBoradcastReceiver();// 注册广播

		sp = getSharedPreferences(zhuanshufuwuname, Activity.MODE_PRIVATE);
		// gridview适配器
		gridAdapter = new GridViewAdapterForHuJiaoFW(context, fuwuzus);
		rv_hujiaofuwu.setAdapter(gridAdapter);
		rv_hujiaofuwu.setNumColumns(3);
		rv_hujiaofuwu.setOnItemClickListener(this);
		// listview适配器
		zhuanshufuwuadapter = new ZhuanShuFuWuAdapter(this);
		list_vipzhuanshufuwu.setAdapter(zhuanshufuwuadapter);
		// 返回键监听
		but_fanhui.setOnClickListener(this);
		// 加载网络时等待对话框
		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
		// 查找
		try {
			DbUtils db = DbUtils.create(this);
			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {

			e.printStackTrace();
		}
		loadData();
		loadGVData();
	}

	List<PchiData> list = new ArrayList<PchiData>();

	private void loadData() {
		// TODO Auto-generated method stub
		// 以下内容将来会是网络访问获取数据
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			if (getHuiChangFromCache(hand.obtainMessage()).equals("")) {
				pro.dismiss();
				Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			}
			return;
		}
		try {
			ClientParams client = new ClientParams();
			client.url = "/vipmembers.do";
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=vipFwDetails&vipid=");

			strbuf.append(parent.getUserjuese().equals("3") ? Constant.decode(
					Constant.key, parent.getVipid()) : Constant.decode(
					Constant.key, parent.getUserId()));

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
			// 调用数组
			Type type = new TypeToken<ArrayList<ZhuanShuFuWu>>() { // json返回值为数组时需要创建一个Type对象
				// Json 解析
			}.getType();
			NetTask<ZhuanShuFuWu> net = new NetTask<ZhuanShuFuWu>(
					hand.obtainMessage(), client, type, context, 6); // Htpp的异步类
			net.execute(); // 相当于线程的Star方法 开始运行
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 加载gridview的数据
	private void loadGVData() {

		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			// pro.dismiss();
			Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			ClientParams client = new ClientParams();
			client.url = "/InfoPublish.do";
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("method=EmTel");
			strbuf.append("&lg=");
			if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				strbuf.append("2");
			} else {
				strbuf.append("1");
			}
			strbuf.append("&pagenum=");
			strbuf.append("1");
			strbuf.append("&pagesize=");
			strbuf.append("20");
			String str = strbuf.toString();
			client.params = str;

			Type type = new TypeToken<ArrayList<FuWuZu>>() {
			}.getType();
			NetTask<FuWuZu> net = new NetTask<FuWuZu>(hander.obtainMessage(),
					client, type, context);
			net.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ResponseBody<ZhuanShuFuWu> body;

	/**
	 * 从缓存按会场获取json解析成对象并返回
	 * 
	 * @param pagenum
	 *            页码
	 * @return 解析后的数据集合
	 */
	public String getHuiChangFromCache(Message msg) {

		if (!sp.getString("zhuanshufuwu", "").equals("")) {
			body = new ResponseBody<ZhuanShuFuWu>();
			Type type = new TypeToken<ArrayList<ZhuanShuFuWu>>() {
			}.getType();// 设置集合type

			try {
				body.list = g.fromJson(
						Constant.decode(Constant.key,
								sp.getString("zhuanshufuwu", "")), type);
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
		// 设置标题
		in_title.setText(getLanguageString("服务呼叫"));
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (but_fanhui.getId() == arg0.getId()) {
			VipZhuanShuFWActivity.this.finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			Intent taxi = new Intent(VipZhuanShuFWActivity.this,
					TaxisActivity.class);
			context.startActivity(taxi);
			break;
		default:
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ fuwuzus.get(position).tell));
			context.startActivity(intent);
			break;
		}
	}

	/**
	 * 拨打电话
	 * 
	 * @param num
	 */
	private void canPhone(String phonenumber) {
		System.out.println(phonenumber);
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ phonenumber));
		startActivity(intent);

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
		// vip_vipxiangqing.setBackgroundDrawable(null);
		System.gc();
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
