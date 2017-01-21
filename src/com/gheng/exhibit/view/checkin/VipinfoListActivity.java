package com.gheng.exhibit.view.checkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.VipInfoListBaen;
import com.gheng.exhibit.http.body.response.VipInfoListBaen.InfoBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.CharacterParser_Hs;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.adapter.FriendsAdapter;
import com.gheng.exhibit.view.adapter.FriendsSearchAdapter;
import com.gheng.exhibit.widget.BladeView;
import com.gheng.exhibit.widget.BladeView.OnItemClickListener;
import com.gheng.exhibit.widget.PinnedHeaderListView;
import com.hebg3.mxy.utils.VipListTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;

@SuppressLint("InlinedApi")
public class VipinfoListActivity extends Activity implements OnClickListener {
	private Button iv_back;
	private TextView tv_title;
	private View view;
	EditText search_et;
	private TextView vil_searchEmpty;
	// private ListView listView;
	PinnedHeaderListView listView;
	ListView search_lv;
	ArrayList<InfoBean> search_list = new ArrayList<>();
	FriendsSearchAdapter search_adapter;
	private Context mContext;
	private ArrayList<InfoBean> listData = new ArrayList<>();
	private ArrayList<String> usernames = new ArrayList<>();
	// private VipInfoListAdapter adapter;
	FriendsAdapter adapter;
	private User user;
	private String userid;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				VipInfoListBaen result = (VipInfoListBaen) msg.obj;
				listData.clear();
				listData.addAll(result.getInfo());
				// adapter.notifyDataSetChanged();
				process();
				initListView();
				break;
			case -1:
				Toast.makeText(mContext, "---->嘉宾列表信息请求有误", Toast.LENGTH_LONG)
						.show();
				break;
			case -2:
				Toast.makeText(mContext, "---->嘉宾列表信息为空", Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
		};
	};

	// zyj
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<String>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;
	// 新增内容...................................
	// 汉字转换成拼音的类
	private CharacterParser_Hs characterParser;
	private BladeView mLetter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipinfo_list);
//		setStatusBar();
		mContext = this;
		initView();

		loadData();
		setAllClick();
	}

	/**
	 * 透明状态栏
	 */
//	public void setStatusBar() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			Window window = getWindow();
//			// Translucent status bar
//			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		} else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
//			view.setVisibility(View.GONE);
//		}else {
//			view.setVisibility(View.GONE);
//		}
//	}

	private void initView() {
		vil_searchEmpty = (TextView) findViewById(R.id.vil_searchEmpty);
		search_lv = (ListView) findViewById(R.id.search_lv);
		search_et = (EditText) findViewById(R.id.search_et);
		listView = (PinnedHeaderListView) findViewById(R.id.vil_listview);
		mLetter = (BladeView) findViewById(R.id.friends_myletterlistview);
		iv_back = (Button) findViewById(R.id.but_fanhui);
		iv_back.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.in_title);
		tv_title.setText("嘉宾信息");
//		view = findViewById(R.id.vil_view);
		//
		search_adapter = new FriendsSearchAdapter(mContext, search_list);
		search_lv.setAdapter(search_adapter);
	}

	private void process() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser_Hs.getInstance();
		mSections = new ArrayList<String>();
		mMap = new HashMap<String, List<String>>();
		mPositions = new ArrayList<Integer>();
		mIndexer = new HashMap<String, Integer>();

		for (int i = 0; i < listData.size(); i++) {
			String pinyin = characterParser.getSelling(listData.get(i)
					.getUsername());
			String firstName = pinyin.substring(0, 1).toUpperCase();
			Log.i("firstName", "firstName=" + firstName + "pingyin=" + pinyin);
			if (firstName.matches("[A-Z]")) {
				if (mSections.contains(firstName)) {
					mMap.get(firstName).add(listData.get(i).getUsername());//
				} else {
					mSections.add(firstName);// 给集合添加数据
					List<String> list = new ArrayList<String>();
					list.add(listData.get(i).getUsername());
					mMap.put(firstName, list);// 给Map添加新数据
				}
			} else {
				if (mSections.contains("#")) {
					mMap.get("#").add(listData.get(i).getUsername());
				} else {
					mSections.add("#");
					List<String> list = new ArrayList<String>();
					list.add(listData.get(i).getUsername());
					mMap.put("#", list);
				}
			}
		}
		Log.i("mMap", "mMap" + mMap);
		Collections.sort(mSections);
		Log.i("mSections", "mSections" + mSections);
		int position = 0;
		usernames.clear();
		for (int i = 0; i < mSections.size(); i++) {
			mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
			mPositions.add(position);// 首字母在listview中位置，存入list中
			position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
			usernames.addAll(mMap.get(mSections.get(i)));
		}
	}

	private void initListView() {
		mLetter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					listView.setSelection(mIndexer.get(s));
//					Toast.makeText(mContext, "=====mIndexer.get(s)==>"+mIndexer.get(s), Toast.LENGTH_LONG).show();
				}
			}
		});
		adapter = new FriendsAdapter(this, usernames, mSections, mPositions,
				listData);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(adapter);
		listView.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.vipinfo_lv_item_head, listView, false));
	}

	/**
	 * 从网络加载数据
	 */
	private void loadData() {
		// 查找
		try {
			DbUtils db = DbUtils.create(mContext);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			userid = Constant.decode(Constant.key, user.getUserId());
			Log.d("tag", "=======MainActivity--->userid=" + userid);
			db.close();
			new VipListTask(handler.obtainMessage(), userid, mContext)
					.execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_fanhui:
			finish();
			break;

		default:
			break;
		}
	}

	private void setAllClick() {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView vil_tv_name = (TextView) view
						.findViewById(R.id.vil_tv_name);
				InfoBean infobean = null;
				for (int i = 0; i < listData.size(); i++) {
					if (vil_tv_name.getText().toString()
							.equals(listData.get(i).getUsername())) {
						infobean = listData.get(i);
					}
				}
				if (infobean != null) {
					Intent intent = new Intent(mContext, VIPInfoActivity.class);
					intent.putExtra("vipid", infobean.getvipId());
					intent.putExtra("title", "嘉宾信息");
					startActivity(intent);
				}
			}
		});
		search_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				System.out.println("======onTextChanged=====s=>" + s.length());
				if (s.length() != 0) {

					listView.setVisibility(View.GONE);
					mLetter.setVisibility(View.GONE);
					search_lv.setVisibility(View.VISIBLE);
					InfoBean infobean = null;
					search_list.clear();
					for (int i = 0; i < listData.size(); i++) {
						if (listData.get(i).getUsername().contains(s)) {
							infobean = listData.get(i);
							search_list.add(infobean);
						}
					}
					search_adapter.notifyDataSetChanged();
					if (search_list.size() == 0) {
						search_lv.setVisibility(View.GONE);
						vil_searchEmpty.setVisibility(View.VISIBLE);
					} else {
						search_lv.setVisibility(View.VISIBLE);
						vil_searchEmpty.setVisibility(View.GONE);
					}
				} else {
					search_lv.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					mLetter.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		search_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InfoBean infobean = search_list.get(position);
				if (infobean != null) {
					Intent intent = new Intent(mContext, VIPInfoActivity.class);
					intent.putExtra("vipid", infobean.getvipId());
					intent.putExtra("title", "嘉宾信息");
					startActivity(intent);
				}
			}
		});
	}
}
