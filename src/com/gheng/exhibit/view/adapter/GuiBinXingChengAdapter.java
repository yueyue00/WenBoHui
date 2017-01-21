package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.VipXingCheng;
import com.gheng.exhibit.http.body.response.VipXingChengGuest;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.map.DingWeiDaoHangActivity;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView.HeaderAdapter;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class GuiBinXingChengAdapter extends BaseExpandableListAdapter implements
		HeaderAdapter {

	private List<VipXingCheng> datas = new ArrayList<VipXingCheng>();

	private BaseActivity a;

	private boolean isMine = false;

	private PinnedHeaderExpandableListView listView;
	private String smallPorurl = "";

	int listsize = 0;
	private DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();

	public GuiBinXingChengAdapter(BaseActivity a,
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

	public List<VipXingCheng> getDatas() {

		return datas;
	}

	public void setDatas(List<VipXingCheng> datas) {
		this.datas = datas;
	}

	/**
	 * @param isMine
	 *            the isMine to set
	 */
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	@Override
	public int getGroupCount() {
		for (int i = 0; i < datas.size(); i++) {
			listsize = datas.get(i).journey.size() + listsize;
		}
		return datas.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition >= listsize || listsize == 0)
			return 0;
		return datas.get(groupPosition % listsize).journey.size();
	}

	@Override
	public VipXingCheng getGroup(int groupPosition) {
		if (listsize == 0)
			return null;
		groupPosition %= listsize;
		return datas.get(groupPosition);
	}

	@Override
	public VipXingChengGuest getChild(int groupPosition, int childPosition) {
		groupPosition %= listsize;
		return datas.get(groupPosition).journey.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		groupPosition %= listsize;
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
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
		groupPosition %= listsize;
		ViewHolderTitle holder = null;
		if (convertView == null) {
			holder = new ViewHolderTitle();
			convertView = a.getLayoutInflater().inflate(
					R.layout.item_guibinxingcheng_title, null);

			ViewUtils.inject(holder, convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolderTitle) convertView.getTag();
		}
		if (groupPosition % 3 == 0) {
			holder.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang1);
			holder.tv.setTextColor(a.getResources().getColor(R.color.hong));
		} else if (groupPosition % 3 == 1) {
			holder.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang2);
			holder.tv.setTextColor(a.getResources().getColor(R.color.cheng));
		} else if (groupPosition % 3 == 2) {
			holder.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang3);
			holder.tv.setTextColor(a.getResources().getColor(R.color.lv));
		}
		if (groupPosition == 0) {
			// 以下内容将来会是网络访问获取数据
			// 获取数据库中保存的用户信息
			User Parent = null;
			try {
				DbUtils db = DbUtils.create(a);
				Parent = db.findFirst(Selector.from(User.class).where("id",
						"=", "1"));
				db.close();
				if (Parent.getSmallPhotoUrl() != null)
					setSmallPorurl(Constant.decode(Constant.key,
							Parent.getSmallPhotoUrl()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * 设置会议头像
			 */
			if (getSmallPorurl() != null && !getSmallPorurl().equals("")) {
				holder.image_photo.setPadding(10, 10, 10, 10);
			}
			imageLoader.displayImage(getSmallPorurl(), holder.image_photo,
					options);
			holder.relative_xingchengtouxiang.setVisibility(View.VISIBLE);
		} else {
			holder.relative_xingchengtouxiang.setVisibility(View.GONE);
		}
		holder.tv.setText(datas.get(groupPosition).date + "   "
				+ datas.get(groupPosition).week);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		groupPosition %= listsize;
		ViewHolderItem holder = null;
		if (convertView == null) {
			holder = new ViewHolderItem();
			convertView = a.getLayoutInflater().inflate(
					R.layout.item_guibinxingcheng_info, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderItem) convertView.getTag();
		}

		holder.textview.setText(BaseActivity.getLanguageString("位置"));
		holder.dahuitype.setText(datas.get(groupPosition).journey
				.get(childPosition).typename);
		holder.text_dahuitime.setText(datas.get(groupPosition).journey
				.get(childPosition).time);
		holder.text_dahuiplace.setText(datas.get(groupPosition).journey
				.get(childPosition).location);
		// holder.radio_guibindaohang.setOnClickListener(new ClickListener(
		// groupPosition, childPosition));
		holder.text_dahuimingcheng.setText(datas.get(groupPosition).journey
				.get(childPosition).title);
		holder.text_dahuidescription.setText(datas.get(groupPosition).journey
				.get(childPosition).description);
		holder.text_dahuidescription.setSelected(true);
		// 导航点和位置出现的代码
		// if (datas.get(groupPosition).journey.get(childPosition).type
		// .equals("0")) {
		// holder.radio_guibindaohangz.setVisibility(View.VISIBLE);
		// holder.textview.setVisibility(View.VISIBLE);
		// } else {
		// holder.radio_guibindaohangz.setVisibility(View.GONE);
		// holder.textview.setVisibility(View.GONE);
		// }

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolderTitle {

		@ViewInject(R.id.relative_xingchengtouxiang)
		LinearLayout relative_xingchengtouxiang;
		@ViewInject(R.id.tv)
		TextView tv;
		@ViewInject(R.id.image_photo)
		ImageView image_photo;
		@ViewInject(R.id.iv_angle)
		ImageView iv_angle;
		@ViewInject(R.id.view_fengexian)
		View view_fengexian;
	}

	class ViewHolderItem {

		@ViewInject(R.id.iv)
		ImageView iv;
		@ViewInject(R.id.text_dahuitype)
		TextView dahuitype;
		@ViewInject(R.id.text_dahuitime)
		TextView text_dahuitime;
		@ViewInject(R.id.text_dahuiplace)
		TextView text_dahuiplace;
		@ViewInject(R.id.text_dahuimingcheng)
		TextView text_dahuimingcheng;
		@ViewInject(R.id.textview)
		TextView textview;
		@ViewInject(R.id.radio_guibindaohang)
		RelativeLayout radio_guibindaohang;
		@ViewInject(R.id.radio_guibindaohangz)
		ImageView radio_guibindaohangz;
		@ViewInject(R.id.text_dahuidescription)
		TextView text_dahuidescription;
		@ViewInject(R.id.item_guibingxingcheng_line)
		View line;
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
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (datas.get(groupPosition).journey.get(childPosition).type
					.equals("0")) {
				if (datas.get(groupPosition).journey.get(childPosition).location
						.equals("华美宫")
						|| datas.get(groupPosition).journey.get(childPosition).location
								.equals("Hua Mei Conference Hall")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Waterside Resort");// 酒店id
					i.putExtra("searchspacename", "华美宫");// 搜索space
					i.putExtra("openfloornum", 3);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("华美宫"));// 酒店名称
					i.setClass(a, DingWeiDaoHangActivity.class);
					a.startActivity(i);
				} else if (datas.get(groupPosition).journey.get(childPosition).location
						.equals("龙凤厅")
						|| datas.get(groupPosition).journey.get(childPosition).location
								.equals("Long Feng Banquet Hall")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Waterside Resort");// 酒店id
					i.putExtra("searchspacename", "龙凤厅");// 搜索space
					i.putExtra("openfloornum", 1);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("龙凤厅"));// 酒店名称
					i.setClass(a, DingWeiDaoHangActivity.class);
					a.startActivity(i);
				} else if (datas.get(groupPosition).journey.get(childPosition).location
						.equals("宫音厅")
						|| datas.get(groupPosition).journey.get(childPosition).location
								.equals("Gong Yin Hall")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Waterside Resort");// 酒店id
					i.putExtra("searchspacename", "宫音厅");// 搜索space
					i.putExtra("openfloornum", 3);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("宫音厅"));// 酒店名称
					i.setClass(a, DingWeiDaoHangActivity.class);
					a.startActivity(i);
				} else if (datas.get(groupPosition).journey.get(childPosition).location
						.equals("荣锦厅")
						|| datas.get(groupPosition).journey.get(childPosition).location
								.equals("Rong Jin Conference Room")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Waterside Resort");// 酒店id
					i.putExtra("searchspacename", "荣锦厅");// 搜索space
					i.putExtra("openfloornum", 2);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("荣锦厅"));// 酒店名称
					i.setClass(a, DingWeiDaoHangActivity.class);
					a.startActivity(i);
				} else if (datas.get(groupPosition).journey.get(childPosition).location
						.equals("国乐厅")
						|| datas.get(groupPosition).journey.get(childPosition).location
								.equals("Guo Yue Hall")) {
					Intent i = new Intent();
					i.putExtra("openmappathid", "Tong An Hotel NO.1");// 酒店id
					i.putExtra("searchspacename", "国乐厅");// 搜索space
					i.putExtra("openfloornum", 1);// 所在楼层
					i.putExtra("titletv", BaseActivity.getLanguageString("国乐厅"));// 酒店名称
					i.setClass(a, DingWeiDaoHangActivity.class);
					a.startActivity(i);
				} else {
					Toast.makeText(a, BaseActivity.getLanguageString("该会场无地图"),
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	}
}
