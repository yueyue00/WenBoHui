package com.gheng.exhibit.view.noused;

import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.database.task.ScheduleInfoTask;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.response.ScheduleInfoData;
import com.gheng.exhibit.http.body.response.ScheduleInfoSupportData;
import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.DateTools;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.utils.UIUtils;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.ScheduleInfoAdapter2;
import com.gheng.exhibit.widget.PopMenu;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smartdot.wenbo.huiyi.R;

/**
 * 会议详情
 * 
 * @author lileixing
 */
public class ScheduleInfoActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.tv_remark)
	private TextView tv_remark;

	@ViewInject(R.id.tv_name_label)
	private TextView tv_name_label;

	@ViewInject(R.id.tv_name_2)
	private TextView tv_name_2;

	@ViewInject(R.id.tv_time_label)
	private TextView tv_time_label;

	@ViewInject(R.id.tv_time)
	private TextView tv_time;

	@ViewInject(R.id.tv_address_label)
	private TextView tv_address_label;

	@ViewInject(R.id.tv_address)
	private TextView tv_address;

	@ViewInject(R.id.tv_sponser_label)
	private TextView tv_sponser_label;

	@ViewInject(R.id.tv_sponser)
	private TextView tv_sponser;

	@ViewInject(R.id.lv)
	private ListView lv;

	@ViewInject(R.id.tv_label)
	private View tv_label;

	private ScheduleInfoAdapter2 adapter;
	
	private Boolean isActive = true;

	// 点赞
	@ViewInject(R.id.layout_praise)
	private View layout_praise;
	@ViewInject(R.id.tv_praise_count)
	private TextView tv_praise_count;
	@ViewInject(R.id.iv_praise)
	private ImageView iv_praise;
	// 收藏
	@ViewInject(R.id.layout_fav)
	private View layout_fav;
	@ViewInject(R.id.iv_fav)
	private ImageView ivFav;
	@ViewInject(R.id.tv_fav)
	private TextView tvFav;

	// 报名
	@ViewInject(R.id.layout_baoming)
	private View layout_baoming;
	@ViewInject(R.id.tv_baoming)
	private TextView tv_baoming;

	private Schedule model;
	private long id;

	//检测是否有变化
	private int isPraise;
	private int isFav;
	private int isSign = -1;

	@ViewInject(R.id.tv_remark_label)
	private TextView tv_remark_label;
	
	private PopMenu popMenu;
