package com.gheng.exhibit.view.checkin.checkin;

import java.net.URLEncoder;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.CulturalInfoBean;
import com.gheng.exhibit.http.body.response.CulturalInfoBean.InfoBean;
import com.gheng.exhibit.http.body.response.InitData;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.service.PlayVoiceService;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.CulturalExhibitActivity;
import com.hebg3.mxy.utils.CulturalExhibitionDetailAsyncTask;
import com.hebg3.mxy.utils.CulturalExhibitionDetailWebAsyncTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CultrualExhibitDetailActivity extends BaseActivity implements OnClickListener {
   
	private View view;
	private ImageButton iv_back;
	private TextView tv_title;
	private ImageView iv_icon;
	private TextView tv_name;
	private LinearLayout linear_local;
	private TextView tv_hall;
	private TextView play;
	private TextView tv_introduce;
	private LinearLayout linear_play;
	private ImageView iv_play;
	private WebView webView;
	
	private Context mContext;
	private User user;
	private String userid;
	private String uniqueCode;
	private int FLAG_PLAY = 1;//用来判断是播放还是暂停
	/**
	 * 用于区分是由展厅activity跳转的还是由列表跳转的
	 */
	private MyMusicBroadCastReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cultrual_exhibit_detail);
		mContext = this;
		Intent intent = getIntent();
		uniqueCode = intent.getStringExtra("uniqueCode");
		initView();
		setStatusBar();
		loadData();
		receiver = new MyMusicBroadCastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("play");
		registerReceiver(receiver, filter);
		
	}
	/**
	 * 从网络加载数据
	 */
	private void loadData() {
       //查找		
		try {
			DbUtils db = DbUtils.create(mContext);
			user = db.findFirst(Selector.from(User.class).where("id", "=", "1"));
			userid = Constant.decode(Constant.key, user.getUserId());
			System.out.println("====文化年展列表详情==userid=》"+userid);
			db.close();
//			new CulturalExhibitionDetailWebAsyncTask(handler.obtainMessage(), userid, uniqueCode,mContext).execute(1);
//			String url = "http://192.168.253.9:8080/wenbo2/cultureHib.do"+"?method=cultureView&language="
					String url = Constant.DOMAIN+"/cultureHib.do?method=cultureView&language="
					+ SharedData.getInt("i18n", Language.ZH)
					+"&uniqueCode="
					+uniqueCode
					+ "&userid="
					+ URLEncoder.encode(Constant.encode(Constant.key, userid),
							"UTF-8");
					System.out.println("文化年展==="+url);
			webviewShow(url);
			new CulturalExhibitionDetailAsyncTask(detailHandler.obtainMessage(), userid, uniqueCode, mContext).execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 透明状态栏 
	 */
	public void setStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  
            Window window = getWindow();  
            // Translucent status bar  
            window.setFlags(  
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
        } else {
			view.setVisibility(View.GONE);
		}
	}
	
    public void webviewShow(String url) {
    	webView.loadUrl(url);// webView加载html片段
//    	webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
    	webView.setHorizontalScrollBarEnabled(false);//水平不显示 
    	webView.setVerticalScrollBarEnabled(false);//垂直不显示 
         
		webView.getSettings().setJavaScriptEnabled(true);//开启webview对JS的支持
		webView.getSettings().setSupportZoom(true);
        
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);

		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);

		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAllowContentAccess(true);
	}
	/**
     * 初始化view
     */
   private void initView() {
		view = findViewById(R.id.ced_view);
		iv_back = (ImageButton) findViewById(R.id.ced_goback);
		iv_back.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.ced_titletv);
		tv_title.setText(getLanguageString("展品介绍"));
		iv_icon = (ImageView) findViewById(R.id.ced_iv_icon);
		tv_name = (TextView) findViewById(R.id.ced_tv_name);
		linear_local = (LinearLayout) findViewById(R.id.ced_linear_location);
		linear_local.setOnClickListener(this);
		tv_hall = (TextView) findViewById(R.id.ced_tv_hall);
		play = (TextView) findViewById(R.id.ced_play);
		if (SharedData.getInt("i18n", Language.ZH) == 1) {//中文
			play.setBackgroundResource(R.drawable.btn_exhibition_stop);
		}else if (SharedData.getInt("i18n", Language.ZH) == 2) {//英文
			play.setBackgroundResource(R.drawable.btn_english_stop);
			
		}
//		play.setOnClickListener(this);
		tv_introduce = (TextView) findViewById(R.id.ced_tv_introduce);
