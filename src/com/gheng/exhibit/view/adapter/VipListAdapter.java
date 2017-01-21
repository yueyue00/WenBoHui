package com.gheng.exhibit.view.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

import com.gheng.exhibit.http.body.response.VipPersonData;
import com.gheng.exhibit.view.viplist.VIPXiangQingActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

public class VipListAdapter extends RecyclerView.Adapter<ViewHolder> {
	private List<VipPersonData> list;
	private LayoutInflater li;
	private Context context;
	private DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();

	public VipListAdapter(ArrayList<VipPersonData> list, Context context) {
		this.list = list;
		this.context = context;
		this.li = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.build();
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		View v = this.li.inflate(R.layout.item_viplist_rv, vg, false);

		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder

		mvh.name.setText(list.get(position).username);
		mvh.info.setText(list.get(position).workplace + "  "
				+ list.get(position).job);

		imageLoader.displayImage(list.get(position).photourl, mvh.iv, options);
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
		TextView name;
		TextView info;
		ImageView iv;
		View line;

		public MyViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.vip_item_name);
			info = (TextView) itemView.findViewById(R.id.vip_item_info);
			iv = (ImageView) itemView.findViewById(R.id.vip_item_iv);
			line = itemView.findViewById(R.id.viplist_lien);
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
			Intent intent = new Intent(context, VIPXiangQingActivity.class);
			// intent.putExtra("vip_id", list.get(arg2).getId());
			VipPersonData beanData = list.get(position);
			intent.putExtra("vip_bean", (Serializable) beanData);
			context.startActivity(intent);
		}
	}
}
