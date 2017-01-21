package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.http.body.request.PartnerListParam;
import com.gheng.exhibit.http.body.response.PartnerListData;
import com.gheng.exhibit.http.response.PartnerListResponse;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.PartnerAdapter;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 合作伙伴列表
 * 
 * @author lileixing
 */
public class PartnerListActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	
	
	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.lv)
	private ListView lv;
	
	private PartnerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partener_list);
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
		titleBar.setText(getIntent().getStringExtra("title"));
	}
	
	@Override
	protected void init() {
		adapter = new PartnerAdapter(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
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
		loadPartnerFromServer();
	}
	
	/**
	 * 加载合作伙伴数据
	 */
	private void loadPartnerFromServer()
	{
		ProgressTools.showDialog(this);
		BaseRequestData<PartnerListParam> requestData = new BaseRequestData<PartnerListParam>("partnerlist");
		PartnerListParam param = new PartnerListParam();
		requestData.body = param;
		http.post(requestData, new CallBack<PartnerListResponse>()
		{
			@Override
			public void onSuccess(PartnerListResponse entity)
			{
				ProgressTools.hide();
//				toastShort(entity.retmesg);
				if (entity.retcode == 200)
				{
					PageBody<PartnerListData> page = entity.body;
					if (page.rdata.size() > 0)
					{
						// page.rdata
						adapter.setData(page.rdata);
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				ProgressTools.hide();
				toastNetError();
				System.out.println(error.getMessage());
			}
		});
	}

}
