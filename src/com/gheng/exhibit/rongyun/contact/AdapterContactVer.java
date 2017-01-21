package com.gheng.exhibit.rongyun.contact;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.ContactBean;
import com.gheng.exhibit.rongyun.utils.MSharePreferenceUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

public class AdapterContactVer extends RecyclerView.Adapter<ViewHolder> {

	private List<ContactBean> mlist = new ArrayList<>();
	private Context context;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options_liebbiao;

	private Boolean showcb = false;
	public Boolean isEdit = false;

	// 修改的那些结合》》用来展示用

	@SuppressWarnings("deprecation")
	public AdapterContactVer(Context context) {
		this.context = context;
		MSharePreferenceUtils.getAppConfig(context);
		options_liebbiao = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(200)).build();
	}

	@Override
	public int getItemCount() {
		return mlist.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int arg1) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.zyjitem_dept_vertical, vg, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, final int position) {
		if (isEdit && !mlist.get(position).type.equals("1")) {
			vh.itemView.setClickable(false);
		} else {
			vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		}
		MyViewHolder mvh = (MyViewHolder) vh;
		String type = mlist.get(position).type;
		if (showcb && type.equals("2")) {
			mvh.checkbox.setVisibility(View.VISIBLE);
		} else {
			mvh.checkbox.setVisibility(View.GONE);
		}
		LayoutParams params = (RelativeLayout.LayoutParams) mvh.userordept
				.getLayoutParams();
		if (type.equals("1")) {
			type = "部门";
			mvh.email_iamge.setVisibility(View.GONE);
			mvh.phone_image.setVisibility(View.GONE);
			mvh.group_right.setVisibility(View.VISIBLE);
			mvh.user_icon.setBackgroundResource(R.drawable.btn_contact_group);
			//
			params.addRule(RelativeLayout.LEFT_OF, R.id.group_right);
			mvh.userordept.setLayoutParams(params); // 使layout更新
		} else {
			type = "员工";
			mvh.group_right.setVisibility(View.GONE);
			mvh.email_iamge.setVisibility(View.VISIBLE);
			mvh.phone_image.setVisibility(View.VISIBLE);
			mvh.user_icon
					.setBackgroundResource(R.drawable.a_huiyiricheng_guesticon);
			//
			params.addRule(RelativeLayout.LEFT_OF, R.id.phone_image);
			mvh.userordept.setLayoutParams(params); // 使layout更新
		}
		mvh.userordept.setText(mlist.get(position).name);
		mvh.phone_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ mlist.get(position).mobile));
				context.startActivity(intent);
			}
		});
		mvh.email_iamge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (mlist.get(position).map.ry_userId != null
//						&& RongIM.getInstance() != null) {
//					try {
//						RongIM.getInstance().startPrivateChat(context,
//								mlist.get(position).map.ry_userId,
//								mlist.get(position).truename);
//					} catch (Exception e) {
//					}
//				}
				doSendSMSTo(mlist.get(position).mobile, "");
			}
		});
		mvh.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mlist.get(position).setCheck(true);
					((ContactActivity) context).setCheck(mlist.get(position));
				} else {
					mlist.get(position).setCheck(false);
					((ContactActivity) context).removeCheck(mlist.get(position));
				}
			}
		});
		if (mlist.get(position).isCheck()) {
			mvh.checkbox.setChecked(true);
		} else {
			mvh.checkbox.setChecked(false);
		}
	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	public void setData(List<ContactBean> data) {
		this.mlist.clear();
		this.mlist.addAll(data);
		notifyDataSetChanged();
	}

	public void showCheckBox() {
		for (int i = 0; i < mlist.size(); i++) {
			mlist.get(i).setCheck(false);
		}
		showcb = true;
		notifyDataSetChanged();
	}

	public void closeCheckBox() {
		showcb = false;
		notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	public void saveData() {
		if (isEdit) {
			MSharePreferenceUtils.putBean(context, mlist.get(0).pid, mlist);
			((ContactActivity) context).changeids.add(mlist.get(0).pid);
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView group_right, phone_image, email_iamge, user_icon;
		TextView userordept;
		CheckBox checkbox;
		View line;

		public MyViewHolder(View itemview) {
			super(itemview);
			group_right = (ImageView) itemview.findViewById(R.id.group_right);
			phone_image = (ImageView) itemview.findViewById(R.id.phone_image);
			email_iamge = (ImageView) itemview.findViewById(R.id.email_iamge);
			user_icon = (ImageView) itemview.findViewById(R.id.user_icon);
			userordept = (TextView) itemview.findViewById(R.id.userordept);
			checkbox = (CheckBox) itemview.findViewById(R.id.checkbox);
			line = itemview.findViewById(R.id.line);
		}
	}

	// item点击事件
	class itemClickLinstener implements OnClickListener {

		public int position;

		public itemClickLinstener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (mlist.get(position).type.equals("1")) {
				saveData();
				String pid = mlist.get(position).pid;
				String id = mlist.get(position).id;
				String sign = "2";
				// Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
				((ContactActivity) context).LoadData(pid, id, sign);
			} else {
				if (mlist.get(position).map.ry_userId != null
						&& RongIM.getInstance() != null) {
					try {
						RongIM.getInstance().startPrivateChat(context,
								mlist.get(position).map.ry_userId,
								mlist.get(position).truename);
					} catch (Exception e) {
					}
				}
			}
		}
	}

	public void doSendSMSTo(String phoneNumber, String message) {
		if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
					+ phoneNumber));
			intent.putExtra("sms_body", message);
			context.startActivity(intent);
		}
	}
}
