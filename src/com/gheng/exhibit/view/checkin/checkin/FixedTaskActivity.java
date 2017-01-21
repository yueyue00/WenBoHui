package com.gheng.exhibit.view.checkin.checkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.a.a.be;
import cn.jpush.a.a.l;

import com.dreamlive.hotimglibrary.entity.HotArea;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.FixedTaskCommonVipBean;
import com.gheng.exhibit.http.body.response.FixedTaskCommonVipBean.InfoBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.CharacterParser_Hs;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.CulturalExhibitActivity;
import com.gheng.exhibit.view.adapter.FixedTaskAdapter;
import com.gheng.exhibit.view.adapter.FriendsAdapter;
import com.gheng.exhibit.view.adapter.PinnearHeaderListViewAdapter;
import com.gheng.exhibit.view.checkin.CulturalExhibitListActivity;
import com.gheng.exhibit.widget.BladeView;
import com.gheng.exhibit.widget.PinnedHeaderListView;
import com.hebg3.mxy.utils.FixedTaskAsyncTask;
import com.hebg3.mxy.utils.TaskCommitAsyncTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;

public class FixedTaskActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	private Button ib_back;
	private View view;
	private TextView tv_title;
	private TextView tv_commit;
	private LinearLayout tv_selectall;
//	private ListView listView;
	private LinearLayout ft_select_ll;
	private TextView ft_tv_empty;
	private ImageView ft_iv_selecteAll;
	
	// 分组listview
	private LinearLayout ft_linear;
	private PinnedHeaderListView ft_PinnedHeaderlistview;
	private BladeView ft_myletterlistview;
	// 首字母集
	private List<String> mSections = new ArrayList<String>();
	// 根据首字母存放数据
	private Map<String, List<String>> mMap = new HashMap<String, List<String>>();
	// 首字母位置集
	private List<Integer> mPositions = new ArrayList<Integer>();
	// 首字母对应的位置
	private Map<String, Integer> mIndexer = new HashMap<String, Integer>();
	// 新增内容...................................
	// 汉字转换成拼音的类
	private CharacterParser_Hs characterParser= CharacterParser_Hs.getInstance();
    //pinnearHeaderListView适配器 	
	private PinnearHeaderListViewAdapter adapterForPinnear;
	//用于分组
	private ArrayList<String> usernames = new ArrayList<String>();

	private Context mContext;
	private String title;
	private int taskid;
	private String taskuniquecode;
	private List<InfoBean> datas = new ArrayList<>();
	private List<InfoBean> selectedDatas = new ArrayList<>();
//	private FixedTaskAdapter adapter;
	private String vipName;
	private boolean isSelectAll = false;// 用于判断是否是全选
	 boolean isEdit = false;
	private User user;
	private String userid;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				// String result = (String) msg.obj;
				// System.out.println("=========FixedTaskActivity======>"+result);
				FixedTaskCommonVipBean bean = (FixedTaskCommonVipBean) msg.obj;
				if (bean.getInfo() != null && bean.getInfo().size() !=0) {
					
				datas.clear();
				datas.addAll(bean.getInfo());
//				adapter.notifyDataSetChanged();
				System.out.println("=======fixedActivity----->" + datas.size());
				
				//分组listview
//				listData.clear();
//				listData.addAll(bean.getInfo());
				process();
				initListView();
				adapterForPinnear.notifyDataSetChanged();
                
				}else {
					ft_tv_empty.setVisibility(View.VISIBLE);
					ft_select_ll.setVisibility(View.GONE);
//					listView.setVisibility(View.GONE);
					//分组listview
					ft_linear.setVisibility(View.GONE);
				}
				break;
			case -1:
//				Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
				break;
			case -2:
//				Toast.makeText(mContext, "请求数据info为空", Toast.LENGTH_LONG)
//						.show();
				ft_tv_empty.setVisibility(View.VISIBLE);
				ft_select_ll.setVisibility(View.GONE);
