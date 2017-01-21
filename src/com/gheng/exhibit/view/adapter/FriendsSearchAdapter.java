package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.VipInfoListBaen.InfoBean;
import com.smartdot.wenbo.huiyi.R;

public class FriendsSearchAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<InfoBean> mlist;

	/**
	 * 构造函数
	 */
	public FriendsSearchAdapter(Context context, ArrayList<InfoBean> mlist) {
		this.context = context;
		this.mlist = mlist;
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.vipinfo_lv_item, null);
			convertView.setTag(holder);
			holder.friends_item_header_parent = (LinearLayout) convertView
					.findViewById(R.id.friends_item_header_parent);
			holder.vil_tv_name = (TextView) convertView
					.findViewById(R.id.vil_tv_name);
			holder.vil_btn_call = (Button) convertView
					.findViewById(R.id.vil_btn_call);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		holder.friends_item_header_parent.setVisibility(View.GONE);
		holder.vil_tv_name.setText(mlist.get(position).getUsername());
		holder.vil_btn_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mobile = mlist.get(position).getMobile();
				if (mobile != null && !mobile.equals("")) {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + mobile));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		LinearLayout friends_item_header_parent;
		TextView vil_tv_name;
		Button vil_btn_call;
	}
}