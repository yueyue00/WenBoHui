package com.gheng.exhibit.view.viplist;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.SiXinLieBiao;
import com.gheng.exhibit.http.body.response.SiXinLieBiaoArr;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.MainActivity;
import com.gheng.exhibit.view.adapter.SiXinAdapter;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetSiXinTask;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class FuWuSiXingActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener {

	public ListView list_sixin;
	// public ArrayList<XiaoXiPojo> list = new ArrayList<XiaoXiPojo>();//
	// 消息提醒数据集合
	public SiXinAdapter sixinadapter;

	private Boolean isActive = true;

	// 发送私信输入框
	@ViewInject(R.id.edittext_sixin)
	EditText edittext_sixin;
	// 标题
	@ViewInject(R.id.in_title)
	TextView in_title;
	// title处返回键
	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;
	// 发送按钮
	@ViewInject(R.id.btn_send)
	Button btn_send;
	// * 私信刷新控件
	public SwipeRefreshLayout swipe;
	// vip《id,name,photourl》
	String rserviceid;
	String rservicename;
	String rservicephoto;
	private int page = 1;
	private ProgressDialog pro;
	List<SiXinLieBiaoArr> list_sx;
	private String ACTION_NAME = "私信接收";
	// xUtiles框架请求接口
	private HttpUtils httputils;
	public int noticinumber = 10000;
	User Parent = null;
	// 接收广播
	private BroadcastReceiver mBroadcastReceiverSiXin = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_NAME)) {
				String serviceidb = intent.getExtras().getString("serviceid");
				String servicename = intent.getExtras().getString("servicename");
				String content = intent.getExtras().getString("content");
				if (serviceidb.equals(rserviceid)) {// 刷新
					rserviceid = serviceidb;
					page = 1;
					try {
						loadData();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {// 响应通知栏
						// 弹出通知栏 没有点击事件 因为页面覆盖切换是个问题； 并检查SharedPreferences数值
					System.out.println("kwakjfkldasjkfadhgjkadf");
					NotificationManager nm = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification.Builder notice = new Notification.Builder(
							context);
					notice.setContentTitle(BaseActivity.getLanguageString("私信")
							+ ":");
					notice.setContentText(content);// 通知栏显示的内容
					notice.setTicker(content);// 私信内容 发送过来滚动的消息
					notice.setSmallIcon(R.drawable.ic_launcher);
					notice.setDefaults(Notification.DEFAULT_SOUND
							| Notification.DEFAULT_VIBRATE);
					notice.setWhen(System.currentTimeMillis());
					notice.setContentIntent(getPendingIntent(serviceidb,
							servicename, content));
					notice.setAutoCancel(true);
					Notification shownotice = notice.getNotification();
					nm.notify(10086, shownotice);
				}
				// SimpleDateFormat sDateFormat = new SimpleDateFormat(
				// "yyyy-MM-dd hh:mm:ss");
				// String date = sDateFormat.format(new java.util.Date());
				// SiXinLieBiaoArr sixin = new SiXinLieBiaoArr();
				// sixin.setMesstag("0");
				// sixin.setMesscontent(sixincontent);
				//
				// sixin.setCreateTimes(date);
				// sixinadapter.getList().add(sixin);// 向私信列表内添加数据
				// sixinadapter.notifyDataSetChanged();// 刷新私信列表
				// list_sixin.setSelection(sixinadapter.getList().size());
				System.out.println("成功接收推送");

			}
		}

	};

	protected void onNewIntent(Intent intent) {
		// // 注册广播
		registerBoradcastReceiver();
		registerBoradcastReceiverSiXin();

		// 生成xutils访问服务器框架对象
		httputils = new HttpUtils();

		// 返回键监听
		but_fanhui.setOnClickListener(this);
		// 发送按钮监听
		btn_send.setOnClickListener(this);

		// 动态监听布局高度是否发生变化 如果发生变化 判断如果heightDiff如果大于100 则表示键盘弹起
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		final View activityRootView = getWindow().getDecorView().findViewById(
				android.R.id.content);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 100) {
							list_sixin.setSelection(list_sixin.getCount() - 1);
						}
					}
				});

		list_sixin.setOnTouchListener(new OnTouchListener() {// 聊天界面listview的ontuch监听
					// 滑动（点击）键盘消失

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
						imm.hideSoftInputFromWindow(
								edittext_sixin.getWindowToken(), 0);
						return false;
					}
				});

		rserviceid = intent.getExtras().getString("serviceid");
		rservicename = intent.getExtras().getString("servicename");
		rservicephoto = intent.getExtras().getString("servicephoto");

		sixinadapter = new SiXinAdapter(this, 0, rservicephoto);
		list_sixin.setAdapter(sixinadapter);

		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		// 设置标题
		in_title.setText(rservicename);
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
	};

	/**
	 * 注册广播
	 **/
	public void registerBoradcastReceiverSiXin() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);
		// 注册广播
		registerReceiver(mBroadcastReceiverSiXin, myIntentFilter);
	}

	public PendingIntent getPendingIntent(String serviceid, String servicename,
			String content) {
		Intent i = new Intent();
		i.setClass(context, FuWuSiXingActivity.class);
		i.putExtra("serviceid", serviceid);
		i.putExtra("servicename", servicename);
		i.putExtra("content", content);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				noticinumber++, i, PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;
	}

	/**
	 * 发送私信Handler
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (pro != null) {
				pro.dismiss();
			}
			btn_send.setClickable(true);

			if (msg.what == 0) {
				edittext_sixin.setText("");
				page = 1;
				try {
					loadData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面

				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();

			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				Toast.makeText(context, BaseActivity.getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(FuWuSiXingActivity.this);
			} else {// 请求失败
				Toast.makeText(context, BaseActivity.getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();

			}
		}
	};
	/**
	 * 私信列表handler
	 */
	Handler hand = new Handler() {
		public void handleMessage(Message msg) {
			swipe.setRefreshing(false);
			swipe.setEnabled(true);
			if (pro != null) {
				pro.dismiss();
			}
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				if (page == 1) {
					sixinadapter.getList().clear();
					ResponseBody<SiXinLieBiao> res = (ResponseBody<SiXinLieBiao>) msg.obj; // 首先创建接收方法
					List<SiXinLieBiao> list = res.list; // 声明List的泛型
														// 其实就是拿到ResponseBody<HuiYiRiChengRiQiHuoQu>里面的list对象
					// List<SiXinLieBiao> list_sx = sixinadapter.getList();
					// list.addAll(list_sx);
					List<SiXinLieBiaoArr> list_sxlb = new ArrayList<SiXinLieBiaoArr>();
					for (int i = list.size() - 1; i >= 0; i--) {
						SiXinLieBiaoArr sixin = new SiXinLieBiaoArr();
						sixin.setCreateTimes(list.get(i).createTimes);
						sixin.setMesscontent(list.get(i).messcontent);
						sixin.setMesstag(list.get(i).messtag);
						list_sxlb.add(sixin);
					}
					list_sx = sixinadapter.getList();
					list_sxlb.addAll(list_sx);

					sixinadapter.setList(list_sxlb);// 向私信列表内添加数据
					sixinadapter.notifyDataSetChanged();
					list_sixin.setSelection(list_sixin.getCount() - 1);// 设置listview滚动行数
				} else {
					int list_size;// 设置listview的滚动行数
					ResponseBody<SiXinLieBiao> res = (ResponseBody<SiXinLieBiao>) msg.obj; // 首先创建接收方法
					List<SiXinLieBiao> list = res.list; // 声明List的泛型
														// 其实就是拿到ResponseBody<HuiYiRiChengRiQiHuoQu>里面的list对象
					list_size = list.size();
					// List<SiXinLieBiao> list_sx = sixinadapter.getList();
					// list.addAll(list_sx);
					List<SiXinLieBiaoArr> list_sxlb = new ArrayList<SiXinLieBiaoArr>();
					for (int i = list.size() - 1; i >= 0; i--) {
						SiXinLieBiaoArr sixin = new SiXinLieBiaoArr();
						sixin.setCreateTimes(list.get(i).createTimes);
						sixin.setMesscontent(list.get(i).messcontent);
						sixin.setMesstag(list.get(i).messtag);
						list_sxlb.add(sixin);
					}
					list_sx = sixinadapter.getList();
					list_sxlb.addAll(list_sx);

					sixinadapter.setList(list_sxlb);// 向私信列表内添加数据
					sixinadapter.notifyDataSetChanged();// 刷新私信列表
					list_sixin.setSelection(list_size);// 设置listview滚动行数
				}
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				Toast.makeText(context, BaseActivity.getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 3) { // 表示没有拿到列表数据
				// Toast.makeText(context,
				// BaseActivity.getLanguageString("没有历史消息"),
				// Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(FuWuSiXingActivity.this);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MainActivity.isOpenSiXin = true;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sixin);

		// 查找
		try {
			DbUtils db = DbUtils.create(context);

			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		list_sixin = (ListView) findViewById(R.id.list_sixin);
		swipe = (SwipeRefreshLayout) findViewById(R.id.sixinlistswiperefreshlayout);

		btn_send.setText(getLanguageString("发送"));

		// // 注册广播
		registerBoradcastReceiver();
		registerBoradcastReceiverSiXin();

		// 生成xutils访问服务器框架对象
		httputils = new HttpUtils();

		// 返回键监听
		but_fanhui.setOnClickListener(this);
		// 发送按钮监听
		btn_send.setOnClickListener(this);

		// 动态监听布局高度是否发生变化 如果发生变化 判断如果heightDiff如果大于100 则表示键盘弹起
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		final View activityRootView = getWindow().getDecorView().findViewById(
				android.R.id.content);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 100) {
							list_sixin.setSelection(list_sixin.getCount() - 1);
						}
					}
				});

		list_sixin.setOnTouchListener(new OnTouchListener() {// 聊天界面listview的ontuch监听
					// 滑动（点击）键盘消失

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
						imm.hideSoftInputFromWindow(
								edittext_sixin.getWindowToken(), 0);
						return false;
					}
				});

		Intent intent = getIntent();
		rserviceid = intent.getExtras().getString("serviceid");
		rservicename = intent.getExtras().getString("servicename");
		rservicephoto = intent.getExtras().getString("servicephoto");

		sixinadapter = new SiXinAdapter(this, 0, rservicephoto);
		list_sixin.setAdapter(sixinadapter);

		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		// 设置标题
		in_title.setText(rservicename);
		// 加载网络时等待对话框
		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
		pro.show();
		try {
			loadData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadData() throws Exception {
		// TODO Auto-generated method stub
		// 以下内容将来会是网络访问获取数据

		ClientParams client = new ClientParams();
		client.url = "/hxLastregistercheckaction.do";
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=selVipHwMessAction&vipid=");
		strbuf.append(rserviceid);
		strbuf.append("&pagesize=10");
		strbuf.append("&page=");
		strbuf.append(page);
		strbuf.append("&serviceid=");
		strbuf.append(Constant.decode(Constant.key, Parent.getUserId()));
		String str = strbuf.toString();
		client.params = str;
		Type type = new TypeToken<ArrayList<SiXinLieBiao>>() {
		}.getType();
		NetTask<SiXinLieBiao> net = new NetTask<SiXinLieBiao>(
				hand.obtainMessage(), client, type, context);
		net.execute();
	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub

	}

	@Override
	// swipelayout的下拉刷新监听方法
	public void onRefresh() {
		// TODO Auto-generated method stub
		swipe.setEnabled(false);
		page++;
		try {
			loadData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (but_fanhui.getId() == arg0.getId()) {
			FuWuSiXingActivity.this.finish();
		} else if (btn_send.getId() == arg0.getId()) {
			if (!edittext_sixin.getText().toString().trim().equals("")) {// 判断发送框内不为空

				if (isContainEmoji(edittext_sixin.getText().toString().trim())) {
					Toast.makeText(this, getLanguageString("目前只支持发送纯文字内容"),
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!IsWebCanBeUse.isWebCanBeUse(this)) {
					Toast.makeText(this, getLanguageString("网络不给力"),
							Toast.LENGTH_SHORT).show();
					return;
				}
				// 加载网络时等待对话框
				pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
				pro.setCancelable(true);// 点击dialog外空白位置是否消失
				pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
				pro.show();
				try {
					loadData();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btn_send.setClickable(false);

				String strcontext = edittext_sixin.getText().toString();
				strcontext = URLEncoder.encode(strcontext);
				ClientParams client = new ClientParams();
				client.url = "/hxLastregistercheckaction.do";
				StringBuffer strbuf = new StringBuffer();
				strbuf.append("method=vipHwMessAction&vipid=");
				strbuf.append(rserviceid);
				strbuf.append("&serviceid=");
				try {
					strbuf.append(Constant.decode(Constant.key,
							Parent.getUserId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				strbuf.append("&content=");
				strbuf.append(strcontext);
				strbuf.append("&messtag=0");
				strbuf.append("&vipname=");
				strbuf.append(rservicename);
				// try {
				// strbuf.append(Constant.decode(Constant.key,
				// Parent.getName()));
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				strbuf.append("&hwname=");
				try {
					strbuf.append(Constant.decode(Constant.key,
							Parent.getName()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				String str = strbuf.toString();
				client.params = str;

				NetSiXinTask<SiXinLieBiao> net = new NetSiXinTask<SiXinLieBiao>(
						handler.obtainMessage(), client, 1, context);
				net.execute();

			} else {
				Toast.makeText(context, getLanguageString("不能发送空消息"),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if (mBroadcastReceiver != null) {
			this.unregisterReceiver(mBroadcastReceiver);
		}
		if (mBroadcastReceiverSiXin != null) {
			this.unregisterReceiver(mBroadcastReceiverSiXin);
		}
		MyApplication.remove(this);
		MainActivity.isOpenSiXin = false;
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
		MainActivity.isOpenSiXin = true;
		if (!isActive) {

			TimeEnd = System.currentTimeMillis() / 1000;
			Integer time = getResources().getInteger(R.integer.time);
			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
				BaseActivity.loginValidation(context);
			}
		}
	}

}