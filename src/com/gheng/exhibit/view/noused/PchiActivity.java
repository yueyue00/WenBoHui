package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.PchiAdapter;
import com.gheng.exhibit.view.support.PchiData;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * pchi2015和2016页面
 * 
 * @author lileixing
 */
public class PchiActivity extends BaseActivity implements OnItemClickListener,
		OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	private Boolean isActive = true;

	@ViewInject(R.id.lv)
	private ListView lv;

	@ViewInject(R.id.iv)
	private ImageView iv;
	@ViewInject(R.id.iv2)
	private ImageView iv2;

	private PchiAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pchi);
		registerBoradcastReceiver();//注册广播
	}

	@Override
	protected void init() {
		titleBar.setText(getLanguageString("会议日程"));
		adapter = new PchiAdapter(this);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(this);
		titleBar.setOnClickListener(this);

		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_MAIN, 0,
				Constant.TYPE_SCHEDULE, Constant.SERACH_TYPE_ENTER, null);

	}

	@Override
	protected void setI18nValue() {

		adapter.setData(getListValues());
	}

	private List<PchiData> getListValues() {
		List<PchiData> list = new ArrayList<PchiData>();
		list.add(new PchiData(getLanguageString("会议日程"), R.drawable.schedule));
		list.add(new PchiData(getLanguageString("演讲者简介"), R.drawable.speaker));
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
		case 0: // 会议日程
			bd.putString("title", title);
			startTo(ScheduleListActivity.class, bd);
			break;
		case 1: // 商旅信息
			bd.putString("title", title);
			startTo(SpeakerListActivity.class, bd);
			break;
		}
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {

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
