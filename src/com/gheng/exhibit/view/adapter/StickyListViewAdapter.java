package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import cn.jpush.a.a.b;

import com.gheng.exhibit.http.body.response.MyTaskContent;
import com.gheng.exhibit.http.body.response.MyTaskContent.GroupcontentBean;
import com.gheng.exhibit.http.body.response.MyTaskContentNew.InfoBean;
import com.gheng.exhibit.http.body.response.MyTaskContentNew.InfoBean.JourneyBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.hebg3.mxy.utils.MytaskAsynctask;
import com.hebg3.mxy.utils.TaskCommitAsyncTask;
import com.iadiae.acquaintancehelp.view.stickylistheaders.StickyListHeadersAdapter;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class StickyListViewAdapter extends BaseAdapter implements
		StickyListHeadersAdapter, SectionIndexer {

	private final Context context;
	private String[] mCountries = { "aBC", "bar" };
	private List<InfoBean> datas; // 原始数据源
	private int[] mSectionIndices;// 用来存放每一轮分组的第一个item的位置。
	private String[] mSectionLetters;// 用来存放每一个分组要展现的数据
	private LayoutInflater mInflater;
	private String vipid;

	public StickyListViewAdapter(Context context, List<InfoBean> datas,String vipid) {
		this.context = context;
		this.datas = datas;
		this.vipid = vipid;
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
				letters[i] = datas.get(i).getDate()+" "+datas.get(i).getWeek();
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
			convertView = mInflater.inflate(R.layout.mytask_new_list_item,
					parent, false);
			holder.container_ll = (LinearLayout) convertView
					.findViewById(R.id.item_container);

			// holder.tv_num = (TextView)
			// convertView.findViewById(R.id.mytask_list_tv_num);
			// holder.tv_content = (TextView)
			// convertView.findViewById(R.id.mytask_list_tv_content);
			// holder.cbBox = (CheckBox)
			// convertView.findViewById(R.id.mytask_list_cb);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		 InfoBean item = datas.get(position);
		int Num = item.getJourney().size();
		holder.container_ll.removeAllViews();
		for (int i = 0; i < Num; i++) {
			final JourneyBean bean = item.getJourney().get(i);
			View view = LayoutInflater.from(context).inflate(
					R.layout.mytask_list_item, null);
			TextView tv_num = (TextView) view
					.findViewById(R.id.mytask_list_tv_num);
			TextView tv_content = (TextView) view
					.findViewById(R.id.mytask_list_tv_content);
			CheckBox checkBox = (CheckBox) view
					.findViewById(R.id.mytask_list_cb);

			tv_num.setText((i+1)+".");
			tv_content.setText(bean.getTitle());
			if (bean.getSign().equals("1")) {
				checkBox.setChecked(true);
				checkBox.setEnabled(false);
			} else {
				checkBox.setChecked(false);
			}
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// Toast.makeText(context,
					// "选中了："+position+",内容："+data.get(position).getContent(),
					// Toast.LENGTH_LONG).show();
					if (isChecked) {
						showCustomDialog(context, bean, buttonView);
					}

				}
			});
			View line = new View(context);
			android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, 2);
			line.setLayoutParams(params);
			line.setBackgroundColor(Color.parseColor("#f3f3f3"));

			holder.container_ll.addView(view);
			holder.container_ll.addView(line);
		}

		return convertView;
	}

	public void showCustomDialog(final Context context,
			final JourneyBean bean, final CompoundButton buttonView) {
		// 将布局文件转为View
		View customDialogView = LayoutInflater.from(context).inflate(
				R.layout.custom_dialog, null);
		// 对话框
		final Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);//设置进度条是否可以按退回键取消
		dialog.setCanceledOnTouchOutside(false);//设置点击不会消失
		dialog.show();
		dialog.getWindow().setContentView(customDialogView);
		// 确认button
		Button btn_ok = (Button) customDialogView.findViewById(R.id.dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "确认完成该任务-->" + bean.getTitle(),
//						Toast.LENGTH_LONG).show();
				buttonView.setChecked(true);
//				bean.setWCSign("1");
				buttonView.setEnabled(false);
				loadData(bean);
				dialog.dismiss();
			}
		});
		// 取消Button
		Button btn_cancel = (Button) customDialogView
				.findViewById(R.id.dialog_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonView.setChecked(false);
				dialog.dismiss();
			}
		});
	}
	/**
	 * 从网络请求数据
	 */
	private User user;
	private String userid;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				System.out.println("==StickListViewAdapter 中的获取到的提交状态----》"+result);
				break;

			default:
				break;
			}
		};
	};
	private void loadData( JourneyBean bean) {
		// 查找
		try {
			DbUtils db = DbUtils.create(context);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			// user.getUserjuese().equals("3") ?
//			 Constant.decode(Constant.key,user.getVipid());
			// Constant.decode(Constant.key,user.getUserId())
			// URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
			userid = Constant.decode(Constant.key, user.getUserId());
			Log.d("tag", "=======StickyListViewAdapter--->userid=" + userid);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
          String type = "";
          String id = "";
          if (bean.getType().equals("1")) {//会议行程   对应的提交type 为2
			type = "2";
			id = bean.getMeetingId()+"";
        	System.out.println("=========会议行程-----》"+id);
		   }else if (bean.getType().equals("2")) {//个人行程 对应的提交type为 3
			type = "3";
			 id = bean.getXcid()+"";
			 System.out.println("=========个人行程-----》"+id);
		}
			try {
				new TaskCommitAsyncTask(handler.obtainMessage(), userid,vipid,id,type,"","",context)
						.execute(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
	}
	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.mytask_header_view,
					parent, false);
			holder.text = (TextView) convertView
					.findViewById(R.id.mytask_stick_tv_date);
//			holder.icon = (ImageView) convertView
//					.findViewById(R.id.mytask_stick_iv_icon);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		holder.text.setText(datas.get(position).getDate()+" "+datas.get(position).getWeek());
//        if (position == 0) {
//			holder.text.setBackgroundResource(R.drawable.bg_date1);
//			holder.icon.setImageResource(R.drawable.btn_meetingschedule_1);
//		}else if (position == 1) {
//			holder.text.setBackgroundResource(R.drawable.bg_date2);
//			holder.icon.setImageResource(R.drawable.btn_meetingschedule_2);
//		}else if (position == 2) {
//			holder.text.setBackgroundResource(R.drawable.bg_date3);
//			holder.icon.setImageResource(R.drawable.btn_meetingschedule_3);
//		}
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
//		ImageView icon;
	}

	class ViewHolder {
		LinearLayout container_ll;
		// TextView tv_num,tv_content;
		// CheckBox cbBox;
	}
}
