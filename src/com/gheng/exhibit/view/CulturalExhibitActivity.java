package com.gheng.exhibit.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.dreamlive.hotimglibrary.entity.HotArea;
import com.dreamlive.hotimglibrary.utils.FileUtils;
import com.dreamlive.hotimglibrary.view.HotClickView;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.CulturalExhibitListBean;
import com.gheng.exhibit.http.body.response.CulturalExhibitListBean.InfoBean;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.adapter.CulturalExhibitListAdapter;
import com.gheng.exhibit.view.checkin.CulturalExhibitListActivity;
import com.gheng.exhibit.view.checkin.checkin.CultrualExhibitDetailActivity;
import com.hebg3.mxy.utils.CulturalExhibitListAsyncTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CulturalExhibitActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {
	private LinearLayout exhibit_title_layout;
	private View exhibit_view;
	private View exhibit_view_line;
	private RelativeLayout exhibit_titlelayout;
	private ImageButton exhibit_goback;
	private ImageButton exhibit_sliptolist;
	private TextView exhibit_titletv;
	private HotClickView mHotView;
	private EditText et_search;
	private TextView search;
	private RadioGroup rg;
	private RadioButton rb_1;
	private RadioButton rb_2;
	private RadioButton rb_3;
	private PopupWindow popupWindow;
	private LinearLayout container_linear;
	/**
	 * 用于区分楼层，在搜索的时候仅限于在本楼层搜索
	 */
	private static int FlOOR_TAG = 2;
	private String title = "";
	private String floor;
	private String exhibit;
	private int REQUEST_CODE = 11;
	private String[] exhibitionsb1f = new String[] { "敦煌创意文化产品展", "兰生染缬作品展",
			"互动时代港澳台创意文化展", "莫高霞光当代艺术展", "浙江丝绸展", "敦煌传奇丝绸之路动漫作品",
			"影像丝路国际摄影艺术作品", "书香丝路丝绸之路精品图书", "丝路茶韵丝绸之路茶文化展演" };
	private String[] exhibition1f = new String[] { "丝路沿线省区市文物精品", "明代手绘丝绸之路地图",
			"海丝记忆", "意会中国", "中国近现代书画精品", "文明之旅·魅力法国", "世·象中意艺术家联展",
			"瓷上敦煌中国陶瓷艺术展", "四海一家世界多元文化展", "敦煌印象·丝路霓虹长卷", "文明交响" };
	private String[] exhibition2f = new String[] { "丝路之邮", "魅力创意", "洮砚上的敦煌",
			"华夏文明在甘肃", "唐卡艺术展", "黄河万里图", "澄怀味象采风写生创作作品", "花开敦煌常沙娜艺术展",
			"朝圣敦煌美术精品", "敦煌飞天九天揽月", "文化记忆" };

	private Context mContext;
	private User user;
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cultural_exhibit);
		Log.d("tag", "============ce--》oncreate()");
		mContext = this;
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		initView();
		setStatusBar(exhibit_view);
		initDatas("bc_2f.png","exhibition2.xml");
