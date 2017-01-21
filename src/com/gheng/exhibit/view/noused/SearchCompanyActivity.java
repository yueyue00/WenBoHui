package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.BaseResponse;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.http.body.request.BookParam;
import com.gheng.exhibit.http.body.request.CompanyListParam;
import com.gheng.exhibit.http.body.response.CompanyListData;
import com.gheng.exhibit.http.response.CompanyListResponse;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.DataBaseHelper;
import com.gheng.exhibit.model.databases.OrderBy;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.SearchCompanyAdapter;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 	搜索展商列表
 * @author lileixing
 */
public class SearchCompanyActivity extends BaseActivity {

	@ViewInject(R.id.pull_refresh_list)
	private PullToRefreshListView lv;
	
	private SearchCompanyAdapter adapter;
	
	@ViewInject(R.id.edt_name)
	private EditText edt_name;
	
	private DataBaseHelper dbHelper;
	
	private int pageNo = 1;
	
	private String code;
	
	private int floor;
	
	private long id;
	
	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	@ViewInject(R.id.tv_add)
	private TextView tv_add;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_company);
		adapter = new SearchCompanyAdapter(this);
		dbHelper = new DataBaseHelper(getDbUtils());
		lv.setAdapter(adapter);
		code = getIntent().getExtras().getString("code");
		floor = getIntent().getExtras().getInt("floor");
		initTestData();
		initListener();
		loadDataFromDB(1);
	}
	
	private void initListener(){
		
		tv_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startTo(CompanyEditAcitivity.class);
			}
		});
		
		lv.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				loadDataFromDB(1);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				loadDataFromDB(SearchCompanyActivity.this.pageNo+1);
			}
		});
		edt_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				loadDataFromDB(1);
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				id = adapter.getItem(arg2-1).getId();
				book();
			}
		});
		
		titleBar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void clickRightImage() {
			}
			
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
	}
	
	@Override
	protected void setI18nValue() {
		I18NUtils.setTextView(tv_add, getLanguageString(20016));
		titleBar.setText(getLanguageString(20017));
		I18NUtils.setTextView(edt_name, "",getLanguageString(20018));
		I18NUtils.setPullView(lv, getLanguageString(20024), getLanguageString(20053), getLanguageString(20025), getLanguageString(20026));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		loadCompanyFromServer();
//		handler.post(new LoadRunable());
	}
	
	private void loadCompanyFromServer(){
		try {
			if(getDbUtils().count(Company.class) == 0){
				ProgressTools.showDialog(this);
			}
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		BaseRequestData<CompanyListParam> requestData = new BaseRequestData<CompanyListParam>("companylist");
		requestData.body = new CompanyListParam();
//		requestData.body.time = "0";
		http.post(requestData, new CallBack<CompanyListResponse>() {
			@Override
			public void onSuccess(CompanyListResponse entity) {
				if(entity.retcode == 200){
					handler.post(new LoadRunable(entity));
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				toastNetError();
			}
		});
	}
	
	
	private void loadDataFromDB(int pageNo){
		this.pageNo = pageNo;
		String name = edt_name.getText().toString();
		Selector selector = Selector.from(Company.class);
		selector.where("state", "<>", 0);
		if(StringTools.isNotBlank(name)){
			WhereBuilder wb = WhereBuilder.b();
			wb.and("NAME", "like", "%"+name+"%");
			wb.or("EN_NAME", "like", "%"+name+"%");
			selector.and(wb);
			
//			selector.and("name", "like", "%"+name+"%");
//			selector.or("enName", "like", "%"+name+"%");
//			selector.and(wb);
		}

		List<OrderBy> list = new ArrayList<OrderBy>();
//		list.add(OrderBy.create("updateTime", true));
		list.add(OrderBy.create("name",false,true));
		
		PageBody<Company> page = dbHelper.pageQuery(selector, pageNo,list);
		if(page == null)
			return;
		//lv.onRefreshComplete();
		handler.sendEmptyMessageDelayed(2,500);
		if(page.pageno == 1){
			adapter.setData(page.rdata);
		}else{
			adapter.add(page.rdata);
		}
		if(page.pagecount == page.pageno){
			lv.setMode(Mode.PULL_FROM_START);
		}else{
			lv.setMode(Mode.BOTH);
		}
	}
	
	private void book(){
		Builder builder = new Builder(this);
		builder.setMessage(getLanguageString(20020));
		builder.setPositiveButton(getLanguageString(10003),new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(getLanguageString(10005),new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				loadDataFromServer();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	private void loadDataFromServer(){
		ProgressTools.showDialog(this);
		BaseRequestData<BookParam> requestData = new BaseRequestData<BookParam>("book");
		BookParam param = new BookParam();
		param.code = code;
		param.floor = floor+"";
		param.companyid = id+"";
		requestData.body = param;
		http.post(requestData, new CallBack<BaseResponse>() {

			@Override
			public void onSuccess(BaseResponse entity) {
//				toastShort(entity.retmesg);
				ProgressTools.hide();
				if(entity.retcode == 200){
					setResult(100);
					finish();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				toastNetError();
			}
		});
	}
	
	private void initTestData(){
		try {
			getDbUtils().createTableIfNotExist(Company.class);
//			dbUtils.deleteAll(Company.class);
//			String[] names = {"广州汇朗生物科技有限公司","浙江圣效化学品有限公司"};
//			String[] enNames = {"Guangzhou CDLUNION Biotechnology Co.,Ltd","Zhejiang Shengxiao Chemical Co., Ltd"};
//			for (int i = 0; i< names.length;i++) {
//				Company model = new Company();
//				model.setName(names[i]);
//				model.setEnName(enNames[i]);
//				model.setId(i+1);
//				dbUtils.save(model);
//			}
			
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				ProgressTools.hide();
				loadDataFromDB(1);
				break;
			default:
				lv.onRefreshComplete();
				break;
			}
		};
	};
	
	class LoadRunable implements Runnable{
		
		private CompanyListResponse entity;

		LoadRunable(CompanyListResponse entity){
			this.entity = entity;
		}
		
		@Override
		public void run() {
			PageBody<CompanyListData> body = entity.body;
			if(body.rdata.size() > 0){
				SharedData.commit("time", body.time);
				for (CompanyListData model : body.rdata) {
					Company company = new Company();
					company.setId(model.id);
//					company.setName(model.name);
//					company.setEnName(model.enname);
//					company.setShortName(model.shortame);
//					company.setEnShortName(model.enshortname);
//					company.setUpdateTime(model.updatetime);
					company.setState(model.state);
					try {
						getDbUtils().saveOrUpdate(company);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(1);
			}
		}
		
	}
}
