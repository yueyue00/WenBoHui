package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.DaHuiData;
import com.gheng.exhibit.http.body.response.DaHuiDataFiles;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView.HeaderAdapter;
import com.hebg3.mxy.utils.AsyncTaskForDownloadFile;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class DaHuiDataAdapter1 extends BaseExpandableListAdapter implements
		HeaderAdapter {

	private List<DaHuiData> datas = new ArrayList<DaHuiData>();

	private BaseActivity a;

	private boolean isMine = false;

	private PinnedHeaderExpandableListView listView;
	private String smallPorurl = "";

	int listsize = 0;
	private DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();

	public DaHuiDataAdapter1(BaseActivity a,
			PinnedHeaderExpandableListView listView) {
		this.a = a;
		this.listView = listView;

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(180)).build();

	}

	public String getSmallPorurl() {
		return smallPorurl;
	}

	public void setSmallPorurl(String smallPorurl) {
		this.smallPorurl = smallPorurl;
	}

	public List<DaHuiData> getDatas() {
		return datas;
	}

	public void setDatas(List<DaHuiData> datas) {
		this.datas = datas;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	@Override
	public int getGroupCount() {
		for (int i = 0; i < datas.size(); i++) {
			listsize = datas.get(i).filedata.size() + listsize;
		}
		return datas.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition >= listsize || listsize == 0)
			return 0;
		return datas.get(groupPosition % listsize).filedata.size();
	}

	@Override
	public DaHuiData getGroup(int groupPosition) {
		if (listsize == 0)
			return null;
		groupPosition %= listsize;
		return datas.get(groupPosition);
	}

	@Override
	public DaHuiDataFiles getChild(int groupPosition, int childPosition) {
		groupPosition %= listsize;
		return datas.get(groupPosition).filedata.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		if (listsize != 0)
			groupPosition %= listsize;
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		if (listsize != 0)
			groupPosition %= listsize;
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (listsize != 0)
			groupPosition %= listsize;
		ViewHolderTitle holder = null;
		if (convertView == null) {
			holder = new ViewHolderTitle();
			convertView = a.getLayoutInflater().inflate(
					R.layout.item_dahuidata_group, null);

			ViewUtils.inject(holder, convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolderTitle) convertView.getTag();
		}
		holder.tv_file.setText(datas.get(groupPosition).folderName);
		if (groupPosition == 0) {
			holder.item_dahuidata_line.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (listsize != 0)
			groupPosition %= listsize;
		ViewHolderItem holder = null;
		if (convertView == null) {
			holder = new ViewHolderItem();
			convertView = a.getLayoutInflater().inflate(
					R.layout.item_dahuidata_child, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderItem) convertView.getTag();
		}
		if (datas.get(groupPosition).filedata.size() != 0
				&& !datas.get(groupPosition).filedata.equals("")) {
			DaHuiDataFiles file = datas.get(groupPosition).filedata
					.get(childPosition);
			String fileFormat = "";
			if (file.fileFormat != null && !file.fileFormat.equals("")) {
				fileFormat = file.fileFormat;
			}
			holder.filename_tv.setText(file.dataUploadName + fileFormat);
		}
		if (childPosition == 0) {
			holder.item_guibingxingcheng_line.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolderTitle {
		@ViewInject(R.id.item_dahuidata_line)
		View item_dahuidata_line;
		@ViewInject(R.id.tv_file)
		TextView tv_file;
	}

	class ViewHolderItem {
		@ViewInject(R.id.item_guibingxingcheng_line)
		View item_guibingxingcheng_line;
		@ViewInject(R.id.filename_tv)
		TextView filename_tv;
		@ViewInject(R.id.download_pb)
		ProgressBar pb;
		@ViewInject(R.id.download_ptv)
		TextView pbtv;
		@ViewInject(R.id.open_tv)
		TextView opentv;
		// @ViewInject(R.id.download_tv)
		// TextView download_tv;
	}

	static final int TYPE_ZAN = 1;
	static final int TYPE_BAOMING = 2;

	class OnClickListenerImpl {

		long id; // 点击的管理ID
		int type;
		int groupPosition;
		int childPosition;

		OnClickListenerImpl(long id, int type, int groupPosition,
				int childPosition) {
			this.id = id;
			this.type = type;
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
		}

	}

	@Override
	public int getHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		// ScheduleListData groupData = getGroup(groupPosition);
		// if (groupData != null) {
		// ((TextView) header.findViewById(R.id.tv)).setText(groupData.title);
		// }
	}

	private SparseIntArray groupStatusMap = new SparseIntArray();

	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getGroupClickStatus(int groupPosition) {
		if (groupStatusMap.keyAt(groupPosition) >= 0) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

	public class ClickListener implements OnClickListener {

		private int groupPosition, childPosition;

		public ClickListener(int groupPosition, int childPosition) {
			// TODO Auto-generated constructor stub
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
		}

		@Override
		public void onClick(View v) {
			AsyncTaskForDownloadFile at = new AsyncTaskForDownloadFile(
					a,
					datas.get(groupPosition).filedata.get(childPosition).dataUploadUrl);
			at.execute(1);
		}

	}
}
