package com.gheng.exhibit.view.adapter;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.ScheduleInfoSupportData;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.noused.CommentListActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class ScheduleInfoAdapter2 extends ScheduleInfoAdapter{

	/**
	 * @param context
	 */
	public ScheduleInfoAdapter2(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		int layout = R.layout.item_schedule_info2;
		if(getItemViewType(position) == 1){
			layout = R.layout.item_schedule_host;
		}
		View v = inflater.inflate(layout, null);
//		if(position % 2 == 1){
//			v.setBackgroundColor(context.getResources().getColor(R.color.agray));
//		}else{
//			v.setBackgroundColor(Color.WHITE);
//		}
		return v;
	}

	@Override
	protected Object makeHolder() {
		return new ViewHolder();
	}

	@Override
	protected void setHolderValue(Object holder, ScheduleInfoSupportData data) {
		BaseActivity a = (BaseActivity) context;
		ViewHolder obj = (ViewHolder) holder;
		obj.name.setVisibility(View.VISIBLE);
		
		if(data.type == 1){ //主持人
			obj.name.setText(data.name);
		}else{
//			obj.arrow.setVisibility(View.VISIBLE);
			
			obj.title.setText(data.name);
			
			if(StringTools.isBlank(data.speaker)){
				obj.name_row.setVisibility(View.GONE);
//				obj.arrow.setVisibility(View.INVISIBLE);
			}else{
				obj.name_row.setVisibility(View.VISIBLE);
				obj.name_label.setText(a.getLanguageString("演讲人")+": ");
				obj.name.setText(data.speaker);
			}
			
			if(StringTools.isBlank(data.time)){
				obj.time.setVisibility(View.GONE);
			}else{
				obj.time.setVisibility(View.VISIBLE);
				obj.time_label.setText("");
				obj.time.setText(data.time);
			}
			
			obj.tv_comment_count.setText(data.commentcount+"");
			
			obj.ll_to_comment_list.setOnClickListener(new OnClickImpl(data, 3));
			
			
			if(StringTools.isBlank(data.companyname)){
				obj.workplace_row.setVisibility(View.GONE);
			}else{
				obj.workplace_row.setVisibility(View.VISIBLE);
				obj.workplace_label.setText(a.getLanguageString("工作单位")+": ");
				obj.workplace.setText(data.companyname);
			}
			obj.tv_praise_count.setText(data.praisecount + "");
			obj.tv_fav_list.setText(a.getLanguageString("收藏"));

			if(data.isfav == 1) {
				obj.iv_fav_list.setImageResource(R.drawable.fav2);
			} else {
				obj.iv_fav_list.setImageResource(R.drawable.fav);
			}
			if(data.ispraise == 1) {
				obj.iv_praise_count.setImageResource(R.drawable.btn_zan2);
			} else {
				obj.iv_praise_count.setImageResource(R.drawable.btn_zan);
			}
			obj.tv_fav_list.setOnClickListener(new OnClickImpl(data, 1));
			obj.iv_fav_list.setOnClickListener(new OnClickImpl(data, 1));
			//
			obj.iv_praise_count.setOnClickListener(new OnClickImpl(data, 2));
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return getItem(position).type;
	}

	class OnClickImpl implements OnClickListener{

		ScheduleInfoSupportData data;
		int type;//1收藏   2点赞  3评论
		
		public OnClickImpl(ScheduleInfoSupportData data, int type) {
			this.data = data;
			this.type = type;
		}
		
		@Override
		public void onClick(View v) {
			if(type == 3) {
				Bundle bd = new Bundle();
				bd.putInt("type", Constant.TYPE_SCHEDULE_INFO);
				bd.putSerializable("model",data);
				((BaseActivity)context).startTo(CommentListActivity.class,bd);
			} else if(type == 2) {
				postPraise(data);
			} else if(type == 1) {
				postFav(data);
			}
		}
		
	}
	/**
	 * 收藏操作
	 * @param infoData
	 */
	private void postFav(final ScheduleInfoSupportData infoData) {
		if (!AppTools.isLogin()) {
//			UIUtils.startToRegister((BaseActivity) context);
			return;
		}
		ProgressTools.showDialog(context);
		int mode = 0;
		if (infoData.isfav == 0) {
			mode = 1;
		} else {
			mode = 0;
		}
		ApiUtil.postFav(infoData.getId(), Constant.TYPE_SCHEDULE_INFO, mode, new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
			}

			@Override
			public void callback(boolean success, long id, int type, int mode, Object data) {
				ProgressTools.hide();
				String[] strs = { "取消收藏成功", "收藏成功" };
				String message = ((BaseActivity) context).getLanguageString(strs[mode]);
				if (success) {
					((BaseActivity) context).toastShort(message);
					if (mode == 0) {
						infoData.isfav = 0;
					} else {
						infoData.isfav = 1;
					}
					notifyDataSetChanged();
				} else {
					((BaseActivity) context).toastNetError();
				}
			}
		});
	}
	
	// 点赞和取消赞
	private void postPraise(final ScheduleInfoSupportData infoData) {
		ProgressTools.showDialog(context);
		int mode = 0;
		if (infoData.ispraise == 0) {
			mode = 1;
		} else {
			mode = 0;
		}
		ApiUtil.postZan(infoData.getId(), Constant.TYPE_SCHEDULE_INFO, mode, new ApiCallBack() {
			@Override
			public void callback(Map<TimeRecordType, Integer> map) {
			}

			@Override
			public void callback(boolean success, long id, int type, int mode, Object data) {
				ProgressTools.hide();
				String[] strs = { "取消赞成功", "点赞成功" };
				String message = ((BaseActivity) context).getLanguageString(strs[mode]);
				int count = infoData.praisecount;
				if (success) {
					((BaseActivity) context).toastShort(message);
					if (mode == 0) {
						infoData.ispraise = 0;
						count = count > 0 ? count - 1 : 0;
					} else {
						count ++;
						infoData.ispraise = 1;
					}
					infoData.praisecount = count;
					notifyDataSetChanged();
				} else {
					((BaseActivity) context).toastNetError();
				}
			}
		});
	}
	
	class ViewHolder {
		@ViewInject(R.id.title)
		TextView title;
		@ViewInject(R.id.title_row)
		View title_row;
		
		@ViewInject(R.id.name)	//演讲者名称
		TextView name;
		@ViewInject(R.id.name_row)	//演讲者名称
		View name_row;
		@ViewInject(R.id.name_label)	//演讲者名称
		TextView name_label;
		
		@ViewInject(R.id.time)
		TextView time;
		
		@ViewInject(R.id.time_label)
		TextView time_label;
		
//		@ViewInject(R.id.arrow)
//		ImageView arrow;
		@ViewInject(R.id.workplace_label)
		TextView workplace_label;
		@ViewInject(R.id.workplace)
		TextView workplace;
		@ViewInject(R.id.workplace_row)
		View workplace_row;
		
		@ViewInject(R.id.ll_to_comment_list)
		private View ll_to_comment_list;
		
		@ViewInject(R.id.tv_comment_count)
		private TextView tv_comment_count;
		
		@ViewInject(R.id.iv_praise_count)
		private ImageView iv_praise_count;
		
		@ViewInject(R.id.tv_praise_count)
		private TextView tv_praise_count;
		
		@ViewInject(R.id.iv_fav_list)
		private ImageView iv_fav_list;
		
		@ViewInject(R.id.tv_fav_list)
		private TextView tv_fav_list;
	}

}
