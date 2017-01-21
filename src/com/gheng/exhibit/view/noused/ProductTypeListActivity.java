package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.ProductType;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.MyViewPapgerAdapter;
import com.gheng.exhibit.view.adapter.ProductTypeAdapter;
import com.gheng.exhibit.widget.LetterLayout.OnClickLetterListener;
import com.gheng.exhibit.widget.MyViewPager;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 产品类型列表
 * 
 * @author lileixing
 */
public class ProductTypeListActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener, OnClickLetterListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.radio_group)
	private RadioGroup radio_group;

	@ViewInject(R.id.lv)
	private ExpandableListView lv;

	@ViewInject(R.id.vp)
	private MyViewPager vp;

	private MyViewPapgerAdapter adapter;

	@ViewInject(R.id.rbtn_cmef)
	private RadioButton rbtn_cmef;

	@ViewInject(R.id.rbtn_icmd)
	private RadioButton rbtn_icmd;

	@ViewInject(R.id.rbtn_other)
	private RadioButton rbtn_other;

	@ViewInject(R.id.tab_group)
	private LinearLayout tab_group;

	@ViewInject(R.id.edt_name)
	private EditText edt_name;

	private ViewHolder holder1 = new ViewHolder();

	private ViewHolder holder2 = new ViewHolder();

	private ViewHolder holder3 = new ViewHolder();

	private List<ProductType> root = new ArrayList<ProductType>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_type_list);
	}

	@Override
	protected void setI18nValue() {

	}

	@Override
	protected void init() {
		// titleBar.setText(getLanguageString("展品分类"));
		edt_name.setHint(getLanguageString("搜索展品/展商名/展位号"));
		edt_name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bd = new Bundle();
				bd.putString("showType", "all");// 全部显示
				startTo(ProductListActivity.class, bd);
			}
		});
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
				Bundle bd = new Bundle();
				bd.putString("showType", "all");// 全部显示
				startTo(ProductListActivity.class, bd);
			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		List<View> lists = new ArrayList<View>();
		View v1 = getLayoutInflater().inflate(R.layout.expandable_listview,
				null);
		View v2 = getLayoutInflater().inflate(R.layout.expandable_listview,
				null);
		View v3 = getLayoutInflater().inflate(R.layout.expandable_listview,
				null);
		lists.add(v1);
		lists.add(v2);
		lists.add(v3);

		holder1.lv = (ExpandableListView) v1;
		holder1.adapter = new ProductTypeAdapter(this);
		holder1.lv.setAdapter(holder1.adapter);
		holder1.lv.setOnChildClickListener(new OnChildClickImpl());

		holder2.lv = (ExpandableListView) v2;
		holder2.adapter = new ProductTypeAdapter(this);
		holder2.lv.setAdapter(holder2.adapter);
		holder2.lv.setOnChildClickListener(new OnChildClickImpl());

		holder3.lv = (ExpandableListView) v3;
		holder3.adapter = new ProductTypeAdapter(this);
		holder3.lv.setAdapter(holder3.adapter);
		holder3.lv.setOnGroupClickListener(new OnGroupClickImpl());

		adapter = new MyViewPapgerAdapter(lists);
		vp.setAdapter(adapter);
		vp.setOffscreenPageLimit(3);
		radio_group.setOnCheckedChangeListener(this);

		loadData();
		updateProductFromServer();
	}

	private void updateProductFromServer() {
		ApiUtil.invokeProduct(new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
				if (map != null && map.get(TimeRecordType.PRODUCT_TYPE) != null) {
					if (map.get(TimeRecordType.PRODUCT_TYPE) > 0) {
						loadData();
					}
				}
			}
		});
	}

	// 加载产品分类及分组
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				List<ProductType> list = (List<ProductType>) msg.obj;
				for (int i = 0; i < list.size(); i++) {
					ProductType parent = list.get(i);
					if (i == 0) {
						holder1.adapter.setDatas(parent.children);
					} else if (i == 1) {
						holder2.adapter.setDatas(parent.children);
					} else if (i == 2) {
						holder3.adapter.setDatas(parent.children);
					}
				}
				ProgressTools.hide();
				break;
			case 2:
				setNames();// 设置标签名称
				break;
			case 3:
				ProgressTools.hide();
				break;
			}
		}
	};

	private void loadData() {
		ProgressTools.showDialog(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Selector selector = Selector.from(ProductType.class);
				ApiUtil.changeSelector(selector);
				selector.orderBy("sorting");
				try {
					List<ProductType> list = getDbUtils().findAll(selector);
					if (AppTools.isBlack(list)) {
						handler.sendEmptyMessage(3);
						return;
					}
					// 响应结果
					List<ProductType> result = new ArrayList<ProductType>();
					for (int i = 0; i < list.size(); i++) {
						ProductType model = list.get(i);
						if (model.getPid() == null) {
							result.add(model);
						}
					}
					list.removeAll(result);
					root.addAll(result);
					handler.sendEmptyMessage(2);
					loadChildData(result, list);
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = result;
					handler.sendMessage(msg);
				} catch (DbException e) {
					handler.sendEmptyMessage(3);
					e.printStackTrace();
				}
			}
		}).start();
	}

	private ProductType findByPid(List<ProductType> result, long pid) {
		for (ProductType productType : result) {
			if ((long) productType.getId() == pid) {
				return productType;
			}
		}
		return null;
	}

	class ViewHolder {
		ExpandableListView lv;
		ProductTypeAdapter adapter;
	}

	@Override
	public void onClick(TextView tv) {
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_cmef:
			vp.setCurrentItem(0, false);
			showIndex(0);
			break;
		case R.id.rbtn_icmd:
			vp.setCurrentItem(1, false);
			showIndex(1);
			break;
		case R.id.rbtn_other:
			vp.setCurrentItem(2, false);
			showIndex(2);
			break;
		}
	}

	@Override
	public void clickLeftImage() {
	}

	@Override
	public void clickRightImage() {

	}

	private void setNames() {
		int childCount = radio_group.getChildCount();
		int index = 0;
		for (int i = 0; i < 2; i++) {
			View view = radio_group.getChildAt(i);
			if (view instanceof RadioButton) {
				RadioButton rb = (RadioButton) view;
				rb.setText((String) I18NUtils.getValue(root.get(index), "name"));
				index++;
			}
		}
	}

	private void loadChildData(List<ProductType> result, List<ProductType> list) {
		List<ProductType> removes = new ArrayList<ProductType>();
		for (ProductType productType : list) {
			ProductType findByPid = findByPid(result, productType.getPid());
			if (findByPid != null) {
				findByPid.children.add(productType);
				removes.add(productType);
			}
		}
		list.removeAll(removes);
		for (ProductType productType : list) {
			ProductType findByPid = findByPid(removes, productType.getPid());
			if (findByPid != null) {
				findByPid.children.add(productType);
			}
		}
	}

	private void showIndex(int index) {
		int childCount = tab_group.getChildCount();
		for (int i = 0; i < childCount; i++) {
			if (index == i) {
				tab_group.getChildAt(i).setVisibility(View.VISIBLE);
			} else {
				tab_group.getChildAt(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 点击事件
	 * 
	 * @author zhaofangfang
	 */
	class OnChildClickImpl implements OnChildClickListener {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			ProductTypeAdapter adapter = (ProductTypeAdapter) parent
					.getExpandableListAdapter();
			Bundle bd = new Bundle();
			ProductType productType = adapter.getGroup(groupPosition).children
					.get(childPosition);
			bd.putSerializable("model", productType);
			startTo(ProductListActivity.class, bd);
			return false;
		}
	}

	class OnGroupClickImpl implements OnGroupClickListener {
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			ProductTypeAdapter adapter = (ProductTypeAdapter) parent
					.getExpandableListAdapter();
			Bundle bd = new Bundle();
			ProductType productType = adapter.getGroup(groupPosition);
			bd.putSerializable("model", productType);
			startTo(ProductListActivity.class, bd);
			return false;
		}
	}

}
