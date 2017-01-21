package com.gheng.exhibit.view.checkin.checkin;

import java.util.ArrayList;
import java.util.List;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.VipSchdule.GroupcontentBean;
import com.gheng.exhibit.http.body.response.VipSchduleBean.InfoBean.JourneyBean;
import com.gheng.exhibit.http.body.response.VipXingChengGuest;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.ScheduleGridViewAdapter;
import com.gheng.exhibit.widget.CustomGridView;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class XingChengDetailActivity extends BaseActivity implements OnClickListener {

	 private TextView tvTitle;
	   private View schedule_view ;
	   private ImageButton iv_back ;
	   private TextView tv_title ;
	   private TextView tv_date ;
	   private TextView tv_location;
	   private TextView tv_host ;
	   private TextView tv_introduce ;
	   private CustomGridView gridView;
	   
	   private List<String> gridList;
	   private ScheduleGridViewAdapter adapter;
	   private Context mContext;
	   private 	VipXingChengGuest vipXingChengGuest;
	   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xing_cheng_detail);
		mContext = this;
		Intent intent = getIntent(); 
		Bundle bundle = intent.getExtras();
		vipXingChengGuest = (VipXingChengGuest) bundle.getSerializable("journey");
		initView();
		initGridData();
	}
	/**
	 *  初始化gridview的数据
	 */
	private void initGridData() {
        gridList = new ArrayList<>();
        gridList.add("erty");
        gridList.add("vn");
        gridList.add("hjk");
        gridList.add("wertrtyu");
        gridList.add("asdfghj");
        gridList.add("xcvbnm,");
        gridList.add("dfgh");
        gridList.add("ghjk");
        gridList.add("hj");
        gridList.add("ghhjk");
        
        adapter = new ScheduleGridViewAdapter(mContext, gridList);
        gridView.setAdapter(adapter);
	}


	private void initView() {

		 tvTitle = (TextView) findViewById(R.id.schedule_titletv);
		 tvTitle.setText("会议详情");
		 schedule_view = findViewById(R.id.schedule_view);
		 iv_back = (ImageButton) findViewById(R.id.schedule_goback);
		 iv_back.setOnClickListener(this);
		 tv_title = (TextView) findViewById(R.id.schedule_tv_title);
		 tv_title.setText(vipXingChengGuest.title);
		 tv_date =(TextView) findViewById(R.id.schdule_tv_date);
		 tv_date.setText(" "+vipXingChengGuest.date+"  "+vipXingChengGuest.time);
		 tv_location  = (TextView) findViewById(R.id.schdule_tv_location);
		 tv_location.setText(vipXingChengGuest.location);
		 tv_host = (TextView) findViewById(R.id.schedule_tv_host);
		 tv_introduce = (TextView) findViewById(R.id.schedule_tv_introduce);
		 tv_introduce.setText(vipXingChengGuest.description);
		 gridView = (CustomGridView) findViewById(R.id.schedule_gridview);
	}


	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
     switch (v.getId()) {
	case R.id.schedule_goback:
		finish();
		break;

	default:
		break;
	}		
	}
}
