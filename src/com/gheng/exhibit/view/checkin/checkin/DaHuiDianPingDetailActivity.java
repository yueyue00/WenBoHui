package com.gheng.exhibit.view.checkin.checkin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForDaHuiDianPingZhuTiDetail;
import com.hebg3.mxy.utils.AsyncTaskForDaHuiDianPingSendComment;
import com.hebg3.mxy.utils.AsyncTaskForGetDaHuiDianPingZhuTiPojo;
import com.hebg3.mxy.utils.AsyncTaskForSwispLayoutRefreshStop;
import com.hebg3.mxy.utils.DaHuiDianPingZhuTiListPojo;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 大会点评_详情 界面
 * 
 * @author 马晓勇
 * 
 */
public class DaHuiDianPingDetailActivity extends Activity implements
		OnClickListener, OnRefreshListener {

	@ViewInject(R.id.dianpingdetail_fabuzhutilayout)
	RelativeLayout xiezhutilayout;

	@ViewInject(R.id.dianpingdetail_fapinglunlayout)
	RelativeLayout fapinglunlayout;
	@ViewInject(R.id.dianpingdetail_downlayout)
	RelativeLayout downlayout;
	@ViewInject(R.id.dianpingdetail_but_fanhui)
	Button but_fanhui;
	@ViewInject(R.id.dianpingdetail_in_title)
	TextView in_title;
	@ViewInject(R.id.dianpingdetail_swipe)
	SwipeRefreshLayout swipe;
	@ViewInject(R.id.dianpingdetail_rv)
	RecyclerView rv;
	// @ViewInject(R.id.wodelogo)
	// ImageButton wodelogo;
	@ViewInject(R.id.dianpingdetail_sendbutton)
	Button sendbutton;
	@ViewInject(R.id.dianpingdetail_fapingluned)
	public EditText fapingluned;

	public String replyuserid = "";// 回复人id
	public String replyusername = "";// 回复人姓名

	public LinearLayoutManager llm;
	public AdapterForDaHuiDianPingZhuTiDetail adapter;
	public DaHuiDianPingZhuTiListPojo pojo;

	String userid = "";
	User Parent;
	ProgressDialog pd;

	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (pd != null) {
				pd.dismiss();
			}

			swipe.setRefreshing(false);
			swipe.setEnabled(true);

			if (msg.what == 500) {
				BaseActivity.gotoLoginPage(DaHuiDianPingDetailActivity.this);
				return;
			}

			if (msg.what == 1 && msg.obj instanceof DaHuiDianPingZhuTiListPojo) {// 刷新成功
				// 刷新adapter
				pojo = (DaHuiDianPingZhuTiListPojo) msg.obj;
				adapter = null;// 释放原适配器对象
				adapter = new AdapterForDaHuiDianPingZhuTiDetail(
						DaHuiDianPingDetailActivity.this, pojo);// pojo更新完毕，重新生成适配器
				rv.setAdapter(adapter);// 重新为recyclerView设置适配器
			} else if (msg.what == 1 && msg.obj instanceof Integer) {// 评论成功
				fapingluned.setText("");
				replyuserid = "";// 重置回复人id
				replyusername = "";// 重置回复人姓名
				fapingluned.setHint(BaseActivity.getLanguageString("发表评论"));
				Toast.makeText(DaHuiDianPingDetailActivity.this,
						BaseActivity.getLanguageString("谢谢参与"),
						Toast.LENGTH_SHORT).show();
				onRefresh();
			} else {// 网络不给力
				Toast.makeText(DaHuiDianPingDetailActivity.this,
						BaseActivity.getLanguageString("发布失败"),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dahuidianpingdetail);
		ViewUtils.inject(this);
		initView();
		but_fanhui.setOnClickListener(this);
		xiezhutilayout.setVisibility(View.GONE);
		// wodelogo.setVisibility(View.GONE);
		fapinglunlayout.setVisibility(View.VISIBLE);
		sendbutton.setOnClickListener(this);
		swipe.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));
		swipe.setOnRefreshListener(this);
		// 查找
		try {
			DbUtils db = DbUtils.create(DaHuiDianPingDetailActivity.this);
			Parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			userid = Constant.decode(Constant.key, Parent.getUserId());
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// zyj添加
		if (!Parent.getUserjuese().equals("1")
				&& !Parent.getUserjuese().equals("0"))
			downlayout.setVisibility(View.GONE);
		pojo = (DaHuiDianPingZhuTiListPojo) getIntent().getSerializableExtra(
				"pojo");

		llm = new LinearLayoutManager(this);// 设置RecyclerView的布局管理器（必须设置，否则RecyclerView会崩溃）
		llm.setOrientation(LinearLayoutManager.VERTICAL);// 设置横向或纵向展示item
		rv.setLayoutManager(llm);// 为RecyclerView配置布局管理器 必须手动配置！

		adapter = new AdapterForDaHuiDianPingZhuTiDetail(this, pojo);// 初始化用户自定义适配器
		rv.setAdapter(adapter);// 为recyclerView设置适配器

	}

	private void initView() {
		in_title.setText(BaseActivity.getLanguageString("详情"));
		fapingluned.setHint(BaseActivity.getLanguageString("发表评论"));
		sendbutton.setText(BaseActivity.getLanguageString("发送"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == but_fanhui) {
			this.finish();
		}
		if (v == sendbutton) {// 判断ed是否为空，不为空，启动线程发表评论
			if (fapingluned.getText().toString().trim().equals("")) {
				Toast.makeText(this, BaseActivity.getLanguageString("不能发送空消息"),
						Toast.LENGTH_SHORT).show();
				return;
			} else if (fapingluned.getText().toString().trim().length() > 200) {
				Toast.makeText(this,
						BaseActivity.getLanguageString("回复不能超过200字"),
						Toast.LENGTH_SHORT).show();
				return;
			} else if (BaseActivity.isContainEmoji(fapingluned.getText()
					.toString().trim())) {
				Toast.makeText(this,
						BaseActivity.getLanguageString("目前只支持发送纯文字内容"),
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				// 启动线程 发评论或回复
				if (!IsWebCanBeUse.isWebCanBeUse(this)) {
					Toast.makeText(this,
							BaseActivity.getLanguageString("网络不给力"),
							Toast.LENGTH_SHORT).show();
					return;
				}

				// pd = new ProgressDialog(this);
				// pd.setMessage(BaseActivity.getLanguageString("加载中..."));
				// pd.setCancelable(true);
				// pd.setCanceledOnTouchOutside(false);
				// pd.show();

				// 启动线程
				AsyncTaskForDaHuiDianPingSendComment at = new AsyncTaskForDaHuiDianPingSendComment(
						this, h.obtainMessage(), pojo.actionid, userid,
						fapingluned.getText().toString().trim(), replyuserid,
						replyusername);
				at.execute(1);
			}
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			AsyncTaskForSwispLayoutRefreshStop at = new AsyncTaskForSwispLayoutRefreshStop(
					this.h.obtainMessage());
			at.execute(1);
		} else {
			swipe.setEnabled(false);
			pd = new ProgressDialog(this);
			pd.setMessage(BaseActivity.getLanguageString("加载中..."));
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();

			AsyncTaskForGetDaHuiDianPingZhuTiPojo at = new AsyncTaskForGetDaHuiDianPingZhuTiPojo(
					h.obtainMessage(), pojo.actionid, this, userid);
			at.execute(1);
		}
	}
}
