package com.gheng.exhibit.view.map;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForShiNeiDingWeiShuJuRecyclerView;
import com.hebg3.mxy.utils.AsyncTaskForCheckMapVersion;
import com.hebg3.mxy.utils.AsyncTaskForDownLoadMapZip;
import com.hebg3.mxy.utils.ChangJingListPojo;
import com.hebg3.mxy.utils.CheckMapVersionPojo;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;

public class ShiNeiDaoHangActivity extends BaseActivity implements OnClickListener {

	public RelativeLayout zhuhuichangchosebuttonlayout;
	public RelativeLayout yanhuitingchosebuttonlayout;
	public RelativeLayout jiudianchosebuttonlayout;

	@ViewInject(R.id.zhuhuichanglogo)
	public ImageView zhuhuichanglogo;
	@ViewInject(R.id.yanhuitinglogo)
	public ImageView yanhuitinglogo;
	@ViewInject(R.id.jiudianlogo)
	public ImageView jiudianlogo;

	public ImageButton goback;

	public ArrayList<ChangJingListPojo> huichanglist = new ArrayList<ChangJingListPojo>();// 会场数据集合
	public ArrayList<ChangJingListPojo> yanhuitinglist = new ArrayList<ChangJingListPojo>();// 宴会厅数据集合
	public ArrayList<ChangJingListPojo> jiudianlist = new ArrayList<ChangJingListPojo>();// 酒店数据集合

	public ArrayList<ChangJingListPojo> changjinglist = new ArrayList<ChangJingListPojo>();// 场景数据集合
																							// adapter传这个对象，上面三个分别保存不同类型的场景数据，变更时只需要对这个集合操作即可
	public RecyclerView rv;
	public StaggeredGridLayoutManager sgl;
	public AdapterForShiNeiDingWeiShuJuRecyclerView adapter;
	public ImageButton search;

	@ViewInject(R.id.titletv)
	TextView titletv;

	@ViewInject(R.id.zhuhuichangtv)
	TextView zhuhuichangtv;

	@ViewInject(R.id.yanhuitingtv)
	TextView yanhuitingtv;

	@ViewInject(R.id.jiudiantv)
	TextView jiudiantv;

	SharedPreferences sp;
	Editor e;
	int mymapversion;
	int newmapversion;
	ProgressDialog pd;
	
	private Boolean isActive = true;
	
