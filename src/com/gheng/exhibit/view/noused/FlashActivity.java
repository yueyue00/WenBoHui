package com.gheng.exhibit.view.noused;

import java.util.UUID;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.MapHelper;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.MainActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *	启动页面
 * @author lileixing
 */
public class FlashActivity extends BaseActivity {
	
	private boolean isFinish = false;
	
	@ViewInject(R.id.iv_flash)
	private ImageView iv_flash;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);
	}
	
	@Override
	protected void init() {
		if(!isFinish){
			String snum = SharedData.getString("snum");
			if(StringTools.isBlank(snum)){
				snum = UUID.randomUUID().toString().replace("-", "");
				SharedData.commit("snum", snum);
			}
		}
		new Thread(){
			@Override
			public void run() {
				if(!MapHelper.isExistMap() || !SharedData.getBoolean("copy", false) || SharedData.getInt("imapversion", 0) < Constant.MAP_VERSION) {
					SharedData.commit("imapversion", Constant.MAP_VERSION);
					MapHelper.copyFromAsset(FlashActivity.this);
					SharedData.commit("copy", true);
				}
			}
		}.start();
		startToMain();
	}
	
	@Override
	protected void setI18nValue() {

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			isFinish = true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
//	private Handler handler = new Handler(){、
//		@Override
//		public void handleMessage(android.os.Message msg) {
//			startToMain();
//		};
//	};
	
//	public void startAnmi1() {
//		final AlphaAnimation animation = new AlphaAnimation(0, 1); 
//		animation.setDuration(2000);//设置动画持续时间
//		iv_flash.setAnimation(animation); 
//		animation.setInterpolator(new AccelerateDecelerateInterpolator());
//		animation.setAnimationListener(new AnimationListener() {
//		    @Override
//		    public void onAnimationStart(Animation animation) {
//		    }
//		    @Override
//		    public void onAnimationRepeat(Animation animation) {
//		    }
//		    @Override
//		    public void onAnimationEnd(Animation animation) {
//		    	startAnmi2();
//		    }
//		});
//		animation.setFillAfter(true);
//		animation.startNow();
//	}
	
//	@Override
//	protected void onStart() {
//		super.onStart();
//		startAnmi1();
//	}

//	public void startAnmi2() {
//		final Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_out);
//		iv_flash.setAnimation(animation);
//		animation.setAnimationListener(new AnimationListener() {
//		    @Override
//		    public void onAnimationStart(Animation animation) {
//		    	handler.sendEmptyMessageDelayed(1, 550);
//		    }
//		    @Override
//		    public void onAnimationRepeat(Animation animation) {
//		    }
//		    @Override
//		    public void onAnimationEnd(Animation animation) {
//		    	
//		    }
//		});
//		animation.setFillAfter(true);
//		animation.startNow();
//	}

	private void startToMain(){
		if(!isFinish){
    		startTo(MainActivity.class);
    		overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  
    		context.finish();
    	}
	}
	
}
