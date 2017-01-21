package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gheng.exhibit.view.map.DingWeiDaoHangActivity;
import com.gheng.indoormap.result.FloorResult;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForChoseFloorPopWindowRv extends
		RecyclerView.Adapter<ViewHolder> {

	Context cont;
	ArrayList<FloorResult> floors;
	LayoutInflater lf;

	public AdapterForChoseFloorPopWindowRv(Context cont,
			ArrayList<FloorResult> floors) {
		this.cont = cont;
		this.floors = floors;
		this.lf = LayoutInflater.from(cont);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return floors.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		View v = this.lf
				.inflate(R.layout.item_chosefloorpopwindowrv, vg, false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new ItemClickListener(position));
		MyViewHolder mvh = (MyViewHolder) vh;
		mvh.floorindex.setText(floors.get(position).floorIndex + "F");
		if (floors.get(position).floorIndex == ((DingWeiDaoHangActivity) cont).openfloornum) {
			mvh.floorindex.setTextColor(cont.getResources().getColor(
					R.color.chosefloor));
		} else {
			mvh.floorindex.setTextColor(cont.getResources().getColor(
					R.color.white));
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView floorindex;

		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			floorindex = (TextView) itemView.findViewById(R.id.floorindextv);
		}
	}

	class ItemClickListener implements OnClickListener {

		int position;

		public ItemClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {// 点击某一item，切换楼层
			// TODO Auto-generated method stub
			((DingWeiDaoHangActivity) cont).changeFloorWithPois(floors
					.get(position).floorIndex);
			notifyDataSetChanged();
		}
	}
}
