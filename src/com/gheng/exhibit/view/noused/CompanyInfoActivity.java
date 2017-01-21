package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.CompanyInfoProductAdapter;
import com.gheng.exhibit.widget.PopMenu;
import com.gheng.exhibit.widget.TabPager;
import com.gheng.exhibit.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 展商详情
 * 
 * @author lileixing
 */
public class CompanyInfoActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.iv)
	private ImageView iv;

	@ViewInject(R.id.tv_name)
	private TextView tv_name;

	// 展位
	@ViewInject(R.id.tv_exhibit_label)
	private TextView tv_exhibit_label;
	@ViewInject(R.id.tv_exhibit)
	private TextView tv_exhibit;

	private long id;

	private String h72;

	private CompanyInfoProductAdapter adapter;

	private Company model;

	private String code;
	private String floorName;

	// tab页面
	@ViewInject(R.id.tabPager)
	private TabPager tabPager;

	private PullToRefreshListView lv;

	private TextView tv_remark;

	private TextView tv_not_found;

	private TextView tv_not_found_info;

	// 评论
	@ViewInject(R.id.layout_comment_to)
	private View layout_comment_to;
	@ViewInject(R.id.tv_comment)
	private TextView tv_comment;
	// 收藏
	@ViewInject(R.id.layout_fav)
	private View layout_fav;
	@ViewInject(R.id.iv_fav)
	private ImageView ivFav;
	@ViewInject(R.id.tv_fav)
	private TextView tvFav;

	// 展位图
	@ViewInject(R.id.layout_location)
	private View layout_location;

	@ViewInject(R.id.tv_loaction)
	private TextView tv_loaction;
	AlertDialog dialog = null;

	private PopMenu popMenu;
