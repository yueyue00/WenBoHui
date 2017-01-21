package com.gheng.exhibit.view.adapter;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Schedule;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.DateTools;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.UIUtils;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *	主页列表
 * @author lileixing
 */
public class MainListAdapter extends AbstractEntityAdapter<Schedule>{

	/**
	 * @param context
	 */
	public MainListAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_main_list, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, Schedule data) {
		ViewHolder obj = (ViewHolder)holder;
		BaseActivity a = (BaseActivity) context;
		obj.iv.setImageResource(data.icon);
		Date parse = DateTools.parse(data.getDate(), "yyyy-MM-dd");
		String formatNoYear = DateTools.formatNoYear(parse);
		String[] split = data.getTime().split(" ");
		String timeTxt = split[split.length - 1];
		
		obj.tv_time.setText(formatNoYear+" "+timeTxt);

		obj.tv_name.setText((String)I18NUtils.getValue(data, "name"));
		if(data.getIssign() == 0){
			obj.tv_baoming.setTextColor(context.getResources().getColor(R.color.lansebaoming));
			obj.tv_baoming.setText(a.getLanguageString("查看详情") +">");
			obj.tv_baoming.setOnClickListener(new OnClickImpl(data));
		}else{
			obj.tv_baoming.setText(a.getLanguageString("查看详情") +">");
			obj.tv_baoming.setTextColor(context.getResources().getColor(R.color.lansebaoming));
			obj.tv_baoming.setOnClickListener(null);
		}
	
	}

	class ViewHolder{
		@ViewInject(R.id.iv)
		ImageView iv;
		@ViewInject(R.id.tv_name)
		TextView tv_name; 
		@ViewInject(R.id.tv_time)
		TextView tv_time; 
		@ViewInject(R.id.tv_baoming)
		TextView tv_baoming; 
	}
	
	class OnClickImpl implements View.OnClickListener{
		
		Schedule schedule;
		
		OnClickImpl(Schedule schedule){
			this.schedule = schedule;
		}
		
		@Override
		public void onClick(View v) {
			if(!AppTools.isLogin()){
//				UIUtils.startToRegister((BaseActivity)context);
				return;
			}
			UIUtils.goSignUpForResult((BaseActivity)context,schedule.getId());
		}
		
	}
	
}
