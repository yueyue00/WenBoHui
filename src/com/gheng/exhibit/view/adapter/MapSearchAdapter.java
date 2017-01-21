package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gheng.exhibit.utils.I18NUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 * @param <T>
 */
public class MapSearchAdapter<T> extends AbstractEntityAdapter<T> {

	/**
	 * @param context
	 */
	public MapSearchAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_mapsearch, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHoder();
	}

	protected void setHolderValue(Object holder, T data) {
		ViewHoder obj = (ViewHoder) holder;
		obj.tv_name.setText((String) I18NUtils.getValue(data, "name"));
		// I18NUtils.setTextView(obj.tv_exhibit,
		// ((BaseActivity) context).getLanguageString("展位号"));
		// obj.tv_exhibit_number.setText(data.getStandreference());
	}

	class ViewHoder {

		@ViewInject(R.id.tv_name)
		TextView tv_name;
		@ViewInject(R.id.tv_exhibit)
		TextView tv_exhibit;
		@ViewInject(R.id.tv_exhibit_number)
		TextView tv_exhibit_number;
	}
}
