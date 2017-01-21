package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.widget.ImageView;

import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *	展位图片预览
 * @author lileixing
 */
public class ExhibitImageActivity extends BaseActivity implements OnClickListener{
	
	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	@ViewInject(R.id.iv)
	private ImageView iv;
	
	private int pchi;
	
	private String imageUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhibit_image);
	}

	@Override
	protected void init() {
		ProgressTools.showDialog(context);
		pchi = getIntent().getIntExtra("pchi", 2015);
		titleBar.setText(getIntent().getStringExtra("title"));
		titleBar.setOnClickListener(this);
		
		imageUrl = getIntent().getStringExtra("imageurl");
		
		bitmapUtils.display(iv, AppTools.imageChange(imageUrl));
	}
	
	@Override
	protected void setI18nValue() {
		
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {
		
	}

}
