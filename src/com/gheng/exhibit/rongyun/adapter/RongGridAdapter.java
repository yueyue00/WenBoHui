package com.gheng.exhibit.rongyun.adapter;

import io.rong.imkit.RongIM;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.GroupInfoBean;
import com.gheng.exhibit.http.body.response.RongGroup.RenyuanBean;
import com.gheng.exhibit.rongyun.activity.GroupInfoActivity;
import com.gheng.exhibit.rongyun.contact.ContactActivity;
import com.smartdot.wenbo.huiyi.R;

/**
 * 群组详情的群成员适配器
 */
public class RongGridAdapter extends MyBaseAdapter {
	private Context mContext;

	public RongGridAdapter(Context mContext) {
		super(mContext);
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 加载或复用item界面
		ViewHolder holder = null;
		// 如果没有可重用的item界面，则加载一个新的item
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.zrong_grid_item, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv_item);// 姓名
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv_item2);// 部门
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_item);// 头像
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// zyj修改 去掉群组拉人通道
		// if (position < itemList.size()) {
		holder.tv2.setVisibility(View.GONE);
//		GroupInfoBean bean = (GroupInfoBean) itemList.get(position);
		RenyuanBean mbean = (RenyuanBean) itemList.get(position);
		holder.tv.setVisibility(View.VISIBLE);
//		holder.tv.setText(bean.truename);
		holder.tv.setText(mbean.truename);
		holder.iv.setImageResource(R.drawable.a_huiyiricheng_guesticon);
		// } else {
		// holder.tv.setVisibility(View.GONE);
		// holder.tv2.setVisibility(View.GONE);
		// holder.iv.setImageResource(R.drawable.addpicturebutton);
		// }

		// imageLoaderUtils.showHead(imgs[position], holder.iv);// 现在没有头像一律用默认头像
		// String imageURL = String.format(GlobalConfig.SHOW_USER_IMAGE,
		// bean.id);
		// imageLoaderUtils.showHead(imageURL, holder.iv);
		// holder.tv.setText(img_text[position]); // 测试用 一堆名字

		// 点击头像即可进行单人聊天  zyj修改去掉进入聊天通道
//		holder.iv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// zyj修改 去掉群组拉人通道
//				// if (position < itemList.size()) {
//				GroupInfoBean bean = (GroupInfoBean) itemList.get(position);
//				if (RongIM.getInstance() != null) {
//					RongIM.getInstance().startPrivateChat(context,
//							bean.USER_ID, bean.truename);
//				}
//				// } else {
//				// String groupid = ((GroupInfoActivity) context).groupId;
//				// String groupname = ((GroupInfoActivity) context).groupName;
//				// Intent intent = new Intent(context, ContactActivity.class);
//				// intent.putExtra("groupId", groupid);
//				// intent.putExtra("groupName", groupname);
//				// context.startActivity(intent);
//				// }
//			}
//		});

		return convertView;
	}

	class ViewHolder {
		private TextView tv;
		private TextView tv2;
		private ImageView iv;
	}
}
