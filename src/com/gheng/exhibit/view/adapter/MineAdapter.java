package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.support.PchiData;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class MineAdapter extends AbstractEntityAdapter<PchiData> {

	/**
	 * @param context
	 */
	public MineAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_mine, null);
		v.setLayoutParams(new ListView.LayoutParams(-1, context.getResources().getDimensionPixelSize(R.dimen.pchi_item_height)));
		if (position % 2 == 0) {
			v.setBackgroundColor(Color.WHITE);
		} else {
			int color = context.getResources().getColor(R.color.btn_gray);
			v.setBackgroundColor(color);
		}
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, PchiData data) {
		BaseActivity a = (BaseActivity)context;
		ViewHolder obj = (ViewHolder) holder;
		obj.tv_mobible.setText(data.name);
		obj.iv.setImageResource(data.icon);
		obj.tv.setVisibility(View.GONE);
		
		if(data.mode == 0){
			obj.iv_right.setVisibility(View.INVISIBLE);
		}else if(data.mode == 1){
			obj.tv.setVisibility(View.VISIBLE);
			if(SharedData.getBoolean("bindMobile",false)){
				obj.iv_right.setVisibility(View.INVISIBLE);
				obj.tv.setTextColor(a.getResources().getColor(R.color.common_gray_font));
				obj.tv.setText(a.getLanguageString("已绑定"));
				obj.iv_right.setVisibility(View.INVISIBLE);
			}else{
				obj.tv.setTextColor(a.getResources().getColor(R.color.title_font));
				obj.tv.setText(a.getLanguageString("未绑定"));
				obj.iv_right.setVisibility(View.VISIBLE);
			}
		}else{
			obj.iv_right.setVisibility(View.VISIBLE);
		}
	}

	class ViewHolder {
		@ViewInject(R.id.tv)
		TextView tv;

		@ViewInject(R.id.tv_mobile)
		TextView tv_mobible;
		
		@ViewInject(R.id.iv)
		ImageView iv;
		@ViewInject(R.id.iv_right)
		ImageView iv_right;
	}

}
