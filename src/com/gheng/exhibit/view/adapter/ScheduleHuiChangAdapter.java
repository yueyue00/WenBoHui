//package com.gheng.exhibit.view.adapter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.util.SparseIntArray;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.gheng.exhibit.http.body.response.HYRiChengGuest;
//import com.gheng.exhibit.http.body.response.HYRiChengRiQi;
//import com.gheng.exhibit.view.BaseActivity;
//import com.gheng.exhibit.view.DingWeiDaoHangActivity;
//import com.gheng.exhibit.view.checkin.MineAttentionPchiDetailsActivity;
//import com.gheng.exhibit.widget.PinnedHeaderExpandableHuiChangListView;
//import com.gheng.exhibit.widget.PinnedHeaderExpandableHuiChangListView.HeaderHuiChangAdapter;
//import com.hebg3.mxy.utils.NsTextView;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//import com.smartdot.wuzhen.huiyi.R;
//
///**
// * 
// * @author wanglei
// */
//@SuppressLint("ResourceAsColor")
//public class ScheduleHuiChangAdapter extends BaseExpandableListAdapter
//		implements HeaderHuiChangAdapter {
//
//	// private List<ScheduleListData> datas = new ArrayList<ScheduleListData>();
//	private List<HYRiChengRiQi> dahuiinfos_date = new ArrayList<HYRiChengRiQi>();
//	private BaseActivity a;
//
//	private boolean isMine = false;
//
//	private PinnedHeaderExpandableHuiChangListView listView;
//	private Map<Integer, View> map = new HashMap<Integer, View>();
//	private DisplayImageOptions options;
//	ImageLoader imageLoader = ImageLoader.getInstance();
//	private int Position;
//	/**
//	 * 通过时间判断是否显示左侧图标
//	 * 
//	 * @param a
//	 * @param listView
//	 */
//	private int istime = 0;
//
//	public ScheduleHuiChangAdapter(BaseActivity a,
//			PinnedHeaderExpandableHuiChangListView listView) {
//		this.a = a;
//		this.listView = listView;
//		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.testphoto)
//				.showImageForEmptyUri(R.drawable.testphoto)
//				.showImageOnFail(R.drawable.testphoto).cacheInMemory(true)
//				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
//				.displayer(new RoundedBitmapDisplayer(180)).build();
//	}
//
//	public List<HYRiChengRiQi> getDahuiinfos_date() {
//		return dahuiinfos_date;
//	}
//
//	public void setDahuiinfos_date(List<HYRiChengRiQi> dahuiinfos_date) {
//		this.dahuiinfos_date = dahuiinfos_date;
//	}
//
//	/**
//	 * @param isMine
//	 *            the isMine to set
//	 */
//	public void setMine(boolean isMine) {
//		this.isMine = isMine;
//	}
//
//	@Override
//	public int getGroupCount() {
//		// int xh = 0;
//		// for (int i = 0; i < dahuiinfos_date.size(); i++) {
//		// int lengh = dahuiinfos_date.get(i).meetting.size();
//		// xh = xh + lengh;
//		// }
//		return dahuiinfos_date.size();
//	}
//
//	@Override
//	public int getChildrenCount(int groupPosition) {
//		if (groupPosition >= dahuiinfos_date.size()
//				|| dahuiinfos_date.size() == 0)
//			return 0;
//		if (dahuiinfos_date.get(groupPosition).rlist.size() % 2 == 0)
//			return dahuiinfos_date.get(groupPosition).rlist.size() / 2;
//		else
//			return (dahuiinfos_date.get(groupPosition).rlist.size() / 2) + 1;
//	}
//
//	@Override
//	public HYRiChengRiQi getGroup(int groupPosition) {
//		if (dahuiinfos_date.size() == 0)
//			return null;
//		groupPosition %= dahuiinfos_date.size();
//		return dahuiinfos_date.get(groupPosition);
//	}
//
//	@Override
//	public HYRiChengGuest getChild(int groupPosition, int childPosition) {
//		groupPosition %= dahuiinfos_date.size();
//		return dahuiinfos_date.get(groupPosition).rlist.get(childPosition);
//	}
//
//	@Override
//	public long getGroupId(int groupPosition) {
//		groupPosition %= dahuiinfos_date.size();
//		return groupPosition;
//	}
//
//	@Override
//	public long getChildId(int groupPosition, int childPosition) {
//		groupPosition %= dahuiinfos_date.size();
//		return dahuiinfos_date.get(groupPosition).rlist.get(childPosition).id;
//	}
//
//	@Override
//	public boolean hasStableIds() {
//		return false;
//	}
//
//	// @Override
//	// public void onGroupCollapsed(int groupPosition) {
//	// // TODO Auto-generated method stub
//	//
//	// super.onGroupCollapsed(groupPosition);
//	// System.out.println(groupPosition);
//	// istime = 0;
//	// dahuiinfos_date.get(groupPosition).setIskz(0);
//	// map.clear();
//	// // ScheduleAdapter.this.notifyDataSetInvalidated();
//	// System.out.println(dahuiinfos_date.get(groupPosition).getIskz());
//	// }
//
//	@Override
//	public void notifyDataSetChanged() {
//		// TODO Auto-generated method stub
//		super.notifyDataSetChanged();
//	}
//
//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent) {
//		groupPosition %= dahuiinfos_date.size();
//		ViewHolderTitle holder = null;
//		if (convertView == null) {
//			holder = new ViewHolderTitle();
//			convertView = a.getLayoutInflater().inflate(
//					R.layout.item_schedule_title, null);
//			ViewUtils.inject(holder, convertView);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolderTitle) convertView.getTag();
//		}
//		holder.imagebutton_dingwei.setVisibility(View.VISIBLE);
//		// 主讲人：textview
//		holder.text_zhujiangren.setText(BaseActivity.getLanguageString("主持人")
//				+ "：");
//		// 判断是否展开子列表
//		if (isExpanded) {
//			holder.image_dahuixiangqing
//					.setBackgroundResource(R.drawable.wuzhen_details_yes);
//		} else {
//			holder.image_dahuixiangqing
//					.setBackgroundResource(R.drawable.wuzhen_details_no);
//		}
//		if (dahuiinfos_date.get(groupPosition).getHeadflag()) {
//			holder.tv.setText(dahuiinfos_date.get(groupPosition).getweek());
//			holder.relative_riqi.setVisibility(View.VISIBLE);
//			holder.view_huibeijing.setVisibility(View.VISIBLE);
//
//			// 点击会场跳转室内地图
//			holder.relative_riqi.setOnClickListener(new ClickListener(
//					groupPosition));
//		} else {
//			holder.relative_riqi.setVisibility(View.GONE);
//			holder.view_huibeijing.setVisibility(View.GONE);
//		}
//
//		if (dahuiinfos_date.get(groupPosition).getIscolor() == 0) {
//			holder.iv_angle
//					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang1);
//			holder.tv.setTextColor(a.getResources().getColor(R.color.hong));
//		} else if (dahuiinfos_date.get(groupPosition).getIscolor() == 1) {
//			holder.iv_angle
//					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang2);
//			holder.tv.setTextColor(a.getResources().getColor(R.color.cheng));
//		} else if (dahuiinfos_date.get(groupPosition).getIscolor() == 2) {
//			holder.iv_angle
//					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang3);
//			holder.tv.setTextColor(a.getResources().getColor(R.color.lv));
//		}
//		holder.text_id.isfirsttimeondraw = true;
//		holder.text_id.setText(dahuiinfos_date.get(groupPosition)
//				.getMeettingtitle());
//		holder.time.setText(dahuiinfos_date.get(groupPosition)
//				.getMeettingtime());
//		holder.textView_zhujiangren.setText(dahuiinfos_date.get(groupPosition)
//				.getMeettingtalkman());
//
//		if (groupPosition == 0) {
//			holder.view_huibeijing.setVisibility(View.GONE);
//		}
//		/**
//		 * 添加详情按钮文字
//		 * **/
//		holder.text_dahuixiangqing
//				.setText(BaseActivity.getLanguageString("详情"));
//
//		final int pg = groupPosition;
//		/**
//		 * 大会详情页按钮点击事件
//		 * **/
//		holder.linear_dahuixiangqing.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(a,
//						MineAttentionPchiDetailsActivity.class);
//				intent.putExtra("pchi_id", dahuiinfos_date.get(pg)
//						.getMeettingid());
//				a.startActivity(intent);
//			}
//		});
//		// map.put(groupPosition, convertView);
//		return convertView;
//	}
//
//	@Override
//	public View getChildView(int groupPosition, int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent) {
//		groupPosition %= dahuiinfos_date.size();
//		ViewHolderItem holder = null;
//		if (convertView == null) {
//			holder = new ViewHolderItem();
//			convertView = a.getLayoutInflater().inflate(
//					R.layout.item_mschedule_info, null);
//			ViewUtils.inject(holder, convertView);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolderItem) convertView.getTag();
//		}
//
//		// /**
//		// * 小会详情按钮
//		// * **/
//		// holder.linear_xiaohuixiangqing.setOnClickListener(new
//		// OnClickListener() {
//		//
//		// @Override
//		// public void onClick(View arg0) {
//		// // TODO Auto-generated method stub
//		// Intent intent = new Intent(a,MineAttentionPchiDetailsActivity.class);
//		// a.startActivity(intent);
//		// }
//		// });
//
//		/**
//		 * 设置会议头像
//		 */
//		imageLoader.displayImage(dahuiinfos_date.get(groupPosition).getRlist()
//				.get(childPosition * 2).guestphotourl,
//				holder.image_huiyirentouxiang, options);
//		// holder.text_zhujiangrenzhiwei.isfirsttimeondraw = true;
//		holder.text_zhujiangrenname.setText(dahuiinfos_date.get(groupPosition)
//				.getRlist().get(childPosition * 2).guestname);
//		holder.text_zhujiangrenzhiwei
//				.setText(dahuiinfos_date.get(groupPosition).getRlist()
//						.get(childPosition * 2).guestzhiwu);
//		if (dahuiinfos_date.get(groupPosition).rlist.size() % 2 != 0
//				&& dahuiinfos_date.get(groupPosition).rlist.size() / 2 == childPosition) {// 判断最后一行是否只有一个演讲人
//																							// 如果只有一个演讲人隐藏第二个布局
//			holder.relative_item.setVisibility(View.GONE);
//		} else {
//			holder.relative_item.setVisibility(View.VISIBLE);
//			/**
//			 * 设置会议头像
//			 */
//			imageLoader.displayImage(dahuiinfos_date.get(groupPosition)
//					.getRlist().get(childPosition * 2 + 1).guestphotourl,
//					holder.image_huiyirentouxiangtwo, options);
//			holder.text_zhujiangrennametwo
//					.setText(dahuiinfos_date.get(groupPosition).getRlist()
//							.get(childPosition * 2 + 1).guestname);
//			// holder.text_zhujiangrenzhiweitwo.isfirsttimeondraw = true;
//			holder.text_zhujiangrenzhiweitwo
//					.setText(dahuiinfos_date.get(groupPosition).getRlist()
//							.get(childPosition * 2 + 1).guestzhiwu);
//		}
//
//		// LayoutParams lp = holder.v.getLayoutParams();
//		// lp.height = (int) (a.getResources().getDimensionPixelSize(
//		// R.dimen.schedule_list_logo_sizee) + 2 * a.getResources()
//		// .getDimension(R.dimen.common_list_padding));
//		return convertView;
//	}
//
//	@Override
//	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		return true;
//	}
//
//	class ViewHolderTitle {
//
//		@ViewInject(R.id.view_huibeijing)
//		View view_huibeijing;
//		@ViewInject(R.id.relative_riqi)
//		RelativeLayout relative_riqi;
//		@ViewInject(R.id.text_dahuixiangqing)
//		TextView text_dahuixiangqing;
//		@ViewInject(R.id.linear_dahuixiangqing)
//		LinearLayout linear_dahuixiangqing;
//		@ViewInject(R.id.tv)
//		TextView tv;
//		@ViewInject(R.id.text_id)
//		NsTextView text_id;
//		@ViewInject(R.id.time)
//		TextView time;
//		@ViewInject(R.id.textView_zhujiangren)
//		TextView textView_zhujiangren;
//		@ViewInject(R.id.iv_angle)
//		ImageView iv_angle;
//		@ViewInject(R.id.image_dahuixiangqing)
//		ImageView image_dahuixiangqing;
//		@ViewInject(R.id.text_zhujiangren)
//		TextView text_zhujiangren;
//		@ViewInject(R.id.imagebutton_dingwei)
//		ImageView imagebutton_dingwei;
//	}
//
//	class ViewHolderItem {
//
//		@ViewInject(R.id.iv)
//		ImageView iv;
//
//		@ViewInject(R.id.image_huiyirentouxiang)
//		ImageView image_huiyirentouxiang;
//		@ViewInject(R.id.text_zhujiangrenname)
//		TextView text_zhujiangrenname;
//		@ViewInject(R.id.text_zhujiangrenzhiwei)
//		TextView text_zhujiangrenzhiwei;
//
//		@ViewInject(R.id.image_huiyirentouxiangtwo)
//		ImageView image_huiyirentouxiangtwo;
//		@ViewInject(R.id.text_zhujiangrennametwo)
//		TextView text_zhujiangrennametwo;
//		@ViewInject(R.id.text_zhujiangrenzhiweitwo)
//		TextView text_zhujiangrenzhiweitwo;
//		@ViewInject(R.id.relative_item)
//		RelativeLayout relative_item;
//
//	}
//
//	static final int TYPE_ZAN = 1;
//	static final int TYPE_BAOMING = 2;
//
//	class OnClickListenerImpl {
//
//		long id; // 点击的管理ID
//		int type;
//		int groupPosition;
//		int childPosition;
//
//		OnClickListenerImpl(long id, int type, int groupPosition,
//				int childPosition) {
//			this.id = id;
//			this.type = type;
//			this.groupPosition = groupPosition;
//			this.childPosition = childPosition;
//		}
//
//	}
//
//	@Override
//	public int getHeaderState(int groupPosition, int childPosition) {
//		final int childCount = getChildrenCount(groupPosition);
//		if (childPosition == childCount - 1) {
//			return PINNED_HEADER_PUSHED_UP;
//		} else if (childPosition == -1
//				&& !listView.isGroupExpanded(groupPosition)) {
//			return PINNED_HEADER_GONE;
//		} else {
//			return PINNED_HEADER_VISIBLE;
//		}
//	}
//
//	@Override
//	public void configureHeader(View header, int groupPosition,
//			int childPosition, int alpha) {
//		HYRiChengRiQi groupData = getGroup(groupPosition);
//		if (groupData != null) {
//			// ((TextView)
//			// header.findViewById(R.id.tv)).setText(groupData.title);
//		}
//	}
//
//	private SparseIntArray groupStatusMap = new SparseIntArray();
//
//	@Override
//	public void setGroupClickStatus(int groupPosition, int status) {
//		groupStatusMap.put(groupPosition, status);
//	}
//
//	@Override
//	public int getGroupClickStatus(int groupPosition) {
//		// if (groupStatusMap.keyAt(groupPosition) >= 0) {
//		return groupStatusMap.get(groupPosition);
//		// } else {
//		// return 0;
//		// }
//	}
//
//	class ClickListener implements OnClickListener {
//		private int position;
//
//		ClickListener(int position) {
//			this.position = position;
//		}
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//
//			if (dahuiinfos_date.get(position).getweek().equals("华美宫")
//					|| dahuiinfos_date.get(position).getweek()
//							.equals("Hua Mei Conference Hall")) {
//				Intent i = new Intent();
//				i.putExtra("openmappathid", "Waterside Resort");// 酒店id
//				i.putExtra("searchspacename", "华美宫");// 搜索space
//				i.putExtra("openfloornum", 3);// 所在楼层
//				i.putExtra("titletv", BaseActivity.getLanguageString("华美宫"));// 酒店名称
//				i.setClass(a, DingWeiDaoHangActivity.class);
//				a.startActivity(i);
//			} else if (dahuiinfos_date.get(position).getweek().equals("龙凤厅")
//					|| dahuiinfos_date.get(position).getweek()
//							.equals("Long Feng Banquet Hall")) {
//				Intent i = new Intent();
//				i.putExtra("openmappathid", "Waterside Resort");// 酒店id
//				i.putExtra("searchspacename", "龙凤厅");// 搜索space
//				i.putExtra("openfloornum", 1);// 所在楼层
//				i.putExtra("titletv", BaseActivity.getLanguageString("龙凤厅"));// 酒店名称
//				i.setClass(a, DingWeiDaoHangActivity.class);
//				a.startActivity(i);
//			} else if (dahuiinfos_date.get(position).getweek().equals("宫音厅")
//					|| dahuiinfos_date.get(position).getweek()
//							.equals("Gong Yin Hall")) {
//				Intent i = new Intent();
//				i.putExtra("openmappathid", "Waterside Resort");// 酒店id
//				i.putExtra("searchspacename", "宫音厅");// 搜索space
//				i.putExtra("openfloornum", 3);// 所在楼层
//				i.putExtra("titletv", BaseActivity.getLanguageString("宫音厅"));// 酒店名称
//				i.setClass(a, DingWeiDaoHangActivity.class);
//				a.startActivity(i);
//			} else if (dahuiinfos_date.get(position).getweek().equals("荣锦厅")
//					|| dahuiinfos_date.get(position).getweek()
//							.equals("Rong Jin Conference Room")) {
//				Intent i = new Intent();
//				i.putExtra("openmappathid", "Waterside Resort");// 酒店id
//				i.putExtra("searchspacename", "荣锦厅");// 搜索space
//				i.putExtra("openfloornum", 2);// 所在楼层
//				i.putExtra("titletv", BaseActivity.getLanguageString("荣锦厅"));// 酒店名称
//				i.setClass(a, DingWeiDaoHangActivity.class);
//				a.startActivity(i);
//			} else if (dahuiinfos_date.get(position).getweek().equals("国乐厅")
//					|| dahuiinfos_date.get(position).getweek()
//							.equals("Guo Yue Hall")) {
//				Intent i = new Intent();
//				i.putExtra("openmappathid", "Tong An Hotel NO.1");// 酒店id
//				i.putExtra("searchspacename", "国乐厅");// 搜索space
//				i.putExtra("openfloornum", 1);// 所在楼层
//				i.putExtra("titletv", BaseActivity.getLanguageString("国乐厅"));// 酒店名称
//				i.setClass(a, DingWeiDaoHangActivity.class);
//				a.startActivity(i);
//			} else {
//				Toast.makeText(a, BaseActivity.getLanguageString("该会场无地图"),
//						Toast.LENGTH_SHORT).show();
//			}
//		}
//
//	}
//}
