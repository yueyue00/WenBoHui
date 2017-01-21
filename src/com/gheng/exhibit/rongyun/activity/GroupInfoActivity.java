package com.gheng.exhibit.rongyun.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GroupInfoProvider;
import io.rong.imlib.model.Group;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.GroupInfoBean;
import com.gheng.exhibit.http.body.response.RongGroup;
import com.gheng.exhibit.http.body.response.RongGroup.RenyuanBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.rongyun.adapter.RongGridAdapter;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.CustomGridView;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.zAsyncTaskForRongGroup;
import com.hebg3.wl.net.Base;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

/**
 * 融云 - 群组成员界面,显示成员列表,并可以解散/退出/改名群
 * 
 * @author W.b
 * 
 */
public class GroupInfoActivity extends Activity implements OnClickListener {
	private Context context;
	private TextView title_tv, group_name_tv, group_id_tv, group_remove;;
	private Button back_iv;
	private LinearLayout group_name_linear;
	/** 是否群主 等于1 */
	// private String isOwner;
	/** 群id */
	public String groupId;
	/** 群名 */
	public String groupName;
	/** 修改后的群名 */
	private String changeGroupName;
	/** 群组成员适配器 */
	private RongGridAdapter adapter;
	private CustomGridView gridview;
	private ScrollView mScrollView;
	private List<GroupInfoBean> list;
	/** 当前群组信息 */
	GroupInfoBean data;
	User muser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zactivity_group_info);
		context = this;
		try {
			DbUtils db = DbUtils.create(this);
			muser = db.findFirst(Selector.from(User.class)
					.where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		list = new ArrayList<GroupInfoBean>();
		data = (GroupInfoBean) getIntent().getSerializableExtra("groupInfo");
		groupId = data.GROUP_ID;
		groupName = data.groupName;
		setupView();
	}

	@SuppressWarnings("unchecked")
	private void setupView() {
		// SharePreferenceUtils.getAppConfig(context);
		disableAutoScrollToBottom();
		gridview = (CustomGridView) findViewById(R.id.gridview);
		adapter = new RongGridAdapter(this);
		gridview.setAdapter(adapter);// 这里将群成员的信息传递过去
		adapter.changeData(list);
		//
		group_name_linear = (LinearLayout) findViewById(R.id.group_name_linear);
		group_id_tv = (TextView) findViewById(R.id.group_id);
		group_name_tv = (TextView) findViewById(R.id.group_name);
		group_remove = (TextView) findViewById(R.id.group_remove);
		back_iv = (Button) findViewById(R.id.back_img);
		title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText("群组信息");
		group_remove.setOnClickListener(this);
		group_name_linear.setOnClickListener(this);
		back_iv.setOnClickListener(this);
	}

	private void getData(String pagesize, String pagenum) {
		if (!IsWebCanBeUse.isWebCanBeUse(context)) {
			Toast.makeText(context, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				zAsyncTaskForRongGroup at = new zAsyncTaskForRongGroup(
						users_handler.obtainMessage(), context,
						Constant.decode(Constant.key, muser.getUserId()));
				at.execute(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		/** 拿到点击群组详情获得的群组对象 */
		// isOwner = GlobalConfig.RongYun.groupOwnerMap.get(groupId);
		// data.isManager = isOwner;
		// 将群组名字显示在UI上
		group_id_tv.setText(groupId);
		group_name_tv.setText(groupName);
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			// method=groupUsers&userId=%s&groupId=%s&groupName=%s
			try {
				ClientParams client = new ClientParams(); // 创建一个新的Http请求
				client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
				StringBuffer strbuf = new StringBuffer();
				strbuf.append("method=groupUsers&userId=");
				strbuf.append(Constant.decode(Constant.key, muser.getUserId()));
				strbuf.append("&groupId=");
				strbuf.append(groupId);
				strbuf.append("&groupName=");
				strbuf.append(groupName);

				String str = strbuf.toString();
				client.params = str;

				Type type = new TypeToken<ArrayList<GroupInfoBean>>() {
				}.getType();
				NetTask<GroupInfoBean> net = new NetTask<GroupInfoBean>(
						users_handler.obtainMessage(), client, type, context);
				net.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_img:// 初始化back
			// 返回群聊界面传当前更改过的名字
			if (changeGroupName != null) {
				Intent it = new Intent();
				it.putExtra("groupTitle", changeGroupName);
				setResult(101, it);
				finish();
			} else {
				finish();
			}
			break;
		case R.id.group_remove:// 退群/删群
			removeDialog();
			break;
		// zyj 修改去掉更改群组名称的通道
		// case R.id.group_name_linear:// 更改群名字
		// setNameDialog();
		// break;
		}
	}

	String rename;

	/** 修改群名称 dialog */
	@SuppressLint("InflateParams")
	private void setNameDialog() {
		LayoutInflater factory = LayoutInflater.from(GroupInfoActivity.this);// 提示框
		final View view = factory.inflate(R.layout.zcustom_altert_layout, null);// 这里必须是final的
		final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
		edit.setFilters(new InputFilter[] { emojiFilter });

		Dialog alertDialog = new AlertDialog.Builder(GroupInfoActivity.this)
				.setTitle("群聊名称")
				.setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						try {
							changeGroupName = edit.getText().toString().trim();
							rename = URLEncoder.encode(
									edit.getText().toString().trim()
											.replace("\"", "\\\""), "UTF-8")
									.replace("%0A", "\\r\\n");
							if (rename != null && !rename.equals("")) {
								setGroupName(rename);
							} else {
								Toast.makeText(context, "输入群组名称为空!",
										Toast.LENGTH_SHORT).show();
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		alertDialog.show();
	}

	/** 退出/解散群 dialog */
	private void removeDialog() {
		Dialog alertDialog = new AlertDialog.Builder(GroupInfoActivity.this)
				.setTitle("提示").setMessage("删除并退出后，将不再接收此群聊信息")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 退出操作
						removeGroup();
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		alertDialog.show();
	}

	/** 退出群 */
	private void removeGroup() {

		// zyj 没有这个功能---将当前退群的ID保存,以在消息列表中删除该条群
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				// method=groupUsers&userId=%s&groupId=%s&groupName=%s
				ClientParams client = new ClientParams(); // 创建一个新的Http请求
				client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
				StringBuffer strbuf = new StringBuffer();
				strbuf.append("method=leaveGroup&userId=");

				strbuf.append(Constant.decode(Constant.key, muser.getUserId()));

				strbuf.append("&groupId=");
				strbuf.append(groupId);
				strbuf.append("&groupName=");
				strbuf.append(groupName);

				String str = strbuf.toString();
				client.params = str;

				NetTask<Base> net = new NetTask<Base>(
						quit_handler.obtainMessage(), client, 0, context);
				net.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 修改群名称 */
	private void setGroupName(String name) {
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (!changeGroupName.equals("") || !changeGroupName.isEmpty()) {
			try {
				ClientParams client = new ClientParams(); // 创建一个新的Http请求
				client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
				StringBuffer strbuf = new StringBuffer();
				strbuf.append("method=editGroup&userId=");
				strbuf.append(Constant.decode(Constant.key, muser.getUserId()));
				strbuf.append("&groupId=");
				strbuf.append(groupId);
				strbuf.append("&groupName=");
				strbuf.append(name);

				String str = strbuf.toString();
				client.params = str;

				NetTask<Base> net = new NetTask<Base>(
						rename_handler.obtainMessage(), client, 0, context);
				net.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context, "请输入要修改的群名称!", Toast.LENGTH_SHORT).show();
		}
	}

	/** 保存修改过的群名字 */
	protected void saveGrouName() {
		group_name_tv.setText("群组名称: " + changeGroupName);
		data.groupName = changeGroupName;
		// 判断是否是收藏的群组
	}

	private List<RenyuanBean> mList;
	// 群组列表
	Handler newusers_handler = new Handler() {
		@SuppressWarnings("static-access")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				RongGroup rongroup = (RongGroup) msg.obj;
				ArrayList<RenyuanBean> renyuan = (ArrayList<RenyuanBean>) rongroup.renyuan;
				ArrayList<RenyuanBean> tuan = (ArrayList<RenyuanBean>) rongroup.qunzu;
				try {
					for (int i = 0; i < renyuan.size(); i++) {

						if (renyuan.get(i).shortname.equals(Constant.decode(
								Constant.key, muser.getUserId()))) {
							renyuan.remove(i);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mList.clear();
				mList.addAll(renyuan);
				mList.addAll(tuan);
//				mAdapter.setmList(mList);
//				mAdapter.notifyDataSetChanged();
				adapter.changeData(mList);
				break;
			case 1:
				Toast.makeText(context, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(context, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(context, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};
	// 获取群组成员列表的返回值
	Handler users_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				RongGroup rongroup = (RongGroup) msg.obj;
				ArrayList<RenyuanBean> renyuan = (ArrayList<RenyuanBean>) rongroup.renyuan;
				ArrayList<RenyuanBean> tuan = (ArrayList<RenyuanBean>) rongroup.qunzu;
				try {
					for (int i = 0; i < renyuan.size(); i++) {

						if (renyuan.get(i).shortname.equals(Constant.decode(
								Constant.key, muser.getUserId()))) {
							renyuan.remove(i);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				adapter.changeData(renyuan);
				break;
			case 1:
				Toast.makeText(context, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(context, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(context, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};
	// 获取退出群的返回值
	Handler quit_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				String message = (String) msg.obj;
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(context, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(context, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(context, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};
	// 获取修改群名称的返回值
	Handler rename_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				String message = (String) msg.obj;
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				group_name_tv.setText(rename);
				break;
			case 1:
				Toast.makeText(context, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(context, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(context, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 禁止ScrollView的childview自动滑动到底部
	 */
	private void disableAutoScrollToBottom() {
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		mScrollView
				.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		mScrollView.setFocusable(true);
		mScrollView.setFocusableInTouchMode(true);
		mScrollView.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
		});
	}

	/**
	 * 过滤emoj表情
	 */
	InputFilter emojiFilter = new InputFilter() {
		Pattern emoji = Pattern
				.compile(
						"[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
						Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {

				return "";
			}
			return null;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 返回群聊界面传当前更改过的名字
		if (changeGroupName != null) {
			Intent it = new Intent();
			it.putExtra("groupTitle", changeGroupName);
			setResult(101, it);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initData();
		getData("20", "1");
	}
}
