package com.gheng.exhibit.view.checkin;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.HuiYiJiaBin;
import com.gheng.exhibit.http.body.response.HuiYiYiTi;
import com.gheng.exhibit.http.body.response.PchiDetailsRespnseData;
import com.gheng.exhibit.http.body.response.YiTiDetailsRespnseData;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.DipUtils;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForHuiYiXiangQJiaBin;
import com.gheng.exhibit.view.adapter.AdapterForHuiYiXiangQYT;
import com.gheng.exhibit.widget.MyListView;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class HuiYiXiangQingActivity extends BaseActivity implements
		View.OnClickListener {

	// 设置大会详情标题
	@ViewInject(R.id.in_title)
	TextView titletv;
	// 大会详情返回键按钮
	@ViewInject(R.id.but_fanhui)
	Button vipbut;
	// 会议详情关注按钮
	@ViewInject(R.id.attention_button)
	ImageButton attention_button;
	@ViewInject(R.id.share_tv)
	TextView share_tv;

	// **************最新修改的布局（现有布局）******************
	@ViewInject(R.id.hyxq_tv_title)
	private TextView hyxq_tv_title;
	@ViewInject(R.id.hyxq_tv_date)
	private TextView hyxq_tv_date;
	@ViewInject(R.id.hyxq_tv_location)
	private TextView hyxq_tv_location;
	@ViewInject(R.id.hyxq_tv_host)
	private TextView hyxq_tv_host;
	@ViewInject(R.id.hyxq_tv_introduce)
	private TextView hyxq_tv_introduce;
	@ViewInject(R.id.location_icon)
	private ImageView location_icon;
	// ********************************

	@ViewInject(R.id.myscrollview)
	private ScrollView mySv;

	@ViewInject(R.id.rv_jiabin)
	RecyclerView rv_jiabin;
	private AdapterForHuiYiXiangQJiaBin adapter_jiabin;
	private ArrayList<HuiYiJiaBin> jiabins = new ArrayList<>();
	private LinearLayoutManager llm_jiabin;

	@ViewInject(R.id.lv_yiti)
	MyListView lv_yiti;
	private AdapterForHuiYiXiangQYT adapter_yiti;
	private ArrayList<HuiYiYiTi> yitis = new ArrayList<HuiYiYiTi>();
	private AdapterForHuiYiXiangQJiaBin adapter_yitijiabin;
	private ArrayList<HuiYiJiaBin> yitijiabins = new ArrayList<HuiYiJiaBin>();
	private LinearLayoutManager llm_yitijiabin;

	private View poplayout;
	PopViewHolder popvh;
	YiTiDetailsRespnseData yitidetails;
	private ProgressDialog pro;
	PchiDetailsRespnseData pchidetailsrespnsedata;
	private Boolean isActive = true;
	String meettingid;
	String suijima;
	String method = "getMeetInfoById";

	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huiyixiangqing);
		// registerBoradcastReceiver();// 注册广播
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
		// popwindow查找地址=============================================================================
		poplayout = LayoutInflater.from(this).inflate(
				R.layout.popwindow_huiyixiangqing, null);
		popvh = new PopViewHolder();
		ViewUtils.inject(popvh, poplayout);
		// attention_button.setVisibility(View.VISIBLE);=============================================
		share_tv.setVisibility(View.VISIBLE);
		// relative_pchi_details_adressdetails.setVisibility(View.GONE);
		vipbut.setOnClickListener(this);
		// attention_button.setOnClickListener(this);
		share_tv.setOnClickListener(this);
		location_icon.setOnClickListener(this);
		// relative_pchi_details_adressdetails.setOnClickListener(this);
		// 给会议嘉宾列表设置适配器/////////////////////////////////////////////////////////////////
		adapter_jiabin = new AdapterForHuiYiXiangQJiaBin(jiabins, context);
		rv_jiabin.setHasFixedSize(true);// 强制item高度一致，加强加载效率
		llm_jiabin = new LinearLayoutManager(this);
		llm_jiabin.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置横向或纵向展示item
		rv_jiabin.setLayoutManager(llm_jiabin);
		rv_jiabin.setAdapter(adapter_jiabin);
		// 给议题详情嘉宾列表设置适配器////////////////////////////////////////////////////////////
		adapter_yitijiabin = new AdapterForHuiYiXiangQJiaBin(yitijiabins,
				context);
		popvh.rv_yitijiabin.setHasFixedSize(true);// 强制item高度一致，加强加载效率
		llm_yitijiabin = new LinearLayoutManager(this);
		llm_yitijiabin.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置横向或纵向展示item
		popvh.rv_yitijiabin.setLayoutManager(llm_yitijiabin);
		popvh.rv_yitijiabin.setAdapter(adapter_yitijiabin);
		// 给议题列表设置适配器////////////////////////////////////////////////////////////////
		adapter_yiti = new AdapterForHuiYiXiangQYT(context, yitis);
		lv_yiti.setAdapter(adapter_yiti);
		// 会议详情页面从服务器接收数据
		// 加载网络时等待对话框
		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失

		loadDetailsData();
		lv_yiti.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				loadDetailYiTi(id);
				showPopUp(view);
			}
		});
	};

	public class PopViewHolder {
		@ViewInject(R.id.tv_yitiname)
		TextView tv_yiiname;
		@ViewInject(R.id.tv_yititime)
		TextView tv_time;
		@ViewInject(R.id.tv_talkman)
		TextView tv_talkman;
		@ViewInject(R.id.tv_guest)
		TextView tv_guest;
		@ViewInject(R.id.tv_yiticontent)
		TextView tv_yiticontent;

		@ViewInject(R.id.yitiname)
		TextView yitiname;
		@ViewInject(R.id.yititime)
		TextView yititime;
		@ViewInject(R.id.yiti_talkman)
		TextView yititalkman;
		@ViewInject(R.id.yiticontent)
		TextView yiticontent;
		@ViewInject(R.id.rv_yitijiabin)
		RecyclerView rv_yitijiabin;
	}

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
		popvh.tv_yiiname.setText(getLanguageString("议题名称") + ":");
		popvh.tv_time.setText(getLanguageString("议题时间") + ":");
		popvh.tv_talkman.setText(getLanguageString("主持人") + ":");
		popvh.tv_guest.setText(getLanguageString("议题嘉宾") + ":");
		popvh.tv_yiticontent.setText(getLanguageString("议题内容") + ":");
	}

	@Override
	public void onClick(View v) {
		// 按钮的点击监听事件
		if (vipbut.getId() == v.getId()) {
			// 点击返回按钮之后，从会议详情页面跳转回我的关注页面
			finish();
		} else if (location_icon.getId() == v.getId()) {
			if (!longitude.equals("") && !latitude.equals("")
					&& !address.equals("")) {
				Intent baidu = new Intent(HuiYiXiangQingActivity.this,
						BaiDuMapActivity.class);
				baidu.putExtra("latitude", latitude);
				baidu.putExtra("longitude", longitude);
				baidu.putExtra("address", address);
				startActivity(baidu);
			}
		}
	}

	// 获取会议详情数据实现方法，请求网络数据
	private void loadDetailsData() {
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
		try {
			strbuf.append(Constant.decode(Constant.key, Parent.getUserId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (suijima != null) {
			strbuf.append("&suijima=");
			strbuf.append(suijima); // 接口ID
		}

		String str = strbuf.toString();
		client.params = str;
		System.out.println("aaa:HuiYiXaingQingActivity:loadDetailsData():"
				+ client.toString());
		NetTask<PchiDetailsRespnseData> net = new NetTask<PchiDetailsRespnseData>(
				handler.obtainMessage(), client, PchiDetailsRespnseData.class,
				new PchiDetailsRespnseData(), context);
		net.execute();

	}

	private void showPopUp(View v) { // 在View上面显示一个PopupWindow
		int width = 7 * DipUtils.getWindows(this)[0] / 8;
		int height = 4 * DipUtils.getWindows(this)[0] / 5;
		final PopupWindow popupWindow = new PopupWindow(poplayout, width,
				height, true);

		popupWindow.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(getResources().getColor(
				R.color.LightGrey));
		// popupWindow .setBackgroundDrawable(dw);// 必须设置这条不然就消失不了了！！！！！！！！！！！
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		int[] location = new int[2];
		v.getLocationOnScreen(location);

		// popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
		// + DipUtils.getWindows(this)[0] / 12,
		// location[1] - popupWindow.getHeight());
		popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		// 设置背景颜色变暗
		WindowManager.LayoutParams lp = getWindow().getAttributes();

		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // 加上这句
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}

	// // 关注按钮点击后取消关注实现方法，请求网络数据
	/** 记录前台切后台时间 */
	private Long TimeStart;
	/** 记录后台切前台时间 */
	private Long TimeEnd;
	// 接收广播
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

	// 注册广播
	// public void registerBoradcastReceiver() {
	// IntentFilter myIntentFilter = new IntentFilter();
	// myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
	// // 注册广播
	// registerReceiver(mBroadcastReceiver, myIntentFilter);
	// }

	// 唤醒时调用的方法
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

	// 挂起时调用的方法
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (!BaseActivity.isAppOnForeground(this)) {
			isActive = false;
			TimeStart = System.currentTimeMillis() / 1000;
		}
	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		MyApplication.remove(this);
		super.onDestroy();
	}

	// 根据议题id获取议题详情
	private void loadDetailYiTi(long yitiId) {
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
		strbuf.append("method=getYitiById" + "&yitiId=");
		strbuf.append(yitiId);
		strbuf.append("&lg=");
		if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
			strbuf.append("2");
		} else {
			strbuf.append("1");
		}
		strbuf.append("&userid=");
		try {
			strbuf.append(Constant.decode(Constant.key, Parent.getUserId()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String str = strbuf.toString();
		client.params = str;
		System.out.println("aaa:HuiYiXaingQingActivity:loadDetailYiTi():"
				+ client.toString());

		NetTask<YiTiDetailsRespnseData> net = new NetTask<YiTiDetailsRespnseData>(
				handl.obtainMessage(), client, YiTiDetailsRespnseData.class,
				new YiTiDetailsRespnseData(), context);
		net.execute();
	}

	String latitude = "";
	String longitude = "";
	String address = "";
	// 会议详情页面的数据请求 请求网络获取数据成功后，将要进行的操作
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			System.out.println("aaa:HuiYiXiangQingActivity:Handler传回来数据！");
			pro.dismiss();
			if (msg.what == 0) {
				System.out.println("aaa:HuiYiXiangQingActivity:获取数据成功！");
				pchidetailsrespnsedata = (PchiDetailsRespnseData) msg.obj;
				hyxq_tv_title.setText(pchidetailsrespnsedata.meettingtitle);
				hyxq_tv_date.setText(" " + pchidetailsrespnsedata.meettingtime);
				hyxq_tv_location.setText(pchidetailsrespnsedata.meettingroom);
				if (pchidetailsrespnsedata.meettingtalkman !=null && !pchidetailsrespnsedata.meettingtalkman.equals("")) {
				
				if (SharedData.getInt("i18n", Language.ZH) == 1){//中文
					
					hyxq_tv_host.setText("演讲人:"+pchidetailsrespnsedata.meettingtalkman);
				}else if (SharedData.getInt("i18n", Language.ZH) == 2){//英文
					hyxq_tv_host.setText("Presenter:"+pchidetailsrespnsedata.meettingtalkman);
					
				}
				
				}
				hyxq_tv_introduce
						.setText(pchidetailsrespnsedata.mettingcontent);
				latitude = pchidetailsrespnsedata.venceLatitude;
				longitude = pchidetailsrespnsedata.venceLongitude;
				address = pchidetailsrespnsedata.meettingroom;
				// 给嘉宾列表刷新适配器
				jiabins.clear();
				jiabins.addAll(pchidetailsrespnsedata.getGuests());
				adapter_jiabin.notifyDataSetChanged();
				yitis.clear();
				yitis.addAll(pchidetailsrespnsedata.getMeettingyiti());
				adapter_yiti.notifyDataSetChanged();
				// 会议议题遇到||后自动换行
				// pchidetailsrespnsedata.meettingyiti =
				// pchidetailsrespnsedata.meettingyiti
				// .replace("||", "\r\n");
				System.out.print(pchidetailsrespnsedata.meettingyiti);

			} else if (msg.what == 1) {
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {
				Toast.makeText(context, getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(HuiYiXiangQingActivity.this);
			}
		}
	};
	// 议题详情页面的数据请求 请求网络获取数据成功后，将要进行的操作
	Handler handl = new Handler() {
		public void handleMessage(android.os.Message msg) {
			System.out.println("aaa:HuiYiXiangQingActivity:Handl传回来数据！");
			pro.dismiss();
			if (msg.what == 0) {
				System.out.println("aaa:HuiYiXiangQingActivity:获取数据成功！");
				yitidetails = (YiTiDetailsRespnseData) msg.obj;
				// 给popwindow设置数据
				popvh.yitiname.setText(yitidetails.yitititle);
				popvh.yititime.setText(yitidetails.yititime);
				popvh.yititalkman.setText(yitidetails.yititalkman);
				popvh.yiticontent.setText(yitidetails.yiticontent);
				// 给议题嘉宾列表刷新适配器
				yitijiabins.clear();
				yitijiabins.addAll(yitidetails.getGuests());
				adapter_yitijiabin.notifyDataSetChanged();
			} else if (msg.what == 1) {
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) {
				Toast.makeText(context, getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(HuiYiXiangQingActivity.this);
			}
		}
	};

}
