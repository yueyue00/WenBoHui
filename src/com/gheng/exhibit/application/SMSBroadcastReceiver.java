package com.gheng.exhibit.application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * 短信监听
 * 
 * @author
 * 
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

	private static MessageListener mMessageListener;
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	public SMSBroadcastReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("ddddddddddddddddddddddddd");

		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				// 短信内容
				String content = smsMessage.getDisplayMessageBody();

				// 这里我是要获取自己短信服务号码中的验证码~~
				Pattern pattern = Pattern.compile("[a-zA-Z0-9]{4}");
				Matcher matcher = pattern.matcher(content);// String
															// body="测试验证码2346ds";
				if (matcher.find()) {
					String res = matcher.group().substring(0, 4);// 获取短信的内容
					System.out.println(res);
					// 过滤不需要读取的短信的发送号码
					mMessageListener.onReceived(res);
					abortBroadcast();
				}

			}
		}

	}

	// 回调接口
	public interface MessageListener {
		public void onReceived(String message);
	}

	public void setOnReceivedMessageListener(MessageListener messageListener) {
		this.mMessageListener = messageListener;
	}
}
