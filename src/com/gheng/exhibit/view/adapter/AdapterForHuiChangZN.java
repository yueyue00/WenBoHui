package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.HuiChangMap;
import com.gheng.exhibit.view.checkin.checkin.ShowDaHuiDianPingPhotosActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForHuiChangZN extends RecyclerView.Adapter<ViewHolder> {

	private ArrayList<HuiChangMap> list = new ArrayList<HuiChangMap>();
	private LayoutInflater li;
	private Context context;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options_huichangmap;

	public AdapterForHuiChangZN(ArrayList<HuiChangMap> list, Context context) {
		this.list = list;
		this.context = context;
		this.li = LayoutInflater.from(context);
		options_huichangmap = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyinews_viewpager)
				.showImageForEmptyUri(R.drawable.a_huiyinews_viewpager)
				.showImageOnFail(R.drawable.a_huiyinews_viewpager)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		View v = this.li.inflate(R.layout.item_recycleview_huichangzhinan, vg,
				false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
		mvh.tv_mapname.setText(list.get(position).time + " "
				+ list.get(position).name);
		mvh.tv_location.setText(list.get(position).position);
		imageLoader.displayImage(list.get(position).imgurl, mvh.huichang_ic,
				options_huichangmap);
		if (position == list.size() - 1) {
			mvh.view_line.setVisibility(View.GONE);
		}
	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	/**
	 * 开发者需要自己创建ViewHolder类
	 * 并定义item中控件对象，并完成初始化，不同的item（比如header或footer，需要开发者另行自定义ViewHolder）
	 */

	class MyViewHolder extends RecyclerView.ViewHolder {
		@ViewInject(R.id.tv_mapname)
		TextView tv_mapname;
		@ViewInject(R.id.huichang_ic)
		ImageView huichang_ic;
		@ViewInject(R.id.tv_location)
		TextView tv_location;
		@ViewInject(R.id.view_line)
		View view_line;

		public MyViewHolder(View itemView) {
			super(itemView);
			ViewUtils.inject(this, itemView);
		}
	}

	// item点击事件
	class itemClickLinstener implements OnClickListener {

		public int position;

		public itemClickLinstener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// 获取图片集合数据，启动photoview 展示图集
			Intent i = new Intent();
			i.setClass(context, ShowDaHuiDianPingPhotosActivity.class);
			i.putExtra("position", position);
			i.putExtra("maplist", list);
			context.startActivity(i);
		}
	}
}
