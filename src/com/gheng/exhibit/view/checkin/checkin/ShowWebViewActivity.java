package com.gheng.exhibit.view.checkin.checkin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class ShowWebViewActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.in_title)
	TextView titletv;
	@ViewInject(R.id.webview)
	WebView wv;
	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;
	@ViewInject(R.id.myProgressBar)
	ProgressBar bar;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showwebview);

		but_fanhui.setOnClickListener(this);

		String title = "通知详情";
		String url = getIntent().getStringExtra("url");
		System.out.println("aaa:ShowWebViewActivity:url=" + url);
		if (getIntent().getStringExtra("title") != null
				&& !"".equals(getIntent().getStringExtra("title"))) {
			title = getIntent().getStringExtra("title");
		}
		System.out.println("aaa:ShowWebViewActivity:title=" + title);

		titletv.setText(getLanguageString(title));

		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setSupportZoom(true);

		wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setLoadWithOverviewMode(true);

		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setDisplayZoomControls(false);
		wv.setDownloadListener(new MyWebViewDownLoadListener());
		wv.setWebViewClient(new WebViewClient() {
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
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				view.setVisibility(View.GONE);
			}
		});
		wv.setWebChromeClient(new WebChromeClient() {// 为webview添加进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					bar.setVisibility(View.INVISIBLE);
				} else {
					if (View.INVISIBLE == bar.getVisibility()) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
					System.out.println("newProgress      " + newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

		});
		// wv.setWebViewClient(new WebViewClient());
		wv.loadUrl(url);

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
			wv.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		} else {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == but_fanhui) {
			if (wv.canGoBack()) {
				wv.goBack(); // goBack()表示返回WebView的上一页面
			} else {
				finish();
			}
		}
	}

	/**
	 * 给webview添加downloadlistener 调用downloadManager下载apk文件
	 */
	class MyWebViewDownLoadListener implements DownloadListener {
		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		wv.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		wv.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("aaa:ShowWebViewActivity:finish");
		super.onDestroy();
		wv.destroy();
	}

}
