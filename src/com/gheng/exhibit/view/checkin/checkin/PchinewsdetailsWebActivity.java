package com.gheng.exhibit.view.checkin.checkin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

//import cn.sharesdk.onekeyshare.SharePopupWindow;

/**
 * 会议新闻列表点击之后跳转到的会议新闻详情页面
 * 
 * 在这个页面上要实现TitleBar上面分享按钮的分享功能
 * 
 * anthor:renzhihua
 * 
 */

public class PchinewsdetailsWebActivity extends BaseActivity implements
		OnClickListener, Callback {

	/**
	 * 返回键按钮
	 **/
	@ViewInject(R.id.but_fanhui)
	Button vipbut;
	// 会议新闻详情页面的WebView
	@ViewInject(R.id.pchinewsdetails_web)
	WebView pchinewsdetails_web;
	// 分享按钮
	@ViewInject(R.id.sharebutton)
	Button sharebutton;
	/**
	 * 会议新闻详情标题
	 */
	@ViewInject(R.id.in_title)
	TextView in_title;

	@ViewInject(R.id.titlelayout)
	RelativeLayout titlelayout;

	/**
	 * 点击分享按钮后，实现分享页面的显示
	 * 
	 * anthor: renzhihua
	 */
	private String text = "";
	private String imageurl = "";
	private String title = "";
	private String url = null;

	// private SharePopupWindow share;

	// private void showShare() {
	// ShareSDK.initSDK(this);
	// OnekeyShare oks = new OnekeyShare();
	// // 关闭sso授权
	// oks.disableSSOWhenAuthorize();
	//
	// // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
	// // oks.setNotification(R.drawable.ic_launcher,
	// // getString(R.string.app_name));
	// // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	// oks.setTitle(getString(R.string.share));
	// // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	// oks.setTitleUrl("http://sharesdk.cn");
	// // text是分享文本，所有平台都需要这个字段
	// oks.setText("我是分享文本");
	// //
	// // // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
	// // oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
	// // url仅在微信（包括好友和朋友圈）中使用
	// oks.setUrl("http://sharesdk.cn");
	// // comment是我对这条分享的评论，仅在人人网和QQ空间使用
	// oks.setComment("我是测试评论文本");
	// // site是分享此内容的网站名称，仅在QQ空间使用
	// oks.setSite(getString(R.string.app_name));
	// // siteUrl是分享此内容的网站地址，仅在QQ空间使用
	// oks.setSiteUrl("http://sharesdk.cn");
	//
	// // 启动分享GUI
	// oks.show(this);
	// }
	/**
	 * 调出分享窗口 里面填写的分享内容不能为空，当为空时会分享失败。
	 */
	private void showShare() {
		if (title == null || text == null || url == null) {
			Toast.makeText(context, "分享内容为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享标题
		oks.setTitle(title);
		// 分享正文（text是分享文本，所有平台都需要这个字段）
		oks.setText(text);
		// 分享的图标
//		oks.setImageUrl(imageurl);
		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl(url);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(url);
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				switch (platform.getName()) {
				case "ShortMessage":
				case "Email":
					paramsToShare.setText(title + ":" + text + url);
					paramsToShare.setImageUrl("");
					break;
				default:
					break;
				}
			}
		});
		// 启动分享GUI
		oks.show(this);
	}

	public void systemShare() {
		if (title == null || text == null || url == null) {
			Toast.makeText(context, "分享内容为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setType("image/jpg");
		intent.setType("text/*");
		intent.putExtra(Intent.EXTRA_STREAM, url);
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(intent, "来自文博会"));
	}

	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pchinewsdetails_web);
		vipbut.setOnClickListener(this);

		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

		// 获取新闻列表页面传递值
		Intent intent = getIntent();
		url = intent.getStringExtra("newsurl");
		imageurl = intent.getStringExtra("imageurl");
		title = intent.getStringExtra("title");
		text = intent.getStringExtra("content");

		// 分享按钮
//		sharebutton.setVisibility(View.VISIBLE);
		// 给分享按钮添加点事事件监听器
		sharebutton.setOnClickListener(this);// 分享功能被砍 马晓勇

		// ShareSDK.initSDK(this);//分享功能屏蔽

		// 显示webview
		pchinewsdetails_web.getSettings().setJavaScriptEnabled(true);
		// 设置网页自适应
		pchinewsdetails_web.getSettings().setUseWideViewPort(true);
		pchinewsdetails_web.getSettings().setLoadWithOverviewMode(true);
		// 支持变焦
		pchinewsdetails_web.getSettings().setSupportZoom(true);
		// 支持手动方法缩小
		pchinewsdetails_web.getSettings().setBuiltInZoomControls(true);
		pchinewsdetails_web.getSettings().setDisplayZoomControls(false);// 不要显示按钮，否则会崩溃

		pchinewsdetails_web.setWebViewClient(new WebViewClient() {
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
		pchinewsdetails_web.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					// 网页加载完成
					progressBar.setProgress(0);
					progressBar.setVisibility(View.GONE);
				} else {
					// 加载中
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress(newProgress);

				}
			}
		});
		pchinewsdetails_web.loadUrl(url);
	}



	@Override
	public boolean handleMessage(Message msg) {
		int what = msg.what;
		// if (what == 1) {
		// Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
		// }
		// if (share != null) {
		// share.dismiss();
		// }
		return false;
	}

	@Override
	protected void setI18nValue() {
		in_title.setText(getLanguageString("详情"));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1001) {
			System.out.println("发送成功");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == vipbut) {
			finish();
		}
		if (v == sharebutton) {
			showShare();
			// systemShare();
		}
		// if (v == sharebutton) {//分享功能被屏蔽
		// share = new SharePopupWindow(PchinewsdetailsWebActivity.this, this);
		// share.setPlatformActionListener(PchinewsdetailsWebActivity.this);
		// ShareModel model = new ShareModel();
		// model.setUrl(url);
		// model.setImageUrl(imageurl);
		// model.setText(text);
		// model.setTitle(title);
		//
		// share.initShareParams(model);
		// share.showShareWindow();
		// // 显示窗口 (设置layout在PopupWindow中显示的位置)
		// share.showAtLocation(PchinewsdetailsWebActivity.this.findViewById(R.id.share_main),
		// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		// }

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		pchinewsdetails_web.onResume();

		// if (share != null) {
		// share.dismiss();
		// }
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		pchinewsdetails_web.onPause();
	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存

		pchinewsdetails_web.destroy();

		// ShareSDK.stopSDK(this);
		MyApplication.remove(this);
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		/**
		 * 当横屏时自动隐藏状态栏，下拉出现 标题栏隐藏
		 * **/
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			titlelayout.setVisibility(View.GONE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {// 当竖屏时状态栏自动出现，标题栏显示
			titlelayout.setVisibility(View.VISIBLE);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}
}
