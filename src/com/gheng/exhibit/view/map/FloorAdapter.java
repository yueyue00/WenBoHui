package com.gheng.exhibit.view.map;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gheng.indoormap.result.FloorResult;
import com.smartdot.wenbo.huiyi.R;

/**
 * @author cff
 * @version 创建时间 2014-9-9 上午9:36:36
 * 
 */
public class FloorAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	ViewHolder holder = null;
	Context context;
	List<FloorResult> floors;
	int n = 0;
	int c;

	public FloorAdapter(Context context, List<FloorResult> floors) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.floors = floors;
		c = context.getResources().getColor(R.color.floor_selected);
	}

	public void setData(int type) {
		n = type;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return floors.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_floor, null);
			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == n) {
			convertView.setBackgroundColor(c);
		} else {
			convertView.setBackgroundDrawable(null);
		}
		holder.tv.setText(floors.get(position).floorName);
		return convertView;
	}

	private class ViewHolder {
		TextView tv;
	}

}
