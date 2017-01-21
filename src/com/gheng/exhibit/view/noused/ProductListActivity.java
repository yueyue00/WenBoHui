package com.gheng.exhibit.view.noused;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.database.task.ProductListTask;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.model.databases.ProductType;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.ProductAdapter;
import com.gheng.exhibit.view.support.SearchData;
import com.gheng.exhibit.widget.EmptyView;
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
 * 展品列表
 * 
 * @author lileixing
 */
public class ProductListActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.lv)
	private PullToRefreshListView lv;

	@ViewInject(R.id.edt_name)
	private EditText edt_name;

	private ProductAdapter adapter;
	
	private ProductType model;
	
	@ViewInject(R.id.tv_type_name)
	private TextView tv_type_name;
	
	int pageno = 1;
	
	private EmptyView emptyView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		emptyView = new EmptyView(this);
		lv.setEmptyView(emptyView);
	}

	@Override
	protected void setI18nValue() {
		I18NUtils.setPullView(lv, this);
		String showType = getIntent().getStringExtra("showType");
		if(StringTools.equals(showType, "all")) {
			I18NUtils.setTextView(edt_name, null, getLanguageString("搜索展品/展商名/展位号"));
		} else {
			I18NUtils.setTextView(edt_name, null, getLanguageString("当前分类下搜索"));
		}
		
		I18NUtils.setTextView(tv_type_name, getLanguageString((String)I18NUtils.getValue(model, "name")));
	}

	@Override
	protected void init() {
		sendLog(Constant.SERACH_TYPE_ENTER);
		String showType = getIntent().getStringExtra("showType");
		edt_name.setVisibility(View.VISIBLE);
		if(StringTools.equals(showType, "all")) {
			tv_type_name.setVisibility(View.GONE);
		} else {
			model = (ProductType) getIntent().getSerializableExtra("model");
			tv_type_name.setVisibility(View.VISIBLE);
		}
		adapter = new ProductAdapter(this);
		lv.setAdapter(adapter);
		titleBar.setOnClickListener(this);

		lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(1);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(2);
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Product item = adapter.getItem(position-1);
				Intent intent = new Intent(ProductListActivity.this, ProductInfoActivity.class);
				intent.putExtra("id", item.getId());
				startActivityForResult(intent, 100);
			}
		});
		loadData(1);
		updateCompany();
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {
		if (edt_name.getVisibility() == View.GONE) {
			edt_name.setVisibility(View.VISIBLE);
		} else {
			sendLog(Constant.SERACH_TYPE_CLICK);
			loadData(1);
		}
	}
	//1:下拉  2:上拉
	private void loadData(int mode){
		ProgressTools.showDialog(this);
		if(mode == 1){
			pageno = 1;
		}else{
			pageno++;
		}
		Selector selector = Selector.from(Product.class);
		String showType = getIntent().getStringExtra("showType");
		String keyword = edt_name.getText().toString();
		if(StringTools.equals(showType, "all")) {
			if(StringTools.isNotBlank(keyword)){
				WhereBuilder wb = WhereBuilder.b("searchtxt", "like", "%"+keyword+"%");
				selector.where(wb);
			}
		} else {
			selector.where("typeid", "=", model.getId());
			if(StringTools.isNotBlank(keyword)){
				WhereBuilder wb = WhereBuilder.b("searchtxt", "like", "%"+keyword+"%");
				selector.and(wb);
			}
		}
		selector.orderBy("sorting");
		selector.orderBy("searchtxt");
		new ProductListTask(selector, pageno, new CallBack<PageBody<Product>>() {
			@Override
			public void onSuccess(PageBody<Product> page) {
				ProgressTools.hide();
				emptyView.show(true);
				lv.onRefreshComplete();
				if(page.pageno == 1){
					adapter.setData(page.rdata);
				}else{
					adapter.add(page.rdata);
				}
				if (page.pageno >= page.pagecount) {
					lv.setMode(Mode.PULL_FROM_START);
				} else {
					lv.setMode(Mode.BOTH);
				}
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				lv.onRefreshComplete();
			}
		}).execute();
	}
	
	private void sendLog(int searchType){
		SearchData data = null;
		String name = edt_name.getText().toString();
		if(StringTools.isNotBlank(name)){
			data = new SearchData();
			data.searchtxt = name;
		}
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, 0, Constant.TYPE_PRODUCT, searchType, data);
	}
	
	private void updateCompany(){
		ApiUtil.invokeCompany(new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
				if(map != null && map.get(TimeRecordType.COMPANY_INFO) != null){
					if(map.get(TimeRecordType.COMPANY_INFO) > 0){
						loadData(1);
					}
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 100) {//点击CompanyInfo 返回
			if(data == null) {
				return;
			}
			Long productId = data.getLongExtra("productId", 0);
			Integer isfav = data.getIntExtra("isfav", 0);
			int count = adapter.getCount();
			for(int i = 0; i < count; i++) {
				Product product = adapter.getItem(i);
				if(product.getId() == productId) {
					product.setIsfav(isfav);
					break;
				}
			}
			adapter.notifyDataSetChanged();
		}
	}


}
