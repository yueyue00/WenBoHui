package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gheng.exhibit.http.BaseRequestData;
import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.body.request.HotelParam;
import com.gheng.exhibit.http.response.HotelListResponse;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.HotelAdapter;
import com.gheng.exhibit.widget.CustomListView;
import com.gheng.exhibit.widget.TitleBar;
import com.gheng.exhibit.widget.TitleBar.OnClickListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 酒店列表
 * 
 * @author lileixing
 */
public class HotelListActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;

	@ViewInject(R.id.lv)
	private CustomListView lv;

	//联系人
	@ViewInject(R.id.contact_lable)
	private TextView contactLable;
	@ViewInject(R.id.contact_value)
	private TextView contactValue;
	
	//电话
	@ViewInject(R.id.phone_lable)
	private TextView phoneLable;
	@ViewInject(R.id.phone_value)
	private TextView phoneValue;

	//手机
	@ViewInject(R.id.mobile_lable)
	private TextView mobile_label;
	@ViewInject(R.id.mobile_value)
	private TextView mobile_value;
	
	//传真
	@ViewInject(R.id.fax_lable)
	private TextView fax_label;
	@ViewInject(R.id.fax_value)
	private TextView fax_value;
	
	//邮箱
	@ViewInject(R.id.email_lable)
	private TextView email_label;
	@ViewInject(R.id.email_value)
	private TextView email_value;
	
	//QQ
	@ViewInject(R.id.qq_lable)
	private TextView qqLable;
	@ViewInject(R.id.qq_value)
	private TextView qqValue;

	//地址
	@ViewInject(R.id.address_lable)
	private TextView addressLable;
	@ViewInject(R.id.address_value)
	private TextView addressValue;
	
	//邮编
	@ViewInject(R.id.postal_lable)
	private TextView postal_lable;
	@ViewInject(R.id.postal_value)
	private TextView postal_value;

	@ViewInject(R.id.tv_remark)
	private TextView tv_remark;
	
	private ViewHolder hoder1 = new ViewHolder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotel_list);
	}

	@Override
	protected void setI18nValue() {
		contactLable.setText(getLanguageString("联系人") + "：");
		phoneLable.setText(getLanguageString("电话") + "：");
		mobile_label.setText(getLanguageString("手机") + "：");
		fax_label.setText(getLanguageString("传真") + "：");
		email_label.setText(getLanguageString("邮箱") + "：");
		qqLable.setText("QQ：");
		addressLable.setText(getLanguageString("地址") + "：");
		postal_lable.setText(getLanguageString("邮编") + "：");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void init() {
		titleBar.setText(getIntent().getStringExtra("title"));

		hoder1.lv = lv;
		hoder1.adapter = new HotelAdapter(this);
		hoder1.lv.setAdapter(hoder1.adapter);

		titleBar.setOnClickListener(this);
		
		if (hoder1.pageno == -1) {
			hoder1.pageno = 1;
			loadData(false);
		}
		ApiUtil.postBrowseLog(Constant.BROWSE_TYPE_LIST, 0, Constant.TYPE_RESTAURANT, Constant.SERACH_TYPE_ENTER, null);
	}

	@Override
	public void clickLeftImage() {
		finish();
	}

	@Override
	public void clickRightImage() {
	}

	class ViewHolder {
		CustomListView lv;
		HotelAdapter adapter;

		int pageno = -1;
	}

	private void loadData(boolean more) {
		BaseRequestData<HotelParam> requestData = new BaseRequestData<HotelParam>(
				"hotellist");
		HotelParam param = new HotelParam();
		param.eid = SharedData.getBatchId()+"";
		requestData.body = param;
		ProgressTools.showDialog(this);
		http.post(requestData, new CallBack<HotelListResponse>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				ProgressTools.hide();
				toastNetError();
			}

			@Override
			public void onSuccess(HotelListResponse entity) {
				ProgressTools.hide();
				//联系人
				contactValue.setText(entity.body.linkname);
				if(StringTools.isBlank(contactValue.getText().toString())){
					((View)contactValue.getParent()).setVisibility(View.GONE);
				}
				//电话
				phoneValue.setText(entity.body.linktel);
				if(StringTools.isBlank(phoneValue.getText().toString())){
					((View)phoneValue.getParent()).setVisibility(View.GONE);
				}
				//手机
				mobile_value.setText(entity.body.mobile);
				if(StringTools.isBlank(mobile_value.getText().toString())){
					((View)mobile_value.getParent()).setVisibility(View.GONE);
				}
				//传真
				if(StringTools.isBlank(entity.body.fax)){
					((View)fax_value.getParent()).setVisibility(View.GONE);
				}else{
					fax_value.setText(entity.body.fax);
				}
				//邮箱
				if(StringTools.isBlank(entity.body.email)){
					((View)email_value.getParent()).setVisibility(View.GONE);
				}else{
					email_value.setText(entity.body.email.replace(",", "\n"));
				}
				//QQ
				qqValue.setText(entity.body.qq);
				if(StringTools.isBlank(qqValue.getText().toString())){
					((View)qqValue.getParent()).setVisibility(View.GONE);
				}
				//地址
				addressValue.setText(entity.body.linkaddress);
				if(StringTools.isBlank(addressValue.getText().toString())){
					((View)addressValue.getParent()).setVisibility(View.GONE);
				}
				//邮编
				postal_value.setText(entity.body.postal);
				if(StringTools.isBlank(postal_value.getText().toString())){
					((View)postal_value.getParent()).setVisibility(View.GONE);
				}
				
				if(SharedData.getInt("i18n", Language.ZH) == Language.EN){
					((View)tv_remark.getParent()).setVisibility(View.VISIBLE);
					tv_remark.setText("Burnaby Solutions China");
				}
				
				hoder1.adapter.setData(entity.body.rdata);
			}
		});
	}
}
