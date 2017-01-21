package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.checkin.checkin.DaHuiDianPingDetailActivity;
import com.hebg3.mxy.utils.DaHuiDianPingZhuTiListPojo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;

public class AdapterForDaHuiDianPingZhuTiDetail extends
		RecyclerView.Adapter<ViewHolder> {

	public DaHuiDianPingZhuTiListPojo pojo;// 主题对象
	public Context cont;
	public LayoutInflater lf;
	Drawable commentslogo;
	Drawable nocommentslogo;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	ItemDecoration itemline;

	public AdapterForDaHuiDianPingZhuTiDetail(Context cont,
			DaHuiDianPingZhuTiListPojo pojo) {

		this.cont = cont;
		this.pojo = pojo;
		this.lf = LayoutInflater.from(cont);
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
		return pojo.commentarray.size() + 1;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int ViewType) {
		// TODO Auto-generated method stub
		if (ViewType == 0) {
			return new ZhutiViewHolder(this.lf.inflate(
					R.layout.item_dahuidianping_zhutilist, vg, false));
		} else {
			return new CommentViewHolder(this.lf.inflate(
					R.layout.item_dahuidianpingdetailcomment, vg, false));
		}

	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		if (position == 0) {// 主题item
			ZhutiViewHolder mvh = (ZhutiViewHolder) vh;
			mvh.itemView.setOnClickListener(new itemClickLinstener(position));

			imageLoader.displayImage(pojo.uploaduserphoto, mvh.uploaduserphoto,
					options);

			if (pojo.comments != null && !pojo.comments.equals("0")) {
				commentslogo.setBounds(0, 0, commentslogo.getMinimumWidth(),
						commentslogo.getMinimumHeight());
				mvh.commentsdown.setCompoundDrawables(commentslogo, null, null,
						null);
				mvh.commentsright.setCompoundDrawables(commentslogo, null,
						null, null);
			} else {
				nocommentslogo.setBounds(0, 0,
						nocommentslogo.getMinimumWidth(),
						nocommentslogo.getMinimumHeight());
				mvh.commentsdown.setCompoundDrawables(nocommentslogo, null,
						null, null);
				mvh.commentsright.setCompoundDrawables(nocommentslogo, null,
						null, null);
			}
			mvh.commentsdown.setText(pojo.comments);
			mvh.commentsright.setText(pojo.comments);

			mvh.content.setText(pojo.content);
			mvh.uploadtime.setText(pojo.uploadtime);
			mvh.uploadusername.setText(pojo.uploadusername);

			if (pojo.photoarray.size() > 0) {
				mvh.photorv.setVisibility(View.VISIBLE);

				GridLayoutManager glm = new GridLayoutManager(cont, 3);
				glm.setOrientation(GridLayoutManager.VERTICAL);
				mvh.photorv.setLayoutManager(glm);
				mvh.photorv.removeItemDecoration(itemline);
				mvh.photorv.addItemDecoration(itemline);

				mvh.photorv
						.setAdapter(new AdapterForDaHuiDianPingZhuTiListPhotos(
								pojo.photoarray, cont));
				if (pojo.photoarray.size() > 3) {
					LayoutParams lp = mvh.photorv.getLayoutParams();
					lp.height = BaseActivity.dip2px((DaHuiDianPingDetailActivity) cont, 220);
					mvh.photorv.setLayoutParams(lp);
				} else {
					LayoutParams lp = mvh.photorv.getLayoutParams();
					lp.height = BaseActivity.dip2px((DaHuiDianPingDetailActivity) cont, 100);
					mvh.photorv.setLayoutParams(lp);
				}
			} else {
				mvh.photorv.setVisibility(View.GONE);
			}

			mvh.zhutipinglunfengexian.setVisibility(View.VISIBLE);
			mvh.commentsdown.setVisibility(View.GONE);
			mvh.state.setVisibility(View.GONE);
			if (pojo.commentarray.size() > 0) {
				mvh.quanbuhuiyingtv.setVisibility(View.VISIBLE);
				mvh.quanbuhuiyingtv.setText(BaseActivity
						.getLanguageString("全部回应"));
			}
		} else {// 评论item
			CommentViewHolder cvh = (CommentViewHolder) vh;
			cvh.itemView.setOnClickListener(new itemClickLinstener(position));

			if (!pojo.commentarray.get(position - 1).replyusername.equals("")) {// 回复某人
				String comment = "<font color=#4074C3>"
						+ BaseActivity.getLanguageString("回复") + ":"
						+ pojo.commentarray.get(position - 1).replyusername
						+ "</font>" + "&nbsp;&nbsp;&nbsp;"
						+ pojo.commentarray.get(position - 1).content;
				cvh.content.setText(Html.fromHtml(comment));// 现拼接成一个字符串，再统一用Html.fromHtml解析，才行，否则不变色
			} else {
				cvh.content
						.setText(pojo.commentarray.get(position - 1).content);
			}
			cvh.uploadtime
					.setText(pojo.commentarray.get(position - 1).uploadtime);
			cvh.uploadusername
					.setText(pojo.commentarray.get(position - 1).commentusername);

			if (!pojo.commentarray.get(position - 1).commentuserphoto
					.equals("")) {
				imageLoader.displayImage(
						pojo.commentarray.get(position - 1).commentuserphoto,
						cvh.uploaduserphoto, options);
			}
		}
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 主题的ViewHolder
	 */
	class ZhutiViewHolder extends RecyclerView.ViewHolder {

		ImageView uploaduserphoto;// 发布人头像
		TextView content; // 内容
		TextView commentsright; // 被评论次数 右边
		TextView commentsdown; // 被评论次数 下边
		TextView uploadusername; // 发起人姓名
		TextView uploadtime; // 发起时间
		TextView state; // 当前状态 1 已发布 2 发布中 3 未通过
		RecyclerView photorv; // 6张图片展示控件
		TextView zhutipinglunfengexian;// 详情界面 主题和评论的分割线
		TextView quanbuhuiyingtv;// //详情界面 主题和评论的分割线下面的 全部回应 tv

		public ZhutiViewHolder(View itemView) {
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
			zhutipinglunfengexian = (TextView) itemView
					.findViewById(R.id.zhutipinglunfengexian);
			quanbuhuiyingtv = (TextView) itemView
					.findViewById(R.id.quanbuhuiyingtv);
		}
	}

	/**
	 * 评论的ViewHolder
	 */
	class CommentViewHolder extends RecyclerView.ViewHolder {

		ImageView uploaduserphoto;// 发布人头像
		TextView content; // 内容
		TextView uploadusername; // 发起人姓名
		TextView uploadtime; // 发起时间

		public CommentViewHolder(View itemView) {
			super(itemView);

			uploaduserphoto = (ImageView) itemView
					.findViewById(R.id.uploaduserphoto);
			uploadusername = (TextView) itemView
					.findViewById(R.id.uploadusername);
			uploadtime = (TextView) itemView.findViewById(R.id.uploadtime);
			content = (TextView) itemView.findViewById(R.id.content);
		}
	}

	/**
	 * item点击事件 用来切换评论目标 主题还是评论
	 */
	class itemClickLinstener implements OnClickListener {

		public int position;

		public itemClickLinstener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (position == 0) {
				((DaHuiDianPingDetailActivity) cont).replyuserid = "";
				((DaHuiDianPingDetailActivity) cont).replyusername = "";
				((DaHuiDianPingDetailActivity) cont).fapingluned
						.setHint(BaseActivity.getLanguageString("发表评论"));
			} else {
				((DaHuiDianPingDetailActivity) cont).replyuserid = pojo.commentarray
						.get(position - 1).commentuserid;
				((DaHuiDianPingDetailActivity) cont).replyusername = pojo.commentarray
						.get(position - 1).commentusername;
				((DaHuiDianPingDetailActivity) cont).fapingluned
						.setHint(BaseActivity.getLanguageString("回复")
								+ " : "
								+ pojo.commentarray.get(position - 1).commentusername);
			}
		}
	}

}
