package com.gheng.exhibit.view.checkin;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gheng.exhibit.database.task.MineScheduleFavListTask;
import com.gheng.exhibit.database.task.ScheduleTask;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.response.ScheduleInfoSupportData;
import com.gheng.exhibit.http.body.response.ScheduleListData;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.MainActivity;
import com.gheng.exhibit.view.adapter.MineScheduleInfoAdapter;
import com.gheng.exhibit.view.adapter.MyViewPapgerAdapter;
import com.gheng.exhibit.view.noused.ScheduleInfoActivity;
import com.gheng.exhibit.view.noused.ScheduleSearchActivity;
import com.gheng.exhibit.widget.EmptyView;
import com.gheng.exhibit.widget.MyViewPager;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView;
import com.gheng.exhibit.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smartdot.wenbo.huiyi.R;

/**
 *	会议列表
 * @author lileixing
 */
public class MineScheduleListActivity extends BaseActivity implements OnCheckedChangeListener{

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	SignViewHolder signViewHolder = new SignViewHolder();
	FavViewHolder favViewHolder = new FavViewHolder();
	
	@ViewInject(R.id.vp)
	private MyViewPager vp;
	
	private List<View> views = new ArrayList<View>();
	
	private MyViewPapgerAdapter adapter;
	
	@ViewInject(R.id.radio_group)
	private RadioGroup radio_group;
	
	@ViewInject(R.id.tab_group)
	private LinearLayout tab_group;
	
	@ViewInject(R.id.rbtn_sign)
	private RadioButton rbtn_sign;
	@ViewInject(R.id.rbtn_fav)
	private RadioButton rbtn_fav;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_schedule_list);
		
		View v1 = getLayoutInflater().inflate(R.layout.view_mine_sign_schedule, null);
		ViewUtils.inject(signViewHolder, v1);
		
		favViewHolder.lv = (PullToRefreshListView) getLayoutInflater().inflate(R.layout.pull_listview, null);
		favViewHolder.emptyView = new EmptyView(this);
		favViewHolder.emptyView.show(false);
		favViewHolder.lv.setEmptyView(favViewHolder.emptyView);
		favViewHolder.lv.setMode(Mode.DISABLED);
		favViewHolder.adapter = new MineScheduleInfoAdapter(this);
		favViewHolder.lv.setAdapter(favViewHolder.adapter);
		
		views.add(v1);
		views.add(favViewHolder.lv);
		adapter = new MyViewPapgerAdapter(views);
		vp.setAdapter(adapter);
		
		signViewHolder.lv.setHeaderView(getLayoutInflater().inflate(R.layout.item_schedule_title,
				signViewHolder.lv, false));
		//因为去掉ScheduleAdapter这个类所以这里报错了
		// signViewHolder.adapter = new ScheduleAdapter(this,signViewHolder.lv);
		// signViewHolder.adapter.setMine(true);
		// signViewHolder.lv.setAdapter(signViewHolder.adapter);
		signViewHolder.emptyView.setVisibility(View.GONE);
		signViewHolder.emptyView.setText(getLanguageString("未找到相关信息"));
		
		
		radio_group.setOnCheckedChangeListener(this);
		
		rbtn_sign.setText(getLanguageString("我的报名"));
		rbtn_fav.setText(getLanguageString("我的收藏"));
	}
	
	@Override
	protected void setI18nValue() {
	}
	
	public void showEmptyView(boolean show){
		if(show){
			signViewHolder.emptyView.setVisibility(View.VISIBLE);
		}else{
			signViewHolder.emptyView.setVisibility(View.GONE);
		}
	}
	
	@OnClick(value = {R.id.edt_name})
	public void clickEdtName(View v){
		Intent intent = new Intent(context, ScheduleSearchActivity.class);
		startActivityForResult(intent, 100);
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
	
	@Override
	protected void init() {
		titleBar.setText(getIntent().getStringExtra("title"));
		signViewHolder.lv.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent i = new Intent(context, ScheduleInfoActivity.class);
				Bundle bd = new Bundle();
				bd.putLong("id", id);
				bd.putInt("groupPosition", groupPosition);
				bd.putInt("childPosition", childPosition);
				i.putExtras(bd);
				startActivityForResult(i, 200);
				return false;
			}
		});
		
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.home);
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
		
		favViewHolder.lv.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadMineFav(2);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadMineFav(3);
			}
		});
		
		loadSignSchedule();
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, 0, Constant.TYPE_MINE_SCHEDULE, Constant.SERACH_TYPE_ENTER, null);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//详情页面返回数据
		/*if(requestCode == 200 && resultCode == 100){
			int groupPosition = data.getIntExtra("groupPosition", 0);
			int childPosition = data.getIntExtra("childPosition", 0);
			ScheduleListSupportData child = signViewHolder.adapter.getChild(groupPosition, childPosition);
			child.issign = data.getIntExtra("issign", child.issign);
			child.ispraise = data.getIntExtra("ispraise", child.ispraise);
			child.praisecount = data.getIntExtra("praisecount", child.praisecount);
			adapter.notifyDataSetChanged();
		}*/
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private void loadSignSchedule(){
		ProgressTools.showDialog(this);
		new ScheduleTask(true,null,new CallBack<List<ScheduleListData>>() {
			@Override
			public void onSuccess(List<ScheduleListData> entity) {
				ProgressTools.hide();
				if(AppTools.isNotBlack(entity)){
					signViewHolder.emptyView.setVisibility(View.GONE);
				}else{
					signViewHolder.emptyView.setVisibility(View.VISIBLE);
				}
//				signViewHolder.adapter.setDatas(entity);
				int groupCount = signViewHolder.lv.getCount();
				for (int i = 0; i < groupCount; i++) {
//					ScheduleListData group = signViewHolder.adapter.getGroup(i);
					boolean expand = false;
//					if(group.isToday){
//						signViewHolder.lv.expandGroup(i);
//						expand = true;
//					}else{
//						signViewHolder.lv.collapseGroup(i);
//					}
					if(!expand){
						signViewHolder.lv.expandGroup(0);
					}
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
			}
		}).execute();
	}
	
	//加载我的收藏数据
	private void loadMineFav(int mode){
		if(mode == 1)
			ProgressTools.showDialog(this);
		favViewHolder.pageno = 0;
		new MineScheduleFavListTask(new CallBack<List<ScheduleInfoSupportData>>() {
			@Override
			public void onSuccess(List<ScheduleInfoSupportData> entity) {
				ProgressTools.hide();
				favViewHolder.emptyView.show(true);
				favViewHolder.adapter.setData(entity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				favViewHolder.emptyView.show(true);
			}
		}).execute();
	}

	//报名列表
	class SignViewHolder{
		@ViewInject(R.id.lv)
		private PinnedHeaderExpandableListView lv;
		
		//private ScheduleAdapter adapter;
		
		@ViewInject(R.id.emptyView)
		private TextView emptyView;
	}
	
	//收藏列表
	class FavViewHolder{
		PullToRefreshListView lv;
		
		MineScheduleInfoAdapter adapter;
		
		private EmptyView emptyView;
		
		private int pageno = -1;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_sign:
			vp.setCurrentItem(0, false);
			showIndex(0);
			break;
		case R.id.rbtn_fav:
			vp.setCurrentItem(1, false);
			showIndex(1);
			if(favViewHolder.pageno == -1){
				loadMineFav(1);
			}
			break;
		}
	}
	
}
