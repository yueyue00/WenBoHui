package com.gheng.exhibit.view.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.HuiYiJiaBin;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForHuiYiXiangQJiaBin extends
		RecyclerView.Adapter<ViewHolder> {

	private List<HuiYiJiaBin> list;
	private Context context;
	private LayoutInflater li;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options_liebbiao;

	public AdapterForHuiYiXiangQJiaBin(List<HuiYiJiaBin> list, Context context) {
		this.list = list;
		this.context = context;
		li = LayoutInflater.from(context);
		options_liebbiao = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(200)).build();
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int arg1) {
		View view = this.li.inflate(R.layout.item_huiyixiangqing_rvjiabin, vg,
				false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		MyViewHolder mvh = (MyViewHolder) vh;
		mvh.guest_name.setText(list.get(position).guestname);
//		if(list.get(position).guestphotourl!=null&&!list.get(position).guestphotourl.equals("")){
//			mvh.guest_icon.setPadding(4,4,4,4);
//		}	
		imageLoader.displayImage(list.get(position).guestphotourl,
				mvh.guest_icon, options_liebbiao);
	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView guest_icon;
		TextView guest_name;

		public MyViewHolder(View itemview) {
			super(itemview);
			guest_icon = (ImageView) itemview.findViewById(R.id.guest_icon);
			guest_name = (TextView) itemview.findViewById(R.id.guest_name);
		}

	}
}
