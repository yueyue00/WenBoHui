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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.OrderBy;
import com.gheng.exhibit.model.databases.Speakers;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.SpeakerAdapter;
import com.gheng.exhibit.view.support.SearchData;
import com.gheng.exhibit.widget.EmptyView;
import com.gheng.exhibit.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *	演讲者列表
 * @author lileixing
 */
public class SpeakerListActivity extends BaseActivity {

	@ViewInject(R.id.edt_name)
	private EditText edt_name;
	
	@ViewInject(R.id.lv)
	private PullToRefreshListView lv;
	
	private Boolean isActive = true;
	
	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	private SpeakerAdapter adapter;
	
	@ViewInject(R.id.tv_time)
	private TextView tv_time;
	
	private int pageno = 1;
	
	private EmptyView emptyView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speaker_list);
		registerBoradcastReceiver();//注册广播
		emptyView = new EmptyView(this);
		lv.setEmptyView(emptyView);
	}
	
	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString("专家简介"));
		I18NUtils.setPullView(lv, this);
		I18NUtils.setTextView(edt_name,null, getLanguageString("名称/职位/工作单位"));
	}
	
	@Override
	protected void init() {
		sendLog(Constant.SERACH_TYPE_ENTER);
		adapter = new SpeakerAdapter(this);
		lv.setAdapter(adapter);
		lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadData(1);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadData(pageno + 1);
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bd = new Bundle();
				bd.putLong("id", adapter.getItem(position-1).getId());
				bd.putAll(context.getIntent().getExtras());
				startTo(SpeakerInfoActivity.class,bd);				
			}
		});
		
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			
			@Override
			public void clickRightImage() {
				if (edt_name.getVisibility() == View.GONE) {
					edt_name.setVisibility(View.VISIBLE);
				} else {
					sendLog(Constant.SERACH_TYPE_CLICK);
					loadData(1);
				}
			}
			
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		
		loadData(1);
		updateData();
	}
	
	private void sendLog(int searchType){
		SearchData data = null;
		String name = edt_name.getText().toString();
		if(StringTools.isNotBlank(name)){
			data = new SearchData();
			data.searchtxt = name;
		}
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, 0, Constant.TYPE_SPEAKER, searchType, data);
	}
	
	private void updateData(){
		ApiUtil.invokeSchedule(new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
				if(map != null){
					if(map.get(TimeRecordType.SPEAKER) != null
							&& map.get(TimeRecordType.SPEAKER) > 0){
						loadData(1);
					}
				}
			}
		});
	}
	
	private void loadData(int pno){
		this.pageno = pno;
		String keyword = edt_name.getText().toString();
		
		Selector selector = Selector.from(Speakers.class);
		
		ApiUtil.changeSelector(selector);
	
		if(StringTools.isNotBlank(keyword)){
			WhereBuilder whereBuilder = WhereBuilder.b("name", "like", "%"+keyword+"%");
			whereBuilder.or("enname", "like", "%"+keyword+"%");
			whereBuilder.or("office", "like", "%"+keyword+"%");
			whereBuilder.or("enoffice", "like", "%"+keyword+"%");
			whereBuilder.or("workplace", "like", "%"+keyword+"%");
			whereBuilder.or("enworkplace", "like", "%"+keyword+"%");
			selector.and(whereBuilder);
		}
		List<OrderBy> orderList = new ArrayList<OrderBy>();
		orderList.add(OrderBy.create("name", false, true));
//		if(SharedData.getInt("i18n", Language.ZH) == Language.ZH){
//			orderList.add(OrderBy.create("name", false, true));
//		}else{
//			orderList.add(OrderBy.create("enname", false, true));
//		}
		
		http.postToDataBase(selector, pno,orderList, new CallBack<PageBody<Speakers>>() {

			@Override
			public void onSuccess(PageBody<Speakers> entity) {
				emptyView.show(true);
				lv.onRefreshComplete();
				if(pageno == 1){
					adapter.setData(entity.rdata);
				}else{
					adapter.add(entity.rdata);
				}
				if(pageno >= entity.pagecount){
					lv.setMode(Mode.PULL_FROM_START);
				}else{
					lv.setMode(Mode.BOTH);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				lv.onRefreshComplete();
			}
		});
		
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
