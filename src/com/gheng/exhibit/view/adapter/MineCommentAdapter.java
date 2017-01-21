package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.MineCommentData;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class MineCommentAdapter extends AbstractEntityAdapter<MineCommentData> {

	public MineCommentAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		View v = inflater.inflate(R.layout.item_mine_comment, null);
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
	protected void setHolderValue(Object holder, MineCommentData data) {
		BaseActivity a = (BaseActivity)context;
		ViewHolder obj = (ViewHolder) holder;
		obj.tvObjname.setText(data.objname);
		obj.tvContent.setText(data.content);
		if(data.state == 1) {
			obj.tvState.setText(a.getLanguageString("发布中"));
		} else if(data.state == 2) {
			obj.tvState.setText(a.getLanguageString("已发布"));
		} else if(data.state == 3) {
			obj.tvState.setText(a.getLanguageString("未通过"));
		}
		obj.tvTime.setText(data.createtime);
		obj.ratingBar.setRating(data.score);
		
	}
	class ViewHolder {
		@ViewInject(R.id.tv_objname)
		TextView tvObjname;

		@ViewInject(R.id.ratingBar)
		RatingBar ratingBar;
		
		@ViewInject(R.id.tv_time)
		TextView tvTime;
		
		@ViewInject(R.id.tv_content)
		TextView tvContent;
		
		@ViewInject(R.id.tv_state)
		TextView tvState;
	}

}
