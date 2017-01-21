package com.gheng.exhibit.view.map;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gheng.exhibit.http.body.response.FloorListData;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.FloorListAdapter;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.gheng.indoormap.GeoPoint;
import com.gheng.indoormap.MapView.OnMapViewClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 地图展示层，列出所有的展馆及地图
 * 
 * @author zhaofangfang
 */
public class FloorListActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnMapViewClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	// @ViewInject(R.id.mapView)
	// private MapView mapView;

	private ViewHoder holderFloorList = new ViewHoder();

	private static final int TYPE_MAP = 1;
	private static final int TYPE_LIST = 2;

	private int type = TYPE_LIST; // 当前显示类型

	@ViewInject(R.id.mapFloorContainer)
	private View mapFloorContainer;

	String[] floorNameArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_map_floorlist);
		ViewUtils.inject(holderFloorList, mapFloorContainer);
		initFloorList();
	}

	@Override
	protected void onStart() {
		super.onStart();
		int b = 0;
		System.out.println(b);
	}

	@Override
	protected void init() {
		titleBar.setOnClickListener(this);
		initMap();
	}

	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString("展馆列表"));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		FloorListData data = (FloorListData) arg0.getAdapter().getItem(index);
		gotoMap("7.2");
	}

	private void gotoMap(String floorName) {
		if ("H7.2".equals(floorName)) {
			Bundle bd = new Bundle();
			bd.putString("title", "H7.2" + getLanguageString("概念馆"));
			bd.putString("floorName", floorName);
			startTo(MapShowActivity.class, bd);
		} else {
			Bundle bd = new Bundle();
			bd.putString("title", floorName);
			bd.putString("floorName", floorName);
			startTo(MapShowActivity.class, bd);
		}
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {
		if (type == TYPE_LIST) {
			type = TYPE_MAP;
			mapFloorContainer.setVisibility(View.GONE);
			titleBar.setRightDrawable(getResources().getDrawable(
					R.drawable.list_change_selector));
		} else {
			mapFloorContainer.setVisibility(View.VISIBLE);
			type = TYPE_LIST;
			titleBar.setRightDrawable(getResources().getDrawable(
					R.drawable.map_change_selector));
		}
	}

	private void initFloorList() {
		FloorListAdapter adapter = new FloorListAdapter(this);
		holderFloorList.lv.setAdapter(adapter);
		holderFloorList.lv.setOnItemClickListener(this);

		floorNameArray = this.getResources().getStringArray(R.array.floorNames);
		String[] floorDescArray = this.getResources().getStringArray(
				R.array.floorDescs);
		String[] floorEnDescArray = this.getResources().getStringArray(
				R.array.floorEnDescs);
		List<FloorListData> datas = new ArrayList<FloorListData>();
		int lg = SharedData.getInt("i18n", Language.ZH);
		for (int i = 0; i < floorNameArray.length; i++) {
			FloorListData data = new FloorListData();
			data.floorName = floorNameArray[i];
			if (lg == Language.ZH) {
				data.floorDesc = floorDescArray[i];
			} else {
				data.floorDesc = floorEnDescArray[i];
			}
			if ("H7.2".equals(data.floorName)) {
				data.specialName = getLanguageString("概念馆");
			}
			datas.add(data);
		}
		adapter.setData(datas);
	}

	private void initMap() {
		/*
		 * mapView.openPath(MapHelper.getMapFloorPath("outline"));
		 * mapView.getController().zoomToExtent();
		 * mapView.setInnerPoiSize(InnerPoiSize.SMALL);
		 * mapView.getController().setFloor(1); int lg =
		 * SharedData.getInt("i18n", Language.ZH); if (lg == Language.ZH) {
		 * mapView.setEN(false); } else { mapView.setEN(true); }
		 * mapView.setOnMapViewClickListener(this); mapView.refresh();
		 */
	}

	class ViewHoder {
		@ViewInject(R.id.lv)
		private ListView lv;
	}

	@Override
	public boolean onClick(GeoPoint gp) {
		/*
		 * FeatureResult featureResult = mapView.selectClicked(gp); switch
		 * (featureResult.type) { case FeatureResult.TYPE_SPACE: case
		 * FeatureResult.TYPE_ALL: SpaceResult spaceResult =
		 * featureResult.spaceResult; String name = spaceResult.name;
		 * if(contains(name)){ gotoMap(name); } break; }
		 */
		return false;
	}

	private boolean contains(String name) {
		if (StringTools.isBlank(name) || floorNameArray == null)
			return false;
		for (String fName : floorNameArray) {
			if (fName.trim().equals(name.trim())) {
				return true;
			}
		}
		return false;
	}

}
