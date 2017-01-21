package com.gheng.exhibit.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.BaseResponse;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.request.LoginParam;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author zhao
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener{
    
	//TitleBar
	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	//显示密码
	@ViewInject(R.id.tv_password)
	private TextView tv_password;
	//显示用户名
	@ViewInject(R.id.tv_username)
	private TextView tv_username;
	//输入用户名
	@ViewInject(R.id.edt_username)
	private EditText edt_username;
	//输入密码
	@ViewInject(R.id.edt_password)
	private EditText edt_password;
	//登陆button
	@ViewInject(R.id.btn_login)
	private Button btn_login;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		btn_login.setOnClickListener(this);//创建登陆按钮的监听
		if(AppTools.isLogin()){//如何已经登陆，则直接跳入主界面
			//startTo(MapActivity.class);
			finish();
		}
		if(!SharedData.getBoolean("show")){
			showSettingDialog();
			SharedData.commit("show", true);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(!SharedData.getBoolean("copy")){
					AppTools.copyMapData(context);
					SharedData.commit("coty", true);
				}
			}
		}).start();
	}
	
	@Override
	protected void setI18nValue() {
		//中英文转换
		titleBar.setText(getLanguageString(20004));
		tv_password.setText(getLanguageString(20003));
		tv_username.setText(getLanguageString(20002));
		edt_username.setHint(getLanguageString(20001));
		edt_password.setHint(getLanguageString(20005));
		btn_login.setText(getLanguageString(20004));
	}
	
	@Override
	public void onClick(View v) {
		//登陆按钮监听方法
		switch (v.getId()) {
		case R.id.btn_login:
			login();//跳到登陆方法中
			break;

		default:
			break;
		}
	}
	
	private void login(){
		final String uname = edt_username.getText().toString();//输入用户名中英文转换
		String password = edt_password.getText().toString();//输入密码中英文转换
		if(StringTools.isBlank(uname)){
			//如何没有填写用户名,提示请输入用户名
			toastShort(getLanguageString(20047));
			return;
		}
		if(StringTools.isBlank(password)){
			//如何没有填写密码,提示请输入密码
			toastShort(getLanguageString(20048));
			return;
		}
		ProgressTools.showDialog(this);//加载数据提示  Loading
		BaseRequestData<LoginParam> requestData = new BaseRequestData<LoginParam>("login");
		LoginParam param = new LoginParam();
		param.uname = uname;
		param.p = StringTools.getMd5Str(password);
		
		requestData.body = param;
		
		http.post(requestData, new CallBack<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse entity) {
				toastShort(entity.retmesg);
				if(entity.retcode == 200){
					//验证成功，跳转到主应用页面
					SharedData.saveUser(uname);
					//startTo(MapActivity.class);
					finish();
				}
				ProgressTools.hide();//收起加载数据提示
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				//提示用户名或者密码输入错误
				ProgressTools.hide();
				toastNetError();
			}
		});
	}
	
	private void showSettingDialog(){
		Builder builder = new Builder(this);
		builder.setMessage(getLanguageString(20043));
		builder.setPositiveButton(getLanguageString(20045),new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				SharedData.commit("i18n",Language.EN);
				setI18nValue();
			}
		});
		builder.setNegativeButton(getLanguageString(20044),new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				SharedData.commit("i18n",Language.ZH);
				setI18nValue();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

}
