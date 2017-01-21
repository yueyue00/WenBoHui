package com.gheng.exhibit.view.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.HuiYiYiTi;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForHuiYiXiangQYiTi extends RecyclerView.Adapter<ViewHolder> {

	private List<HuiYiYiTi> list;
	private Context context;
	private LayoutInflater li;

	public AdapterForHuiYiXiangQYiTi(List<HuiYiYiTi> list, Context context) {
		this.list = list;
		this.context = context;
		this.li = LayoutInflater.from(context);
	}

	@Override
	public int getItemCount() {
		System.out.println("aaa:AdapterForHuiYiXiangQYiTi:getItemCount:"
				+ list.size());
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int arg1) {
		View view = this.li.inflate(R.layout.item_huiyixiangqing_rvyiti, vg,
				false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		MyViewHolder mvh = (MyViewHolder) vh;
		mvh.talkman.setText(list.get(position).yititalkman);
		mvh.yitiname.setText(list.get(position).yitiname);
	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView talkman;
		TextView yitiname;

		public MyViewHolder(View itemview) {
			super(itemview);
			talkman = (TextView) itemview.findViewById(R.id.talk_man);
			yitiname = (TextView) itemview.findViewById(R.id.yiti_name);
		}

	}
}
