package com.gheng.exhibit.view.adapter;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.VipInfoListBaen.InfoBean;
import com.gheng.exhibit.view.checkin.VIPInfoActivity;
import com.gheng.exhibit.widget.CircleBitmapDisplayer;
import com.gheng.exhibit.widget.PinnedHeaderListView;
import com.gheng.exhibit.widget.PinnedHeaderListView.PinnedHeaderAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class FriendsAdapter extends BaseAdapter implements SectionIndexer,
		PinnedHeaderAdapter, OnScrollListener {
	private int mLocationPosition = -1;
	private ArrayList<String> listData;
	private ArrayList<InfoBean> mlist;
	// 首字母集
	private List<String> mFriendsSections;
	private List<Integer> mFriendsPositions;
	private LayoutInflater inflater;
	private Context context;
//	private ImageLoader imageLoader = ImageLoader.getInstance();
//	private DisplayImageOptions options;

	public FriendsAdapter(Context context, ArrayList<String> listData,
			List<String> friendsSections, List<Integer> friendsPositions,
			ArrayList<InfoBean> mlist) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.listData = listData;
		this.mFriendsSections = friendsSections;
		this.mFriendsPositions = friendsPositions;
		this.mlist = mlist;
		this.context = context;
//		 options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.a_huiyiricheng_guesticon)
//		.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
//		.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
//		.cacheInMemory(true)
//		.cacheOnDisc(true)
//		.bitmapConfig(Bitmap.Config.RGB_565)
//		.displayer(new CircleBitmapDisplayer())
//		.build();

		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int section = getSectionForPosition(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.vipinfo_lv_item, null);
		}
		LinearLayout mHeaderParent = (LinearLayout) convertView
				.findViewById(R.id.friends_item_header_parent);
		TextView mHeaderText = (TextView) convertView
				.findViewById(R.id.friends_item_header_text);
		
		// 打电话图标
		Button vil_btn_call = (Button) convertView
				.findViewById(R.id.vil_btn_call);
		vil_btn_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InfoBean infobean = null;
				for (int i = 0; i < listData.size(); i++) {
					if (listData.get(position).equals(
							mlist.get(i).getUsername())) {
						infobean = mlist.get(i);
					}
				}
				if (infobean != null) {
					String mobile = infobean.getMobile();
					if (mobile != null && !mobile.equals("")) {
						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:" + mobile));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
				}
			}
		});
		//发消息的图标
		Button vil_btn_sendinfo = (Button) convertView.findViewById(R.id.vil_btn_sendinfo);
		vil_btn_sendinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				InfoBean infobean = null;
				for (int i = 0; i < listData.size(); i++) {
					if (listData.get(position).equals(
							mlist.get(i).getUsername())) {
						infobean = mlist.get(i);
					}
				}
				/**
				 * 跳转融云单聊界面
				 */
				if (infobean.getMap().getRy_userId() != null
						&& RongIM.getInstance() != null) {
					try {
						RongIM.getInstance().startPrivateChat(context,
								infobean.getMap().getRy_userId(),
								infobean.getUsername());
					} catch (Exception e) {
					}
				}
			}				
		});
		//
		if (getPositionForSection(section) == position) {
			mHeaderParent.setVisibility(View.VISIBLE);
			mHeaderText.setText(mFriendsSections.get(section));
		} else {
			mHeaderParent.setVisibility(View.GONE);
		}
		TextView textView = (TextView) convertView
				.findViewById(R.id.vil_tv_name);
		textView.setText(listData.get(position));
//		ImageView iv_icon = (ImageView) convertView.findViewById(R.id.vil_ivicon);
//		for (int i = 0; i < mlist.size(); i++) {
//			if (mlist.get(i).getUsername().equals(listData.get(position))) {
//				imageLoader.displayImage(mlist.get(i).getPhotourl(), iv_icon, options);
//			}
//		}
		return convertView;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		String title = (String) getSections()[section];
		((TextView) header.findViewById(R.id.friends_list_header_text))
				.setText(title);
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return mFriendsSections.toArray();
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= mFriendsSections.size()) {
			return -1;
		}
		return mFriendsPositions.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(mFriendsPositions.toArray(), position);
		return index >= 0 ? index : -index - 2;
	}

}
