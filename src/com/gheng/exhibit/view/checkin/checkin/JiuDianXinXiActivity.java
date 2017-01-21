package com.gheng.exhibit.view.checkin.checkin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.JiuDianXinXi;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.map.DingWeiDaoHangActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class JiuDianXinXiActivity extends BaseActivity implements
		OnClickListener {

	/**
	 * 跟布局 手动设置处理了分辨率的背景图片
	 **/
	@ViewInject(R.id.genbuju)
	LinearLayout genbuju;

	/**
	 * 设置酒店信息标题
	 **/
	@ViewInject(R.id.in_title)
	TextView titletv;

	/**
	 * 酒店信息返回键按钮
	 **/
	@ViewInject(R.id.but_fanhui)
	Button vipbut;

	/**
	 * 酒店名称
	 **/
	@ViewInject(R.id.text_jiudianxinxi)
	TextView text_jiudianxinxi;
	/**
	 * 酒店位置地点
	 **/
	@ViewInject(R.id.text_jiudianweizhi)
	TextView text_jiudianweizhi;
	/**
	 * 房间位置
	 **/
	@ViewInject(R.id.text_fangjianweizhi)
	TextView text_fangjianweizhi;
	/**
	 * 酒店房间定位button
	 **/
	@ViewInject(R.id.linear_fangjiandingwei)
	LinearLayout but_fangjiandingwei;
	@ViewInject(R.id.imagebutton_dingwei)
	ImageButton imagebutton_dingwei;
	// /**
	// * 最外层嵌套的下拉刷新
	// **/
	// @ViewInject(R.id.jiudianxinxiswiperefreshlayout)
	// public SwipeRefreshLayout swipe;
	/**
	 * 酒店名称
	 */
	@ViewInject(R.id.text_jiudianname)
	TextView text_jiudianname;
	/**
	 * 酒店位置
	 */
	@ViewInject(R.id.text_jiudianlocation)
	TextView text_jiudianlocation;
	/**
	 * 酒店房间位置
	 */
	@ViewInject(R.id.text_viproom)
	TextView text_viproom;
	/**
	 * 酒店信息地址图片
	 */
	@ViewInject(R.id.image_jiudianweizhi)
	ImageView image_jiudianweizhi;

	private Boolean isActive = true;

	User parent;

	/**
	 * imageloader使用参数
	 */
	DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();
	private ProgressDialog pro;
	private JiuDianXinXi jiudianxinxi;
	public String jiudianxinxiname = "jiudianxinxi";
	public SharedPreferences sp;
	public Gson g = new Gson();

	public JiuDianXinXiActivity() {

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.color.qianhuise)
				.showImageForEmptyUri(R.color.qianhuise)
				.showImageOnFail(R.color.qianhuise).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				// .displayer(new RoundedBitmapDisplayer(10))//设置圆角弧度
				.build();
	}

	Handler hand = new Handler() {
		public void handleMessage(Message msg) {
			pro.dismiss();
			// swipe.setRefreshing(false);
			// swipe.setEnabled(true);
			if (msg.what == 0) {
				jiudianxinxi = (JiuDianXinXi) msg.obj;

				text_jiudianname
						.setText(getLanguageString(jiudianxinxi.jiudianname));
				text_jiudianlocation.setText(jiudianxinxi.jiudianlocation);
				text_viproom.setText(jiudianxinxi.jiudianroomid);
				// 判断酒店信息界面显示的图片 服务器返回mid有问题 所以注释
				if (jiudianxinxi.jiudianmapid
						.equals("Wuzhen Clubhouse-Healtown")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				} else if (jiudianxinxi.jiudianmapid
						.equals("Wuzhen Clubhouse-Splendid Clubhouse")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				} else if (jiudianxinxi.jiudianmapid
						.equals("Wuzhen Clubhouse-Shinetown")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				} else if (jiudianxinxi.jiudianmapid
						.equals("Dockside Boutique Hotel")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				} else if (jiudianxinxi.jiudianmapid.equals("Eden Club House")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				} else if (jiudianxinxi.jiudianmapid.equals("Waterside Resort")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				} else if (jiudianxinxi.jiudianmapid
						.equals("Tong An Hotel VIP Tower")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				} else if (jiudianxinxi.jiudianmapid
						.equals("Tong An Hotel NO.1")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				} else if (jiudianxinxi.jiudianmapid
						.equals("Tong An Hotel NO.2")) {
					image_jiudianweizhi.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_launcher));
				}

			} else if (msg.what == 1) {
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {
				Toast.makeText(context, getLanguageString("您未绑定VIP,请联系管理员"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 3) { // 表示没有拿到列表数据
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(JiuDianXinXiActivity.this);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jiudianxinxi);
		registerBoradcastReceiver();// 注册广播
		image_jiudianweizhi.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_launcher));

		sp = getSharedPreferences(jiudianxinxiname, Activity.MODE_PRIVATE);

		vipbut.setOnClickListener(this);
		but_fangjiandingwei.setOnClickListener(this);
		imagebutton_dingwei.setOnClickListener(this);

		// swipe.setOnRefreshListener(this);
		// swipe.setColorSchemeColors(this.getResources().getColor(
		// R.color.swiperefreshlayoutcolor));

		// 获取屏幕宽高3
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

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

			if (getDataFromCache(JiuDianXinXi.class,
					new JiuDianXinXiActivity(), hand.obtainMessage())
					.equals("")) {
				pro.dismiss();
				Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			}
			return;
		}

		// 查找
		try {
			DbUtils db = DbUtils.create(context);
			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		System.out.println(parent.getVipid() + "asd");

		ClientParams client = new ClientParams();
		client.url = "/vipmembers.do";
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=vipJdsDetails&vipid=");
		if (parent.getUserjuese().equals("3")) {
			strbuf.append(parent.getUserjuese().equals("3") ? Constant.decode(
					Constant.key, parent.getVipid()) : Constant.decode(
					Constant.key, parent.getUserId()));
		} else {
			strbuf.append(Constant.decode(Constant.key, parent.getUserId()));
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

		NetTask<JiuDianXinXi> net = new NetTask<JiuDianXinXi>(
				hand.obtainMessage(), client, JiuDianXinXi.class,
				new JiuDianXinXi(), context, 5);

		net.execute();
	}

	/**
	 * 从缓存获取json解析成对象并返回
	 * 
	 * @param pagenum
	 *            页码
	 * @return 解析后的数据集合
	 */
	@SuppressWarnings("unchecked")
	public String getDataFromCache(Class clazz, Object obj, Message msg) {
		if (!sp.getString("jiudianxinxi", "").equals("")) {
			try {
				obj = (Object) Constant.gson.fromJson(
						Constant.decode(Constant.key,
								sp.getString("jiudianxinxi", "")), clazz);
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.obj = obj;
			msg.sendToTarget();
			return "cache";
		} else {
			return "";
		}
	}

	@Override
	protected void setI18nValue() {

		titletv.setText(getLanguageString("酒店信息"));
		text_jiudianxinxi.setText(getLanguageString("下榻酒店") + ":");
		text_jiudianweizhi.setText(getLanguageString("酒店位置地点") + ":");
		text_fangjianweizhi.setText(getLanguageString("您的房间") + ":");
	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == vipbut.getId()) {// 退出按钮监听
			JiuDianXinXiActivity.this.finish();
		} else if (arg0.getId() == but_fangjiandingwei.getId()
				|| arg0.getId() == imagebutton_dingwei.getId()) {// 酒店房间定位监听
			if (jiudianxinxi != null && jiudianxinxi.jiudianname != null
					&& jiudianxinxi.jiudianmapid != null
					&& !jiudianxinxi.jiudianname.equals("")
					&& !jiudianxinxi.jiudianmapid.equals("")) {
				// mapid不可以传汉字 只能传程序内室内地图文件夹名称！
				// 楼层要传相对应的楼层、目前已经确定的九个酒店最高的为四层！
				Intent i = new Intent();
				if (jiudianxinxi.jiudianfloor.equals("")) {
					i.putExtra("openfloornum", 1);// 打开会场所在楼层
				} else {
					int floor = Integer.parseInt(jiudianxinxi.jiudianfloor);
					i.putExtra("openfloornum", floor);// 打开会场所在楼层
				}
				i.putExtra("openmappathid", jiudianxinxi.jiudianmapid);// 枕水酒店id
				i.putExtra("searchspacename", jiudianxinxi.jiudianroomid);// 搜索space
				i.putExtra(
						"titletv",
						getLanguageString(getLanguageString(jiudianxinxi.jiudianname)));// 酒店名称
				i.setClass(context, DingWeiDaoHangActivity.class);
				context.startActivity(i);
			}

		}
	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		MyApplication.remove(this);
		if (mBroadcastReceiver != null) {
			this.unregisterReceiver(mBroadcastReceiver);
		}
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

	// @Override
	// public void onRefresh() {
	//
	// swipe.setEnabled(false);
	//
	// loadData();
	//
	// }
}