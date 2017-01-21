package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.view.support.PchiData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *
 * @author lileixing
 */
public class MainGridAdapter extends AbstractEntityAdapter<PchiData>{

	/**
	 * @param context
	 */
	public MainGridAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_main_grid, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, PchiData data) {
		ViewHolder obj = (ViewHolder)holder;
		obj.iv.setImageResource(data.icon);
		obj.tv.setText(data.name);
	}
	
	class ViewHolder{
		
		@ViewInject(R.id.tv)
		TextView tv;
		@ViewInject(R.id.iv)
		ImageView iv;
	}

}
