package com.gheng.exhibit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constant {

	// ==================================乌镇正式服务器地址=======================================
	/**
	 * 正式服务器地址
	 */
	// public static String DOMAIN = "https://service.wicwuzhen.cn:8088";
	/**
	 * 保存cookie（是cookie请求时和保存cookie、获取cookie时用的）
	 */
	// public final static String domain = "service.wicwuzhen.cn";
	// ==================================乌镇测试服务器地址=======================================
	/**
	 * =======测试服务器地址
	 */
	// public final static String DOMAIN = "http://wuzhen.smartdot.com:8088";

	/**
	 * \ =======测试服务器地址 保存cookie
	 */
	// public final static String domain = "wuzhen.smartdot.com";
	// ===================================慧点正式服务======================================
	/**
	 * =======慧点正式服务地址
	 */
//	 public final static String DOMAIN = "http://wuzhen.smartdot.com:8088/";
	// 正式服务器域名
	public final static String DOMAIN = "https://mobile.silkroaddunhuang.com:8088";
//	public final static String DOMAIN = "http://172.22.59.2:8080/wenbo2";
	// 测试服务器域名
//	public final static String DOMAIN = "http://wuzhen.smartdot.com:8088/wenbo2";
//	public final static String DOMAIN = "http://172.20.15.11:8080/wenbo2";
	// 正式服务器ip
	// public final static String DOMAIN = "http://220.197.198.106:8088";
	// 测试服务器
	// public final static String DOMAIN =
	// "http://wuzhen.smartdot.com:8088/lvfa";
	// 个人测试
//	 public final static String DOMAIN = "http://192.168.1.118:8080/wenbo2";

	public final static int fuwq = 0;// 0:代表正式服务器;1:代表测试服务器
	/**
	 * . =======慧点正式服务地址 保存cookie
	 */
	public final static String domain = "mobile.silkroaddunhuang.com";
//	 public final static String domain = "wuzhen.smartdot.com";
//	 public final static String domain = "192.168.1.118";

	public final static String apkTarget = "Download"; // apk下载路径
	public final static String target = "/sdcard/wenbohui/cache/"; // 大会资料的下载路径
	public final static String dianpingTarget = "/sdcard/wenbohui/photo/"; // 点评图片下载路径
	public final static String uploadTarget = "/sdcard/wenbohui/upload/"; // 点评图片下载路径
	public final static String databaseTarget="/sdcard/wenbohui/databases/";

	/**
	 * =====================================其他常量配置==============================
	 * ===================
	 */

	public static final String EMPTY_STRING = "";

	/**
	 * 建筑物的名称
	 */
	public static final String SELECT_BUILDING = "sb";

	/**
	 * APP的配置信息目录
	 */
	public static final String APP_CONFIG = "apgather";

	public static final String TAG = "AP_TAG";

	private static Properties p = new Properties();

	static {
		// init("config.properties");
	}
	/**
	 * 建筑物信息表，利用;隔开， 其中每个建筑的ID,NAME
	 */
	public static final String BUILDING_INFO = getProperty("building.info",
			EMPTY_STRING);

	public static final String APP_FILE_ROOT = getAppRoot().getAbsolutePath();
	public static final String APP_CACAHE_DIRNAME = "webviewCache";

	// MD5加密串
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	// DES加密
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	// DES加密需要和服务器统一的加密私钥，长度不能够小于8位
	public static final String key = "95880288";

	/**
	 * 静态读入属性文件到Properties p变量中
	 * 
	 * @param propertyFileName
	 */
	private static void init(String propertyFileName) {
		InputStream in = null;
		try {
			File file = getAppRoot();
			String filePath = file.getAbsolutePath() + File.separator
					+ propertyFileName;
			FileInputStream s = new FileInputStream(filePath);
			p.load(s);
			if (in != null)
				p.load(in);
		} catch (IOException e) {
			Log.e(TAG, "load " + propertyFileName + " into Constants error!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e(TAG, "close " + propertyFileName + " error!");
				}
			}
		}
	}

	/**
	 * 封装了Properties类的getProperty函数,使p变量对子类透明.
	 * 
	 * @param key
	 *            property key.
	 * @param defaultValue
	 *            当使用property key在properties中取不到值时的默认值.
	 */
	private static String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	private static File getAppRoot() {
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root + File.separator + APP_CONFIG);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	// 验证手机号码
	public static boolean checkString(String s) {
		return s.matches("^1[3|4|5|7|8][0-9]\\d{8}$");
	}

	public static boolean checkUrlString(String s) {
		return s.matches("[a-zA-z]+://[^\\s]*");

	}

	public static String CACHE_DIR = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ "Android/data"
			+ File.separator + "com.gheng.exhibit";

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	/** 本地数据库每次查询的数据记录数 **/
	public static int PAGE_SIZE = 20;

	public static String MALL_ID = "21";

	public static String IMEI = "";

	public static String cookie;

	/**
	 * 删除cookie
	 * 
	 * @param context
	 */
	public static void removeCookie(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
		cookie = null;
	}

	/**
	 * 获取cookie
	 * 
	 * @param context
	 * @param domain
	 * @return
	 */
	public static String getCookie(Context context, String domain) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();
		cookieManager.removeExpiredCookie();
		String cookie = cookieManager.getCookie(domain);
		if (cookie == null)
			cookie = "";
		// Log.v("cookie", "---" + cookie);
		CookieSyncManager.getInstance().sync();
		return cookie;
	}

	/**
	 * 保存cookie
	 * 
	 * @param context
	 * @param domain
	 * @param con
	 */
	public static void setCookie(Context context, String domain,
			HttpURLConnection con) {

		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();
		cookieManager.setAcceptCookie(true);
		String cookieVal = "";
		String key = "";
		for (int i = 1; (key = con.getHeaderFieldKey(i)) != null; i++) {
			if (key.equalsIgnoreCase("set-cookie")) {
				cookieVal = con.getHeaderField(i);
				cookieManager.setCookie(domain, cookieVal);// 保存加密后的cookie
				System.out.println("setcookie = " + cookieVal);
			}
		}
		CookieSyncManager.getInstance().sync();
	}

	// MD5加密
	public static String getMD5(String val) {
		byte[] m = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(val.getBytes());
			m = md5.digest();// 加密
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toHexString(m);
	}

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * 
	 * DES算法，加密
	 * 
	 * @param data待加密字符串
	 * 
	 * @param key加密私钥
	 *            ，长度不能够小于8位
	 * 
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * 
	 * @throws InvalidAlgorithmParameterException
	 * 
	 * @throws Exception
	 */

	// public static String encode(String key, String data) {
	// if (data == null)
	// return null;
	// try {
	// DESKeySpec dks = new DESKeySpec(key.getBytes());
	// SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	// // key的长度不能够小于8位字节
	// Key secretKey = keyFactory.generateSecret(dks);
	// Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
	// IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
	// AlgorithmParameterSpec paramSpec = iv;
	// cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
	// byte[] bytes = cipher.doFinal(data.getBytes());
	// return byte2String(bytes);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return data;
	// }
	// }

	private static final byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * 加密
	 * 
	 * @param encryptKey
	 * @param encryptString
	 * @return
	 */
	public static String encode(String encryptKey, String encryptString) {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher;
		byte[] encryptedData = null;
		try {
			cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			encryptedData = cipher.doFinal(encryptString.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Base64.encode(encryptedData);
	}

	/**
	 * 解密
	 */
	public static String decode(String decryptKey, String decryptString)
			throws Exception {
		byte[] byteMi = Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData);
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * 
	 * @return
	 */

	private static String byte2String(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase(Locale.CHINA);
	}
	 /**
     * 根据long类型数据获取String类型时间
     * @return
     */
	public static String getCurrentTime(long currentTime) {
		String str = "";
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日  HH:mm:ss     ");
		Date curDate = new Date(currentTime);// 获取当前时间
		System.out.println("===========long==当前时间为："+System.currentTimeMillis());
		str = formatter.format(curDate);
		return str;
	}
	
	public static Gson gson = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation().create();

	public static String SERVER_URL = "http://app.reed-sinopharm.com:8080";
	public static String API_URL = SERVER_URL + "/api/";
	public static String UDP_SERVER = "";
	public static String UDP_SERVER_OUT = "";
	public static int UDP_SERVER_PORT = 6500;

	public static String CHARSET = "UTF-8";

	public static String UUID = "E6C56DB5-DFFB-48D2-B060-D0F5A7-1096E0";
	public static int SCALE = 1;

	// public static long eid = 168800;

	public static long pchi2016 = 124550;

	public static int zoneid = 1;

	public static final int DB_VSERION = 88;
	public static final int MAP_VERSION = 12;
	/**
	 * 1：展位预售APP 2: 展会APP
	 */
	public static final int APP_TYPE = 1;

	// 分类类型
	public static final int TYPE_COMPANY = 1;// 展商
	public static final int TYPE_PRODUCT = 2;// 展品
	public static final int TYPE_SCHEDULE = 3;// 会议
	public static final int TYPE_SPEAKER = 4;// 演讲者
	public static final int TYPE_SCHEDULE_INFO = 5;// 演讲题目
	public static final int TYPE_APP_MAIN = 6;// app主页
	public static final int TYPE_BATCH_IMAGE = 7;// 展位图/展馆列表
	public static final int TYPE_NEWS = 8; // 新闻列表
	public static final int TYPE_SERVICE = 9; // 展会服务
	public static final int TYPE_HOWTO_GO = 10; // 如何到达
	public static final int TYPE_TOUR_ROUTE = 11; // 参观路线
	public static final int TYPE_RESTAURANT = 12; // 馆内餐饮
	public static final int TYPE_WEATHER = 13; // 天气
	public static final int TYPE_HOTEL = 14; // 酒店信息
	public static final int TYPE_CITY = 15; // 城市介绍
	public static final int TYPE_SERVICES = 16; // 服务设施
	public static final int TYPE_MINE = 17; // 我的
	public static final int TYPE_MINE_INFO = 18; // 我的信息
	public static final int TYPE_MINE_SCHEDULE = 19; // 我的会议
	public static final int TYPE_MINE_COMMENT = 20; // 我的评论
	public static final int TYPE_MORE = 21; // 更多
	public static final int TYPE_ABOUT_GUOYAO = 22; // 关于国药励展
	public static final int TYPE_ABOUT_CMEF = 23; // 关于CMEF
	public static final int TYPE_HOWTO_EXHIBIT = 24; // 符合参展
	public static final int TYPE_HOWTO_VISIT = 25; // 如何参观
	public static final int TYPE_CONTACT_US = 26; // 联系我们
	public static final int TYPE_COMMENT = 27; // 评论
	public static final int TYPE_COMMENT_SUBMIT = 28; // 评论提交页面
	public static final int TYPE_MINE_BINDMOBILE = 29; // 手机绑定页面
	public static final int TYPE_MINE_FAV = 30; // 我的收藏页面
	public static final int TYPE_REGISTER_AGREENMENT = 31; // 注册协议
	public static final int TYPE_REGISTER_INFO = 32; // 注册的个人信息页面
	public static final int TYPE_REGISTER_SELECT_BATCH = 33; // 注册的展会选择
	public static final int TYPE_REGISTER_QUESTION_INFO = 34; // 回答问题页面

	// 浏览的类型
	public static final int BROWSE_TYPE_LIST = 1; // 列表
	public static final int BROWSE_TYPE_INFO = 2; // 详情
	public static final int BROWSE_TYPE_MAIN = 3; // 主页类型

	// 怎么触发搜索时间的
	public static final int SERACH_TYPE_ENTER = 1; // 进入页面
	public static final int SERACH_TYPE_CLICK = 2; // 点击搜索按钮触发

	// 挑战到地图页面的控制值
	public static final String TO_MAP_TYPE_TKEY = "toMapType";
	public static final String SEARCH_TYPE = "searchType";
	public static final String MODEL_KEY = "model";
	public static final String KEYWORD = "keyword";
	public static String TIMECODE = "timeCode";

	public static final int FROM_EXHIBIT = 1;
	public static final int FROM_PRODUCT = 2;
	public static final int FROM_POITYPE = 3;
	public static final int FROM_ZONE = 4;

	public static final int MAP_LOCATION = 1;
	public static final int MAP_NAVTO = 2;
	// 104.08049,30.565796
	public static final double EXHIBIT_LNG = 104.08049;
	public static final double EXHIBIT_LAT = 30.565796;
	public static final String EXHIBIT_CITY = "成都";
	// 默认的注册 消失时间
	public static final int DEFAULT_MSG_TIMEOUT = 180;

	/**
	 * 服务人员-------》请求嘉宾日程需要传递的参数------》在嘉宾信息的基本信息里获取到与服务人员一对一的嘉宾vipid
	 */
	public static String VIPID = "";
	/**
	 * 用于判断某服务人员对应的是多个嘉宾还是单个嘉宾 应用于嘉宾信息界面
	 */
	public static boolean isMulti = true;
	/**
	 * 用于存取嘉宾列表信息的vip嘉宾数，以此来判断vip是单个还是多个
	 */
	public static int VIP_COUNT = -1;
	/**
	 * 用于判断某服务人员对应的是多个嘉宾还是单个嘉宾 应用于服务签到的vip模块
	 */
	public static boolean isMultiVip = true;
	/**
	 * 用于判断车辆服务人员 点击“出发”按钮的状态
	 */
	public static boolean GO = false;
	/**
	 * 用于判断车辆服务人员 点击“到达”按钮的状态
	 */
	public static boolean ARRIVE = false;
	/**
	 * 用于服务人员 服务签到 FixedTaskActivity界面 控制全选状态
	 */
	public static boolean STATEFORSELECTALL = false;
	/**
	 * 用于判断服务签到 固定任务 vip模块的二级界面跳转 根据vip人数来判断 0---》1个 1----》多个
	 */
	public static int SIGN = -1;
	/**
	 * 当服务签到的vip模块对应的vip列表人数为1的时候跳转二级界面用到的唯一标识，invitationCode
	 */
	public static String INVITATION_CODE = "";
	/**
	 * 当服务签到的vip模块对应的vip列表人数为1的时候跳转二级界面用到的title，该嘉宾的name
	 */
	public static String VIP_NAME = "";
	/**
	 * 保存当前登录人员的userjuese
	 */
	public static String USER_JUESE = "";
}
