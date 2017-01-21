package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.widget.ViewHolderz;
import com.smartdot.wenbo.huiyi.R;

public class AdapterDaoHang extends BaseAdapter {

	private static ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Context context;
	static {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("name", "第五会议厅");
		map1.put("Id", R.drawable.ic_launcher);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("name", "第一会议室");
		map2.put("Id", R.drawable.ic_launcher);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("name", "会议室");
		map3.put("Id", R.drawable.ic_launcher);
		list.add(map1);
		list.add(map2);
		list.add(map3);
	}

	public AdapterDaoHang(Context context) {
		this.context = context;
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_daohang, null);

		ImageView image = ViewHolderz.get(convertView, R.id.image);
		TextView name = ViewHolderz.get(convertView, R.id.map_name);
		image.setImageResource((Integer) list.get(position).get("Id"));
		name.setText(list.get(position).get("name").toString());
		return convertView;
	}

}
