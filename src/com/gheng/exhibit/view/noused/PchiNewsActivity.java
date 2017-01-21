package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.ProductType;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.MyViewPapgerAdapter;
import com.gheng.exhibit.view.adapter.PchiNewsAdapter;
import com.gheng.exhibit.widget.MyViewPager;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 会议新闻页面
 * 
 * @author renzhihua
 */
public class PchiNewsActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener, android.view.View.OnClickListener {
	
	private Boolean isActive = true;
	
	/**
	 * 设置大会详情标题
	 **/
	@ViewInject(R.id.in_title)
	TextView titletv;

	/**
	 * 大会详情返回键按钮
	 **/
	@ViewInject(R.id.but_fanhui)
	Button vipbut;

	@ViewInject(R.id.radio_group)
	private RadioGroup radio_group;

	@ViewInject(R.id.lv)
	private ListView lv;

	@ViewInject(R.id.pchinews_vp)
	private MyViewPager pchinews_vp;

	private MyViewPapgerAdapter adapter;

	private ViewHolder holder1 = new ViewHolder();

	private ViewHolder holder2 = new ViewHolder();

	private ViewHolder holder3 = new ViewHolder();

	private ViewHolder holder4 = new ViewHolder();

	private List<ProductType> root = new ArrayList<ProductType>();

	@ViewInject(R.id.rbtn_pchinews_news)
	private RadioButton rbtn_pchinews_news;

	@ViewInject(R.id.rbtn_pchinews_press)
	private RadioButton rbtn_pchinews_press;

	@ViewInject(R.id.rbtn_pchinews_exhibition)
	private RadioButton rbtn_pchinews_exhibition;

	@ViewInject(R.id.rbtn_pchinews_activity)
	private RadioButton rbtn_pchinews_activity;

	@ViewInject(R.id.rbtn_other)
	private RadioButton rbtn_other;

	@ViewInject(R.id.tab_group)
	private LinearLayout tab_group;

	@ViewInject(R.id.iv_first)
	View iv_first;
	@ViewInject(R.id.iv_second)
	ImageView iv_second;
	@ViewInject(R.id.iv_third)
	ImageView iv_third;
	@ViewInject(R.id.iv_four)
	ImageView iv_four;

	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pchi_news);
		registerBoradcastReceiver();//注册广播
		radio_group.setOnCheckedChangeListener(this);

		vipbut.setOnClickListener(this);

	};

	@Override
	protected void setI18nValue() {
		String surname = SharedData.getString(SharedData.SURNAME);
		String name = SharedData.getString(SharedData.NAME);
		int lg = SharedData.getInt("i18n", Language.ZH);
		if (lg == Language.ZH) {
			name = surname + name;
		} else {
			name += " " + surname;
		}
		titletv.setText(getLanguageString("会议新闻"));
	}

	@Override
	public void clickLeftImage() {
		finish();

	}

	@Override
	public void clickRightImage() {

	}

	@Override
	protected void init() {

		List<View> lists = new ArrayList<View>();
		View v1 = getLayoutInflater().inflate(R.layout.item_pchinews_relist,
				null);
		View v2 = getLayoutInflater().inflate(R.layout.item_pchinews_relist,
				null);
		View v3 = getLayoutInflater().inflate(R.layout.item_pchinews_relist,
				null);
		View v4 = getLayoutInflater().inflate(R.layout.item_pchinews_relist,
				null);
		lists.add(v1);
		lists.add(v2);
		lists.add(v3);
		lists.add(v4);

		holder1.lv = (ListView) v1;
		holder1.adapter = new PchiNewsAdapter(this);
		holder1.lv.setAdapter(holder1.adapter);

		holder2.lv = (ListView) v2;
		holder2.adapter = new PchiNewsAdapter(this);
		holder2.lv.setAdapter(holder2.adapter);

		holder3.lv = (ListView) v3;
		holder3.adapter = new PchiNewsAdapter(this);
		holder3.lv.setAdapter(holder3.adapter);

		holder4.lv = (ListView) v4;
		holder4.adapter = new PchiNewsAdapter(this);
		holder4.lv.setAdapter(holder4.adapter);

		adapter = new MyViewPapgerAdapter(lists);
		pchinews_vp.setAdapter(adapter);
		pchinews_vp.setOffscreenPageLimit(4);

	}

	class ViewHolder {
		ListView lv;
		PchiNewsAdapter adapter;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_pchinews_news:
			pchinews_vp.setCurrentItem(0, false);
			iv_first.setVisibility(View.VISIBLE);
			iv_second.setVisibility(View.INVISIBLE);
			iv_third.setVisibility(View.INVISIBLE);
			iv_four.setVisibility(View.INVISIBLE);
			break;
		case R.id.rbtn_pchinews_press:
			pchinews_vp.setCurrentItem(1, false);
			iv_first.setVisibility(View.INVISIBLE);
			iv_second.setVisibility(View.VISIBLE);
			iv_third.setVisibility(View.INVISIBLE);
			iv_four.setVisibility(View.INVISIBLE);
			break;
		case R.id.rbtn_pchinews_exhibition:
			pchinews_vp.setCurrentItem(2, false);
			iv_first.setVisibility(View.INVISIBLE);
			iv_second.setVisibility(View.INVISIBLE);
			iv_third.setVisibility(View.VISIBLE);
			iv_four.setVisibility(View.INVISIBLE);
			break;
		case R.id.rbtn_pchinews_activity:
			pchinews_vp.setCurrentItem(3, false);
			iv_first.setVisibility(View.INVISIBLE);
			iv_second.setVisibility(View.INVISIBLE);
			iv_third.setVisibility(View.INVISIBLE);
			iv_four.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (vipbut.getId() == v.getId()) {
			PchiNewsActivity.this.finish();
		}
	}
	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if(mBroadcastReceiver!=null){
			this.unregisterReceiver(mBroadcastReceiver);
		}
		MyApplication.remove(this);
		super.onDestroy();
	}

	/** 记录前台切后台时间 */
	private Long TimeStart;
	/** 记录后台切前台时间 */
	private Long TimeEnd;
	/**
	 * 接收广播
	 **/
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
		    if(Intent.ACTION_SCREEN_OFF.equals(action)){
				if (BaseActivity.isAppOnForeground(context)) {
					isActive = false;
					TimeStart = System.currentTimeMillis() / 1000;
				}
			}
		}
	};
	/**
	 * 注册广播
	 **/
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);	
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	/**
	 * 挂起时调用的方法
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (!BaseActivity.isAppOnForeground(this)) {
			isActive = false;
			TimeStart = System.currentTimeMillis() / 1000;
		}
	}

	/**
	 * 唤醒时调用的方法
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!isActive) {
			TimeEnd = System.currentTimeMillis() / 1000;
			Integer time = getResources().getInteger(R.integer.time);
			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
				BaseActivity.loginValidation(context);
			}
		}
	}

}
