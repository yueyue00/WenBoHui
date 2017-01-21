package com.gheng.exhibit.view.adapter;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *
 * @author lileixing
 */
public class CompanyAdapter extends AbstractEntityAdapter<Company> {

	private boolean isMine = false;
	
	/**
	 * @param context
	 */
	public CompanyAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_company, null);
	}
	
	@Override
	protected Object makeHolder() {
		return new ViewHoder();
	}

	@Override
	protected void setHolderValue(Object holder, Company data) {
		ViewHoder obj = (ViewHoder)holder;
		obj.tv_name.setText((String)I18NUtils.getValue(data, "name"));
		I18NUtils.setTextView(obj.tv_exhibit, ((BaseActivity)context).getLanguageString("展位"));
		obj.tv_exhibit_number.setText(data.getStandreferenceNew());
		if(data.getIsfav() == 1){
			obj.iv_fav.setImageResource(R.drawable.fav2);
		}else{
			obj.iv_fav.setImageResource(R.drawable.fav);
		}
		obj.tv_fav.setText(((BaseActivity)context).getLanguageString("收藏"));
		obj.iv_fav.setOnClickListener(new OnClickImpl(data));
		obj.tv_fav.setOnClickListener(new OnClickImpl(data));
		bitmapUtils.display(obj.iv, AppTools.imageChange(data.getLogo()));
	}
	
	class ViewHoder{
		
		@ViewInject(R.id.iv)
		ImageView iv;
		@ViewInject(R.id.tv_name)
		TextView tv_name;
		@ViewInject(R.id.tv_exhibit)
		TextView tv_exhibit;
		@ViewInject(R.id.tv_exhibit_number)
		TextView  tv_exhibit_number;
		
		@ViewInject(R.id.iv_fav)
		ImageView iv_fav;
		@ViewInject(R.id.tv_fav)
		private TextView tv_fav;
	}
	
	/**
	 * @param isMine the isMine to set
	 */
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	class OnClickImpl implements OnClickListener{
		
		private Company company;
		
		private OnClickImpl(Company company){
			this.company = company;
		}

		@Override
		public void onClick(View v) {
			if(!AppTools.isLogin()){
//				UIUtils.startToRegister((BaseActivity)context);
				return;
			}
			int mode = 0;
			if(company.getIsfav() == 0){
				mode = 1;
			}else{
				mode = 0;
			}
			ProgressTools.showDialog(context);
			ApiUtil.postFav(company.getId(), Constant.TYPE_COMPANY, mode, new ApiCallBack() {
				@Override
				public void callback(Map<TimeRecordType, Integer> map) {
				}
				@Override
				public void callback(boolean success, long id, int type,
						int mode, Object data) {
					ProgressTools.hide();
					String[] strs = {"取消收藏成功","收藏成功"};
					String message = ((BaseActivity)context).getLanguageString(strs[mode]);
					if(success){
						((BaseActivity)context).toastShort(message);
						company.setIsfav(mode);
						if(isMine && mode == 0){
							remove(company);
						}
						notifyDataSetChanged();
					}else{
						((BaseActivity)context).toastNetError();
					}
				}
			});
		}
		
	}
	
}
