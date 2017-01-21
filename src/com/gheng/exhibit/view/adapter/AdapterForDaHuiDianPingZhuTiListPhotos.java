package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gheng.exhibit.view.checkin.checkin.AddDaHuiDianPingZhuTiActivity;
import com.gheng.exhibit.view.checkin.checkin.ShowDaHuiDianPingPhotosActivity;
import com.hebg3.mxy.utils.PhotoInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForDaHuiDianPingZhuTiListPhotos extends RecyclerView.Adapter<ViewHolder> {

	ArrayList<PhotoInfo> list;
	Context cont;
	LayoutInflater lf;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	public AdapterForDaHuiDianPingZhuTiListPhotos(ArrayList<PhotoInfo> list,Context cont) {
		this.list = list;
		this.cont=cont;
		this.lf=LayoutInflater.from(cont);
		
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.imageloading)
				.showImageOnLoading(R.drawable.imageloading)
				// 加载时显示图片
				.showImageOnFail(R.drawable.imageloading)
				// 加载失败时显示缺省图片
				.imageScaleType(ImageScaleType.EXACTLY)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)//考虑图片拍照时被旋转问题
				.resetViewBeforeLoading(true).build();
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if(cont instanceof AddDaHuiDianPingZhuTiActivity){
	        if(list.size()<6){
				return list.size()+1;
			}else{
				return list.size();
			}
		}else{
			return list.size();
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		if(cont instanceof AddDaHuiDianPingZhuTiActivity){//只有添加主题界面特殊，所以只判断这个
			return new MyViewHolder(this.lf.inflate(R.layout.item_dahuidianping_zhutilist_photos2, vg, false));
		}else{
			return new MyViewHolder(this.lf.inflate(R.layout.item_dahuidianping_zhutilist_photos, vg, false));
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		MyViewHolder mvh=(MyViewHolder)vh;
		if(cont instanceof AddDaHuiDianPingZhuTiActivity){//添加主题界面
			
			mvh.photo.setOnClickListener(new Photoclick(position));//发布主题界面，图片点击事件
			mvh.addpicturebutton.setOnClickListener(new Addphotobutton(position));//发布主题界面，添加图片按钮事件
			mvh.deletephoto.setOnClickListener(new DeletePhotoButton(position));//删除已添加图片监听
			
			if(list.size()==0||(list.size()<6&&position==list.size())){//如果图片数量为0，或图片数量小于6张并且当前是最后一个item  显示添加图片按钮
				mvh.addpicturebutton.setVisibility(View.VISIBLE);
				mvh.photo.setVisibility(View.GONE);
				mvh.deletephoto.setVisibility(View.GONE);
			}else{
				mvh.addpicturebutton.setVisibility(View.GONE);
				mvh.photo.setVisibility(View.VISIBLE);
				mvh.deletephoto.setVisibility(View.VISIBLE);
				imageLoader.displayImage(list.get(position).photourl,mvh.photo,options);//这里用photourl展示，因为有file:// 这里是新增主题界面
			}
		}else{//大会点评及详情及“我的”界面 使用
			mvh.itemView.setOnClickListener(new Photoclick(position));
			imageLoader.displayImage(list.get(position).smallphotourl,mvh.photo,options);
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView photo;//图片展示控件
		ImageButton addpicturebutton;
		ImageButton deletephoto;
		
		public MyViewHolder(View itemView) {
			super(itemView);

			photo = (ImageView) itemView.findViewById(R.id.photo);
			addpicturebutton = (ImageButton) itemView.findViewById(R.id.addphotobutton);
			deletephoto = (ImageButton) itemView.findViewById(R.id.deletephoto);
		}
	}
		
	/**
	 * 图片点击事件
	 */
	class Photoclick implements OnClickListener {

		public int position;

		public Photoclick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//获取图片集合数据，启动photoview 展示图集 
			Intent i=new Intent();
			i.setClass(cont, ShowDaHuiDianPingPhotosActivity.class);
			i.putExtra("photolist", list);
			i.putExtra("position", position);
			cont.startActivity(i);
		}
	}
	
	/**
	 * 发布主题界面 添加图片按钮
	 */
	class Addphotobutton implements OnClickListener {

		public int position;

		public Addphotobutton(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//获取图片集合数据，启动photoview 展示图集
			Intent intent=new Intent();
			intent.setClass(cont, MultiImageSelectorActivity.class);
            // 是否显示拍摄图片
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            // 最大可选择图片数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 6);
            // 选择模式
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
            // 已经选择的图片集合传过去
            if(list != null && list.size()>0){
            	ArrayList<String> selectedphotos=new ArrayList<String>();
            	for(int i=0;i<list.size();i++){
            		selectedphotos.add(list.get(i).smallphotourl);//传给选择图片界面使用smallphotourl 没有file:// 传给图片展示界面使用phtoturl有file://
            	}
                intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, selectedphotos);
            }
            ((AddDaHuiDianPingZhuTiActivity)cont).startActivityForResult(intent,100);
			
		}
	}
	
	/**
	 * 发布主题界面 删除已添加的图片按钮
	 */
	class DeletePhotoButton implements OnClickListener {

		public int position;

		public DeletePhotoButton(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			list.remove(position);
			((AddDaHuiDianPingZhuTiActivity)cont).changeShowPhotoLayout();
			notifyDataSetChanged();
		}
	}
}
