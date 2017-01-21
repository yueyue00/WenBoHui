package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.CompanyType;
import com.gheng.exhibit.utils.I18NUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class CompanyTypeAdapter extends AbstractEntityAdapter<CompanyType> {

	/**
	 * @param context
	 */
	public CompanyTypeAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_about, null);
		v.setLayoutParams(new ListView.LayoutParams(-1, context.getResources()
				.getDimensionPixelSize(R.dimen.pchi_item_height)));
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, CompanyType data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText((String)I18NUtils.getValue(data, "name"));
		obj.tv_new.setVisibility(View.VISIBLE);
		obj.tv_new.setText(data.getTypecount() + "");
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;
		@ViewInject(R.id.tv_new)
		TextView tv_new;
	}

}
