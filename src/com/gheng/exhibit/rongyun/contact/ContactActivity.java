package com.gheng.exhibit.rongyun.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.ContactBean;
import com.gheng.exhibit.http.body.response.Contacts;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.rongyun.activity.ContactSearchActivity;
import com.gheng.exhibit.rongyun.utils.MSharePreferenceUtils;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.zAsyncTaskForContact;
import com.hebg3.wl.net.Base;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

//通讯录界面
public class ContactActivity extends Activity {

	RecyclerView rv_hor;
	RecyclerView rv_ver;
	public TextView back_tv, right_tv, title_tv;
	ImageView add_group_img;
	Button back_img;
	RelativeLayout search_layout;
	private LinearLayoutManager llm_hor;
	private LinearLayoutManager llm_ver;
	AdapterContactHor adapter_rvHor;
	AdapterContactVer adapter_rvVer;
	ArrayList<ContactBean> horlist = new ArrayList<>();
	ArrayList<ContactBean> verlist = new ArrayList<>();
	Context mcontext;
	// 选中的那些人》》用来提交的时候使用
	int hasdata = 0;// 获取到当前页是否之前保存了数据的标识(有数据：1；没数据：0)
	ArrayList<ContactBean> mchecks = new ArrayList<ContactBean>();
	// 修改的那些集合对应的id的集合
	public ArrayList<String> changeids = new ArrayList<>();
	String addgroupid = "";// 从拉人界面传来的
	String addgroupname = "";// 从拉人界面传来的
	Boolean creategroup = false;// 从rongdemotabs进入创建群组
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
		setContentView(R.layout.zyjactivity_contact);
		MSharePreferenceUtils.getAppConfig(this);
		mcontext = ContactActivity.this;
		try {
			DbUtils db = DbUtils.create(this);
			muser = db.findFirst(Selector.from(User.class)
					.where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (getIntent().getStringExtra("groupId") != null) {
			addgroupid = getIntent().getStringExtra("groupId");
			addgroupname = getIntent().getStringExtra("groupName");
		} else if (getIntent().getBooleanExtra("creategroup", false)) {
			creategroup = getIntent().getBooleanExtra("creategroup", false);
		}
		initView();
		LoadData("0", "0", "2");
		process();
		setAllClick();
	}

	// 加载通讯录列表及层级关系的数据
	@SuppressWarnings("unchecked")
	public void LoadData(String pid, String id, String sign) {

		ArrayList<ContactBean> liebiao = null;
		String ppid = "-1";
		if (horlist.size() >= 2) {
			ppid = horlist.get(horlist.size() - 1).pid;
		}
		for (int i = 0; i < changeids.size(); i++) {
			String changeid = changeids.get(i);
			if (sign.equals("1")) {
				if (changeid.equals(ppid)) {
					liebiao = (ArrayList<ContactBean>) MSharePreferenceUtils
							.getBean(mcontext, ppid);
					hasdata = 1;
				}
			} else if (sign.equals("2")) {
				if (changeid.equals(id)) {
					liebiao = (ArrayList<ContactBean>) MSharePreferenceUtils
							.getBean(mcontext, id);
					hasdata = 1;
				}
			}
		}
		if (hasdata == 1 && liebiao != null) {
			if (liebiao.size() <= 0) {
				hasdata = 0;
			} else {
				Message msg = contact_handler.obtainMessage(5);
				msg.obj = liebiao;
				msg.sendToTarget();
			}
		} else {
			hasdata = 0;
		}
		// ----------------------------------------------
		try {
			// method=groupUsers&userId=%s&groupId=%s&groupName=%s
			// ClientParams client = new ClientParams(); // 创建一个新的Http请求
			// client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
			// StringBuffer strbuf = new StringBuffer();
			// strbuf.append("method=getAllUserMail&userId=");
			// strbuf.append(Constant.decode(Constant.key, muser.getUserId()));
			// strbuf.append("&id=");
			// strbuf.append(id);
			// strbuf.append("&pid=");
			// strbuf.append(pid);
			// strbuf.append("&sign=");
			// strbuf.append(sign);
			// String str = strbuf.toString();
			// client.params = str;
			// Type type = new TypeToken<ArrayList<Contacts>>() {
			// }.getType();
			// NetTask<Contacts> net = new NetTask<Contacts>(
			// contact_handler.obtainMessage(), client, type, mcontext);
			// net.execute();

			zAsyncTaskForContact at = new zAsyncTaskForContact(
					contact_handler.obtainMessage(), mcontext, Constant.decode(
							Constant.key, muser.getUserId()), pid, id, sign);
			at.execute(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 创建群组的亲求
	public void CreateGroup(ArrayList<ContactBean> checks, String groupname) {
		// zyj
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
				strbuf.append("method=newGroup&userId=");
				strbuf.append(Constant.decode(Constant.key, muser.getUserId()));
				strbuf.append("&groupName=");
				strbuf.append(groupname);
				strbuf.append("&userIds=");
				for (int i = 0; i < checks.size(); i++) {
					ContactBean bean = checks.get(i);
					String id = bean.shortname;
					strbuf.append(id);
					strbuf.append(",");
				}
				String str = strbuf.toString();
				client.params = str;

				NetTask<Base> net = new NetTask<Base>(
						creategp_handler.obtainMessage(), client, 0, mcontext);
				net.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 群组拉人的亲求
	public void AddUsers(ArrayList<ContactBean> checks, String groupid,
			String groupname) {
		// zyj
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
				strbuf.append("method=groupAddUser&userId=");
				strbuf.append(Constant.decode(Constant.key, muser.getUserId()));
				strbuf.append("&groupId=");
				strbuf.append(groupid);
				strbuf.append("&groupName=");
				strbuf.append(groupname);
				strbuf.append("&userIds=");
				for (int i = 0; i < checks.size(); i++) {
					ContactBean bean = checks.get(i);
					String id = bean.shortname;
					strbuf.append(id);
					strbuf.append(",");
				}
				String str = strbuf.toString();
				client.params = str;
				NetTask<Base> net = new NetTask<Base>(
						adduser_handler.obtainMessage(), client, 0, mcontext);
				net.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initView() {
		back_tv = (TextView) findViewById(R.id.back_tv);
		right_tv = (TextView) findViewById(R.id.right_tv);
		title_tv = (TextView) findViewById(R.id.title_tv);
		back_img = (Button) findViewById(R.id.back_img);
		add_group_img = (ImageView) findViewById(R.id.add_group_img);
		rv_hor = (RecyclerView) findViewById(R.id.rv_horzontal);
		rv_ver = (RecyclerView) findViewById(R.id.rv_vertical);
		search_layout = (RelativeLayout) findViewById(R.id.search_layout);
		llm_hor = new LinearLayoutManager(this);
		llm_hor.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置横向或纵向展示item
		rv_hor.setLayoutManager(llm_hor);
		llm_ver = new LinearLayoutManager(this);
		llm_ver.setOrientation(LinearLayoutManager.VERTICAL);
		rv_ver.setLayoutManager(llm_ver);
	}

	public void setCheck(ContactBean contact) {
		mchecks.add(contact);
		contact_handler.sendEmptyMessage(6);
	}

	public void removeCheck(ContactBean contact) {
		mchecks.remove(contact);
		contact_handler.sendEmptyMessage(6);
	}

	private void process() {
		title_tv.setText("通讯录");
		right_tv.setText("确定");
		adapter_rvHor = new AdapterContactHor(horlist, mcontext);
		rv_hor.setAdapter(adapter_rvHor);
		adapter_rvVer = new AdapterContactVer(mcontext);
		rv_ver.setAdapter(adapter_rvVer);
		// zyj修改-去掉创建群组通道
		if (!addgroupid.equals("") || creategroup) {
			if (creategroup)
				showDialog();
			mchecks.clear();
			adapter_rvVer.isEdit = true;
			adapter_rvVer.showCheckBox();
			add_group_img.setVisibility(View.GONE);
			right_tv.setVisibility(View.VISIBLE);
		} else {
			// zyj修改-去掉创建群组通道
			// add_group_img.setVisibility(View.VISIBLE);
		}
	}

	private void setAllClick() {
		back_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pid = "0";// 默认是0
				if (verlist != null && verlist.size() > 0) {// 如果当前页有数据进入
					pid = verlist.get(0).pid;// 如果pid存在或者当前页是最后一页时pid=0
				}
				if (!pid.equals("0")) {
					adapter_rvVer.saveData();
					LoadData(pid, pid, "1");
				} else {
					MSharePreferenceUtils.clear(mcontext);
					finish();
				}
			}
		});
		add_group_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
				mchecks.clear();
				adapter_rvVer.isEdit = true;
				adapter_rvVer.showCheckBox();
				add_group_img.setVisibility(View.GONE);
				right_tv.setVisibility(View.VISIBLE);
			}
		});
		right_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!addgroupid.equals("")) {
					AddUsers(mchecks, addgroupid, addgroupname);
				} else {
					CreateGroup(mchecks, editgroupName);
				}
				adapter_rvVer.closeCheckBox();
				// 在提交成功之后设置
				changeids.clear();
				adapter_rvVer.isEdit = false;
				MSharePreferenceUtils.clear(mcontext);
				add_group_img.setVisibility(View.VISIBLE);
				right_tv.setVisibility(View.GONE);
			}
		});
		search_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mcontext,
						ContactSearchActivity.class);
				startActivity(intent);
			}
		});
	}

	// 过滤emoj表情
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
	String editgroupName = "默认群组名称";

	private void showDialog() {
		LayoutInflater factory = LayoutInflater.from(mcontext);// 提示框
		final View view = factory.inflate(R.layout.zcustom_altert_layout, null);// 这里必须是final的
		final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
		edit.setHint("请输入群名称:");
		edit.setInputType(InputType.TYPE_CLASS_NUMBER);
		edit.setFilters(new InputFilter[] { emojiFilter });

		Dialog alertDialog = new AlertDialog.Builder(mcontext)
				.setTitle("创建群组")
				.setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 加入群组
						if (!edit.getText().toString().trim().equals("")) {
							editgroupName = edit.getText().toString().trim();
						} else {
							Toast.makeText(mcontext, "没有输入群组名称!",
									Toast.LENGTH_SHORT).show();
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		alertDialog.show();
	}

	// 获取通讯录列表返回的数据
	Handler contact_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				Contacts contacts = (Contacts) msg.obj;

				ArrayList<ContactBean> cengji = (ArrayList<ContactBean>) contacts.group;
				ContactBean contactBean = new ContactBean();
				contactBean.name = "通讯录";
				contactBean.pid = "0";
				contactBean.id = "0";
				contactBean.type = "1";
				cengji.add(0, contactBean);
				ArrayList<ContactBean> liebiao = (ArrayList<ContactBean>) contacts.groupuser;
				if (hasdata == 0) {
					verlist.clear();
					verlist.addAll(liebiao);
					adapter_rvVer.setData(verlist);
				}
				horlist.clear();
				horlist.addAll(cengji);
				adapter_rvHor.notifyDataSetChanged();
				// 设置左边的文字
				if (cengji.size() >= 2) {
					if (cengji.get(cengji.size() - 2).name != null) {
						back_tv.setText(cengji.get(cengji.size() - 2).name);
					}
				} else {
					back_tv.setText("");
				}
				break;
			case 1:
				Toast.makeText(mcontext, "暂无团成员!", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mcontext, "暂无团成员!", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(mcontext, "暂无团成员!", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mcontext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 5:
				List<ContactBean> liebiao2 = (List<ContactBean>) msg.obj;
				verlist.clear();
				verlist.addAll(liebiao2);
				adapter_rvVer.setData(verlist);
				break;
			case 6:
				right_tv.setText("确定(" + mchecks.size() + ")");
				break;
			default:
				break;
			}
		};
	};
	// 获取创建群的返回值
	Handler creategp_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				String message = (String) msg.obj;
				Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
				if (creategroup) {
					finish();
				}
				break;
			case 1:
				Toast.makeText(mcontext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mcontext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(mcontext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mcontext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};
	// 获取群组拉人的返回值
	Handler adduser_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				String message = (String) msg.obj;
				Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
				if (!addgroupid.equals("")) {
					finish();
				}
				break;
			case 1:
				Toast.makeText(mcontext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mcontext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(mcontext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mcontext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};

}
