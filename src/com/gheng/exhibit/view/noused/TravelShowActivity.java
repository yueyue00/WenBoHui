package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 如何到达
 * 
 * @author lileixing
 */
public class TravelShowActivity extends BaseActivity implements OnClickListener
{

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

//	@ViewInject(R.id.webView1)
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_travel_howto);
	}

	@Override
	protected void init()
	{
		titleBar.setText(getIntent().getStringExtra("title"));
		titleBar.setOnClickListener(this);

		webView.setWebViewClient(new WebViewClient()
		{
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
		});
		webView.loadUrl("http://www.baidu.com");
	}

	@Override
	public void clickLeftImage()
	{
		finish();
	}

	@Override
	public void clickRightImage()
	{

	}

	@Override
	protected void setI18nValue()
	{
		// TODO Auto-generated method stub

	}

}
