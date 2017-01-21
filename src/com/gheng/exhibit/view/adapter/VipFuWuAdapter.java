package com.gheng.exhibit.view.adapter;

import android.annotation.SuppressLint;
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
public class VipFuWuAdapter extends AbstractEntityAdapter<PchiData> {

	/**
	 * @param context
	 */
	public VipFuWuAdapter(Context context) {
		super(context);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_pchi, null);

		// if (position == 0 || position == 1 ||position == 3) {
		// v.findViewById(R.id.view_pchi).setVisibility(View.VISIBLE);
		// } else {
		// v.findViewById(R.id.view_pchi).setVisibility(View.GONE);
		// }
		// if (position == 1) {
		// v.findViewById(R.id.view_pchixia).setVisibility(View.GONE);
		// }else{
		// v.findViewById(R.id.view_pchixia).setVisibility(View.VISIBLE);
		// }
		// if (position == 2) {
		// v.findViewById(R.id.view_fengexian).setVisibility(View.GONE);
		// } else {
		// v.findViewById(R.id.view_fengexian).setVisibility(View.VISIBLE);
		// }
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, PchiData data) {
		ViewHolder obj = (ViewHolder) holder;
		obj.tv.setText(data.name);
		obj.iv.setImageResource(data.icon);
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;

		@ViewInject(R.id.iv)
		ImageView iv;

		@ViewInject(R.id.view_pchixia)
		View view_pchixia;

	}

}
