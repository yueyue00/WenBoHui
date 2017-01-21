package com.gheng.exhibit.view;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.mobstat.StatService;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.ShouYeLunBo;
import com.gheng.exhibit.http.body.response.VipInfoListBaen;
import com.gheng.exhibit.http.body.response.VipInfoListBaen.InfoBean;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;
import com.gheng.exhibit.rongyun.RongDemoTabs;
import com.gheng.exhibit.rongyun.contact.ContactActivity;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.adapter.AdapterForXiaoXiZhongXin;
import com.gheng.exhibit.view.adapter.MainGridAdapter;
import com.gheng.exhibit.view.checkin.BaiDuMapActivity;
import com.gheng.exhibit.view.checkin.CulturalExhibitListActivity;
import com.gheng.exhibit.view.checkin.DaHuiDataActivity;
import com.gheng.exhibit.view.checkin.DaHuiDianPingActivity;
import com.gheng.exhibit.view.checkin.HuiYiRiChengActivity;
import com.gheng.exhibit.view.checkin.MytaskListActivity;
import com.gheng.exhibit.view.checkin.VIPInfoActivity;
import com.gheng.exhibit.view.checkin.VipinfoListActivity;
import com.gheng.exhibit.view.checkin.XiaoXiZhongXinActivity;
import com.gheng.exhibit.view.checkin.XingChengAnPaiActivity;
import com.gheng.exhibit.view.checkin.checkin.VipZhuanShuFWActivity;
import com.gheng.exhibit.view.support.PchiData;
import com.gheng.exhibit.xinwen.ActivityMy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.AsyncTaskForCheckApkVersion;
import com.hebg3.mxy.utils.AsyncTaskForDownloadTask;
import com.hebg3.mxy.utils.AsyncTaskForGetXiaoXin;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.NewApkVersionPojo;
import com.hebg3.mxy.utils.VipListTask;
import com.hebg3.mxy.utils.XiaoXiPojo;
import com.hebg3.mxy.utils.zAsyncTaskForRongGlobalGroup;
import com.hebg3.mxy.utils.zAsyncTaskForRongGlobalUser;
import com.hebg3.wl.net.ResponseBody;
import com.hebg3.wl.push.ExampleUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 主页
 * 
 * @author lileixing
 */
public class FWMainActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {
	@ViewInject(R.id.main_title)
	private TextView tv;
	@ViewInject(R.id.tv_time)
	private TextView tv_time;
	@ViewInject(R.id.nodatafound)
	public TextView nodatafound;
	@ViewInject(R.id.xiaoxizhongxinbutton)
	TextView xiaoxizhongxinbutton;// 进入消息中心列表界面按钮
	// 设置按钮
	@ViewInject(R.id.image_shezhi)
	ImageView image_shezhi;
	@ViewInject(R.id.xiaoxilogotext)
	TextView xiaoxilogotext;
	// 消息列表
	@ViewInject(R.id.rv)
	RecyclerView rv;
	public LinearLayoutManager llm;
	public AdapterForXiaoXiZhongXin rvadapter;
	public ArrayList<XiaoXiPojo> list = new ArrayList<XiaoXiPojo>();// 消息提醒数据集合
	// GridView布局
	@ViewInject(R.id.gridview)
	private GridView gridview;
	private MainGridAdapter adapter;
	/**
	 * 程序是否是从后台切回前台判断值，如果是新界面盖过原界面而执行的onStop方法isActive会返回true，
	 * 如果用户是按home键后台挂起程序会返回false
	 */
	private Boolean isActive = true;
	int myversioncode = 1;// 当前系统版本号
	public SharedPreferences sp;
	public Gson g = new Gson();
	/** 判断有没有打开私信界面 打开为true 未打开为false */
	public static Boolean isOpenSiXin = false;
	// 数据库对象
	User Parent;
	public Context context = this;
	public boolean refreshtime = true;
	ProgressDialog pro;
	/** 记录前台切后台时间 */
	private Long TimeStart;
	/** 记录后台切前台时间 */
	private Long TimeEnd;
	public int downcount = 0;
	public boolean downloading = true;
	// 下载新版本apk 广播接收
	DownloadReceiver downreceiver = new DownloadReceiver();
	// 推送属性设置（自定义消息）
	public static boolean isForeground = false;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	private static final int MSG_SET_ALIAS = 1001;
	private String TAG = "MainActivity";
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				Log.d(TAG, "Set alias in handler.");
				// 调用 JPush 接口来设置别名。
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;

			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		MyApplication.initImageLoader(this);
		setContentView(R.layout.activity_fwmain);
		// zyj新增 默认进来就行中文
		if (SharedData.getInt("i18n") == 1) {
			SharedData.commit("i18n", Language.ZH);
		} else {
			SharedData.commit("i18n", Language.ZH);
		}
		process();
		initView();

