package com.gheng.exhibit.view.adapter;

import java.util.List;

import com.gheng.exhibit.http.body.response.VipBeanForVipList;
import com.gheng.exhibit.http.body.response.VipBeanForVipList.InfoBean.VipJiabinBean;
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
import android.widget.ImageView;
import android.widget.TextView;

public class MyTaskVipListAdapter extends BaseAdapter{
    private Context context;
    private List<VipJiabinBean> data;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = null;
   
	public MyTaskVipListAdapter(Context context, List<VipJiabinBean> data) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.mytask_viplist_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.mtvl_tv_name);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.mtvl_iv_icon);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(data.get(position).getSmall_icon(), holder.iv_icon, options);
		holder.tv_name.setText(data.get(position).getName());
		
		return convertView;
	}
  class ViewHolder{
	  TextView tv_name;
	  ImageView iv_icon;
  }
}
