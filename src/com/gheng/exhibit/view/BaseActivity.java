package com.gheng.exhibit.view;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.BitmapHelper;
import com.gheng.exhibit.http.HttpWrapper;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.LogUtils;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.utils.TypeFaceUtils;
import com.gheng.exhibit.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * 基础Activity
 * 
 * @author zhao
 */
public abstract class BaseActivity extends Activity {

	protected BaseActivity context;
	protected HttpWrapper http = null;
	protected BitmapUtils bitmapUtils;
	private int[] wh = null;

	private static Toast toast;

	private static DbUtils dbUtils;

	private static Map<String, Language> map = new HashMap<String, Language>();

	private boolean isInit = false;

	private boolean isDestroy = false;

	private Handler h = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (!isInit && !isDestroy) {
				TypeFaceUtils.setTypeValue(context);
				init();
				setI18nValue();
				isInit = true;
			}
		};
	};

	static {
		System.out.println("aaa:MainActivity(BaseActivity):sharep:dbversion"
				+ SharedData.getInt("dbversion", 0));
		if (!checkDatabaseIsExit()
				|| SharedData.getInt("dbversion", 0) < Constant.DB_VSERION) {
			copyDatabases();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dbversion", Constant.DB_VSERION);
			SharedData.commit(map);
		}
		DaoConfig daoConfig = new DaoConfig(MyApplication.getInstance());
		daoConfig.setDbName("THIS.sqlite");
		//zyj 修改数据库保存位置
		// daoConfig.setDbDir(AppTools.getRootPath() + File.separator
		// + "databases");
		daoConfig.setDbDir(Constant.databaseTarget);

		dbUtils = DbUtils.create(daoConfig);
		System.out
				.println("aaaa" + "MainActivity(BaseActivity)界面创建dbtuils对象成功");
		try {
			List<Language> list = getDbUtils().findAll(Language.class);
			if (list != null)
				for (Language model : list) {
					map.put(model.getZhName().trim(), model);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		http = new HttpWrapper();
		if (null == wh) {
			wh = UIUtils.getWindows(this);
			Constant.SCREEN_WIDTH = wh[0];
			Constant.SCREEN_HEIGHT = wh[1];
		}
		bitmapUtils = BitmapHelper.createBitmapUtils(this);
		context = this;
		if (toast == null) {
			toast = Toast.makeText(BaseActivity.this, "", Toast.LENGTH_LONG);
		}

		initBaiduTongji();// 百度统计
		h.sendEmptyMessage(1);
	}

	private static Context mContext;

	/**
	 * 程序是否在前台运行判断方法
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 登录验证方法
	 */
	public static void loginValidation(Context context) {
		mContext = context;
		// 打开数据库
		// User Parent = null;
		// try {
		// DbUtils db = DbUtils.create(context);
		// Parent = db.findFirst(Selector.from(User.class).where("id", "=",
		// "1"));
		// } catch (DbException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// ClientParams client = new ClientParams();
		// client.url = "/hylogin.do";
		// StringBuffer strbuf = new StringBuffer();
		// strbuf.append("method=passwordLogin&userid=");
		// strbuf.append(Parent.getUserId());
		// strbuf.append("&password=");
		// strbuf.append(Parent.getPassword());
		// strbuf.append("&lg=");
		// if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
		// strbuf.append("2"); // 获取是中英文 2是英文 1是中文
		// } else {
		// strbuf.append("1");
		// }
		// strbuf.append("&phonetype=android");
		// String str = strbuf.toString();
		// client.params = str;
		// NetLoginTask<YongHuMingDengLu> net = new
		// NetLoginTask<YongHuMingDengLu>(
		// hand.obtainMessage(), client, YongHuMingDengLu.class,
		// new YongHuMingDengLu(), context);
		// net.execute();
	}

	/**
	 * 初始化百度统计
	 */
	public void initBaiduTongji() {

		/*
		 * 设置渠道的推荐方法。该方法同setAppChannel（String），
		 * 如果第三个参数设置为true（防止渠道代码设置会丢失的情况），将会保存该渠道，每次设置都会更新保存的渠道，
		 * 如果之前的版本使用了该函数设置渠道
		 * ，而后来的版本需要AndroidManifest.xml设置渠道，那么需要将第二个参数设置为空字符串,并且第三个参数设置为false即可。
		 * appChannel是应用的发布渠道，不需要在mtj网站上注册，直接填写就可以 该参数也可以设置在AndroidManifest.xml中
		 */
		StatService.setAppChannel(this, "乌镇会议", true);
		// 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动退出会产生大量日志。
		StatService.setSessionTimeOut(1);
		/*
		 * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s到30s之间<br/>
		 * 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
		 * 
		 * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/>
		 * 这个参数的设置暂时只支持代码加入， 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
		 */
		StatService.setLogSenderDelayed(0);
		/*
		 * 用于设置日志发送策略 嵌入位置：Activity的onCreate()函数中
		 * 
		 * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum.
		 * SET_TIME_INTERVAL, 1, false); 第二个参数可选： SendStrategyEnum.APP_START
		 * SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
		 * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、
		 * 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
		 * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
		 */
		StatService.setSendLogStrategy(this,
				SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
		// 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
		StatService.setDebugOn(false);

	}

	public void onResume() {
		super.onResume();

		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();

		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onPause(this);
	}

	private static void copyDatabases() {
		//zyj 修改数据库保存位置
		// String fileP = MyApplication.getInstance().getCacheDir().getParent()
		// + File.separator + "databases";
		// fileP = AppTools.getRootPath() + File.separator + "databases";
		String fileP = Constant.databaseTarget;
		File file = new File(fileP);
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(file, "THIS.sqlite");
		if (file.exists()) {
			file.delete();
		}
		AppTools.copy(MyApplication.getInstance(), "THIS.sqlite", fileP,
				"THIS.sqlite");
	}

	private static boolean checkDatabaseIsExit() {
		// zyj 修改数据库保存位置
		// String fileP = MyApplication.getInstance().getCacheDir().getParent()
		// + File.separator + "databases";
		// fileP = AppTools.getRootPath() + File.separator + "databases";
		// File file = new File(fileP + File.separator + "THIS.sqlite");
		String fileP = Constant.databaseTarget;
		File file = new File(fileP + "THIS.sqlite");
		return file.exists();
	}

	protected abstract void setI18nValue();

	protected void init() {
	};

	public static DbUtils getDbUtils() {
		if (!dbUtils.getDatabase().isOpen())
			dbUtils = DbUtils.create(dbUtils.getDaoConfig());
		return dbUtils;
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ViewUtils.inject(this);
		h.sendEmptyMessage(1);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		ViewUtils.inject(this);
		h.sendEmptyMessage(1);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		ViewUtils.inject(this);
		h.sendEmptyMessage(1);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		isDestroy = true;
		if (dbUtils.getDatabase().isOpen())
			dbUtils.close();
	}

	public void toastLong(Object text) {
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setText(String.valueOf(text));
		toast.show();
	}

	public void toastShort(Object text) {
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setText(String.valueOf(text));
		toast.show();
	}

	public void toastNetError() {
		toastShort(getLanguageString("网络不给力"));
	}

	public String getLanguageString(int code) {
		return "";
	}

	public static String getLanguageString(String languageStringZh) {
		if (StringTools.isBlank(languageStringZh)) {
			return "";
		}
		languageStringZh = languageStringZh.trim();
		int languageType = SharedData.getInt("i18n", Language.ZH);

		genericNew(languageStringZh);

		if (languageType == Language.ZH) {
			return languageStringZh;
		} else if (languageType == Language.EN) {
			Language model = map.get(languageStringZh);
			String result = null;
			try {
				if (model == null) {
					Selector selector = Selector.from(Language.class);
					selector.where("LanguageStringZh", "=", languageStringZh);
					model = getDbUtils().findFirst(selector);
				}
				if (model != null) {
					map.put(model.getZhName().trim(), model);
					result = model.getEnName();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (result == null)
				result = languageStringZh;
			return result;
		} else {
			Language model = map.get(languageStringZh);
			String result = null;
			try {
				if (model == null) {
					Selector selector = Selector.from(Language.class);
					selector.where("LanguageStringZh", "=", languageStringZh);
					model = getDbUtils().findFirst(selector);
				}
				if (model != null) {
					map.put(model.getZhName().trim(), model);
					result = model.getTwName();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (result == null)
				result = languageStringZh;
			return result;
		}

	}

	private static void genericNew(String languageStringZh) {
		Language model = map.get(languageStringZh);
		if (model == null) {
			String sql = "select max(id) from LanguageStrings";
			long id = 9999;
			try {
				Cursor execQuery = getDbUtils().execQuery(sql);
				execQuery.moveToNext();
				id = execQuery.getLong(0);
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			model = new Language();
			model.setId(id + 1);
			model.setState(1);
			model.setZhName(languageStringZh);
			try {
				getDbUtils().save(model);
				map.put(languageStringZh, model);
			} catch (DbException e) {
				LogUtils.d("错误", e.getMessage());
				System.out
						.println("----------------------------------------------------------------");
				e.printStackTrace();
			}
		}
	}

	public void startTo(Class<?> cls, Bundle bd) {
		Intent intent = new Intent(this, cls);
		if (bd != null) {
			intent.putExtras(bd);
		}
		startActivity(intent);
	}

	public void startTo(Class<?> cls) {
		startTo(cls, null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!isInit && keyCode == KeyEvent.KEYCODE_BACK)
			return true;
		return super.onKeyDown(keyCode, event);
	}

	// 获取手机屏幕大小《0是普通屏，1是大屏》
	public static int getPingMuSize(Context con) {
		// 测量手机尺寸
		DisplayMetrics dm = new DisplayMetrics();
		dm = con.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int widpx = px2dip1(con, screenWidth);
		if (widpx > 400) {
			return 1;
		}
		return 0;
	}

	// zyj添加
	public static int px2dip1(Context context, int pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 **/
	public static int dip2px(Activity context, float dipValue) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dipValue, metrics);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Activity context, float pxValue) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		float scale = metrics.density;
		return (int) (pxValue / scale + 0.5f);
	}

	// 判断发送时是否包含emoji表情 ------------------------------------马晓勇加

	/**
	 * 检测是否有emoji表情
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isContainEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是Emoji
	 * 
	 * @param codePoint
	 *            比较的单个字符
	 * @return
	 */
	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * cookie失效或userid被后台删除，需要用户重新登录
	 */
	public static void gotoLoginPage(Activity cont) {
		// 清除本应用通知栏显示的通知
		try {
			NotificationManager notiManager = (NotificationManager) cont
					.getSystemService(NOTIFICATION_SERVICE);
			notiManager.cancelAll();
		} catch (Exception e) {
			// TODO: handle exception
		}
		DbUtils db = DbUtils.create(cont);
		try {
			db.dropTable(User.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JPushInterface.stopPush(cont);

		Intent i = new Intent();
		i.setClass(cont, DengLuActivity.class);
		i.putExtra("yanzheng", 1);
		cont.startActivity(i);
		// 销毁所有activity
		MyApplication.finishProgram();
	}
}
