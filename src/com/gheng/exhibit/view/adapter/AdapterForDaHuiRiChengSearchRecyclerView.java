package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gheng.exhibit.rongyun.activity.ContactSearchActivity;
import com.gheng.exhibit.view.checkin.MineAttentionActivity;
import com.gheng.exhibit.view.checkin.checkin.HuiYiRiChengSearchActivity;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForDaHuiRiChengSearchRecyclerView extends RecyclerView.Adapter<ViewHolder>{

	public Context activity;
	public ArrayList<String> historysearchlist;
	public LayoutInflater lf;
	
	public AdapterForDaHuiRiChengSearchRecyclerView(Context activity,ArrayList<String> historysearchlist){
		this.activity=activity;
		this.historysearchlist=historysearchlist;
		this.lf=LayoutInflater.from(activity);
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return historysearchlist.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		View v=this.lf.inflate(R.layout.item_dahuirichengsearchhistoryrecyclerview, vg, false);
		return new MyViewHolder(v);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new itemclicklistener(position));
		MyViewHolder mvh=(MyViewHolder)vh;
		mvh.content.setText(this.historysearchlist.get(position));
		if(position==this.historysearchlist.size()-1){
			mvh.content.setTextColor(activity.getResources().getColor(R.color.blue));
		}else{
			mvh.content.setTextColor(activity.getResources().getColor(R.color.text_details));
		}
	}
	
	class itemclicklistener implements OnClickListener{

		public int position;
		
		public itemclicklistener(int position){
			this.position=position;
		}
		
		@Override
		public void onClick(View v) {//点击事件处理意图：如果存在用户的搜索历史记录，当用户点击某一条记录搜索时，当前界面（变量activity），应当finish，同时把点击的item上的内容，通过setResault()方法回传到上一个界面，上一个界面收到该内容之后，再进行检索操作。
			// TODO Auto-generated method stub
			if(activity instanceof HuiYiRiChengSearchActivity){//如果是大会搜索，调用大会搜索界面方法
				try {
					((HuiYiRiChengSearchActivity)activity).itemclicked(position);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(activity instanceof MineAttentionActivity){//如果是我的关注的搜索，调用我的关注方法
				try {
					((MineAttentionActivity)activity).itemclicked(position);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (activity instanceof ContactSearchActivity) {// 如果是我的关注的搜索，调用我的关注方法
				try {
					((ContactSearchActivity) activity).itemclicked(position);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * item的ViewHolder
	 */
	class MyViewHolder extends RecyclerView.ViewHolder{
		
		TextView  content;

		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			content=(TextView)itemView.findViewById(R.id.content);
		}
	}
}
