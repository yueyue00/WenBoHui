package com.gheng.exhibit.view.noused;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smartdot.wenbo.huiyi.R;

/**
 *	我要报名页面
 * @author lileixing
 */
public class SignUpActivity extends BaseActivity{

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	@ViewInject(R.id.workPlace)
	private EditText workPlace;
	
	@ViewInject(R.id.position)
	private EditText position;
	
	private long id;
	
	@ViewInject(R.id.next)
	private TextView next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		id = getIntent().getLongExtra("id", 0);
		position.setHint(getLanguageString("请输入职位"));
		workPlace.setHint(getLanguageString("请输入单位名称"));
		workPlace.setText(SharedData.getString(SharedData.WORKPLACE));
		position.setText(SharedData.getString(SharedData.POSITION));
	}
	
	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString("报名"));
		next.setText(getLanguageString("报名"));
	}
	
	@Override
	protected void init() {
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
			}
			
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
	}

	//签到
	@OnClick(value={R.id.next})
	public void signUp(View v){
		String positionTxt = position.getText().toString();
		String workPlaceTxt = workPlace.getText().toString();
		
		String message = "请完善信息";
		if(StringTools.isBlank(positionTxt) || StringTools.isBlank(workPlaceTxt)){
			toastShort(getLanguageString(message));
			return;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put(SharedData.WORKPLACE, workPlaceTxt);
		map.put(SharedData.POSITION, positionTxt);
		SharedData.commit(map);
		
		ProgressTools.showDialog(context);
		Map<String,String> data = new HashMap<String, String>();
		data.put("workPlace", workPlaceTxt);
		data.put("job", positionTxt);
		
		ApiUtil.postSign(id, 1,data, new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
			}
			@Override
			public void callback(boolean success, long id, int type, int mode, Object data) {
				ProgressTools.hide();
				if(success){
					toastShort(getLanguageString("报名成功"));
					Intent i = new Intent();
					i.putExtras(getIntent());
					setResult(100, i);
					finish();
				}else{
					toastNetError();
				}
			}
		});
	}
}
