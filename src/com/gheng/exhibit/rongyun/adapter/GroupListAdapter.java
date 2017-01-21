package com.gheng.exhibit.rongyun.adapter;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.ContactBean;
import com.gheng.exhibit.http.body.response.RongGroup.RenyuanBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.smartdot.wenbo.huiyi.R;

/**
 * 群组列表的adapter
 * 
 * @author Administrator
 * 
 */
public class GroupListAdapter extends BaseAdapter {

	private Context mContext;
	private List<RenyuanBean> mList = new ArrayList<RenyuanBean>();
	private ImageLoader mImageLoader;
	private DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.rc_default_group_portrait)
			.showImageForEmptyUri(R.drawable.rc_default_group_portrait)
			.showImageOnFail(R.drawable.rc_default_group_portrait)
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
			.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
			.build();

	public GroupListAdapter(Context mContext, List<RenyuanBean> mList) {
		this.mContext = mContext;
		this.mList = mList;
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.group_list_item, null);
			holder.groupHeadImageView = (ImageView) convertView
					.findViewById(R.id.group_img);
			holder.groupNameTextView = (TextView) convertView
					.findViewById(R.id.group_name_tv);
			holder.email_iamge = (ImageView) convertView
					.findViewById(R.id.email_iamge);
			holder.phone_image = (ImageView) convertView
					.findViewById(R.id.phone_image);
			holder.group_right = (ImageView) convertView
					.findViewById(R.id.group_right);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mList.get(position).truename != null) {
			holder.groupNameTextView.setText(mList.get(position).truename);
			holder.groupHeadImageView.setBackgroundResource(R.drawable.a_huiyiricheng_guesticon);
			holder.email_iamge.setVisibility(View.VISIBLE);
			holder.phone_image.setVisibility(View.VISIBLE);
		} else {
			holder.groupNameTextView.setText(mList.get(position).name);
			holder.groupHeadImageView
					.setBackgroundResource(R.drawable.btn_contact_group);
			holder.email_iamge.setVisibility(View.GONE);
			holder.phone_image.setVisibility(View.GONE);
		}
		holder.phone_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ mList.get(position).mobile));
				mContext.startActivity(intent);
			}
		});
		holder.email_iamge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mList.get(position).truename != null
						&& mList.get(position).USER_ID != null) {
					if (RongIM.getInstance() != null) {
						try {
							RongIM.getInstance().startPrivateChat(mContext,
									mList.get(position).USER_ID,
									mList.get(position).truename);
						} catch (Exception e) {
						}
					}
				}
			}
		});
		return convertView;
	}

	private class ViewHolder {
		ImageView groupHeadImageView, group_right, email_iamge, phone_image;
		TextView groupNameTextView;
	}

	public void setmList(List<RenyuanBean> mList) {
		this.mList = mList;
	}

}
