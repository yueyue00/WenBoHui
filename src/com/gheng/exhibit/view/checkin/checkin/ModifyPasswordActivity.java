package com.gheng.exhibit.view.checkin.checkin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.hebg3.mxy.utils.AsyncTaskForModifyPassword;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class ModifyPasswordActivity extends BaseActivity implements OnClickListener{

	@ViewInject(R.id.goback)
	ImageButton goback;
	@ViewInject(R.id.titletv)
	TextView titletv;
	@ViewInject(R.id.ok)
	Button ok;
	
	@ViewInject(R.id.newpasswordtv)
	TextView newpasswordtv;
	@ViewInject(R.id.newpasswordet)
	EditText newpasswordet;
	@ViewInject(R.id.oldpasswordtv)
	TextView oldpasswordtv;
	@ViewInject(R.id.oldpasswordet)
	EditText oldpasswordet;
	@ViewInject(R.id.confirmpasswordtv)
	TextView confirmpasswordtv;
	@ViewInject(R.id.confirmpasswordet)
	EditText confirmpasswordet;
	
	User user;
	private Boolean isActive = true;
	
	String userid="";
	String oldpass="";//用户当前密码
	
	ProgressDialog pd;
	
	Handler h=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			if (pd != null) {
				pd.dismiss();
			}
			
			if(msg.what==200){//成功 跳转登录界面
				Toast.makeText(ModifyPasswordActivity.this, getLanguageString("密码修改成功请重新登录"), Toast.LENGTH_SHORT).show();
				setResult(1);
				finish();
			}
			if(msg.what==300){//原密码错误
				Toast.makeText(ModifyPasswordActivity.this, getLanguageString("原密码不正确"), Toast.LENGTH_SHORT).show();
			}
			if(msg.what==400){//请求失败
				Toast.makeText(ModifyPasswordActivity.this, getLanguageString("请求失败"), Toast.LENGTH_SHORT).show();
			}
			if(msg.what==500){//cookie超时
				BaseActivity.gotoLoginPage(ModifyPasswordActivity.this);
			}
			if(msg.what==-1000){//网络不给力
				Toast.makeText(ModifyPasswordActivity.this, getLanguageString("网络不给力"), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.add(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modifypassword);
		registerBoradcastReceiver();//注册广播
		
		goback.setOnClickListener(this);
		ok.setOnClickListener(this);
		
		// 查找
		try {
			DbUtils db = DbUtils.create(context);
			user = db.findFirst(Selector.from(User.class).where("id", "=","1"));
			oldpass=user.getPassword();
			userid=Constant.decode(Constant.key,user.getUserId());
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		titletv.setText(getLanguageString("修改密码"));
		newpasswordtv.setText(getLanguageString("新密码")+" : ");
		oldpasswordtv.setText(getLanguageString("原密码")+" : ");
		confirmpasswordtv.setText(getLanguageString("确认密码")+" : ");
		ok.setText(getLanguageString("确定"));
		
		//以下：将旧密码和新密码以及确认密码的textview控件调整成一样宽
		LayoutParams lp=confirmpasswordtv.getLayoutParams();
		
		if (SharedData.getInt("i18n") == 1) {
			lp.width=dip2px(this,70);
		} else {
			lp.width=dip2px(this,130);
		}
		confirmpasswordtv.setLayoutParams(lp);
		oldpasswordtv.setLayoutParams(lp);
		newpasswordtv.setLayoutParams(lp);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==goback){
			this.finish();
		}
		if(v==ok){
			
			//判断流程：1.三个都填，2.原密码正确 3.密码长度不能小于6位 4 新密码两次一致
			if(oldpasswordet.getText().toString().trim().equals("")||newpasswordet.getText().toString().trim().equals("")||confirmpasswordet.getText().toString().trim().equals("")){
				Toast.makeText(this, getLanguageString("请填写完整"), Toast.LENGTH_SHORT).show();
				return;
			}
			if(!oldpass.equals(Constant.getMD5(oldpasswordet.getText().toString().trim()))){
				Toast.makeText(this, getLanguageString("原密码不正确"), Toast.LENGTH_SHORT).show();
				return;
			}
			if(newpasswordet.getText().toString().trim().length()<6){
				Toast.makeText(this, getLanguageString("新密码不能少于6位"), Toast.LENGTH_SHORT).show();
				return;
			}
			if(!newpasswordet.getText().toString().trim().equals(confirmpasswordet.getText().toString().trim())){
				Toast.makeText(this, getLanguageString("两次新密码必须一致"), Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!IsWebCanBeUse.isWebCanBeUse(this)){
				Toast.makeText(ModifyPasswordActivity.this, getLanguageString("网络不给力"), Toast.LENGTH_SHORT).show();
				return;
			}
			
			pd = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			
			//以上都通过，调用接口修改密码
			AsyncTaskForModifyPassword at=new AsyncTaskForModifyPassword(h.obtainMessage(),userid,oldpass,newpasswordet.getText().toString().trim(),getApplicationContext());
			at.execute(1);
		}
	}

	/** 记录前台切后台时间 */
	private Long TimeStart;
	/** 记录后台切前台时间 */
	private Long TimeEnd;
	/**
	 * 接收广播
	 **/
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
		    if(Intent.ACTION_SCREEN_OFF.equals(action)){
				if (BaseActivity.isAppOnForeground(context)) {
					isActive = false;
					TimeStart = System.currentTimeMillis() / 1000;
				}
			}
		}
	};
	
	
	
	/**
	 * 注册广播
	 **/
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);	
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	
	/**
	 * 唤醒时调用的方法
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!isActive) {
			TimeEnd = System.currentTimeMillis() / 1000;
			Integer time = getResources().getInteger(R.integer.time);
			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
				BaseActivity.loginValidation(context);
			}
		}
	}
	
	/**
	 * 挂起时调用的方法
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (!BaseActivity.isAppOnForeground(this)) {
			isActive = false;
			TimeStart = System.currentTimeMillis() / 1000;
		}
	}
	
	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if(mBroadcastReceiver!=null){
			this.unregisterReceiver(mBroadcastReceiver);
		}
		MyApplication.remove(this);
		super.onDestroy();
	}
}
