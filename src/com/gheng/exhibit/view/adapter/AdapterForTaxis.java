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

import com.gheng.exhibit.http.body.response.TaxiInfos;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.view.checkin.checkin.TaxisActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForTaxis extends RecyclerView.Adapter<ViewHolder> {

	public ArrayList<TaxiInfos> list;// 消息提醒数据集合
	public Context cont;
	public LayoutInflater lf;
	User user;

	public AdapterForTaxis(Context cont, ArrayList<TaxiInfos> list) {
		this.cont = cont;
		this.list = list;
		this.lf = LayoutInflater.from(cont);
		// 查找
		try {
			DbUtils db = DbUtils.create(cont);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	// 这个方法返回item的view 不必考虑复用问题
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		View v = this.lf.inflate(R.layout.item_recyclerview_taxi, vg, false);
		return new MyViewHolder(v);
	}

	@Override
	// 这个方法用来实现数据和item的捆绑
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
		mvh.username.setText(list.get(position).short_name);
		mvh.username_en.setText(list.get(position).enshortname);
		mvh.car_number.setText(list.get(position).car_number);
		mvh.car_name.setText(list.get(position).name);
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

		ImageView taxi_usericon;// 司机头像
		TextView username; // 司机姓名
		TextView username_en;// 司机英文名
		TextView car_number;
		TextView car_name;
		View line;

		public MyViewHolder(View itemView) {
			super(itemView);
			taxi_usericon = (ImageView) itemView
					.findViewById(R.id.taxi_usericon);
			username = (TextView) itemView.findViewById(R.id.username);
			username_en = (TextView) itemView.findViewById(R.id.username_en);
			line = itemView.findViewById(R.id.xiaoxi_mainpage_line);
			car_number = (TextView) itemView.findViewById(R.id.car_number);
			car_name = (TextView) itemView.findViewById(R.id.car_name);
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
			((TaxisActivity) cont).CallTaxi(list.get(position).invitationCode);
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ list.get(position).tel));
			cont.startActivity(intent);
		}
	}
}