//				listView.setVisibility(View.GONE);
				//分组listview
				ft_linear.setVisibility(View.GONE);
				break;
			case 300:
				
				ft_tv_empty.setVisibility(View.VISIBLE);
				ft_select_ll.setVisibility(View.GONE);
//				listView.setVisibility(View.GONE);
				//分组listview
				ft_linear.setVisibility(View.GONE);
//				Toast.makeText(mContext, "请求数据为空", Toast.LENGTH_LONG).show();
				break;
			case 400:
//				Toast.makeText(mContext, "请求失败", Toast.LENGTH_LONG).show();
				break;
			case 500:
//				Toast.makeText(mContext, "请求超时", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};
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
		setContentView(R.layout.activity_fixed_task);
		mContext = this;
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		taskid = intent.getIntExtra("taskid", 0);
		taskuniquecode = intent.getStringExtra("taskuniquecode");
		initview();
//		setStatusBar();
		selectedDatas.clear();
		getUserid();
		loadData();
//		adapter = new FixedTaskAdapter(mContext, datas);
//		adapter.setCheckedListener(this);
//		listView.setAdapter(adapter);
////		 zyj 添加
//		listView.setOnItemClickListener(new OnItemClickListener() {
//            
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				CheckBox cb = (CheckBox) view.findViewById(R.id.ft_item_cb);
//				if (cb.isChecked()) {
//						cb.setChecked(false);
//				} else {
//					cb.setChecked(true);//该方法会自动调用onCheckedChangeListenter
//				}
//				System.out.println("========list_itemClick===renshu===>"+selectedDatas.size());
////				Toast.makeText(mContext, "====list==itemClick===renshu->"+selectedDatas.size(), Toast.LENGTH_LONG).show();
//			}
//		});
		System.out.println("========onCreate=isSelectAll--->"+isSelectAll);
		//分组listview
		adapterForPinnear = new PinnearHeaderListViewAdapter(this, usernames, mSections, mPositions,
				datas);
		adapterForPinnear.setCheckedListener(this);
		ft_PinnedHeaderlistview.setAdapter(adapterForPinnear);
	}

	/**
	 * 获取到用于网络请求的userid
	 */
	private void getUserid() {
		// 查找
		try {
			DbUtils db = DbUtils.create(mContext);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			userid = Constant.decode(Constant.key, user.getUserId());
			Log.d("tag", "=======FixedTaskActivity--->userid=" + userid);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从网络请求数据
	 */
	private void loadData() {
		try {
			new FixedTaskAsyncTask(handler.obtainMessage(), userid,
					taskuniquecode, mContext).execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  
	/**
	 * 点击提交按钮 请求提交结果数据
	 */
	public void getCommitResult(Handler commitHandler, List<InfoBean> datas) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < datas.size(); i++) {
			stringBuffer.append(datas.get(i).getInvitationCode()).append(",");
		}
		System.out.println("=======getCommitResult===="+datas.size());
		String vipid = stringBuffer.toString();
		System.out.println("====StringBuffer---->vipid==>" + vipid
				+ "commitHandler=" + commitHandler);
		new TaskCommitAsyncTask(commitHandler.obtainMessage(), userid, vipid,
				taskuniquecode, "1", "", "", mContext).execute(1);
	}

	/**
	 * 沉浸式状态栏
//	 */
//	public void setStatusBar() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			Window window = getWindow();
//			// Translucent status bar
//			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		} else {
//			view.setVisibility(View.GONE);
//		}
//	}

	private void initview() {
		ib_back = (Button) findViewById(R.id.but_fanhui);
		tv_title = (TextView) findViewById(R.id.in_title);
		tv_title.setText(title);
//		view = findViewById(R.id.ft_view);
		tv_commit = (TextView) findViewById(R.id.ft_tv_comimit);
		tv_selectall = (LinearLayout) findViewById(R.id.ft_tv_selectAll);
//		listView = (ListView) findViewById(R.id.ft_lv);
		ft_tv_empty = (TextView) findViewById(R.id.ft_tv_empty);
		ft_select_ll = (LinearLayout) findViewById(R.id.ft_select_ll);
		ft_iv_selecteAll = (ImageView) findViewById(R.id.ft_iv_selecteAll);
		ib_back.setOnClickListener(this);
		tv_commit.setOnClickListener(this);
		tv_selectall.setOnClickListener(this);
		//分组listview
		ft_linear = (LinearLayout) findViewById(R.id.ft_linear);
		ft_PinnedHeaderlistview = (PinnedHeaderListView) findViewById(R.id.ft_PinnedHeaderlistview);
		ft_myletterlistview = (BladeView) findViewById(R.id.ft_myletterlistview);
	}

	@Override
	protected void setI18nValue() {

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_fanhui:
			finish();
			break;
		case R.id.ft_tv_comimit:
			if (selectedDatas.size() == 0) {
				Toast.makeText(mContext, "请至少选择一个人", Toast.LENGTH_LONG).show();
			} else {
				showCustomDialog();
				
			}

			break;
		case R.id.ft_tv_selectAll:
				Drawable drawable = null;
				if (isSelectAll) {//全选被选中，点击后取消全选
					System.out.println("=========点击后取消全选");
					ft_iv_selecteAll.setImageResource(R.drawable.btn_mytask_nomal);
					selectedDatas.removeAll(datas);
					selectedDatas.clear();
					
					for (int i = 0; i < datas.size(); i++) {
						datas.get(i).setCheckState(false);
					}
					adapterForPinnear.notifyDataSetChanged();
					
//					drawable = getResources().getDrawable(
//							R.drawable.btn_mytask_nomal);
//					// / 这一步必须要做,否则不会显示.
//					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
//							drawable.getMinimumHeight());
//					tv_selectall.setCompoundDrawables(drawable, null, null,
//							null);
					isSelectAll = false;

				} else {//全选没有被选中，点击后为全选状态
					ft_iv_selecteAll.setImageResource(R.drawable.btn_mytask_selected);
					System.out.println("=========点击后为全选状态");
					selectedDatas.clear();
					for (int i = 0; i < datas.size(); i++) {
						datas.get(i).setCheckState(true);
					}
					selectedDatas.addAll(datas);
					adapterForPinnear.notifyDataSetChanged();
//					drawable = getResources().getDrawable(
//							R.drawable.btn_mytask_selected);
//					// / 这一步必须要做,否则不会显示.
//					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
//							drawable.getMinimumHeight());
//					tv_selectall.setCompoundDrawables(drawable, null, null,
//							null);
					isSelectAll = true;
					
				}
				System.out.println("========点击全选按钮=isSelectAll--->"+isSelectAll);
				System.out.println("=========selectall====>"+selectedDatas.size());
//				 Toast.makeText(mContext,
//				 "---selectall->"+selectedDatas.size(),
//				 Toast.LENGTH_LONG).show();
//				adapter.notifyDataSetChanged();
				
			break;
		default:
			break;
		}

	}
	/**
	 * 自定义Dialog
	 */
	public void showCustomDialog() {
		// 将布局文件转为View
		View customDialogView = LayoutInflater.from(mContext).inflate(
				R.layout.custom_dialog, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(mContext).create();
		dialog.show();
		dialog.getWindow().setContentView(customDialogView);
		// 提示内容
		TextView dialog_text = (TextView) customDialogView
				.findViewById(R.id.dialog_text);
		dialog_text.setText("提交后此任务状态不可被修改，请您确认该项任务是否完成？");
		// 确认button
		Button btn_ok = (Button) customDialogView.findViewById(R.id.dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				System.out.println("=====ft_tv_comimit==="+selectedDatas.size());
//				 Toast.makeText(mContext, "选择了"+selectedDatas.size()+"个人",
//				 Toast.LENGTH_LONG).show();
				getCommitResult(commitHandler, selectedDatas);
				selectedDatas.clear();
				finish();
				dialog.dismiss();
			}
		});
		// 取消Button
		Button btn_cancel = (Button) customDialogView
				.findViewById(R.id.dialog_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		Object tag = buttonView.getTag();
		if (tag != null && tag instanceof String) {

			vipName = (String) tag;
		}
		
		InfoBean bean = null;
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getName().equalsIgnoreCase(vipName)) {
				
				bean = datas.get(i);
			}
		}
		// 判断如选中就将对应的条目添加到存放被选中数据的集合中，然后将标记值设置为true
		if (isChecked) {

			selectedDatas.add(bean);
			bean.setCheckState(true);
		} else {
			bean.setCheckState(false);
			selectedDatas.remove(bean);
		}
		
		if (selectedDatas.size() != datas.size()) {
			ft_iv_selecteAll.setImageResource(R.drawable.btn_mytask_nomal);
			isSelectAll = false;
		}else {
			ft_iv_selecteAll.setImageResource(R.drawable.btn_mytask_selected);
			isSelectAll = true;
		}
		System.out.println("=====onCheckedChanged==>"+selectedDatas.size());
