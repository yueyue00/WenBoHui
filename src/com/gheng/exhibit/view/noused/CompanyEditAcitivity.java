package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.BaseResponse;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.request.CompanyEditParam;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *	展商编辑页面
 * @author lileixing
 */
public class CompanyEditAcitivity extends BaseActivity {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	@ViewInject(R.id.edt_company_name)
	private EditText edt_company_name;
	
	@ViewInject(R.id.edt_company_enname)
	private EditText edt_company_enname;
	
	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;
	
	@ViewInject(R.id.tv_name_remark)
	private TextView tv_name;
	
	@ViewInject(R.id.tv_enname_remark)
	private TextView tv_enname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_edit);
		tv_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveToServer();
			}
		});
		titleBar.setOnClickListener(new OnClickListener() {
			@Override
			public void clickRightImage() {
			
			}
			
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
	}
	
	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString(20039));
		I18NUtils.setTextView(tv_submit, getLanguageString(20042));
		I18NUtils.setTextView(edt_company_name, null,getLanguageString(20055));
		I18NUtils.setTextView(edt_company_enname, null,getLanguageString(20056));
		I18NUtils.setTextView(tv_name, getLanguageString(20040));
		I18NUtils.setTextView(tv_enname, getLanguageString(20041));
	}
	
	private void saveToServer(){
		String name = edt_company_name.getText().toString();
		if(StringTools.isBlank(name) && StringTools.isBlank(edt_company_enname.getText().toString())){
			toastShort(getLanguageString(20055));
			return;
		}
		String ename = edt_company_enname.getText().toString();
		
		ProgressTools.showDialog(this);
		BaseRequestData<CompanyEditParam> requestData = new BaseRequestData<CompanyEditParam>("companyedit");
		CompanyEditParam body = new CompanyEditParam();
		requestData.body = body;
		body.companyid = "";
		body.enname = ename;
		body.name = name;
		http.post(requestData, new CallBack<BaseResponse>() {

			@Override
			public void onSuccess(BaseResponse entity) {
				ProgressTools.hide();
//				toastShort(entity.retmesg);
				if(entity.retcode == 200){
					finish();
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
