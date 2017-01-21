package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.map.DingWeiDaoHangActivity;
import com.hebg3.mxy.utils.ChangJingListPojo;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForShiNeiDingWeiShuJuRecyclerView extends RecyclerView.Adapter<ViewHolder> {

	public ArrayList<ChangJingListPojo> changjinglist;//场景数据集合
	public Context cont;
	public LayoutInflater lf;
	
	
	public AdapterForShiNeiDingWeiShuJuRecyclerView(Context cont,ArrayList<ChangJingListPojo> changjinglist){
		this.cont=cont;
		this.changjinglist=changjinglist;
		this.lf=LayoutInflater.from(cont);
		System.out.println(changjinglist.size());
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return changjinglist.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		View v=this.lf.inflate(R.layout.item_shineidingwei_list_recyclerview,vg,false);
		return new MyViewHolder(v);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new itemClickListener(position));
		MyViewHolder mvh=(MyViewHolder)vh;
		mvh.photo.setImageDrawable(this.cont.getResources().getDrawable(changjinglist.get(position).drawableid));
		
		//英文模式下截取-后面的文字
		int isen=this.changjinglist.get(position).roomname.lastIndexOf("-");
		if(isen!=-1&&SharedData.getInt("i18n", Language.ZH)==2){
			mvh.name.setText(this.changjinglist.get(position).roomname.substring(isen+1));
		}else{
			mvh.name.setText(this.changjinglist.get(position).roomname);
		}
	}
	
	/**
	 * 自定义ViewHolder
	 */
	class MyViewHolder extends RecyclerView.ViewHolder{

		ImageView photo;//场景图片
		TextView name;//场景名称
		
		public MyViewHolder(View itemView) {
			super(itemView);
			photo=(ImageView)itemView.findViewById(R.id.photo);
			name=(TextView)itemView.findViewById(R.id.name);
		}
	}
	
	class itemClickListener implements OnClickListener{

		public int position;
		
		public itemClickListener(int position){
			this.position=position;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(changjinglist.get(position).type==0){//会场场景
				Intent i = new Intent();
				i.putExtra("openmappathid", changjinglist.get(position).roomid);// 枕水酒店id
				i.putExtra("openfloornum", changjinglist.get(position).floor);// 打开宴会厅所在楼层
				i.putExtra("searchspacename", changjinglist.get(position).roomnamezh);// 搜索space 必须要传roomnamezh 中文
				i.putExtra("titletv",changjinglist.get(position).roomname);
				i.setClass(cont, DingWeiDaoHangActivity.class);
				cont.startActivity(i);
			}
			if(changjinglist.get(position).type==1){//宴会厅场景
				Intent i = new Intent();
				i.putExtra("openmappathid", changjinglist.get(position).roomid);// 枕水酒店id
				i.putExtra("openfloornum", changjinglist.get(position).floor);// 打开宴会厅所在楼层
				i.putExtra("searchspacename", changjinglist.get(position).roomnamezh);// 搜索space 必须要传roomnamezh 中文
				i.putExtra("titletv",changjinglist.get(position).roomname);
				i.setClass(cont, DingWeiDaoHangActivity.class);
				cont.startActivity(i);
			}
			if(changjinglist.get(position).type==2){//酒店场景
				Intent i=new Intent();
				i.putExtra("openmappathid",changjinglist.get(position).roomid);
				i.putExtra("titletv",changjinglist.get(position).roomname);
				i.setClass(cont,DingWeiDaoHangActivity.class);
				cont.startActivity(i);
			}
		}
	}
}
