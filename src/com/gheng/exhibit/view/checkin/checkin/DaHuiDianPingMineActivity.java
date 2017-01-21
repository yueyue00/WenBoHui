package com.gheng.exhibit.view.checkin.checkin;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForDaHuiDianPingZhuTiList;
import com.hebg3.mxy.utils.AsyncTaskForGetDaHuiDianPingZhuTiList;
import com.hebg3.mxy.utils.AsyncTaskForSwispLayoutRefreshStop;
import com.hebg3.mxy.utils.DaHuiDianPingZhuTiListPojo;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;

/**
 * 大会点评 我的 界面
 * 
 * @author 马晓勇
 * 
 */
public class DaHuiDianPingMineActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener {

	@ViewInject(R.id.downlayout)
	RelativeLayout downlayout;
	@ViewInject(R.id.goback)
	ImageButton goback;
	@ViewInject(R.id.titletv)
	TextView titletv;
	@ViewInject(R.id.swipe)
	SwipeRefreshLayout swipe;
	@ViewInject(R.id.rv)
	RecyclerView rv;
//	@ViewInject(R.id.wodelogo)
//	ImageButton wodelogo;

	public LinearLayoutManager llm;
	public AdapterForDaHuiDianPingZhuTiList adapter;
	public ArrayList<DaHuiDianPingZhuTiListPojo> list = new ArrayList<DaHuiDianPingZhuTiListPojo>();// 消息提醒数据集合
	public int pagecount = 1;// 总页数
	public int pagenum = 1;// 当前页
	public ProgressDialog pd;
	public int loadmode = 0;// 0 下拉刷新 1 上提分页
	public int isloading = 0;// 是否正在加载数据 0未加载 1正在加载

	String userid = "";
	String username = "";
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
				BaseActivity.gotoLoginPage(DaHuiDianPingMineActivity.this);
			}
			if (msg.what == -1) {// 查询失败
				if (loadmode == 1) {
					pagenum--;
				}
				Toast.makeText(DaHuiDianPingMineActivity.this,
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
					Toast.makeText(DaHuiDianPingMineActivity.this,
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
				adapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dahuidianping);

		downlayout.setVisibility(View.GONE);
		goback.setOnClickListener(this);
//		wodelogo.setVisibility(View.GONE);
		userid = getIntent().getStringExtra("userid");
		username = getIntent().getStringExtra("username");
		if (userid == null || userid.equals("")) {
			// 查找
			try {
				DbUtils db = DbUtils.create(context);
				User parent = db.findFirst(Selector.from(User.class).where(
						"id", "=", "1"));
				userid = Constant.decode(Constant.key, parent.getUserId());
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		rv.setHasFixedSize(true);// 强制item高度一致，加强加载效率
		rv.addItemDecoration(// 为RecyclerView添加divider
		new HorizontalDividerItemDecoration.Builder(this)
				.color(this.getResources().getColor(R.color.share_text))
				.size(getResources().getDimensionPixelSize(R.dimen.half1dp))
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

		goRequest(pagenum);

	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stubx
		// titletv.setText(getLanguageString("我的"));
		titletv.setText(username);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == goback) {
			this.finish();
		}
	}

	public void goRequest(int page) {

		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			if (loadmode == 1) {
				this.pagenum--;
			}
			Toast.makeText(this, getLanguageString("网络不给力"), Toast.LENGTH_SHORT)
					.show();
		} else {
			pd = new ProgressDialog(this);
			pd.setMessage(BaseActivity.getLanguageString("加载中..."));
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();

			AsyncTaskForGetDaHuiDianPingZhuTiList at = new AsyncTaskForGetDaHuiDianPingZhuTiList(
					h.obtainMessage(), pagenum, userid, this);
			at.execute(1);
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
			goRequest(pagenum);
		}
	}

	/**
	 * RecyclerView 的滑动监听事件，用来判断是否拉倒最后一项，自动加载下一页
	 */
	class rvonscrolllistener extends RecyclerView.OnScrollListener {

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			if (llm.findLastCompletelyVisibleItemPosition() == list.size() - 1) {// 显示到最后一行启动线程加载下一页
				if (pagenum + 1 <= pagecount && isloading == 0) {
					loadmode = 1;
					pagenum++;
					goRequest(pagenum);
					System.out.println("请求下一页");
				}
			}
		}
	}
}
