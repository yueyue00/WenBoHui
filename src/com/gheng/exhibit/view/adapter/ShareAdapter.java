package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.gheng.exhibit.view.support.ShareData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *
 * @author lileixing
 */
public class ShareAdapter extends AbstractEntityAdapter<ShareData> {

	/**
	 * @param context
	 */
	public ShareAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.popwindow_share_item, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, ShareData data) {
		ViewHolder obj = (ViewHolder)holder;
		obj.iv.setImageResource(data.icon);
	}
	
	class ViewHolder{
		
		@ViewInject(R.id.iv)
		ImageView iv;
		
	}
	
}
