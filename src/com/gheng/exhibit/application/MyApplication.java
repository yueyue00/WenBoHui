package com.gheng.exhibit.application;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.gheng.exhibit.rongyun.utils.RongCloudEvent;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * 自定义Application，在里面进行Activity的管理和对全局异常的处理
 * 
 * @author Administrator
 * 
 */
public class MyApplication extends Application {

	private static MyApplication application = null;

	static {
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		final File imgCacheDir = new File(
				Environment.getExternalStorageDirectory() + "/hebg3Tools/cache",
				"images");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).diskCache(new UnlimitedDiscCache(imgCacheDir))
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(70 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.writeDebugLogs()
				.imageDownloader(new BaseImageDownloader(this, 20000, 20000))
				.build();
		ImageLoader.getInstance().init(config);
		SDKInitializer.initialize(this);
		if (getApplicationInfo().packageName
				.equals(getCurProcessName(getApplicationContext()))
				|| "io.rong.push"
						.equals(getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第一步 初始化
			 */
			RongIM.init(this);

			if (getApplicationInfo().packageName
					.equals(getCurProcessName(getApplicationContext()))) {
				RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
				RongCloudEvent.init(this);
				DemoContext.init(this);
			}
		}
	}

	public static void initImageLoader(Context context) {

		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static MyApplication getInstance() {
		return application;
	}

	/** 用于销毁所有被创建的acitivty */
	public static List<Activity> activityList = new ArrayList<Activity>();

	/** 销毁list中存储的对应activity */
	public static void remove(Activity activity) {
		activityList.remove(activity);
	}

	/** 向list中添加activity对象 */
	public static void add(Activity activity) {
		activityList.add(activity);
	}

	/** 销毁所有被启用的activity */
	public static void finishProgram() {
		int n = activityList.size() - 1;
		for (int i = n; i > -1; i--) {
			activityList.get(i).finish();
		}
	}

	/**
	 * 获得当前进程的名字
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {

				return appProcess.processName;
			}
		}
		return null;
	}
	private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

        /**
         * 收到消息的处理。
         * @param message 收到的消息实体。
         * @param left 剩余未拉取消息数目。
         * @return
         */
        @Override
        public boolean onReceived(Message message, int left) {
            //开发者根据自己需求自行处理
        	System.out.println("zyjzyj"+"000000");
            return false;
        }
    }
}
