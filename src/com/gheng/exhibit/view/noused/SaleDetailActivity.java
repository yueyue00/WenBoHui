package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.BaseResponse;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.request.CompanyEditParam;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.DipUtils;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 售出的详情页面
 * 
 * @author lileixing
 */
public class SaleDetailActivity extends BaseActivity
{

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.edt_company_shortname)
	private EditText edt_company_shortname;

	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;

	@ViewInject(R.id.tv_name)
	private TextView tv_name;

	@ViewInject(R.id.tv_enname)
	private TextView tv_enname;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_detail);
		setValue();
		initWindow();
	}

	@Override
	protected void setI18nValue()
	{
		I18NUtils.setTextView(tv_submit, getLanguageString(20042));
	}

	private void initWindow()
	{
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		// p.height = (int) (d.getHeight() -
		// ProgressTools.getStatusHeight(this)); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() - DipUtils.dip2px(this, 10)); // 宽度设置为屏幕的0.8
		getWindow().setAttributes(p);
	}

	private void saveToServer()
	{
		final String name = edt_company_shortname.getText().toString();

		ProgressTools.showDialog(this);
		BaseRequestData<CompanyEditParam> requestData = new BaseRequestData<CompanyEditParam>("companyedit");
		CompanyEditParam body = new CompanyEditParam();
		requestData.body = body;
		body.companyid = getIntent().getExtras().getLong("id") + "";
		switch (SharedData.getInt("i18n",Language.ZH))
		{
		case Language.ZH:
			body.name = name;
			break;
		default:
			body.enname = name;
			break;
		}

		body.type = "2";
		http.post(requestData, new CallBack<BaseResponse>()
		{

			@Override
			public void onSuccess(BaseResponse entity)
			{
				ProgressTools.hide();
//				toastShort(entity.retmesg);
				if (entity.retcode == 200)
				{
					setResult(101);
					finish();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				ProgressTools.hide();
				toastNetError();
			}
		});
	}

	private void setValue()
	{
		titleBar.setText(getLanguageString(20039));
		I18NUtils.setTextView(tv_submit, getLanguageString(20042));
		switch (SharedData.getInt("i18n",Language.ZH))
		{
		case Language.ZH:
			I18NUtils.setTextView(edt_company_shortname, getIntent().getExtras().getString("name"), getLanguageString(20055));
			break;
		default:
			I18NUtils.setTextView(edt_company_shortname, getIntent().getExtras().getString("name"), getLanguageString(20056));
			break;
		}
		I18NUtils.setTextView(tv_name, getLanguageString(20040));
		I18NUtils.setTextView(tv_enname, getLanguageString(20041));

		tv_submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				saveToServer();
			}
		});
		titleBar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void clickRightImage()
			{

			}

			@Override
			public void clickLeftImage()
			{
				finish();
			}
		});
	}

}
