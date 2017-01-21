package com.gheng.exhibit.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.FuWuZu;
import com.gheng.exhibit.view.BaseActivity;
import com.smartdot.wenbo.huiyi.R;

public class GridViewAdapterForHuJiaoFW extends BaseAdapter {

	private Context context;
	private List<FuWuZu> data;

	/**
	 * 构造函数
	 */
	public GridViewAdapterForHuJiaoFW(Context context, List<FuWuZu> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridViewHolder holder = null;
		if (convertView == null) {
			holder = new GridViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_gv_hujiaofw, null);
			convertView.setTag(holder);
			holder.textview = (TextView) convertView
					.findViewById(R.id.textview);
			holder.hujiaofw_icon = (ImageView) convertView
					.findViewById(R.id.hujiaofw_icon);
		} else {
			holder = (GridViewHolder) convertView.getTag();

		}
		switch (data.get(position).name) {
		case "找出租":
			holder.hujiaofw_icon
					.setBackgroundResource(R.drawable.btn_callservice_findtaxi);
			holder.textview.setText(BaseActivity.getLanguageString("找出租"));
			break;
		case "会务总机":
			holder.hujiaofw_icon
					.setBackgroundResource(R.drawable.btn_callservice_huiwuzongji);
			holder.textview.setText(BaseActivity.getLanguageString("会务总机"));
			break;
		case "医护中心":
			holder.hujiaofw_icon
					.setBackgroundResource(R.drawable.btn_callservice_hospitalcenter);
			holder.textview.setText(BaseActivity.getLanguageString("医护中心"));
			break;
		case "安保中心":
			holder.hujiaofw_icon
					.setBackgroundResource(R.drawable.btn_callservice_securitycenter);
			holder.textview.setText(BaseActivity.getLanguageString("安保中心"));
			break;
		case "车辆中心":
			holder.hujiaofw_icon
					.setBackgroundResource(R.drawable.btn_callservice_carcenter);
			holder.textview.setText(BaseActivity.getLanguageString("车辆中心"));
			break;
		case "酒店管理中心":
			holder.hujiaofw_icon
					.setBackgroundResource(R.drawable.btn_callservice_hotelcenter);
			holder.textview.setText(BaseActivity.getLanguageString("酒店中心"));
			break;
		default:
			break;
		}
		return convertView;
	}

	class GridViewHolder {
		TextView textview;
		ImageView hujiaofw_icon;
	}
}