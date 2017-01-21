package com.gheng.exhibit.view.map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.MainAdapter;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.ViewPagerPoint2;
import com.gheng.exhibit.widget.ViewPagerPoint2.OnItemChangeListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *
 * @author lileixing
 */
public class MapIndexActivity extends BaseActivity implements OnItemClickListener,OnItemChangeListener{

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	@ViewInject(R.id.vp)
	private ViewPagerPoint2 vp;
	
	private MainAdapter adapter;
	
	int cIndex = 0;
	
	boolean fromMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_index);
		titleBar.showRightImage(false);
		fromMap = getIntent().getBooleanExtra("fromMap", false);
	}
	
	@Override
	protected void setI18nValue() {
		titleBar.setText(getIntent().getStringExtra("title"));
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
				gotoMap();
			}
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
	}
	
	@Override
	protected void init() {
		vp.setOnItemChangeListener(this);
		vp.setOnItemClickListener(this);
		adapter = new MainAdapter(this);
		adapter.setFromMap(true);
		adapter.setData(new Integer[]{
				R.drawable.ic_launcher,
				R.drawable.ic_launcher});
		vp.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		gotoMap();
	}

	@Override
	public void change(int index) {
		if(fromMap)
			return;
		cIndex = index;
		if(index == adapter.getCount() - 1){
			titleBar.showRightImage(true);
		}else{
			titleBar.showRightImage(false);
		}
	}
	
	private void gotoMap(){
		if(fromMap)
			return;
		if(cIndex == adapter.getCount() - 1){
			SharedData.commit("view_index",true);
			Bundle bd = new Bundle();
			bd.putAll(getIntent().getExtras());
			startTo(MapShowActivity.class,bd);
			finish();
		}
	}
	
}
