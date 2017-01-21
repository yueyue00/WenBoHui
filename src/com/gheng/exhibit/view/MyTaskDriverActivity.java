package com.gheng.exhibit.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.Driver_Vip;
import com.gheng.exhibit.http.body.response.QianDaoBackInfo;
import com.gheng.exhibit.http.body.response.QianDaoHuiChangInfo;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.adapter.DriverListviewAdapter;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.zAsyncTaskForHuiChang;
import com.hebg3.mxy.utils.zAsyncTaskForQianDaoBackInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class MyTaskDriverActivity extends Activity implements OnClickListener {
	/**
	 * btn_back 返回按钮
	 */
	private Button btn_back;
	/**
	 * titleView 沉浸式状态栏的一部分
	 */
	private View titleView;
	/**
	 * tv_title 界面标题
	 */
	private TextView tv_title;
	/**
	 * tv_name 司机名
	 */
	private TextView tv_name;
	/**
	 * iv_carImg 车的图片
	 */
	private ImageView iv_carImg;
	/**
	 * tv_carName 车型
	 */
	private TextView tv_carName;
	/**
	 * tv_carNum 车牌
	 */
	private TextView tv_carNum;
	/**
	 * tv_peopleNum 载客量
	 */
	private TextView tv_peopleNum;

	private ImageButton btn_gotowork;
	private ImageButton btn_gooffwork;
	private ImageButton btn_qiaodao;
	private ImageView iv_qd_bj, iv_go_bj, iv_arrive_bj;// 签到，出发，到达的标记
	private ImageView iv_qd_car, iv_go_car, iv_arrive_car;

	// private GridView gridView ;
	private ListView listView;
	private Button myDrivier_fache;
	private Button myDrivier_shouche;

	private Context mContext;
	private User user;
	private String title = "服务签到";
	private List<Driver_Vip> gridList;
	private List<Driver_Vip> listData;
	private List<String> selectedList;
	boolean flag_fache=true;
	private DriverListviewAdapter adapter;
	// zyj 添加
	QianDaoHuiChangInfo qinChangInfo;
	QianDaoBackInfo qinBackInfo;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; // 是否首次定位
	// 定位相关
	LocationClient mLocClient;
	LatLng ml1 = null;

	// zyj 修改
	// private Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	// case 1:
	// String result = (String) msg.obj;
	// System.out.println("======司机签到接口数据====》" + result);
	// break;
	// case -1:
	// Toast.makeText(mContext, "数据请求有误", Toast.LENGTH_LONG).show();
	// break;
	// case 300:
	// Toast.makeText(mContext, "数据请求为空", Toast.LENGTH_LONG).show();
	// break;
	// case 400:
	// Toast.makeText(mContext, "数据请求失败", Toast.LENGTH_LONG).show();
	// break;
	// case 500:
	// Toast.makeText(mContext, "数据请求超时", Toast.LENGTH_LONG).show();
	// break;
	// default:
	// break;
	// }
	// };
	// };

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
		setContentView(R.layout.activity_my_task_driver);
		mContext = this;
		try {
			DbUtils db = DbUtils.create(mContext);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		intiview();
//		setStatusBar();
		// initGridViewData();
		initListView();
		process();
		// loadData();
		LoadData();
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

	/**
	 * zyj 修改 从网络请求数据
	 */
	private void loadData() {
		// 查找
		// try {
		// new DriverTaskAsynctask(handler.obtainMessage(), userid, mContext)
		// .execute(1);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	/**
	 * 初始化gridview 数据
	 */
	// private void initGridViewData() {
	// gridList = new ArrayList<>();
	// gridList.clear();
	// for (int i = 0; i < 5; i++) {
	// Driver_Vip driver_Vip = new Driver_Vip();
	// driver_Vip.setName("成龙"+i);
	// gridList.add(driver_Vip);
	// }
	// adapter = new DriverGridviewAdapter(mContext, gridList);
	// gridView.setAdapter(adapter);
	// gridView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// Toast.makeText(mContext, "点击了--》"+gridList.get(position).getName(),
	// Toast.LENGTH_LONG).show();
	// }
	// });
	// }
	private int position;

	public void initListView() {
		listData = new ArrayList<>();
		selectedList = new ArrayList<>();
		listData.clear();
		selectedList.clear();
		for (int i = 0; i < 18; i++) {
			Driver_Vip dVip = new Driver_Vip();
			dVip.setId(i + "");
			dVip.setName("藤林杏" + i);
			dVip.setCheck(false);
			dVip.setJob("副局长");
			dVip.setCpMobile("18823456789");
			dVip.setMobile("10086");
			dVip.setIconPath("http://a2.att.hudong.com/58/52/01300000329092128263525854136.jpg");
			listData.add(dVip);
		}
		adapter = new DriverListviewAdapter(mContext, listData);
		listView.setAdapter(adapter);
		adapter.setListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				Object tag = buttonView.getTag();

				if (tag instanceof Integer) {
					position = (Integer) tag;
				}
				Driver_Vip driver_Vip = listData.get(position);
				if (isChecked) {
					driver_Vip.setCheck(true);
					selectedList.add(driver_Vip.getName());
				} else {
					driver_Vip.setCheck(false);
					selectedList.remove(driver_Vip.getName());
				}
				adapter.notifyDataSetChanged();
			}
		});

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
//			titleView.setVisibility(View.GONE);
//		}
//	}

	private void intiview() {
		btn_back = (Button) findViewById(R.id.but_fanhui);
//		titleView = (View) findViewById(R.id.my_driver_view);
		tv_title = (TextView) findViewById(R.id.in_title);
		tv_title.setText("会场");
		tv_name = (TextView) findViewById(R.id.my_driver_tv_name);
		iv_carImg = (ImageView) findViewById(R.id.my_driver_iv_carImg);
		tv_carName = (TextView) findViewById(R.id.my_driver_tv_carName);
		tv_carNum = (TextView) findViewById(R.id.my_driver_tv_carNum);
		tv_peopleNum = (TextView) findViewById(R.id.my_driver_tv_peopleNum);
		btn_gotowork = (ImageButton) findViewById(R.id.my_driver_btn_gotowork);
		btn_gooffwork = (ImageButton) findViewById(R.id.my_driver_btn_gooffwork);
		btn_qiaodao = (ImageButton) findViewById(R.id.my_driver_btn_qiaodao);
		listView = (ListView) findViewById(R.id.my_driver_listview);
		iv_qd_bj = (ImageView) findViewById(R.id.my_driver_qd_biaoji);
		iv_go_bj = (ImageView) findViewById(R.id.my_driver_go_biaoji);
		iv_arrive_bj = (ImageView) findViewById(R.id.my_driver_arrive_biaoji);
		iv_qd_car = (ImageView) findViewById(R.id.my_driver_qd_car);
		iv_go_car = (ImageView) findViewById(R.id.my_driver_go_car);
		iv_arrive_car = (ImageView) findViewById(R.id.my_driver_arrive_car);
        myDrivier_fache = (Button) findViewById(R.id.myDrivier_fache);
        myDrivier_shouche = (Button) findViewById(R.id.myDrivier_shouche);
		
		
		btn_back.setOnClickListener(this);
		btn_qiaodao.setOnClickListener(this);
		btn_gotowork.setOnClickListener(this);
		btn_gooffwork.setOnClickListener(this);
		
		myDrivier_fache.setOnClickListener(this);
		myDrivier_shouche.setOnClickListener(this);
	}

	// zyj 添加
	public void LoadData() {
		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
			Toast.makeText(mContext, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				zAsyncTaskForHuiChang at = new zAsyncTaskForHuiChang(
						qiandao_handler.obtainMessage(), mContext,
						Constant.decode(Constant.key, user.getUserId()));
				at.execute(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// zyj 添加
	public void LoadQianDao(Handler handler,String taskId, String type, String longitude,
			String latitude) {
		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
			Toast.makeText(mContext, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				zAsyncTaskForQianDaoBackInfo at = new zAsyncTaskForQianDaoBackInfo(
						handler.obtainMessage(), mContext,
						Constant.decode(Constant.key, user.getUserId()),
						taskId, type, longitude, latitude);
				at.execute(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_fanhui:// 返回按钮
			finish();
			break;
		case R.id.my_driver_btn_qiaodao:
			showCustomDialog("签到", btn_qiaodao,
					R.drawable.ic_mytask_qiaodao_highlight, iv_qd_bj,
					R.drawable.ic_mytask_biaoji_highlight);
			Toast.makeText(mContext, "===>" + selectedList.toString(),
					Toast.LENGTH_LONG).show();
			break;
		case R.id.myDrivier_fache://发车
			// String taskId, String type, String longitude,
			// String latitude
			if(flag_fache==true){
				if (ml1 != null) {
					String longitude = String.valueOf(ml1.longitude);
					String latitude = String.valueOf(ml1.latitude);
					LoadQianDao(faChebBack_handler,"chufa", "1", longitude, latitude);
				} else {
					LoadQianDao(faChebBack_handler,"chufa", "1", "", "");
				}
				flag_fache=false;
			}
			
//			if (Constant.GO) {
//				btn_gotowork.setBackgroundResource(R.drawable.ic_mytask_go);
//				iv_go_bj.setImageResource(R.drawable.ic_mytask_biaoji);
//				iv_go_car.setVisibility(View.INVISIBLE);
//				Constant.GO = false;
//			} else {
//				btn_gotowork
//						.setBackgroundResource(R.drawable.ic_mytask_go_highlight);
//				iv_go_bj.setImageResource(R.drawable.ic_mytask_biaoji_highlight);
//				iv_go_car.setVisibility(View.VISIBLE);
//				Constant.GO = true;
//
//				Constant.ARRIVE = false;
//				btn_gooffwork
//						.setBackgroundResource(R.drawable.ic_mytask_arrive);
//				iv_arrive_bj.setImageResource(R.drawable.ic_mytask_biaoji);
//				iv_arrive_car.setVisibility(View.INVISIBLE);
//			}

			// showCustomDialog("上岗",btn_gotowork,R.drawable.btn_mytask_gotowork_highlight);
			break;
		case R.id.myDrivier_shouche://收车
			if(flag_fache==false){
				if (ml1 != null) {
					String longitude = String.valueOf(ml1.longitude);
					String latitude = String.valueOf(ml1.latitude);
					LoadQianDao(faChebBack_handler,"daoda", "1", longitude, latitude);
				} else {
					LoadQianDao(faChebBack_handler,"daoda", "1", "", "");
				}
				flag_fache=true;
			}
			// showCustomDialog("下班",btn_gooffwork,R.drawable.btn_mytask_gooffwork_highlight);
//			if (Constant.GO) {
//				if (Constant.ARRIVE) {
//					btn_gooffwork
//							.setBackgroundResource(R.drawable.ic_mytask_arrive);
//					iv_arrive_bj.setImageResource(R.drawable.ic_mytask_biaoji);
//					iv_arrive_car.setVisibility(View.INVISIBLE);
//					Constant.ARRIVE = false;
//
//				} else {
//					btn_gooffwork
//							.setBackgroundResource(R.drawable.ic_mytask_arrive_hihglight);
//					iv_arrive_bj
//							.setImageResource(R.drawable.ic_mytask_biaoji_highlight);
//					iv_arrive_car.setVisibility(View.VISIBLE);
//					Constant.ARRIVE = true;
//
//					Constant.GO = false;
//					btn_gotowork.setBackgroundResource(R.drawable.ic_mytask_go);
//					iv_go_bj.setImageResource(R.drawable.ic_mytask_biaoji);
//					iv_go_car.setVisibility(View.INVISIBLE);
//
//				}
//
//			} else {
//				Toast.makeText(mContext, "您应该先点击出发", Toast.LENGTH_LONG).show();
//			}
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

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		super.onDestroy();
	}

	/**
	 * 展示Dialog
	 */
	public void showCustomDialog(final String taskContent,
			final ImageButton button, final int resourceId,
			final ImageView ivBiaoji, final int bjResourceId) {
		// 将布局文件转为View
		View customDialogView = LayoutInflater.from(mContext).inflate(
				R.layout.custom_dialog, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(mContext).create();
		dialog.show();
		dialog.getWindow().setContentView(customDialogView);
		// 确认button
		Button btn_ok = (Button) customDialogView.findViewById(R.id.dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button.setEnabled(false);
				button.setImageResource(resourceId);
				ivBiaoji.setImageResource(bjResourceId);
				Toast.makeText(mContext, "确认完成该任务-->" + taskContent,
						Toast.LENGTH_LONG).show();
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

	// zyj 添加
	Handler qiandao_handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				qinChangInfo = (QianDaoHuiChangInfo) msg.obj;
				if (qinChangInfo.userMap.sjname != null)
					tv_name.setText(qinChangInfo.userMap.sjname + ",您好" + "!");
				if (qinChangInfo.userMap.sjchexing != null)
					tv_carName.setText("车型：" + qinChangInfo.userMap.sjchexing);
				if (qinChangInfo.userMap.sjcarNumber != null)
					tv_carNum.setText("车牌：" + qinChangInfo.userMap.sjcarNumber);
				if (qinChangInfo.userMap.sjcarryNo != null)
					tv_peopleNum.setText("载客数："
							+ qinChangInfo.userMap.sjcarryNo);
				switch (qinChangInfo.userMap.sign) {
				case "xingshizhong":// 发车不可以点击
					// btn_gotowork.setBackgroundResource(R.drawable.btn_mytask_gotowork);
					// btn_gooffwork.setBackgroundResource(R.drawable.btn_mytask_gooffwork_highlight);
					flag_fache=false;
					myDrivier_fache.setSelected(true);
					myDrivier_shouche.setSelected(false);
					myDrivier_fache.setClickable(false);
					myDrivier_shouche.setClickable(true);
					break;
				case "kongxian":// 收车不可以点击
					// btn_gotowork.setBackgroundResource(R.drawable.btn_mytask_gotowork_highlight);
					// btn_gooffwork.setBackgroundResource(R.drawable.btn_mytask_gooffwork);
					flag_fache=true;
					myDrivier_fache.setSelected(false);
					myDrivier_shouche.setSelected(true);
					myDrivier_fache.setClickable(true);
					myDrivier_shouche.setClickable(false);
					break;

				default:
					break;
				}
				ImageLoader imageLoader = ImageLoader.getInstance();
				DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.car_icon)
				.showImageForEmptyUri(R.drawable.car_icon)
				.showImageOnFail(R.drawable.car_icon)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
				imageLoader.displayImage(qinChangInfo.userMap.sjimg, iv_carImg, options);
				break;
			case 1:
//				Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
//				Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
//				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
//				Toast.makeText(mContext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
//						.show();
				break;
			default:
				break;
			}
		}
	};
	// zyj 添加
	//发车
	Handler faChebBack_handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 因为info里面没有返回数据所有不走这里
				qinBackInfo = (QianDaoBackInfo) msg.obj;
				if (qinBackInfo.sign.equals("1")) {
					Toast.makeText(mContext, "您已经发车了!", Toast.LENGTH_SHORT)
							.show();
				} else if(qinBackInfo.sign.equals("0")){
					Toast.makeText(mContext, "发车成功!", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 1:
				String message = (String) msg.obj;
				LoadData();
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "返回数据异常!", Toast.LENGTH_SHORT).show();
				break;
			case 3:
//				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
//				Toast.makeText(mContext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
//						.show();
				break;
			case 5:
				Toast.makeText(mContext, "info信息为空", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};
	//收车
//	Handler shouCheBack_handler = new Handler() {
//		@SuppressWarnings("unchecked")
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 0:// 融云获取token时返回内容
////				String result = (String) msg.obj;
////				Toast.makeText(mContext, result, Toast.LENGTH_SHORT)
////				.show();
//				qinBackInfo = (QianDaoBackInfo) msg.obj;
//				if (qinBackInfo.sign.equals("1")) {
//					Toast.makeText(mContext, "您还没发车不可以收车!", Toast.LENGTH_SHORT)
//							.show();
//				} else if(qinBackInfo.sign.equals("0")){
//					Toast.makeText(mContext, "收车成功!", Toast.LENGTH_SHORT)
//							.show();
//				}
//				break;
//			case 1:
////				Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
//				break;
//			case 2:
//				Toast.makeText(mContext, "返回数据异常!", Toast.LENGTH_SHORT).show();
//				break;
//			case 3:
////				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
//				break;
//			case 4:
////				Toast.makeText(mContext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
////						.show();
//				break;
//			case 5:
//				Toast.makeText(mContext, "返回info信息为空!", Toast.LENGTH_SHORT)
//						.show();
//				break;
//			default:
//				break;
//			}
//		}
//	};
}