	Handler h=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what==1000){
				CheckMapVersionPojo pojo=(CheckMapVersionPojo)msg.obj;
				if(pojo.version>mymapversion){//有新版本
					newmapversion=pojo.version;
					showDialogDownloadNewApk(pojo.zipurl);
				}
			}
			if(msg.what==10000){//地图解压完毕
				if(pd!=null){
					pd.dismiss();
				}
				e.putInt("shineimapversion", newmapversion);
				e.apply();
				Toast.makeText(ShiNeiDaoHangActivity.this, getLanguageString("地图下载成功"), Toast.LENGTH_SHORT).show();
			}
			if(msg.what==-1){//下载失败
				if(pd!=null){
					pd.dismiss();
				}
				Toast.makeText(ShiNeiDaoHangActivity.this, getLanguageString("地图下载失败"), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chosechangguan);
		registerBoradcastReceiver();//注册监听

		zhuhuichangchosebuttonlayout = (RelativeLayout) findViewById(R.id.zhuhuichangchosebuttonlayout);
		yanhuitingchosebuttonlayout = (RelativeLayout) findViewById(R.id.yanhuitingchosebuttonlayout);
		jiudianchosebuttonlayout = (RelativeLayout) findViewById(R.id.jiudianchosebuttonlayout);

		goback = (ImageButton) findViewById(R.id.goback);
		goback.setOnClickListener(this);

		zhuhuichangchosebuttonlayout.setOnClickListener(this);
		yanhuitingchosebuttonlayout.setOnClickListener(this);
		jiudianchosebuttonlayout.setOnClickListener(this);

		search = (ImageButton) findViewById(R.id.search);
		search.setOnClickListener(this);

		rv = (RecyclerView) findViewById(R.id.changjinglist);
		sgl = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
		rv.setLayoutManager(sgl);
		rv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
				.color(this.getResources().getColor(R.color.shineidingweichangguanlistdivider))
				.size(getResources().getDimensionPixelSize(R.dimen.shineidingweichangguanlistdivider1))
				.margin(getResources().getDimensionPixelSize(R.dimen.shineidingweichangguanlistdivider1),
						getResources().getDimensionPixelSize(R.dimen.shineidingweichangguanlistdivider1))
				.build());
		
		setData();
		
		adapter = new AdapterForShiNeiDingWeiShuJuRecyclerView(this, changjinglist);
		rv.setAdapter(adapter);
		
		sp=this.getSharedPreferences("shineimapversion", Activity.MODE_PRIVATE);
		e=sp.edit();
		mymapversion=sp.getInt("shineimapversion", 1);
		
		jiudianchosebuttonlayout.performClick();//先显示酒店信息
		
		//判断是否有新地图zip
		if(IsWebCanBeUse.isWebCanBeUse(this)){
			AsyncTaskForCheckMapVersion at=new AsyncTaskForCheckMapVersion(h.obtainMessage(),this.getApplicationContext());
			at.execute(1);
		}
	}
	
	
	/**
	 * 初始化酒店信息
	 */
	public void setData(){
		//会场------------------------------------------------------------------------------
		ChangJingListPojo huichang1 = new ChangJingListPojo();
		huichang1.type = 0;
		huichang1.roomname = getLanguageString("华美宫");
		huichang1.roomnamezh = "华美宫";
		huichang1.roomid = "Waterside Resort";
		huichang1.drawableid=R.drawable.ic_launcher;
		huichang1.floor=3;
		huichanglist.add(huichang1);
		ChangJingListPojo huichang2 = new ChangJingListPojo();
		huichang2.type = 0;
		huichang2.roomname = getLanguageString("龙凤厅");
		huichang2.roomnamezh = "龙凤厅";
		huichang2.roomid = "Waterside Resort";
		huichang2.drawableid=R.drawable.ic_launcher;
		huichang2.floor=1;
		huichanglist.add(huichang2);
		ChangJingListPojo huichang3 = new ChangJingListPojo();
		huichang3.type = 0;
		huichang3.roomname = getLanguageString("宫音厅");
		huichang3.roomnamezh = "宫音厅";
		huichang3.roomid = "Waterside Resort";
		huichang3.drawableid=R.drawable.ic_launcher;
		huichang3.floor=3;
		huichanglist.add(huichang3);
		ChangJingListPojo huichang4 = new ChangJingListPojo();
		huichang4.type = 0;
		huichang4.roomname = getLanguageString("荣锦厅");
		huichang4.roomnamezh = "荣锦厅";
		huichang4.roomid = "Waterside Resort";
		huichang4.drawableid=R.drawable.ic_launcher;
		huichang4.floor=2;
		huichanglist.add(huichang4);
		ChangJingListPojo huichang5 = new ChangJingListPojo();
		huichang5.type = 0;
		huichang5.roomname = getLanguageString("国乐厅");
		huichang5.roomnamezh = "国乐厅";
		huichang5.roomid = "Tong An Hotel NO.1";
		huichang5.drawableid=R.drawable.ic_launcher;
		huichang5.floor=1;
		huichanglist.add(huichang5);

		//宴会厅------------------------------------------------------------------------------
		ChangJingListPojo yanhui1 = new ChangJingListPojo();
		yanhui1.type = 1;
		yanhui1.roomname = getLanguageString("龙凤厅");
		yanhui1.roomnamezh = "龙凤厅";
		yanhui1.roomid = "Waterside Resort";
		yanhui1.drawableid = R.drawable.ic_launcher;
		yanhui1.floor=1;
		yanhuitinglist.add(yanhui1);

		//酒店------------------------------------------------------------------------------
		
		ChangJingListPojo zhenshuijiudian = new ChangJingListPojo();
		zhenshuijiudian.type = 2;
		zhenshuijiudian.roomid = "Waterside Resort";
		zhenshuijiudian.roomname = getLanguageString("枕水度假酒店");
		zhenshuijiudian.drawableid=R.drawable.ic_launcher;
		
		ChangJingListPojo wangjinlijiudian = new ChangJingListPojo();
		wangjinlijiudian.type = 2;
		wangjinlijiudian.roomid = "Dockside Boutique Hotel";
		wangjinlijiudian.roomname = getLanguageString("望津里精品酒店");
		wangjinlijiudian.drawableid=R.drawable.ic_launcher;
		
		ChangJingListPojo hengyitangjiudian = new ChangJingListPojo();
		hengyitangjiudian.type = 2;
		hengyitangjiudian.roomid = "Wuzhen Clubhouse-Healtown";
		hengyitangjiudian.roomname = getLanguageString("乌镇行馆-恒益堂");
		hengyitangjiudian.drawableid=R.drawable.ic_launcher;
		
		ChangJingListPojo jintangjiudian = new ChangJingListPojo();
		jintangjiudian.type = 2;
		jintangjiudian.roomid = "Wuzhen Clubhouse-Splendid Clubhouse";
		jintangjiudian.roomname = getLanguageString("乌镇行馆-锦堂");
		jintangjiudian.drawableid=R.drawable.ic_launcher;
		
		ChangJingListPojo shengtingjiudian = new ChangJingListPojo();
		shengtingjiudian.type = 2;
		shengtingjiudian.roomid = "Wuzhen Clubhouse-Shinetown";
		shengtingjiudian.roomname = getLanguageString("乌镇行馆-盛庭");
		shengtingjiudian.drawableid=R.drawable.ic_launcher;
		
		ChangJingListPojo yiyuanjiudian = new ChangJingListPojo();
		yiyuanjiudian.type = 2;
		yiyuanjiudian.roomid = "Eden Club House";
		yiyuanjiudian.roomname = getLanguageString("宜园精品酒店");
		yiyuanjiudian.drawableid=R.drawable.ic_launcher;
		
		ChangJingListPojo tongan1 = new ChangJingListPojo();
		tongan1.type = 2;
		tongan1.roomid = "Tong An Hotel NO.1";
		tongan1.roomname = getLanguageString("通安1号楼");
		tongan1.drawableid=R.drawable.ic_launcher;

		ChangJingListPojo tongan2 = new ChangJingListPojo();
		tongan2.type = 2;
		tongan2.roomid = "Tong An Hotel NO.2";
		tongan2.roomname = getLanguageString("通安2号楼");
		tongan2.drawableid=R.drawable.ic_launcher;
		
		ChangJingListPojo tonganguibinlou = new ChangJingListPojo();
		tonganguibinlou.type = 2;
		tonganguibinlou.roomid = "Tong An Hotel VIP Tower";
		tonganguibinlou.roomname = getLanguageString("通安贵宾楼");
		tonganguibinlou.drawableid=R.drawable.ic_launcher;

		
		jiudianlist.add(zhenshuijiudian);
		jiudianlist.add(wangjinlijiudian);
		jiudianlist.add(hengyitangjiudian);
		jiudianlist.add(jintangjiudian);
		jiudianlist.add(shengtingjiudian);
		jiudianlist.add(yiyuanjiudian);
		jiudianlist.add(tongan1);
		jiudianlist.add(tongan2);
		jiudianlist.add(tonganguibinlou);
		// 以上 测试数据
	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		titletv.setText(getLanguageString("场馆线路规划"));
		zhuhuichangtv.setText(getLanguageString("会场"));
		yanhuitingtv.setText(getLanguageString("宴会厅"));
		jiudiantv.setText(getLanguageString("酒店"));

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == goback) {
			this.finish();
		}
		if (v == search) {// 搜索定位
			startTo(ShiNeiDaoHangSearchActivity.class);
		}
		if (v == zhuhuichangchosebuttonlayout) {// 会场
			changjinglist.clear();
			changjinglist.addAll(huichanglist);
			adapter.notifyDataSetChanged();
			
			zhuhuichangtv.setBackgroundDrawable(getResources().getDrawable(R.drawable.lanjiantoubg));
			yanhuitingtv.setBackgroundColor(getResources().getColor(R.color.white));
			jiudiantv.setBackgroundColor(getResources().getColor(R.color.white));
			
			zhuhuichangtv.setTextColor(this.getResources().getColor(R.color.white));
			yanhuitingtv.setTextColor(this.getResources().getColor(R.color.text_details));
			jiudiantv.setTextColor(this.getResources().getColor(R.color.text_details));
		}
		if (v == yanhuitingchosebuttonlayout) {// 宴会厅
			changjinglist.clear();
			changjinglist.addAll(yanhuitinglist);
			adapter.notifyDataSetChanged();
			
			zhuhuichangtv.setBackgroundColor(getResources().getColor(R.color.white));
			yanhuitingtv.setBackgroundDrawable(getResources().getDrawable(R.drawable.lanjiantoubg));
			jiudiantv.setBackgroundColor(getResources().getColor(R.color.white));
			
			zhuhuichangtv.setTextColor(this.getResources().getColor(R.color.text_details));
			yanhuitingtv.setTextColor(this.getResources().getColor(R.color.white));
			jiudiantv.setTextColor(this.getResources().getColor(R.color.text_details));
		}
		if (v == jiudianchosebuttonlayout) {// 酒店
			changjinglist.clear();
			changjinglist.addAll(jiudianlist);
			adapter.notifyDataSetChanged();
			
			zhuhuichangtv.setBackgroundColor(getResources().getColor(R.color.white));
			yanhuitingtv.setBackgroundColor(getResources().getColor(R.color.white));
			jiudiantv.setBackgroundDrawable(getResources().getDrawable(R.drawable.lanjiantoubg));
			
			zhuhuichangtv.setTextColor(this.getResources().getColor(R.color.text_details));
			yanhuitingtv.setTextColor(this.getResources().getColor(R.color.text_details));
			jiudiantv.setTextColor(this.getResources().getColor(R.color.white));
		}
	}
	
	
	/**
	 * 如果发现新版本，调用本方法询问用户是否下载
	 */
	public void showDialogDownloadNewApk(final String downloadurl){
		new AlertDialog.Builder(context)
		.setTitle(getLanguageString("有新地图")+"!")
		.setMessage(getLanguageString("现在下载吗")+"?")
		.setPositiveButton(getLanguageString("确定"),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						if(!IsWebCanBeUse.isWebCanBeUse(getApplicationContext())){
							Toast.makeText(ShiNeiDaoHangActivity.this, getLanguageString("网络不给力"), Toast.LENGTH_SHORT).show();
						}else{
							//启动线程下载地图zip文件
							pd = ProgressDialog.show(ShiNeiDaoHangActivity.this, "", "Downloading..."); 
							pd.setCancelable(true);
							pd.setCanceledOnTouchOutside(false);
							AsyncTaskForDownLoadMapZip at=new AsyncTaskForDownLoadMapZip(h.obtainMessage(),downloadurl,getApplicationContext());
							at.execute(1);
						}
					}
				})
		.setNegativeButton(getLanguageString("取消"),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						
					}
				}).show();
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
