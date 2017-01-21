package com.gheng.exhibit.view.adapter;

import java.util.List;

import cn.jpush.a.a.r;

import com.gheng.exhibit.http.body.response.CommonVipBeanForFixedTask;
import com.gheng.exhibit.http.body.response.FixedTaskCommonVipBean.InfoBean;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class FixedTaskAdapter extends BaseAdapter{
    
	private Context context;
	private List<InfoBean> data;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = null;
	private CompoundButton.OnCheckedChangeListener checkedListener;
	
	public FixedTaskAdapter(Context context,
			List<InfoBean> data) {
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

	
	public void setCheckedListener(
			CompoundButton.OnCheckedChangeListener checkedListener) {
		this.checkedListener = checkedListener;
	}

	@Override
	public int getCount() {
		if (data !=null) {
			return data.size();
		}else {
			return 0;
		}
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
			convertView = LayoutInflater.from(context).inflate(R.layout.ft_listview_item_container, null);
			holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ft_listview_item_container);
//			holder.iv_icon = (ImageView) convertView.findViewById(R.id.ft_item_iv_icon);
//			holder.tv_name = (TextView) convertView.findViewById(R.id.ft_item_tv_name);
//			holder.cb = (CheckBox) convertView.findViewById(R.id.ft_item_cb);
//			holder.cb.setOnCheckedChangeListener(checkedListener);//、将监听事件传回到对应的activity中
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		  holder.ll_container.removeAllViews();
		  InfoBean jbListBean = data.get(position);
		  View view = LayoutInflater.from(context).inflate(R.layout.ft_listview_item, null);
		  ImageView iv_icon = (ImageView) view.findViewById(R.id.ft_item_iv_icon);
		  TextView tv_name = (TextView) view.findViewById(R.id.ft_item_tv_name);
		  CheckBox cb = (CheckBox) view.findViewById(R.id.ft_item_cb);
		  imageLoader.displayImage(jbListBean.getSmall_icon(), iv_icon, options);
		  tv_name.setText(jbListBean.getName());
		  if (jbListBean.isCheckState()) {
			cb.setChecked(true);
		  }else {
			cb.setChecked(false);
		  }
//		  if (task.isHaveSelected()) {//通过haveSelected这个字段来判断复选框的状态
//			cb.setChecked(true);
//			cb.setEnabled(false);
//			Constant.STATEFORSELECTALL = true;
//			}else {
//				cb.setEnabled(true);
//			}
		  cb.setTag(position);
		  cb.setOnCheckedChangeListener(checkedListener);//将监听事件传回到对应的activity中
		  holder.ll_container.addView(view);
		
//		imageLoader.displayImage(data.get(position).getImgPath(), holder.iv_icon, options);
//		holder.tv_name.setText(data.get(position).getName());
//		holder.cb.setTag(position);
//		if(data.get(position).isCheckState()){
//			holder.cb.setChecked(true);
//			holder.cb.setEnabled(false);
//		}else {
//			holder.cb.setChecked(false);
//			holder.cb.setEnabled(true);
//		}
		
		return convertView;
	}
  class ViewHolder{

  LinearLayout ll_container;
  }
}
