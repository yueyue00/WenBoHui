package com.gheng.exhibit.view.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TouchImageView;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class ImageActivity extends BaseActivity {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.iv)
	private TouchImageView iv;
	
	private Bundle bd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
	}

	@Override
	protected void setI18nValue() {

	}

	@Override
	protected void init() {
		ProgressTools.showDialog(this);
		bd = getIntent().getExtras();
		titleBar.setText(bd.getString("title"));
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {

			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		iv.setMaxZoom(6);
		String url = Constant.SERVER_URL + "/" + SharedData.getBatchId() + "/" + bd.getString("title") + ".png";
		bitmapUtils.configDefaultLoadingImage(this.getResources().getDrawable(bd.getInt("resId")));
		//BitmapDisplayConfig displayConfig = new BitmapDisplayConfig();
		//displayConfig.setShowOriginal(true);
		//bitmapUtils.configDefaultDisplayConfig(displayConfig);
		bitmapUtils.display(iv, url, new CustomBitmapLoadCallBack());
		
	}

	class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView> {  
		@Override  
        public void onLoading(ImageView container, String uri,  
                BitmapDisplayConfig config, long total, long current) {
//			float t = total;
//			float c = current;
//			float n = c / t * 100;
//			Log.i("onLoading " + ((int) n) + "%", uri);
        }
  
        @Override  
        public void onLoadCompleted(ImageView container, String uri,  
                Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
        	Log.i("TAG", "completes");
        	ProgressTools.hide();
        	super.onLoadCompleted(container, uri, bitmap, config, from);
        }
  
        @Override  
        public void onLoadFailed(ImageView container, String uri,  
                Drawable drawable) {
        	iv.setImageResource(bd.getInt("resId"));
        	ProgressTools.hide();
        }  
	}
}