//	private SNSUtils snsUtils;
	@ViewInject(R.id.tv)
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_info);
		registerBoradcastReceiver();//注册广播
		tv_label.setVisibility(View.GONE);
		tvFav.setText(getLanguageString("收藏"));
		tv_baoming.setText(getLanguageString("报名"));
		tv_praise_count.setText("(0)");
	}

	@Override
	protected void setI18nValue() {

		tv_name_label.setText(getLanguageString("名称") + " : ");
		tv_time_label.setText(getLanguageString("时间") + " : ");
		tv_address_label.setText(getLanguageString("地点") + " : ");
		tv_sponser_label.setText(getLanguageString("主承办") + " : ");
		
		tv_remark_label.setText(getLanguageString("简介")+" : ");

		tv_remark.setTag(true);
		tv_label.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((Boolean) tv_remark.getTag() == true) {
					tv_remark.setSingleLine(false);
					tv_remark.setTag(false);
				} else {
					tv_remark.setSingleLine(true);
					tv_remark.setTag(true);
				}
			}
		});
		tv_label.setVisibility(View.GONE);
		titleBar.setText(getLanguageString("会议日程"));
	}

	@Override
	protected void init() {
		sendLog();
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {

			@Override
			public void clickRightImage() {
				showPopMenu();
			}

			@Override
			public void clickLeftImage() {
				myFinish();
			}
		});
		adapter = new ScheduleInfoAdapter2(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		loadData();
	}
	
	private void showPopMenu(){
		if(popMenu == null){
//			snsUtils = new SNSUtils(this);
			popMenu = new PopMenu(this);
			popMenu.addItem(getLanguageString("首页"), R.drawable.home);
			popMenu.addItem(getLanguageString("分享"), R.drawable.share);
			
			popMenu.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					popMenu.dismiss();
					switch (position) {
					case 0:
//						UIUtils.goHome();
						break;
					case 1:
						String title = getResources().getString(R.string.app_name);
						title = getLanguageString(title);
						String content = (String) I18NUtils.getValue(model, "name");
						content += " " + tv_time.getText() +" " + tv_address.getText();
						// snsUtils.share(title, content,"");
						break;
					}
				}
			});
		}
		popMenu.showAsDropDown(tv);
	}
	
	@OnClick(value={R.id.v_to_func})
	public void gotoFunc(View v){
		Bundle bd = new Bundle();
		bd.putString("web_page", "function_chart");
		bd.putString("title", getLanguageString("服务设施"));
		startTo(WebViewActivity.class, bd);
	}

	private void loadData() {
		ProgressTools.showDialog(this);
		id = getIntent().getLongExtra("id", 0);
		new ScheduleInfoTask(id, new CallBack<ScheduleInfoData>() {

			@Override
			public void onSuccess(ScheduleInfoData entity) {
				ProgressTools.hide();
				model = entity.model;
				if(model == null)
					return;
				if(isSign == -1){
					isFav = model.getIsfav();
					isPraise = model.getIspraise();
					isSign = model.getIssign();
				}
				
			
				tv_remark.setText(entity.remark);
				tv_name_2.setText(entity.name);

				layout_fav.setOnClickListener(ScheduleInfoActivity.this);
				layout_praise.setOnClickListener(ScheduleInfoActivity.this);
				layout_baoming.setOnClickListener(ScheduleInfoActivity.this);

				String time = DateTools.formatNoYear(DateTools.parse(entity.date, "yyyy-MM-dd"));

				tv_time.setText(time + " " + entity.startime + "-" + entity.endtime);

				if (StringTools.isBlank(entity.address)) {
					((View)tv_address_label.getParent()).setVisibility(View.GONE);
				}
				if (StringTools.isBlank(entity.sponsor)) {
					((View)tv_sponser_label.getParent()).setVisibility(View.GONE);
				}

				if (StringTools.isNotBlank(entity.remark)) {
					tv_label.setVisibility(View.VISIBLE);
				}

				tv_address.setText(entity.address);
				tv_sponser.setText(entity.sponsor);

				adapter.setData(entity.rdata);

				if (model.getIsfav() == 1) {
					ivFav.setImageResource(R.drawable.fav2);
				} else {
					ivFav.setImageResource(R.drawable.fav);
				}

				if (model.getIspraise() == 1) {
					iv_praise.setImageResource(R.drawable.btn_zan2);
				} else {
					iv_praise.setImageResource(R.drawable.btn_zan);
				}
				tv_praise_count.setText("("+model.getPraisecount()+")");
				
				if(model.getIssign() == 1){
					tv_baoming.setText(getLanguageString("取消报名"));
				}else{
					tv_baoming.setText(getLanguageString("报名"));
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				toastNetError();
			}
		}).execute();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ScheduleInfoSupportData model = adapter.getItem(position);
		if (StringTools.isBlank(model.speaker))
			return;
		String[] idStr = model.speakerid.split(",");
		Bundle bd = new Bundle();
		if (idStr.length == 1) {
			bd.putLong("id", Long.valueOf(idStr[0]));
			startTo(SpeakerInfoActivity.class, bd);
		} else {
			showSelectDialog(model);
		}
	}

	private void sendLog() {
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_INFO, getIntent().getLongExtra("id", 0), Constant.TYPE_SCHEDULE, Constant.SERACH_TYPE_ENTER, null);
	}

	private void showSelectDialog(final ScheduleInfoSupportData model) {
		Builder builder = new Builder(this);
		builder.setSingleChoiceItems(model.speaker.split(","), -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String[] ids = model.speakerid.split(",");
				Bundle bd = new Bundle();
				bd.putLong("id", Long.valueOf(ids[which]));
				startTo(SpeakerInfoActivity.class, bd);
			}
		});
		builder.setPositiveButton(getLanguageString("取消"), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_baoming: // 报名
			if (!AppTools.isLogin()) {
//				UIUtils.startToRegister(this);
				return;
			}
			if(model.getIssign() == 0){
				UIUtils.goSignUpForResult(this, id);
			}else{
				cacelSign();
			}
			break;
		case R.id.layout_fav:
			if (AppTools.isLogin()) {
				postFav();
			} else {
//				UIUtils.startToRegister(this);
			}
			break;
		case R.id.layout_praise: // 点赞
			postPraise();
			break;
		}
	}

	// 点赞和取消赞
	private void postPraise() {
		ProgressTools.showDialog(this);
		int mode = 0;
		if (model.getIspraise() == 0) {
			mode = 1;
		} else {
			mode = 0;
		}
		ApiUtil.postZan(id, Constant.TYPE_SCHEDULE, mode, new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
			}

			@Override
			public void callback(boolean success, long id, int type, int mode, Object data) {
				ProgressTools.hide();
				String[] strs = { "取消赞成功", "点赞成功" };
				String message = ((BaseActivity) context).getLanguageString(strs[mode]);
				int count = model.getPraisecount();
				if (success) {
					((BaseActivity) context).toastShort(message);
					if (mode == 0) {
						model.setIspraise(0);
						iv_praise.setImageResource(R.drawable.btn_zan);
						count = count > 0 ? count - 1 : 0;
					} else {
						count ++;
						model.setIspraise(1);
						iv_praise.setImageResource(R.drawable.btn_zan2);
					}
					model.setPraisecount(count);
					tv_praise_count.setText("("+count+")");
				} else {
					((BaseActivity) context).toastNetError();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 300 && resultCode == 100){
			loadData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	// 收藏和取消收藏
	private void postFav() {
		ProgressTools.showDialog(this);
		int mode = 0;
		if (model.getIsfav() == 0) {
			mode = 1;
		} else {
			mode = 0;
		}
		ApiUtil.postFav(id, Constant.TYPE_SCHEDULE, mode, new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
			}

			@Override
			public void callback(boolean success, long id, int type, int mode, Object data) {
				ProgressTools.hide();
				String[] strs = { "取消收藏成功", "收藏成功" };
				String message = ((BaseActivity) context).getLanguageString(strs[mode]);
				if (success) {
					((BaseActivity) context).toastShort(message);
					if (mode == 0) {
						model.setIsfav(0);
						ivFav.setImageResource(R.drawable.fav);
					} else {
						model.setIsfav(1);
						ivFav.setImageResource(R.drawable.fav2);
					}
				} else {
					((BaseActivity) context).toastNetError();
				}
			}
		});
	}
	
	private void cacelSign(){
		ApiUtil.postSign(id, 0, null, new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
			}

			@Override
			public void callback(boolean success, long id, int type, int mode, Object data) {
				ProgressTools.hide();
				model.setIssign(0);
				tv_baoming.setText(getLanguageString("报名"));
				toastShort(getLanguageString("取消报名成功"));
			}
		});
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			myFinish();
		}
		return super.dispatchKeyEvent(event);
	}
	
	private void myFinish(){
		if(model != null){
			if(isPraise != model.getIspraise() || isSign != model.getIssign()){
				Intent i = new Intent();
				i.putExtras(getIntent());
				i.putExtra("ispraise", model.getIspraise());
				i.putExtra("issign", model.getIssign());
				i.putExtra("praisecount", model.getPraisecount());
				setResult(100,i);
			}
		}
		finish();
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