//		loadData();
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
			System.out.println("========文化年展列表==userid=》" + userid);
			db.close();
			new CulturalExhibitListAsyncTask(handler.obtainMessage(), userid,"onePosition",
					mContext).execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		exhibit_title_layout = (LinearLayout) findViewById(R.id.exhibit_title_layout);
		exhibit_view = (View) findViewById(R.id.exhibit_view);
		exhibit_view_line = findViewById(R.id.exhibit_view_line);
		exhibit_titlelayout = (RelativeLayout) findViewById(R.id.exhibit_titlelayout);
		exhibit_titletv = (TextView) findViewById(R.id.exhibit_titletv);
		exhibit_titletv.setText(title);
		exhibit_goback = (ImageButton) findViewById(R.id.exhibit_goback);
		exhibit_goback.setOnClickListener(this);
		exhibit_sliptolist = (ImageButton) findViewById(R.id.exhibit_sliptolist);
		mHotView = (HotClickView) findViewById(R.id.a_main_hotview);
		rg = (RadioGroup) findViewById(R.id.ce_rg);
		rb_1 = (RadioButton) findViewById(R.id.rb_1);
		rb_2 = (RadioButton) findViewById(R.id.rb_2);
		rb_3 = (RadioButton) findViewById(R.id.rb_3);
		rg.setOnCheckedChangeListener(this);
		et_search = (EditText) findViewById(R.id.et_search);
		search = (TextView) findViewById(R.id.btn);
		search.setOnClickListener(this);
		if (SharedData.getInt("i18n", Language.ZH) == 1) {// 1 中文
			et_search.setHint("请输入要搜索的展厅");
			search.setText("搜索");
		} else {// 2 英文
			et_search.setHint("Enter search Hall");
			search.setText("Search");
		}
		et_search
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						/* 判断是否是“search”键 */
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							/* 隐藏软键盘 */
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							if (imm.isActive()) {
								imm.hideSoftInputFromWindow(
										v.getApplicationWindowToken(), 0);
							}

							searchExhibit();

							return true;
						}
						return false;
					}

				});

	}

	@Override
	protected void setI18nValue() {

	}

	/**
	 * 透明状态栏
	 */
	public void setStatusBar(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 搜索展品的方法
	 */
	public void searchExhibit() {
		String et_content = et_search.getText().toString();
		if (!TextUtils.isEmpty(et_content)) {

			if (FlOOR_TAG == 2) {
				switch (et_content) {
				case "紫金厅1":
					mHotView.move(673, 1525);
					break;
				case "签到处":
					mHotView.move(673, 2625);
					break;
				case "紫华厅1":
					mHotView.move(673, 625);
					break;
				default:
					Toast.makeText(CulturalExhibitActivity.this,
							"1F没有您要搜索的展厅，请切换楼层重新搜索!", Toast.LENGTH_LONG).show();
					break;

				}

			} else if (FlOOR_TAG == 1) {
				switch (et_content) {

				default:
					Toast.makeText(CulturalExhibitActivity.this,
							"2F没有您要搜索的展厅，请切换楼层重新搜索!", Toast.LENGTH_LONG).show();
					break;
				}

			} else if (FlOOR_TAG == -1) {

				switch (et_content) {
				case "签到处":
					mHotView.move(673, 2625);
					break;
				default:
					Toast.makeText(CulturalExhibitActivity.this,
							"3F没有您要搜索的展厅，请切换楼层重新搜索!", Toast.LENGTH_LONG).show();
					break;
				}

			}

		} else {
			Toast.makeText(mContext, "搜索内容不能为空", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exhibit_goback:
			finish();
			break;
		case R.id.btn:// 搜索
			// 1.得到InputMethodManager对象
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// 2.调用hideSoftInputFromWindow方法隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
			searchExhibit();

			break;
		default:
			break;
		}
	}

	/**
	 * 在详情点击展品位置后，跳转搜索
	 * 
	 * @param floor
	 *            楼层
	 * @param exhibit
	 *            展品名
	 */
	public void searchByDetail(String floor, String exhibit) {
		if (floor != null && exhibit != null) {
			if (floor.equals("1")) {// 1F
				rb_1.setChecked(true);
				if (exhibit.equals("紫金厅1")) {
					Toast.makeText(mContext, "如果是紫金厅1，则区域放大",
							Toast.LENGTH_SHORT).show();
					mHotView.move(673, 1525);
				} else if (exhibit.equals("签到处")) {
					mHotView.move(673, 2625);
				} else if (exhibit.equals("紫华厅1")) {
					mHotView.move(673, 625);
				}

			} else if (floor.equals("2")) {// 2F
				rb_2.setChecked(true);
				Toast.makeText(mContext, "进入2F", Toast.LENGTH_SHORT).show();
			} else if (floor.equals("3")) {// 3F
				rb_2.setChecked(true);
				Toast.makeText(mContext, "进入3F", Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.d("tag", "=========floor或者exhibit为空");
		}
	}

	private void getPopWindow(List<InfoBean> daBeans) {
		if (null != popupWindow) {
			closePopupWindow();
			return;
		} else {
			initPopupWindow(daBeans);
		}
	}

	/**
	 * 关闭popupWindow
	 */
	// 关闭popupWindow时，要在动画结束后再关闭popupWindow，在动画结束的监听事件里关闭popupWindow并将其置空
	public void closePopupWindow() {
		// 涉及到动画就要注意屏幕的坐标原点是在左上角
		// container_linear 为popupwindow的根布局
		popupWindow.dismiss();
	}

	private ImageButton ib_back;
	private TextView tv_title;
	private ImageButton ib_sliptopic;
	private ListView listView;
	private List<InfoBean> list;
	private CulturalExhibitListAdapter adapter;

	/**
	 * 弹出popupWindow
	 */
	private void initPopupWindow(List<InfoBean> daBeans) {
		View popView = LayoutInflater.from(mContext).inflate(
				R.layout.activity_cultural_exhibit_list, null, false);
		popupWindow = new PopupWindow(popView,
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		container_linear = (LinearLayout) popView
				.findViewById(R.id.ced_list_linear);
		tv_title = (TextView) popView.findViewById(R.id.cedl_titletv);
		tv_title.setText(getLanguageString("展品列表"));
		ib_back = (ImageButton) popView.findViewById(R.id.cedl_goback);
		ib_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closePopupWindow();
			}
		});
		ib_sliptopic = (ImageButton) popView.findViewById(R.id.cedl_sliptopic);
		ib_sliptopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopupWindow();
			}
		});
		listView = (ListView) popView.findViewById(R.id.cedl_listview);
		initPopListData(daBeans);
		// 给popwindow_relative_total添加监听，捕获KEYCODE_BACK，然后关闭popupWindow
		container_linear.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_BACK) {
					closePopupWindow();
				}
				return false;
			}
		});

		// // 点击其他地方，关闭popupWindow
		// popView.setOnTouchListener(new View.OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (popupWindow != null && popupWindow.isShowing()) {
		// closePopupWindow();
		// }
		// return false;
		// }
		// });
	}

	/**
	 * 初始pop化数据
	 */
	private void initPopListData(final List<InfoBean> data) {
		list = new ArrayList<>();
		list.clear();
		list.addAll(data);
		adapter = new CulturalExhibitListAdapter(mContext, list);
		listView.setAdapter(adapter);
		listView.setEmptyView((TextView) findViewById(R.id.cedl_empty));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CulturalExhibitActivity.this,
						CultrualExhibitDetailActivity.class);
				intent.putExtra("uniqueCode", data.get(position)
						.getUniqueCode());
				startActivityForResult(intent, 11);
				closePopupWindow();

			}
		});
	}

	/**
	 * 初始化楼层图片
	 * 
	 * @param img
	 */
	protected void initDatas(String img,String assetsFile) {
		AssetManager assetManager = getResources().getAssets();
		InputStream imgInputStream = null;
		InputStream fileInputStream = null;
		try {
			imgInputStream = assetManager.open(img);// 图
			fileInputStream = assetManager.open(assetsFile);// 坐标"china.xml"
			mHotView.setImageBitmap(fileInputStream, imgInputStream,
					HotClickView.FIT_XY);
			mHotView.setOnClickListener(new HotClickView.OnClickListener() {

				@Override
				public void OnClick(View view, HotArea hotArea) {
//					showCustomDialog(hotArea);
					// Toast.makeText(CulturalExhibitActivity.this, "你点击了" +
					// hotArea.getDesc(), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(mContext,CulturalExhibitListActivity.class);
					intent.putExtra("location", hotArea.getDesc());
					startActivity(intent);
//					finish();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtils.closeInputStream(imgInputStream);
			FileUtils.closeInputStream(fileInputStream);
		}
	}

	/**
	 * 楼层切换
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_1:// 2F
			initDatas("bc_2f.png","exhibition2.xml");
			FlOOR_TAG = 2;
			break;
		case R.id.rb_2:// 1F
			initDatas("bc_1f.png","exhibition1.xml");
			FlOOR_TAG = 1;
			break;
		case R.id.rb_3:// B1F
			initDatas("bc_b1.png","exhibitionb1.xml");
			FlOOR_TAG = -1;
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("tag", "===========onActivityResult");
		if (resultCode == RESULT_OK) {
			floor = data.getStringExtra("floor");
			exhibit = data.getStringExtra("exhibit");
			Log.d("tag", "------->" + floor + "," + exhibit);
			// Toast.makeText(CulturalExhibitActivity.this,"------->"+floor+","+exhibit,Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 自定义Dialog
	 */
	public void showCustomDialog(final HotArea hotArea) {
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
		dialog_text.setText("您是否定位到" + hotArea.getAreaTitle());
		// 确认button
		Button btn_ok = (Button) customDialogView.findViewById(R.id.dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				if (hotArea.getDesc().equals("紫金厅1")) {
//					mHotView.move(673, 1525);
//				}
//				if (hotArea.getDesc().equals("签到处")) {
//					mHotView.move(673, 2625);
//				}
//				if (hotArea.getDesc().equals("紫华厅1")) {
//					mHotView.move(673, 625);
//				}
				Intent intent = new Intent(CulturalExhibitActivity.this,
						CulturalExhibitListActivity.class);
//				intent.putExtra("uniqueCode", hotArea.getAreaTitle());
				intent.putExtra("location", "onePosition");
				startActivity(intent);
				dialog.dismiss();
//				finish();
//				startActivityForResult(intent, REQUEST_CODE);// 11--->requestCode
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

	/**
	 * 文化年展列表数据-->用于popWindow展示
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				final CulturalExhibitListBean bean = (CulturalExhibitListBean) msg.obj;
				System.out.println("=======wenhua===size==>"
						+ bean.getInfo().size());
				exhibit_sliptolist.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 以popupwindow的形式弹出列表
						getPopWindow(bean.getInfo());
						popupWindow.showAsDropDown(exhibit_view_line, 0, 50);

						// popupWindow.showAtLocation(exhibit_view,
						// Gravity.TOP,0,10);
					}
				});

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("tag", "============ce--》onStart()");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("tag", "============ce--》onResume()");
		searchByDetail(floor, exhibit);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("tag", "============ce--》onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("tag", "============ce--》onStop()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("tag", "============ce--》onRestart()");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("tag", "============ce--》onDestroy()");
		mHotView.recycleBitmap();
	}
}
