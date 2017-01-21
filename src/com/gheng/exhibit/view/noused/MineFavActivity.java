package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gheng.exhibit.database.task.ProductListTask;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.MainActivity;
import com.gheng.exhibit.view.adapter.CompanyAdapter;
import com.gheng.exhibit.view.adapter.MyViewPapgerAdapter;
import com.gheng.exhibit.view.adapter.ProductAdapter;
import com.gheng.exhibit.widget.LetterLayout.OnClickLetterListener;
import com.gheng.exhibit.widget.MyViewPager;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的收藏
 * 
 * @author lileixing
 */
public class MineFavActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, OnClickLetterListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.radio_group)
	private RadioGroup radio_group;

	@ViewInject(R.id.vp)
	private MyViewPager vp;

	private MyViewPapgerAdapter adapter;

	@ViewInject(R.id.rbtn_company)
	private RadioButton rbtn_company;

	@ViewInject(R.id.rbtn_product)
	private RadioButton rbtn_product;

	private ViewHolder holder1 = new ViewHolder();

	private ViewHolder2 holder2 = new ViewHolder2();
	
	@ViewInject(R.id.tab_group)
	private LinearLayout tab_group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_fav);
		titleBar.setText(getLanguageString("我的收藏"));
		
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.home);
		titleBar.setRightDrawable(drawable);
		titleBar.showRightImage(true);
	}

	@Override
	protected void setI18nValue() {
		I18NUtils.setPullView(holder1.lv, this);
		I18NUtils.setPullView(holder2.lv, this);

		I18NUtils.setTextView(rbtn_company, getLanguageString("展商"));
		I18NUtils.setTextView(rbtn_product, getLanguageString("展品"));

	}

	@Override
	protected void init() {
		List<View> lists = new ArrayList<View>();
		View v1 = getLayoutInflater().inflate(R.layout.pull_listview, null);
		View v2 = getLayoutInflater().inflate(R.layout.pull_listview, null);
		lists.add(v1);
		lists.add(v2);

		holder1.lv = (PullToRefreshListView) v1;
		holder1.adapter = new CompanyAdapter(this);
		holder1.adapter.setMine(true);
		holder1.lv.setAdapter(holder1.adapter);

		holder2.lv = (PullToRefreshListView) v2;
		holder2.adapter = new ProductAdapter(this);
		holder2.adapter.setMine(true);
		holder2.lv.setAdapter(holder2.adapter);

		holder1.lv.setOnItemClickListener(new OnItemClickImpl(TYPE_COMPANY));
		holder2.lv.setOnItemClickListener(new OnItemClickImpl(TYPE_PRODUCT));

		adapter = new MyViewPapgerAdapter(lists);
		vp.setAdapter(adapter);
		vp.setOffscreenPageLimit(3);

		titleBar.setOnClickListener(this);
		radio_group.setOnCheckedChangeListener(this);

		holder1.lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadCompany(2);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadCompany(3);
			}
		});
		holder2.lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadProduct(2);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadProduct(3);
			}
		});
		loadCompany(1);
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, 0, Constant.TYPE_MINE_FAV, Constant.SERACH_TYPE_ENTER, null);
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {
		startTo(MainActivity.class);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_company: // 展商
			vp.setCurrentItem(0, false);
			if(holder1.pageno == -1){
				loadCompany(1);
			}
			showIndex(0);
			break;
		case R.id.rbtn_product: // 展品
			vp.setCurrentItem(1, false);
			if(holder2.pageno == -1){
				loadProduct(1);
			}
			showIndex(1);
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
	
	static final int TYPE_COMPANY = 1;
	static final int TYPE_PRODUCT = 2;

	class OnItemClickImpl implements OnItemClickListener {

		private int type;

		OnItemClickImpl(int type) {
			this.type = type;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Bundle bd = new Bundle();
			switch (type) {
			case TYPE_COMPANY:
				Company company = holder1.adapter.getItem(position - 1);
				bd.putLong("id", company.getId());
				startTo(CompanyInfoActivity.class, bd);
				break;
			case TYPE_PRODUCT:
				Product item = holder2.adapter.getItem(position - 1);
				bd.putLong("id", item.getId());
				startTo(ProductInfoActivity.class, bd);
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
		selector.where("isfav", "=", 1);
		ApiUtil.changeSelector(selector);
		selector.orderBy("id");
		http.postToDataBase(selector, holder1.pageno, new CallBack<PageBody<Company>>() {
			@Override
			public void onSuccess(PageBody<Company> page) {
				holder1.lv.onRefreshComplete();
				ProgressTools.hide();
				if (page.pageno == 1) {
					holder1.adapter.setData(page.rdata);
				} else {
					holder1.adapter.add(page.rdata);
				}
				if (page.pageno < page.pagecount) {
					holder1.lv.setMode(Mode.BOTH);
				} else {
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

	private void loadProduct(int mode) {
		if (mode == 1) {
			holder2.pageno = 1;
			ProgressTools.showDialog(context);
		} else if (mode == 2) {
			holder2.pageno = 1;
		} else {
			holder2.pageno++;
		}
		Selector selector = Selector.from(Product.class);
		selector.where("isfav", "=", 1);
		ApiUtil.changeSelector(selector);
		selector.orderBy("id");
		new ProductListTask(selector, holder2.pageno, new CallBack<PageBody<Product>>() {
			@Override
			public void onSuccess(PageBody<Product> page) {
				ProgressTools.hide();
				holder2.lv.onRefreshComplete();
				if(page.pageno == 1){
					holder2.adapter.setData(page.rdata);
				}else{
					holder2.adapter.add(page.rdata);
				}
				if (page.pageno >= page.pagecount) {
					holder2.lv.setMode(Mode.PULL_FROM_START);
				} else {
					holder2.lv.setMode(Mode.BOTH);
				}
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				holder2.lv.onRefreshComplete();
			}
		}).execute();
	}

	class ViewHolder {
		PullToRefreshListView lv;
		CompanyAdapter adapter;
		int pageno = -1;
	}

	class ViewHolder2 {
		PullToRefreshListView lv;
		ProductAdapter adapter;
		int pageno = -1;
	}

	@Override
	public void onClick(TextView tv) {

	}

}
