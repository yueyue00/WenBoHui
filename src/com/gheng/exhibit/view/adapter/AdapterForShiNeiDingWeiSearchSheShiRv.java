package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.map.DingWeiDaoHangActivity;
import com.gheng.exhibit.view.map.ShiNeiDaoHangSearchActivity;
import com.hebg3.mxy.utils.SheShiListPojo;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForShiNeiDingWeiSearchSheShiRv extends RecyclerView.Adapter<ViewHolder>{

	public ArrayList<SheShiListPojo> sheshilist;
	public ShiNeiDaoHangSearchActivity cont;
	public LayoutInflater lf;
	
	public AdapterForShiNeiDingWeiSearchSheShiRv(ShiNeiDaoHangSearchActivity cont,ArrayList<SheShiListPojo> sheshilist){
		this.cont=cont;
		this.sheshilist=sheshilist;
		this.lf=LayoutInflater.from(cont);
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return sheshilist.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		View v=this.lf.inflate(R.layout.item_shineidingweisearch_sheshi_rv, vg, false);
		return new MyViewHolder(v);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new itemClickListener(position));
		MyViewHolder mvh=(MyViewHolder)vh;
		mvh.sheshiname.setText(sheshilist.get(position).sheshiname);
		mvh.sheshilogo.setImageDrawable(this.cont.getResources().getDrawable(sheshilist.get(position).drawableid));
	}
	
	
	class MyViewHolder extends RecyclerView.ViewHolder{

		public ImageView sheshilogo;
		public TextView sheshiname; 
		
		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			sheshilogo=(ImageView)itemView.findViewById(R.id.sheshilogo);
			sheshiname=(TextView)itemView.findViewById(R.id.sheshiname);
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
			if(cont.changjingadapter.nowchoseuitem==-1){
				Toast.makeText(cont, BaseActivity.getLanguageString("请选择酒店"), Toast.LENGTH_SHORT).show();
			}else{
				Intent i = new Intent();
				i.putExtra("openmappathid", cont.changjinglist.get(cont.changjingadapter.nowchoseuitem).roomid);
				i.putExtra("searchpoiid", sheshilist.get(position).sheshinamezh);
				i.putExtra("titletv",cont.changjinglist.get(cont.changjingadapter.nowchoseuitem).roomname);
				i.setClass(cont, DingWeiDaoHangActivity.class);
				cont.startActivity(i);
				cont.finish();
			}
		}
	}
}
