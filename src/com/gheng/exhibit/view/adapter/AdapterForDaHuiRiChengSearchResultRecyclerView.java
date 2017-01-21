package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.DaHuiInfo_forsearch;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.checkin.HuiYiXiangQingActivity;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForDaHuiRiChengSearchResultRecyclerView extends
		RecyclerView.Adapter<ViewHolder> {

	Context cont;
	ArrayList<DaHuiInfo_forsearch> search_result;
	LayoutInflater lf;

	public AdapterForDaHuiRiChengSearchResultRecyclerView(Context cont,
			ArrayList<DaHuiInfo_forsearch> search_result) {
		this.cont = cont;
		this.search_result = search_result;
		this.lf = LayoutInflater.from(cont);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return search_result.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewtype) {
		// TODO Auto-generated method stub
		View v = this.lf.inflate(R.layout.item_searchdahuiresultrv, vg, false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new itemclickListener(position));
		MyViewHolder mvh = (MyViewHolder) vh;
		mvh.room.setText(BaseActivity.getLanguageString("会场") + ":"
				+ search_result.get(position).meettingroom);
		mvh.talkman.setText(BaseActivity.getLanguageString("主持人") + ":"
				+ search_result.get(position).meettingtalkman);
		mvh.time.setText(BaseActivity.getLanguageString("时间") + ":"
				+ search_result.get(position).meettingtime);
		mvh.title.setText(search_result.get(position).meettingtitle);
		if (position % 3 == 0) {
			mvh.logo.setImageDrawable(cont.getResources().getDrawable(
					R.drawable.wuzhen_mineroute_date1));
		}
		if (position % 3 == 1) {
			mvh.logo.setImageDrawable(cont.getResources().getDrawable(
					R.drawable.wuzhen_mineroute_date2));
		}
		if (position % 3 == 2) {
			mvh.logo.setImageDrawable(cont.getResources().getDrawable(
					R.drawable.wuzhen_mineroute_date3));
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView title;// 主题
		TextView time;// 时间安排
		TextView talkman;// 主持人
		TextView room;// 会议室
		ImageView logo;

		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			title = (TextView) itemView.findViewById(R.id.title);
			time = (TextView) itemView.findViewById(R.id.time);
			talkman = (TextView) itemView.findViewById(R.id.talkman);
			room = (TextView) itemView.findViewById(R.id.room);
			logo = (ImageView) itemView.findViewById(R.id.logo);
		}
	}

	class itemclickListener implements OnClickListener {

		public int position;

		public itemclickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.putExtra("pchi_id", search_result.get(position).meettingid);
			i.setClass(cont, HuiYiXiangQingActivity.class);
			cont.startActivity(i);
		}
	}
}
