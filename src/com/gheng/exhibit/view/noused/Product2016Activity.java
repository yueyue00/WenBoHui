package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class Product2016Activity extends BaseActivity {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.iv)
	private ImageView iv;

	@ViewInject(R.id.tv)
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_2016);
	}

	@Override
	protected void init() {
		bitmapUtils.display(iv,
				AppTools.imageChange(getIntent().getStringExtra("imageurl")));
		titleBar.setText(getIntent().getStringExtra("title"));
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {

			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});
	}

	@Override
	protected void setI18nValue() {
		tv.setText(getLanguageString(10121));
	}

}
