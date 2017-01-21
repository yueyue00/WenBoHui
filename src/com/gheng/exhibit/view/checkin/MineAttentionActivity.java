package com.gheng.exhibit.view.checkin;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.MineAttentionListGet;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForDaHuiRiChengSearchRecyclerView;
import com.gheng.exhibit.view.adapter.MineAttentionListAdapter;
import com.google.gson.reflect.TypeToken;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;

/**
 * 我的关注页面
 * 
 * @author renzhihua
 */

public class MineAttentionActivity extends BaseActivity implements
		android.view.View.OnClickListener, TextWatcher {

	// 设置大会详情标题
	@ViewInject(R.id.in_title)
	TextView titletv;

	// 设置大会详情返回键按钮
	@ViewInject(R.id.but_fanhui)
	Button backbut;

	@ViewInject(R.id.zuijinsousuotv)
	TextView zuijinsousuotv;
	
	// 设置我的关注EditText按钮框
	@ViewInject(R.id.edittext)
	EditText edittext;
	// 设置我的关注搜索按钮
	@ViewInject(R.id.searchmapbutton)
	Button searchmapbutton;
	// 创建我的关注ListView会议列表
	@ViewInject(R.id.mine_attention_lv)
	private ListView mine_attention_lv;
	// 为我的关注列表创建适配器
	private MineAttentionListAdapter mineAttentionListAdapter;

	// 我的关注接口等待对话框
	private ProgressDialog pro;
	private Context context = this;
	// 声明全局变量
	String meettingid;

	ResponseBody<MineAttentionListGet> mine;
	ArrayList<MineAttentionListGet> list_all = new ArrayList<MineAttentionListGet>();// 我的关注
																						// 列表数据集合
																						// 全部
	ArrayList<MineAttentionListGet> list_sraech = new ArrayList<MineAttentionListGet>();// 我的关注
																						// 列表数据集合
																						// 查询
	ArrayList<MineAttentionListGet> list_adapter = new ArrayList<MineAttentionListGet>();// 我的关注
																							// 列表数据集合
																							// adapter使用
	/**
	 * 添加搜索功能
	 */
	// 创建搜索RelativeLayout视图布局
	@ViewInject(R.id.zuijinlayout)
	private RelativeLayout zuijinlayout;

	// 搜索历史rv
	@ViewInject(R.id.searchhistorylistrv)
	RecyclerView searchhistorylistrv;

	// 搜索历史记录
	ArrayList<String> historysearchlist = new ArrayList<String>();
	// 搜索历史记录rv布局管理器
	LinearLayoutManager llm_historysearchlist;
	// 搜索历史记录 rv adapter
	AdapterForDaHuiRiChengSearchRecyclerView adapter_historysearchlist;

	// 本地数据库
	SharedPreferences sp;
	Editor e;
	String spname = "wodeguanzhusearchhistory";
	String keyname = "wodeguanzhusearchhistory";
	// 搜索接口等待对话框
	ProgressDialog pd;

	private Boolean isActive = true;

	@ViewInject(R.id.nodatafound)
	// 暂无数据提示
	RelativeLayout nodatafound;
	GuanzhuChangeBroadCastReceiver receiver;// 广播接受器
	IntentFilter intentFilter;// 广播过滤器

	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mineattention);
		backbut.setOnClickListener(this);// 返回键的监听事件
		searchmapbutton.setOnClickListener(this);// 搜索按钮的监听事件
		receiver = new GuanzhuChangeBroadCastReceiver();
		intentFilter = new IntentFilter("guanzhuchanged");

		this.registerReceiver(receiver, intentFilter);// 注册广播接收器
		registerBoradcastReceiver();// 注册广播
		// 计算没有关注会议展示的图片大小
		ImageView image = new ImageView(this);

		if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
			int width = this.getResources().getDisplayMetrics().widthPixels - 120;
			float scale = 0.35f;
			int img_height = (int) (width * scale);
			image.setLayoutParams(new LayoutParams(width, img_height));
			image.setBackgroundResource(R.drawable.mineattention_en);
		} else {
			int width = this.getResources().getDisplayMetrics().widthPixels - 120;
			float scale = 0.36f;
			int img_height = (int) (width * scale);
			image.setLayoutParams(new LayoutParams(width, img_height));
			image.setBackgroundResource(R.drawable.mineattention_ch);
		}
		nodatafound.addView(image);
		// 设置点击未关注会议
		nodatafound.setOnClickListener(this);

		sp = this.getSharedPreferences(spname, Activity.MODE_PRIVATE);
		e = sp.edit();
		edittext.addTextChangedListener(this);// 这个监听是判断用户输入"",显示全部数据用
		edittext.setOnClickListener(this);// 这个监听是用户点击内容编辑框，弹出搜索历史记录

		nodatafound.setVisibility(View.GONE);

		// 初始化搜索历史记录rv
		searchhistorylistrv.setHasFixedSize(true);
		llm_historysearchlist = new LinearLayoutManager(this);
		llm_historysearchlist.setOrientation(LinearLayoutManager.VERTICAL);
		searchhistorylistrv.setLayoutManager(llm_historysearchlist);
		searchhistorylistrv.setHasFixedSize(true);// 强制item高度一致，加强加载效率

		searchhistorylistrv.addItemDecoration(// 为RecyclerView添加divider
				new HorizontalDividerItemDecoration.Builder(this)
						.color(this.getResources().getColor(
								R.color.searchhuiyirvfengexian))
						.size(getResources().getDimensionPixelSize(
								R.dimen.half1dp))
						.margin(getResources().getDimensionPixelSize(
								R.dimen.recylerviewitemdivider_pchi),
								getResources().getDimensionPixelSize(
										R.dimen.recylerviewitemdivider_pchi))
						.build());

		adapter_historysearchlist = new AdapterForDaHuiRiChengSearchRecyclerView(
				this, historysearchlist);
		searchhistorylistrv.setAdapter(adapter_historysearchlist);

		// 初始化我的关注列表
		mineAttentionListAdapter = new MineAttentionListAdapter(this,
				list_adapter); // 创建我的关注列表的Adapter适配器
		mine_attention_lv.setAdapter(mineAttentionListAdapter); // 将列表与创建的adapter适配器绑定
		mine_attention_lv.setOnItemClickListener(new OnItemClickListener() {
			// 为列表的每一个Item创建点击监听事件
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("position" + position + "----------------"
						+ mine.list.get(position).meettingid);
				meettingid = mine.list.get(position).meettingid;
				Intent intent = new Intent(MineAttentionActivity.this,
						MineAttentionPchiDetailsActivity.class);

				intent.putExtra("pchi_id", meettingid); // 用于后面接收返回的数据
				startActivity(intent);
			}
		});
		// 加载网络时等待对话框
		pro = ProgressDialog.show(this, "", "Loading……");// google自带dialog
		pro.setCancelable(true);// 点击dialog外空白位置是否消失
		pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
		try {
			loadData();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	};

	/**
	 * 点击编辑框，展示历史记录
	 * @throws Exception 
	 */
	public void showHistroyRecords() throws Exception{
		String[] records=null;
		String record=Constant.decode(Constant.key,sp.getString(keyname,"").trim());
		if(!record.equals("")){
			records=record.split(",");
		}else{
			return;
		}
		if(records.length>0){//有历史搜索记录
			historysearchlist.clear();
			for(int i=records.length-1;i>=0;i--){
				historysearchlist.add(records[i]);
			}
			historysearchlist.add(getLanguageString("清空历史数据"));
		    //显示历史搜索记录列表
			adapter_historysearchlist.notifyDataSetChanged();
			zuijinlayout.setVisibility(View.VISIBLE);
		}else{//没有历史搜索记录，隐藏搜索记录层
			zuijinlayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 历史搜索记录被点击后，在adapter中执行本方法
	 * @throws Exception 
	 */
	public void itemclicked(int position) throws Exception{
		if(position==historysearchlist.size()-1){//用户点击"清空历史数据"
			e.putString(keyname,"");
			e.apply();
			zuijinlayout.setVisibility(View.GONE);
		}else{
			String clickcontent=historysearchlist.get(position)+",";
			String record=Constant.decode(Constant.key,sp.getString(keyname,"").trim());
			record=record.replace(historysearchlist.get(position)+",","");
			e.putString(keyname,Constant.encode(Constant.key,record+clickcontent));
			e.apply();
			zuijinlayout.setVisibility(View.GONE);
			edittext.setText(historysearchlist.get(position));
			goSearch(edittext.getEditableText().toString().trim());
		}
	}

	/**
	 * 根据内容请求服务器
	 */
	public void goSearch(String content) {// 我的关注，搜索在本地搜索
		if (list_all.size() < 1) {
			return;
		} else {
			nodatafound.setVisibility(View.GONE);
		}
		list_sraech.clear();
		for (int i = 0; i < list_all.size(); i++) {
			if (list_all.get(i).meettingtitle.contains(content)) {
				list_sraech.add(list_all.get(i));
			}
		}
		list_adapter.clear();
		list_adapter.addAll(list_sraech);
		mineAttentionListAdapter.notifyDataSetChanged();
	}

	/**
	 * 我的关注列表请求访问网络
	 * @throws Exception 
	 */
	private void loadData() throws Exception {

		// 查找
		User Parent = null;
		try {
			DbUtils db = DbUtils.create(context);

			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 以下内容将来会是网络访问获取数据
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/meetings.do"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer(); // 封装需要请求的字段
		strbuf.append("method=getAttentionMeets&userid="); // 请求的字段名 要和接口文档保持一致
		strbuf.append(Constant.decode(Constant.key, Parent.getUserId()));
		strbuf.append("&lg="); // 判断语言
		if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
			strbuf.append("2"); // 获取是中英文 2是英文 1是中文
		} else {
			strbuf.append("1");
		}

		String str = strbuf.toString(); // 转换成String类型
		client.params = str; // 把请求的参数封装到params 这个属性里面

		// 调用数组
		Type type = new TypeToken<ArrayList<MineAttentionListGet>>() { // json返回值为数组时需要创建一个Type对象
			// Json 解析
		}.getType();
		NetTask<MineAttentionListGet> net = new NetTask<MineAttentionListGet>(
				hand.obtainMessage(), client, type, context); // Htpp的异步类
		net.execute(); // 相当于线程的Star方法 开始运行
	}

	/**
	 * 发送网络请求成功获得数据后要进行的操作
	 */
	Handler hand = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			pro.dismiss();
			if (msg.what == 0) {
				mine = (ResponseBody<MineAttentionListGet>) msg.obj;
				if (mine.list.size() > 0) {
					nodatafound.setVisibility(View.GONE);
				} else {
					nodatafound.setVisibility(View.VISIBLE);
				}
				list_all.clear();
				list_all.addAll(mine.list);
				list_adapter.clear();
				list_adapter.addAll((list_all));
				mineAttentionListAdapter.notifyDataSetChanged();
			} else if (msg.what == 1) {
				nodatafound.setVisibility(View.VISIBLE);
				Toast.makeText(context, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();

			} else if (msg.what == 2) {// 这里比较尴尬，因为用户如果没有关注信息，请求是正常的，返回的集合是长度为0，界面上清空数据，这样是合理的，而不应该和请求服务器出错混在一起，请求出错，那么原来界面上的数据应该保留。
				Toast.makeText(context, getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 3) {
				nodatafound.setVisibility(View.VISIBLE);
				list_all.clear();
				list_adapter.clear();
				list_adapter.addAll((list_all));
				mineAttentionListAdapter.notifyDataSetChanged();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(MineAttentionActivity.this);
			}
		}
	};

	@Override
	protected void setI18nValue() {

		titletv.setText(getLanguageString("我的关注"));
		edittext.setHint(getLanguageString("搜索我的关注"));
		searchmapbutton.setText(getLanguageString("搜索"));
		zuijinsousuotv.setText(getLanguageString("最近搜索"));

	}

	@Override
	public void onClick(View v) {
		if (backbut.getId() == v.getId()) {
			MineAttentionActivity.this.finish();
		} else if (v.getId() == searchmapbutton.getId()) {
			// 搜索按钮的监听事件实现方法
			String searchcontent=edittext.getEditableText().toString().trim();//去掉前后空格
			if(null==searchcontent||searchcontent.equals("")){
				Toast.makeText(this, getLanguageString("请输入检索内容"), Toast.LENGTH_SHORT).show();
				return;
			}else{//将搜索记录保存到ShardPreference中
				String history;
				try {
					history = Constant.decode(Constant.key,sp.getString(keyname,"").trim());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					history="";
				}//用，隔开
				if(!history.contains(searchcontent)){//去重复
					history=history+searchcontent+",";
					e.putString(keyname,Constant.encode(Constant.key,history));
					e.apply();
				}
				zuijinlayout.setVisibility(View.GONE);
				goSearch(searchcontent);
			}
		} else if (edittext.getId() == v.getId()) {
			if (zuijinlayout.getVisibility() != View.VISIBLE) {
				try {
					showHistroyRecords();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (nodatafound.getId() == v.getId()) {
			Intent intentricheng = new Intent(this, HuiYiRiChengActivity.class);
			intentricheng.putExtra("isricheng", 1);
			startActivity(intentricheng);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if (edittext.getEditableText().toString().trim().equals("")) {
			list_adapter.clear();
			list_adapter.addAll(list_all);
			mineAttentionListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		if (zuijinlayout.getVisibility() == View.VISIBLE) {
			zuijinlayout.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * 我的关注数据变更 广播接收器
	 */
	class GuanzhuChangeBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			pro = ProgressDialog.show(MineAttentionActivity.this, "",
					"Loading……");// google自带dialog
			pro.setCancelable(true);// 点击dialog外空白位置是否消失
			pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
			try {
				loadData();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		// TODO Auto-generated method stub
		if (mBroadcastReceiver != null) {
			this.unregisterReceiver(mBroadcastReceiver);
		}
		if(receiver!=null){
			this.unregisterReceiver(receiver);
		}
		MyApplication.remove(this);
		super.onDestroy();
	}

	/** 记录前台切后台时间 */
	private Long TimeStart;
	/** 记录后台切前台时间 */
	private Long TimeEnd;
	/**
	 * 接收广播
	 **/
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				if (BaseActivity.isAppOnForeground(context)) {
					isActive = false;
					TimeStart = System.currentTimeMillis() / 1000;
				}
			}
		}
	};

	/**
	 * 注册广播
	 **/
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 挂起时调用的方法
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (!BaseActivity.isAppOnForeground(this)) {
			isActive = false;
			TimeStart = System.currentTimeMillis() / 1000;
		}
	}

	/**
	 * 唤醒时调用的方法
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!isActive) {
			TimeEnd = System.currentTimeMillis() / 1000;
			Integer time = getResources().getInteger(R.integer.time);
			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
				BaseActivity.loginValidation(context);
			}
		}
	}
}
