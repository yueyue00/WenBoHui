package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.NewsLieBiao;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.DipUtils;
import com.gheng.exhibit.utils.MyAdGallery;
import com.gheng.exhibit.utils.MyAdGallery.MyOnItemClickListener;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.checkin.checkin.PchinewsdetailsWebActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForXinWen extends RecyclerView.Adapter<ViewHolder> {

	public List<NewsLieBiao> list = new ArrayList<NewsLieBiao>();// 消息提醒数据集合
	public LayoutInflater lf;
	Activity activity;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options_liebbiao;

	public AdapterForXinWen(Activity activity) {
		this.lf = LayoutInflater.from(activity);
		this.activity = activity;
		options_liebbiao = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyinews_listview)
				.showImageForEmptyUri(R.drawable.a_huiyinews_listview)
				.showImageOnFail(R.drawable.a_huiyinews_listview)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.build();
	}

	public List<NewsLieBiao> getList() {
		return list;
	}

	public void setList(List<NewsLieBiao> list) {
		this.list = list;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if (list.size() == 0) {
			return 0;
		} else if (list.get(0).lunbo.size() != 0) {
			return list.get(0).liebiao.size() + 1;
		} else {
			return list.get(0).liebiao.size();
		}

	}

	@Override
	// 这个方法返回item的view 不必考虑复用问题
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		System.out.println("3" + list.get(0).lunbo.size());
		if (viewType == 0 && list.get(0).lunbo.size() != 0) {// header
			View v = this.lf.inflate(R.layout.frag2_a, vg, false);
			initLunBo(v);
			System.out.println("1");
			return new HeaderViewHolder(v);
		} else {
			View v = this.lf.inflate(R.layout.item_xinwenlistrecyclerview, vg,
					false);
			System.out.println("2");
			return new MyViewHolder(v);
		}
	}

	/**
	 * @author liyy 初始化轮播图布局的大小2:1
	 */
	private void initLunBo(View v) {
		RelativeLayout lunbo = (RelativeLayout) v.findViewById(R.id.lunbo);
		int height = DipUtils.getWindows(activity)[0] / 2;
		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) lunbo
				.getLayoutParams();
		p.height = height;
		lunbo.setLayoutParams(p);
	}

	@Override
	// 这个方法用来实现数据和item的捆绑
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		// vh.itemView.setOnClickListener(new
		// itemClickLinstener(position));// 为item添加点击item事件

		if (list.get(0).lunbo.size() != 0 && position != 0) {
			MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
			mvh.title_xinwen
					.setText(list.get(0).liebiao.get(position - 1).title);
			mvh.time_xinwen
					.setText(list.get(0).liebiao.get(position - 1).content);
			mvh.relative_xinwenliebiao
					.setOnClickListener(new itemClickLinstener(position));

			/**
			 * 新闻列表缩略图
			 */
			imageLoader.displayImage(
					list.get(0).liebiao.get(position - 1).photourl,
					mvh.xinwentypelogo, options_liebbiao);
		} else if (list.get(0).liebiao.size() != 0) {
			if (list.get(0).lunbo.size() == 0) {
				MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
				mvh.title_xinwen
						.setText(list.get(0).liebiao.get(position).title);
				mvh.time_xinwen
						.setText(list.get(0).liebiao.get(position).content);
				mvh.relative_xinwenliebiao
						.setOnClickListener(new itemClickLinstener(position + 1));
				/**
				 * 新闻列表缩略图
				 */
				imageLoader.displayImage(
						list.get(0).liebiao.get(position).photourl,
						mvh.xinwentypelogo, options_liebbiao);

			}
		}

	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	class HeaderViewHolder extends RecyclerView.ViewHolder {

		// private int[] zhIcons = { R.drawable.kangfu_zh, R.drawable.quyu_zh,
		// R.drawable.cmef_zh, R.drawable.icmd_zh };
		// private int[] enIcons = { R.drawable.kangfu_en, R.drawable.quyu_en,
		// R.drawable.cmef_en, R.drawable.icmd_en };
		MyAdGallery gallery;
		LinearLayout ovalLayout;
		// ImageView image_moren;
		TextView text_lunbotucontent;

		public HeaderViewHolder(View itemView) {
			super(itemView);
			gallery = (MyAdGallery) itemView.findViewById(R.id.adgallery);// 获取Gallery组件
			ovalLayout = (LinearLayout) itemView.findViewById(R.id.ovalLayout);
			// image_moren = (ImageView)
			// itemView.findViewById(R.id.image_moren);
			text_lunbotucontent = (TextView) itemView
					.findViewById(R.id.text_lunbotucontent);
			// -------------------lyy 注释掉
			/*
			 * // 获取屏幕宽高 DisplayMetrics metrics = new DisplayMetrics();
			 * activity.
			 * getWindowManager().getDefaultDisplay().getMetrics(metrics);
			 * gallery.getLayoutParams().height = metrics.heightPixels / 4;
			 * image_moren.getLayoutParams().height = metrics.heightPixels / 4;
			 */

			if (list.get(0).lunbo.size() == 0) {
				// image_moren.setVisibility(View.VISIBLE);
				gallery.setVisibility(View.GONE);
			} else {
				// image_moren.setVisibility(View.GONE);
				gallery.setVisibility(View.VISIBLE);
				String[] mris = new String[list.get(0).lunbo.size()];
				List<String> listlunbo = new ArrayList<String>();
				for (int i = 0; i < list.get(0).lunbo.size(); i++) {
					mris[i] = list.get(0).lunbo.get(i).photourl;
					listlunbo.add(list.get(0).lunbo.get(i).title);
				}
				// 第二和第三参数 2选1 ,参数2为 图片网络路径数组 ,参数3为图片id的数组,本地测试用 ,2个参数都有优先采用 参数2
				gallery.start(activity, mris, 0, ovalLayout,
						R.drawable.wuzhen_dot_focused,
						R.drawable.wuzhen_dot_normal, text_lunbotucontent,
						listlunbo);
				gallery.setMyOnItemClickListener(new MyOnItemClickListener() {

					public void onItemClick(int curIndex) {
						Intent i = new Intent();
						i.setClass(activity, PchinewsdetailsWebActivity.class);
						i.putExtra("title",
								list.get(0).lunbo.get(curIndex).title);
						i.putExtra("content",
								list.get(0).lunbo.get(curIndex).content);
						i.putExtra("newsurl",
								list.get(0).lunbo.get(curIndex).newsurl);
						i.putExtra("imageurl",
								list.get(0).lunbo.get(curIndex).photourl);
						activity.startActivity(i);
					}
				});
			}

		}

		private List<Integer> getValues() {
			List<Integer> list = new ArrayList<Integer>();
			int[] icons;
			if (SharedData.getInt("i18n", Language.ZH) == Language.ZH) {
				// icons = zhIcons;
			} else {
				// icons = enIcons;
			}
			// for (int i : icons) {
			// list.add(i);
			// }
			return list;
		}
	}

	/**
	 * 开发者需要自己创建ViewHolder类
	 * 并定义item中控件对象，并完成初始化，不同的item（比如header或footer，需要开发者另行自定义ViewHolder）
	 */
	class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView xinwentypelogo;// 消息类型图标
		TextView time_xinwen; // 消息时间
		TextView title_xinwen;// 消息标题
		RelativeLayout relative_xinwenliebiao;

		public MyViewHolder(View itemView) {
			super(itemView);
			xinwentypelogo = (ImageView) itemView
					.findViewById(R.id.xinwentypelogo);
			title_xinwen = (TextView) itemView.findViewById(R.id.title_xinwen);
			time_xinwen = (TextView) itemView.findViewById(R.id.time_xinwen);
			relative_xinwenliebiao = (RelativeLayout) itemView
					.findViewById(R.id.relative_xinwenliebiao);
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
			Intent i = new Intent();
			i.setClass(activity, PchinewsdetailsWebActivity.class);
			i.putExtra("title", list.get(0).liebiao.get(position - 1).title);
			i.putExtra("content", list.get(0).liebiao.get(position - 1).content);
			i.putExtra("newsurl", list.get(0).liebiao.get(position - 1).newsurl);
			i.putExtra("imageurl",
					list.get(0).liebiao.get(position - 1).photourl);
			activity.startActivity(i);
		}
	}
}
