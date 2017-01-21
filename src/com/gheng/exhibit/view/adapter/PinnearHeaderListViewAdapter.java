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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.gheng.exhibit.http.body.response.FixedTaskCommonVipBean.InfoBean;
import com.gheng.exhibit.view.adapter.FixedTaskAdapter.ViewHolder;
import com.gheng.exhibit.widget.CircleBitmapDisplayer;
import com.gheng.exhibit.widget.PinnedHeaderListView;
import com.gheng.exhibit.widget.PinnedHeaderListView.PinnedHeaderAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class PinnearHeaderListViewAdapter extends BaseAdapter implements
		SectionIndexer, PinnedHeaderAdapter, OnScrollListener {

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = null;
	private CompoundButton.OnCheckedChangeListener checkedListener;

	private int mLocationPosition = -1;
	private ArrayList<String> listData;
	private List<InfoBean> mlist;
	// 首字母集
	private List<String> mFriendsSections;
	private List<Integer> mFriendsPositions;
	private LayoutInflater inflater;
	private Context context;

	public PinnearHeaderListViewAdapter(Context context,
			ArrayList<String> listData, List<String> friendsSections,
			List<Integer> friendsPositions, List<InfoBean> mlist) {
		this.inflater = LayoutInflater.from(context);
		this.listData = listData;
		this.mFriendsSections = friendsSections;
		this.mFriendsPositions = friendsPositions;
		this.mlist = mlist;
		this.context = context;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer()).build();
	}

	public void setCheckedListener(
			CompoundButton.OnCheckedChangeListener checkedListener) {
		this.checkedListener = checkedListener;
	}

	@Override
	public int getCount() {
		if (listData != null) {
			return listData.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int section = getSectionForPosition(position);
		if (convertView == null) {
			// convertView =
			// inflater.inflate(R.layout.ft_pinnearhearderlistview_item, null);
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.ft_listview_item_container, null);
			holder.ll_container = (LinearLayout) convertView
					.findViewById(R.id.ft_listview_item_container);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ll_container.removeAllViews();

		// InfoBean jbListBean = mlist.get(position);// lixm
		// 此处不能使用postition来直接取每个listview的item，因为进行了排序会有错位的可能
		InfoBean jbListBean = null;
		for (int i = 0; i < mlist.size(); i++) {
			if (mlist.get(i).getName().equalsIgnoreCase(listData.get(position))) {// lixm
																					// 需要通过排序的名称来取值listview
																					// item的数据
				jbListBean = mlist.get(i);
			}
		}

		View view = LayoutInflater.from(context).inflate(
				R.layout.ft_pinnearhearderlistview_item, null);
		LinearLayout mHeaderParent = (LinearLayout) view
				.findViewById(R.id.ft_item_header_parent);
		TextView mHeaderText = (TextView) view
				.findViewById(R.id.ft_item_header_text);
		ImageView iv_icon = (ImageView) view
				.findViewById(R.id.ft_pin_item_iv_icon);
		CheckBox cb = (CheckBox) view.findViewById(R.id.ft_pin_item_cb);
		imageLoader.displayImage(jbListBean.getSmall_icon(), iv_icon, options);
		if (jbListBean.isCheckState()) {
			cb.setChecked(true);
		} else {
			cb.setChecked(false);
		}
		cb.setTag(listData.get(position));
		cb.setOnCheckedChangeListener(checkedListener);// 将监听事件传回到对应的activity中
		holder.ll_container.addView(view);

		if (getPositionForSection(section) == position) {
			mHeaderParent.setVisibility(View.VISIBLE);
			mHeaderText.setText(mFriendsSections.get(section));
		} else {
			mHeaderParent.setVisibility(View.GONE);
		}
		TextView textView = (TextView) view
				.findViewById(R.id.ft_pin_item_tv_name);
		textView.setText(listData.get(position));
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

	class ViewHolder {

		LinearLayout ll_container;
	}
}