//		linear_play = (LinearLayout) findViewById(R.id.ced_linear_play);
//		linear_play.setOnClickListener(this);
//		iv_play = (ImageView) findViewById(R.id.ced_iv_play);
		webView = (WebView) findViewById(R.id.ced_webview);
        
	}
    
	@Override
	protected void setI18nValue() {
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ced_goback:
			finish();
			break;
		case R.id.ced_linear_location:
			
//				Intent intent = new Intent();
//	            intent.putExtra("floor","1");
//	            intent.putExtra("exhibit","签到处");
//	            setResult(RESULT_OK,intent);
//	            finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//停止服务
		stopService(new Intent(mContext, PlayVoiceService.class));
		if (receiver !=null) {
			unregisterReceiver(receiver);
		}
	}
	class MyMusicBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("play")) {//接收到播放完毕
			
			// 接收广播处理
				if (SharedData.getInt("i18n", Language.ZH) == 1) {//中文
					Toast.makeText(mContext, "播放完毕!", Toast.LENGTH_LONG).show();
					play.setBackgroundResource(R.drawable.btn_exhibition_stop);
				}else if (SharedData.getInt("i18n", Language.ZH) == 2) {//英文
					Toast.makeText(mContext, "Play finished !", Toast.LENGTH_LONG).show();
					play.setBackgroundResource(R.drawable.btn_english_stop);
				} 
				FLAG_PLAY++;
				
				
//			// 弹出对话框
//			AlertDialog.Builder builder = new AlertDialog.Builder(
//					mContext);
//			builder.setTitle("音乐播放");
//			builder.setMessage(intent.getStringExtra("info"));
//			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// 如果确认,再次启动服务，做播放音乐：state：1
//					Intent intent = new Intent(mContext,
//							PlayVoiceService.class);
//					intent.putExtra("op", 1);
//					intent.putExtra("voiceurl", "http://www.baidu.com");
//					startService(intent);
//				}
//			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//                  				FLAG_PLAY++;
//                  				play.setBackgroundResource(R.drawable.btn_exhibition_stop);
//				}
//			}).create().show();
			
			
			}
			if (action.equals("empty")) {
				if (SharedData.getInt("i18n", Language.ZH) == 1) {//中文
					Toast.makeText(mContext, "音频文件为空!", Toast.LENGTH_LONG).show();
				}else if (SharedData.getInt("i18n", Language.ZH) == 2) {//英文
					Toast.makeText(mContext, "Audio file is empty !", Toast.LENGTH_LONG).show();	
				}
			}
			if (action.equals("voiceError")) {
				if (SharedData.getInt("i18n", Language.ZH) == 1) {//中文
					Toast.makeText(mContext, "音频源有误!", Toast.LENGTH_LONG).show();
				}else if (SharedData.getInt("i18n", Language.ZH) == 2) {//英文
					Toast.makeText(mContext, "Audio source error !", Toast.LENGTH_LONG).show();
				}
				System.out.println("========音频有误");
			}			

		}

	}
	//加载图片
	private void loadImage(String pic ,ImageView imageView) {
	   ImageLoader imageLoader = ImageLoader.getInstance();
	   DisplayImageOptions options = new DisplayImageOptions.Builder()
	                                .showStubImage(R.drawable.ic_launcher)
	                                .showImageForEmptyUri(R.drawable.ic_launcher)
	                                .showImageOnFail(R.drawable.ic_launcher)
	                                .cacheInMemory(true)
	                                .cacheOnDisc(true)
	                                .bitmapConfig(Bitmap.Config.RGB_565)
	                                .build();
	   imageLoader.displayImage(pic, imageView, options);
	}
/**
 * 加载html	
 */
//private Handler handler = new Handler(){
//	public void handleMessage(android.os.Message msg) {
//		switch (msg.what) {
//		case 1:
//			String result = (String) msg.obj;
////			webviewShow("http://fo.ifeng.com/a/20151101/41499716_0.shtml#p=1");
//			webviewShow(result);
//			break;
//
//		default:
//			break;
//		}
//	};
//};
private Handler detailHandler = new Handler(){
	public void handleMessage(android.os.Message msg) {
		switch (msg.what) {
		case 1:
			CulturalInfoBean culturalInfoBean = (CulturalInfoBean) msg.obj;
			 InfoBean infoBean = culturalInfoBean.getInfo();
			loadImage(Constant.DOMAIN+infoBean.getPic(),iv_icon);
			if (SharedData.getInt("i18n", Language.ZH) == 1) {
			
			tv_name.setText("名称:"+infoBean.getName());
			tv_hall.setText(infoBean.getAddress());
			tv_introduce.setText("简介:"+infoBean.getRemark());
			
			}else if (SharedData.getInt("i18n", Language.ZH) == 2) {
				tv_name.setText("Designation:"+infoBean.getName());
				tv_hall.setText(infoBean.getAddress());
				tv_introduce.setText("Summary:"+infoBean.getRemark());
			}
			final String voice = infoBean.getVoice();
//			final String url = "http://192.168.253.9:8080/wenbo2"+voice;
			final String url = Constant.DOMAIN+voice;
			play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FLAG_PLAY++;
					
					if (voice != null && !voice.equals("")) {
					
					Intent playIntent = new Intent(mContext, PlayVoiceService.class);
					if (FLAG_PLAY % 2 == 0) {
						if (SharedData.getInt("i18n", Language.ZH) == 1) {//中文
							play.setBackgroundResource(R.drawable.btn_exhibition_play);
						}else if (SharedData.getInt("i18n", Language.ZH) == 2) {//英文
							play.setBackgroundResource(R.drawable.btn_english_play);
							
						}
						playIntent.putExtra("op", 1);//播放
						
					}else {
						if (SharedData.getInt("i18n", Language.ZH) == 1) {
							play.setBackgroundResource(R.drawable.btn_exhibition_stop);
						}else if (SharedData.getInt("i18n", Language.ZH) == 2) {
							play.setBackgroundResource(R.drawable.btn_english_stop);
						}
						playIntent.putExtra("op", 2);//暂停
//						playIntent.putExtra("op", 3);//停止
					}
					playIntent.putExtra("voiceurl", url);
					startService(playIntent);
					
					}else {
						if (SharedData.getInt("i18n", Language.ZH) == 1) {//中文
							Toast.makeText(mContext, "音频文件为空!", Toast.LENGTH_LONG).show();
						}else if (SharedData.getInt("i18n", Language.ZH) == 2){//英文 2
							Toast.makeText(mContext, "Audio file is empty !", Toast.LENGTH_LONG).show();
						}
					}					
				}
			});
			break;

		default:
			break;
		}
		
	};

	
};

}
