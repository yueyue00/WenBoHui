package com.gheng.exhibit.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.gheng.exhibit.application.MyApplication;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * App中常用的工具类<br>
 * 这个类中定义了一系列私有的常量，便于保存ShareData时直接调用，防止保存或者获取数据时因为名称不一样造成bug
 * 
 * @author Administrator
 * 
 */
public class AppTools {

	private static TelephonyManager telephonyManager = null;

	private AppTools() {
	}

	/**
	 * 获取手机IMEI
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getIMEI(Context mContext) {
		String imei;
		try {
			if (telephonyManager == null) {
				telephonyManager = (TelephonyManager) mContext
						.getSystemService(Context.TELEPHONY_SERVICE);
			}
			imei = telephonyManager.getDeviceId();
		} catch (Exception e) {
			return "";
		}
		if (imei == null) {
			return "";
		}
		return imei;
	}

	public static <E> boolean isBlack(List<E> list) {
		return (list == null || list.size() == 0);
	}

	public static <E> boolean isNotBlack(List<E> list) {
		return !isBlack(list);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = listView.getListPaddingTop()
				+ listView.getListPaddingBottom() + totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 获取指定类的泛型
	 */
	public static Class<?> getGenericClass(Class<?> clazz) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (!(params[0] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[0];
	}

	/**
	 * 调用系统浏览器
	 */
	public static void invokeBrowser(Activity activity, String url) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		activity.startActivity(intent);
	}

	/**
	 * 调用系统打电话页面
	 */
	public static void call(Activity activity, String number) {
		Uri uri = Uri.parse("tel:" + number);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		activity.startActivity(intent);
	}

	public static void copy(Context myContext, String ASSETS_NAME,
			String savePath, String saveName) {
		String filename = savePath + File.separator + saveName;
		File dir = new File(savePath);
		if (!dir.exists())
			dir.mkdir();
		try {
			if (!(new File(filename)).exists()) {
				InputStream is = myContext.getResources().getAssets()
						.open(ASSETS_NAME);
				FileOutputStream fos = new FileOutputStream(filename);
				byte[] buffer = new byte[7168];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copyFromUrl(String path, String savePath, String saveName) {
		String filename = savePath + File.separator + saveName;
		File dir = new File(savePath);
		if (!dir.exists())
			dir.mkdir();
		try {
			// File f = new File(filename);
			// if (f.exists()) {
			// f.delete();
			// }
			URL url = new URL(path);
			InputStream is = url.openStream();
			FileOutputStream fos = new FileOutputStream(filename);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝ghmap.data ghmap.sld ghmap.lst到工程默认存储目录中
	 */
	public static void copyMapData(Context context) {
		// String path = getRootPath();
		String path = context.getCacheDir().getAbsolutePath();
		System.out.println(path);
		String filename = "ghmap.dat";
		{
			File dataFile = new File(path + File.separator + filename);
			if (dataFile.exists()) {
				dataFile.delete();
			}
		}
		{
			File dataFile = new File(path + File.separator + "ghmap.sld");
			if (dataFile.exists()) {
				dataFile.delete();
			}
		}
		// {
		// File dataFile = new File(path + File.separator + "ghmap.lst");
		// if (dataFile.exists()) {
		// dataFile.delete();
		// }
		// }
		copy(context, filename, path, filename);
		copy(context, "ghmap.sld", path, "ghmap.sld");
	}

	public static String imageChange(String url) {
		if (StringTools.isBlank(url)) {
			return null;
		}

		return Constant.SERVER_URL + url;
	}

	public static String getRootPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在

		if (sdCardExist) { // 如果SD卡存在，则获取跟目录
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
			sdDir = Environment.getRootDirectory();// 如果没有SD卡，则存放于内存卡根目录
		}
		return sdDir.toString();
	}

	/**
	 * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
	 * 
	 * @param content
	 *            将要生成一维码的内容
	 * @return 返回生成好的一维码bitmap
	 * @throws WriterException
	 *             WriterException异常
	 */
	public static Bitmap CreateOneDCode(Context context, String content,
			BarcodeFormat barcodeFormat) throws WriterException {
		// 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				barcodeFormat, UIUtils.dip2px(context, 400),
				UIUtils.dip2px(context, 150));
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static String getVersionCode() {
		String packageName = MyApplication.getInstance().getPackageName();
		try {
			return MyApplication.getInstance().getPackageManager()
					.getPackageInfo(packageName, 0).versionCode
					+ "";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "0";
		}
	}

	public static boolean isLogin() {
		return StringTools.isNotBlank(SharedData.getUser())
				&& SharedData.getBoolean("_register");
	}

	public static String getVersionName() {
		String packageName = MyApplication.getInstance().getPackageName();
		try {
			return MyApplication.getInstance().getPackageManager()
					.getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0";
		}
	}

	public static boolean getVerify(Context context) {
		// boolean isVerify = true;
		// if (isWifiEnabled(context)) {
		// WifiManager wifiManager = (WifiManager)
		// context.getSystemService(context.WIFI_SERVICE);
		// WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		// if (null != wifiInfo) {
		// Log.d("wifiInfo", wifiInfo.toString());
		// String ssid = wifiInfo.getSSID();
		// if (!StringTools.isBlank(ssid)) {
		// Log.d("SSID", ssid);
		// ssid = ssid.toUpperCase();
		// if (ssid.contains("RSE-FREE")) {
		// isVerify = false;
		// }
		// }
		// }
		// }

		return true;
	}

	/**
	 * 判断WIFI是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()
				.getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 判断是否是3G网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * Android 获取本机IP地址方法：
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * 获得手机号
	 * 
	 * @param activity
	 * @return
	 */
	public static String getLocalPhoneNum(Activity activity) {
		TelephonyManager tm = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = tm.getLine1Number();

		return phoneId;
	}

	public static String filterEmoji(String source) {
		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		}

		StringBuilder buf = null;
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			} else {
			}
		}
		if (buf == null) {
			return "";
		} else {
			if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}

	// 判别是否包含Emoji表情
	public static boolean containsEmoji(String str) {
		int len = str.length();
		for (int i = 0; i < len; i++) {
			if (isEmojiCharacter(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
	}
}
