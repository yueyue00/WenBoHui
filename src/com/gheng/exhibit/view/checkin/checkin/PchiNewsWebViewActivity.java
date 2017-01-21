package com.gheng.exhibit.view.checkin.checkin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.utils.WebViewLogInterceptor;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.MainActivity;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 大会新闻页面-----使用WebView来展现的形式
 * 
 * @author renzhihua
 */
public class PchiNewsWebViewActivity extends BaseActivity
		implements OnClickListener, android.view.View.OnClickListener {

	private Boolean isActive = true;
	
	/**
	 * 设置标题
	 **/
	@ViewInject(R.id.in_title)
	TextView titletv;

	/**
	 * 返回键按钮
	 **/
	@ViewInject(R.id.but_fanhui)
	Button vipbut;
	/**
	 * 分享按钮
	 **/
	@ViewInject(R.id.sharebutton)
	Button sharebutton;

	@ViewInject(R.id.webView1)
	private WebView webView;
	private String web_page = "";

	private WebViewLogInterceptor interceptor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pchinews_weblist);
		registerBoradcastReceiver();//注册广播
		vipbut.setOnClickListener(this);
		sharebutton.setOnClickListener(this);
		
		interceptor = new WebViewLogInterceptor();
		interceptor.logToServer(getIntent().getExtras());

	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void init() {
		web_page = getIntent().getExtras().getString("web_page");

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
					Intent intent = new Intent(PchiNewsWebViewActivity.this,PchinewsdetailsWebActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
					
				}

				return true;

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				ProgressTools.hide();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				view.setVisibility(View.GONE);
				/* handler.sendEmptyMessage(1); */
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
			url += "/services/" + SharedData.getBatchId() + "/" + web_page + "_zh.html";
			break;
		case Language.EN:
			url += "/services/" + SharedData.getBatchId() + "/" + web_page + "_en.html";
			break;
		}
		url = changeUrl(url);
		System.out.println(url);
		String imageurl = getIntent().getExtras().getString("imageurl");
		if (!"".equals(imageurl) && null != imageurl) {
			url = Constant.SERVER_URL + imageurl;
			WebSettings ws = webView.getSettings();
			ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			webView.setInitialScale(1);
		}
		String web_url = getIntent().getExtras().getString("web_url");
		if (StringTools.isNotBlank(web_url)) {
			url = web_url;
			url = changeUrl(url);
		}
		webView.loadUrl(url);
	}

	private String changeUrl(String url) {
		if (url.contains("?")) {
			url += "&";
		} else {
			url += "?";
		}
		url += "snum=" + SharedData.getString("snum") + "&uname=" + SharedData.getUser();
		return url;
	}

	@Override
	public void clickLeftImage() {

	}

	@Override
	public void clickRightImage() {

	}

	@Override
	protected void setI18nValue() {
		titletv.setText(getLanguageString("会议新闻"));
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
	public void onClick(View v) {
		if (v == vipbut) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
		}
		if (v == sharebutton) {
			startTo(MainActivity.class);
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
