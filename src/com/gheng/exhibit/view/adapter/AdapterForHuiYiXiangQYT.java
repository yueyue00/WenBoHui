package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.HuiYiYiTi;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForHuiYiXiangQYT extends BaseAdapter {
	private Context context;
	private ArrayList<HuiYiYiTi> list = new ArrayList<HuiYiYiTi>();

	public AdapterForHuiYiXiangQYT(Context context, ArrayList<HuiYiYiTi> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(list.get(position).yitiid);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(
				R.layout.item_huiyixiangqing_rvyiti, null);
		ViewHolder vh = new ViewHolder();
		vh.talkman = (TextView) convertView.findViewById(R.id.talk_man);
		vh.yitiname = (TextView) convertView.findViewById(R.id.yiti_name);
		vh.tv_time = (TextView) convertView.findViewById(R.id.yiti_tv_time);
		vh.talkman.setText(list.get(position).yititalkman);
		vh.yitiname.setText(list.get(position).yitiname);
		vh.tv_time.setText(list.get(position).yititime);
		return convertView;
	}

	class ViewHolder {
		TextView talkman;
		TextView yitiname;
		TextView tv_time;
	}
}
