package com.gheng.exhibit.view.checkin;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForDaHuiDianPingZhuTiList;
import com.gheng.exhibit.view.checkin.checkin.AddDaHuiDianPingZhuTiActivity;
import com.gheng.exhibit.view.checkin.checkin.DaHuiDianPingMineActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.AsyncTaskForGetDaHuiDianPingZhuTiList;
import com.hebg3.mxy.utils.AsyncTaskForSwispLayoutRefreshStop;
import com.hebg3.mxy.utils.DaHuiDianPingZhuTiListPojo;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 大会点评 界面
 * 
 * @author 马晓勇
 * 
 */
public class DaHuiDianPingActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener {

	@ViewInject(R.id.but_fanhui)
	Button but_fanhui;
	@ViewInject(R.id.in_title)
	TextView in_title;
	@ViewInject(R.id.fazhutitv)
	TextView fazhutitv;
	// @ViewInject(R.id.wodelogo)
	// ImageButton wodelogo;
	@ViewInject(R.id.downlayout)
	RelativeLayout downlayout;
	@ViewInject(R.id.swipe)
	SwipeRefreshLayout swipe;
	@ViewInject(R.id.rv)
	RecyclerView rv;
	@ViewInject(R.id.fabuzhutismalllayout)
	RelativeLayout fabuzhutismalllayout;

	public LinearLayoutManager llm;
	public AdapterForDaHuiDianPingZhuTiList adapter;
	public ArrayList<DaHuiDianPingZhuTiListPojo> list = new ArrayList<DaHuiDianPingZhuTiListPojo>();// 消息提醒数据集合
	public int pagecount = 1;// 总页数
	public int pagenum = 1;// 当前页
	public ProgressDialog pd;
	public int loadmode = 0;// 0 下拉刷新 1 上提分页
	public int isloading = 0;// 是否正在加载数据 0未加载 1正在加载

	public String userid = "0";// 0是查询全部
	private User Parent;
	public String cachename = "dahuidianpingzhuticache";
	public SharedPreferences sp;
	public int count = 0;
	Gson g = new Gson();

