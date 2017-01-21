package com.gheng.exhibit.view.viplist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.IsRead;
import com.gheng.exhibit.http.body.response.VipPersonData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.checkin.XingChengAnPaiActivity;
import com.gheng.exhibit.view.checkin.checkin.JiuDianActivity;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

//vip服务界面
public class VIPXiangQingActivity extends BaseActivity implements
		OnClickListener {
	private Boolean isActive = true;
	@ViewInject(R.id.in_title)
	TextView in_title;
	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;
	@ViewInject(R.id.vip_image)
	ImageView vip_image;
	@ViewInject(R.id.vip_name)
	TextView vip_name;
	@ViewInject(R.id.vip_zhiwei)
	TextView vip_zhiwei;
	@ViewInject(R.id.vip_xingcheng)
	LinearLayout vip_xingcheng;
	@ViewInject(R.id.vip_jiudian)
	LinearLayout vip_jiuidian;
	@ViewInject(R.id.vip_sixing)
	LinearLayout vip_sixing;
	@ViewInject(R.id.tv_vipxingcheng)
	TextView tv_vipxingcheng;
	@ViewInject(R.id.tv_vipjiudian)
	TextView tv_vipjiudian;
	@ViewInject(R.id.tv_vipsixing)
	TextView tv_vipsixing;
	@ViewInject(R.id.tv_vipisread)
	TextView tv_vipisread;
	Context context;
	private VipPersonData vipperson;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options_vipiocn;

	private Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 0) {
				System.out.println("aaa:VIPXiang:hand");
				IsRead isread = (IsRead) msg.obj;
				if (isread.state.equals("0")) {
					tv_vipisread.setTextColor(getResources().getColor(
							R.color.isnotreadplan));
					tv_vipisread.setText(getLanguageString("未读"));
				} else {
					tv_vipisread.setTextColor(getResources().getColor(
							R.color.isreadplan));
					tv_vipisread.setText(getLanguageString("已读"));
				}
			} else if (msg.what == 1) {
				Toast.makeText(context, R.string.network, Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 2) {
				Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(VIPXiangQingActivity.this);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vipxiangqing);
		ViewUtils.inject(this);
		registerBoradcastReceiver();// 注册广播
		context = VIPXiangQingActivity.this;

		vipperson = (VipPersonData) getIntent()
				.getSerializableExtra("vip_bean");
		process();
		loadData();
		setAllClickListener();
	}

	private void loadData() {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 访问网络获取数据
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/viptrips.do"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer(); // 封装需要请求的字段
		strbuf.append("method=isRead&vipid="); // 请求的字段名
		strbuf.append(vipperson.id);

		String str = strbuf.toString(); // 转换成String类型
		client.params = str; // 把请求的参数封装到params 这个属性里面
		NetTask<IsRead> net = new NetTask<IsRead>(hand.obtainMessage(), client,
				IsRead.class, new IsRead(), context);

		net.execute(); // 相当于线程的Star方法 开始运行
	}

	private void process() {
		but_fanhui.setOnClickListener(this);
		vip_xingcheng.setOnClickListener(this);
		vip_jiuidian.setOnClickListener(this);
		vip_sixing.setOnClickListener(this);
		options_vipiocn = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
				.build();

		imageLoader
				.displayImage(vipperson.photourl, vip_image, options_vipiocn);
	}

	private void setAllClickListener() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_fanhui:
			VIPXiangQingActivity.this.finish();
			break;
		case R.id.vip_xingcheng:
			Intent xingcheng = new Intent(VIPXiangQingActivity.this,
					XingChengAnPaiActivity.class);
			xingcheng.putExtra("vipid", vipperson.id);
			startActivity(xingcheng);
			break;
		case R.id.vip_jiudian:
			Intent jiudian = new Intent(VIPXiangQingActivity.this,
					JiuDianActivity.class);
			jiudian.putExtra("vipid", vipperson.id);
			startActivity(jiudian);
			break;
		case R.id.vip_sixing:
			Intent sixing = new Intent(VIPXiangQingActivity.this,
					FuWuSiXingActivity.class);
			sixing.putExtra("serviceid", vipperson.id);
			sixing.putExtra("servicename", vipperson.username);
			sixing.putExtra("servicephoto", vipperson.photourl);
			startActivity(sixing);
			break;
		default:
			break;
		}
	}

	@Override
	protected void setI18nValue() {
		vip_name.setText(vipperson.username);
		vip_zhiwei.setText(vipperson.job);
		in_title.setText(BaseActivity.getLanguageString("嘉宾服务"));
		tv_vipxingcheng.setText(getLanguageString("行程安排"));
		tv_vipjiudian.setText(getLanguageString("酒店信息"));
		tv_vipsixing.setText(getLanguageString("消息沟通"));

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
