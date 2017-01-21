package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.SiXinLieBiaoArr;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.checkin.checkin.ShowWebViewActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

public class SiXinAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	List<SiXinLieBiaoArr> list = new ArrayList<SiXinLieBiaoArr>();
	private BaseActivity activity;
	private Map<Integer, View> map = new HashMap<Integer, View>();
	DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();
	User user;
	int flag = 0;// 0是会务的，1是会议的.
	String vipphoto = "";// zyj新添的参数，为了服务人员登陆聊天窗口时显示vip的头像

	public SiXinAdapter(BaseActivity activity, int flag, String vipphoto) {
		mInflater = LayoutInflater.from(activity);
		this.activity = activity;
		this.flag = flag;
		this.vipphoto = vipphoto;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(30)).build();

		// 查找
		try {
			DbUtils db = DbUtils.create(activity);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public List<SiXinLieBiaoArr> getList() {
		return list;
	}

	public void setList(List<SiXinLieBiaoArr> list) {
		this.list = list;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		map.clear();
		super.notifyDataSetChanged();

	}

	public int getCount() {

		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	// public int getViewTypeCount() {
	// // TODO Auto-generated method stub
	// return 2;
	// }
	private class MyClickSpan implements OnClickListener {

		private String url;

		public MyClickSpan(String url) {
			// TODO Auto-generated constructor stub
			this.url = url;
		}

		@Override
		public void onClick(View widget) {
			Intent i = new Intent(activity, ShowWebViewActivity.class);
			i.putExtra("title", "私信");
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("url", url);
			activity.startActivity(i);
		}
	}

	public List<String> getUrlsInContent(String content) {
		List<String> termList = new ArrayList<String>();

		String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
		Pattern pattern = Pattern.compile(patternString);

		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			termList.add(matcher.group());
		}

		for (String temp : termList) {

		}

		return termList;
	}

	@SuppressWarnings("null")
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= 0) {
			convertView = map.get(position);
			if (convertView != null) {
				return convertView;
			}
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (flag == 0) {
				if (list.get(position).getMesstag().equals("0")) {
					convertView = mInflater.inflate(R.layout.item_chat_right,
							null);
				} else {
					convertView = mInflater.inflate(R.layout.item_chat_left,
							null);
				}
			} else {
				if (list.get(position).getMesstag().equals("0")) {
					convertView = mInflater.inflate(R.layout.item_chat_left,
							null);
				} else {
					convertView = mInflater.inflate(R.layout.item_chat_right,
							null);
				}
			}
			viewHolder = new ViewHolder();
			viewHolder.tv_sendtime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.iv_userhead = (ImageView) convertView
					.findViewById(R.id.iv_userhead);
			viewHolder.tv_chatcontent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_sendtime.setText(list.get(position).getCreateTimes());
		viewHolder.tv_chatcontent.setText(list.get(position).getMesscontent());

		// 设置textview 中超链接点击
		SpannableString ss = new SpannableString(list.get(position)
				.getMesscontent());
		List<String> tempL = getUrlsInContent(list.get(position)
				.getMesscontent());
		if (tempL.size() != 0) {
			for (String temp : tempL) {
				int index = list.get(position).getMesscontent().indexOf(temp);
				ss.setSpan(new URLSpan(temp), index, index + temp.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.tv_chatcontent.setText(ss);
				break;
			}

			viewHolder.tv_chatcontent.setOnClickListener(new MyClickSpan(tempL
					.get(0)));
		}
		if (flag == 0) {//0是会务
			if (list.get(position).getMesstag().equals("0")) {// 0是服务人员
				viewHolder.iv_userhead
						.setBackgroundResource(R.drawable.ic_launcher);
			} else {
				imageLoader.displayImage(vipphoto, viewHolder.iv_userhead,
						options);
			}
		} else {
			if (list.get(position).getMesstag().equals("0")) {// 0是服务人员
				viewHolder.iv_userhead
						.setBackgroundResource(R.drawable.ic_launcher);
			} else {
				try {
					if (user.getSmallPhotoUrl() != null)
						imageLoader.displayImage(
								Constant.decode(Constant.key,
										user.getSmallPhotoUrl()),
								viewHolder.iv_userhead, options);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		map.put(position, convertView);
		return convertView;
	}

	static class ViewHolder {
		/**
		 * 设置时间
		 * **/
		TextView tv_sendtime;
		/**
		 * 设置头像
		 * **/
		ImageView iv_userhead;
		/**
		 * 设置聊天内容
		 * **/
		TextView tv_chatcontent;
	}

}
