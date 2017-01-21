package com.gheng.exhibit.view.adapter;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.ZhuanShuFuWu;
import com.gheng.exhibit.view.BaseActivity;
import com.smartdot.wenbo.huiyi.R;

public class ZhuanShuFuWuAdapter extends BaseAdapter {
	private LayoutInflater la;
	private BaseActivity activity;
	public List<ZhuanShuFuWu> list = new ArrayList<ZhuanShuFuWu>();
	public int positions = 1;

	public ZhuanShuFuWuAdapter(BaseActivity activity) {
		this.activity = activity;
		la = LayoutInflater.from(this.activity);
	}

	public List<ZhuanShuFuWu> getList() {
		return list;
	}

	@SuppressWarnings("static-access")
	public void setList(List<ZhuanShuFuWu> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View converView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (converView == null) {
			holder = new ViewHolder();
			converView = la.inflate(R.layout.adapter_hujiaofuwu, null);
			holder.user_name = (TextView) converView
					.findViewById(R.id.user_name);
			holder.user_type = (TextView) converView
					.findViewById(R.id.user_type);
			holder.phone_number = (TextView) converView
					.findViewById(R.id.phone_number);
			holder.mail_image = (ImageView) converView
					.findViewById(R.id.mail_image);
			holder.user_icon = (ImageView) converView
					.findViewById(R.id.user_icon);
			holder.margin_line = converView.findViewById(R.id.margin_line);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		if (position == 0) {
			holder.margin_line.setVisibility(View.VISIBLE);
//			holder.line.setVisibility(View.VISIBLE);
		} else {
//			if (position == list.size() - 1) {
//				holder.line.setVisibility(View.GONE);
//			} else {
//				holder.line.setVisibility(View.VISIBLE);
//			}
		}
		holder.user_type.setText(list.get(position).juese);
		holder.user_name.setText(list.get(position).name);
		holder.phone_number.setText(list.get(position).mobile);
		holder.mail_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (list.get(position).ry_userId != null
						&& RongIM.getInstance() != null
						&& list.get(position).name != null) {
					try {
						RongIM.getInstance().startPrivateChat(activity,
								list.get(position).ry_userId, list.get(position).name);
					} catch (Exception e) {
					}
				}
			}
		});
		/**
		 * 拨打随行人员电话监听
		 */
		holder.phone_number.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				canPhone(list.get(position).mobile);
			}
		});

		return converView;
	}

	public class ViewHolder {
		TextView user_name, user_type, phone_number;
		ImageView user_icon, mail_image;
		View margin_line;
	}

	/**
	 * 拨打电话
	 * 
	 * @param num
	 */
	private void canPhone(String phonenumber) {
		System.out.println(phonenumber);
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ phonenumber));
		activity.startActivity(intent);

	}
}