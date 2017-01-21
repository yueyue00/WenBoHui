package com.gheng.exhibit.view.noused;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdot.wenbo.huiyi.R;

public class PopupWindowActivity extends Activity implements OnClickListener,
		OnTouchListener {

	TextView english;
	TextView tw;

	RelativeLayout languagechoselayout;
	Sanjiaoxing view;
	Point sanjiaoxingleft;
	Point sanjiaoxingright;
	Point sanjiaoxingdown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test);

		english = (TextView) findViewById(R.id.english);
		tw = (TextView) findViewById(R.id.tw);

		english.setOnClickListener(this);
		tw.setOnClickListener(this);

		languagechoselayout = (RelativeLayout) findViewById(R.id.languagechoselayout);

		view = new Sanjiaoxing(this);
		view.setMinimumHeight(LayoutParams.MATCH_PARENT);
		view.setMinimumWidth(LayoutParams.MATCH_PARENT);
		// 通知view组件重绘
		view.invalidate();
		view.setOnTouchListener(this);
		languagechoselayout.addView(view);

	}

	class Sanjiaoxing extends View {

		Paint sanjiaoxing = new Paint();
		Paint sanjiaoxingtext = new Paint();

		public Sanjiaoxing(Context context) {
			super(context);
			sanjiaoxing.setAntiAlias(true);
			sanjiaoxing.setColor(context.getResources().getColor(
					R.color.zhongyaohuiyi));

			sanjiaoxingtext.setAntiAlias(true);
			sanjiaoxingtext.setColor(context.getResources().getColor(
					R.color.black));
			sanjiaoxingtext.setTextSize(dip2px(PopupWindowActivity.this, 16));
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// 绘制三角形
			Path path = new Path();
			sanjiaoxingleft = new Point(0, 0);
			path.moveTo(0, 0);// 此点为多边形的起点
			sanjiaoxingright = new Point(canvas.getWidth(), 0);
			path.lineTo(canvas.getWidth(), 0);
			sanjiaoxingdown = new Point(canvas.getWidth() / 2,
					canvas.getHeight());
			path.lineTo(canvas.getWidth() / 2, canvas.getHeight());
			path.close(); // 使这些点构成封闭的多边形
			canvas.drawPath(path, sanjiaoxing);
			canvas.drawText("简体中文",
					(canvas.getWidth() / 3) + dip2px(PopupWindowActivity.this, 10),
					(canvas.getHeight() / 2) - dip2px(PopupWindowActivity.this, 10),
					sanjiaoxingtext);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == english) {
			Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
		}
		if (v == tw) {
			Toast.makeText(this, "繁體中文", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Activity context, float dipValue) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dipValue, metrics);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();

		Point usertouch = new Point((int) x, (int) y);

		// 判断用户点击的坐标是否在三角形内

		if ((((usertouch.y - sanjiaoxingleft.y) * (sanjiaoxingright.x - sanjiaoxingleft.x)) - ((sanjiaoxingright.y - sanjiaoxingleft.y) * (usertouch.x - sanjiaoxingleft.x))) > 0
				&& (((usertouch.y - sanjiaoxingright.y) * (sanjiaoxingdown.x - sanjiaoxingright.x)) - ((sanjiaoxingdown.y - sanjiaoxingright.y) * (usertouch.x - sanjiaoxingright.x))) > 0
				&& (((usertouch.y - sanjiaoxingdown.y) * (sanjiaoxingleft.x - sanjiaoxingdown.x)) - ((sanjiaoxingleft.y - sanjiaoxingdown.y) * (usertouch.x - sanjiaoxingdown.x))) > 0) {
			Toast.makeText(this, "简体中文", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return false;
		}
	}
}
