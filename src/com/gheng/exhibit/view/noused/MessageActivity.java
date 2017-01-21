package com.gheng.exhibit.view.noused;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * Message消息
 * 
 * @author zhaofangfang
 * 
 */
public class MessageActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.webView1)
	private WebView webView;
	private String web_page = "";
	private String id = "";
	
	private Boolean isActive = true;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				toastNetError();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_howto);
		registerBoradcastReceiver();//注册广播
	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void init() {
		System.out.println(1);
		web_page = getIntent().getExtras().getString("web_page");
		titleBar.setText(getIntent().getStringExtra("title"));
		System.out.println(titleBar);
		id = getIntent().getExtras().getString("id");
		titleBar.setOnClickListener(this);
		titleBar.showRightImage(false);

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.indexOf("tel:") >= 0) {// 页面上有数字会导致连接电话
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_CALL, uri);
					startActivity(intent);

				} else if (url.indexOf("mailto:") >= 0) {
					Intent data = new Intent(Intent.ACTION_SENDTO);
					data.setData(Uri.parse(url));
					data.putExtra(Intent.EXTRA_SUBJECT, "");
					data.putExtra(Intent.EXTRA_TEXT, "");
					startActivity(data);
				} else {
					view.loadUrl(url);
				}

				return true;

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				ProgressTools.hide();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				view.setVisibility(View.GONE);
				handler.sendEmptyMessage(1);
			}
		});
		ProgressTools.showDialog(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setDisplayZoomControls(true);// 设定缩放控件隐藏

		int languageType = SharedData.getInt("i18n", Language.ZH);
		String url = Constant.SERVER_URL;
		switch (languageType) {
		case Language.ZH:
			url += "/services/" + SharedData.getBatchId() + "/" + web_page
					+ "_zh.html";
			break;
		case Language.EN:
			url += "/services/" + SharedData.getBatchId() + "/" + web_page
					+ "_en.html";
			break;
		}
		url = url + "?snum=" + SharedData.getString("snum") + "&loginName="
				+ SharedData.getUser() + "&id=" + id;
		System.out.println(url);
		webView.loadUrl(url);
	}

	@Override
	protected void setI18nValue() {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void clickLeftImage() {
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		view.removeAllViews();
		finish();
	}

	@Override
	public void clickRightImage() {
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