//		 Toast.makeText(mContext, "---->"+selectedDatas.size(),
//		 Toast.LENGTH_LONG).show();
	}
	
	//分组listview
	private void process() {
		// 实例化汉字转拼音类
//		characterParser = CharacterParser_Hs.getInstance();
//		mSections = new ArrayList<String>();
//		mMap = new HashMap<String, List<String>>();
//		mPositions = new ArrayList<Integer>();
//		mIndexer = new HashMap<String, Integer>();

		for (int i = 0; i < datas.size(); i++) {
			String pinyin = characterParser.getSelling(datas.get(i)
					.getName());
			String firstName = pinyin.substring(0, 1).toUpperCase();
			Log.i("firstName", "firstName=" + firstName + "pingyin=" + pinyin);
			if (firstName.matches("[A-Z]")) {
				if (mSections.contains(firstName)) {
					mMap.get(firstName).add(datas.get(i).getName());//
				} else {
					mSections.add(firstName);// 给集合添加数据
					List<String> list = new ArrayList<String>();
					list.add(datas.get(i).getName());
					mMap.put(firstName, list);// 给Map添加新数据
				}
			} else {
				if (mSections.contains("#")) {
					mMap.get("#").add(datas.get(i).getName());
				} else {
					mSections.add("#");
					List<String> list = new ArrayList<String>();
					list.add(datas.get(i).getName());
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
		ft_myletterlistview.setOnItemClickListener(new BladeView.OnItemClickListener() {

			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					ft_PinnedHeaderlistview.setSelection(mIndexer.get(s));
//					Toast.makeText(mContext, "=====mIndexer.get(s)==>"+mIndexer.get(s), Toast.LENGTH_LONG).show();
				}
			}

		});
//		adapterForPinnear = new PinnearHeaderListViewAdapter(this, usernames, mSections, mPositions,
//				datas);
//		adapterForPinnear.setCheckedListener(this);
//		ft_PinnedHeaderlistview.setAdapter(adapterForPinnear);
		ft_PinnedHeaderlistview.setOnScrollListener(adapterForPinnear);
		ft_PinnedHeaderlistview.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.vipinfo_lv_item_head, ft_PinnedHeaderlistview, false));
		ft_PinnedHeaderlistview.setOnItemClickListener(new OnItemClickListener() {
            
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb = (CheckBox) view.findViewById(R.id.ft_pin_item_cb);
				if (cb.isChecked()) {
						cb.setChecked(false);
				} else {
					cb.setChecked(true);//该方法会自动调用onCheckedChangeListenter
				}
				System.out.println("========list_itemClick===renshu===>"+selectedDatas.size());
//				Toast.makeText(mContext, "====list==itemClick===renshu->"+selectedDatas.size(), Toast.LENGTH_LONG).show();
			}
		});
		
	}
	  /**
     * 任务提交
     */
	private Handler commitHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				System.out.println("========固定任务==>" + taskuniquecode
						+ "-----提交结果：>" + result);
				break;

			default:
				break;
			}
		};
	};

}
