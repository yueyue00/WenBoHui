package com.gheng.exhibit.view.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.request.LocationBody;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.DipUtils;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.MapHelper;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.Projection;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.utils.UdpHelper;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.noused.CompanyInfoActivity;
import com.gheng.exhibit.view.support.PoiData;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.indoormap.GeoPoint;
import com.gheng.indoormap.MapPoi;
import com.gheng.indoormap.MapView;
import com.gheng.indoormap.MapView.InnerPoiSize;
import com.gheng.indoormap.MapView.OnMapOpenEndListener;
import com.gheng.indoormap.MapView.OnMapViewClickListener;
import com.gheng.indoormap.result.FeatureResult;
import com.gheng.indoormap.result.PoiResult;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 地图显示
 * 
 * @author zhaofangfang
 * 
 */
public class MapShowActivity extends BaseActivity implements OnClickListener,
		OnMapOpenEndListener {

	@ViewInject(R.id.mapView)
	private MapView mapView;

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.clear_btn)
	private Button clearBtn;

	@ViewInject(R.id.more_detail)
	private View moreDetailView;

	@ViewInject(R.id.startEt)
	private Button startEt;

	@ViewInject(R.id.endEt)
	private Button endEt;

	@ViewInject(R.id.startNav)
	private ImageView startNav;

	@ViewInject(R.id.endNav)
	private ImageView endNav;

	@ViewInject(R.id.change_btn)
	private Button changeBtn;

	@ViewInject(R.id.routing_btn)
	private ImageView routingView;

	@ViewInject(R.id.search)
	private ImageView searchView;

	@ViewInject(R.id.info_btn)
	private ImageView infoBtn;

	int selectMode = 0;// 1选择起点 2选择终点
	// 点击的名称及坐标
	String[] str;
	GeoPoint point;
	PopupWindow mPopup;// 弹出窗口
	int startFloor, endFloor;
	GeoPoint startPoint, endPoint;
	String startName, endName;
	int curFloor = 2;
	boolean locFlag = false;
	Animation mAnimation = null;

	int requestCode = 100;
	int startNavCode = 101;
	int endNavCode = 102;
	int isBeaconLocation = 0;

	private Bitmap poiIcon;
	private ArrayList<PoiData> poiDatas;
	private String floorTitle;
	private String floorName;
	private String floorNo;
	private String h72;
	private String title;
	private Long selectCompanyId;
	private boolean isFirstLoc = true;
	private Bitmap icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_map);

		floorTitle = this.getIntent().getStringExtra("floorName");
		title = this.getIntent().getStringExtra("title");
		floorNo = this.getIntent().getStringExtra("floorNo");
		h72 = this.getResources().getString(R.string.h72);
		if (!floorTitle.startsWith(h72)) {
			((View) (clearBtn.getParent())).setVisibility(View.VISIBLE);
			((View) (routingView.getParent())).setVisibility(View.VISIBLE);
			((View) (infoBtn.getParent())).setVisibility(View.VISIBLE);
			// routingView.setVisibility(View.GONE);
		}
		icon = BitmapFactory.decodeResource(getResources(),
				R.drawable.nav_loc_center);
		ProgressTools.showDialog(this);
	}

	@Override
	protected void init() {

		floorName = MapHelper.getFloorName(floorTitle);
		super.init();
		poiIcon = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.marker_red)).getBitmap();
		mapView.setOnMapOpenEndListener(this);// 打开地图
		mapView.openPath(MapHelper.getMapFloorPath(floorName));
		// mapView.getFloors();//获取当前场景 楼层数据 1 F1 -1 B1
		mapView.setOnMapViewClickListener(onMapViewClickListener);
		// mapView.getController().setFloor(-1);//打开当前场景地图的 -1层地图
		mapView.getController().zoomToExtent();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2.0)
				+ Math.pow(dm.heightPixels, 2.0));

		double screenSize = diagonalPixels / (160 * dm.density);
		if (screenSize <= 7) {
			mapView.setInnerPoiSize(InnerPoiSize.SMALL);// 设置图标大小
		} else {
			mapView.setInnerPoiSize(InnerPoiSize.MIDDLE);
		}
		int lg = SharedData.getInt("i18n", Language.ZH);
		if (lg == Language.ZH) {
			mapView.setEN(false);
		} else {
			mapView.setEN(true);
		}
		mapView.refresh();
		// mapView.setStartEndBitmapScale(0.6f);
		Integer no = Integer.parseInt(MapHelper.getFloorNo(floorName));
		mapView.getController().setFloor(no);//

		List<PoiResult> poiResults = mapView.searchPOI("", "");
		poiDatas = handlePoi(poiResults);

		startEt.setOnClickListener(this);
		endEt.setOnClickListener(this);
		startNav.setOnClickListener(this);
		endNav.setOnClickListener(this);
		clearBtn.setOnClickListener(this);
		changeBtn.setOnClickListener(this);
		routingView.setOnClickListener(this);
		searchView.setOnClickListener(this);
		infoBtn.setOnClickListener(this);
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});

		searchOnMap(getIntent(), 0);

		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});

		mAnimation = AnimationUtils.loadAnimation(this, R.anim.nav_roate);
	}

	/**
	 * 复写onActivityResult，这个方法 是要等到SimpleTaskActivity点了提交过后才会执行的
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != data) {
			Bundle extras = data.getExtras();
			int searchType = extras.getInt(Constant.SEARCH_TYPE);
			Serializable serializable = extras
					.getSerializable(Constant.MODEL_KEY);
			Company company = null;
			if (searchType == Constant.FROM_EXHIBIT) {
				company = (Company) serializable;

			} else if (searchType == Constant.FROM_PRODUCT) {
				Product product = (Product) serializable;
				try {
					company = (Company) getDbUtils().findFirst(
							Selector.from(Company.class).where("id", "=",
									product.getCompanyid()));
				} catch (DbException e) {
					e.printStackTrace();
				}
			} else if (searchType == Constant.FROM_ZONE) {
				PoiData poiData = (PoiData) serializable;
				List<MapPoi> pois = new ArrayList<MapPoi>();
				MapPoi poi = new MapPoi();
				poi.geoPt = new GeoPoint((float) poiData.x, (float) poiData.y);
				poi.floorIndex = mapView.getFloor();
				poi.icon = poiIcon;
				poi.center = false;
				pois.add(poi);
				mapView.getMapOverlay().setMapPois(pois);
				mapView.refresh();
				super.onActivityResult(requestCode, resultCode, data);
				return;
			}
			if (company != null) {
				List<Map<String, String>> list = company.getStandreferences();
				for (Map<String, String> map : list) {
					String zone = map.get("zone");
					String no = map.get("no");
					if (zone.contains(floorName)) {
						floorNo = no;
						break;
					}
				}
			}
			searchOnMap(data, requestCode);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 根据跳转界面来的关键字进行地图搜索 导航
	 * 
	 * @param data
	 */
	private void searchOnMap(Intent data, int requestCode) {

		List<PoiResult> searchPOI = null;
		Bundle extras = data.getExtras();
		if (null != extras) {
			int searchType = extras.getInt(Constant.SEARCH_TYPE);
			String keyword = extras.getString(Constant.KEYWORD);
			Serializable serializable = extras
					.getSerializable(Constant.MODEL_KEY);

			// 判断是导航还是 查看位置
			int action = extras.getInt(Constant.TO_MAP_TYPE_TKEY);
			if (requestCode == startNavCode || requestCode == endNavCode) {
				action = Constant.MAP_NAVTO;
			}

			switch (searchType) {
			case Constant.FROM_EXHIBIT:
				if (StringTools.isNotBlank(floorNo)) {
					searchPOI = mapView.searchSpaceWithNum(floorNo);// 通过id 搜索
																	// 平面（房间）
					// mapView.searchSpaceWithKey(name)
				}
				break;
			case Constant.FROM_PRODUCT:
				if (StringTools.isNotBlank(floorNo)) {
					searchPOI = mapView.searchSpaceWithNum(floorNo);
				}
				break;
			case Constant.FROM_POITYPE:
				searchPOI = mapView.searchPOI(keyword);

				break;
			case Constant.FROM_ZONE:
				searchPOI = mapView.searchPOI("101800", keyword);// 模糊查询
				break;
			}
			if (null != searchPOI && searchPOI.size() > 0) {
				List<MapPoi> pois = new ArrayList<MapPoi>();
				for (PoiResult poiResult : searchPOI) {
					MapPoi poi = new MapPoi();
					poi.center = false;
					poi.geoPt = poiResult.gp;
					poi.floorIndex = poiResult.floorIndex;
					poi.icon = poiIcon;
					// poi.scale = 0.4f;
					pois.add(poi);

					str = new String[] { poiResult.name };
					point = poiResult.gp;
				}
				if (action == Constant.MAP_NAVTO) {
					if (this.startNavCode == requestCode) {
						selectMode = 1;
					} else {
						selectMode = 2;
					}
					handler.sendEmptyMessage(11);
					mapView.selectClicked(point);
					moreDetailView.setVisibility(View.VISIBLE);

					if (startPoint != null && endPoint != null) {
						routing();
					}

				} else {
					mapView.getController().zoomToExtent();
					mapView.getMapOverlay().setMapPois(pois);
					mapView.refresh();
				}
			}
		}
	}

	@Override
	protected void setI18nValue() {
		startEt.setHint(getLanguageString("请点选起点"));
		endEt.setHint(getLanguageString("请点选终点"));
		titleBar.setText(getLanguageString("地图") + " " + title);
	}

	public OnMapViewClickListener onMapViewClickListener = new OnMapViewClickListener() {
		@Override
		public boolean onClick(GeoPoint gp) {
			FeatureResult result = mapView.selectClicked(gp);// 选择空间的面（方块）
			if (result.type == FeatureResult.TYPE_SPACE) {
				// mapView.getController().setCenter(gp);//点击地图 某一地点，将被点击的点作为中心点
				point = gp;

				String spaceName = MapHelper
						.getSpaceNames(result.spaceResult.name);// 获取位置信息

				if (StringTools.isNotBlank(result.spaceResult.code)) {// 展位 编码
																		// 例如：酒店房间号

					try {
						String code = result.spaceResult.code;
						String n = title.replace(getLanguageString("概念馆"), "");
						n = n.trim();
						String zoneNum = n.substring(n.length() - 3);
						Selector selector = Selector.from(Company.class);
						WhereBuilder nameBuilder = WhereBuilder.b(
								"standreference", "like", "%" + code + "%")
								.and("trim(zoneid)", "like", "%" + zoneNum);
						selector.where(nameBuilder);
						ApiUtil.changeSelector(selector);

						Company company = getDbUtils().findFirst(selector);
						if (null != company) {
							spaceName = I18NUtils.getValue(company, "name")
									.toString();
							selectCompanyId = company.getId();
						} else {
							selectCompanyId = 0L;
						}
					} catch (DbException e) {
						e.printStackTrace();
					}
				}

				str = new String[] { spaceName, result.spaceResult.code };
				if (StringTools.isBlank(spaceName)
						&& StringTools.isBlank(result.spaceResult.code)) {

				} else {
					showPopup();
				}
			}
			if (result.type == FeatureResult.TYPE_POI) {
				mapView.getController().setCenter(gp);
				point = gp;
				String poiName = MapHelper.getPoiName(result.poiResult.name);
				str = new String[] { poiName, "" };
				showPopup();
			}

			if (selectMode == 1) {// 选择起点
				handler.sendEmptyMessage(11);
			}
			if (selectMode == 2) {// 选择终点
				handler.sendEmptyMessage(11);
			}
			return false;
		}

	};

	public void showPopup() {
		if (selectMode == 1 || selectMode == 2) {
			return;
		}
		View pop = LayoutInflater.from(this).inflate(R.layout.nv_indoor_pop,
				null);
		// pop.getBackground().setAlpha(240);// 0~255透明度值
		TextView tv = (TextView) pop.findViewById(R.id.pop_tv);
		TextView num = (TextView) pop.findViewById(R.id.num);

		TextView viewInfo = (TextView) pop.findViewById(R.id.view_info);
		TextView navRoute = (TextView) pop.findViewById(R.id.nav_route);

		View navLine = (View) pop.findViewById(R.id.nav_line);
		I18NUtils.setTextView(viewInfo, getLanguageString("详情"));
		I18NUtils.setTextView(navRoute, getLanguageString("导航"));
		if (!floorTitle.startsWith(h72)) {
			navRoute.setVisibility(View.GONE);
			navLine.setVisibility(View.GONE);
		}
		String s = "";
		if (StringTools.isNotBlank(str[0])) {
			s = str[0];
			tv.setText(s);
		}
		if (StringTools.isNotBlank(str[1])) {
			num.setText(getLanguageString("展位") + ":" + str[1]);
		}

		tv.setOnClickListener(this);
		navRoute.setOnClickListener(this);
		viewInfo.setOnClickListener(this);

		mPopup = new PopupWindow(pop, (int) DipUtils.dip2px(this, 200),
				LayoutParams.WRAP_CONTENT);
		mPopup.setBackgroundDrawable(new BitmapDrawable());
		mPopup.setOutsideTouchable(true);

		PointF pointF = mapView.gp2px(point);
		mPopup.showAtLocation(findViewById(R.id.indoor_map), Gravity.LEFT
				| Gravity.BOTTOM,
				(int) (pointF.x - DipUtils.dip2px(this, 100)), (int) pointF.y);
		mPopup.setFocusable(true);
		mPopup.setAnimationStyle(R.style.PopupAnimation);
		mPopup.update();
	}

	@Override
	public void onClick(View v) {
		Bundle bd = new Bundle();
		switch (v.getId()) {
		case R.id.view_info:
			mPopup.dismiss();
			bd.putString("floorName", floorName);
			if (selectCompanyId == null || selectCompanyId == 0) {
				return;
			}
			bd.putLong("id", selectCompanyId);
			startTo(CompanyInfoActivity.class, bd);
			break;
		case R.id.pop_tv:
			mPopup.dismiss();
			Bundle bd1 = new Bundle();
			bd1.putString("floorName", floorName);
			// bd1.putString("code", str[1]);
			if (selectCompanyId == null || selectCompanyId == 0) {
				return;
			}
			bd1.putLong("id", selectCompanyId);
			startTo(CompanyInfoActivity.class, bd1);
			break;
		case R.id.search:// 查找
			Intent intent = new Intent(MapShowActivity.this,
					MapSearchActivity.class);
			intent.putExtra("title", title + getLanguageString("查询"));
			intent.putExtra("floorName", floorName);
			intent.putExtra("poiDatas", (Serializable) poiDatas);
			startActivityForResult(intent, requestCode);

			break;
		case R.id.nav_route:
			mPopup.dismiss();
			mapView.getMapOverlay().clearRouting();
			this.endFloor = curFloor;
			this.endPoint = this.point;
			this.endName = str[0];
			this.endEt.setText(endName);
			mapView.getMapOverlay().setEndPoint(endPoint);
			mapView.refresh();
			moreDetailView.setVisibility(View.VISIBLE);
			ProgressTools.showDialog(context);
			break;
		case R.id.startEt:
			Toast.makeText(this, getLanguageString("地图上点选起点"),
					Toast.LENGTH_SHORT).show();
			selectMode = 1;
			break;
		case R.id.startNav:
			selectMode = 1;
			Intent startIntent = new Intent(MapShowActivity.this,
					MapSearchActivity.class);
			startIntent.putExtra("title", getLanguageString("H7.2 概念馆"));
			startIntent.putExtra("poiDatas", (Serializable) poiDatas);
			startIntent.putExtra("code", startNavCode);
			startIntent.putExtra("floorName", floorName);
			startActivityForResult(startIntent, startNavCode);
			// Toast.makeText(this, "地图上点选起点", Toast.LENGTH_SHORT).show();
			break;
		case R.id.endEt:
			Toast.makeText(this, getLanguageString("地图上点选终点"),
					Toast.LENGTH_SHORT).show();
			selectMode = 2;
			break;
		case R.id.endNav:
			Intent endIntent = new Intent(MapShowActivity.this,
					MapSearchActivity.class);
			endIntent.putExtra("title", getLanguageString("H7.2 概念馆"));
			endIntent.putExtra("poiDatas", (Serializable) poiDatas);
			endIntent.putExtra("code", endNavCode);
			endIntent.putExtra("floorName", floorName);
			startActivityForResult(endIntent, endNavCode);
			Toast.makeText(this, getLanguageString("地图上点选终点"),
					Toast.LENGTH_SHORT).show();
			selectMode = 2;
			break;
		case R.id.clear_btn:// 重置地图
			mapView.getMapOverlay().clearRouting();
			mapView.getMapOverlay().clearMapPois();
			this.endEt.setText("");
			this.startEt.setText("");
			this.selectMode = 0;
			moreDetailView.setVisibility(View.INVISIBLE);
			break;
		case R.id.change_btn:
			routing();
			break;
		case R.id.info_btn:
			bd.putAll(getIntent().getExtras());
			bd.putBoolean("fromMap", true);
			InfoDialog.showDialog(context);
			// startTo(MapIndexActivity.class, bd);
			break;
		case R.id.routing_btn:// 导航按钮
			moreDetailView.setVisibility(View.VISIBLE);
			break;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 11:// 赋值
				if (selectMode == 2) {
					endFloor = curFloor;
					endPoint = point;
					if (str != null && str.length > 0) {
						endName = str[0];
					} else {
						endName = getLanguageString("地图上的点");
					}

					endEt.setText(endName);
					mapView.getMapOverlay().setEndPoint(endPoint);
					mapView.refresh();
				}
				if (selectMode == 1) {
					startFloor = curFloor;
					startPoint = point;
					if (str != null && str.length > 0) {
						startName = str[0];
					} else {
						startName = getLanguageString("地图上的点");
					}
					startEt.setText(startName);
					mapView.getMapOverlay().setStartPoint(startPoint);
					mapView.refresh();
				}
				break;
			}
		}
	};

	public void routing() {// 调用本方法，展示路径规划
		if (startPoint == null) {
			Toast.makeText(this, getLanguageString("请设置起点"), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (endPoint == null) {
			Toast.makeText(this, getLanguageString("请设置终点"), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		mapView.getMapOverlay().clearRouting();
		mapView.getMapOverlay().routing(startFloor, endFloor, startPoint,
				endPoint, 0, null);
	}
	

	@Override
	public void openEnd() {
		ProgressTools.hide();
	}

	public static double[] cal(double locx, double locy) {
		double llon = 114.276010;// 图片左边X坐标点
		double llat = 30.573191; // 左边Y坐标点
		double[] lll = Projection.lonLatToMeters(llon, llat);
		double rlon = 114.277122;// 图片右边X坐标点
		double rlat = 30.571079; // 右边Y坐标点
		double[] rll = Projection.lonLatToMeters(rlon, rlat);

		double dlon = rll[0] - lll[0];
		double dlat = lll[1] - rll[1];
		double wpx = 944;
		double hpx = 2091;
		int SCALE = 50;

		double nlon = lll[0] + (dlon / wpx) * locx;
		double nlat = lll[1] - (dlat / hpx) * locy;

		// System.out.println(nlon + ", " + nlat);

		// 地图坐标
		double mapLon = 114.276010;
		double mapLat = 30.573191;
		double[] mapll = Projection.lonLatToMeters(mapLon, mapLat);

		// System.out.println("mll: " + mll[0] + ", " + mll[1]);

		float x = (float) (nlon * SCALE - mapll[0] * SCALE);
		float y = (float) (mapll[1] * SCALE - nlat * SCALE);
		// System.out.println(x + ", " + y);

		return new double[] { x, y };
	}

	private ArrayList<PoiData> handlePoi(List<PoiResult> results) {
		ArrayList<PoiData> datas = new ArrayList<PoiData>();
		for (PoiResult result : results) {
			PoiData data = new PoiData();
			if (StringTools.isNotBlank(result.name)) {
				String[] args = result.name.split("###");
				if (args.length == 2) {
					data.name = args[0];
					data.enName = args[1];
				} else if (args.length == 1) {
					data.name = args[0];
				} else {
					data.name = data.name;
				}
				data.x = result.gp.x;
				data.y = result.gp.y;
				data.poiType = result.type;
				datas.add(data);
			}
		}
		return datas;
	}

	public void sendByUdp(final int type, final double x, final double y) {
		new Thread() {
			@Override
			public void run() {
				UdpHelper.send(new LocationBody(MapShowActivity.this, type, 2,
						x, y).toString());
			}
		}.start();
	}
}
