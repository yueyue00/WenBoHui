package com.gheng.exhibit.view.adapter;

import io.rong.imkit.RongIM;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.ContactBean;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForContactSearchResult extends
		RecyclerView.Adapter<ViewHolder> {

	Context cont;
	ArrayList<ContactBean> search_result;
	LayoutInflater lf;

	public AdapterForContactSearchResult(Context cont,
			ArrayList<ContactBean> search_result) {
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
		View v = this.lf
				.inflate(R.layout.zitem_result_contactsearch, vg, false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, final int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new itemclickListener(position));
		MyViewHolder mvh = (MyViewHolder) vh;
		mvh.usericon.setImageResource(R.drawable.a_huiyiricheng_guesticon);
		mvh.username.setText(search_result.get(position).truename);// 这个字段是用户的用户名
		mvh.phone_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ search_result.get(position).mobile));
				cont.startActivity(intent);
			}
		});
		mvh.email_iamge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (search_result.get(position).map.ry_userId != null
						&& RongIM.getInstance() != null) {
					try {
						RongIM.getInstance().startPrivateChat(cont,
								search_result.get(position).map.ry_userId,
								search_result.get(position).truename);
					} catch (Exception e) {
					}
				}
			}
		});
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView usericon; // 用户头像
		TextView username; // 用户名字
		ImageView phone_image, email_iamge;
		View line;

		public MyViewHolder(View itemView) {
			super(itemView);
			usericon = (ImageView) itemView.findViewById(R.id.user_icon);
			username = (TextView) itemView.findViewById(R.id.userordept);
			phone_image = (ImageView) itemView.findViewById(R.id.phone_image);
			email_iamge = (ImageView) itemView.findViewById(R.id.email_iamge);
			line = itemView.findViewById(R.id.line);
		}
	}

	class itemclickListener implements OnClickListener {

		public int position;

		public itemclickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (search_result.get(position).map.ry_userId != null
					&& RongIM.getInstance() != null) {
				try {
					RongIM.getInstance().startPrivateChat(cont,
							search_result.get(position).map.ry_userId,
							search_result.get(position).truename);
				} catch (Exception e) {
				}
			}
		}
	}
}
