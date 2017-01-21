package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gheng.exhibit.view.support.AboutData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class KeywordAdapter extends AbstractEntityAdapter<AboutData> {

	public KeywordAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_keyword, null);
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, AboutData data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText(data.name);
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;
	}

}
