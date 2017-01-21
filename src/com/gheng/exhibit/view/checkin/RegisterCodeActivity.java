package com.gheng.exhibit.view.checkin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.request.RegCodeParam;
import com.gheng.exhibit.http.response.CompanySearchResponse;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 绑定手机功能
 * 
 * @author zhaofangfang
 */
public class RegisterCodeActivity extends BaseActivity {

	@ViewInject(R.id.et_mobile)
	private EditText mobileEt;

	@ViewInject(R.id.et_code)
	private EditText codeEt;

	@ViewInject(R.id.btn_code)
	private Button codeBtn;
	
	@ViewInject(R.id.btn_submit)
	private Button submitBtn;
	
	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	private int defaultNum = Constant.DEFAULT_MSG_TIMEOUT;
	private int num = defaultNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registercode);
	}

	@Override
	protected void init() {
		if(codeBtn == null){
			codeBtn = (Button) findViewById(R.id.btn_code);
		}
		codeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onCodeBtnClick();
			}
		});
		codeBtn.setText(getLanguageString("获取验证码"));
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSubmitBtnClick();
			}
		});
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
			}
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		mobileEt.setText(SharedData.getString(SharedData.MOBILE));
		
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_INFO, 0, Constant.TYPE_MINE_BINDMOBILE, Constant.SERACH_TYPE_ENTER, null);
	}

	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString("手机验证"));
		codeEt.setHint(getLanguageString("请输入验证码"));
		mobileEt.setHint(getLanguageString("请输入手机号"));
		submitBtn.setText(getLanguageString("绑定"));
	}

	/**
	 * code btn 操作
	 * 获取验证码
	 */
	private void onCodeBtnClick() {
		String mobile = mobileEt.getText().toString();
		if (StringTools.isBlank(mobile)) {
			this.toastShort(getLanguageString("请输入手机号"));
			return;
		}
		if (!StringTools.isMobile(mobile)) {
			this.toastShort(getLanguageString("请输入合法的手机号"));
			return;
		}
		codeBtn.setClickable(false);
		BaseRequestData<RegCodeParam> requestData = new BaseRequestData<RegCodeParam>(
				"registercheck");
		RegCodeParam rcp = new RegCodeParam();
		rcp.mobile = mobile;
		rcp.type = "1";
		requestData.body = rcp;
		ProgressTools.showDialog(this);
		http.post(requestData, new CallBack<CompanySearchResponse>() {
			@Override
			public void onSuccess(CompanySearchResponse entity) {
				ProgressTools.hide();
				if(entity.retcode == 200) {
					System.out.println(entity.retcode + " , " + entity.retmesg);
					codeBtn.setText(getCodeNumber(defaultNum));
					codeBtn.setBackgroundResource(R.drawable.validate_gray);
					mHandler.sendEmptyMessage(1);
				} else {
					codeBtn.setClickable(true);
					toastShort(entity.retmesg);
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				codeBtn.setClickable(true);
				LogUtils.e(msg);
				toastShort(msg);
			}
		});
	}
	/**
	 * 提交验证码
	 */
	private void onSubmitBtnClick() {
		final String mobile = mobileEt.getText().toString();
		String code = codeEt.getText().toString();
		if (StringTools.isBlank(mobile)) {
			this.toastShort(getLanguageString("请输入手机号"));
			return;
		}
		if (!StringTools.isMobile(mobile)) {
			this.toastShort(getLanguageString("请输入合法的手机号"));
			return;
		}
		if (StringTools.isBlank(code)) {
			this.toastShort(getLanguageString("请输入验证码"));
			return;
		}
		ProgressTools.showDialog(this);
		BaseRequestData<RegCodeParam> requestData = new BaseRequestData<RegCodeParam>(
				"registercheck");
		RegCodeParam rcp = new RegCodeParam();
		rcp.mobile = mobile;
		rcp.code = code;
		rcp.type = "2";
		requestData.body = rcp;
		http.post(requestData, new CallBack<CompanySearchResponse>() {
			@Override
			public void onSuccess(CompanySearchResponse entity) {
				ProgressTools.hide();
				if(entity.retcode == 200) {
					toastShort(getLanguageString("绑定手机号成功"));
					//处理绑定成功事项
					setResult(200);
					SharedData.commit("bindMobile", true);
					SharedData.commit(SharedData.MOBILE,mobile);
					RegisterCodeActivity.this.finish();
				} else {
					toastShort(entity.retmesg);
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				LogUtils.e(msg);
				toastShort(msg);
			}
		});
	}

	private String getCodeNumber(int num) {
		return getLanguageString("获取验证码") + "(" + num + ")";
	}

	// 在主线程里面处理消息并更新UI界面
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				num--;
				if(num <= 0) {
					mHandler.removeMessages(1);
					toastShort(getLanguageString("请重新获取验证码"));
					resetCodeBtn();
				} else {
					codeBtn.setText(getCodeNumber(num));
					mHandler.sendEmptyMessageDelayed(1, 1000);
				}
				break;
			default:
				break;
			}
		}
	};
	
	private void resetCodeBtn() {
		codeBtn.setText(getLanguageString("获取验证码"));
		codeBtn.setClickable(true);
		codeBtn.setBackgroundResource(R.drawable.validate_red);
		num = defaultNum;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeMessages(1);
	}
	
}
