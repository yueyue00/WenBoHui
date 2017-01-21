package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class ProductInfoCompanyAdapter extends AbstractEntityAdapter<Company> {

	/**
	 * @param context
	 */
	public ProductInfoCompanyAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_company_product, null);
		v.setLayoutParams(new ListView.LayoutParams(-1, context.getResources()
				.getDimensionPixelSize(R.dimen.pchi_item_height)));
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, Company data) {
		ViewHolder obj = (ViewHolder) holder;
		
		
		bitmapUtils.display(obj.iv_logo, AppTools.imageChange(data.getLogo()));
		if(this.getCount() == 1) {
			obj.tv_dj.setText("(" + ((BaseActivity)context).getLanguageString("独家") + ")");
		} else {
			obj.tv.setText("");
		}
		obj.tv.setText((String)I18NUtils.getValue(data, "name"));
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;
		@ViewInject(R.id.iv_logo)
		ImageView iv_logo;
		@ViewInject(R.id.tv_dj)
		TextView tv_dj;
	}

}
