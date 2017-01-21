package com.gheng.exhibit.view.noused;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.HttpWrapper;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.http.body.request.MineCommentParam;
import com.gheng.exhibit.http.body.response.MineCommentData;
import com.gheng.exhibit.http.response.MineCommentResponse;
import com.gheng.exhibit.model.databases.ScheduleInfo;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.MainActivity;
import com.gheng.exhibit.view.adapter.MineCommentAdapter;
import com.gheng.exhibit.view.adapter.MyViewPapgerAdapter;
import com.gheng.exhibit.widget.EmptyView;
import com.gheng.exhibit.widget.MyViewPager;
import com.gheng.exhibit.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的评论列表
 * 
 * @author zhaofangfang
 */
public class MineCommentActivity extends BaseActivity implements
		OnCheckedChangeListener, OnClickListener, OnItemClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.radio_group)
	private RadioGroup radio_group;

	@ViewInject(R.id.vp)
	private MyViewPager vp;

	private MyViewPapgerAdapter adapter;

	@ViewInject(R.id.rbtn_company)
	private RadioButton rbtn_company;

	@ViewInject(R.id.rbtn_product)
	private RadioButton rbtn_product;

	@ViewInject(R.id.rbtn_scheduleinfo)
	private RadioButton rbtn_scheduleinfo;

	@ViewInject(R.id.tab_group)
	private LinearLayout tab_group;

	private ViewHolder holder1 = new ViewHolder();

	private ViewHolder holder2 = new ViewHolder();

	private ViewHolder holder3 = new ViewHolder();

	private int currentType = Constant.TYPE_COMPANY;
	private EmptyView ev1;
	private EmptyView ev2;
	private EmptyView ev3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_mine_comment);
		ev1 = new EmptyView(this);
		ev2 = new EmptyView(this);
		ev3 = new EmptyView(this);
	}

	@Override
	protected void setI18nValue() {

	}

	@Override
	protected void init() {
		titleBar.setText(getLanguageString("我的评论"));
		Drawable drawable = this.getResources().getDrawable(R.drawable.home);
		titleBar.setRightDrawable(drawable);
		titleBar.showRightImage(true);
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
				startTo(MainActivity.class);
			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		List<View> lists = new ArrayList<View>();
		View v1 = getLayoutInflater().inflate(R.layout.pull_listview, null);
		View v2 = getLayoutInflater().inflate(R.layout.pull_listview, null);
		View v3 = getLayoutInflater().inflate(R.layout.pull_listview, null);
		lists.add(v1);
		lists.add(v2);
		lists.add(v3);
		holder1.lv = (PullToRefreshListView) v1;
		holder1.adapter = new MineCommentAdapter(this);
		holder1.lv.setAdapter(holder1.adapter);
		holder1.lv.setEmptyView(ev1);
		holder1.lv.setOnItemClickListener(new OnItemClickImpl(
				Constant.TYPE_COMPANY));
		// holder1.lv.setOnItemClickListener(this);

		holder2.lv = (PullToRefreshListView) v2;
		holder2.adapter = new MineCommentAdapter(this);
		holder2.lv.setAdapter(holder2.adapter);
		holder2.lv.setEmptyView(ev2);
		holder2.lv.setOnItemClickListener(new OnItemClickImpl(
				Constant.TYPE_PRODUCT));
		// holder2.lv.setOnItemClickListener(this);

		holder3.lv = (PullToRefreshListView) v3;
		holder3.adapter = new MineCommentAdapter(this);
		holder3.lv.setAdapter(holder3.adapter);
		holder3.lv.setEmptyView(ev3);
		holder3.lv.setOnItemClickListener(new OnItemClickImpl(
				Constant.TYPE_SCHEDULE_INFO));

		adapter = new MyViewPapgerAdapter(lists);
		vp.setAdapter(adapter);
		vp.setOffscreenPageLimit(3);
		radio_group.setOnCheckedChangeListener(this);

		rbtn_company.setText(this.getLanguageString("展商"));
		rbtn_product.setText(this.getLanguageString("展品"));
		rbtn_scheduleinfo.setText(this.getLanguageString("演讲题目"));

		I18NUtils.setPullView(holder1.lv, this);
		I18NUtils.setPullView(holder2.lv, this);
		I18NUtils.setPullView(holder3.lv, this);
		loadData();
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, 0,
				Constant.TYPE_MINE_COMMENT, Constant.SERACH_TYPE_ENTER, null);
	}

	class ViewHolder {
		PullToRefreshListView lv;
		MineCommentAdapter adapter;
		int pageno = -1;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_company:
			vp.setCurrentItem(0, false);
			showIndex(0);
			currentType = Constant.TYPE_COMPANY;
			loadData();
			break;
		case R.id.rbtn_product:
			vp.setCurrentItem(1, false);
			showIndex(1);
			currentType = Constant.TYPE_PRODUCT;
			loadData();
			break;
		case R.id.rbtn_scheduleinfo:
			vp.setCurrentItem(2, false);
			showIndex(2);
			currentType = Constant.TYPE_SCHEDULE_INFO;
			loadData();
			break;
		}
	}

	private void showIndex(int index) {
		int childCount = tab_group.getChildCount();
		for (int i = 0; i < childCount; i++) {
			if (index == i) {
				tab_group.getChildAt(i).setVisibility(View.VISIBLE);
			} else {
				tab_group.getChildAt(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 加载数据
	 */
	private void loadData() {

		BaseRequestData<MineCommentParam> requestData = new BaseRequestData<MineCommentParam>(
				"membercomment");
		MineCommentParam param = new MineCommentParam();
		requestData.body = param;
		param.type = currentType + "";
		ProgressTools.showDialog(this);
		new HttpWrapper().post(requestData,
				new CallBack<MineCommentResponse>() {
					@Override
					public void onSuccess(MineCommentResponse entity) {
						ProgressTools.hide();
						PageBody<MineCommentData> body = entity.body;
						if (currentType == Constant.TYPE_COMPANY) {
							ev1.show(true);
							holder1.lv.onRefreshComplete();
							holder1.adapter.setData(body.rdata);
							if (holder1.pageno >= entity.body.pagecount) {
								holder1.lv.setMode(Mode.PULL_FROM_START);
							} else {
								holder1.lv.setMode(Mode.BOTH);
							}
						} else if (currentType == Constant.TYPE_PRODUCT) {
							ev2.show(true);
							holder2.lv.onRefreshComplete();
							holder2.adapter.setData(body.rdata);
							if (holder2.pageno >= entity.body.pagecount) {
								holder2.lv.setMode(Mode.PULL_FROM_START);
							} else {
								holder2.lv.setMode(Mode.BOTH);
							}
						} else if (currentType == Constant.TYPE_SCHEDULE_INFO) {
							ev3.show(true);
							holder3.lv.onRefreshComplete();
							holder3.adapter.setData(body.rdata);
							if (holder3.pageno >= entity.body.pagecount) {
								holder3.lv.setMode(Mode.PULL_FROM_START);
							} else {
								holder3.lv.setMode(Mode.BOTH);
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ProgressTools.hide();
						toastNetError();
					}
				});
	}

	class OnItemClickImpl implements OnItemClickListener {
		private int type;

		OnItemClickImpl(int type) {
			this.type = type;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			Bundle bd = new Bundle();
			switch (type) {
			case Constant.TYPE_COMPANY:
				MineCommentData companyData = holder1.adapter
						.getItem(position - 1);
				bd.putLong("id", Long.parseLong(companyData.objid));
				startTo(CompanyInfoActivity.class, bd);
				break;
			case Constant.TYPE_PRODUCT:
				MineCommentData productData = holder2.adapter
						.getItem(position - 1);
				bd.putLong("id", Long.parseLong(productData.objid));
				startTo(ProductInfoActivity.class, bd);
				break;
			case Constant.TYPE_SCHEDULE_INFO:
				MineCommentData scheduleInfoData = holder3.adapter
						.getItem(position - 1);
				try {
					ScheduleInfo info = BaseActivity.getDbUtils().findById(
							ScheduleInfo.class, scheduleInfoData.objid);
					bd.putLong("id", info.getScheduleid());
					startTo(ScheduleInfoActivity.class, bd);
				} catch (DbException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		System.out.println("onItemClickonItemClick");
	}

}
