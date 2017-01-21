package com.gheng.exhibit.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartdot.wenbo.huiyi.R;

public class MyGridAdapter extends BaseAdapter {
	private AQuery aq2;
	private Context context;
	private ArrayList<String> titleStringArray;
	private int click = 0;

	public MyGridAdapter(Context context, ArrayList<String> mtitleStringArray) {
		this.context = context;
		aq2 = new AQuery(context);
		this.titleStringArray = mtitleStringArray;
	}

	@Override
	public int getCount() {
		return titleStringArray.size();
	}

	@Override
	public Object getItem(int position) {
		return titleStringArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View
					.inflate(context, R.layout.a_news_tilteitem, null);
		}
		TextView name = ViewHolderz.get(convertView, R.id.tv_titlename);
		View dot = ViewHolderz.get(convertView, R.id.gridview_dot);
		if (position == click) {
			name.setTextColor(context.getResources().getColor(
					R.color.text_click));
			dot.setVisibility(View.VISIBLE);
		} else {
			name.setTextColor(Color.parseColor("#8C8D8D"));
			dot.setVisibility(View.INVISIBLE);
		}
		name.setText(titleStringArray.get(position));
		return convertView;
	}

	public void refresh(int x) {
		click = x;
		notifyDataSetChanged();
	}
}