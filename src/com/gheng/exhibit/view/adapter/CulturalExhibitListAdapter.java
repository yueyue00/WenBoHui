package com.gheng.exhibit.view.adapter;

import java.util.List;

import cn.jpush.a.a.i;

import com.gheng.exhibit.http.body.response.CulturalExhibitListBean;
import com.gheng.exhibit.http.body.response.CulturalExhibitListBean.InfoBean;
import com.gheng.exhibit.utils.Constant;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import com.smartdot.wenbo.huiyi.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CulturalExhibitListAdapter extends BaseAdapter{
	
	private Context context;
	private List<InfoBean> data;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	

	public CulturalExhibitListAdapter(Context context,
			List<InfoBean> data) {
		
		this.context = context;
		this.data = data;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyinews_listview) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.a_huiyinews_listview)// 设置图片Uri为空或者是错误的时候显示的图片
				.showImageOnFail(R.drawable.a_huiyinews_listview)// 设置图片加载或者解码的过程发生错误显示的
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡
//				.displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();// 创建配置过的DisplayImageOptions对象
		
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
			convertView = LayoutInflater.from(context).inflate(R.layout.cultural_exhibit_list_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.ced_list_item_tv_name);
			holder.tv_location = (TextView) convertView.findViewById(R.id.ced_list_item_tv_hall);
			holder.tv_introduce = (TextView) convertView.findViewById(R.id.ced_list_item_tv_introduce);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.ced_list_item_iv_icon);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(data.get(position).getName());
		holder.tv_location.setText(data.get(position).getAddress());
		holder.tv_introduce.setText(data.get(position).getRemark());
		imageLoader.displayImage(Constant.DOMAIN+data.get(position).getPicUrl(), holder.iv_icon, options);
		return convertView;
	}
  class ViewHolder{
	  TextView tv_name,tv_introduce,tv_location;
	  ImageView iv_icon;
  }
}
