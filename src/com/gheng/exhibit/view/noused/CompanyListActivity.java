package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.CompanyType;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.CompanyAdapter;
import com.gheng.exhibit.view.adapter.CompanyTypeAdapter;
import com.gheng.exhibit.view.adapter.MyViewPapgerAdapter;
import com.gheng.exhibit.view.support.SearchData;
import com.gheng.exhibit.widget.EmptyView;
import com.gheng.exhibit.widget.LetterLayout;
import com.gheng.exhibit.widget.LetterLayout.OnClickLetterListener;
import com.gheng.exhibit.widget.MyViewPager;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 展商列表
 * 
 * @author lileixing
 */
public class CompanyListActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener,OnClickLetterListener {
	
	static final int TYPE_COMPANY = 1;
	static final int TYPE_COMPANY_TYPE = 2;
	static final int TYPE_COMPANY_GROUP = 3;

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.radio_group)
	private RadioGroup radio_group;

	@ViewInject(R.id.vp)
	private MyViewPager vp;

	private MyViewPapgerAdapter adapter;

	@ViewInject(R.id.edt_name)
	private EditText edt_name;

	@ViewInject(R.id.rbtn_all)
	private RadioButton rbtn_all;

	@ViewInject(R.id.rbtn_nation)
	private RadioButton rbtn_nation;
	
	@ViewInject(R.id.rbtn_group)
	private RadioButton rbtn_group;

	private ViewHolder holder1 = new ViewHolder();

	private ViewHolder2 holder2 = new ViewHolder2();
	
	private ViewHolder2 holder3 = new ViewHolder2();
	
	private CompanyType companyType;
	
	@ViewInject(R.id.tv_type_name)
	private TextView tv_type_name;
	
	@ViewInject(R.id.letter)
	private LetterLayout letterLayout;
	
	@ViewInject(R.id.tab_group)
	private LinearLayout tab_group;
	
	private String selectLetter = "";
	private EmptyView emptyView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_list);
		companyType = (CompanyType) getIntent().getSerializableExtra("companyType");
		
		if(companyType != null){	//从展商类型进来的
			radio_group.setVisibility(View.GONE);
			titleBar.setText(getLanguageString("展商名录"));
			titleBar.showRightImage(true);
			letterLayout.setVisibility(View.GONE);
			tv_type_name.setVisibility(View.VISIBLE);
			tv_type_name.setText((String)I18NUtils.getValue(companyType, "name"));
			tab_group.setVisibility(View.GONE);
		}else{
			titleBar.setText(getLanguageString("展商名录"));
			edt_name.setVisibility(View.VISIBLE);
		}
		emptyView = new EmptyView(this);
		
	}

	@Override
	protected void setI18nValue() {
		I18NUtils.setPullView(holder1.lv, this);
		I18NUtils.setPullView(holder2.lv, this);
		I18NUtils.setPullView(holder3.lv, this);
		I18NUtils.setTextView(edt_name, null, getLanguageString("搜索展商/展位号"));

		I18NUtils.setTextView(rbtn_all, getLanguageString("展商"));
		I18NUtils.setTextView(rbtn_nation, getLanguageString("展区"));
		I18NUtils.setTextView(rbtn_group, getLanguageString("国际展团"));
	}

	@Override
	protected void init() {
		sendLog(Constant.SERACH_TYPE_ENTER);
		letterLayout.setOnClickLetterListener(this);
		List<View> lists = new ArrayList<View>();
		View v1 = getLayoutInflater().inflate(R.layout.pull_listview, null);
		View v2 = getLayoutInflater().inflate(R.layout.pull_listview, null);
		View v3 = getLayoutInflater().inflate(R.layout.pull_listview, null);
		lists.add(v1);
		lists.add(v2);
		lists.add(v3);

		holder1.lv = (PullToRefreshListView) v1;
		holder1.adapter = new CompanyAdapter(this);
		holder1.lv.setAdapter(holder1.adapter);
		holder1.lv.setEmptyView(emptyView);

		holder2.lv = (PullToRefreshListView) v2;
		holder2.adapter = new CompanyTypeAdapter(this);
		holder2.lv.setAdapter(holder2.adapter);
		
		holder3.lv = (PullToRefreshListView) v3;
		holder3.adapter = new CompanyTypeAdapter(this);
		holder3.lv.setAdapter(holder3.adapter);

		holder1.lv.setOnItemClickListener(new OnItemClickImpl(TYPE_COMPANY));
		holder2.lv
				.setOnItemClickListener(new OnItemClickImpl(TYPE_COMPANY_TYPE));
		holder3.lv
				.setOnItemClickListener(new OnItemClickImpl(TYPE_COMPANY_GROUP));

		adapter = new MyViewPapgerAdapter(lists);
		vp.setAdapter(adapter);
		vp.setOffscreenPageLimit(3);

		titleBar.setOnClickListener(this);
		radio_group.setOnCheckedChangeListener(this);
		edt_name.setTag(0);

		holder1.lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadCompany(2);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadCompany(3);
			}
		});
		holder2.lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadCompanyType(2);
			}
			
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadCompanyType(3);
			}
		});
		holder3.lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadCompanyGroup(2);
			}
			
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadCompanyGroup(3);
			}
		});
		
		updateDataFromServer();
		loadCompany(1);
		loadCompanyType(1);
		loadCompanyGroup(1);
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {
		if (edt_name.getVisibility() == View.GONE) {
			edt_name.setVisibility(View.VISIBLE);
			edt_name.setTag(1);
		} else {
			loadCompany(1);
			radio_group.check(R.id.rbtn_all);
			sendLog(Constant.SERACH_TYPE_CLICK);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_all: // 全部
			vp.setCurrentItem(0, false);
			titleBar.showRightImage(true);
			letterLayout.setVisibility(View.VISIBLE);
			edt_name.setVisibility(View.VISIBLE);
			showIndex(0);
			break;
		case R.id.rbtn_nation: // 展区列表
			vp.setCurrentItem(1, false);
			titleBar.showRightImage(true);
			edt_name.setVisibility(View.VISIBLE);
			letterLayout.setVisibility(View.GONE);
			showIndex(1);
			break;
		case R.id.rbtn_group: // 展团列表
			vp.setCurrentItem(2, false);
			titleBar.showRightImage(true);
			edt_name.setVisibility(View.VISIBLE);
			letterLayout.setVisibility(View.GONE);
			showIndex(2);
			break;
		}
	}
	
	private void showIndex(int index){
		int childCount = tab_group.getChildCount();
		for (int i = 0; i < childCount; i++) {
			if(index == i){
				tab_group.getChildAt(i).setVisibility(View.VISIBLE);
			}else{
				tab_group.getChildAt(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	

	class OnItemClickImpl implements OnItemClickListener {

		private int type;

		OnItemClickImpl(int type) {
			this.type = type;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Bundle bd = new Bundle();
			switch (type) {
			case TYPE_COMPANY:
				Company company = holder1.adapter.getItem(position-1);
				//bd.putLong("id", company.getId());
				//startTo(CompanyInfoActivity.class, bd);
				Intent intent = new Intent(CompanyListActivity.this, CompanyInfoActivity.class);
				intent.putExtra("id", company.getId());
				startActivityForResult(intent, 100);
				break;
			case TYPE_COMPANY_TYPE:
				CompanyType item = holder2.adapter.getItem(position-1);
				bd.putSerializable("companyType", item);
				startTo(CompanyListActivity.class, bd);
				break;
			case TYPE_COMPANY_GROUP:
				CompanyType item1 = holder3.adapter.getItem(position-1);
				bd.putSerializable("companyType", item1);
				startTo(CompanyListActivity.class, bd);
				break;
			}
		}

	}

	/**
	 * 1:第一次进入页面 2:下拉刷新 3:上拉加载更多
	 */
	private void loadCompany(int mode) {
		if (mode == 1) {
			holder1.pageno = 1;
			ProgressTools.showDialog(context);
		} else if (mode == 2) {
			holder1.pageno = 1;
		} else {
			holder1.pageno++;
		}
		Selector selector = Selector.from(Company.class);
		String keyword = edt_name.getText().toString();
		ApiUtil.changeSelector(selector);
		if(companyType != null){
			selector.and("typeid", "=", companyType.getId());
			if(StringTools.isNotBlank(keyword)) {
				WhereBuilder wb = WhereBuilder.b("searchtxt", "like", "%" + keyword + "%");
				selector.and(wb);
			}
		}else{
			if(StringTools.isNotBlank(keyword)) {
				WhereBuilder wb = WhereBuilder.b("searchtxt", "like", "%" + keyword + "%");
				selector.and(wb);
			}
			if(StringTools.isNotBlank(selectLetter)) {
				WhereBuilder wb = WhereBuilder.b("searchtxt", "like", selectLetter + "%");
				selector.and(wb);
			}
		}
		selector.orderBy("sorting");
		selector.orderBy("searchtxt");
		http.postToDataBase(selector, holder1.pageno,
				new CallBack<PageBody<Company>>() {
					@Override
					public void onSuccess(PageBody<Company> page) {
						holder1.lv.onRefreshComplete();
						ProgressTools.hide();
						emptyView.show(true);
						if (page.pageno == 1) {
							holder1.adapter.setData(page.rdata);
						} else {
							holder1.adapter.add(page.rdata);
						}
						if (page.pageno < page.pagecount) {
							holder1.lv.setMode(Mode.BOTH);
						}else{
							holder1.lv.setMode(Mode.PULL_FROM_START);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ProgressTools.hide();
						holder1.lv.onRefreshComplete();
					}
				});
	}
	
	private void loadCompanyType(int mode) {
		if (mode == 1) {
			holder2.pageno = 1;
			ProgressTools.showDialog(context);
		} else if (mode == 2) {
			holder2.pageno = 1;
		} else {
			holder2.pageno++;
		}
		Selector selector = Selector.from(CompanyType.class);
		ApiUtil.changeSelector(selector);
		selector.and("tuantype", "=", 0);
		selector.orderBy("sorting");
		http.postToDataBase(selector, holder2.pageno,
				new CallBack<PageBody<CompanyType>>() {
					@Override
					public void onSuccess(PageBody<CompanyType> page) {
						holder2.lv.onRefreshComplete();
						ProgressTools.hide();
						if (page.pageno == 1) {
							holder2.adapter.setData(page.rdata);
						} else {
							holder2.adapter.add(page.rdata);
						}
						if (page.pageno < page.pagecount) {
							holder2.lv.setMode(Mode.BOTH);
						}else{
							holder2.lv.setMode(Mode.PULL_FROM_START);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ProgressTools.hide();
						holder2.lv.onRefreshComplete();
					}
				});
	}
	
	private void loadCompanyGroup(int mode) {
		if (mode == 1) {
			holder3.pageno = 1;
			ProgressTools.showDialog(context);
		} else if (mode == 2) {
			holder3.pageno = 1;
		} else {
			holder3.pageno++;
		}
		Selector selector = Selector.from(CompanyType.class);
		ApiUtil.changeSelector(selector);
		selector.and("tuantype", "=", 1);
		selector.orderBy("sorting");
		http.postToDataBase(selector, holder3.pageno,
				new CallBack<PageBody<CompanyType>>() {
					@Override
					public void onSuccess(PageBody<CompanyType> page) {
						holder3.lv.onRefreshComplete();
						ProgressTools.hide();
						if (page.pageno == 1) {
							holder3.adapter.setData(page.rdata);
						} else {
							holder3.adapter.add(page.rdata);
						}
						if (page.pageno < page.pagecount) {
							holder3.lv.setMode(Mode.BOTH);
						}else{
							holder3.lv.setMode(Mode.PULL_FROM_START);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ProgressTools.hide();
						holder3.lv.onRefreshComplete();
					}
				});
	}
	
	private void sendLog(int searchType){
		SearchData data = null;
		String name = edt_name.getText().toString();
		if(StringTools.isNotBlank(name)){
			data = new SearchData();
			data.searchtxt = name;
		}
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, 0, Constant.TYPE_COMPANY, searchType, data);
	}
	
	private void updateDataFromServer() {
		ApiUtil.invokeCompany(new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
				if(map != null){
					if(map.get(TimeRecordType.COMPANY_INFO) != null && map.get(TimeRecordType.COMPANY_INFO) > 0){
						loadCompany(2);
					}
					if(map.get(TimeRecordType.COMPANY_TYPE) != null && map.get(TimeRecordType.COMPANY_TYPE) > 0){
						loadCompanyType(2);
						loadCompanyGroup(2);
					}
				}
			}
		});
	}

	class ViewHolder {
		PullToRefreshListView lv;
		CompanyAdapter adapter;
		int pageno = -1;
	}
	class ViewHolder2 {
		PullToRefreshListView lv;
		CompanyTypeAdapter adapter;
		int pageno;
	}

	@Override
	public void onClick(TextView tv) {
		String t = tv.getText().toString();
		if(selectLetter.equals(t)) {
			selectLetter = "";
			letterLayout.clearSelected();
		} else {
			selectLetter = t;
		}
		this.loadCompany(2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 100) {//点击CompanyInfo 返回
			if(data == null) {
				return;
			}
			Long companyId = data.getLongExtra("companyId", 0);
			Integer isfav = data.getIntExtra("isfav", 0);
			int count = holder1.adapter.getCount();
			for(int i = 0; i < count; i++) {
				Company company = holder1.adapter.getItem(i);
				if(company.getId() == companyId) {
					company.setIsfav(isfav);
					break;
				}
			}
			holder1.adapter.notifyDataSetChanged();
		}
	}

}