//	private SNSUtils snsUtils;
	@ViewInject(R.id.tv)
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_info);
		id = getIntent().getLongExtra("id", 0);
		code = getIntent().getStringExtra("code");
		floorName = getIntent().getStringExtra("floorName");
	}

	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString("展商详情"));
		I18NUtils.setTextView(tv_exhibit_label, getLanguageString("展位") + ": ");
		tvFav.setText(getLanguageString("收藏"));
		tv_comment.setText(getLanguageString("评论"));
		tv_loaction.setText(getLanguageString("展位图"));
	}
	
	private void showPopMenu(){
		if(popMenu == null){
//			snsUtils = new SNSUtils(this);
			popMenu = new PopMenu(this);
			popMenu.addItem(getLanguageString("首页"), R.drawable.home);
			popMenu.addItem(getLanguageString("分享"), R.drawable.share);
			
			popMenu.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					popMenu.dismiss();
					switch (position) {
					case 0:
//						UIUtils.goHome();
						break;
					case 1:
						String title = getResources().getString(R.string.app_name);
						title = getLanguageString(title);
						String t = (String) I18NUtils.getValue(model, "name");
						String code = t+" " + getLanguageString("展位")+": "+ (String)model.getStandreferenceNew();
						String logo = (String)model.getLogo();
						if(StringTools.isNotBlank(logo)){
							logo = AppTools.imageChange(logo);
						}
//						snsUtils.share(title, code,logo);
						break;
					}
				}
			});
		}
		popMenu.showAsDropDown(tv);
	}

	@Override
	protected void init() {
		sendLog();
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
				showPopMenu();
			}

			@Override
			public void clickLeftImage() {

				finish();
			}
		});
		adapter = new CompanyInfoProductAdapter(this);
		h72 = this.getResources().getString(R.string.h72);

		initTabPager();

		tvFav.setOnClickListener(this);
		tv_loaction.setOnClickListener(this);
		loadData();
		
		updateProductFromServer();
	}

	private void initTabPager() {
		adapter = new CompanyInfoProductAdapter(this);
		List<View> views = new ArrayList<View>();
		View v2 = getLayoutInflater().inflate(R.layout.view_company_product,
				null);
		lv = (PullToRefreshListView) v2.findViewById(R.id.lv);
		lv.setAdapter(adapter);
		lv.setMode(Mode.DISABLED);
		lv.setOnItemClickListener(this);
		tv_not_found = (TextView) v2.findViewById(R.id.tv_not_found);
		tv_not_found.setText(getLanguageString("未找到相关展品"));

		View v1 = getLayoutInflater().inflate(R.layout.view_company_info, null);
		tv_remark = (TextView) v1.findViewById(R.id.tv_remark);
		tv_not_found_info = (TextView) v1.findViewById(R.id.tv_not_found_info);
		tv_not_found_info.setText(getLanguageString("未找到展商详情"));

		views.add(v1);
		views.add(v2);

		tabPager.setViews(views);
		tabPager.setTexts(new String[] { getLanguageString("展商详情"),
				getLanguageString("相关展品") });
		tabPager.select(0);
	}

	private void loadData() {
		final String message = context.getLanguageString("抱歉,未找到信息");
		if (StringTools.isBlank(code)) {
			http.getById(id, new CallBack<Company>() {
				@Override
				public void onSuccess(Company entity) {
					model = entity;
					if (entity != null) {
						updateView(entity);
						loadProduct();
					} else {
						toastLong(message);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					toastLong(message);
				}
			});
		} else {
			http.getByCode(floorName, code, new CallBack<Company>() {
				@Override
				public void onSuccess(Company entity) {
					model = entity;
					if (entity != null) {
						id = model.getId();
						updateView(entity);
						loadProduct();
					} else {
						toastLong(message);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					toastLong(message);
				}
			});
		}
	}

	private void updateView(Company entity) {
		layout_comment_to.setOnClickListener(this);
		layout_fav.setOnClickListener(this);
		layout_location.setOnClickListener(this);

		if (StringTools.isNotBlank(model.getZoneid())
				&& model.getZoneid().startsWith(h72)) {
		}

		bitmapUtils.display(iv, AppTools.imageChange(entity.getLogo()));
		tv_name.setText((String) I18NUtils.getValue(entity, "name"));
		String remark = (String) I18NUtils.getValue(entity, "remark");
		if (StringTools.isNotBlank(remark)) {
			tv_not_found_info.setVisibility(View.GONE);
		}
		tv_remark.setText(remark);
		tv_exhibit.setText(entity.getStandreferenceNew());
		if (model.getIsfav() == 1) {
			ivFav.setImageResource(R.drawable.fav2);
		} else {
			ivFav.setImageResource(R.drawable.fav);
		}
	}

	@Override
	public void onClick(View v) {
		Bundle bd = new Bundle();
		switch (v.getId()) {
		case R.id.layout_comment_to:
			bd.putInt("type", Constant.TYPE_COMPANY);
			bd.putSerializable("model", model);
			startTo(CommentListActivity.class, bd);
			break;
		case R.id.layout_fav:
			postFav();
			break;
		case R.id.tv_loaction:
		case R.id.layout_location:
			goToMap();
			break;
		default:

			break;
		}
	}

	private void updateProductFromServer() {
		ApiUtil.invokeProduct(new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
				if (map != null && map.get(TimeRecordType.PRODUCT_INFO) != null) {
					if (map.get(TimeRecordType.PRODUCT_INFO) > 0) {
						loadProduct();
					}
				}
			}
		});
	}

	private void sendLog() {
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_INFO, id,
				Constant.TYPE_COMPANY, Constant.SERACH_TYPE_ENTER, null);
	}

	// 产品模块
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				List<Product> list = (List<Product>) msg.obj;
				if (AppTools.isNotBlack(list)) {
					tv_not_found.setVisibility(View.GONE);
					adapter.setData(list);
				} else {
					tv_not_found.setText(getLanguageString("未找到相关展品"));
				}
				break;
			}
		};
	};

	private void loadProduct() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Selector selector = Selector.from(Product.class);
				ApiUtil.changeSelector(selector);
				selector.and("companyid", "=", id);
				selector.limit(30);
				selector.orderBy("searchtxt");
				List<Product> list = null;
				try {
					list = BaseActivity.getDbUtils().findAll(selector);
				} catch (DbException e) {
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.obj = list;
				handler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bd = new Bundle();
		bd.putLong("id", adapter.getItem(position - 1).getId());
		startTo(ProductInfoActivity.class, bd);
	}

	/**
	 * 收藏操作
	 */
	private void postFav() {
		if (!AppTools.isLogin()) {
//			UIUtils.startToRegister((BaseActivity) context);
			return;
		}
		ProgressTools.showDialog(this);
		int mode = 0;
		if (model.getIsfav() == 0) {
			mode = 1;
		} else {
			mode = 0;
		}
		ApiUtil.postFav(id, Constant.TYPE_COMPANY, mode, new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
			}

			@Override
			public void callback(boolean success, long id, int type, int mode,
					Object data) {
				ProgressTools.hide();
				String[] strs = { "取消收藏成功", "收藏成功" };
				String message = ((BaseActivity) context)
						.getLanguageString(strs[mode]);
				if (success) {
					((BaseActivity) context).toastShort(message);
					Intent intent = new Intent();
					if (mode == 0) {
						model.setIsfav(0);
						ivFav.setImageResource(R.drawable.fav);
						intent.putExtra("companyId", model.getId());
						intent.putExtra("isfav", model.getIsfav());
						CompanyInfoActivity.this.setResult(100, intent);
					} else {
						model.setIsfav(1);
						ivFav.setImageResource(R.drawable.fav2);
						intent.putExtra("companyId", model.getId());
						intent.putExtra("isfav", model.getIsfav());
						CompanyInfoActivity.this.setResult(100, intent);
					}
				} else {
					((BaseActivity) context).toastNetError();
				}
			}
		});
	}

	// ====跳转地图============
	private void goToMap() {
		List<Map<String, String>> list = model.getStandreferences();
		if (list.size() == 0) {
			toastShort(this.getLanguageString("暂无展位号"));
		} else if (list.size() == 1) {
			Map<String, String> map = list.get(0);
			toMapActivity(map.get("zone"), map.get("no"));
		} else {
			showDialog(list);
		}
	}

	private void showDialog(final List<Map<String, String>> list) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		CharSequence[] items = new CharSequence[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> m = list.get(i);
			items[i] = m.get("all");
		}
		builder.setTitle(getLanguageString("选择展位"));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int index) {
				Map<String, String> map = list.get(index);
				toMapActivity(map.get("zone"), map.get("no"));
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	private void toMapActivity(String zoneId, String no) {
//		Bundle bd = new Bundle();
//		int toMapTypeKey = 0;
//		bd.putInt(Constant.TO_MAP_TYPE_TKEY, toMapTypeKey);
//		bd.putInt(Constant.SEARCH_TYPE, Constant.FROM_EXHIBIT);
//		bd.putSerializable(Constant.MODEL_KEY, model);
//		bd.putString("floorName", zoneId);
//		bd.putString("floorNo", no);
//		bd.putString("title", zoneId);
//		startTo(MapShowActivity.class, bd);
		if(StringTools.isNotBlank(zoneId) && zoneId.toUpperCase().startsWith("H")){
			String index = zoneId.substring(1); 
			String url ="file:///android_asset/no"+index+".png";
			Bundle bd = new Bundle();
			bd.putString("title", "Hall"+index);
			bd.putString("web_url", url);
			bd.putInt("browsetype", Constant.BROWSE_TYPE_INFO);
			bd.putInt("type", Constant.TYPE_BATCH_IMAGE);
			bd.putBoolean("log", true);
			startTo(WebViewActivity.class, bd);
		}else{
			toastShort(getLanguageString("展位号不正确"));
		}
	}
}
