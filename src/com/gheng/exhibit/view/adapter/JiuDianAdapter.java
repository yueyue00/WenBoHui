package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.http.body.response.JiuDianList;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class JiuDianAdapter extends RecyclerView.Adapter<ViewHolder> {
	private List<JiuDianList> list;
	private LayoutInflater li;
	private Context context;

	public JiuDianAdapter(ArrayList<JiuDianList> list, Context context) {
		this.list = list;
		this.context = context;
		this.li = LayoutInflater.from(context);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		View v = this.li.inflate(R.layout.item_hotel_rv, vg, false);

		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
		mvh.tv_start.setText(BaseActivity.getLanguageString("入店时间") + ":");
		mvh.tv_end.setText(BaseActivity.getLanguageString("离开时间") + ":");
		String startTime = list.get(position).startTime;
		String endTime = list.get(position).endTime;
		mvh.time_start.setText(startTime);
		mvh.time_end.setText(endTime);
		if (startTime != null) {
			startTime = startTime.substring(0, startTime.lastIndexOf(":"));
			mvh.time_start.setText(startTime);
			endTime = endTime.substring(0, endTime.lastIndexOf(":") );
			mvh.time_end.setText(endTime);
		}
		if (position % 3 == 0) {
			mvh.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang1);
			mvh.time_start.setTextColor(context.getResources().getColor(
					R.color.hong));
			mvh.tv_start.setTextColor(context.getResources().getColor(
					R.color.hong));
			mvh.time_end.setTextColor(context.getResources().getColor(
					R.color.hong));
			mvh.tv_end.setTextColor(context.getResources().getColor(
					R.color.hong));
		} else if (position % 3 == 1) {
			mvh.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang2);
			mvh.time_start.setTextColor(context.getResources().getColor(
					R.color.cheng));
			mvh.tv_start.setTextColor(context.getResources().getColor(
					R.color.cheng));
			mvh.time_end.setTextColor(context.getResources().getColor(
					R.color.cheng));
			mvh.tv_end.setTextColor(context.getResources().getColor(
					R.color.cheng));
		} else if (position % 3 == 2) {
			mvh.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_huichang3);
			mvh.time_start.setTextColor(context.getResources().getColor(
					R.color.lv));
			mvh.tv_start.setTextColor(context.getResources().getColor(
					R.color.lv));
			mvh.time_end.setTextColor(context.getResources().getColor(
					R.color.lv));
			mvh.tv_end
					.setTextColor(context.getResources().getColor(R.color.lv));
		}
		mvh.text_jiudianname.setText(BaseActivity.getLanguageString("酒店名称")
				+ ": " + list.get(position).hotelInfo.jiudianname);
		mvh.text_jiudianlocation.setText(BaseActivity.getLanguageString("酒店位置")
				+ ": " + list.get(position).hotelInfo.jiudianlocation);
		mvh.text_roomid.setText(BaseActivity.getLanguageString("房间号") + ": "
				+ list.get(position).hotelInfo.jiudianroomid);
		if (position == 0) {
			mvh.view_huibeijing.setVisibility(View.GONE);
		}
	}

	@Override
	// 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	/**
	 * 开发者需要自己创建ViewHolder类
	 * 并定义item中控件对象，并完成初始化，不同的item（比如header或footer，需要开发者另行自定义ViewHolder）
	 */

	class MyViewHolder extends RecyclerView.ViewHolder {
		@ViewInject(R.id.view_huibeijing)
		View view_huibeijing;
		@ViewInject(R.id.iv_angle)
		ImageView iv_angle;
		@ViewInject(R.id.time_start)
		TextView time_start;
		@ViewInject(R.id.time_end)
		TextView time_end;
		@ViewInject(R.id.text_jiudianname)
		TextView text_jiudianname;
		@ViewInject(R.id.text_roomid)
		TextView text_roomid;
		@ViewInject(R.id.text_jiudianlocation)
		TextView text_jiudianlocation;
		@ViewInject(R.id.tv_start)
		TextView tv_start;
		@ViewInject(R.id.tv_end)
		TextView tv_end;

		public MyViewHolder(View itemView) {
			super(itemView);
			ViewUtils.inject(this, itemView);
		}
	}

	// item点击事件
	class itemClickLinstener implements OnClickListener {

		public int position;

		public itemClickLinstener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {

		}
	}
}
