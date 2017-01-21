package com.gheng.exhibit.view.checkin;

import android.os.Bundle;
import android.widget.TextView;

import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 注册协议
 * @author zhaofangfang
 *
 */
public class RegisterNoteActivity extends BaseActivity implements OnClickListener {
	
	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	@ViewInject(R.id.tv_note)
	private TextView tvNote;
	@ViewInject(R.id.tv_next)
	private TextView tvNext;

	@Override
	protected void setI18nValue() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_note);
	}
	
	protected void init() {
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_INFO, 0, Constant.TYPE_REGISTER_AGREENMENT, Constant.SERACH_TYPE_ENTER, null);
		titleBar.setOnClickListener(this);
		titleBar.setText(getLanguageString("注册服务协议"));
		tvNext.setText(getLanguageString("接受并继续"));
//		tvNext.setOnClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				Bundle bd = new Bundle();
//				startTo(RegisterActivity.class, bd);
//			}
//		});
		tvNote.setText(this.getResources().getString(R.string.register_note_cn));
//		if(SharedData.getInt("i18n") == Language.ZH) {
//		} else {
//			tvNote.setText(this.getResources().getString(R.string.register_note_en));
//		}
	}
	
	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {
	}
}
