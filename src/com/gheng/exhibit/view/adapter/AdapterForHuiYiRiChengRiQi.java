package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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

import com.gheng.exhibit.http.body.response.HYRiChengRiQi;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.checkin.HuiYiXiangQingActivity;
import com.hebg3.mxy.utils.NsTextView;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForHuiYiRiChengRiQi extends
		RecyclerView.Adapter<ViewHolder> {
	private List<HYRiChengRiQi> list;
	private LayoutInflater li;
	private Context context;

	public AdapterForHuiYiRiChengRiQi(ArrayList<HYRiChengRiQi> list,
			Context context) {
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
		View v = this.li.inflate(R.layout.item_recycleview_huiyirichengrq, vg,
				false);

		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
		// 主讲人：textview
		mvh.text_zhujiangren.setText(BaseActivity.getLanguageString("主持人")
				+ "：");
		if (list.get(position).getHeadflag()) {
			mvh.tv.setText(list.get(position).getdate() + "  "
					+ list.get(position).getweek());
			mvh.relative_riqi.setVisibility(View.VISIBLE);
			mvh.view_huibeijing.setVisibility(View.VISIBLE);
		} else {
			mvh.relative_riqi.setVisibility(View.GONE);
			mvh.view_huibeijing.setVisibility(View.GONE);
		}
		if (list.get(position).getIscolor() == 0) {
			mvh.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_date1);
			mvh.tv.setTextColor(context.getResources().getColor(R.color.hong));
		} else if (list.get(position).getIscolor() == 1) {
			mvh.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_date2);
			mvh.tv.setTextColor(context.getResources().getColor(R.color.cheng));
		} else if (list.get(position).getIscolor() == 2) {
			mvh.iv_angle
					.setBackgroundResource(R.drawable.wuzhen_mineroute_date3);
			mvh.tv.setTextColor(context.getResources().getColor(R.color.lv));
		}

		mvh.text_id.isfirsttimeondraw = true;
		mvh.text_id.setText(list.get(position).getMeettingtitle());
		mvh.time.setText(list.get(position).getMeettingtime());
		mvh.textView_zhujiangren.setText(list.get(position)
				.getMeettingtalkman());

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
		View view_huibeijing;
		RelativeLayout relative_riqi;
		TextView text_dahuixiangqing;
		LinearLayout linear_dahuixiangqing;
		TextView tv;
		NsTextView text_id;
		TextView time;
		TextView textView_zhujiangren;
		ImageView iv_angle;
		ImageView image_dahuixiangqing;
		TextView text_zhujiangren;
		View line;

		public MyViewHolder(View itemView) {
			super(itemView);
			view_huibeijing = itemView.findViewById(R.id.view_huibeijing);
			relative_riqi = (RelativeLayout) itemView
					.findViewById(R.id.relative_riqi);
			text_dahuixiangqing = (TextView) itemView
					.findViewById(R.id.text_dahuixiangqing);
			linear_dahuixiangqing = (LinearLayout) itemView
					.findViewById(R.id.linear_dahuixiangqing);
			tv = (TextView) itemView.findViewById(R.id.tv);
			text_id = (NsTextView) itemView.findViewById((int) R.id.text_id);
			time = (TextView) itemView.findViewById(R.id.time);
			textView_zhujiangren = (TextView) itemView
					.findViewById(R.id.textView_zhujiangren);
			iv_angle = (ImageView) itemView.findViewById(R.id.iv_angle);
			image_dahuixiangqing = (ImageView) itemView
					.findViewById(R.id.image_dahuixiangqing);
			text_zhujiangren = (TextView) itemView
					.findViewById(R.id.text_zhujiangren);
			line = itemView.findViewById(R.id.huiyirichengriqi_line);
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
			Intent i = new Intent(context, HuiYiXiangQingActivity.class);
			i.putExtra("pchi_id", list.get(position).getMeettingid());
			context.startActivity(i);
		}
	}
}
