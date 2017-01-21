package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.checkin.DaHuiDianPingActivity;
import com.gheng.exhibit.view.checkin.checkin.DaHuiDianPingDetailActivity;
import com.gheng.exhibit.view.checkin.checkin.DaHuiDianPingMineActivity;
import com.hebg3.mxy.utils.AsyncTaskForGetDaHuiDianPingZhuTiPojo;
import com.hebg3.mxy.utils.DaHuiDianPingZhuTiListPojo;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForDaHuiDianPingZhuTiList extends
		RecyclerView.Adapter<ViewHolder> {

	public ArrayList<DaHuiDianPingZhuTiListPojo> list;// 消息提醒数据集合
	public Context cont;
	public LayoutInflater lf;
	Drawable commentslogo;
	Drawable nocommentslogo;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	ProgressDialog pd;
	ItemDecoration itemline;
	String userid;
	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			if (msg.what == 1) {// 有数据
				Intent i = new Intent(cont, DaHuiDianPingDetailActivity.class);
				i.putExtra("pojo", (DaHuiDianPingZhuTiListPojo) msg.obj);
				cont.startActivity(i);
			} else {// 网络不给力
				Toast.makeText(cont, BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	public AdapterForDaHuiDianPingZhuTiList(Context cont,
			ArrayList<DaHuiDianPingZhuTiListPojo> list, String userid) {

		this.cont = cont;
		this.list = list;
		this.lf = LayoutInflater.from(cont);
		this.userid = userid;
		this.itemline = new HorizontalDividerItemDecoration.Builder(cont)
				.color(cont.getResources().getColor(R.color.touming))
				.size(cont.getResources().getDimensionPixelSize(
						R.dimen.recylerviewitemdivider_pchi))
				.margin(cont.getResources().getDimensionPixelSize(
						R.dimen.title_padding),
						cont.getResources().getDimensionPixelSize(
								R.dimen.title_padding)).build();

		commentslogo = cont.getResources().getDrawable(R.drawable.commentslogo);
		nocommentslogo = cont.getResources().getDrawable(
				R.drawable.nocommentslogo);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnLoading(R.drawable.a_huiyiricheng_guesticon)
				// 加载时显示选择进度条
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				// 加载失败时显示缺省头像
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
				.cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(180))// 圆形图片
																				// 头像
				.considerExifParams(true)// 考虑图片拍照时被旋转问题
				.resetViewBeforeLoading(true).build();
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int ViewType) {
		// TODO Auto-generated method stub
		return new MyViewHolder(this.lf.inflate(
				R.layout.item_dahuidianping_zhutilist, vg, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		MyViewHolder mvh = (MyViewHolder) vh;
		mvh.itemView.setOnClickListener(new itemClickLinstener(position));
		// mvh.uploaduserphoto
		// .setOnClickListener(new userClickLinstener(position));
		imageLoader.displayImage(list.get(position).uploaduserphoto,
				mvh.uploaduserphoto, options);

		if (list.get(position).comments != null
				&& !list.get(position).comments.equals("0")) {
			commentslogo.setBounds(0, 0, commentslogo.getMinimumWidth(),
					commentslogo.getMinimumHeight());
			mvh.commentsdown.setCompoundDrawables(commentslogo, null, null,
					null);
			mvh.commentsright.setCompoundDrawables(commentslogo, null, null,
					null);
		} else {
			nocommentslogo.setBounds(0, 0, nocommentslogo.getMinimumWidth(),
					nocommentslogo.getMinimumHeight());
			mvh.commentsdown.setCompoundDrawables(nocommentslogo, null, null,
					null);
			mvh.commentsright.setCompoundDrawables(nocommentslogo, null, null,
					null);
		}
		mvh.commentsdown.setText(list.get(position).comments);
		mvh.commentsright.setText(list.get(position).comments);

		mvh.content.setText(list.get(position).content);
		mvh.uploadtime.setText(list.get(position).uploadtime);
		mvh.uploadusername.setText(list.get(position).uploadusername);

		if (list.get(position).state == 2) {
			mvh.state.setText(BaseActivity.getLanguageString("已发布"));
			mvh.state.setBackgroundDrawable(cont.getResources().getDrawable(
					R.drawable.passedbg));
		}
		if (list.get(position).state == 1) {
			mvh.state.setText(BaseActivity.getLanguageString("发布中"));
			mvh.state.setBackgroundDrawable(cont.getResources().getDrawable(
					R.drawable.checkingbg));
		}
		if (list.get(position).state == 3) {
			mvh.state.setText(BaseActivity.getLanguageString("未通过"));
			mvh.state.setBackgroundDrawable(cont.getResources().getDrawable(
					R.drawable.bohuibg));
		}
		if (list.get(position).photoarray.size() > 0) {
			mvh.photorv.setVisibility(View.VISIBLE);

			GridLayoutManager glm = new GridLayoutManager(cont, 3);
			glm.setOrientation(GridLayoutManager.VERTICAL);
			mvh.photorv.setLayoutManager(glm);
			mvh.photorv.removeItemDecoration(itemline);
			mvh.photorv.addItemDecoration(itemline);
			mvh.photorv.setAdapter(new AdapterForDaHuiDianPingZhuTiListPhotos(
					list.get(position).photoarray, cont));
			if (list.get(position).photoarray.size() > 3) {
				LayoutParams lp = mvh.photorv.getLayoutParams();
				lp.height = BaseActivity.dip2px((BaseActivity) cont, 220);
				mvh.photorv.setLayoutParams(lp);
			} else {
				LayoutParams lp = mvh.photorv.getLayoutParams();
				lp.height = BaseActivity.dip2px((BaseActivity) cont, 100);
				mvh.photorv.setLayoutParams(lp);
			}
		} else {
			mvh.photorv.setVisibility(View.GONE);
		}

		// 判断是那个activity 该隐藏的隐藏，该显示的显示
		if (cont instanceof DaHuiDianPingActivity) {// 大会点评界面
			mvh.commentsright.setVisibility(View.GONE);
			mvh.state.setVisibility(View.GONE);

		}
		if (cont instanceof DaHuiDianPingMineActivity) {// 大会点评_我的界面
			mvh.commentsright.setVisibility(View.GONE);
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView uploaduserphoto;// 发布人头像
		TextView content; // 内容
		TextView commentsright; // 被评论次数 右边
		TextView commentsdown; // 被评论次数 下边
		TextView uploadusername; // 发起人姓名
		TextView uploadtime; // 发起时间
		TextView state; // 当前状态 1 已发布 2 发布中 3 未通过
		RecyclerView photorv; // 6张图片展示控件

		public MyViewHolder(View itemView) {
			super(itemView);

			uploaduserphoto = (ImageView) itemView
					.findViewById(R.id.uploaduserphoto);
			content = (TextView) itemView.findViewById(R.id.content);
			commentsright = (TextView) itemView
					.findViewById(R.id.commentsright);
			commentsdown = (TextView) itemView.findViewById(R.id.commentsdown);
			uploadusername = (TextView) itemView
					.findViewById(R.id.uploadusername);
			uploadtime = (TextView) itemView.findViewById(R.id.uploadtime);
			state = (TextView) itemView.findViewById(R.id.state);
			photorv = (RecyclerView) itemView.findViewById(R.id.photorv);
		}
	}

	/**
	 * item点击事件
	 */
	class itemClickLinstener implements OnClickListener {

		public int position;

		public itemClickLinstener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 根据actionid发起线程请求接口，获取这个主题单个详情 跳转详情界面

			if (!IsWebCanBeUse.isWebCanBeUse(cont)) {
				Toast.makeText(cont, BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
				return;
			}

			pd = new ProgressDialog(cont);
			pd.setMessage(BaseActivity.getLanguageString("加载中..."));
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();

			AsyncTaskForGetDaHuiDianPingZhuTiPojo at = new AsyncTaskForGetDaHuiDianPingZhuTiPojo(
					h.obtainMessage(), list.get(position).actionid, cont,
					userid);
			at.execute(1);

		}
	}

	// 头像等用户信息的点击事件
	class userClickLinstener implements OnClickListener {

		public int position;

		public userClickLinstener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 根据updateuserid跳转到详情界面
			Intent intent = new Intent(cont, DaHuiDianPingMineActivity.class);
			intent.putExtra("userid", list.get(position).uploaduserid);
			intent.putExtra("username", list.get(position).uploadusername);
			cont.startActivity(intent);
		}
	}
}
