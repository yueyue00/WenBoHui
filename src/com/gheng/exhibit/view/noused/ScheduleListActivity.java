package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.database.task.ScheduleTask;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.response.ScheduleListData;
import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smartdot.wenbo.huiyi.R;

/**
 * 会议列表
 * 
 * @author lileixing
 */
public class ScheduleListActivity extends BaseActivity {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.lv)
	private PinnedHeaderExpandableListView lv;

	@ViewInject(R.id.tv_time)
	private TextView tv_time;

	// private ScheduleAdapter adapter;

	private boolean isMine = false;

	@ViewInject(R.id.edt_name)
	private TextView edt_name;

	private Selector selector = null;

	@ViewInject(R.id.emptyView)
	private TextView emptyView;

	private Boolean isActive = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_list);
		registerBoradcastReceiver();// 注册广播
		lv.setHeaderView(getLayoutInflater().inflate(
				R.layout.item_schedule_title, lv, false));
		// adapter = new ScheduleAdapter(this,lv);
		// lv.setAdapter(adapter);
		isMine = getIntent().getBooleanExtra("isMine", false);
		if (isMine) {
			edt_name.setVisibility(View.GONE);
			titleBar.showRightImage(false);
		}
		emptyView.setVisibility(View.GONE);
		emptyView.setText(getLanguageString("未找到相关信息"));
	}

	@Override
	protected void setI18nValue() {
		edt_name.setHint(getLanguageString("搜索会议"));
	}

	@OnClick(value = { R.id.edt_name })
	public void clickEdtName(View v) {
		Intent intent = new Intent(context, ScheduleSearchActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	protected void init() {
		sendLog();
		titleBar.setText(getIntent().getStringExtra("title"));
		// adapter.setMine(isMine);
		lv.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent i = new Intent(context, ScheduleInfoActivity.class);
				Bundle bd = new Bundle();
				bd.putLong("id", id);
				bd.putInt("groupPosition", groupPosition);
				bd.putInt("childPosition", childPosition);
				i.putExtras(bd);
				startActivityForResult(i, 200);
				return false;
			}
		});

		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
				if (!isMine) {
					Intent intent = new Intent(context,
							ScheduleSearchActivity.class);
					startActivityForResult(intent, 100);
				}
			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		loadData();
		if (!isMine) {
			updateData();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100 && resultCode == 100) {
			String startTime = data.getStringExtra("startTime");
			String endTime = data.getStringExtra("endTime");
			String keyword = data.getStringExtra("keyword");
			ArrayList<Integer> list = data.getIntegerArrayListExtra("array");

			List<String> dates = data.getStringArrayListExtra("date");
			selector = Selector.from(Schedule.class);
			selector.where("starttime", ">=", chageTime(startTime));
			selector.and("starttime", "<=", chageTime(endTime));
			if (dates.size() > 0) {
				selector.and("date", "in", dates);
			}
			if (StringTools.isNotBlank(keyword)) {
				WhereBuilder wb = WhereBuilder.b("name", "like", "%" + keyword
						+ "%");
				wb.or("enname", "like", "%" + keyword + "%");
				selector.and(wb);
			}
			if (list.size() > 0) {
				WhereBuilder wb = null;
				if (contains(list, 0)) {
					wb = WhereBuilder.b("syncflag", "=", 1);
				}
				if (contains(list, 1)) {
					if (wb == null) {
						wb = WhereBuilder.b("feeflag", "=", 1);
					} else {
						wb.or("feeflag", "=", 1);
					}
				}
				selector.and(wb);
			}
			loadData();
		}
		// 详情页面返回数据
		if (requestCode == 200 && resultCode == 100) {
			int groupPosition = data.getIntExtra("groupPosition", 0);
			int childPosition = data.getIntExtra("childPosition", 0);
			// ScheduleListSupportData child = adapter.getChild(groupPosition,
			// childPosition);
			// child.issign = data.getIntExtra("issign", child.issign);
			// child.ispraise = data.getIntExtra("ispraise", child.ispraise);
			// child.praisecount = data.getIntExtra("praisecount",
			// child.praisecount);
			// adapter.notifyDataSetChanged();
		}
		// 列表点击报名返回数据
		if (requestCode == 300 && resultCode == 100) {
			int groupPosition = data.getIntExtra("groupPosition", 0);
			int childPosition = data.getIntExtra("childPosition", 0);
			// ScheduleListSupportData child = adapter.getChild(groupPosition,
			// childPosition);
			// child.issign = 1;
			// adapter.notifyDataSetChanged();
			// loadData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean contains(List<Integer> list, int value) {
		for (Integer integer : list) {
			if (integer == value)
				return true;
		}
		return false;
	}

	// 8:00 -> 08:00
	private String chageTime(String time) {
		if (time.length() < 5) {
			return "0" + time;
		}
		return time;
	}

	private void sendLog() {
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, 0,
				Constant.TYPE_SCHEDULE, Constant.SERACH_TYPE_ENTER, null);
	}

	private void updateData() {
		ApiUtil.invokeSchedule(new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
				boolean reload = false;
				if (map != null) {
					if (map.get(TimeRecordType.SCHEDULE_INFO) != null
							&& map.get(TimeRecordType.SCHEDULE_INFO) > 0) {
						reload = true;
					}
					if (!reload) {
						if (map.get(TimeRecordType.SCHEDULE_TYPE) != null
								&& map.get(TimeRecordType.SCHEDULE_TYPE) > 0) {
							reload = true;
						}
					}
					if (reload)
						loadData();
				}
			}
		});
	}

	private void loadData() {
		ProgressTools.showDialog(this);
		new ScheduleTask(isMine, selector,
				new CallBack<List<ScheduleListData>>() {

					@Override
					public void onSuccess(List<ScheduleListData> entity) {
						ProgressTools.hide();
						if (AppTools.isNotBlack(entity)) {
							emptyView.setVisibility(View.GONE);
						} else {
							emptyView.setVisibility(View.VISIBLE);
						}
						// adapter.setDatas(entity);
						int groupCount = lv.getCount();
						for (int i = 0; i < groupCount; i++) {
							// ScheduleListData group = adapter.getGroup(i);
							boolean expand = false;
							// if(group.isToday){
							// lv.expandGroup(i);
							// expand = true;
							// }else{
							// lv.collapseGroup(i);
							// }
							if (!expand) {
								lv.expandGroup(0);
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ProgressTools.hide();
					}
				}).execute();
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
