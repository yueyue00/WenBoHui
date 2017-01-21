package com.gheng.exhibit.view.noused;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.BaseResponse;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.model.databases.BaseModel;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 评论提交
 * 
 * @author lileixing
 */
public class CommentSubmitActivity extends BaseActivity implements android.view.View.OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	// 提示
	@ViewInject(R.id.tv_tip)
	private TextView tv_tip;
	// 评论数量
	@ViewInject(R.id.tv_comment_num)
	private TextView tv_comment_num;
	// 编辑用户评论
	@ViewInject(R.id.edt_comment)
	private EditText edt_comment;
	// 触发提交功能
	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;
	
	@ViewInject(R.id.ratingBar)
	private RatingBar ratingBar;

	private BaseModel model;
	
	@ViewInject(R.id.ck_showname)
	private CheckBox ck_showname;
	
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		model = (BaseModel) getIntent().getSerializableExtra("model");
	}

	@Override
	protected void setI18nValue() {
		edt_comment.setHint(getLanguageString("我来说两句..."));
		tv_tip.setText(getLanguageString("评分"));
		tv_submit.setText(getLanguageString("发表评价"));
		titleBar.setText(getLanguageString("评论"));
		tv_name.setText((String) I18NUtils.getValue(model, "name"));
		tv_comment_num.setText(I18NUtils.getV(model, "commentcount") + getLanguageString("人评价过"));
		ck_showname.setText(getLanguageString("匿名评论"));
	}

	@Override
	protected void init() {
		titleBar.setOnClickListener(new OnClickListener() {
			@Override
			public void clickRightImage() {
			}

			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		tv_submit.setOnClickListener(this);
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_INFO, model.getId(), Constant.TYPE_COMMENT_SUBMIT, Constant.SERACH_TYPE_ENTER, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			commit();
			break;
		}
	}
	
	private void commit(){
		String content = edt_comment.getText().toString();
		int score = (int)ratingBar.getRating();
		if(score == 0){
			toastShort(getLanguageString("请打分"));
			return;
		}
		if(StringTools.isBlank(content)){
			toastShort(getLanguageString("请输入评论"));
			return ;
		}
		BaseRequestData<Map<String,String>> requestData = new BaseRequestData<Map<String,String>>("commentsubmit");
		Map<String,String> body = new HashMap<String, String>();
		int type = getIntent().getIntExtra("type", 0);
		body.put("id", model.getId()+"");
		body.put("type", type+"");
		body.put("score", score+"");
		body.put("content", content);
		if(ck_showname.isChecked()){
			body.put("showname", "0");
		}else{
			body.put("showname", "1");
		}
		body.put("surname", SharedData.getString(SharedData.SURNAME));
		body.put("name", SharedData.getString(SharedData.NAME));
		
		requestData.body = body;
		ProgressTools.showDialog(this);
		http.post(requestData, new CallBack<BaseResponse>() {

			@Override
			public void onSuccess(BaseResponse entity) {
				ProgressTools.hide();
				if(entity.retcode == 200){
					toastShort(getLanguageString("评论成功") + "," + getLanguageString("请等待"));
					finish();
				}else{
					toastShort(getLanguageString("评论异常"));
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				toastNetError();
			}
		});
	}

}
