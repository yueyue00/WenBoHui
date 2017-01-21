package com.gheng.exhibit.view.checkin.checkin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.a.a.ad;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.VipSchdule;
import com.gheng.exhibit.http.body.response.VipSchduleBean;
import com.gheng.exhibit.http.body.response.VipSchdule.GroupcontentBean;
import com.gheng.exhibit.http.body.response.VipSchduleBean.InfoBean.JourneyBean;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SchduleDetailActivity extends BaseActivity implements OnClickListener {
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
   private 	GroupcontentBean groupcontentBean;
   private JourneyBean journeyBean;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schdule_detail);
		mContext = this;
		Intent intent = getIntent(); 
		Bundle bundle = intent.getExtras();
//		groupcontentBean = (GroupcontentBean) bundle.getSerializable("journey");
		journeyBean = (JourneyBean) bundle.getSerializable("journey");
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
		 tv_title.setText(journeyBean.getTitle());
		 tv_date =(TextView) findViewById(R.id.schdule_tv_date);
		 tv_date.setText(" "+journeyBean.getDate()+"  "+journeyBean.getTime());
		 tv_location  = (TextView) findViewById(R.id.schdule_tv_location);
		 tv_location.setText(journeyBean.getLocation());
		 tv_host = (TextView) findViewById(R.id.schedule_tv_host);
		 tv_introduce = (TextView) findViewById(R.id.schedule_tv_introduce);
		 tv_introduce.setText(journeyBean.getDescription());
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
