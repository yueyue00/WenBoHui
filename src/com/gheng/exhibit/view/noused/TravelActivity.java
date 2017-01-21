package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.TravelAdapter;
import com.gheng.exhibit.view.support.PchiData;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 商旅信息页面
 * 
 * @author lileixing
 */
public class TravelActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.lv)
	private ListView lv;

	@ViewInject(R.id.iv)
	private ImageView iv;

	private TravelAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel);
	}

	@Override
	protected void init() {
		titleBar.setText(getLanguageString("相关服务"));
		adapter = new TravelAdapter(this);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(this);
		titleBar.setOnClickListener(this);
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_MAIN, 0, Constant.TYPE_SERVICE, Constant.SERACH_TYPE_ENTER, null);
		// loadOverViewFromServer();

		// iv.setImageResource(R.drawable.travel);
	}

	@Override
	protected void setI18nValue() {
		adapter.setData(getListValues());

	}

	private List<PchiData> getListValues() {
		List<PchiData> list = new ArrayList<PchiData>();
		list.add(new PchiData(getLanguageString("如何到达"),
				R.drawable.service_how_to_go));
		list.add(new PchiData(getLanguageString("参观路线"), R.drawable.tourrout));
		list.add(new PchiData(getLanguageString("馆内餐饮"),
				R.drawable.service_rest));
		list.add(new PchiData(getLanguageString("天气"),
				R.drawable.service_weather));
		list.add(new PchiData(getLanguageString("酒店信息"),
				R.drawable.service_hotel));
		list.add(new PchiData(getLanguageString("城市介绍"),
				R.drawable.service_city));
		list.add(new PchiData(getLanguageString("服务设施"),
				R.drawable.service_how_to_go));
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PchiData data = adapter.getItem(position);
		String title = data.name;
		processList(position, title);
	}

	private void processList(int position, String title) {
		Bundle bd = new Bundle();
		switch (position) {
		case 0: // 如何到达展馆
			bd.putString("web_page", "traffic");
			bd.putString("title", title);
			startTo(WebViewActivity.class, bd);
			break;
		case 1: // 参观路线
			bd.putString("web_page", "tour_route");
			bd.putString("title", title);
			startTo(WebViewActivity.class, bd);
			break;
		case 2: // 周边餐饮
			// bd.putString("title", title);
			bd.putString("web_page", "dining_reservation_list");
			bd.putString("title", title);
			startTo(WebViewActivity.class, bd);
			break;
		case 3: // 天气
			bd.putString("web_page", "weather");
			bd.putString("title", title);
			startTo(WebViewActivity.class, bd);
			break;
		case 4: // 周边酒店
			bd.putString("title", title);
			startTo(HotelListActivity.class, bd);
			break;
		case 5: // 城市及介绍
			bd.putString("web_page", "city");
			bd.putString("title", title);
			startTo(WebViewActivity.class, bd);
			break;
		case 6: // 服务设施
			bd.putString("web_page", "function_chart");
			bd.putString("title", title);
			startTo(WebViewActivity.class, bd);
			break;
		}
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {

	}

}
