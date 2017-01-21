package com.gheng.exhibit.view.checkin.checkin;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.QianDaoBackInfo;
import com.gheng.exhibit.http.body.response.QianDaoInfo;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.MyTaskDriverActivity.MyLocationListenner;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.zAsyncTaskForQianDaoBackInfo;
import com.hebg3.mxy.utils.zAsyncTaskForQianDaoInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

public class SignActivity extends Activity implements OnClickListener {

	private TextView tv_title, tv_name, tv_date;
	private View view;
	private Button btn_gotowork, btn_gooffwork;
	private Button iv_back;

	private Context mContext;
	private String title;
	// zyj
	boolean flag_shanggang=true;
	User parent = null;
	QianDaoInfo qiInfo;
	QianDaoBackInfo qinBackInfo;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; // 是否首次定位
	// 定位相关
	LocationClient mLocClient;
	LatLng ml1 = null;

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
//		setStatusBar();//这个应该放在setContentView()的方法下面
		setContentView(R.layout.activity_sign);
		mContext = this;
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		// 查找
		try {
			DbUtils db = DbUtils.create(mContext);
			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		initView();
		process();
		LoadData();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.in_title);
		tv_title.setText(title);
		tv_name = (TextView) findViewById(R.id.sign_tv_name);
		tv_date = (TextView) findViewById(R.id.sign_tv_date);
		tv_date.setText("今天是：" + getToadyDate(System.currentTimeMillis()));
//		view = findViewById(R.id.sign_view);
		iv_back = (Button) findViewById(R.id.but_fanhui);
		btn_gotowork = (Button) findViewById(R.id.sign_btn_gotowork);
		btn_gooffwork = (Button) findViewById(R.id.sign_btn_gooffwork);
		iv_back.setOnClickListener(this);
		btn_gotowork.setOnClickListener(this);
		btn_gooffwork.setOnClickListener(this);
	}

	// zyj 添加百度定位
	private void process() {
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	// zyj 添加
	public void LoadData() {
		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
			Toast.makeText(mContext, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				zAsyncTaskForQianDaoInfo at = new zAsyncTaskForQianDaoInfo(
						qiandao_handler.obtainMessage(), mContext,
						Constant.decode(Constant.key, parent.getUserId()));
				at.execute(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// zyj 添加
	public void LoadQianDao(String taskId, String type, String longitude,
			String latitude) {
		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
			Toast.makeText(mContext, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				zAsyncTaskForQianDaoBackInfo at = new zAsyncTaskForQianDaoBackInfo(
						qiandaoback_handler.obtainMessage(), mContext,
						Constant.decode(Constant.key, parent.getUserId()),
						taskId, type, longitude, latitude);
				at.execute(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取当天时间
	 * 
	 * @param currentTime
	 * @return
	 */
	public String getToadyDate(long currentTime) {
		String currentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		currentDate = format.format(new Date(currentTime));
		return currentDate;
	}

	/**
	 * 透明状态栏
	 */
//	public void setStatusBar() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			Window window = getWindow();
//			// Translucent status bar
//			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		} else {
//			view.setVisibility(View.GONE);
//		}
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_fanhui:
			finish();
			break;
		case R.id.sign_btn_gotowork:
			// showCustomDialog("上岗", btn_gotowork,
			// R.drawable.btn_mytask_gotowork_highlight);
			if(flag_shanggang==true){
				if (ml1 != null) {
					String longitude = String.valueOf(ml1.longitude);
					String latitude = String.valueOf(ml1.latitude);
					LoadQianDao("shanggang", "1", longitude, latitude);
				} else {
					LoadQianDao("shanggang", "1", "", "");
				}
				flag_shanggang=false;
			}
			break;
		case R.id.sign_btn_gooffwork:
			// showCustomDialog("下班", btn_gooffwork,
			// R.drawable.btn_mytask_gooffwork_highlight);
			if(flag_shanggang==false){
				if (ml1 != null) {
					String longitude = String.valueOf(ml1.longitude);
					String latitude = String.valueOf(ml1.latitude);
					LoadQianDao("xiaban", "1", longitude, latitude);
				} else {
					LoadQianDao("xiaban", "1", "", "");
				}
				flag_shanggang=true;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * zyj 添加 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null) {
				return;
			}
			ml1 = new LatLng(location.getLatitude(), location.getLongitude());
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	/**
	 * 展示Dialog
	 */
	public void showCustomDialog(final String taskContent, final Button button,
			final int resourceId) {
		// 将布局文件转为View
		View customDialogView = LayoutInflater.from(mContext).inflate(
				R.layout.custom_dialog, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(mContext).create();
		dialog.show();
		dialog.setCancelable(false);// 设置进度条是否可以按退回键取消
		dialog.setCanceledOnTouchOutside(false);// 设置点击不会消失
		dialog.getWindow().setContentView(customDialogView);
		// 确认button
		Button btn_ok = (Button) customDialogView.findViewById(R.id.dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button.setEnabled(false);
				button.setBackgroundResource(resourceId);
				// Toast.makeText(mContext, "确认完成该任务-->"+taskContent,
				// Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
		});
		// 取消Button
		Button btn_cancel = (Button) customDialogView
				.findViewById(R.id.dialog_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	// zyj添加
	Handler qiandao_handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				qiInfo = (QianDaoInfo) msg.obj;
				if (qiInfo.user.sjname != null)
					tv_name.setText("您好," + qiInfo.user.sjname + "!");
				switch (qiInfo.user.sign) {
				case "shanggang":// 上班不可以点击
					// btn_gotowork.setBackgroundResource(R.drawable.btn_mytask_gotowork);
					// btn_gooffwork.setBackgroundResource(R.drawable.btn_mytask_gooffwork_highlight);
					flag_shanggang=false;
					btn_gotowork.setSelected(true);
					btn_gooffwork.setSelected(false);
					btn_gotowork.setClickable(false);
					btn_gooffwork.setClickable(true);
					break;
				case "xiaban":// 下班不可以点击
					// btn_gotowork.setBackgroundResource(R.drawable.btn_mytask_gotowork_highlight);
					// btn_gooffwork.setBackgroundResource(R.drawable.btn_mytask_gooffwork);
					flag_shanggang=true;
					btn_gotowork.setSelected(false);
					btn_gooffwork.setSelected(true);
					btn_gotowork.setClickable(true);
					btn_gooffwork.setClickable(false);
					break;

				default:
					break;
				}
				break;
			case 1:
				Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mContext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};
	// zyj添加
	Handler qiandaoback_handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				qinBackInfo = (QianDaoBackInfo) msg.obj;
				if (qinBackInfo.sign.equals("1")) {
					Toast.makeText(mContext, "您已经签到过!", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(mContext, "签到成功!", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 1:
				String message = (String) msg.obj;
				LoadData();
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mContext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

}
