package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.FuWuZu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class AdapterJiaBin extends RecyclerView.Adapter<ViewHolder> {
	private ArrayList<FuWuZu> list = new ArrayList<FuWuZu>();
	private LayoutInflater li;
	private Context context;

	public AdapterJiaBin(ArrayList<FuWuZu> list, Context context) {
		this.list = list;
		this.context = context;
		this.li = LayoutInflater.from(context);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		View v = this.li.inflate(R.layout.item_recycleview_zhanshangsj, vg,
				false);

		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
		if (position % 3 == 0) {
			mvh.iv_zhanshang
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao1);
		} else if (position % 3 == 1) {
			mvh.iv_zhanshang
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao2);
		} else if (position % 3 == 2) {
			mvh.iv_zhanshang
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao3);
		}
		mvh.tv_zhanshang.setText(list.get(position).name);
		mvh.arrow.setImageResource(R.drawable.wuzhen_viptelephone);
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
		@ViewInject(R.id.iv_zhanshang)
		ImageView iv_zhanshang;
		@ViewInject(R.id.tv_zhanshang)
		TextView tv_zhanshang;
		@ViewInject(R.id.arrow)
		ImageView arrow;
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
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ list.get(position).tell));
			context.startActivity(intent);
		}
	}
}
