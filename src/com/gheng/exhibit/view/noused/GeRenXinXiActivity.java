package com.gheng.exhibit.view.noused;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class GeRenXinXiActivity extends BaseActivity implements OnClickListener{

	public Button goback;
	@ViewInject(R.id.titletv)
	TextView titletv;
	@ViewInject(R.id.nametv)
	TextView nametv;
	@ViewInject(R.id.sextv)
	TextView sextv;
	@ViewInject(R.id.guojitv)
	TextView guojitv;
	@ViewInject(R.id.zongjiaotv)
	TextView zongjiaotv;
	@ViewInject(R.id.leveltv)
	TextView leveltv;
	@ViewInject(R.id.zhiweitv)
	TextView zhiweitv;
	@ViewInject(R.id.fangjiantv)
	TextView fangjiantv;
	@ViewInject(R.id.suixingrenyuantv)
	TextView suixingrenyuantv;
	@ViewInject(R.id.gerenjieshaotv)
	TextView gerenjieshaotv;
	
	private Boolean isActive = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userinfo);
		registerBoradcastReceiver();//注册广播
		goback=(Button)findViewById(R.id.goback);
		goback.setOnClickListener(this);
		
	}
	
	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		titletv.setText(getLanguageString("个人信息"));
		nametv.setText(getLanguageString("姓名"));
		sextv.setText(getLanguageString("性别"));
		guojitv.setText(getLanguageString("国籍"));
		zongjiaotv.setText(getLanguageString("宗教信仰"));
		leveltv.setText(getLanguageString("嘉宾级别"));
		zhiweitv.setText(getLanguageString("职位"));
		fangjiantv.setText(getLanguageString("房间号"));
		suixingrenyuantv.setText(getLanguageString("随行人员"));
		gerenjieshaotv.setText(getLanguageString("个人介绍"));
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==goback){
			this.finish();
		}
	}
	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if(mBroadcastReceiver!=null){
			this.unregisterReceiver(mBroadcastReceiver);
		}
		MyApplication.remove(this);
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
		    if(Intent.ACTION_SCREEN_OFF.equals(action)){
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

		if (!isActive) {
			TimeEnd = System.currentTimeMillis() / 1000;
			Integer time = getResources().getInteger(R.integer.time);
			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
				BaseActivity.loginValidation(context);
			}
		}
	}
}
