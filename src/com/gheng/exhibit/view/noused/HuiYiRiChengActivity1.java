package com.gheng.exhibit.view.noused;
//package com.gheng.exhibit.view;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
//import android.support.v7.widget.RecyclerView;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.gheng.exhibit.application.MyApplication;
//import com.gheng.exhibit.http.body.response.DaHuiDate;
//import com.gheng.exhibit.http.body.response.DaHuiHuiChang;
//import com.gheng.exhibit.http.body.response.HYRiChengGuest;
//import com.gheng.exhibit.http.body.response.HYRiChengRiQi;
//import com.gheng.exhibit.http.body.response.ScheduleListData;
//import com.gheng.exhibit.model.databases.User;
//import com.gheng.exhibit.utils.Constant;
//import com.gheng.exhibit.view.adapter.ScheduleAdapter;
//import com.gheng.exhibit.view.adapter.ScheduleHuiChangAdapter;
//import com.gheng.exhibit.widget.PinnedHeaderExpandableHuiChangListView;
//import com.gheng.exhibit.widget.PinnedHeaderExpandableListView;
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.hebg3.mxy.utils.AsyncTaskForRequestDaHuiInfos_ByDate;
//import com.hebg3.mxy.utils.AsyncTaskForRequestDaHuiInfos_ByHuiChang;
//import com.hebg3.mxy.utils.AsyncTaskForRequestVipDaHuiInfos_ByDate;
//import com.hebg3.mxy.utils.AsyncTaskForRequestVipDaHuiInfos_ByHuiChang;
//import com.hebg3.mxy.utils.IsWebCanBeUse;
//import com.lidroid.xutils.DbUtils;
//import com.lidroid.xutils.db.sqlite.Selector;
//import com.lidroid.xutils.exception.DbException;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.lidroid.xutils.view.annotation.event.OnClick;
//import com.smartdot.wuzhen.huiyi.R;
//
///**
// * 会议列表
// * 
// * @author lileixing
// */
//public class HuiYiRiChengActivity1 extends BaseActivity implements
//		OnCheckedChangeListener, OnClickListener {
//
//	// @ViewInject(R.id.lv)
//	// private PinnedHeaderExpandableListView lv;
//	@ViewInject(R.id.lv_huicahng)
//	private RecyclerView re_huichang;
//	@ViewInject(R.id.lv)
//	private RecyclerView re_lv;
//	
//	@ViewInject(R.id.tv_time)
//	private TextView tv_time;
//
//	private ScheduleAdapter adapter;
//
//	private ScheduleHuiChangAdapter adapter_huichang;
//
//	private boolean isMine = false;
//
//	@ViewInject(R.id.edt_name)
//	private TextView edt_name;
//
//	@ViewInject(R.id.emptyView)
//	private TextView emptyView;
//	/**
//	 * 登录存储的数据库字段对象
//	 */
//	User Parent;
//
//	/**
//	 * 王雷 2015-08-31 加
//	 **/
//
//	/** 会议日程中，按日期选择的内容 **/
//	@ViewInject(R.id.relative_riqi)
//	private RelativeLayout relative_riqi;
//
//	/** 会议日程中，按会场选择的内容 **/
//	@ViewInject(R.id.relative_huichang)
//	private RelativeLayout relative_huichang;
//
//	/** 日期button和会场button的radiogroup **/
//	@ViewInject(R.id.radiogroup)
//	private RadioGroup radiogroup;
//
//	/** 日期的radiobutton **/
//	@ViewInject(R.id.radiobutton_riqi)
//	private RadioButton radiobutton_riqi;
//
//	/** 会场的radiobutton **/
//	@ViewInject(R.id.radiobutton_huichang)
//	private RadioButton radiobutton_huichang;
//
//	/** 会场扩展listview **/
//	// @ViewInject(R.id.lv_huicahng)
//	// private PinnedHeaderExpandableHuiChangListView lv_huicahng;
//	/**
//	 * 设置大会详情标题
//	 **/
//	@ViewInject(R.id.in_title)
//	TextView titletv;
//
//	/**
//	 * 大会详情返回键按钮
//	 **/
//	@ViewInject(R.id.but_fanhui)
//	Button vipbut;
//	/**
//	 * 大会日程接口等待对话框
//	 */
//	ProgressDialog pro;
//
//	private Boolean isActive = true;
//
//	/**
//	 * 保存所有大会数据 马晓勇 按日期大会数据 这里先初始化集合，配置adapter时用，数据请求回来，使用addAll，然后刷新即可
//	 */
//	ArrayList<DaHuiDate> dahuiinfos_date = new ArrayList<DaHuiDate>();
//	/**
//	 * 保存所有大会数据 马晓勇 按会场大会数据 这里先初始化集合，配置adapter时用，数据请求回来，使用addAll，然后刷新即可
//	 */
//	ArrayList<DaHuiHuiChang> dahuiinfos_huichang = new ArrayList<DaHuiHuiChang>();
//	/**
//	 * 判断是从首页的大会日程点击进去的还是从VIP服务中的会议日程点击进去的
//	 */
//	private int ishuiyiricheng = 0;
//	/** 会议按日期列表下拉刷新控件 */
//	@ViewInject(R.id.swipe_anriqi)
//	SwipeRefreshLayout swipe_anriqi;
//	/** 会议按会场列表下拉刷新控件 */
//	@ViewInject(R.id.swipe_anhuichang)
//	SwipeRefreshLayout swipe_anhuichang;
//	/** 没有数据时显示的暂无数据textview */
//	@ViewInject(R.id.nodatafounddate)
//	TextView nodatafounddate;
//	@ViewInject(R.id.nodatafoundhuichang)
//	TextView nodatafoundhuichang;
//	public String huiyirichengname = "huiyiricheng";
//	public SharedPreferences sp;
//	public Gson g = new Gson();
//
//	/**
//	 * 请求服务器获取所有大会数据 msg.what=0：请求超时 msg.what=-1：请求成功 但没有数据 msg.what=1：按日期请求数据成功
//	 * msg.what=2：按会场请求数据成功
//	 */
//	Handler handler = new Handler() {
//		@SuppressWarnings("unchecked")
//		public void handleMessage(Message msg) {
//			// swipe_anriqi.setRefreshing(false);
//			// swipe_anriqi.setEnabled(true);
//			// swipe_anhuichang.setRefreshing(false);
//			// swipe_anhuichang.setEnabled(true);
//			nodatafounddate.setVisibility(View.GONE);
//			nodatafoundhuichang.setVisibility(View.GONE);
//			pro.dismiss();// 取消加载网络对话框
//			try {
//				if (msg.what == 500) {// cookie超时
//					BaseActivity.gotoLoginPage(HuiYiRiChengActivity1.this);
//				}
//				if (msg.what == 400) {
//					Toast.makeText(context,
//							getLanguageString("您未绑定VIP,请联系管理员"),
//							Toast.LENGTH_SHORT).show();
//				}
//				if (msg.what == 0) {// 请求成功，但没有数据
//					nodatafounddate.setVisibility(View.VISIBLE);
//					nodatafoundhuichang.setVisibility(View.VISIBLE);
//				}
//
//				if (msg.what == -1) {// 请求超时
//					// Toast.makeText(HuiYiRiChengActivity.this,
//					// getLanguageString("请求失败"), Toast.LENGTH_SHORT)
//					// .show();
//				}
//				if (msg.what == 1) {// 按日期获取大会数据成功，并有数据
//					System.out
//							.println("aaa:HuiYiRiChengActivity:handler里面的数据：已经进入到Handler");
//					dahuiinfos_date.clear();
//					dahuiinfos_date.addAll((ArrayList<DaHuiDate>) msg.obj);
//					// 王雷在这里把数据放上去，然后更新
//					List<HYRiChengRiQi> list = new ArrayList<HYRiChengRiQi>();
//
//					for (int i = 0; i < dahuiinfos_date.size(); i++) {// 获取到info级下的jsonarray数组
//
//						for (int j = 0; j < dahuiinfos_date.get(i).meetting
//								.size(); j++) {// 获取到meeting级下的jsonarray数组
//							HYRiChengRiQi riqi = new HYRiChengRiQi();
//							if (j == 0) {
//								riqi.setdate(dahuiinfos_date.get(i).date);
//								riqi.setweek(dahuiinfos_date.get(i).week);
//							}
//							riqi.setIskz(0);
//							riqi.setMeettingtime(dahuiinfos_date.get(i).meetting
//									.get(j).meettingtime);
//							riqi.setMeettingid(dahuiinfos_date.get(i).meetting
//									.get(j).meettingid);
//							riqi.setMeettingtalkman(dahuiinfos_date.get(i).meetting
//									.get(j).meettingtalkman);
//							System.out.println(riqi.getMeettingtalkman());
//							riqi.setMeettingtitle(dahuiinfos_date.get(i).meetting
//									.get(j).meettingtitle);
//							// 此功能是点击会议展开下面的嘉宾列表放入数据
//							// for (int k = 0; k <
//							// dahuiinfos_date.get(i).meetting
//							// .get(j).guests.size(); k++) {
//							// HYRiChengGuest guest = new HYRiChengGuest();
//							// guest.setGuestphotourl(dahuiinfos_date.get(i).meetting
//							// .get(j).guests.get(k).guestphotourl);
//							// guest.setGuestname(dahuiinfos_date.get(i).meetting
//							// .get(j).guests.get(k).guestname);
//							// guest.setGuestzhiwu(dahuiinfos_date.get(i).meetting
//							// .get(j).guests.get(k).guestzhiwu);
//							// riqi.rlist.add(guest);
//							// }
//							if (j == 0) {
//								riqi.setHeadflag(true);
//								riqi.setIscolor(i % 3);
//
//							} else {
//								riqi.setHeadflag(false);
//								riqi.setIscolor(10);
//							}
//							list.add(riqi);
//							System.out
//									.println("aaa:HuiYiRiChengActivity:handler里面的数据："
//											+ list.toString());
//						}
//					}
//					adapter.setDahuiinfos_date(list);
//					adapter.notifyDataSetChanged();
//
//				}
//				if (msg.what == 2) {// 按会场获取大会数据成功，并有数据
//
//					dahuiinfos_huichang.clear();
//					dahuiinfos_huichang
//							.addAll((ArrayList<DaHuiHuiChang>) msg.obj);
//					// 王雷在这里把数据放上去，然后更新
//					List<HYRiChengRiQi> list = new ArrayList<HYRiChengRiQi>();
//
//					for (int i = 0; i < dahuiinfos_huichang.size(); i++) {// 获取到info级下的jsonarray数组
//
//						for (int j = 0; j < dahuiinfos_huichang.get(i).meetting
//								.size(); j++) {// 获取到meeting级下的jsonarray数组
//							HYRiChengRiQi riqi = new HYRiChengRiQi();
//							if (j == 0) {
//								riqi.setdate(dahuiinfos_huichang.get(i).meettingroomid);
//								riqi.setweek(dahuiinfos_huichang.get(i).meettingname);
//							}
//
//							riqi.setMeettingtime(dahuiinfos_huichang.get(i).meetting
//									.get(j).meettingtime);
//							riqi.setMeettingid(dahuiinfos_huichang.get(i).meetting
//									.get(j).meettingid);
//							riqi.setMeettingtalkman(dahuiinfos_huichang.get(i).meetting
//									.get(j).meettingtalkman);
//							riqi.setMeettingtitle(dahuiinfos_huichang.get(i).meetting
//									.get(j).meettingtitle);
//							// 此功能是点击会议展开下面的嘉宾列表放入数据
//							// for (int k = 0; k <
//							// dahuiinfos_huichang.get(i).meetting
//							// .get(j).guests.size(); k++) {
//							// HYRiChengGuest guest = new HYRiChengGuest();
//							// guest.setGuestphotourl(dahuiinfos_huichang
//							// .get(i).meetting.get(j).guests.get(k).guestphotourl);
//							// guest.setGuestname(dahuiinfos_huichang.get(i).meetting
//							// .get(j).guests.get(k).guestname);
//							// guest.setGuestzhiwu(dahuiinfos_huichang.get(i).meetting
//							// .get(j).guests.get(k).guestzhiwu);
//							// }
//							if (j == 0) {
//								riqi.setHeadflag(true);
//								riqi.setIscolor(i % 3);
//
//							} else {
//								riqi.setHeadflag(false);
//								riqi.setIscolor(10);
//							}
//							list.add(riqi);
//						}
//					}
//					adapter_huichang.setDahuiinfos_date(list);
//					adapter_huichang.notifyDataSetChanged();
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		MyApplication.add(this);
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_huiyiricheng);
//		registerBoradcastReceiver();// 注册广播
//		ishuiyiricheng = getIntent().getIntExtra("isricheng", 0);
//		swipe_anriqi.setColorSchemeColors(this.getResources().getColor(
//				R.color.swiperefreshlayoutcolor));
//		swipe_anhuichang.setColorSchemeColors(this.getResources().getColor(
//				R.color.swiperefreshlayoutcolor));
//
//		sp = getSharedPreferences(huiyirichengname, Activity.MODE_PRIVATE);
//
//		// 打开数据库
//		try {
//			DbUtils db = DbUtils.create(this);
//			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
//					"1"));
//			db.close();
//		} catch (DbException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		vipbut.setOnClickListener(this);
//		List<ScheduleListData> entity = new ArrayList<ScheduleListData>();
//
//		adapter = new ScheduleAdapter(this, lv);
//		// adapter.setDatas(entity);
//		lv.setAdapter(adapter);
//
//		radiobutton_riqi.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
//		radiobutton_riqi.setText(getLanguageString("按日期"));
//
//		radiobutton_huichang.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
//		radiobutton_huichang.setText(getLanguageString("按会场"));
//		// 按会场
//
//		adapter_huichang = new ScheduleHuiChangAdapter(this, lv_huicahng);
//		lv_huicahng.setAdapter(adapter_huichang);
//
//		isMine = getIntent().getBooleanExtra("isMine", false);
//		if (isMine) {
//			edt_name.setVisibility(View.GONE);
//		}
//		emptyView.setVisibility(View.GONE);
//		emptyView.setText(getLanguageString("未找到相关信息"));
//
//		// 选择按日期 或者按会场监听事件
//		radiogroup.setOnCheckedChangeListener(this);
//
//		/** 按日期下拉刷新方法 */
//		swipe_anriqi.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				// TODO Auto-generated method stub
//				// 加载网络数据
//				try {
//					loadData();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		/** 按日期下拉刷新方法 */
//		swipe_anhuichang.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				// TODO Auto-generated method stub
//				// 加载网络数据
//				try {
//					loadData();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//
//		// lv.setOnGroupExpandListener(new OnGroupExpandListener() {
//		//
//		// @Override
//		// public void onGroupExpand(int groupPosition) {
//		// // TODO Auto-generated method stub
//		// for (int i = 0; i < adapter.getGroupCount(); i++) {
//		// if (i != groupPosition && lv.isGroupExpanded(groupPosition)) {
//		// lv.collapseGroup(i);
//		// System.out.println("点击 成功");
//		// }
//		// }
//		// }
//		// });
//		// 加载网络时等待对话框
//		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
//		pro.setCancelable(true);// 点击dialog外空白位置是否消失
//		pro.setCanceledOnTouchOutside(true);// 点击返回键对话框是否消失
//		// 加载网络数据
//		try {
//			loadData();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 启动线程 从服务器请求所有大会数据
//	 * 
//	 * @throws Exception
//	 */
//	private void loadData() throws Exception {
//		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
//
//			if (ishuiyiricheng == 1) {
//				boolean date = getDataFromCache(handler.obtainMessage(),
//						"huiyirichengbydate").equals("");
//				boolean huichang = getHuiChangFromCache(
//						handler.obtainMessage(), "huiyirichengbyhuichang")
//						.equals("");
//				if (date && huichang) {
//					Toast.makeText(HuiYiRiChengActivity1.this,
//							getLanguageString("网络不给力"), Toast.LENGTH_SHORT)
//							.show();
//					swipe_anriqi.setRefreshing(false);
//					swipe_anriqi.setEnabled(true);
//					swipe_anhuichang.setRefreshing(false);
//					swipe_anhuichang.setEnabled(true);
//					pro.dismiss();
//				}
//			} else if (ishuiyiricheng == 2) {
//				boolean date = getDataFromCache(handler.obtainMessage(),
//						"gerenhuiyirichengbydate").equals("");
//				boolean huichang = getHuiChangFromCache(
//						handler.obtainMessage(), "gerenhuiyirichengbyhuichang")
//						.equals("");
//				if (date && huichang) {
//					Toast.makeText(HuiYiRiChengActivity1.this,
//							getLanguageString("网络不给力"), Toast.LENGTH_SHORT)
//							.show();
//					swipe_anriqi.setRefreshing(false);
//					swipe_anriqi.setEnabled(true);
//					swipe_anhuichang.setRefreshing(false);
//					swipe_anhuichang.setEnabled(true);
//					pro.dismiss();
//				}
//			}
//			return;
//		}
//		if (ishuiyiricheng == 1) {
//			// 按日期请求
//			AsyncTaskForRequestDaHuiInfos_ByDate dateat = new AsyncTaskForRequestDaHuiInfos_ByDate(
//					handler.obtainMessage(), this.getApplicationContext());
//			dateat.execute(1);
//			// 按会场请求
//			AsyncTaskForRequestDaHuiInfos_ByHuiChang huichangat = new AsyncTaskForRequestDaHuiInfos_ByHuiChang(
//					handler.obtainMessage(), this.getApplicationContext());
//			huichangat.execute(1);
//		} else if (ishuiyiricheng == 2) {
//			String vip = Parent.getUserjuese().equals("3") ? Constant.decode(
//					Constant.key, Parent.getVipid()) : Constant.decode(
//					Constant.key, Parent.getUserId());
//
//			// 按日期请求
//			AsyncTaskForRequestVipDaHuiInfos_ByDate dateat = new AsyncTaskForRequestVipDaHuiInfos_ByDate(
//					handler.obtainMessage(), vip, this.getApplicationContext(),
//					Constant.decode(Constant.key, Parent.getUserId()));
//			dateat.execute(1);
//			// 按会场请求
//			AsyncTaskForRequestVipDaHuiInfos_ByHuiChang huichangat = new AsyncTaskForRequestVipDaHuiInfos_ByHuiChang(
//					handler.obtainMessage(), vip, this.getApplicationContext(),
//					Constant.decode(Constant.key, Parent.getUserId()));
//			huichangat.execute(1);
//		}
//
//	}
//
//	ArrayList<DaHuiDate> infos;
//
//	/**
//	 * 从缓存按日期获取json解析成对象并返回
//	 * 
//	 * @param pagenum
//	 *            页码
//	 * @return 解析后的数据集合
//	 */
//	public String getDataFromCache(Message msg, String str) {
//		if (!sp.getString(str, "").equals("")) {
//			Type type = new TypeToken<ArrayList<DaHuiDate>>() {
//			}.getType();// 设置集合type
//
//			try {
//				infos = g.fromJson(
//						Constant.decode(Constant.key, sp.getString(str, "")),
//						type);
//			} catch (JsonSyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			msg.obj = infos;
//			msg.what = 1;
//			msg.sendToTarget();
//			return "cache";
//		} else {
//			return "";
//		}
//	}
//
//	ArrayList<DaHuiHuiChang> infoshc;
//
//	/**
//	 * 从缓存按会场获取json解析成对象并返回
//	 * 
//	 * @param pagenum
//	 *            页码
//	 * @return 解析后的数据集合
//	 */
//	public String getHuiChangFromCache(Message msg, String str) {
//
//		if (!sp.getString(str, "").equals("")) {
//			Type type = new TypeToken<ArrayList<DaHuiHuiChang>>() {
//			}.getType();// 设置集合type
//
//			try {
//				infoshc = g.fromJson(
//						Constant.decode(Constant.key, sp.getString(str, "")),
//						type);
//			} catch (JsonSyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			msg.obj = infoshc;
//			msg.what = 2;
//			msg.sendToTarget();
//			return "cache";
//		} else {
//			return "";
//		}
//	}
//
//	@Override
//	protected void setI18nValue() {
//		// 马晓勇加，从vip服务启动本届面，要设置title为“个人会议日程”，如果是从主界面启动，需要设置为“会议日程”
//		if (getIntent().getExtras().getString("title") != null) {
//			titletv.setText(getIntent().getExtras().getString("title"));
//		} else {
//			titletv.setText(getLanguageString("会议日程"));
//		}
//
//		edt_name.setHint(getLanguageString("按标题,简介,嘉宾搜索"));
//		nodatafounddate.setText(getLanguageString("暂时没有数据"));
//		nodatafoundhuichang.setText(getLanguageString("暂时没有数据"));
//	}
//
//	@OnClick(value = { R.id.edt_name })
//	public void clickEdtName(View v) {
//		Intent intent = new Intent(context, HuiYiRiChengSearchActivity.class);
//		startActivityForResult(intent, 100);
//	}
//
//	@Override
//	public void onCheckedChanged(RadioGroup arg0, int arg1) {
//
//		if (arg1 == radiobutton_riqi.getId()) {// 判断点击 按日期
//			relative_riqi.setVisibility(View.VISIBLE);
//			relative_huichang.setVisibility(View.GONE);
//		} else if (arg1 == radiobutton_huichang.getId()) {// 判断点击 按会场
//			relative_riqi.setVisibility(View.GONE);
//			relative_huichang.setVisibility(View.VISIBLE);
//		}
//	}
//
//	@Override
//	public void onClick(View arg0) {
//
//		if (vipbut.getId() == arg0.getId()) {
//			HuiYiRiChengActivity1.this.finish();
//		}
//	}
//
//	@Override
//	protected void onDestroy() {// 退出应用，清除图片缓存
//		if (mBroadcastReceiver != null) {
//			this.unregisterReceiver(mBroadcastReceiver);
//		}
//		MyApplication.remove(this);
//		super.onDestroy();
//	}
//
//	/** 记录前台切后台时间 */
//	private Long TimeStart;
//	/** 记录后台切前台时间 */
//	private Long TimeEnd;
//	/**
//	 * 接收广播
//	 **/
//	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//				if (BaseActivity.isAppOnForeground(context)) {
//					isActive = false;
//					TimeStart = System.currentTimeMillis() / 1000;
//				}
//			}
//		}
//	};
//
//	/**
//	 * 注册广播
//	 **/
//	public void registerBoradcastReceiver() {
//		IntentFilter myIntentFilter = new IntentFilter();
//		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//		// 注册广播
//		registerReceiver(mBroadcastReceiver, myIntentFilter);
//	}
//
//	/**
//	 * 挂起时调用的方法
//	 */
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		if (!BaseActivity.isAppOnForeground(this)) {
//			isActive = false;
//			TimeStart = System.currentTimeMillis() / 1000;
//		}
//	}
//
//	/**
//	 * 唤醒时调用的方法
//	 */
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//
//		if (!isActive) {
//			TimeEnd = System.currentTimeMillis() / 1000;
//			Integer time = getResources().getInteger(R.integer.time);
//			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
//				BaseActivity.loginValidation(context);
//			}
//		}
//	}
//
//}