	Handler h = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			isloading = 0;
			swipe.setRefreshing(false);
			swipe.setEnabled(true);
			if (pd != null) {
				pd.dismiss();
			}
			if (msg.what == 500) {// cookie超时
				BaseActivity.gotoLoginPage(DaHuiDianPingActivity.this);
			}
			if (msg.what == -1) {// 查询失败
				if (loadmode == 1) {
					pagenum--;
				}
				Toast.makeText(DaHuiDianPingActivity.this,
						getLanguageString("请求失败"), Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 0) {// 没数据
				pagecount = msg.arg1;
				if (loadmode == 1) {
					pagenum--;
				}
				if (list.size() < 1 && loadmode == 0) {// 下拉刷新没有数据才显示没有数据tv
					list.clear();
					adapter.notifyDataSetChanged();
					Toast.makeText(DaHuiDianPingActivity.this,
							getLanguageString("暂时没有数据"), Toast.LENGTH_SHORT)
							.show();
				}
			}
			if (msg.what == 1) {// 有数据
				pagecount = msg.arg1;
				if (loadmode == 0) {
					list.clear();
				}
				list.addAll((ArrayList<DaHuiDianPingZhuTiListPojo>) msg.obj);
				System.out.println("aaa:DaHuiDianPingActivity:Handler:list==="
						+ list.toString());
				adapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dahuidianping);

		sp = context.getSharedPreferences(cachename, Activity.MODE_PRIVATE);
		// 打开数据库
		try {
			DbUtils db = DbUtils.create(this);
			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
			userid = Constant.decode(Constant.key, Parent.getUserId());
		} catch (DbException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		but_fanhui.setOnClickListener(this);
		// 隐藏大会点评"我的"
		// wodelogo.setOnClickListener(this);
		// zyj添加
		// wodelogo.setVisibility(View.GONE);
		if (!Parent.getUserjuese().equals("1")
				&& !Parent.getUserjuese().equals("0"))
			downlayout.setVisibility(View.GONE);
		fabuzhutismalllayout.setOnClickListener(this);

		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		rv.setHasFixedSize(true);// 强制item高度一致，加强加载效率
		rv.addItemDecoration(// 为RecyclerView添加divider
		new HorizontalDividerItemDecoration.Builder(this)
				.color(this.getResources().getColor(R.color.beijing))
				.size(getResources().getDimensionPixelSize(
						R.dimen.jiabinliuyan_line))
				.margin(getResources().getDimensionPixelSize(
						R.dimen.title_padding),
						getResources().getDimensionPixelSize(
								R.dimen.title_padding)).build());

		llm = new LinearLayoutManager(this);// 设置RecyclerView的布局管理器（必须设置，否则RecyclerView会崩溃）
		llm.setOrientation(LinearLayoutManager.VERTICAL);// 设置横向或纵向展示item
		rv.setLayoutManager(llm);// 为RecyclerView配置布局管理器 必须手动配置！
		rv.addOnScrollListener(new rvonscrolllistener());
		adapter = new AdapterForDaHuiDianPingZhuTiList(this, list, userid);// 初始化用户自定义适配器
		rv.setAdapter(adapter);// 为recyclerView设置适配器
		// try {
		// goRequest(pagenum);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		in_title.setText(getLanguageString("嘉宾留言"));
		fazhutitv.setText(getLanguageString("发表话题"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == but_fanhui) {
			this.finish();
		}
		// if (v == wodelogo) {
		// // 启动“我的”主题界面
		// Intent i = new Intent(this, DaHuiDianPingMineActivity.class);
		// startActivity(i);
		// }
		if (v == fabuzhutismalllayout) {
			Intent i = new Intent();
			i.setClass(this, AddDaHuiDianPingZhuTiActivity.class);
			this.startActivity(i);
		}
	}

	public void goRequest(int page, String refresh) throws Exception {

		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			if (getDataFromCache(pagenum) != null) {
				if (loadmode == 1) {
					list.addAll(getDataFromCache(pagenum));
					adapter.notifyDataSetChanged();
				} else {
					list.clear();
					list.addAll(getDataFromCache(pagenum));
					adapter.notifyDataSetChanged();
				}
			} else {
				if (loadmode == 1) {
					this.pagenum--;
				}
				Toast.makeText(this, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			}
			return;
		} else {
			if (refresh.equals("refresh") || count == 0) {
				pd = new ProgressDialog(this);
				pd.setMessage(BaseActivity.getLanguageString("加载中..."));
				pd.setCancelable(true);
				pd.setCanceledOnTouchOutside(false);
				pd.show();
			}
			AsyncTaskForGetDaHuiDianPingZhuTiList at = new AsyncTaskForGetDaHuiDianPingZhuTiList(
					h.obtainMessage(), pagenum, Constant.decode(Constant.key,
							Parent.getUserId()), this);
			at.execute(1);
		}
	}

	/**
	 * 从缓存获取json解析成对象并返回
	 * 
	 * @param pagenum
	 *            页码
	 * @return 解析后的数据集合
	 */
	public ArrayList<DaHuiDianPingZhuTiListPojo> getDataFromCache(int pagenum) {
		if (!sp.getString("pagenum" + pagenum, "").equals("")) {
			Type type = new TypeToken<ArrayList<DaHuiDianPingZhuTiListPojo>>() {
			}.getType();// 设置集合type
			ArrayList<DaHuiDianPingZhuTiListPojo> infos;
			try {
				infos = g.fromJson(
						Constant.decode(Constant.key,
								sp.getString("pagenum" + pagenum, "")), type);
				return infos;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	// swipelayout的下拉刷新监听方法
	public void onRefresh() {
		// TODO Auto-generated method stub
		swipe.setEnabled(false);
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			AsyncTaskForSwispLayoutRefreshStop at = new AsyncTaskForSwispLayoutRefreshStop(
					this.h.obtainMessage());
			at.execute(1);
			Toast.makeText(this, getLanguageString("网络不给力"), Toast.LENGTH_SHORT)
					.show();
		} else {
			loadmode = 0;
			pagenum = 1;
			try {
				goRequest(pagenum, "refresh");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * RecyclerView 的滑动监听事件，用来判断是否拉倒最后一项，自动加载下一页
	 */
	class rvonscrolllistener extends RecyclerView.OnScrollListener {

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			if (!IsWebCanBeUse.isWebCanBeUse(getApplicationContext())) {
				if (llm.findLastCompletelyVisibleItemPosition() == list.size() - 1) {
					loadmode = 1;
					pagenum++;
					try {
						goRequest(pagenum, "refresh");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if (llm.findLastCompletelyVisibleItemPosition() == list.size() - 1) {// 显示到最后一行启动线程加载下一页
					if (pagenum + 1 <= pagecount && isloading == 0) {
						loadmode = 1;
						pagenum++;
						try {
							goRequest(pagenum, "refresh");
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("请求下一页");
					}
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			goRequest(pagenum, "norefresh");
		} catch (Exception e) {
			e.printStackTrace();
		}
		count++;
	}
}
