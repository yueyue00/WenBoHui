package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Speakers;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.I18NUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *
 * @author lileixing
 */
public class SpeakerAdapter extends AbstractEntityAdapter<Speakers> {

	/**
	 * @param context
	 */
	public SpeakerAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_speaker, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, Speakers data) {
		ViewHolder obj = (ViewHolder)holder;
		bitmapUtils.display(obj.iv, AppTools.imageChange(data.getLogo()));
		obj.tv_name.setText((String)I18NUtils.getValue(data, "name"));
		obj.tv_office.setText((String)I18NUtils.getValue(data, "office"));
		obj.tv_workplace.setText((String)I18NUtils.getValue(data, "workplace"));
	}
	
	class ViewHolder{
		@ViewInject(R.id.iv)
		ImageView iv;
		@ViewInject(R.id.tv_name)
		TextView tv_name;
		@ViewInject(R.id.tv_office)
		TextView tv_office;
		@ViewInject(R.id.tv_workplace)
		TextView tv_workplace;
	}

}
