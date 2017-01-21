package com.gheng.exhibit.http;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.DownLoadTask;
import com.gheng.exhibit.utils.DownLoadTask.DownLoadListener;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.smartdot.wenbo.huiyi.R;

public class VersionService extends Service {

	private NotificationManager notificationMrg;
	private int old_process = 0;
	private boolean isFirstStart = false;

	private static String fileName = "CMEF.apk";
	private static String url;
	private static BaseActivity a;

	public void onCreate() {
		super.onCreate();
		isFirstStart = true;
		notificationMrg = (NotificationManager) this
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		DownLoadTask task = new DownLoadTask(new DownLoadImpl());
		task.setOutPath(AppTools.getRootPath());
		task.setFileOutName(fileName);
		task.setPath(url);
		task.execute();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			isFirstStart = false;
			int loading_process = msg.what;
			// 1为出现，2为隐藏
			if (loading_process > 99) {
				notificationMrg.cancel(0);
				stopSelf();
				return;
			}
			if (loading_process > old_process) {
				displayNotificationMessage(loading_process);
			}
			old_process = loading_process;
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void displayNotificationMessage(int count) {

		// Notification的Intent，即点击后转向的Activity
		Intent notificationIntent1 = new Intent(this, this.getClass());
		notificationIntent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent1 = PendingIntent.getActivity(this, 0,
				notificationIntent1, 0);

		int lg = SharedData.getInt("i18n", Language.ZH);
		String notitle = "CMEF版本升级";
		String n_title = "升级提示";
		String n_text = "当前进度：";
		if (lg == Language.EN) {
			notitle = "CMEF APP upgrade";
			n_title = "Upgrade Tips";
			n_text = "Current progress:";
		}
		// 创建Notifcation
		Notification notification = new Notification(R.drawable.ic_launcher,
				notitle, System.currentTimeMillis());// 设定Notification出现时的声音，一般不建议自定义
		if (isFirstStart || count > 97) {
			notification.defaults |= Notification.DEFAULT_SOUND;// 设定是否振动
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// 创建RemoteViews用在Notification中
		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.notification_version);

		contentView.setTextViewText(R.id.n_title, n_title);
		contentView.setTextViewText(R.id.n_text, n_text + count + "% ");

		contentView.setProgressBar(R.id.n_progress, 100, count, false);

		notification.contentView = contentView;
		notification.contentIntent = contentIntent1;

		notificationMrg.notify(0, notification);
	}

	public static void beginning(BaseActivity context, final String url) {
		VersionService.url = url;
		VersionService.a = context;
		
		Bundle bd = new Bundle();
		Intent intent = new Intent(context, VersionService.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		bd.putInt("op", 1);
		intent.putExtras(bd);
		context.startService(intent);
	}

	class DownLoadImpl implements DownLoadListener {

		@Override
		public void onStart() {
			displayNotificationMessage(0);
		}

		@Override
		public void onEnd() {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(new File(AppTools.getRootPath(), fileName)),
					"application/vnd.android.package-archive");
			VersionService.a.startActivity(intent);
		}

		@Override
		public void onUpdate(int value) {
			Message msg = VersionService.this.mHandler.obtainMessage();
			msg.what = value;
			VersionService.this.mHandler.sendMessage(msg);
		}

		@Override
		public void onError(String message) {
			if (notificationMrg != null) {
				notificationMrg.cancel(0);
			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
