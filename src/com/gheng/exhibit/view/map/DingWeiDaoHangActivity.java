package com.gheng.exhibit.view.map;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.DipUtils;
import com.gheng.exhibit.utils.MapHelper;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForChoseFloorPopWindowRv;
import com.gheng.indoormap.GeoPoint;
import com.gheng.indoormap.MapPoi;
import com.gheng.indoormap.MapView;
import com.gheng.indoormap.MapView.InnerPoiSize;
import com.gheng.indoormap.MapView.OnMapFloorChangeListener;
import com.gheng.indoormap.MapView.OnMapOpenEndListener;
import com.gheng.indoormap.MapView.OnMapViewClickListener;
import com.gheng.indoormap.Overlay.OnMapPoiClickListener;
import com.gheng.indoormap.result.FeatureResult;
import com.gheng.indoormap.result.FloorResult;
import com.gheng.indoormap.result.PoiResult;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;


public class DingWeiDaoHangActivity extends BaseActivity implements OnClickListener,OnMapOpenEndListener,OnMapFloorChangeListener{
	
	@ViewInject(R.id.titletv)
	TextView titletv;
	
	@ViewInject(R.id.chosebeginpointtv)
	TextView chosebeginpointtv;
	
	@ViewInject(R.id.choseendpointtv)
	TextView choseendpointtv;
	
	@ViewInject(R.id.mapview)
	MapView mapView;
	
	private Boolean isActive = true;
	
	ImageButton goback;
	RelativeLayout chosebeginpointlayout;
	RelativeLayout choseendpointlayout;
	ImageView searchmapbutton;
	
	String openmappathid="Waterside Resort";//要打开的地图的文件夹名称  枕水酒店
	public int openfloornum=1;//默认打开楼层 用户切换地图后要变更这个值
	String searchpoiid="";//用户要搜索poi的id 
	String searchspacename="";//用户要搜索平面的名称
	int selectMode = 1;// 1选择起点 2选择终点
	Bitmap poiIcon;//坐标点标示图标

	@ViewInject(R.id.endpointname)
	TextView endpointnametv;//导航 终点名称
	@ViewInject(R.id.startpointname)
	TextView startpointnametv;//导航 起点名称
	ArrayList<FloorResult> floors=new ArrayList<FloorResult>();//保存地图楼层信息
	GeoPoint startPoint,endPoint;//起始坐标，结束坐标
	GeoPoint point;//用户点击地图时，获取的节点信息
	@ViewInject(R.id.resetmapbutton)
	Button resetmapbutton;//地图重置按钮
	@ViewInject(R.id.begindaohangbutton)
	Button begindaohangbutton;//开始导航按钮
	
	ArrayList<PoiResult> searchPOI=new ArrayList<PoiResult>();//根据查询结果获取的节点数据
	int startpointfloor=1;//起始点楼层
	int endpointfloor=1;//结束点楼层
	
	@ViewInject(R.id.showfloorslayout)
	RelativeLayout showfloorslayout;//楼层布局
	@ViewInject(R.id.chosefloorrv)
	RecyclerView floorsrv;//楼层rv
	AdapterForChoseFloorPopWindowRv floorsadapter;
	LinearLayoutManager floorsllm;
	PopupWindow pop;// 点击poi弹出窗口
	TextView setbeginpointtv;//设为起点tv
	TextView setendpointtv;//设为终点tv
	ImageButton closemaptishi;
	
	@ViewInject(R.id.maptishibutton)
	ImageButton maptishibutton;//打开地图提示按钮
	
	//判断传进来的地图id是否正确
	String hotelids="Dockside Boutique Hotel;Wuzhen Clubhouse-Healtown;Wuzhen Clubhouse-Shinetown;Wuzhen Clubhouse-Splendid Clubhouse;Waterside Resort;Eden Club House;Tong An Hotel VIP Tower;Tong An Hotel NO.1;Tong An Hotel NO.2";
	
	Handler h=new Handler();//消息管理器
	
