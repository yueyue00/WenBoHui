package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.model.databases.ScheduleInfo;
import com.gheng.exhibit.model.databases.Speakers;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.SpeakerInfoAdapter;
import com.gheng.exhibit.widget.RoundImageView;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class SpeakerInfoActivity extends BaseActivity implements OnItemClickListener{

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.iv)
	private RoundImageView iv;

	@ViewInject(R.id.tv_name)
	private TextView tv_name;

	@ViewInject(R.id.tv_workplace)
	private TextView tv_workplace;

	@ViewInject(R.id.tv_office)
	private TextView tv_office;

	@ViewInject(R.id.tv_background)
	private TextView tv_background;

	@ViewInject(R.id.lv)
	private ListView lv;
	
	private Boolean isActive = true;
	
	//参与会议
	@ViewInject(R.id.tv_ralation_schedule)
	private TextView tv_ralation_schedule;
	
	private SpeakerInfoAdapter adapter;
	
	@ViewInject(R.id.v_line)
	private View v_line;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speaker_info);
		registerBoradcastReceiver();//注册广播
	}

	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString("专家简介"));
		tv_ralation_schedule.setText(getLanguageString("参与会议"));
	}

	@Override
	protected void init() {
		sendLog();
		adapter = new SpeakerInfoAdapter(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
//				UIUtils.goHome();
			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		loadData();
		loadListData();
	}

	private void loadData() {
		long id = getIntent().getLongExtra("id", 0);
		Speakers model = null;;
		try {
			model = getDbUtils().findById(Speakers.class,id);
		} catch (DbException e) {
			e.printStackTrace();
		}
		if(model != null){
			bitmapUtils.display(iv, AppTools.imageChange((String)I18NUtils.getValue(model, "logo")));
			tv_name.setText((String)I18NUtils.getValue(model, "name"));
			tv_office.setText((String)I18NUtils.getValue(model, "office"));
			if(StringTools.isBlank(tv_office.getText().toString())){
				tv_office.setVisibility(View.GONE);
			}
			tv_workplace.setText((String)I18NUtils.getValue(model, "workplace"));
			if(StringTools.isBlank(tv_workplace.getText().toString())){
				tv_workplace.setVisibility(View.GONE);
			}
			tv_background.setText((String)I18NUtils.getValue(model, "remark"));
			if(StringTools.isBlank(tv_background.getText().toString())){
				tv_background.setVisibility(View.GONE);
				v_line.setVisibility(View.GONE);
			}
		}
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			List<Schedule> list = (List<Schedule>) msg.obj;
			adapter.setData(list);
		};
	};
	
	
	private void loadListData(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<ScheduleInfo> list = new ArrayList<ScheduleInfo>();
				Selector selector = Selector.from(ScheduleInfo.class);
				ApiUtil.changeSelector(selector);
				selector.and("speakerid", "<>", null);
				selector.and("speakerid", "<>", "");
				long id = getIntent().getLongExtra("id", 0);
				try {
					List<ScheduleInfo> findAll = getDbUtils().findAll(selector);
					for (ScheduleInfo scheduleInfo : findAll) {
						if(StringTools.isBlank(scheduleInfo.getSpeakerid())){
							continue;
						}
						String[] split = scheduleInfo.getSpeakerid().split(",");
						for (String idStr : split) {
							if(StringTools.isNotBlank(idStr) && Long.valueOf(idStr) == id){
								list.add(scheduleInfo);
								break;
							}
						}
					}
					List<Schedule> result = new ArrayList<Schedule>();
					if(list.size() > 0){
						selector = Selector.from(Schedule.class);
						ApiUtil.changeSelector(selector);
						List<Schedule> schedules = getDbUtils().findAll(selector);
						for (Schedule schedule : schedules) {
							for (ScheduleInfo info : list) {
								if(schedule.getId() == info.getScheduleid()){
									result.add(schedule);
									break;
								}
							}
						}
					}
					if(result.size() > 0){
						Message obtainMessage = handler.obtainMessage();
						obtainMessage.obj = result;
						handler.sendMessage(obtainMessage);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void sendLog(){
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_INFO, getIntent().getLongExtra("id", 0), Constant.TYPE_SPEAKER, Constant.SERACH_TYPE_ENTER, null);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bd = new Bundle();
		bd.putLong("id", adapter.getItem(position).getId());
		startTo(ScheduleInfoActivity.class, bd);
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
