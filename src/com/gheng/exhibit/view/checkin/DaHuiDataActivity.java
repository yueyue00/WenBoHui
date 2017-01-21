package com.gheng.exhibit.view.checkin;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.DaHuiData;
import com.gheng.exhibit.http.body.response.DaHuiDataFiles;
import com.gheng.exhibit.http.download.DownloadInfo;
import com.gheng.exhibit.http.download.DownloadManager;
import com.gheng.exhibit.http.download.DownloadService;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.DaHuiDataAdapter;
import com.gheng.exhibit.view.support.FileManager;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class DaHuiDataActivity extends Activity implements View.OnClickListener {

	private Boolean isActive = true;

	@ViewInject(R.id.swipe_dahuidata)
	SwipeRefreshLayout swipe_dahuidata;
	@ViewInject(R.id.dahuidata_lv)
	PinnedHeaderExpandableListView dahuidata_lv;
	@ViewInject(R.id.emptyView)
	private TextView emptyView;
	@ViewInject(R.id.in_title)
	TextView in_title;
	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;

	ArrayList<DaHuiData> datas = new ArrayList<DaHuiData>();
	ProgressDialog pro;
	Gson g = new Gson();
	// User parent;
	private DaHuiDataAdapter adapter;
	// 下载文件
	private DownloadManager downloadManager;

	Context context;
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			swipe_dahuidata.setRefreshing(false);
			swipe_dahuidata.setEnabled(true);
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				ResponseBody<DaHuiData> res = (ResponseBody<DaHuiData>) msg.obj; // 首先创建接收方法
				// 其实就是拿到ResponseBody<HuiYiRiChengRiQiHuoQu>里面的list对象
				datas.clear();
				datas.addAll(res.list);
				adapter.getDatas().clear();
				adapter.setDatas(res.list);
				adapter.notifyDataSetChanged();
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确<对应于200>
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 3) { // 表示没有拿到列表数据<对应300>
				Toast.makeText(context,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			} else if (msg.what == 4) {// cookie失效<对应500>
				BaseActivity.gotoLoginPage(DaHuiDataActivity.this);
			}
			pro.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dahuidata);
		ViewUtils.inject(this);
		registerBoradcastReceiver();// 注册广播
		context = DaHuiDataActivity.this;
		downloadManager = DownloadService.getDownloadManager(context);
		emptyView.setText(BaseActivity.getLanguageString("暂时没有数据"));
		in_title.setText(BaseActivity.getLanguageString("大会资料"));
		process();
		loadData();
		setAllClickListener();
	}

	private void process() {
		adapter = new DaHuiDataAdapter(context, dahuidata_lv, downloadManager);
		dahuidata_lv.setAdapter(adapter);
		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失

		swipe_dahuidata.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		but_fanhui.setOnClickListener(this);
		emptyView.setVisibility(View.GONE);
		// // 对已经删除的没下载完的任务进行删除
		String target = AppTools.getRootPath() + "/wenbohui/cache/";
		File[] files = new File(target).listFiles();
		if (downloadManager != null)
			for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
				int flag = 0;// 本地不存在DownManager里面的文件
				DownloadInfo info = downloadManager.getDownloadInfo(i);
				for (File fff : files) {
					if ((info.getFileName().equals(fff.getName()))) {
						flag = 1;
					}
				}
				if (flag == 0) {
					try {
						downloadManager.removeDownload(info);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}
	}

	private void loadData() {

		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			pro.dismiss();
			Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				ClientParams client = new ClientParams();
				client.url = "/fileUpload.do";
				StringBuffer strbuf = new StringBuffer();
				strbuf.append("method=getFileData");
				strbuf.append("&lg=");
				if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
					strbuf.append("2");
				} else {
					strbuf.append("1");
				}
				String str = strbuf.toString();
				client.params = str;

				Type type = new TypeToken<ArrayList<DaHuiData>>() {
				}.getType();
				NetTask<DaHuiData> net = new NetTask<DaHuiData>(
						hand.obtainMessage(), client, type, context);
				net.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setAllClickListener() {
		swipe_dahuidata.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadData();
			}
		});

		dahuidata_lv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				DaHuiDataFiles file = datas.get(groupPosition).filedata
						.get(childPosition);
				if (isDownloadHalf(file)) {
					// Toast.makeText(context, "正在下载中...", Toast.LENGTH_SHORT)
					// .show();
				} else {
					if (isDownload(file)) {
						File fff = new File(Constant.target
								+ file.dataUploadName + file.fileFormat);
						FileManager.openFile(context, fff);
					} else {
						try {
							downloadManager.addNewDownload(file.dataUploadUrl,
									file.dataUploadName + file.fileFormat,
									Constant.target + file.dataUploadName
											+ file.fileFormat, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
									true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
									null);
						} catch (DbException e) {
							LogUtils.e(e.getMessage(), e);
						}
					}
				}

				adapter.notifyDataSetChanged();
				return true;
			}
		});
	}

	private boolean isDownloadHalf(DaHuiDataFiles file) {
		if (downloadManager != null)
			for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
				DownloadInfo info = downloadManager.getDownloadInfo(i);
				if (info.getDownloadUrl().equals(file.dataUploadUrl)) {
					HttpHandler.State state = info.getState();
					String aaa = "";
					switch (state) {
					case WAITING:
						aaa = "WAITING";
						break;
					case STARTED:
						aaa = "STARTED";
						break;
					case LOADING:
						aaa = "LOADING";
						break;
					case CANCELLED:
						aaa = "CANCELLED";
						break;
					case SUCCESS:
						aaa = "SUCCESS";
						break;
					case FAILURE:
						File fff = new File(Constant.target
								+ file.dataUploadName + file.fileFormat);
						try {
							downloadManager.removeDownload(info);
						} catch (DbException e) {
							e.printStackTrace();
						}
						// Toast.makeText(context, "文件下载失败，执行删除！",
						// Toast.LENGTH_SHORT).show();
						if (fff.isFile() && fff.exists()) {
							Toast.makeText(context, "文件下载失败，开始重新下载！",
									Toast.LENGTH_SHORT).show();
							return !fff.delete();
						}
						aaa = "FAILURE";
						break;
					default:
						aaa = "default";
						break;
					}
					// 测试用。。。
					// Toast.makeText(context, aaa, Toast.LENGTH_SHORT).show();
					if (state != state.SUCCESS) {
						return true;
					}
				}
			}
		return false;
	}

	public boolean isDownload(DaHuiDataFiles file) {

		String needdown = file.dataUploadName + file.fileFormat;
		String target = AppTools.getRootPath() + "/wenbohui/cache/";
		File[] files = new File(target).listFiles();
		for (File fff : files) {
			if (fff.getName().equals(needdown)) {
				return true;
			}
		}
		return false;
	}

	public boolean searchFile(String filename) {
		String target = AppTools.getRootPath() + "/wenbohui/cache/";
		File[] files = new File(target).listFiles();
		for (File file : files) {
			if (file.getName().equals(filename)) {
				return true;
			}
		}
		return false;
	}

	public DownloadInfo getDownloadInfo(DaHuiDataFiles dfile) {
		if (downloadManager != null)
			for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
				DownloadInfo info = downloadManager.getDownloadInfo(i);
				if (info.getDownloadUrl().equals(dfile.dataUploadUrl))
					return info;
			}
		return null;
	}

	@Override
	public void onClick(View v) {
		if (but_fanhui.getId() == v.getId()) {
			DaHuiDataActivity.this.finish();
		}
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

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

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if (mBroadcastReceiver != null) {
			this.unregisterReceiver(mBroadcastReceiver);
		}
		MyApplication.remove(this);
		try {
			if (adapter != null && downloadManager != null) {
				for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
					DownloadInfo info = downloadManager.getDownloadInfo(i);
					HttpHandler.State state = info.getState();
					if (state != state.LOADING)
						downloadManager.removeDownload(info);
				}
				downloadManager.backupDownloadInfoList();
			}
		} catch (DbException e) {
			LogUtils.e(e.getMessage(), e);
		}
		super.onDestroy();
	}

	/** 记录前台切后台时间 */
	private Long TimeStart;
	/*
	 * * 记录后台切前台时间
	 */
	private Long TimeEnd;

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
