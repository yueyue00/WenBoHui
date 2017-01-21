package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gheng.exhibit.view.support.AboutData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class MapKeywordAdapter extends AbstractEntityAdapter<AboutData> {

	public MapKeywordAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_mapkeyword, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHoder();
	}

	@Override
	protected void setHolderValue(Object holder, AboutData data) {
		ViewHoder obj = (ViewHoder)holder;
		obj.tv.setText(data.name);
	}

	class ViewHoder {
		@ViewInject(R.id.tv)
		TextView tv;
	}

}
