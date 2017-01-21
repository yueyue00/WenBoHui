package com.hebg3.wl.push;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.FWMainActivity;
import com.gheng.exhibit.view.MainActivity;
import com.gheng.exhibit.view.SiXinActivity;
import com.gheng.exhibit.view.checkin.HuiYiXiangQingActivity;
import com.gheng.exhibit.view.checkin.MineAttentionPchiDetailsActivity;
import com.gheng.exhibit.view.checkin.XingChengAnPaiActivity;
import com.gheng.exhibit.view.checkin.checkin.ShowWebViewActivity;
import com.gheng.exhibit.view.viplist.FuWuSiXingActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	public Context context;
	public int noticinumber = 10000;
	User user;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Bundle bundle = intent.getExtras();
		// 查找
		try {
			DbUtils db = DbUtils.create(context);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

			JSONObject customJson = null;
			String serviceid = null;
			String servicename = null;
			String content = null;
			try {
				customJson = new JSONObject(
						bundle.getString(JPushInterface.EXTRA_MESSAGE));
				System.out.println("customJson=======" + customJson);
				serviceid = customJson.getString("serviceid");// 解析返回的推送类型字段
				servicename = customJson.getString("servicename");
				content = customJson.getString("content");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("MainActivity.isOpenSiXin = "
					+ MainActivity.isOpenSiXin);
			//没有极光推送的私信功能所以不考虑
			if (MainActivity.isOpenSiXin || FWMainActivity.isOpenSiXin) {// 直接广播自定义消息
				Intent intent_sixin = new Intent();
				intent_sixin.setAction("私信接收");
				System.out.println("serviceid===" + serviceid
						+ "===servicename===" + servicename + "===content==="
						+ content);
				intent_sixin.putExtra("serviceid", serviceid);
				intent_sixin.putExtra("servicename", servicename);
				intent_sixin.putExtra("content", content);
				context.sendBroadcast(intent_sixin);
			} else {// 响应通知栏
					// 弹出通知栏 没有点击事件 因为页面覆盖切换是个问题； 并检查SharedPreferences数值
				NotificationManager nm = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification.Builder notice = new Notification.Builder(context);
				notice.setContentTitle(BaseActivity.getLanguageString("私信")
						+ ":");
				notice.setContentText(content);// 通知栏显示的内容
				notice.setTicker(content);// 私信内容 发送过来滚动的消息
				notice.setSmallIcon(R.drawable.ic_launcher);
				notice.setDefaults(Notification.DEFAULT_SOUND
						| Notification.DEFAULT_VIBRATE);
				notice.setWhen(System.currentTimeMillis());
				notice.setContentIntent(getPendingIntent(serviceid,
						servicename, content));
				notice.setAutoCancel(true);
				Notification shownotice = notice.getNotification();
				nm.notify(10086, shownotice);

			}
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			processCustomMessage(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			// 打开自定义的Activity
			// Intent i = new Intent(context, TestActivity.class);
			// i.putExtras(bundle);
			// //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
			// Intent.FLAG_ACTIVITY_CLEAR_TOP );
			// context.startActivity(i);
			// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
			String key = printBundle(bundle);
			String type = null;
			String keystr = bundle.getString(key);
			if (!TextUtils.isEmpty(keystr)) {
				JSONObject customJson = null;
				try {
					customJson = new JSONObject(keystr);
					System.out.println("aaa:MyReceiver:customJson"
							+ customJson.toString());
					type = customJson.getString("messagetype");// 解析返回的推送类型字段

					// 以下判断是通过messagetype判断的推送类型对不同的类型做不同的解析和跳转操作
					if (type.equals("0")) {// 解析 会议变更 / 会议召开中
											// 处判断推送类型的messagetype之外的json、跳转到相应的会议变更
											// / 会议召开界面

						String messageid = customJson.getString("messageid");
						// String serviceid = customJson.getString("serviceid");
						// String messageurl =
						// customJson.getString("messageurl");
						String suijima = customJson.getString("suijima");
						// 打开自定义的Activity
						Intent i = new Intent(context, HuiYiXiangQingActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra("pchi_id", messageid);
						i.putExtra("suijima", suijima);
						context.startActivity(i);
					} else if (type.equals("1")) {// 解析 私信
													// 除messagetype参数外的json、并跳转到私信界面
						String serviceid = customJson.getString("serviceid");
						String servicename = customJson
								.getString("servicename");
						Intent i = new Intent();
						if (user.getUserjuese().equals("1")) {
							i.setClass(context, SiXinActivity.class);
						} else {
							i.setClass(context, FuWuSiXingActivity.class);
						}
						// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra("serviceid", serviceid);
						i.putExtra("servicename", servicename);
						System.out.println("serviceid = " + serviceid);
						System.out.println("/n servicename = " + servicename);
						context.startActivity(i);
					} else if (type.equals("2")) {// 解析 vip行程 跳转贵宾行程传tripid
													// 除messagetype参数外的json、并跳转到vip行程界面
						String messageid = customJson.getString("messageid");
						Intent i = new Intent(context,
								XingChengAnPaiActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra("tripid", messageid);
						context.startActivity(i);

					} else if (type.equals("3")) {// 解析 其他类型 showwebview 传url地址
													// 除messagetype参数外的json、并跳转到相应界面
													// String messageid =
													// customJson.getString("messageid");
						// String serviceid = customJson.getString("serviceid");
						String messageurl = customJson.getString("messageurl");
						System.out.println("aaa:MyReceiver:messageurl"
								+ messageurl);
						// 打开自定义的Activity
						Intent i = new Intent(context,
								ShowWebViewActivity.class);
						i.putExtras(bundle);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra("url", messageurl);
						context.startActivity(i);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	public PendingIntent getPendingIntent(String serviceid, String servicename,
			String content) {
		Intent i = new Intent();
		if (user.getUserjuese().equals("1")) {
			i.setClass(context, SiXinActivity.class);
		} else {
			i.setClass(context, FuWuSiXingActivity.class);
		}
		i.putExtra("serviceid", serviceid);
		i.putExtra("servicename", servicename);
		i.putExtra("content", content);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				noticinumber++, i, PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				sb.append(key);
			}
		}
		System.out.println("aaa:MyReceiver:" + sb.toString());
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		} else if (FWMainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(
					FWMainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(FWMainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(FWMainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}
}
