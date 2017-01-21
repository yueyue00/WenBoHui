package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.request.OverViewParam;
import com.gheng.exhibit.http.body.response.NationData;
import com.gheng.exhibit.http.response.NationListResponse;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.NationAdapter;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 国家列表页面
 * 
 * @author lileixing
 */
public class NationListActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	
	
	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.lv)
	private ListView lv;
	
	private NationAdapter adapter;
	
	private int pchi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nation_list);
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString(10124));
	}
	
	@Override
	protected void init() {
		adapter = new NationAdapter(this);
		pchi = getIntent().getIntExtra("pchi", 2015);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NationData item = adapter.getItem(position);
				Bundle bd = new Bundle();
				bd.putLong("nationid", item.id);
				bd.putAll(getIntent().getExtras());
				startTo(CompanyListActivity.class, bd);
			}
		});
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
			}
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		loadDataFromServer();
	}
	
	private void loadDataFromServer(){
		BaseRequestData<OverViewParam> requestData = new BaseRequestData<OverViewParam>("nationlist");
		OverViewParam param = new OverViewParam();
		if(pchi == 2015){
			param.eid = SharedData.getBatchId()+"";
		}else{
			param.eid = Constant.pchi2016 + "";
		}
		requestData.body = param;
		ProgressTools.showDialog(this);
		http.post(requestData, new CallBack<NationListResponse>() {

			@Override
			public void onSuccess(NationListResponse entity) {
				ProgressTools.hide();
//				NationData data = new NationData();
//				data.id = -2;
//				data.name = getLanguageString(10125);
//				entity.body.rdata.add(data);
				adapter.setData(entity.body.rdata);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				toastNetError();
			}
		});
	}
	
}
