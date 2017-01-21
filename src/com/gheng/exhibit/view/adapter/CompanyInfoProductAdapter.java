package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.I18NUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class CompanyInfoProductAdapter extends AbstractEntityAdapter<Product> {

	/**
	 * @param context
	 */
	public CompanyInfoProductAdapter(Context context) {
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
	protected void setHolderValue(Object holder, Product data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText((String)I18NUtils.getValue(data, "name"));
		bitmapUtils.display(obj.iv_logo, AppTools.imageChange(data.getLogo()));
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;
		@ViewInject(R.id.iv_logo)
		ImageView iv_logo;
	}

}
