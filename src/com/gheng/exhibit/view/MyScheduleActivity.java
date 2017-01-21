package com.gheng.exhibit.view;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import cn.jpush.a.a.z;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.ZhuanShuFuWu;
import com.gheng.exhibit.view.adapter.VipFuWuAdapter;
import com.gheng.exhibit.view.checkin.XingChengAnPaiActivity;
import com.gheng.exhibit.view.checkin.ZhanShangSJActivity;
import com.gheng.exhibit.view.checkin.checkin.JiuDianActivity;
import com.gheng.exhibit.view.checkin.checkin.JiuDianXinXiActivity;
import com.gheng.exhibit.view.checkin.checkin.VipZhuanShuFWActivity;
import com.gheng.exhibit.view.support.PchiData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的行程界面
 * 
 * @author lileixing
 */
public class MyScheduleActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {

	@ViewInject(R.id.in_title)
	private TextView titletv;
	@ViewInject(R.id.but_fanhui)
	private Button vipbut;

	@ViewInject(R.id.lv)
	private ListView lv;

	private Boolean isActive = true;

	private VipFuWuAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipfuwu);
		registerBoradcastReceiver();// 注册广播
	}

	@Override
	protected void init() {
		titletv.setText(getLanguageString("我的行程"));
		adapter = new VipFuWuAdapter(this);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(this);
		vipbut.setOnClickListener(this);
	}

	@Override
	protected void setI18nValue() {
		adapter.setData(getListValues());
	}

	private List<PchiData> getListValues() {
		List<PchiData> list = new ArrayList<PchiData>();
		list.add(new PchiData(getLanguageString("行程安排"),
				R.drawable.wuzhen_vipservice_guibinxingcheng));
		// list.add(new PchiData(getLanguageString("个人会议日程"),
		// R.drawable.wuzhen_vipservice_huiyiricheng));

		list.add(new PchiData(getLanguageString("酒店信息"),
				R.drawable.wuzhen_vipservice_hotelinfo));
		list.add(new PchiData(getLanguageString("专属服务"),
				R.drawable.wuzhen_vipservice_zhuanshuservice));
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String title = adapter.getItem(position).name;
		processList(position, title);
	}

	private void processList(int type, String title) {
		Bundle bd = new Bundle();
		switch (type) {
		case 0: // 行程安排
			bd.putString("title", title);
			startTo(XingChengAnPaiActivity.class, bd);
			break;
		// case 1: // 会议日程
		// bd.putString("title", title);
		// bd.putInt("isricheng", 2);
		// startTo(HuiYiRiChengActivity.class, bd);
		// break;
		// case 2: // 宴会餐饮
		// bd.putString("title", title);
		// // startTo(SpeakerListActivity.class, bd);
		// startTo(YanHuiCanYinActivity.class, bd);
		// break;
		case 1: // 酒店信息
			bd.putString("title", title);
			startTo(JiuDianActivity.class, bd);
			break;
		case 2: // 专属服务
//			bd.putString("title", title);
//			startTo(VipZhuanShuFWActivity.class, bd);
			Intent zhuanshu=new Intent(MyScheduleActivity.this,ZhanShangSJActivity.class);
			zhuanshu.putExtra("title", "专属服务");
			startActivity(zhuanshu);
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == vipbut.getId()) {// 推出vip服务界面
			MyScheduleActivity.this.finish();
		}
	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if (mBroadcastReceiver != null) {
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
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
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
