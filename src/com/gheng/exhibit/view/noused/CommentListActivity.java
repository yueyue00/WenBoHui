package com.gheng.exhibit.view.noused;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.response.CommentListResponse;
import com.gheng.exhibit.model.databases.BaseModel;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.CommentAdapter;
import com.gheng.exhibit.widget.EmptyView;
import com.gheng.exhibit.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 评论列表
 * 
 * @author lileixing
 */
public class CommentListActivity extends BaseActivity implements OnClickListener,OnRefreshListener2<ListView>{

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.lv)
	private PullToRefreshListView lv;

	private BaseModel model;

	// 评论条
	@ViewInject(R.id.tv_to_comment)
	private TextView tv_to_comment;
	
	@ViewInject(R.id.ll_to_comment_list)
	private View ll_to_comment_list;
	
	@ViewInject(R.id.tv_name)
	private TextView tv_name;

	private CommentAdapter adapter;
	
	private int pageno = 1;

	private EmptyView emptyView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);
		model = (BaseModel) getIntent().getSerializableExtra("model");
		ll_to_comment_list.setVisibility(View.GONE);
		emptyView = new EmptyView(this);
		lv.setEmptyView(emptyView);
	}

	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString("评论"));
		tv_name.setText((String) I18NUtils.getValue(model, "name"));
		I18NUtils.setPullView(lv, this);
	}

	@Override
	protected void init() {
		adapter = new CommentAdapter(this);
		lv.setAdapter(adapter);
		tv_to_comment.setHint(getLanguageString("我来说两句..."));
		lv.setOnRefreshListener(this);
		tv_to_comment.setOnClickListener(this);
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
//				UIUtils.goHome();
			}
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		loadData(1);
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, model.getId(), Constant.TYPE_COMMENT, Constant.SERACH_TYPE_ENTER, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_to_comment:
			if(AppTools.isLogin()){
				startTo(CommentSubmitActivity.class, getIntent().getExtras());
			}else{
//				UIUtils.startToRegister(this);
			}		
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(2);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(3);
	}
	
	private void loadData(int state){
		if(state == 1){
			pageno = 1;
			ProgressTools.showDialog(this);
		}else if(2 == state){
			pageno = 1;
		}else{
			pageno ++;
		}
		
		BaseRequestData<Map<String,String>> requestData = new BaseRequestData<Map<String,String>>("commentlist");
		Map<String,String> body = new HashMap<String, String>();
		body.put("id", model.getId()+"");
		body.put("type", getIntent().getIntExtra("type", 0)+"");
		body.put("pno", pageno+"");
		requestData.body = body;
		http.post(requestData, new CallBack<CommentListResponse>() {
			@Override
			public void onSuccess(CommentListResponse entity) {
				ProgressTools.hide();
				emptyView.show(true);
				lv.onRefreshComplete();
				if(pageno == 1){
					adapter.setData(entity.body.rdata);
				}else{
					adapter.add(entity.body.rdata);
				}
				if(pageno < entity.body.pagecount){
					lv.setMode(Mode.BOTH);
				}else{
					lv.setMode(Mode.PULL_FROM_START);
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				lv.onRefreshComplete();
				toastNetError();
			}
		});
		
	}

}
