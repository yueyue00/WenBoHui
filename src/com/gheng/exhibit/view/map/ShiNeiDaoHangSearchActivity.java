package com.gheng.exhibit.view.map;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForShiNeiDingWeiSearchChuangGuanRv;
import com.gheng.exhibit.view.adapter.AdapterForShiNeiDingWeiSearchSheShiRv;
import com.hebg3.mxy.utils.ChangJingListPojo;
import com.hebg3.mxy.utils.SheShiListPojo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;

public class ShiNeiDaoHangSearchActivity extends BaseActivity implements OnClickListener{
	
   
	int zhuhuichangrbid;
	int yanhuitingrbid;
	int jiudianrbid;
	ImageButton goback;
	
	private Boolean isActive = true;
	
	public RecyclerView changjinglistrv;//场景数据展示rv		
	public RecyclerView sheshilistrv;//设施数据展示rv
	
	public StaggeredGridLayoutManager changguansgl;//场景rv 布局管理器   管理器只能一对一
	public StaggeredGridLayoutManager sheshisgl;//设施rv 布局管理器   管理器只能一对一
		
	public ArrayList<ChangJingListPojo> changjinglist=new ArrayList<ChangJingListPojo>();//场景数据集合 adapter传这个对象，上面三个分别保存不同类型的场景数据，变更时只需要对这个集合操作即可
	public AdapterForShiNeiDingWeiSearchChuangGuanRv changjingadapter;//场景Rv adapter
	
	public ArrayList<SheShiListPojo> sheshilist=new ArrayList<SheShiListPojo>();//设施数据集合 adapter传这个对象 数据有变更，对这个对象进行操作
	public AdapterForShiNeiDingWeiSearchSheShiRv sheshiadapter;
	@ViewInject(R.id.titletv)
	TextView titletv;
	@ViewInject(R.id.xuanzechangguantv)
	TextView xuanzechangguantv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dingweidaohangsearch);
		registerBoradcastReceiver();//注册广播
		titletv.setText(getLanguageString("场馆设施搜索"));
		xuanzechangguantv.setText(getLanguageString("选择场馆")+":");
		
		goback=(ImageButton)findViewById(R.id.goback);
		goback.setOnClickListener(this);
				
		changguansgl =new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
		sheshisgl =new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
		
		
		setTestData();//填入测试数据
		
		//-----场景数据ListRecyclerView
		changjinglistrv=(RecyclerView)findViewById(R.id.changjinglist);
		changjinglistrv.setLayoutManager(changguansgl);
		changjinglistrv.addItemDecoration(
		new HorizontalDividerItemDecoration.Builder(this)
		.color(this.getResources().getColor(R.color.shineidingweichangguanlistdivider))
		.size(getResources().getDimensionPixelSize(R.dimen.shineidingweichangguanlistdivider2))
		.margin(getResources().getDimensionPixelSize(R.dimen.recylerviewitemdivider2),getResources().getDimensionPixelSize(R.dimen.recylerviewitemdivider3))
		.build());		
		changjingadapter=new AdapterForShiNeiDingWeiSearchChuangGuanRv(this,changjinglist);
		changjinglistrv.setAdapter(changjingadapter);
		
		//-----设施数据ListRecyclerView
		sheshilistrv=(RecyclerView)findViewById(R.id.sheshilist);
		sheshilistrv.setLayoutManager(sheshisgl);
		sheshiadapter=new AdapterForShiNeiDingWeiSearchSheShiRv(this, sheshilist);
		sheshilistrv.setAdapter(sheshiadapter);

	}
	
	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==goback){
			this.finish();
		}
	}
	
	
	/**
	 * 测试数据 设置
	 */
	public void setTestData(){

		//场景数据
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

		
		changjinglist.add(zhenshuijiudian);
		changjinglist.add(wangjinlijiudian);
		changjinglist.add(hengyitangjiudian);
		changjinglist.add(jintangjiudian);
		changjinglist.add(shengtingjiudian);
		changjinglist.add(yiyuanjiudian);
		changjinglist.add(tongan1);
		changjinglist.add(tongan2);
		changjinglist.add(tonganguibinlou);
		
		
		//设施数据
		SheShiListPojo huichangsheshi1=new SheShiListPojo();
		huichangsheshi1.type=0;
		huichangsheshi1.sheshiname=getLanguageString("服务台");
		huichangsheshi1.sheshinamezh="服务台";
		huichangsheshi1.drawableid=R.drawable.mappoiqiantai;
		SheShiListPojo huichangsheshi2=new SheShiListPojo();
		huichangsheshi2.type=0;
		huichangsheshi2.sheshiname=getLanguageString("步行梯");
		huichangsheshi2.sheshinamezh="步行梯";
		huichangsheshi2.drawableid=R.drawable.mappoibuxingti;
		SheShiListPojo huichangsheshi3=new SheShiListPojo();
		huichangsheshi3.type=0;
		huichangsheshi3.sheshiname=getLanguageString("扶梯");
		huichangsheshi3.sheshinamezh="扶梯";
		huichangsheshi3.drawableid=R.drawable.mappoifuti;
		SheShiListPojo huichangsheshi4=new SheShiListPojo();
		huichangsheshi4.type=0;
		huichangsheshi4.sheshiname=getLanguageString("直梯");
		huichangsheshi4.sheshinamezh="直梯";
		huichangsheshi4.drawableid=R.drawable.mappoizhiti;
		SheShiListPojo huichangsheshi5=new SheShiListPojo();
		huichangsheshi5.type=0;
		huichangsheshi5.sheshiname=getLanguageString("餐厅");
		huichangsheshi5.sheshinamezh="餐厅";
		huichangsheshi5.drawableid=R.drawable.mappoicanting;
		SheShiListPojo huichangsheshi6=new SheShiListPojo();
		huichangsheshi6.type=0;
		huichangsheshi6.sheshiname=getLanguageString("卫生间");
		huichangsheshi6.sheshinamezh="卫生间";
		huichangsheshi6.drawableid=R.drawable.mappoiwc;
		SheShiListPojo huichangsheshi7=new SheShiListPojo();
		huichangsheshi7.type=0;
		huichangsheshi7.sheshiname=getLanguageString("会议室");
		huichangsheshi7.sheshinamezh="会议室";
		huichangsheshi7.drawableid=R.drawable.mappoihuiyishi;
		SheShiListPojo huichangsheshi8=new SheShiListPojo();
		huichangsheshi8.type=0;
		huichangsheshi8.sheshiname=getLanguageString("出入口");
		huichangsheshi8.sheshinamezh="出入口";
		huichangsheshi8.drawableid=R.drawable.mappoiexit;
		
		sheshilist.add(huichangsheshi1);
		sheshilist.add(huichangsheshi2);
		sheshilist.add(huichangsheshi3);
		sheshilist.add(huichangsheshi4);
		sheshilist.add(huichangsheshi5);
		sheshilist.add(huichangsheshi6);
		sheshilist.add(huichangsheshi7);
		sheshilist.add(huichangsheshi8);

		//以上 设施测试数据
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
