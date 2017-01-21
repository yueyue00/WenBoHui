package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Company;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *
 * @author lileixing
 */
public class SearchCompanyAdapter extends AbstractEntityAdapter<Company> {

	/**
	 * @param context
	 */
	public SearchCompanyAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_search_company, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, Company data) {
		ViewHolder obj = (ViewHolder)holder;
//		obj.tv_name.setText(data.getName());
//		obj.tv_en_name.setText(data.getEnName());
	}

	class ViewHolder {
		@ViewInject(R.id.tv_name)
		TextView tv_name;
		@ViewInject(R.id.tv_en_name)
		TextView tv_en_name;
	}
	
}
