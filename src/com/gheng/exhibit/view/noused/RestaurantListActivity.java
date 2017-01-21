package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.widget.ListView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.request.RestaurantListParam;
import com.gheng.exhibit.http.response.RestaurantResponse;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.RestaurantAdapter;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 展品列表
 * 
 * @author lileixing
 */
public class RestaurantListActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.pull_refresh_list)
	private PullToRefreshListView pullToRefreshListView;

//	private int pchi;

	private ViewHolder hoder1 = new ViewHolder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);
	}

	@Override
	protected void setI18nValue() {
	}

	@Override
	protected void init() {
//		pchi = getIntent().getIntExtra("pchi", 2015);
		titleBar.setText(getIntent().getStringExtra("title"));

		hoder1.lv = pullToRefreshListView;
		hoder1.adapter = new RestaurantAdapter(this);
		hoder1.lv.setAdapter(hoder1.adapter);

		titleBar.setOnClickListener(this);

		hoder1.lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(true);
			}
		});
		if (hoder1.pageno == -1) {
			hoder1.pageno = 1;
			loadData(false);
		}

	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {
	}

	class ViewHolder {
		PullToRefreshListView lv;
		RestaurantAdapter adapter;

		int pageno = -1;
	}

	private void loadData(boolean more) {
		hoder1.pageno = more ? hoder1.pageno + 1 : 1;
		BaseRequestData<RestaurantListParam> requestData = new BaseRequestData<RestaurantListParam>("catererlist");
		RestaurantListParam param = new RestaurantListParam();
		param.pno = hoder1.pageno + "";
		requestData.body = param;
		ProgressTools.showDialog(this);
		http.post(requestData, new CallBack<RestaurantResponse>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				hoder1.lv.onRefreshComplete();
				ProgressTools.hide();
				toastNetError();
			}

			@Override
			public void onSuccess(RestaurantResponse entity) {
				hoder1.lv.onRefreshComplete();
				ProgressTools.hide();
				// toastShort(entity.retmesg);
				hoder1.adapter.setData(entity.body.rdata);

				if (hoder1.pageno >= entity.body.pagecount) {
					hoder1.lv.setMode(Mode.PULL_FROM_START);
				} else {
					hoder1.lv.setMode(Mode.BOTH);
				}
			}
		});
	}
}