	SharedPreferences sp;
	Editor e;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dingweidaolan);
		registerBoradcastReceiver();//注册广播
		
		//英文模式下截取-后面的文字
		String titletv=getLanguageString(getIntent().getStringExtra("titletv"));
		int isen=titletv.lastIndexOf("-");
		if(isen!=-1&&SharedData.getInt("i18n", Language.ZH)==2){
			this.titletv.setText(titletv.substring(isen+1));
		}else{
			this.titletv.setText(titletv);
		}
		
		maptishibutton.setOnClickListener(this);
		
		goback=(ImageButton)findViewById(R.id.goback);
		goback.setOnClickListener(this);
		
		chosebeginpointlayout=(RelativeLayout)findViewById(R.id.chosebeginpointbuttonlayout);
		choseendpointlayout=(RelativeLayout)findViewById(R.id.choseendpointbuttonlayout);

		begindaohangbutton.setOnClickListener(this);
		resetmapbutton.setOnClickListener(this);
		
		chosebeginpointlayout.setOnClickListener(this);
		choseendpointlayout.setOnClickListener(this);
		
		searchmapbutton=(ImageView)findViewById(R.id.searchbutton);
		searchmapbutton.setOnClickListener(this);
		
		begindaohangbutton.setText(getLanguageString("开始线路规划"));
		resetmapbutton.setText(getLanguageString("重新选择"));
		
		//下面开始 对地图操作 先判断将要打开的地图信息
		if(getIntent().getStringExtra("openmappathid")!=null&&!getIntent().getStringExtra("openmappathid").equals("")){//要打开那个地图
			openmappathid=getIntent().getStringExtra("openmappathid");
			if(!hotelids.contains(openmappathid)){
				Toast.makeText(DingWeiDaoHangActivity.this,getLanguageString("无效的酒店id"), Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		}
		if(getIntent().getIntExtra("openfloornum", 1)!=1){//要打开map的第几层
			openfloornum=getIntent().getIntExtra("openfloornum", 1);
		}
		
		ProgressTools.showDialog(this);//开始加载地图
		poiIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.marker_red)).getBitmap();
		int lg = SharedData.getInt("i18n", Language.ZH);
		if (lg == Language.ZH) {
			mapView.setEN(false);
		} else {
			mapView.setEN(true);
		}
		mapView.refresh();
		mapView.setInnerPoiSize(InnerPoiSize.MIDDLE);//设置节点图标大小
		mapView.setOnMapOpenEndListener(this);
		mapView.setOnMapViewClickListener(onMapViewClickListener);//地图点击监听
		
		mapView.openPath(MapHelper.getMapFloorPath(openmappathid));//打开哪一个文件夹下的地图
		
		mapView.getMapOverlay().setOnMapPoiClickListener(new OnMapPoiClickListener() {//设置地图覆盖物点击事件点击事件
			@Override
			public boolean onClick(MapPoi mp) {
				point=mp.geoPt;
				showPopup();
				return false;
			}
		});
		
		floors.addAll((ArrayList<FloorResult>)mapView.getFloors());// 获取当前地图的楼层信息 这句话在openPath之后
		mapView.getController().setFloor(openfloornum);//默认打开地图首层
		mapView.setOnMapFloorChangeListener(this);//楼层变更事件  导航切换楼层
		//判断是否搜索space
		if(getIntent().getStringExtra("searchspacename")!=null&&!getIntent().getStringExtra("searchspacename").equals("")){//搜索的space的名称
			searchspacename=getIntent().getStringExtra("searchspacename");
			ArrayList<Map<String,Object>> spaces=(ArrayList<Map<String,Object>>)mapView.searchSpaceWithKey(searchspacename);
			if(spaces.size()>0){
				ArrayList<MapPoi> pois = new ArrayList<MapPoi>();
				MapPoi poi = new MapPoi();
				poi.center = false;
				poi.floorIndex = openfloornum;
				poi.icon = poiIcon;
				int x=0;
				int y=0;
				x=(Integer)spaces.get(0).get("x");
				y=(Integer)spaces.get(0).get("y");
				
				if(x!=0&&y!=0){//两个值都不为0
					poi.geoPt = new GeoPoint(x,y);
					pois.add(poi);
				}
				mapView.getMapOverlay().setMapPois(pois);// 如果搜索到数据，再显示
				mapView.getController().setCenter(poi.geoPt);// 点击地图某一地点，将被点击的点作为中心点
				mapView.getController().setZoom(6);
				mapView.refresh();
			}else{
				if(spaces.size()<1){//没有搜索记录
					Toast.makeText(DingWeiDaoHangActivity.this,getLanguageString("搜索位置不存在"), Toast.LENGTH_SHORT).show();
				}
			}
		}else{//如果是点击会场或宴会厅进来，那么久把space居中并设置特定的zoom，如果没有，就zoomToExtent()
			mapView.getController().zoomToExtent();//将地图铺满当前地图控件
		}
		
		//最后判断是否有搜索内容
		if(getIntent().getStringExtra("searchpoiid")!=null){//搜索的poi的名称
			searchpoiid=getIntent().getStringExtra("searchpoiid");
			if(searchpoiid.equals("餐厅")||searchpoiid.equals("会议室")){//餐厅或会议室搜索space 其他搜索poi
				ArrayList<Map<String,Object>> spaces=(ArrayList<Map<String,Object>>)mapView.searchSpaceWithKey(searchpoiid);
				if(spaces.size()>0){
					ArrayList<MapPoi> pois = new ArrayList<MapPoi>();
					for(int i=0;i<spaces.size();i++){
						MapPoi poi = new MapPoi();
						poi.center = false;
						poi.floorIndex = openfloornum;
						poi.icon = poiIcon;
						int x=0;
						int y=0;
						x=(Integer)spaces.get(i).get("x");
						y=(Integer)spaces.get(i).get("y");
						
						if(x!=0&&y!=0){//两个值都不为0
							poi.geoPt = new GeoPoint(x,y);
							pois.add(poi);
						}
					}
					mapView.getMapOverlay().setMapPois(pois);// 如果搜索到数据，再显示
					mapView.refresh();
				}else{
					Toast.makeText(DingWeiDaoHangActivity.this, getLanguageString("搜索位置不存在"), Toast.LENGTH_SHORT).show();
				}
			}else{
				// 搜索
				if (((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid)) != null&& ((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid)).size() > 0) {
					searchPOI.addAll((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid));
					ArrayList<MapPoi> pois = new ArrayList<MapPoi>();
					for (PoiResult poiResult : searchPOI) {
						MapPoi poi = new MapPoi();
						poi.center = false;
						poi.geoPt = poiResult.gp;
						poi.floorIndex = poiResult.floorIndex;
						poi.icon = poiIcon;
						pois.add(poi);
					}
					mapView.getMapOverlay().setMapPois(pois);// 如果搜索到数据，再显示
					mapView.refresh();
				}else{
					Toast.makeText(DingWeiDaoHangActivity.this, getLanguageString("搜索位置不存在"), Toast.LENGTH_SHORT).show();
				}
			}
		}
		//楼层展示
		floorsllm=new LinearLayoutManager(this);
		floorsllm.setOrientation(LinearLayoutManager.VERTICAL);
		floorsrv.setLayoutManager(floorsllm);
		floorsrv.setHasFixedSize(true);// 强制item高度一致，加强加载效率
		floorsrv.addItemDecoration(// 为RecyclerView添加divider
		new HorizontalDividerItemDecoration.Builder(this)
				.color(this.getResources().getColor(
						R.color.xiaoxizhongxinrecyclerviewdivider))
				.size(getResources().getDimensionPixelSize(
						R.dimen.recylerviewitemdivider1))
				.margin(getResources().getDimensionPixelSize(
						R.dimen.recylerviewitemdivider2),
						getResources().getDimensionPixelSize(
								R.dimen.recylerviewitemdivider3)).build());
		floorsadapter=new AdapterForChoseFloorPopWindowRv(this,floors);
		floorsrv.setAdapter(floorsadapter);
		LayoutParams lp=showfloorslayout.getLayoutParams();
		lp.height=dip2px(this, floors.size()*36);
		showfloorslayout.setLayoutParams(lp);
		
		sp=this.getSharedPreferences("maptishi", Activity.MODE_PRIVATE);
		e=sp.edit();
		if(sp.getInt("first", 0)==0){
			e.putInt("first", 1);
			e.apply();
			maptishibutton.performClick();
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Override  //当前activity lunchmode="singletask",并已经启动，在另外一个界面再次start当前activity时 会执行onNewIntent方法
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		//英文模式下截取-后面的文字
		String titletv=getLanguageString(intent.getStringExtra("titletv"));
		int isen=titletv.lastIndexOf("-");
		if(isen!=-1&&SharedData.getInt("i18n", Language.ZH)==2){
			this.titletv.setText(titletv.substring(isen+1));
		}else{
			this.titletv.setText(titletv);
		}
		
		resetmapbutton.performClick();
		openfloornum=1;
		//下面开始 对地图操作 先判断将要打开的地图信息
		if(intent.getStringExtra("openmappathid")!=null&&!intent.getStringExtra("openmappathid").equals("")){//要打开那个地图
			openmappathid=intent.getStringExtra("openmappathid");
		}
		ProgressTools.showDialog(this);//开始加载地图
		poiIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.marker_red)).getBitmap();
		mapView.setInnerPoiSize(InnerPoiSize.MIDDLE);//设置节点图标大小
		mapView.setOnMapOpenEndListener(this);
		mapView.setOnMapViewClickListener(onMapViewClickListener);//地图点击监听
		int lg = SharedData.getInt("i18n", Language.ZH);
		if (lg == Language.ZH) {
			mapView.setEN(false);
		} else {
			mapView.setEN(true);
		}
		mapView.openPath(MapHelper.getMapFloorPath(openmappathid));//打开哪一个文件夹下的地图
		
		floors.clear();
		floors.addAll((ArrayList<FloorResult>)mapView.getFloors());// 获取当前地图的楼层信息 这句话在openPath之后
		floorsadapter.notifyDataSetChanged();
		LayoutParams lp=showfloorslayout.getLayoutParams();
		lp.height=dip2px(this, floors.size()*40);
		showfloorslayout.setLayoutParams(lp);
		mapView.getController().setFloor(openfloornum);//默认打开地图1层，这里不能用openfloornum，因为如果是酒店，那么不传openfloornum，之前的楼层如果和要打开的场景楼层不一样，会报错，例如枕水是3层，其他酒店都是2层
		mapView.getController().zoomToExtent();//将地图铺满当前地图控件
		
		
		//最后判断是否有搜索内容
		if(intent.getStringExtra("searchpoiid")!=null){//搜索的poi的名称
			searchpoiid=intent.getStringExtra("searchpoiid");
			if(searchpoiid.equals("餐厅")||searchpoiid.equals("会议室")){//餐厅或会议室搜索space 其他搜索poi
				ArrayList<Map<String,Object>> spaces=(ArrayList<Map<String,Object>>)mapView.searchSpaceWithKey(searchpoiid);
				if(spaces.size()>0){
					ArrayList<MapPoi> pois = new ArrayList<MapPoi>();
					for(int i=0;i<spaces.size();i++){
						MapPoi poi = new MapPoi();
						poi.center = false;
						poi.floorIndex = openfloornum;
						poi.icon = poiIcon;
						int x=0;
						int y=0;
						x=(Integer)spaces.get(i).get("x");
						y=(Integer)spaces.get(i).get("y");
						
						if(x!=0&&y!=0){//两个值都不为0
							poi.geoPt = new GeoPoint(x,y);
							pois.add(poi);
						}
					}
					mapView.getMapOverlay().setMapPois(pois);// 如果搜索到数据，再显示
					mapView.refresh();
				}else{
					Toast.makeText(DingWeiDaoHangActivity.this, getLanguageString("搜索位置不存在"), Toast.LENGTH_SHORT).show();
				}
			}else{
				// 搜索
				if (((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid)) != null&& ((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid)).size() > 0) {
					searchPOI.addAll((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid));
					ArrayList<MapPoi> pois = new ArrayList<MapPoi>();
					for (PoiResult poiResult : searchPOI) {
						MapPoi poi = new MapPoi();
						poi.center = false;
						poi.geoPt = poiResult.gp;
						poi.floorIndex = poiResult.floorIndex;
						poi.icon = poiIcon;
						pois.add(poi);
					}
					mapView.getMapOverlay().setMapPois(pois);// 如果搜索到数据，再显示
					mapView.refresh();
				}else{
					Toast.makeText(DingWeiDaoHangActivity.this, getLanguageString("搜索位置不存在"), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	/**
	 * 当用户搜索之后，再切换楼层，之前搜索的内容仍然显示
	 */
	public void changeFloorWithPois(int floorIndex){
		openfloornum=floorIndex;
		mapView.getController().setFloor(openfloornum);
		mapView.getController().zoomToExtent();
		mapView.refresh();
		// 搜索
		searchPOI.clear();//切换楼层，先清空poi数据，避免将旧搜索数据显示出来
		if(searchpoiid.equals("")){//如果没有搜索poi，返回
			return;
		}
		
		if(searchpoiid.equals("餐厅")||searchpoiid.equals("会议室")){//餐厅或会议室搜索space 其他搜索poi
			ArrayList<Map<String,Object>> spaces=(ArrayList<Map<String,Object>>)mapView.searchSpaceWithKey(searchpoiid);
			if(spaces.size()>0){
				ArrayList<MapPoi> pois = new ArrayList<MapPoi>();
				for(int i=0;i<spaces.size();i++){
					MapPoi poi = new MapPoi();
					poi.center = false;
					poi.floorIndex = openfloornum;
					poi.icon = poiIcon;
					int x=0;
					int y=0;
					x=(Integer)spaces.get(i).get("x");
					y=(Integer)spaces.get(i).get("y");
					
					if(x!=0&&y!=0){//两个值都不为0
						poi.geoPt = new GeoPoint(x,y);
						pois.add(poi);
					}
				}
				mapView.getMapOverlay().setMapPois(pois);// 如果搜索到数据，再显示
				mapView.refresh();
			}
		}else{
			// 搜索
			if (((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid)) != null&& ((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid)).size() > 0) {
				searchPOI.addAll((ArrayList<PoiResult>) mapView.searchPOI(searchpoiid));
				ArrayList<MapPoi> pois = new ArrayList<MapPoi>();
				for (PoiResult poiResult : searchPOI) {
					MapPoi poi = new MapPoi();
					poi.center = false;
					poi.geoPt = poiResult.gp;
					poi.floorIndex = poiResult.floorIndex;
					poi.icon = poiIcon;
					pois.add(poi);
				}
				mapView.getMapOverlay().setMapPois(pois);// 如果搜索到数据，再显示
				mapView.refresh();
			}
		}
	}
	
	/**
	 * 地图点击事件
	 */
	public OnMapViewClickListener onMapViewClickListener = new OnMapViewClickListener() {
		@Override
		public boolean onClick(GeoPoint gp) {
			FeatureResult result = mapView.selectClicked(gp);// 选择空间的面（方块）
			point = gp;
			
			//点击的是poi+space节点
			if (result.type == FeatureResult.TYPE_ALL) {
				String realname=getLanguageString("已选择");
				String poiName = MapHelper.getPoiName(result.poiResult.name);//获取节点名称
				String spaceName = MapHelper.getSpaceNames(result.spaceResult.name);//获取space名称
				
				if(poiName!=null){
					realname=poiName;
				}else if(spaceName!=null){
					realname=spaceName;
				}
				
				showPopup();
				if (selectMode == 1) {// 选择起点
					startpointnametv.setText(realname);
					startPoint=point;
					startpointfloor=openfloornum;
					mapView.getMapOverlay().setStartPoint(startPoint);
					mapView.refresh();
					selectMode=2;
				}
				else{// 选择终点
					endpointnametv.setText(realname);
					endPoint=point;
					endpointfloor=openfloornum;
					mapView.getMapOverlay().setEndPoint(endPoint);
					mapView.refresh();
					selectMode=1;
				}
			}
			
			//点击的是poi节点
			if (result.type == FeatureResult.TYPE_POI) {		
				showPopup();
				if (selectMode == 1) {// 选择起点
					String poiName = MapHelper.getPoiName(result.poiResult.name);//获取节点名称
					if(poiName==null||poiName.equals("")){
						startpointnametv.setText(getLanguageString("已选择"));
					}else{
						startpointnametv.setText(poiName);
					}
					startPoint=point;
					startpointfloor=openfloornum;
					mapView.getMapOverlay().setStartPoint(startPoint);
					mapView.refresh();
					selectMode=2;
				}
				else {// 选择终点
					String poiName = MapHelper.getPoiName(result.poiResult.name);//获取节点名称
					if(poiName==null||poiName.equals("")){
						endpointnametv.setText(getLanguageString("已选择"));
					}else{
						endpointnametv.setText(poiName);
					}
					endPoint=point;
					endpointfloor=openfloornum;
					mapView.getMapOverlay().setEndPoint(endPoint);
					mapView.refresh();
					selectMode=1;
				}
			}
			
			
			
			//点击的是面
			if (result.type == FeatureResult.TYPE_SPACE) {
				// mapView.getController().setCenter(gp);//点击地图 某一地点，将被点击的点作为中心点
				
				if (selectMode == 1) {// 选择起点
					String spaceName = MapHelper.getSpaceNames(result.spaceResult.name);// 获取位置信息
					if(spaceName==null||spaceName.equals("")){
						startpointnametv.setText(getLanguageString("已选择"));
					}else{
						startpointnametv.setText(spaceName);
					}
					startPoint=point;
					startpointfloor=openfloornum;
					mapView.getMapOverlay().setStartPoint(startPoint);
					mapView.refresh();
					selectMode=2;
				}
				else{// 选择终点
					String spaceName = MapHelper.getSpaceNames(result.spaceResult.name);// 获取位置信息
					if(spaceName==null||spaceName.equals("")){
						endpointnametv.setText(getLanguageString("已选择"));
					}else{
						endpointnametv.setText(spaceName);
					}
					endPoint=point;
					endpointfloor=openfloornum;
					mapView.getMapOverlay().setEndPoint(endPoint);
					mapView.refresh();
					selectMode=1;
				}
			}
			return false;
		}
	};
	
	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		chosebeginpointtv.setText(getLanguageString("选择起点")+":");
		choseendpointtv.setText(getLanguageString("选择终点")+":");		
	}
	
	/**
	 * 打开地图提示操作窗口
	 */
	public void showMapTiShi(){

		View v = LayoutInflater.from(this).inflate(R.layout.maptishipopwindow,null);

		closemaptishi=(ImageButton)v.findViewById(R.id.closebutton);
		closemaptishi.setOnClickListener(this);
		
		ImageView bg=(ImageView)v.findViewById(R.id.maptishibg);
		if(SharedData.getInt("i18n", Language.ZH)==1){//中文
			bg.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		}else{//英文
			bg.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		}

		pop = new PopupWindow(v,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setAnimationStyle(R.style.PopupAnimation);
		pop.showAtLocation(titletv, Gravity.CENTER,0,0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==maptishibutton){//打开地图提示窗 在onCreate方法中如果直接调用展示popupwindow会出现异常，因为showatlotation方法是异步线程，所以有可能锚点和activity尚未建立起连接，正确的方法应该是用过handler.post将调用方法放到消息队列中，当锚点与activity建立起连接后自动执行。
			h.post(new Runnable() {
				public void run() {
					showMapTiShi();
				}
			});
		}
		if(v==closemaptishi){//关闭地图提示框
			if(pop!=null){
				pop.dismiss();
			}
		}
		if(v==goback){
			this.finish();
		}
		if(v==searchmapbutton){
			startTo(ShiNeiDaoHangSearchActivity.class);
		}
		if(v==chosebeginpointlayout){//选择起始点
			startPoint=null;
			Toast.makeText(this, getLanguageString("请设置起点"),Toast.LENGTH_SHORT).show();
			selectMode = 1;
		}
		if(v==choseendpointlayout){//选择终点
			endPoint=null;
			Toast.makeText(this, getLanguageString("请设置终点"),Toast.LENGTH_SHORT).show();
			selectMode = 2;
		}
		if(v==setbeginpointtv){//点击设为起点
			pop.dismiss();
			startpointnametv.setText(getLanguageString("已选择"));
			startPoint=point;
			startpointfloor=openfloornum;
			mapView.getMapOverlay().setStartPoint(startPoint);
			mapView.refresh();
		}
		if(v==setendpointtv){//点击设为终点
			pop.dismiss();
			endpointnametv.setText(getLanguageString("已选择"));
			endPoint=point;
			endpointfloor=openfloornum;
			mapView.getMapOverlay().setEndPoint(endPoint);
			mapView.refresh();
		}
		if(v==begindaohangbutton){//开始导航
			routing();
		}
		if(v==resetmapbutton){
			this.endpointnametv.setText("");
			this.startpointnametv.setText("");
			this.selectMode = 1;
			startPoint=null;
			endPoint=null;
			searchPOI.clear();//清空pois
			searchpoiid="";//搜索记录清除
			mapView.getMapOverlay().clearRouting();
			mapView.getMapOverlay().clearMapPois();
			mapView.refresh();
			mapView.getController().zoomToExtent();//将地图铺满当前地图控件
		}
	}
	
	/**
	 * 开始导航
	 */
	public void routing() {
		if (startPoint == null) {
			Toast.makeText(this, getLanguageString("请设置起点"), Toast.LENGTH_SHORT).show();
			return;
		}
		if (endPoint == null) {
			Toast.makeText(this, getLanguageString("请设置终点"), Toast.LENGTH_SHORT).show();
			return;
		}

		//判断起始点和结束点是否相同，如果相同，不能导航
		if(startpointfloor==endpointfloor){//相同楼层
			if(mapView.selectClicked(startPoint).spaceResult!=null&&mapView.selectClicked(endPoint).spaceResult!=null){
				if (mapView.selectClicked(startPoint).spaceResult.id == mapView.selectClicked(endPoint).spaceResult.id) {
					Toast.makeText(this, getLanguageString("请选择不同节点"), Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		try{
			mapView.getMapOverlay().clearRouting();
			int value=mapView.getMapOverlay().routing(startpointfloor,endpointfloor,startPoint,endPoint,0,null);
			if(value==2){
				Toast.makeText(this, getLanguageString("距离太近"), Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			this.endpointnametv.setText("");
			this.startpointnametv.setText("");
			this.selectMode = 1;
			startPoint=null;
			endPoint=null;
			mapView.getMapOverlay().clearRouting();
			mapView.getMapOverlay().clearMapPois();
			mapView.refresh();
			mapView.getController().zoomToExtent();//将地图铺满当前地图控件
			e.printStackTrace();
		}
//		startPoint=null;
//		endPoint=null;
		this.selectMode = 1;
		
	}
	
	
	public void showPopup() {
		if (selectMode == 1 || selectMode == 2) {
			return;
		}
		mapView.getController().setCenter(point);
		View v = LayoutInflater.from(this).inflate(R.layout.nv_indoor_pop,null);
		TextView tv = (TextView) v.findViewById(R.id.pop_tv);
		tv.setText(getLanguageString("导航"));
		
		setbeginpointtv = (TextView) v.findViewById(R.id.view_info);
		setendpointtv = (TextView) v.findViewById(R.id.nav_route);

		setbeginpointtv.setText(getLanguageString("设为起点"));
		setendpointtv.setText(getLanguageString("设为终点"));

		setbeginpointtv.setOnClickListener(this);
		setendpointtv.setOnClickListener(this);

		pop = new PopupWindow(v,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setAnimationStyle(R.style.PopupAnimation);
		pop.showAtLocation(findViewById(R.id.mapview), Gravity.CENTER,0,-DipUtils.dip2px(this,100) );
	}
	
	@Override
	public void openEnd() {//加载地图完毕，等待对话框消失
		// TODO Auto-generated method stub
		ProgressTools.hide();
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

	/**
	 * 跨楼层导航且换楼层调用
	 */
	public void changeFloorWithRouting(int floornum){
		this.openfloornum=floornum;
		floorsadapter.notifyDataSetChanged();
	}

	@Override
	public void Changed(int floor) {
		// TODO Auto-generated method stub
		changeFloorWithRouting(floor);
	}
}