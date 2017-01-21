package com.gheng.exhibit.view.noused;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.utils.WebViewLogInterceptor;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.MainActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 如何到达
 * 
 * @author lileixing
 */
public class WebViewActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
    /**
     * 添加会议新闻图片轮播View
     * 
     * @author renzhihua
     */
	
	@ViewInject(R.id.webView1)
	private WebView webView;
	private String web_page = "";

	private WebViewLogInterceptor interceptor;
	
	/*private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				toastNetError();
				break;
			}
		};
	};*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_travel_howto);
		setContentView(R.layout.activity_exhibition_news_list);
		interceptor = new WebViewLogInterceptor();
		interceptor.logToServer(getIntent().getExtras());
	}



	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void init() {
		web_page = getIntent().getExtras().getString("web_page");
		titleBar.setText(getIntent().getStringExtra("title"));
		titleBar.setOnClickListener(this);
		if ("traffic".equals(web_page)) {
			Drawable drawable = this.getResources().getDrawable(
					R.drawable.track_collect_running);
			titleBar.setRightDrawable(drawable);
			titleBar.showRightImage(true);
		} else {
			Drawable drawable = this.getResources().getDrawable(
					R.drawable.home);
			titleBar.setRightDrawable(drawable);
			titleBar.showRightImage(true);
		}

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
				/*handler.sendEmptyMessage(1);*/
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
			url += "/services/" +SharedData.getBatchId()+ "/" + web_page + "_en.html";
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
	
	private String changeUrl(String url){
		if(url.contains("?")){
			url += "&";
		}else{
			url += "?";
		}
		url += "snum=" + SharedData.getString("snum") + "&uname="
				+ SharedData.getUser();
		return url;
	}

	@Override
	public void clickLeftImage() {
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		view.removeAllViews();
		finish();
	}

	@Override
	public void clickRightImage() {
		if ("traffic".equals(web_page)) {
			
		} else {
			startTo(MainActivity.class);
		}
	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		
		/*//会议新闻页面加的轮播页面 ，renzhihua添加
		mainAdapter.setData(getValues());
		vp.setAdapter(mainAdapter);*/

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
