package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.gheng.exhibit.http.body.response.News_LunBoTu;
import com.gheng.exhibit.view.checkin.checkin.PchinewsdetailsWebActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

/**
 * @author wanglei
 */
public class NewsAdapter extends AbstractEntityAdapter<String> {

	private boolean fromMap = false;
	private Context context;
	int wangleiposition;// 解决position 越界问题


	private List<News_LunBoTu> list = new ArrayList<News_LunBoTu>();

	public List<News_LunBoTu> getList() {
		return list;
	}

	public void setList(List<News_LunBoTu> list) {
		this.list = list;
	}

	// 宽度铺满还是高度铺满屏幕
	private boolean isWidth;
	private DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();
	private int pd = 0;

	public NewsAdapter(Context context) {
		super(context);
		this.context = context;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyinews_viewpager)
				.showImageForEmptyUri(R.drawable.a_huiyinews_viewpager)
				.showImageOnFail(R.drawable.a_huiyinews_viewpager)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.build();

	}


	@Override
	protected View makeView(final int position) {
		View view = inflater.inflate(R.layout.item_main, null);

		wangleiposition = position;// 先把原来的position赋值给自定义position
		if (wangleiposition >= list.size()) {// position是从0开始 所以判断>即可
			wangleiposition = wangleiposition % list.size();// 如果超过
		}
		System.out.println(list.size()+"     list.size()");
		System.out.println(position+"     position");
		System.out.println(wangleiposition+"     wangleiposition");
		
		
		view.findViewById(R.id.iv).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(context, PchinewsdetailsWebActivity.class);
				i.putExtra("newsurl", list.get(wangleiposition).newsurl);
				context.startActivity(i);
			}
		});
		return view;
	}

	/**
	 * @param fromMap
	 *            the fromMap to set
	 */
	public void setFromMap(boolean fromMap) {
		this.fromMap = false;
	}

	@Override
	public int getCount() {
		if (fromMap) {

		} else {
			if (data != null && data.size() > 1) {
				return Integer.MAX_VALUE;
			}
		}
		return data.size();
	}

	@Override
	public String getItem(int position) {
		if (data != null && data.size() > 0) {
			return data.get(position % data.size());
		}
		return super.getItem(position);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHoler();
	}

	@Override
	protected void setHolderValue(Object holder, String data) {
		ViewHoler obj = (ViewHoler) holder;

		/**
		 * 设置轮播图 图片
		 */
		imageLoader.displayImage(data, obj.iv, options);

	}

	class ViewHoler {
		@ViewInject(R.id.iv)
		ImageView iv;
	}

	/**
	 * @author 马晓勇 处理高分辨率轮播图片，并显示在控件上 防止OOM Resources 应用资源库 Integer 文件id
	 *         ImageView 展示控件
	 */
	public void compressPhotoAndSetPhoto(Resources re, Integer id, ImageView iv) {
		Bitmap bitmap = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// inJustDecodeBounds如果设置为true，解码器将返回null（没有位图的bitmap对象）
		// 但是调用者仍然可以查询位图的实际像素，系统并没有为位图像素分配内存
		opt.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeResource(re, id, opt);

		// 获取到这个图片的原始宽度和高度
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		// 期望的图片宽和高 比如说320×240
		int requestwidth = 800;
		int requestheight = 600;

		// isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
		opt.inSampleSize = 1;
		// 根据屏的大小和图片大小计算出缩放比例
		if (picWidth > requestwidth || picHeight > requestheight) {
			if (picWidth > picHeight) {// 横着的图片
				opt.inSampleSize = picWidth / requestwidth;
			} else {// 竖着的图片或正方形图片
				opt.inSampleSize = picHeight / requestheight;
			}
		}
		System.out.println(opt.inSampleSize);
		// 这次再真正地生成一个有像素的，分辨率经过处理的bitmap
		opt.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeResource(re, id, opt);
		iv.setImageBitmap(bitmap);
	}
}
