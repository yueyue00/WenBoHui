package com.dtr.zxing.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import com.smartdot.wenbo.huiyi.R;

public class TestGeneratectivity extends Activity {
	private ImageView mEnglishIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_generate);

		initView();
		createQRCode();
	}

	private void initView() {
		mEnglishIv = (ImageView) findViewById(R.id.iv_english);
	}

	private void createQRCode() {
		createEnglishQRCode();
	}

	private void createEnglishQRCode() {
		/*
		 * 这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
		 * 请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考
		 * https://github.com/GeniusVJR
		 * /LearningNotes/blob/master/Part1/Android/Android
		 * %E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
		 */
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... params) {
				return QRCodeEncoder.syncEncodeQRCode("bingoogolapple",
						BGAQRCodeUtil.dp2px(TestGeneratectivity.this, 150),
						Color.parseColor("#ff0000"));
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				if (bitmap != null) {
					mEnglishIv.setImageBitmap(bitmap);
				} else {
					Toast.makeText(TestGeneratectivity.this, "生成英文二维码失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}

	public void decodeEnglish(View v) {
		mEnglishIv.setDrawingCacheEnabled(true);
		Bitmap bitmap = mEnglishIv.getDrawingCache();
		decode(bitmap, "解析英文二维码失败");
	}

	public void decodeIsbn(View v) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		decode(bitmap, "解析ISBN失败");
	}

	private void decode(final Bitmap bitmap, final String errorTip) {
		/*
		 * 这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
		 * 请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考
		 * https://github.com/GeniusVJR
		 * /LearningNotes/blob/master/Part1/Android/Android
		 * %E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
		 */
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				return QRCodeDecoder.syncDecodeQRCode(bitmap);
			}

			@Override
			protected void onPostExecute(String result) {
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(TestGeneratectivity.this, errorTip,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TestGeneratectivity.this, result,
							Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}
}
