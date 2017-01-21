package com.gheng.exhibit.view.checkin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.IfAttention;
import com.gheng.exhibit.http.body.response.PchiDetailsRespnseData;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.map.DingWeiDaoHangActivity;
import com.hebg3.mxy.utils.NsTextView;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 关注的会议详情页面
 * 
 * @author renzhihua
 */
public class MineAttentionPchiDetailsActivity extends BaseActivity implements
		android.view.View.OnClickListener {

	/**
	 * 设置大会详情标题
	 **/
	@ViewInject(R.id.in_title)
	TextView titletv;

	/**
	 * 大会详情返回键按钮
	 **/
	@ViewInject(R.id.but_fanhui)
	Button vipbut;
	/**
	 * 会议详情关注按钮
	 **/
	@ViewInject(R.id.attention_button)
	ImageButton attention_button;
	@ViewInject(R.id.share_tv)
	TextView share_tv;

	@ViewInject(R.id.pchi_details_iv)
	private ImageView pchi_details_iv;

	@ViewInject(R.id.pchi_details_tv)
	private TextView pchi_details_tv;

	@ViewInject(R.id.pchi_details_pchitime)
	private TextView pchi_details_pchitime;

	@ViewInject(R.id.pchi_details_adress)
	private TextView pchi_details_adress;

	@ViewInject(R.id.pchi_details_jianjie)
	private TextView pchi_details_jianjie;

	@ViewInject(R.id.pchi_details_zhujiangren)
	private TextView pchi_details_zhujiangren;

	@ViewInject(R.id.pchi_details_yiti)
	private TextView pchi_details_yiti;

	@ViewInject(R.id.pchi_details_meettingtitle)
	TextView pchi_details_meettingtitle;

	@ViewInject(R.id.pchi_details_date)
	TextView pchi_details_date;

	@ViewInject(R.id.pchi_details_adressdetails)
	TextView pchi_details_adressdetails;

	@ViewInject(R.id.pchi_details_zhujiangrenname)
	TextView pchi_details_zhujiangrenname;

	@ViewInject(R.id.pchi_details_jianjiedetails)
	TextView pchi_details_jianjiedetails;

	@ViewInject(R.id.pchi_details_yitineirong)
	NsTextView pchi_details_yitineirong;

	@ViewInject(R.id.daohangtv)
	TextView daohangtv;

	private ProgressDialog pro;

	String meettingid;

	PchiDetailsRespnseData pchidetailsrespnsedata;

	private Boolean isActive = true;

	@ViewInject(R.id.myscrollview)
	public NestedScrollView mySv;
	@ViewInject(R.id.relative_pchi_details_adressdetails)
	RelativeLayout relative_pchi_details_adressdetails;
	String suijima;
	String method = "getMeetInfoById";

	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pchi_details);
		registerBoradcastReceiver();// 注册广播
		meettingid = getIntent().getStringExtra("pchi_id"); // 获取从上一个页面得到的会议ID
		if (meettingid == null || meettingid.equals("")) {
			Toast.makeText(this, getLanguageString("无效的会议ID"),
					Toast.LENGTH_SHORT).show();
			this.finish();
		}
		if (getIntent().getStringExtra("method") != null
				&& !getIntent().getStringExtra("method").equals("")) {
			method = getIntent().getStringExtra("method");
		}
		suijima = getIntent().getStringExtra("suijima");
		vipbut.setOnClickListener(this);
		attention_button.setOnClickListener(this);
		share_tv.setOnClickListener(this);
		relative_pchi_details_adressdetails.setOnClickListener(this);
		attention_button.setVisibility(View.VISIBLE);
		share_tv.setVisibility(View.VISIBLE);

		/**
		 * 会议详情页面从服务器接收数据
		 */
		// 加载网络时等待对话框
		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
		try {
			loadDetailsData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取会议详情数据实现方法，请求网络数据
	 * 
	 * @throws Exception
	 */
	private void loadDetailsData() throws Exception {
		// 查找
		User Parent = null;
		try {
			DbUtils db = DbUtils.create(context);

			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {

			e.printStackTrace();
		}

		// 访问网络获取数据
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/meetings.do"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=" + method + "&meettingid=");
		strbuf.append(meettingid);
		strbuf.append("&lg=");
		if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
			strbuf.append("2");
		} else {
			strbuf.append("1");
		}
		strbuf.append("&userid=");
		strbuf.append(Constant.decode(Constant.key, Parent.getUserId()));
		if (suijima != null) {
			strbuf.append("&suijima=");
			strbuf.append(suijima); // 接口ID
		}

		String str = strbuf.toString();
		client.params = str;

		NetTask<PchiDetailsRespnseData> net = new NetTask<PchiDetailsRespnseData>(
				handler.obtainMessage(), client, PchiDetailsRespnseData.class,
				new PchiDetailsRespnseData(), context);
		net.execute();

	}

	/**
	 * 会议详情页面的数据请求 请求网络获取数据成功后，将要进行的操作
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pro.dismiss();
			if (msg.what == 0) {

				pchidetailsrespnsedata = (PchiDetailsRespnseData) msg.obj;

				pchi_details_meettingtitle
						.setText(pchidetailsrespnsedata.meettingtitle);
				pchi_details_date.setText(pchidetailsrespnsedata.meettingtime);
				pchi_details_adressdetails
						.setText(pchidetailsrespnsedata.meettingroom);
				pchi_details_zhujiangrenname
						.setText(pchidetailsrespnsedata.meettingtalkman);
				pchi_details_jianjiedetails
						.setText(pchidetailsrespnsedata.mettingcontent);

				// 会议议题遇到||后自动换行
				// pchidetailsrespnsedata.meettingyiti =
				// pchidetailsrespnsedata.meettingyiti
				// .replace("||", "\r\n");
				System.out.print(pchidetailsrespnsedata.meettingyiti);
				pchi_details_yitineirong.isfirsttimeondraw = true;// 添加内容之前，设置为true，让textview重新执行setheight
//				pchi_details_yitineirong
//						.setText(pchidetailsrespnsedata.meettingyiti);

				// if (pchidetailsrespnsedata.isguanzhu == 0) {
				// share_tv.setText(getLanguageString("关注我"));
				// attention_button
				// .setBackgroundResource(R.drawable.wuzhen_attention);
				// } else {
				// share_tv.setText(getLanguageString("取消关注"));
				// attention_button
				// .setBackgroundResource(R.drawable.wuzhen_attention_yes);
				// }

			} else if (msg.what == 1) {
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {
				Toast.makeText(context, getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity
						.gotoLoginPage(MineAttentionPchiDetailsActivity.this);
			}
		}
	};

	@Override
	protected void setI18nValue() {
		String surname = SharedData.getString(SharedData.SURNAME);
		String name = SharedData.getString(SharedData.NAME);
		int lg = SharedData.getInt("i18n", Language.ZH);
		if (lg == Language.ZH) {
			name = surname + name;
		} else {
			name += " " + surname;
		}

		titletv.setText(getLanguageString("会议详情"));

		pchi_details_tv.setText(getLanguageString("会议名称") + ":");
		pchi_details_pchitime.setText(getLanguageString("会议时间") + ":");
		pchi_details_adress.setText(getLanguageString("会议地点") + ":");
		pchi_details_zhujiangren.setText(getLanguageString("主持人") + ":");
		pchi_details_jianjie.setText(getLanguageString("简介") + ":");
		pchi_details_yiti.setText(getLanguageString("议题") + ":");
		daohangtv.setText(getLanguageString("位置"));
		/* pchi_details_jiabin.setText(getLanguageString("嘉宾：")); */

	}

	@Override
	public void onClick(View v) {
		// 按钮的点击监听事件
		if (vipbut.getId() == v.getId()) {
			// 点击返回按钮之后，从会议详情页面跳转回我的关注页面
			finish();
		} else if (attention_button.getId() == v.getId()
				|| share_tv.getId() == v.getId()) {
			// 点击关注按钮之后调转到关注的实现方法
			if (pchidetailsrespnsedata != null) {
				// if (pchidetailsrespnsedata.isguanzhu == 0) {
				// try {
				// loadData();
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// } else if (pchidetailsrespnsedata.isguanzhu == 1) {
				// try {
				// loadquxiaodata();
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
			} else {
				Toast.makeText(context, getLanguageString("关注失败"),
						Toast.LENGTH_SHORT).show();
			}

		} else if (relative_pchi_details_adressdetails.getId() == v.getId()) {
			if (pchidetailsrespnsedata != null) {
				if (pchidetailsrespnsedata.meettingroom.equals("华美宫")
						|| pchidetailsrespnsedata.meettingroom
								.equals("Hua Mei Conference Hall")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Waterside Resort");// 酒店id
					i.putExtra("searchspacename", "华美宫");// 搜索space
					i.putExtra("openfloornum", 3);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("华美宫"));// 酒店名称
					i.setClass(context, DingWeiDaoHangActivity.class);
					context.startActivity(i);
				} else if (pchidetailsrespnsedata.meettingroom.equals("龙凤厅")
						|| pchidetailsrespnsedata.meettingroom
								.equals("Long Feng Banquet Hall")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Waterside Resort");// 酒店id
					i.putExtra("searchspacename", "龙凤厅");// 搜索space
					i.putExtra("openfloornum", 1);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("龙凤厅"));// 酒店名称
					i.setClass(context, DingWeiDaoHangActivity.class);
					context.startActivity(i);
				} else if (pchidetailsrespnsedata.meettingroom.equals("宫音厅")
						|| pchidetailsrespnsedata.meettingroom
								.equals("Gong Yin Hall")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Waterside Resort");// 酒店id
					i.putExtra("searchspacename", "宫音厅");// 搜索space
					i.putExtra("openfloornum", 3);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("宫音厅"));// 酒店名称
					i.setClass(context, DingWeiDaoHangActivity.class);
					context.startActivity(i);
				} else if (pchidetailsrespnsedata.meettingroom.equals("荣锦厅")
						|| pchidetailsrespnsedata.meettingroom
								.equals("Rong Jin Conference Room")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Waterside Resort");// 酒店id
					i.putExtra("searchspacename", "荣锦厅");// 搜索space
					i.putExtra("openfloornum", 2);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("荣锦厅"));// 酒店名称
					i.setClass(context, DingWeiDaoHangActivity.class);
					context.startActivity(i);
				} else if (pchidetailsrespnsedata.meettingroom.equals("国乐厅")
						|| pchidetailsrespnsedata.meettingroom
								.equals("Guo Yue Hall")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Tong An Hotel NO.1");// 酒店id
					i.putExtra("searchspacename", "国乐厅");// 搜索space
					i.putExtra("openfloornum", 1);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("国乐厅"));// 酒店名称
					i.setClass(context, DingWeiDaoHangActivity.class);
					context.startActivity(i);
				} else {
					Toast.makeText(context,
							BaseActivity.getLanguageString("该会场无地图"),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**
	 * 关注按钮点击后取消关注实现方法，请求网络数据
	 * 
	 * @throws Exception
	 */
	private void loadquxiaodata() throws Exception {
		// 查找
		User Parent = null;
		try {
			DbUtils db = DbUtils.create(context);

			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
		} catch (DbException e) {

			e.printStackTrace();
		}

		// 访问网络获取数据
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/meetings.do"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer(); // 封装需要请求的字段
		strbuf.append("method=executeAttention&userid="); // 请求的字段名 要和接口文档保持一致
		strbuf.append(Constant.decode(Constant.key, Parent.getUserId()));
		strbuf.append("&meettingid=");// 会议ID
		strbuf.append(meettingid);
		strbuf.append("&operation="); // 判断是否关注
		strbuf.append("0"); // 关注该会议

		String str = strbuf.toString(); // 转换成String类型
		client.params = str; // 把请求的参数封装到params 这个属性里面

		// 调用对象
		NetTask<IfAttention> net = new NetTask<IfAttention>(
				handquxiao.obtainMessage(), client, IfAttention.class,
				new IfAttention(), context);
		net.execute(); // 相当于线程的Star方法 开始运行

	}

	/**
	 * 取消关注请求网络获取数据成功后，将要进行的操作
	 */
	Handler handquxiao = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pro.dismiss();
			if (msg.what == 0) {
				Intent i = new Intent("guanzhuchanged");// 发广播告诉我的关注列表界面，刷新列表
				sendBroadcast(i);
				// pchidetailsrespnsedata.isguanzhu = 0;
				attention_button
						.setBackgroundResource(R.drawable.wuzhen_attention);
				share_tv.setText(getLanguageString("关注我"));

				Toast.makeText(context, getLanguageString("取消关注"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 1) {
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {
				Toast.makeText(context, getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity
						.gotoLoginPage(MineAttentionPchiDetailsActivity.this);
			}
		}
	};

	/**
	 * 关注按钮要关注的实现方法，请求网络数据
	 * 
	 * @throws Exception
	 */
	private void loadData() throws Exception {

		// 查找
		User Parent = null;
		try {
			DbUtils db = DbUtils.create(context);

			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
		} catch (DbException e) {

			e.printStackTrace();
		}
		// 访问网络获取数据
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/meetings.do"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer(); // 封装需要请求的字段
		strbuf.append("method=executeAttention&userid="); // 请求的字段名 要和接口文档保持一致
		strbuf.append(Constant.decode(Constant.key, Parent.getUserId()));
		strbuf.append("&meettingid=");// 会议ID
		strbuf.append(meettingid);
		strbuf.append("&operation="); // 判断是否关注
		strbuf.append("1"); // 关注该会议

		String str = strbuf.toString(); // 转换成String类型
		client.params = str; // 把请求的参数封装到params 这个属性里面

		// 调用对象
		NetTask<IfAttention> net = new NetTask<IfAttention>(
				hand.obtainMessage(), client, IfAttention.class,
				new IfAttention(), context);
		net.execute(); // 相当于线程的Star方法 开始运行
	}

	/**
	 * 添加关注请求网络获取数据成功后，将要进行的操作
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pro.dismiss();
			if (msg.what == 0) {
				Intent i = new Intent("guanzhuchanged");// 发广播告诉我的关注列表界面，刷新列表
				sendBroadcast(i);
				// pchidetailsrespnsedata.isguanzhu = 1;
				attention_button
						.setBackgroundResource(R.drawable.wuzhen_attention_yes);
				share_tv.setText(getLanguageString("取消关注"));
				Toast.makeText(context, getLanguageString("关注成功"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 1) {
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {
				Toast.makeText(context, getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
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
