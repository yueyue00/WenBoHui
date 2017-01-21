package com.gheng.exhibit.view.checkin;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.a.a.ad;
import cn.jpush.a.a.b;
import cn.jpush.a.a.i;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.CulturalExhibitListBean;
import com.gheng.exhibit.http.body.response.CulturalExhibitListBean.InfoBean;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.CulturalExhibitActivity;
import com.gheng.exhibit.view.adapter.CulturalExhibitListAdapter;
import com.gheng.exhibit.view.checkin.checkin.CultrualExhibitDetailActivity;
import com.hebg3.mxy.utils.CulturalExhibitListAsyncTask;
import com.hebg3.mxy.utils.VipListTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author lixiaoming
 *
 */
public class CulturalExhibitListActivity extends BaseActivity implements
		OnClickListener {

	private View view;
	private ImageButton ib_back;
	private TextView tv_title;
	private ImageButton ib_sliptopic;
	private ListView listView;
	private TextView tv_empty;

	private Context mContext;
	private List<InfoBean> list;
	private CulturalExhibitListAdapter adapter;
	private User  user;
	private String userid;
	private String location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cultureexhibittion_list);
		mContext = this;
		Intent intent = getIntent();
		location = intent.getStringExtra("location");
		initView();
		setStatusBar();
		initData();
		loadData();
		initAdapter();
	}
	
   /**
    * 加载网络数据
    */
	private void loadData() {
		// 查找
				try {
					DbUtils db = DbUtils.create(mContext);
					user = db
							.findFirst(Selector.from(User.class).where("id", "=", "1"));
					userid = Constant.decode(Constant.key, user.getUserId());
					System.out.println("========文化年展列表==userid=》"+userid);
					db.close();
					new CulturalExhibitListAsyncTask(handler.obtainMessage(), userid, location,mContext)
							.execute(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		list = new ArrayList<>();
		list.clear();
//		for (int i = 0; i < 2; i++) {
//			CulturalExhibitListBean bean = new CulturalExhibitListBean();
//			bean.setIcon("http://img.coinsky.com/tt/f/c/8/6/35ed8101c046ada8344aec78615d.jpg");
//			bean.setName("七尊佛");
//			bean.setLocation("五号厅B001");
//			bean.setIntrodece("七尊佛是精品中的精品，所以成了特窟。七尊佛是精品中的精品，所以成了特窟。");
//			list.add(bean);
//		}

	}
    /**
     * 初始化适配器
     */
	public void initAdapter() {
		adapter = new CulturalExhibitListAdapter(mContext, list);
		listView.setAdapter(adapter);
		listView.setEmptyView(tv_empty);
		if (SharedData.getInt("i18n", Language.ZH) == 1) {//中文
			tv_empty.setText("暂无数据");
		}else if (SharedData.getInt("i18n", Language.ZH) == 2) {//英文
			tv_empty.setText("No data");
		} 
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		 view = findViewById(R.id.cedl_view_l);
		tv_title = (TextView) findViewById(R.id.cedl_titletv_l);
		tv_title.setText(getLanguageString("展品列表"));
		ib_back = (ImageButton) findViewById(R.id.cedl_goback_l);
		ib_back.setOnClickListener(this);
		ib_sliptopic = (ImageButton) findViewById(R.id.cedl_sliptopic_l);
		ib_sliptopic.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.cedl_listview_l);
		 tv_empty = (TextView) findViewById(R.id.cedl_empty_l);
	}

	/**
	 * 透明状态栏
	 */
	public void setStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	@Override
	protected void setI18nValue() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cedl_goback_l:
			finish();
			break;
		case R.id.cedl_sliptopic_l:
			Intent intent = new Intent(mContext, CulturalExhibitActivity.class);
			intent.putExtra("title", "文化年展");
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				String result = (String) msg.obj;
//				System.out.println("======文化年展列表数据====》"+result);
				CulturalExhibitListBean bean = (CulturalExhibitListBean) msg.obj;
				List<InfoBean> info = bean.getInfo();
				list.clear();
				list.addAll(info);
				adapter.notifyDataSetChanged();
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(CulturalExhibitListActivity.this,
								CultrualExhibitDetailActivity.class);
						intent.putExtra("uniqueCode", list.get(position).getUniqueCode());
						startActivityForResult(intent, 11);

					}
				});
				break;
           case 300:
        	   
           break;
			default:
				break;
			}
		};
	};
}
