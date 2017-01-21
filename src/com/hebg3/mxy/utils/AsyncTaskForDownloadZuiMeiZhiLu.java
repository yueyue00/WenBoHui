package com.hebg3.mxy.utils;

import java.io.File;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.webkit.MimeTypeMap;

public class AsyncTaskForDownloadZuiMeiZhiLu extends AsyncTask<Object, Object, Object> {

	Context cont;
	String applicationdownloadurl;
	String filename = BaseActivity.getLanguageString("醉红之路") + ".apk";// 下载文件的文件名
	SharedPreferences sp;

	public AsyncTaskForDownloadZuiMeiZhiLu(Context cont, String applicationdownloadurl) {
		this.cont = cont;
		this.applicationdownloadurl = applicationdownloadurl;
		sp = cont.getSharedPreferences("WIC", Activity.MODE_PRIVATE);
	}

	@Override
	protected Object doInBackground(Object... arg0) {

		// 下载文件存放路径提前设置
		File f = new File(Constant.apkTarget);
		if (!f.exists()) {
			f.mkdir();
		}
		// 如果存在apk就先删除
		File apk = new File(Constant.apkTarget + filename);
		if (apk.exists()) {
			apk.delete();
		}

		// 下载管理器
		DownloadManager dm = (DownloadManager) cont
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(applicationdownloadurl);// 将url解析成uri
		Request request = new Request(uri); // 创建请求对象，封装uri
		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);

		// 设置禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：
		// android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
		// request.setShowRunningNotification(false); // 下载通知是必须的，所以这里不需要设置

		// 显示下载界面
		request.setVisibleInDownloadsUi(true);
		// 判断下载目录是否存在，如果不存在，手动创建getExternalStoragePublicDirectory方法需要sdcard的支持

		// request请求对象设置下载文件保存路径，一般是使用公共目录，sdcard的某个文件夹。
		// setDestinationInExternalPublicDir第一个参数是公用目录的文件夹名称（之前已经创建），第二个参数是文件下载后的名称
		request.setDestinationInExternalPublicDir(Constant.apkTarget, filename);

		// 表示允许MediaScanner扫描到这个文件，默认不允许。
		request.allowScanningByMediaScanner();
		// 设置下载任务栏的显示标题
		request.setTitle(filename);
		// 设置下载中通知栏提示的介绍
		request.setDescription(BaseActivity.getLanguageString("醉红之路")+"APP");

		// 表示下载进行中和下载完成的通知栏是否显示。默认只显示下载通知，完成之后下载任务通知就消失了。 设置这个方法更人性化
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		// 设置下载文件的mineType。因为下载管理Ui中点击某个已下载完成文件及下载完成点击通知栏提示都会根据mimeType去打开文件
		// 可以同时设置某个Activity的intent-filter为application/com.trinea.download.file，用于响应点击的打开文件。
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		String filetype = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
				.getFileExtensionFromUrl(applicationdownloadurl));
		request.setMimeType(filetype);// 以上三句话必须要有，否则下载文件不能通过安卓系统自动安装

		// 把id保存好，在广播里面要用 每次下载任务开始，DownloadManager都会给下载任务一个唯一的id long类型
		long id = dm.enqueue(request);
		// 下载任务id放到sharedPreference中
		String zmessionsids = sp.getString("zdownloadmessions", "");
		if (zmessionsids == null) {
			zmessionsids = "";
		}
		Editor e = sp.edit();
		e.putString("zdownloadmessions", id + "," + zmessionsids);
		e.apply();

		return id;
	}
}
