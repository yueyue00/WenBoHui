package com.gheng.exhibit.view.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.KeywordAdapter;
import com.gheng.exhibit.view.adapter.MapSearchAdapter;
import com.gheng.exhibit.view.support.AboutData;
import com.gheng.exhibit.view.support.PoiData;
import com.gheng.exhibit.widget.SegmentedGroup;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 地图查询接口
 * 
 * @author zhaofangfang
 * 
 */
public class MapSearchActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener, OnItemClickListener {

	@ViewInject(R.id.gv)
	private GridView gv;

	@ViewInject(R.id.lv)
	private ListView lv;

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.searchType)
	private SegmentedGroup searchTypeBtn;

	@ViewInject(R.id.exhibitor)
	private RadioButton exhibitorButton;

	@ViewInject(R.id.exhibit)
	private RadioButton exhibitButton;

	@ViewInject(R.id.zone)
	private RadioButton zoneButton;

	@ViewInject(R.id.et)
	private EditText et;
	// 1展商 2展品
	private String s = "";
	// Poi类型
	KeywordAdapter adapter;

	private int requestCode = 0;

	private String floorName = "";

	int searchType = Constant.FROM_EXHIBIT;

	private MapSearchAdapter<Company> companyAdapter;
	private MapSearchAdapter<Product> producntAdapter;
	private MapSearchAdapter<PoiData> poiResultAdapter;
	private ArrayList<PoiData> poiDatas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_map_search);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.exhibitor:
			et.setText("");
			et.setHint(getLanguageString("展商/展位号"));
			searchType = Constant.FROM_EXHIBIT;
			lv.setVisibility(View.GONE);
			gv.setVisibility(View.VISIBLE);
			clearAdapter();
			return;
		case R.id.exhibit:
			et.setText("");
			et.setHint(getLanguageString("展品"));
			searchType = Constant.FROM_PRODUCT;
			lv.setVisibility(View.GONE);
			gv.setVisibility(View.VISIBLE);
			clearAdapter();
			return;
		case R.id.zone:
			et.setText("");
			et.setHint(getLanguageString("展区"));
			searchType = Constant.FROM_ZONE;
			lv.setVisibility(View.GONE);
			gv.setVisibility(View.VISIBLE);
			clearAdapter();
			break;
		}
	}

	@Override
	protected void init() {
		super.init();
		et.setHint(getLanguageString("展商/展位号"));
		titleBar.setText(this.getIntent().getStringExtra("title"));
		floorName = this.getIntent().getStringExtra("floorName");
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		requestCode = getIntent().getIntExtra("code", 0);
		int lg = SharedData.getInt("i18n", Language.ZH);

		String[] keywordArray = this.getResources().getStringArray(
				R.array.searchKeyword);
		String[] enKeywordArray = this.getResources().getStringArray(
				R.array.enSearchKeyword);
		poiDatas = (ArrayList<PoiData>) getIntent().getExtras()
				.getSerializable("poiDatas");
		List<AboutData> datas = new ArrayList<AboutData>();
		for (int i = 0; i < keywordArray.length; i++) {
			AboutData d = null;
			if (lg == Language.ZH) {
				d = new AboutData(keywordArray[i]);
				d.searchKey = keywordArray[i];
			} else {
				d = new AboutData(enKeywordArray[i]);
				d.searchKey = keywordArray[i];
			}
			datas.add(d);
		}
		adapter = new KeywordAdapter(this);
		adapter.setData(datas);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(this);
		lv.setOnItemClickListener(this);
		searchTypeBtn.setOnCheckedChangeListener(this);

		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				s = arg0.toString().trim();
				if (!StringTools.isBlank(s)) {
					if (searchType == 1) {
						Selector selector = Selector.from(Company.class);
						WhereBuilder nameBuilder = WhereBuilder.b("searchtxt","like", "%" + s + "%");
						WhereBuilder codeBuilder = WhereBuilder.b("searchtxt","like", "%H" + floorName + "%");
						selector.where(nameBuilder).and(codeBuilder);
						ApiUtil.changeSelector(selector);
						System.out.println(selector.toString());
						http.postToDataBase(selector, 1,
								new CallBack<PageBody<Company>>() {
									@Override
									public void onSuccess(PageBody<Company> page) {
										ProgressTools.hide();
										companyAdapter = new MapSearchAdapter<Company>(
												context);
										companyAdapter.setData(page.rdata);
										lv.setAdapter(companyAdapter);
									}

									@Override
									public void onFailure(HttpException error,
											String msg) {
									}
								});
					} else if (searchType == 2) {
						Selector selector = Selector.from(Product.class);
						WhereBuilder nameBuilder = WhereBuilder.b("searchtxt",
								"like", "%" + s + "%");
						WhereBuilder codeBuilder = WhereBuilder.b("searchtxt",
								"like", "%H" + floorName + "%");
						selector.where(nameBuilder).and(codeBuilder);
						ApiUtil.changeSelector(selector);
						http.postToDataBase(selector, 1,
								new CallBack<PageBody<Product>>() {
									@Override
									public void onSuccess(PageBody<Product> page) {
										ProgressTools.hide();
										producntAdapter = new MapSearchAdapter<Product>(
												context);
										producntAdapter.setData(page.rdata);
										lv.setAdapter(producntAdapter);
									}

									@Override
									public void onFailure(HttpException error,
											String msg) {
									}
								});
					} else if (searchType == 4) {
						List<PoiData> result2 = new ArrayList<PoiData>();
						for (PoiData poiData : poiDatas) {
							if ((StringTools.isNotBlank(poiData.name) && poiData.name
									.contains(s))
									|| (StringTools.isNotBlank(poiData.enName) && poiData.enName
											.contains(s))){
								if(StringTools.isBlank(poiData.poiType) || !poiData.poiType.startsWith("1013")){
									result2.add(poiData);
								}
							}
						}
						ProgressTools.hide();
						poiResultAdapter = new MapSearchAdapter<PoiData>(
								context);
						poiResultAdapter.setData(result2);
						lv.setAdapter(poiResultAdapter);
					}
					lv.setVisibility(View.VISIBLE);
					gv.setVisibility(View.GONE);
				} else {
					lv.setVisibility(View.GONE);
					gv.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected void setI18nValue() {
		exhibitorButton.setText(getLanguageString("展商"));
		exhibitButton.setText(getLanguageString("展品"));
		zoneButton.setText(getLanguageString("展区"));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Object adapter2 = arg0.getAdapter();
		Intent data = new Intent();
		Company company = null;
		if (adapter2 instanceof KeywordAdapter) {
			AboutData aboutData = adapter.getItem(position);
			String key = aboutData.searchKey;
			data.putExtra(Constant.SEARCH_TYPE, Constant.FROM_POITYPE);
			data.putExtra(Constant.KEYWORD, key);
			setResult(100, data);
			finish();
			return;
		} else {
			if (searchType == Constant.FROM_EXHIBIT) {
				data.putExtra(Constant.SEARCH_TYPE, Constant.FROM_EXHIBIT);
				data.putExtra(Constant.MODEL_KEY,
						companyAdapter.getItem(position));
				company = companyAdapter.getItem(position);
				companyAdapter.clear();
				setResult(requestCode, data);
			} else if (searchType == Constant.FROM_ZONE) {
				data.putExtra(Constant.SEARCH_TYPE, Constant.FROM_ZONE);
				data.putExtra(Constant.MODEL_KEY,
						poiResultAdapter.getItem(position));
				data.putExtra(Constant.KEYWORD,
						poiResultAdapter.getItem(position).getName());
				poiResultAdapter.clear();
				setResult(requestCode, data);
			} else if (searchType == Constant.FROM_PRODUCT) {
				data.putExtra(Constant.SEARCH_TYPE, Constant.FROM_PRODUCT);
				data.putExtra(Constant.MODEL_KEY,
						producntAdapter.getItem(position));
				producntAdapter.clear();
				setResult(requestCode, data);
			}
		}

		// 关闭掉这个Activity
		finish();
	}

	private void clearAdapter() {
		if (companyAdapter != null) {
			companyAdapter.clear();
		}
		if (producntAdapter != null) {
			producntAdapter.clear();
		}
		if (poiResultAdapter != null) {
			poiResultAdapter.clear();
		}
	}
}
