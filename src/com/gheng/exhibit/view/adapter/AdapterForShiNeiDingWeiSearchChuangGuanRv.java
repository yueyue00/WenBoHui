package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.SharedData;
import com.hebg3.mxy.utils.ChangJingListPojo;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForShiNeiDingWeiSearchChuangGuanRv extends RecyclerView.Adapter<ViewHolder>{

	public Context cont;
	public ArrayList<ChangJingListPojo> changjinglist;
	public LayoutInflater lf;
	public int nowchoseuitem=-1;//当前用户选择的场景item  跳转实际的室内地图时，通过这个值，获取到changjinglist中这个位置的对象，-1的话提示用户选择
	public int oldchoseitem=-1;//之前用户选择的场景item
	
	public AdapterForShiNeiDingWeiSearchChuangGuanRv(Context cont,ArrayList<ChangJingListPojo> changjinglist){
		this.cont=cont;
		this.changjinglist=changjinglist;
		this.lf=LayoutInflater.from(cont);
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return changjinglist.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		View v=this.lf.inflate(R.layout.item_shineidingwei_search_changuanlist_rv,vg,false);
		return new MyViewHolder(v);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new itemClickListener(position));
		MyViewHolder mvh=(MyViewHolder)vh;
		
		//英文模式下截取-后面的文字
		int isen=this.changjinglist.get(position).roomname.lastIndexOf("-");
		if(isen!=-1&&SharedData.getInt("i18n", Language.ZH)==2){
			mvh.changguanname.setText(this.changjinglist.get(position).roomname.substring(isen+1));
		}else{
			mvh.changguanname.setText(this.changjinglist.get(position).roomname);
		}

		if(this.changjinglist.get(position).ischose==1){//选中 白色字体，蓝色背景
			mvh.changguanname.setTextColor(this.cont.getResources().getColor(R.color.white));
			mvh.changguanname.setBackgroundColor(this.cont.getResources().getColor(R.color.blue));
		}else{//没选中，黑色字体，白色背景
			mvh.changguanname.setTextColor(this.cont.getResources().getColor(R.color.text_details));
			mvh.changguanname.setBackgroundDrawable(this.cont.getResources().getDrawable(R.drawable.heikuangkuang));
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder{

		public TextView changguanname;
		public RelativeLayout tvbglayout;
		
		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			changguanname=(TextView)itemView.findViewById(R.id.changguanname);
			tvbglayout=(RelativeLayout)itemView.findViewById(R.id.tvbglayout);
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
			//用户点击某一场馆之后，该item背景应变色，让用户知道选择了哪一个item
			nowchoseuitem=position;
			if(oldchoseitem!=-1){//不是首次选择
				changjinglist.get(oldchoseitem).ischose=0;
				notifyItemChanged(oldchoseitem);
			}
			changjinglist.get(nowchoseuitem).ischose=1;
			notifyItemChanged(nowchoseuitem);
			oldchoseitem=nowchoseuitem;//最后再更新上次选择item
		}
	}
}
