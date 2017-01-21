package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.RestaurantListData;
import com.gheng.exhibit.utils.AppTools;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lenovo
 * 
 */
public class RestaurantAdapter extends AbstractEntityAdapter<RestaurantListData>
{

	/**
	 * @param context
	 */
	public RestaurantAdapter(Context context)
	{
		super(context);
	}

	@Override
	protected View makeView(int position)
	{
		return inflater.inflate(R.layout.item_restaurant, null);
	}

	@Override
	protected Object makeHolder()
	{
		return new ViewHoder();
	}

	@Override
	protected void setHolderValue(Object holder, RestaurantListData data)
	{
		ViewHoder obj = (ViewHoder) holder;
		obj.tv_name.setText(data.name);
		bitmapUtils.display(obj.iv, AppTools.imageChange(data.logo));
	}

	class ViewHoder
	{
		@ViewInject(R.id.iv)
		ImageView iv;
		@ViewInject(R.id.tv_name)
		TextView tv_name;
	}
}
