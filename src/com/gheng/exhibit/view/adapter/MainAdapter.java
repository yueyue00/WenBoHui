package com.gheng.exhibit.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * @author lileixing
 */
public class MainAdapter extends AbstractEntityAdapter<Integer> {

	private boolean fromMap = false;

	// 宽度铺满还是高度铺满屏幕
	private boolean isWidth;

	public MainAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_main, null);
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
	public Integer getItem(int position) {
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
	protected void setHolderValue(Object holder, Integer data) {
		ViewHoler obj = (ViewHoler) holder;	
		BitmapDrawable photophoto=(BitmapDrawable)obj.iv.getDrawable();//获取 ImageView上的图像对象
		obj.iv.setImageBitmap(null);//清空ImageView
		if(photophoto!=null){//判断BitmapDrawable是否为空
			Bitmap photo=photophoto.getBitmap();//转换成Bitmap
			if(photo!=null){
				photo.recycle();//释放资源
				System.gc();
			}
		}

		compressPhotoAndSetPhoto(this.context.getResources(),data,obj.iv);//重新设置图片
		
	}

	class ViewHoler {
		@ViewInject(R.id.iv)
		ImageView iv;
	}

	/**
	 * @author  马晓勇
	 * 处理高分辨率轮播图片，并显示在控件上 防止OOM
	 * Resources 应用资源库
	 * Integer 文件id
	 * ImageView 展示控件
	 */
	public void compressPhotoAndSetPhoto(Resources re,Integer id,ImageView iv){		
		Bitmap bitmap=null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		//inJustDecodeBounds如果设置为true，解码器将返回null（没有位图的bitmap对象）
		//但是调用者仍然可以查询位图的实际像素，系统并没有为位图像素分配内存
		opt.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeResource(re,id,opt);

		// 获取到这个图片的原始宽度和高度
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		// 期望的图片宽和高  比如说320×240
		int requestwidth = 800;
		int requestheight = 600;

		// isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
		opt.inSampleSize = 1;
		// 根据屏的大小和图片大小计算出缩放比例
		if(picWidth>requestwidth||picHeight>requestheight){
			if(picWidth>picHeight){//横着的图片
				opt.inSampleSize=picWidth/requestwidth;
			}else{//竖着的图片或正方形图片
				opt.inSampleSize=picHeight/requestheight;
			}
		}
		System.out.println(opt.inSampleSize);
		// 这次再真正地生成一个有像素的，分辨率经过处理的bitmap
		opt.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeResource(re,id,opt);
		iv.setImageBitmap(bitmap);
	}
}
