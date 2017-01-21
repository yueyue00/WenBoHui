package com.gheng.exhibit.rongyun.activity;

import io.rong.imkit.tools.PhotoFragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.smartdot.wenbo.huiyi.R;

/**
 * Created by DragonJ on 15/4/13.
 */
public class PhotoActivity extends FragmentActivity {
	PhotoFragment mPhotoFragment;
	Uri mUri;
	Uri mDownloaded;
	TextView title_tv;
	Button back_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zactivity_rong_photo);
		title_tv = (TextView) findViewById(R.id.title_tv);
		back_img = (Button) findViewById(R.id.back_img);

		mPhotoFragment = (PhotoFragment) getSupportFragmentManager()
				.findFragmentById(R.id.photo_fragment);
		Uri uri = getIntent().getParcelableExtra("photo");
		Uri thumbUri = getIntent().getParcelableExtra("thumbnail");

		mUri = uri;
		if (uri != null)
			mPhotoFragment.initPhoto(uri, thumbUri,
					new PhotoFragment.PhotoDownloadListener() {
						@Override
						public void onDownloaded(Uri uri) {
							mDownloaded = uri;
						}

						@Override
						public void onDownloadError() {

						}
					});
		process();
	}

	private void process() {
		title_tv.setText("图片");
		back_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
