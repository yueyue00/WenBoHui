package com.gheng.exhibit.rongyun.contact;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.ContactBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

public class AdapterContactHor extends RecyclerView.Adapter<ViewHolder> {

	private List<ContactBean> list;
	private Context context;
	private LayoutInflater li;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options_liebbiao;

	public AdapterContactHor(List<ContactBean> list, Context context) {
		this.list = list;
		this.context = context;
		li = LayoutInflater.from(context);
		options_liebbiao = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(200)).build();
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int arg1) {
		View view = this.li
				.inflate(R.layout.zyjitem_dept_horizontal, vg, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;
		mvh.nametv.setText(list.get(position).name);
		// imageLoader.displayImage(list.get(position).guestphotourl,
		// mvh.guest_icon, options_liebbiao);
	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView right;
		TextView nametv;

		public MyViewHolder(View itemview) {
			super(itemview);
			right = (TextView) itemview.findViewById(R.id.right);
			nametv = (TextView) itemview.findViewById(R.id.nametv);
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
			String pid = list.get(position).pid;
			String id = list.get(position).id;
			String sign = "2";
			((ContactActivity) context).LoadData(pid, id, sign);
		}
	}
}
