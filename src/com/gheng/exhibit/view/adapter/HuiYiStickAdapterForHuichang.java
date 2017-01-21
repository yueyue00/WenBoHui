package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.DaHuiHuiChang;
import com.gheng.exhibit.http.body.response.DaHuiInfo;
import com.gheng.exhibit.http.body.response.VipSchduleBean.InfoBean;
import com.gheng.exhibit.http.body.response.VipSchduleBean.InfoBean.JourneyBean;
import com.gheng.exhibit.view.adapter.StickyHeaderListViewForVip.HeaderViewHolder;
import com.gheng.exhibit.view.adapter.StickyHeaderListViewForVip.ViewHolder;
import com.gheng.exhibit.view.checkin.HuiYiXiangQingActivity;
import com.gheng.exhibit.view.checkin.checkin.SchduleDetailActivity;
import com.iadiae.acquaintancehelp.view.stickylistheaders.StickyListHeadersAdapter;
import com.smartdot.wenbo.huiyi.R;

public class HuiYiStickAdapterForHuichang extends BaseAdapter implements
StickyListHeadersAdapter, SectionIndexer{
	private final Context context;
	private String[] mCountries = { "aBC", "bar" };
	private List<DaHuiHuiChang> datas; //原始数据源
	private int[] mSectionIndices;//用来存放每一轮分组的第一个item的位置。
	private String[] mSectionLetters;//用来存放每一个分组要展现的数据
	private LayoutInflater mInflater;

	public HuiYiStickAdapterForHuichang(Context context, List<DaHuiHuiChang> datas) {
		this.context = context;
		this.datas = datas;
		mInflater = LayoutInflater.from(context);

		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();

	}

	private int[] getSectionIndices() {
		ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
		char lastFirstChar = mCountries[0].charAt(0);
		sectionIndices.add(0);
		for (int i = 1; i < mCountries.length; i++) {
			if (mCountries[i].charAt(0) != lastFirstChar) {
				lastFirstChar = mCountries[i].charAt(0);
				sectionIndices.add(i);
			}
		}
		int[] sections = new int[sectionIndices.size()];
		for (int i = 0; i < sectionIndices.size(); i++) {
			sections[i] = sectionIndices.get(i);
		}
		return sections;
	}

	private String[] getSectionLetters() {
		String[] letters = new String[datas.size()];
		if (datas != null) {
			for (int i = 0; i < datas.size(); i++) {
				letters[i] = datas.get(i).meettingname;
			}
		}

		return letters;
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.vip_schdule_list_item,
					parent, false);
			holder.container_ll = (LinearLayout) convertView
					.findViewById(R.id.vip_list_item_container);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DaHuiHuiChang item = datas.get(position);//接口数据
		int Num = item.meetting.size();
		holder.container_ll.removeAllViews();
		for (int i = 0; i < Num; i++) {
			final DaHuiInfo bean = item.meetting.get(i);
			View view = LayoutInflater.from(context).inflate(R.layout.huiyi_riqi_list_container, null);
			TextView tv_title = (TextView) view.findViewById(R.id.huiyi_riqiContainer_tv_title);
			TextView tv_host = (TextView) view.findViewById(R.id.huiyi_riqiContainer_host);
            LinearLayout containerLayout = (LinearLayout) view.findViewById(R.id.huiyi_riqiContainer_ll);
            TextView tv_date = (TextView) view.findViewById(R.id.huiyi_riqiContainer_tv_date);
			tv_title.setText(bean.meettingtitle);
			tv_host.setText(bean.meettingtalkman);
			
			if (bean.meettingtalkman.length() == 0) {
				tv_host.setVisibility(View.GONE);
			}
			tv_date.setText(bean.meettingtime);
			
//			long startTime = Long.parseLong(bean.startTime);
//			long endTime = Long.parseLong(bean.endTiime);			
//			long currentTimeMillis = System.currentTimeMillis();
//			if (endTime<currentTimeMillis) {//该会议日程已经完成
//				tv_title.setTextColor(Color.parseColor("#aaaaaa"));
//			}else if (startTime < currentTimeMillis && endTime > currentTimeMillis) {//该会议日程正在进行
//				tv_title.setTextColor(Color.parseColor("#000000"));
//				tv_title.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
//			}else if(currentTimeMillis < startTime){//该会议日程还未开始
//				tv_title.setTextColor(Color.parseColor("#686768"));
//			}
			
           containerLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//            Toast.makeText(context, "您点击的huichang是日程位置为："+bean.meettingtitle, Toast.LENGTH_LONG).show();	
            Intent intent = new Intent(context, HuiYiXiangQingActivity.class);
            intent.putExtra("pchi_id", bean.meettingid);
            context.startActivity(intent);
			}
		});
           //相当于是item的分割线
//			View line = new View(context);
//			android.view.ViewGroup.LayoutParams params  = new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,2);
//		   
//			line.setLayoutParams(params);
//			line.setBackgroundColor(Color.GRAY);
			
			holder.container_ll.addView(view);
//			holder.container_ll.addView(line);
		}

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.mytask_list_stickyhead,
					parent, false);
			holder.text = (TextView) convertView
					.findViewById(R.id.mytask_stickTvHead);
			holder.iv = (ImageView) convertView.findViewById(R.id.vipschedule_stickIvIcon);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		holder.text.setText(datas.get(position).meettingname);
		if (position % 3 == 0) {
			holder.text.setBackgroundResource(R.drawable.bg_date1);
			holder.iv.setImageResource(R.drawable.btn_meetingschedule_1);
		}else if (position % 3 == 1) {
			holder.text.setBackgroundResource(R.drawable.bg_date2);
			holder.iv.setImageResource(R.drawable.btn_meetingschedule_2);
		}else if (position % 3 == 2) {
			holder.text.setBackgroundResource(R.drawable.bg_date3);
			holder.iv.setImageResource(R.drawable.btn_meetingschedule_3);
		}

		return convertView;

	}

	@Override
	public long getHeaderId(int position) {
		return position;
	}

	@Override
	public int getPositionForSection(int section) {
		if (section >= mSectionIndices.length) {
			section = mSectionIndices.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return mSectionIndices[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < mSectionIndices.length; i++) {
			if (position < mSectionIndices[i]) {
				return i - 1;
			}
		}
		return mSectionIndices.length - 1;
	}

	@Override
	public Object[] getSections() {
		return mSectionLetters;
	}

	class HeaderViewHolder {
		TextView text;
		ImageView iv;
	}

	class ViewHolder {
		LinearLayout container_ll;

	}


}
