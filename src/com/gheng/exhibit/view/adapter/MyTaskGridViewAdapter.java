package com.gheng.exhibit.view.adapter;

import java.util.List;

import com.baidu.mobstat.co;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean.InfoBean.CommTaskBean;
import com.gheng.exhibit.http.body.response.MytaskGridViewBean;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.MyTaskActivity;
import com.gheng.exhibit.view.MyTaskDriverActivity;
import com.gheng.exhibit.view.adapter.MyTaskInsideListAdapter.ViewHolder;
import com.gheng.exhibit.view.checkin.checkin.FixedTaskActivity;
import com.gheng.exhibit.view.checkin.checkin.MytaskVipListActivity;
import com.gheng.exhibit.view.checkin.checkin.SignActivity;
import com.smartdot.wenbo.huiyi.R;

import uk.co.senab.photoview.scrollerproxy.GingerScroller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyTaskGridViewAdapter extends BaseAdapter{
	 
	private Context context;
	private List<CommTaskBean> data;
	private OnClickListener listener;
	/**
	 * 构造函数
	 */
	public  MyTaskGridViewAdapter (Context context,List<CommTaskBean> data) {
		this.context = context;
		this.data = data;
	}
	/**
	 * 设置点击事件
	 * @param listener
	 */
    public void setOnClickListenerForButton(OnClickListener listener) {
		this.listener = listener;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GlideViewHolder holder = null;
		if (convertView == null) {
			holder = new GlideViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.mytask_grid_item, null);
			holder.button = (Button) convertView.findViewById(R.id.btn_myt_g);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_myt_g);
			convertView.setTag(holder);
			
		}else {
			holder = (GlideViewHolder) convertView.getTag();
			
		}
		CommTaskBean commTaskBean = data.get(position);

			
			holder.tv.setText(commTaskBean.getNAME());
		
		if (commTaskBean.getNAME().equals("接机")) {
			holder.button.setBackgroundResource(R.drawable.ic_mytask_jieji);
		}else if (commTaskBean.getNAME().equals("签到")) {
			holder.button.setBackgroundResource(R.drawable.ic_mytask_qiaobaodao);
		}else if (commTaskBean.getNAME().equals("办入住")) {
			holder.button.setBackgroundResource(R.drawable.ic_mytask_checkin);
		}else if (commTaskBean.getNAME().equals("领资料")) {
			holder.button.setBackgroundResource(R.drawable.ic_mytask_data);
		}else if (commTaskBean.getNAME().equals("看展览")) {
			holder.button.setBackgroundResource(R.drawable.ic_mytask_seeexhibit);
		}else if (commTaskBean.getNAME().equals("送机")) {
			holder.button.setBackgroundResource(R.drawable.ic_mytask_songji);
		}else if (commTaskBean.getNAME().equalsIgnoreCase("vip")) {
			holder.button.setBackgroundResource(R.drawable.ic_mytask_vip_highlight);
		}else if (commTaskBean.getNAME().equalsIgnoreCase("会场")) {
			holder.button.setBackgroundResource(R.drawable.btn_mytask_huichang_highlight);
		}else if (commTaskBean.getNAME().equalsIgnoreCase("预注册")) {
			holder.button.setBackgroundResource(R.drawable.mytask_icon_yzc_sel);
		}
		
		holder.button.setTag(position);
		final GlideViewHolder viewHolder = holder;
		final int index = position;
		final String task = commTaskBean.getNAME();
		final int taskid = commTaskBean.getID();
		final String taskUniqueCode = commTaskBean.getUNIQUE_CODE();
		holder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				if (task.equalsIgnoreCase("vip")) {
					if (Constant.SIGN == 1) {//多个vip  显示vip列表
						intent = new Intent(context,MytaskVipListActivity.class);
						intent.putExtra("title", task);
						context.startActivity(intent);
					}else if(Constant.SIGN == 0){//单个 显示vip行程
						
						intent = new Intent(context, MyTaskActivity.class);
						intent.putExtra("vipid", Constant.INVITATION_CODE);
//						intent.putExtra("title", task);//task---->"vip"
						intent.putExtra("title", Constant.VIP_NAME);//task----->"嘉宾名字"
						context.startActivity(intent);
					}else {
						Toast.makeText(context, "没有vip嘉宾", Toast.LENGTH_LONG).show();
					}
				}else if (task.equalsIgnoreCase("签到")) {
					intent  = new Intent(context,SignActivity.class);
					intent.putExtra("title", "签到");
					context.startActivity(intent);
				}else if (task.equalsIgnoreCase("会场")) {
					if (Constant.USER_JUESE.equals("61")) {//司机才可以进会场
					
					intent = new Intent(context, MyTaskDriverActivity.class);
					intent.putExtra("title", "会场");
					context.startActivity(intent);
					}else {
						Toast.makeText(context, "对不起，您没有司机权限", Toast.LENGTH_LONG).show();
					}
				}else{
					intent = new Intent(context,FixedTaskActivity.class);
					intent.putExtra("title", task);
					intent.putExtra("taskid", taskid);
					intent.putExtra("taskuniquecode", taskUniqueCode);
					context.startActivity(intent);
					
				}
				System.out.println("--------------->task-->"+task);
				
//				showCustomDialog(context, index, viewHolder);
//                 Toast.makeText(context, "点击了item="+data.get(index).getTaskName(), Toast.LENGTH_LONG).show();				
			}
		});
		
		return convertView;
	}
	/**
	 * 显示自定义dialog
	 * @param context  上下文
	 * @param position  位置
	 * @param holder  
	 */
	public void showCustomDialog(final Context context,final int position,final GlideViewHolder holder) {
		 //将布局文件转为View
		View customDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
		//对话框
		final Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.getWindow().setContentView(customDialogView);
		//确认button
		Button btn_ok = (Button) customDialogView.findViewById(R.id.dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "确认完成该任务-->"+data.get(position).getNAME(), Toast.LENGTH_LONG).show();
				holder.button.setEnabled(false);
				if (data.get(position).getNAME().equals("接机")) {
					holder.button.setBackgroundResource(R.drawable.ic_mytask_jieji_highlight);
				}else if (data.get(position).getNAME().equals("签报到")) {
					holder.button.setBackgroundResource(R.drawable.ic_mytask_qiaobaodao_highlight);
				}else if (data.get(position).getNAME().equals("办入住")) {
					holder.button.setBackgroundResource(R.drawable.ic_mytask_checkin_highlight);
				}else if (data.get(position).getNAME().equals("领资料")) {
					holder.button.setBackgroundResource(R.drawable.ic_mytask_data_hihlight);
				}else if (data.get(position).getNAME().equals("看展览")) {
					holder.button.setBackgroundResource(R.drawable.ic_mytask_seeexhibit_highlight);
				}else if (data.get(position).getNAME().equals("送机")) {
					holder.button.setBackgroundResource(R.drawable.ic_mytask_songji_highlight);
				}else if (data.get(position).getNAME().equals("vip")) {
					holder.button.setBackgroundResource(R.drawable.ic_mytask_songji_highlight);
				}
				
				dialog.dismiss();
			}
		});
		//取消Button
		Button btn_cancel = (Button) customDialogView.findViewById(R.id.dialog_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				holder.cb.setChecked(false);
				dialog.dismiss();
			}
		});
	}
	class GlideViewHolder {
		Button button;
		TextView tv;
	}
}