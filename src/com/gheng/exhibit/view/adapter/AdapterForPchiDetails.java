package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hebg3.mxy.utils.GuestInFoPojo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的关注的会议详情页面中嘉宾RecyclerView显示内容
 * 
 * @author renzhihua
 */

public class AdapterForPchiDetails extends RecyclerView.Adapter<ViewHolder> {

	public ArrayList<GuestInFoPojo> list;// 消息提醒数据集合
	public Context cont;
	public LayoutInflater lf;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public AdapterForPchiDetails(Context cont, ArrayList<GuestInFoPojo> list) {
		this.cont = cont;
		this.list = list;
		this.lf = LayoutInflater.from(cont);

		options = new DisplayImageOptions.Builder()
				// .showStubImage(R.drawable.ic_launcher)
				// .showImageForEmptyUri(defaultImageId)
				// .showImageOnLoading(R.drawable.qiyouxi_page_progress)
				.showImageOnLoading(R.drawable.a_huiyiricheng_guesticon)
				// 加载时显示选择进度条
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				// 加载失败时显示缺省头像
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(false)
				.cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(180))
				.resetViewBeforeLoading(true).build();
	}

	@Override
	public int getItemCount() {

		return list.size();
	}

	@Override
	// 这个方法返回item的view 不必考虑复用问题
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {

		View v = this.lf.inflate(R.layout.item_pchi_details_listrecyclerview,
				vg, false);
		return new MyViewHolder(v);
	}

	@Override
	// 这个方法用来实现数据和item的捆绑
	public void onBindViewHolder(ViewHolder vh, int position) {

		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
		// 为控件赋值
		if (list.get(position).guesttype == 0) {
			mvh.guestphoto.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.a_huiyiricheng_guesticon));
			imageLoader.displayImage("assets://testphoto.png", mvh.guestphoto,
					options);// 圆形图片 如果要加载网络图片，将第一个参数改为实际的url地址即可
			mvh.guestname.setText("张宇");
			mvh.guestpost.setText("互联网事业部首席技术官张宇互联网事业部首席技术官张宇");
		}

	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {

		return super.getItemViewType(position);
	}

	/**
	 * 开发者需要自己创建ViewHolder类
	 * 并定义item中控件对象，并完成初始化，不同的item（比如header或footer，需要开发者另行自定义ViewHolder）
	 */
	class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView guestphoto;// 嘉宾头像
		TextView guestname;// 嘉宾名字
		TextView guestpost; // 嘉宾职位

		public MyViewHolder(View itemView) {
			super(itemView);
			guestphoto = (ImageView) itemView.findViewById(R.id.guestphoto);
			guestname = (TextView) itemView.findViewById(R.id.guest_name);
			guestpost = (TextView) itemView.findViewById(R.id.guest_post);

		}
	}

	/**
	 * item点击事件
	 */
	class itemClickLinstener implements OnClickListener {

		public int position;

		public itemClickLinstener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {

		}
	}
}