		// 获取到嘉宾id
		getVipidByVipList();
	}

	private User user;
	private String userid = "";
	private Handler vipListHandler = new Handler() {
		public void handleMessage(Message msg) {
			getRongData();
			switch (msg.what) {
			case 1:
				VipInfoListBaen result = (VipInfoListBaen) msg.obj;
				List<InfoBean> info = result.getInfo();
				Constant.VIP_COUNT = info.size();
				Constant.VIPID = info.get(0).getvipId();// 只取vip嘉宾列表的第一个人的id，当vip列表只有一个人的时候，此值为唯一的id
				// Toast.makeText(FWMainActivity.this,
				// "=====NUM====>"+Constant.VIP_COUNT+"======>"+Constant.VIPID,Toast.LENGTH_LONG
				// ).show();

				// String result = (String) msg.obj;
				// Toast.makeText(FWMainActivity.this, "---->"+result,
				// Toast.LENGTH_LONG).show();
				// System.out.println("----------viplistTask  嘉宾列表--->"+result);
				break;
			case -1:
				Toast.makeText(FWMainActivity.this, "嘉宾信息数据请求有误",
						Toast.LENGTH_LONG).show();
				break;
			case -2:
				Toast.makeText(FWMainActivity.this, "---->嘉宾列表信息为空",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 从网络请求数据 该段请求只是为了在服务人员登录的时候嘉宾信息中使用vipid
	 * 
	 * 主要是将获取到的vipid存于常量Constant.VIPID中用于scheduleFragment中请求网络数据的参数
	 */
	private void getVipidByVipList() {
		System.out.println("-------->进入MainACtivity的请求嘉宾列表信息的方法");
		// 查找
		try {
			DbUtils db = DbUtils.create(FWMainActivity.this);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			// user.getUserjuese().equals("3") ?
			// Constant.decode(Constant.key,user.getVipid()) :
			// Constant.decode(Constant.key,user.getUserId())
			// URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			userid = Constant.decode(Constant.key, user.getUserId());
			Log.d("tag", "=======MainActivity--->userid=" + userid);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			new VipListTask(vipListHandler.obtainMessage(), userid,
					FWMainActivity.this).execute(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initView() {
		// 消息列表设置适配器============================================================================
		rv.setHasFixedSize(true);// 强制item高度一致，加强加载效率
		rv.addItemDecoration(// 为RecyclerView添加divider
		new HorizontalDividerItemDecoration.Builder(this)
				.color(this.getResources().getColor(R.color.attention))
				.size(getResources().getDimensionPixelSize(
						R.dimen.recylerviewitemdivider1))
				.margin(getResources().getDimensionPixelSize(R.dimen.margin_z),
						getResources().getDimensionPixelSize(R.dimen.margin_z))
				.build());
		llm = new LinearLayoutManager(this);// 设置RecyclerView的布局管理器（必须设置，否则RecyclerView会崩溃）
		llm.setOrientation(LinearLayoutManager.VERTICAL);// 设置横向或纵向展示item
		rv.setLayoutManager(llm);// 为RecyclerView配置布局管理器 必须手动配置！
		rvadapter = new AdapterForXiaoXiZhongXin(this, list);// 初始化用户自定义适配器
		rv.setAdapter(rvadapter);// 为recyclerView设置适配器
		rv.setVisibility(View.INVISIBLE);
		nodatafound.setVisibility(View.VISIBLE);
		image_shezhi.setOnClickListener(this);
		xiaoxizhongxinbutton.setOnClickListener(this);

		pro = ProgressDialog.show(this, "",
				BaseActivity.getLanguageString("加载中..."));// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
	}

	private void process() {
		StatService.onEvent(getApplicationContext(), "login", "参会人员登录", 1);// 百度统计
		registerBoradcastReceiverJPush();
		registerBoradcastReceiverScreen();// 注册广播<启动时间判断的广播>
		try {
			// 获取程序版本号
			PackageManager manager = this.getPackageManager();// 程序包管理器
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			myversioncode = info.versionCode;// 当前系统版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}// 获取PackageInfo对象就可以获取版本名称和版本号

		// 打开数据库
		try {
			DbUtils db = DbUtils.create(this);
			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		// 下载完成广播监听
		IntentFilter downloadcompleteIF = new IntentFilter();
		downloadcompleteIF.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		this.registerReceiver(downreceiver, downloadcompleteIF);
		/**
		 * 初始化极光推送
		 **/
		JPushInterface.init(this.getApplicationContext()); // 初始化 JPush
		JPushInterface.resumePush(this.getApplicationContext());//
		// 停止推送服务后调用该方法恢复服务
		// 调用 Handler 来异步设置别名
		try {
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS,
					Constant.decode(Constant.key, Parent.getUserId().trim())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		// zyj 新加接收融云聊天消息的监听
		final Conversation.ConversationType[] conversationTypes = {
				Conversation.ConversationType.PRIVATE,
				Conversation.ConversationType.DISCUSSION,
				Conversation.ConversationType.GROUP,
				Conversation.ConversationType.SYSTEM,
				Conversation.ConversationType.PUBLIC_SERVICE,
				Conversation.ConversationType.APP_PUBLIC_SERVICE };
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RongIM.getInstance().setOnReceiveUnreadCountChangedListener(
						mCountListener, conversationTypes);
			}
		}, 500);
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
				break;
			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				// 延迟 60 秒来调用 Handler 设置别名
				mHandler.sendMessageDelayed(
						mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				break;
			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}
			// ExampleUtil.showToast(logs, getApplicationContext());
		}
	};

	// 如果发现新版本，调用本方法询问用户是否下载
	public void showDialogDownloadNewApk(final String downloadurl) {
		new AlertDialog.Builder(context)
				.setTitle(getLanguageString("发现新版本") + "!")
				.setMessage(getLanguageString("现在下载吗") + "?")
				.setPositiveButton(getLanguageString("确定"),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 启动线程下载apk
								// 判断下载服务是否可用，如果未启用，启用
								int state = getPackageManager()
										.getApplicationEnabledSetting(
												"com.android.providers.downloads");

								if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
										|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
										|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
									System.out.println("下载服务未启用");
									String packageName = "com.android.providers.downloads";
									try {
										// Open the specific App Info page:
										Intent intent = new Intent(
												android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
										intent.setData(Uri.parse("package:"
												+ packageName));
										startActivity(intent);
									} catch (ActivityNotFoundException e) {
										e.printStackTrace();
										// Open the generic Apps page:
										Intent intent = new Intent(
												android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
										startActivity(intent);
										Toast.makeText(
												getApplicationContext(),
												BaseActivity
														.getLanguageString("下载失败"),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									StatService.onEvent(
											getApplicationContext(),
											"updateAPK", "参会人员更新App", 1);// 百度统计
									System.out.println("下载服务已经启用");
									AsyncTaskForDownloadTask at = new AsyncTaskForDownloadTask(
											getApplicationContext(),
											downloadurl);
									at.execute(1);
								}
							}
						})
				.setNegativeButton(getLanguageString("取消"),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
	}

	// 版本更新、通知中心返回数据的handler
	Handler h = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			pro.dismiss();
			if (msg.what == -1) {// 查询失败
				rv.setVisibility(View.GONE);
				nodatafound.setVisibility(View.VISIBLE);
			}
			if (msg.what == 0) {// 没数据
				if (list.size() < 1) {
					rv.setVisibility(View.GONE);
					nodatafound.setVisibility(View.VISIBLE);
				}
			}
			if (msg.what == 1) {// 有数据
				list.clear();
				list.addAll((ArrayList<XiaoXiPojo>) msg.obj);
				rv.setVisibility(View.VISIBLE);
				nodatafound.setVisibility(View.GONE);
				rvadapter.notifyDataSetChanged();
			}
			if (msg.what == 500) {
				System.out.println("通知接口请求500");
				BaseActivity.gotoLoginPage(FWMainActivity.this);
			}
			if (msg.what == 1000) {// 主界面检查新版本
				NewApkVersionPojo pojo = (NewApkVersionPojo) msg.obj;
				if (pojo.versioncode > myversioncode) {// 有新版本
					showDialogDownloadNewApk(pojo.downloadurl);
				}
			}
		}
	};

	// 发起请求获取通知数据，服务器版本号
	public void goRequest(int pagenum) throws Exception {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			return;
		} else {
			System.out.println("aaa" + "请求了消息中心的数据");
			AsyncTaskForGetXiaoXin at = new AsyncTaskForGetXiaoXin(
					h.obtainMessage(), pagenum, Constant.decode(Constant.key,
							Parent.getUserId()), this.getApplicationContext(),
					10);
			at.execute(1);
		}
	}

	// 接收广播
	// <设置GridView适配器>按角色填入功能模块
	@Override
	protected void init() {
		adapter = new MainGridAdapter(this);
		gridview.setAdapter(adapter);
		adapter.setData(createGridData());
		gridview.setNumColumns(4);
		gridview.setOnItemClickListener(this);
	}

	private List<PchiData> createGridData() {

		List<PchiData> list = new ArrayList<PchiData>();

		list.add(new PchiData(getLanguageString("嘉宾信息"),
				R.drawable.btn_main_vip_info));

		list.add(new PchiData(getLanguageString("服务签到"),
				R.drawable.btn_main_mytask));

		list.add(new PchiData(getLanguageString("服务调度"),
				R.drawable.btn_main_communication));
		list.add(new PchiData(getLanguageString("通讯录"), R.drawable.home_gv_news));
		list.add(new PchiData(getLanguageString("嘉宾留言"),
				R.drawable.btn_main_vip_leavemessage));
		list.add(new PchiData(getLanguageString("大会日程"),
				R.drawable.btn_main_meeting_schedule));
		list.add(new PchiData(getLanguageString("文化年展"),
				R.drawable.btn_main_culturalexhibition));
		list.add(new PchiData(getLanguageString("地图导航"),
				R.drawable.btn_main_map_navigation));
		return list;
	}

	ResponseBody<ShouYeLunBo> body;

	// 从缓存获取json解析成对象并返回 页码 解析后的数据集合

	public String getDataFromCache(Message msg) {
		if (!sp.getString("shouyelunbo", "").equals("")) {
			body = new ResponseBody<ShouYeLunBo>();
			Type type = new TypeToken<ArrayList<ShouYeLunBo>>() {
			}.getType();// 设置集合type
			try {
				body.list = g.fromJson(
						Constant.decode(Constant.key,
								sp.getString("shouyelunbo", "")), type);
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
	protected void setI18nValue() {// 替换图片也可以写在这个方法里面

		if (getPingMuSize(context) == 0) {
			tv.setText(getLanguageString("文博会"));
		} else {
			tv.setText(getLanguageString("文博会"));
		}
		xiaoxilogotext.setText(getLanguageString("通知"));
		nodatafound.setText(getLanguageString("暂时没有通知"));
		xiaoxizhongxinbutton.setText(getLanguageString("更多"));

	}

	@Override
	public void onClick(View v) {
		Bundle bd = new Bundle();

		if (v == xiaoxizhongxinbutton) {// 消息中心
			startTo(XiaoXiZhongXinActivity.class, bd);
		}
		switch (v.getId()) {
		case R.id.image_shezhi: // 设置界面
			Intent i = new Intent();
			i.setClass(this, SettingActivity.class);
			this.startActivityForResult(i, 10);
			break;
		}
	}

	@Override
	// 8个功能模块的点击事件
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getAdapter() instanceof MainGridAdapter) {
			Bundle bd = new Bundle();// 如需传递数据 统一使用这个bd
			Intent intent = new Intent(FWMainActivity.this, ActivityMy.class);
			switch (position) {
			case 0: // 嘉宾信息
				// startTo(VIPlistActivity.class, bd);
				// Intent vipIntent = new Intent(FWMainActivity.this,
				// VIPInfoActivity.class);
				Intent vipIntent = null;
				if (Constant.VIP_COUNT > 1) {
					vipIntent = new Intent(FWMainActivity.this,
							VipinfoListActivity.class);
					vipIntent.putExtra("title", getLanguageString("嘉宾信息"));
					startActivity(vipIntent);
				} else if (Constant.VIP_COUNT == 1) {
					vipIntent = new Intent(FWMainActivity.this,
							VIPInfoActivity.class);
					vipIntent.putExtra("vipid", Constant.VIPID);
					vipIntent.putExtra("title", getLanguageString("嘉宾信息"));
					startActivity(vipIntent);
				} else {
					Toast.makeText(FWMainActivity.this, "嘉宾列表为空",
							Toast.LENGTH_LONG).show();
				}

				break;
			case 1: // 服务签到
				/**
				 * 将登录人员的角色标识存入到全局变量中
				 */
				Constant.USER_JUESE = Parent.getUserjuese();

				Intent fuwwuqd = new Intent();
				// if (Parent.getUserjuese().equals("61")) {
				// fuwwuqd.setClass(this, MyTaskDriverActivity.class);
				// } else {
				fuwwuqd.setClass(this, MytaskListActivity.class);
				// }
				fuwwuqd.putExtra("title", getLanguageString("服务签到"));
				startActivity(fuwwuqd);
				break;
			case 2: // 服务调度
				Intent fuwudiaodu = new Intent();
				fuwudiaodu.setClass(context, RongDemoTabs.class);
				startActivity(fuwudiaodu);
				break;
			case 3: // 大会新闻改为通讯录
				// intent.putExtra("activitymy", "大会新闻");
				// startActivity(intent);
				Intent contact = new Intent(FWMainActivity.this,
						ContactActivity.class);
				startActivity(contact);
				break;
			case 4: // 嘉宾留言
				Intent dahuipinglun = new Intent();
				dahuipinglun.setClass(this, DaHuiDianPingActivity.class);
				startActivity(dahuipinglun);
				break;
			case 5: // 大会日程
				Intent intentricheng = new Intent(FWMainActivity.this,
						HuiYiRiChengActivity.class);
				intentricheng.putExtra("isricheng", 1);
				startActivity(intentricheng);
				break;

			case 6: // 文化年展
				Intent whnz = new Intent();
				whnz.setClass(this, CulturalExhibitActivity.class);
				// whnz.setClass(this, CulturalExhibitListActivity.class);
				whnz.putExtra("title", getLanguageString("文化年展"));
				startActivity(whnz);
				break;
			case 7: // 地图导航
				Intent huichangzhinan = new Intent();
				huichangzhinan.setClass(this, BaiDuMapActivity.class);
				startActivity(huichangzhinan);
				break;
			}
		}
	}

	public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int count) {
			if (count == 0) {
			} else if (count > 0 && count < 100) {
				try {
					goRequest(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
			}
		}
	};
	// JPush收到通知的广播
	private BroadcastReceiver mBroadcastReceiverJPush = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MESSAGE_RECEIVED_ACTION)) {
				try {
					goRequest(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void registerBoradcastReceiverJPush() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(MESSAGE_RECEIVED_ACTION);
		// 注册广播
		registerReceiver(mBroadcastReceiverJPush, myIntentFilter);
	}

	// app启动时间的广播
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
	public void registerBoradcastReceiverScreen() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	// 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	public static int dip2px(Activity context, int dipValue) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dipValue, metrics);
	}

	// 把文件的下载完成 监听全部放到 主页上 广播接收系统所有下载完毕的消息，使用query+downloadid进行过滤
	class DownloadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取本次下载完毕广播的id号
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

			String messionsids = FWMainActivity.this.getSharedPreferences(
					"WIC", Activity.MODE_PRIVATE).getString("downloadmessions",
					"");
			if (messionsids == null) {
				messionsids = "";
			}
			String[] messionids = messionsids.split(",");

			long[] messions = new long[messionids.length];

			for (int i = 0; i < messionids.length; i++) {
				if (!messionids[i].equals("")) {
					messions[i] = Long.parseLong(messionids[i]);
				}
			}
			for (int i = 0; i < messions.length; i++) {
				if (messions[i] == id) {
					// 创建过滤对象
					Query q = new Query();
					// 设置过滤方式by id
					q.setFilterById(id);
					DownloadManager dm = (DownloadManager) FWMainActivity.this
							.getSystemService(Context.DOWNLOAD_SERVICE);
					// 下载事件数据安卓系统全部放在系统sqlite中，使用Query对象做过滤，然后dm.query()查询出相对应的下载事件相关信息；返回cursor
					Cursor c = dm.query(q);
					if (c.moveToNext()) {// 下载任务必定产生一条相关的任务数据，但是下载成功与否，需要做判断
						String status = c.getString(c.getColumnIndex("status"));// 这里的status如果用getString获取，值是200
																				// 字符串类型，如果用getInt获取，值是8
																				// ，int类型，耐人寻味...
						if (status.equals("200")) {// 下载任务必定产生一条相关的任务数据，但是下载成功与否，需要做判断
													// status字段存放下载完成结果，200成功404找不到目标URL
													// 比如：Tomcat目录下的apk文件被勿删，或者文件名称错误，或者文件夹名称错误，都会引起下面代码的空指针异常
							String filepath = c.getString(c
									.getColumnIndex("local_uri"));// 下载数据有很多字段，可以循环打印出来根据需要做处理
							Uri fileuri = Uri.parse(filepath);
							// zyj添加
							String fpath = fileuri.getPath();
							String filename = fpath.substring(fpath
									.lastIndexOf("/") + 1);
							if (filename.contains(".apk")) {
								FWMainActivity.this.openAppFile(new File(
										fileuri.getPath()));
								c.close();
							} else {
								Toast.makeText(
										FWMainActivity.this,
										BaseActivity.getLanguageString(filename
												+ "下载完成"), Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							Toast.makeText(FWMainActivity.this,
									BaseActivity.getLanguageString("下载失败"),
									Toast.LENGTH_SHORT).show();
							c.close();
							return;
						}
					}// 下载数据存在
					else {
						c.close();
					}// 下载数据不存在
				}// 匹配成功，自动打开下载完成的应用
			}// 循环完毕
		}
	}

	// 在手机上打开文件
	private void openAppFile(File f) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		// 设置目标文件的打开方式
		String type = "application/vnd.android.package-archive";
		// 设置intent的目标文件的uri与打开方式MimeType
		intent.setDataAndType(Uri.fromFile(f), type);// 核心方法 设置Intent的数据对象和操作类型
		// 启动intent 打开文件
		this.startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 10 && resultCode == 100) {// 用户注销

			// 清除本应用通知栏显示的通知
			try {
				NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				notiManager.cancelAll();
			} catch (Exception e) {
				// TODO: handle exception
			}
			DbUtils db = DbUtils.create(context);
			try {
				db.dropTable(User.class);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JPushInterface.stopPush(context);
			Intent i = new Intent();
			i.setClass(getApplicationContext(), DengLuActivity.class);
			i.putExtra("yanzheng", 0);
			this.startActivity(i);
			this.finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			new AlertDialog.Builder(context)
					.setTitle(getLanguageString("确认"))
					.setMessage(getLanguageString("确定退出程序") + "?")
					.setPositiveButton(getLanguageString("确定"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
									MyApplication.finishProgram();
								}
							})
					.setNegativeButton(getLanguageString("取消"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}

							}).show();
		}

		return false;
	}

	// 唤醒时调用的方法
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
		try {
			goRequest(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CheckForUpdate();
		isForeground = true;
		if (!isActive) {
			TimeEnd = System.currentTimeMillis() / 1000;
			Integer time = getResources().getInteger(R.integer.time);
			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
				BaseActivity.loginValidation(context);
			}
		}
	}

	@Override
	public void onPause() {
		isForeground = false;
		super.onPause();
		JPushInterface.onPause(this);
	}

	public void CheckForUpdate() {
		SharedPreferences prefs = context.getSharedPreferences(
				"lastUpdateTime", Activity.MODE_PRIVATE);
		long lastUpdateTime = prefs.getLong("lastUpdateTime", 0);

		Calendar c = Calendar.getInstance();// 日历对象
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		if (c.get(Calendar.HOUR_OF_DAY) < 10) {
			hour = 0 + c.get(Calendar.HOUR_OF_DAY);
		}
		if (c.get(Calendar.MINUTE) < 10) {
			min = 0 + c.get(Calendar.MINUTE);
		}
		if (c.get(Calendar.SECOND) < 10) {
			second = 0 + c.get(Calendar.SECOND);
		}
		/* Should Activity Check for Updates Now? */
		if ((lastUpdateTime + (24 * 60 * 60 * 1000 - (hour * 60 * 60 + min * 60 + second) * 1000)) < System
				.currentTimeMillis()) {
			// if ((lastUpdateTime + 60 * 1000) < System.currentTimeMillis()) {
			/* Save current timestamp for next Check */
			lastUpdateTime = System.currentTimeMillis();
			SharedPreferences.Editor editor = prefs.edit();
			editor.putLong("lastUpdateTime", lastUpdateTime);
			editor.commit();

			/* Start Update */
			// 主界面检查版本>>>
			AsyncTaskForCheckApkVersion atapk = new AsyncTaskForCheckApkVersion(
					h.obtainMessage(), this.getApplicationContext());
			atapk.execute(1);
		}

	}

	// 挂起时调用的方法
	@Override
	protected void onStop() {
		super.onStop();
		if (!BaseActivity.isAppOnForeground(this)) {
			isActive = false;
			TimeStart = System.currentTimeMillis() / 1000;
		}
	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		refreshtime = false;
		if (mBroadcastReceiver != null) {
			this.unregisterReceiver(mBroadcastReceiver);
		}
		this.unregisterReceiver(downreceiver);
		this.unregisterReceiver(mBroadcastReceiverJPush);
		MyApplication.remove(this);
		isOpenSiXin = null;
		super.onDestroy();
	}

	public void getRongData() {
		try {
			// zyj 新增获取用户融云信息和群组融云信息
			zAsyncTaskForRongGlobalUser user_at = new zAsyncTaskForRongGlobalUser(
					ronguser_handler.obtainMessage(), context, Constant.decode(
							Constant.key, Parent.getUserId()), null);
//			user_at.execute(0);
			user_at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// zyj融云用户信息的handler
	Handler ronguser_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 获取融云user信息
				String message = (String) msg.obj;

				try {
					zAsyncTaskForRongGlobalGroup group_at = new zAsyncTaskForRongGlobalGroup(
							ronggroup_handler.obtainMessage(), context,
							Constant.decode(Constant.key, Parent.getUserId()),
							null);
//					group_at.execute(0);
					group_at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 1:
				// Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				// Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				break;

			default:
				break;
			}
		};
	};
	// zyj融云群组信息的handler
	Handler ronggroup_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// if (pro != null)
			// pro.dismiss();
			switch (msg.what) {
			case 0:// 获取融云user信息
				String message = (String) msg.obj;
				// finish();
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;

			default:
				break;
			}
		};
	};
}
