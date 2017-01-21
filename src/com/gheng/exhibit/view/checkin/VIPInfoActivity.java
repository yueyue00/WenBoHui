package com.gheng.exhibit.view.checkin;

import java.util.ArrayList;
import java.util.List;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.InitData;
import com.gheng.exhibit.http.body.response.VipInfoBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.VipInfoViewPagerAdapter;
import com.gheng.exhibit.view.checkin.checkin.VipBaseInfoFragment;
import com.gheng.exhibit.view.checkin.checkin.VipScheduleFragment;
import com.hebg3.mxy.utils.VipInfoTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class VIPInfoActivity extends FragmentActivity implements OnCheckedChangeListener, OnClickListener {

	private ViewPager viewPager;
	private View view;
	private Button vip_ivBack;
	private TextView vip_title;
    private RadioGroup vip_rg ;
    private RadioButton vip_rb_baseinfo;
    private RadioButton vip_rb_schedule;
    
	private FragmentManager fm;
	private List<Fragment> fragments;
	private VipBaseInfoFragment baseInfoFragment;
	private VipScheduleFragment scheduleFragment;
	private String title = "";
	private Context mContext;
	
	
	public static String vIPIDString;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipinfo);
		mContext = this;
		fm = getSupportFragmentManager();
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		vIPIDString = intent.getStringExtra("vipid");
//		System.out.println("----------------------->vipid---"+vipid);
		initView();
//		setStatusBar();
		InitData();
		
	}
	/**
	 * 透明状态栏 
	 */
//	public void setStatusBar() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  
//            Window window = getWindow();  
//            // Translucent status bar  
//            window.setFlags(  
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
//        } else {
//			view.setVisibility(View.GONE);
//		}
//	}
	private void InitData() {
        fragments = new ArrayList<>();
        baseInfoFragment = new VipBaseInfoFragment();
        scheduleFragment = new VipScheduleFragment();
        fragments.clear();
        fragments.add(baseInfoFragment);
        fragments.add(scheduleFragment);
         
         
        VipInfoViewPagerAdapter adapter = new VipInfoViewPagerAdapter(fm, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);//默认显示基本信息
        vip_rb_baseinfo.setChecked(true);//默认选中基本信息
        vip_rb_baseinfo.setTextColor(Color.parseColor("#fed893"));
        
	}

	private void initView() {
//		view = findViewById(R.id.vipinfo_view);
		vip_ivBack = (Button) findViewById(R.id.but_fanhui);
		vip_ivBack.setOnClickListener(this);
		vip_title = (TextView) findViewById(R.id.in_title);
		vip_title.setText(title);
		vip_rg = (RadioGroup) findViewById(R.id.vipinfp_rg);
		vip_rb_baseinfo = (RadioButton) findViewById(R.id.vip_rb_baseInfo);
		vip_rb_schedule = (RadioButton) findViewById(R.id.vip_rb_schdule);
		vip_rg.setOnCheckedChangeListener(this);
		viewPager = (ViewPager) findViewById(R.id.vipinfo_viewpager);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				updateRadioButton(arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		
	}
	
	/**
	 * 更新radioButton状态
	 * @param index
	 */
	public void updateRadioButton(int index) {
		switch (index) {
		case 0:
            vip_rb_baseinfo.setChecked(true);
            vip_rb_baseinfo.setTextColor(Color.parseColor("#fed893"));
            vip_rb_schedule.setTextColor(Color.parseColor("#ffffff"));
			break;
		case 1:
           vip_rb_schedule.setChecked(true);
           vip_rb_schedule.setTextColor(Color.parseColor("#fed893"));
           vip_rb_baseinfo.setTextColor(Color.parseColor("#ffffff"));
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.vip_rb_baseInfo:
			viewPager.setCurrentItem(0);
			break;
		case R.id.vip_rb_schdule:
			viewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
       switch (v.getId()) {
	case R.id.but_fanhui:
		finish();
		break;

	default:
		break;
	}		
	}

	
}
