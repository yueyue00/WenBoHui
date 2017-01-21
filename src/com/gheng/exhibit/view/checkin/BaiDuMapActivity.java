package com.gheng.exhibit.view.checkin;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.gheng.exhibit.http.body.response.BaiDuLocations;
import com.gheng.exhibit.http.body.response.BaiDuLocations.HotelLocations;
import com.gheng.exhibit.http.body.response.BaiDuLocations.Locations;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.support.DrivingRouteOverlay;
import com.gheng.exhibit.view.support.OverlayManager;
import com.hebg3.mxy.utils.zAsyncTaskForBaiDuMap;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

public class BaiDuMapActivity extends Activity implements
		BaiduMap.OnMapClickListener, OnGetGeoCoderResultListener,
		OnGetRoutePlanResultListener, OnClickListener {

	OverlayManager routeOverlay = null;
	MapView mMapView = null; // 地图View
	// 搜索相关
	BaiduMap mBaidumap = null;
	GeoCoder gSearch = null; // 搜索模块，也可去掉地图模块独立使用
	RoutePlanSearch rSearch = null; // 搜索模块，也可去掉地图模块独立使用
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; // 是否首次定位
	private Marker mMarkerA;
	private Marker mMarkerB;
	private InfoWindow mInfoWindow;
	ArrayList<HotelLocations> hotels = new ArrayList<>();
	ArrayList<Locations> huichangs = new ArrayList<>();
	ArrayList<Locations> jingdians = new ArrayList<>();
	ArrayList<Marker> markers = new ArrayList<>();
	BitmapDescriptor bdhotel = BitmapDescriptorFactory
			.fromResource(R.drawable.marker_hotel);
	BitmapDescriptor bdmyhotel = BitmapDescriptorFactory
			.fromResource(R.drawable.marker_huichang);
	BitmapDescriptor bdhuichang = BitmapDescriptorFactory
			.fromResource(R.drawable.marker_huichang);
	BitmapDescriptor bdjingdian = BitmapDescriptorFactory
			.fromResource(R.drawable.marker_jingdian);
	Context mcontext;
	EditText search_location;
	Button but_fanhui, jiudian, huichang, jingdian, dingwei_btn;
	TextView search_tv, in_title;
	LinearLayout daohang_route_liner, daohang_linear, route_linear;
	// l1是定位的坐标，l2是点击的搜索地点或者常用的酒店类地点
	LatLng ml1 = null;
	LatLng ml2 = null;
	String latitude = null;
	User muser;
	public Handler ml2_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				BaiDuLocations baidulocations = (BaiDuLocations) msg.obj;
				hotels.addAll(baidulocations.hotel);
				huichangs.addAll(baidulocations.confHall);
				jingdians.addAll(baidulocations.scenicSpot);
				break;
			case 1:
				// Toast.makeText(mcontext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// Toast.makeText(mcontext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				// Toast.makeText(mcontext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				// Toast.makeText(mcontext, "cookie失效，请求超时！",
				// Toast.LENGTH_SHORT)
				// .show();
				break;
			case 5:
				if (ml1 != null && ml2 != null) {
					daohang_route_liner.setVisibility(View.VISIBLE);
				} else {
					daohang_route_liner.setVisibility(View.GONE);
				}
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mroute);
		mcontext = BaiDuMapActivity.this;
		try {
			DbUtils db = DbUtils.create(this);
			muser = db.findFirst(Selector.from(User.class)
					.where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();
		mMapView.showZoomControls(false);
		mBaidumap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		// 百度地图定位----开启定位图层
		mBaidumap.setMyLocationEnabled(true);
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10);
		mLocClient.setLocOption(option);
		initView();
		initData();
		setAllClickListener();
		mLocClient.start();
		if (getIntent().getStringExtra("latitude") != null) {
			try {
				latitude = getIntent().getStringExtra("latitude");
				String longitude = getIntent().getStringExtra("longitude");
				String address = getIntent().getStringExtra("address");
				ml2 = new LatLng(Double.parseDouble(latitude),
						Double.parseDouble(longitude));
				ml2_handler.sendEmptyMessage(5);
				//
				mBaidumap.clear();
				mMarkerA = (Marker) mBaidumap.addOverlay(new MarkerOptions()
						.position(ml2).title(address).icon(bdhuichang));
				MapStatusUpdate u = MapStatusUpdateFactory
						.newMapStatus(new MapStatus.Builder().target(ml2)
								.zoom(15).build());
				mBaidumap.setMapStatus(u);
				//

			} catch (Exception e) {
				// 防止转换崩溃
			}
		}
	}

	private void initView() {
		daohang_route_liner = (LinearLayout) findViewById(R.id.daohang_route_liner);
		daohang_linear = (LinearLayout) findViewById(R.id.daohang_linear);
		route_linear = (LinearLayout) findViewById(R.id.route_linear);

		search_location = (EditText) findViewById(R.id.search_location);
		jiudian = (Button) findViewById(R.id.jiudian);
		huichang = (Button) findViewById(R.id.huichang);
		jingdian = (Button) findViewById(R.id.jingdian);
		dingwei_btn = (Button) findViewById(R.id.dingwei_btn);
		but_fanhui = (Button) findViewById(R.id.but_fanhui);
		in_title = (TextView) findViewById(R.id.in_title);
		search_tv = (TextView) findViewById(R.id.search_tv);
		jiudian.setOnClickListener(this);
		huichang.setOnClickListener(this);
		jingdian.setOnClickListener(this);
		dingwei_btn.setOnClickListener(this);
		daohang_linear.setOnClickListener(this);
		route_linear.setOnClickListener(this);
		but_fanhui.setOnClickListener(this);
		search_tv.setOnClickListener(this);
		// 设置透明度
		jiudian.getBackground().setAlpha(80);
		huichang.getBackground().setAlpha(80);
		jingdian.getBackground().setAlpha(80);
		dingwei_btn.getBackground().setAlpha(80);

		// 初始化搜索模块，注册事件监听
		in_title.setText(BaseActivity.getLanguageString("地图导航"));
		gSearch = GeoCoder.newInstance();
		gSearch.setOnGetGeoCodeResultListener(this);
		rSearch = RoutePlanSearch.newInstance();
		rSearch.setOnGetRoutePlanResultListener(this);
		//
		search_tv.setText(BaseActivity.getLanguageString("搜索"));
		search_location.setHint(BaseActivity.getLanguageString("搜索"));
		jiudian.setText(BaseActivity.getLanguageString("酒店"));
		huichang.setText(BaseActivity.getLanguageString("会场"));
		jingdian.setText(BaseActivity.getLanguageString("景点"));
		dingwei_btn.setText(BaseActivity.getLanguageString("定位"));
	}

	private void initData() {
		try {
			zAsyncTaskForBaiDuMap at = new zAsyncTaskForBaiDuMap(
					ml2_handler.obtainMessage(), mcontext, Constant.decode(
							Constant.key, muser.getUserId()));
			at.execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setAllClickListener() {
		mBaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				button.setTextColor(Color.parseColor("#3a3b3b"));
				button.setTextSize(12);
				button.setText(marker.getTitle());
				// TextView nametv = new TextView(getApplicationContext());
				// nametv.setBackgroundResource(R.drawable.popup);
				// nametv.setTextColor(Color.parseColor("#3a3b3b"));
				// nametv.setTextSize(12);
				// nametv.setText(marker.getTitle());
				ml2 = marker.getPosition();
				ml2_handler.sendEmptyMessage(5);
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory
						.fromView(button), ml2, -47, null);
				mBaidumap.showInfoWindow(mInfoWindow);
				// }
				return true;
			}
		});
		search_location.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				String search = search_location.getText().toString();
				if (search.equals("")) {
					Toast.makeText(mcontext, "输入为空", Toast.LENGTH_SHORT).show();
				} else {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(BaiDuMapActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					gSearch.geocode(new GeoCodeOption().city("敦煌市").address(
							search));
					// Toast.makeText(mcontext, "执行搜索",
					// Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.equals(jiudian)) {
			mBaidumap.clear();
			markers.clear();
			for (int i = 0; i < hotels.size(); i++) {
				String title = hotels.get(i).hotel_name + "\n"
						+ hotels.get(i).address;
				if (hotels.get(i).ll != null) {
					if (hotels.get(i).member_id != null) {
						MarkerOptions oob = new MarkerOptions().title(title)
								.position(hotels.get(i).ll).icon(bdmyhotel)
								.zIndex(i).draggable(true);
						oob.animateType(MarkerAnimateType.grow);
						mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
						markers.add(mMarkerB);
					} else {
						MarkerOptions oob = new MarkerOptions().title(title)
								.position(hotels.get(i).ll).icon(bdhotel)
								.zIndex(i).draggable(true);
						oob.animateType(MarkerAnimateType.grow);
						mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
						markers.add(mMarkerB);
					}
				}
			}
			// MapStatusUpdate u = MapStatusUpdateFactory
			// .newLatLng(hotels.get(0).ll);
			// if (hotels.size() > 0) {
			// MapStatusUpdate u = MapStatusUpdateFactory
			// .newMapStatus(new MapStatus.Builder()
			// .target(hotels.get(0).ll).zoom(15).build());
			// mBaidumap.setMapStatus(u);
			// }
			MapStatusUpdate u = MapStatusUpdateFactory
					.newMapStatus(new MapStatus.Builder().target(ml1).zoom(15)
							.build());
			mBaidumap.setMapStatus(u);
		} else if (v.equals(huichang)) {
			mBaidumap.clear();
			markers.clear();
			for (int i = 0; i < huichangs.size(); i++) {
				MarkerOptions oob = new MarkerOptions()
						.title(huichangs.get(i).name)
						.position(huichangs.get(i).ll).icon(bdhuichang)
						.zIndex(i).draggable(true);
				oob.animateType(MarkerAnimateType.grow);
				mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
				markers.add(mMarkerB);
			}
			// if (huichangs.size() > 0) {
			// MapStatusUpdate u = MapStatusUpdateFactory
			// .newMapStatus(new MapStatus.Builder()
			// .target(huichangs.get(0).ll).zoom(15).build());
			// mBaidumap.setMapStatus(u);
			// }
			MapStatusUpdate u = MapStatusUpdateFactory
					.newMapStatus(new MapStatus.Builder().target(ml1).zoom(15)
							.build());
			mBaidumap.setMapStatus(u);
		} else if (v.equals(jingdian)) {
			mBaidumap.clear();
			markers.clear();
			for (int i = 0; i < jingdians.size(); i++) {
				MarkerOptions oob = new MarkerOptions()
						.title(jingdians.get(i).name)
						.position(jingdians.get(i).ll).icon(bdjingdian)
						.zIndex(i).draggable(true);
				oob.animateType(MarkerAnimateType.grow);
				mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
				markers.add(mMarkerB);
			}
			// if (jingdians.size() > 0) {
			// MapStatusUpdate u = MapStatusUpdateFactory
			// .newMapStatus(new MapStatus.Builder()
			// .target(jingdians.get(0).ll).zoom(15).build());
			// mBaidumap.setMapStatus(u);
			// }
			MapStatusUpdate u = MapStatusUpdateFactory
					.newMapStatus(new MapStatus.Builder().target(ml1).zoom(15)
							.build());
			mBaidumap.setMapStatus(u);
		} else if (v.equals(dingwei_btn)) {
			MapStatus.Builder builder = new MapStatus.Builder();
			builder.target(ml1).zoom(15.0f);
			mBaidumap.animateMapStatus(MapStatusUpdateFactory
					.newMapStatus(builder.build()));
		} else if (v.equals(daohang_linear)) {
			if (ml2 != null) {
				Toast.makeText(mcontext, "正在打开百度地图！", Toast.LENGTH_SHORT)
						.show();
				// 构建 导航参数
				NaviParaOption para = new NaviParaOption().startPoint(ml1)
						.endPoint(ml2).startName("天安门").endName("百度大厦");
				try {
					BaiduMapNavigation.openBaiduMapNavi(para, this);
				} catch (BaiduMapAppNotSupportNaviException e) {
					e.printStackTrace();
					showDialog();
				}
			} else {
				Toast.makeText(mcontext, "请先确定目的地！", Toast.LENGTH_SHORT).show();
			}
		} else if (v.equals(route_linear)) {
			// 重置浏览节点的路线数据
			if (routeOverlay != null)
				routeOverlay.removeFromMap();
			PlanNode stNode1 = PlanNode.withLocation(ml1);
			PlanNode enNode1 = PlanNode.withLocation(ml2);
			rSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode1)
					.to(enNode1));
		} else if (v.equals(but_fanhui)) {
			BaiDuMapActivity.this.finish();
		} else if (v.equals(search_tv)) {
			String search = search_location.getText().toString();
			if (search.equals("")) {
				Toast.makeText(mcontext, "输入为空", Toast.LENGTH_SHORT).show();
			} else {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(BaiDuMapActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				gSearch.geocode(new GeoCodeOption().city("敦煌市").address(search));
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			// 这里一般不会走，走到了ifFrirstLoc里面
			ml1 = new LatLng(location.getLatitude(), location.getLongitude());
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaidumap.setMyLocationData(locData);
			if (isFirstLoc && latitude == null) {
				isFirstLoc = false;
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ml1).zoom(15.0f);
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 回收 bitmap 资源
		bdhotel.recycle();
		bdmyhotel.recycle();
		bdhuichang.recycle();
		bdjingdian.recycle();
		gSearch.destroy();
		rSearch.destroy();
		mMapView.onDestroy();
		mLocClient.stop();
		BaiduMapNavigation.finish(this);
		super.onDestroy();
	}

	// 获取到经纬度的方法
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mcontext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		ml2 = result.getLocation();
		ml2_handler.sendEmptyMessage(1);
		mBaidumap.clear();
		mMarkerA = (Marker) mBaidumap.addOverlay(new MarkerOptions()
				.position(result.getLocation()).title(result.getAddress())
				.icon(bdmyhotel).animateType(MarkerAnimateType.grow));
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		// Toast.makeText(mcontext, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(BaiDuMapActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {

	}

	/**
	 * 提示未安装百度地图app或app版本过低
	 */
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				OpenClientUtil.getLatestBaiduMapApp(mcontext);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();

	}
}