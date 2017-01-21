package com.gheng.exhibit.view.adapter;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.a.a.r;

import com.gheng.exhibit.http.body.response.Driver_Vip;
import com.gheng.exhibit.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DriverListviewAdapter extends BaseAdapter{

	private Context context;
	private List<Driver_Vip> data;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private List<String> selectedList = new ArrayList<>();
	private OnCheckedChangeListener listener;
	
	
	
	public OnCheckedChangeListener getListener() {
		return listener;
	}
	public void setListener(OnCheckedChangeListener listener) {
		this.listener = listener;
	}
	public DriverListviewAdapter(Context context, List<Driver_Vip> data) {
		super();
		this.context = context;
		this.data = data;
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.a_huiyiricheng_guesticon)
		.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
        .showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .bitmapConfig(Bitmap.Config.RGB_565) 
        .displayer(new CircleBitmapDisplayer())
		.build();
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.driver_task_vip_info, null);
			holder.container_ll = (LinearLayout) convertView
					.findViewById(R.id.driver_task_container_ll);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.container_ll.removeAllViews();

		final Driver_Vip driver_Vip = data.get(position);
		View view = LayoutInflater.from(context).inflate(
				R.layout.driver_listview_item_item, null);
		ImageView iv_headView = (ImageView) view
				.findViewById(R.id.driver_task_ivIcon);
		TextView tv_name = (TextView) view
				.findViewById(R.id.driver_task_tvName);
		TextView tv_job = (TextView) view.findViewById(R.id.driver_task_tvJob);
		ImageView iv_call = (ImageView) view
				.findViewById(R.id.driver_task_ivCall);
		ImageView iv_sendinfo = (ImageView) view.findViewById(R.id.driver_task_ivSendInfo);
		TextView tv_phone = (TextView) view
				.findViewById(R.id.driver_task_tvPhoneNum);
		CheckBox cb = (CheckBox) view.findViewById(R.id.driver_task_checkbox);
        
		 imageLoader.displayImage(driver_Vip.getIconPath(), iv_headView,
		 options);
		tv_name.setText(driver_Vip.getName());
		tv_job.setText(driver_Vip.getJob());
		tv_phone.setText(driver_Vip.getCpMobile());// 随行人员电话
		tv_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + driver_Vip.getMobile()));// 点击名字拨打嘉宾电话
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		iv_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + driver_Vip.getCpMobile()));// 点击名字拨打嘉宾电话
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		iv_sendinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * 跳转融云单聊界面
				 */
				if (driver_Vip.getId() != null
						&& RongIM.getInstance() != null) {
					try {
						RongIM.getInstance().startPrivateChat(context,
								driver_Vip.getId(),
								driver_Vip.getName());
					} catch (Exception e) {
					}
				}
			}
		});
		
		if (driver_Vip.isCheck()) {
			cb.setChecked(true);
		}else {
			cb.setChecked(false);
		}
		selectedList.clear();
		cb.setTag(position);
		cb.setOnCheckedChangeListener(listener);
//		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if (isChecked) {
//					driver_Vip.setCheck(true);
//					selectedList.add(driver_Vip.getName());
//				
//				}else {
//					driver_Vip.setCheck(false);
//					selectedList.remove(driver_Vip.getName());
//				}
//				
//				notifyDataSetChanged();
//			}
//		});
		holder.container_ll.addView(view);
        
		return convertView;
	}
	
	class ViewHolder{
	LinearLayout container_ll;
	}
	
}
