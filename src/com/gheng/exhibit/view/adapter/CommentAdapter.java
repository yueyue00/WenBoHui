package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.CommentData;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *
 * @author lileixing
 */
public class CommentAdapter extends AbstractEntityAdapter<CommentData> {

	/**
	 * @param context
	 */
	public CommentAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_comment, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, CommentData data) {
		ViewHolder obj = (ViewHolder) holder;
		if(data.showname == 0){
			obj.tv_uname.setText(((BaseActivity)context).getLanguageString("匿名用户"));
		}else{
			int lg = SharedData.getInt("i18n",Language.ZH);
			if(lg == Language.ZH){
				obj.tv_uname.setText(data.surname+data.name);
			}else{
				obj.tv_uname.setText(data.name+" "+data.surname);
			}
		}
		obj.tv_time.setText(data.createtime);
		obj.tv_content.setText(data.content);
		obj.ratingBar.setRating(data.score);
		
	}

	class ViewHolder{
		@ViewInject(R.id.tv_uname)
		TextView tv_uname;
		@ViewInject(R.id.ratingBar)
		RatingBar ratingBar;
		@ViewInject(R.id.tv_time)
		TextView tv_time;
		@ViewInject(R.id.tv_content)
		TextView tv_content;
		
	}

}
