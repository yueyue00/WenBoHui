package com.gheng.exhibit.view.adapter;

import io.rong.imkit.RongIM;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.ZhanGuan;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class AdapterZhanShangSJ extends RecyclerView.Adapter<ViewHolder> {
	private ArrayList<ZhanGuan> list = new ArrayList<ZhanGuan>();
	private LayoutInflater li;
	private Context context;

	public AdapterZhanShangSJ(ArrayList<ZhanGuan> list, Context context) {
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
		View v = this.li.inflate(R.layout.item_recycleview_zhanshangsj, vg,
				false);

		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
		if (position % 3 == 0) {
			mvh.iv_zhanshang
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao1);
		} else if (position % 3 == 1) {
			mvh.iv_zhanshang
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao2);
		} else if (position % 3 == 2) {
			mvh.iv_zhanshang
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao3);
		}
		mvh.tv_zhanshang.setText(list.get(position).name);
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
		@ViewInject(R.id.iv_zhanshang)
		ImageView iv_zhanshang;
		@ViewInject(R.id.tv_zhanshang)
		TextView tv_zhanshang;

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

			// if (context instanceof ZhanShangSJActivity) {
			// Intent intent = new Intent(context, ZhanGuanActivity.class);
			// intent.putExtra("title", list.get(position).name);
			// intent.putExtra("id", list.get(position).id);
			// context.startActivity(intent);
			// } else if (context instanceof ZhanGuanActivity) {
			// // String url = Constant.DOMAIN
			// // + "/InfoPublish.do?method=viewInfo&language=1&infoid=27700";
			// Intent intent = new Intent(context, ShowWebViewActivity.class);
			// intent.putExtra("title", list.get(position).name);
			// intent.putExtra("url", list.get(position).newsurl);
			// context.startActivity(intent);
			// } else if (context instanceof JiaBinFuWuActivity) {
			// Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
			// + "18810126510"));
			// context.startActivity(intent);
			//
			// }
			if (list.get(position).name.equals("文博会通讯录")) {
				refreshData();
			} else if (list.get(position).name.equals("会场服务群组")) {
				RongIM.getInstance().startGroupChat(context, "2016061311310001", "会场服务群组");
			} else if (list.get(position).name.equals("宾客迎送组")) {
				refreshData1();
			} else if (list.get(position).name.equals("报到及日程安排组")) {
				refreshData2();
			} else if (list.get(position).name.equals("张文哲")) {
				RongIM.getInstance().startPrivateChat(context, "fw_zhangwz", "张文哲");
			} else if (list.get(position).name.equals("满爽")) {
				RongIM.getInstance().startPrivateChat(context, "fw_mans", "满爽");
			} else if (list.get(position).name.equals("王思羽")) {
				RongIM.getInstance().startPrivateChat(context, "fw_wangsy", "王思宇");
			} else if (list.get(position).name.equals("李彪")) {
				RongIM.getInstance().startPrivateChat(context, "fw_lib", "李彪");
			} else if (list.get(position).name.equals("牛惠勇")) {
				RongIM.getInstance().startPrivateChat(context, "fw_niuhy", "牛惠勇");
			} else if (list.get(position).name.equals("殷萌豪")) {
				RongIM.getInstance().startPrivateChat(context, "fw_yinmh", "殷萌豪");
			} else if (list.get(position).name.equals("马春龙")) {
				RongIM.getInstance().startPrivateChat(context, "fw_macl", "马春龙");
			} else if (list.get(position).name.equals("赵明")) {
				RongIM.getInstance().startPrivateChat(context, "fw_zhaom", "赵明");
			} else if (list.get(position).name.equals("毛颖辉")) {
				RongIM.getInstance().startPrivateChat(context, "fw_maoyh", "毛颖辉");
			}else if(list.get(position).name.equals("张文哲 ")){
				RongIM.getInstance().startPrivateChat(context, "fw_zhangwz", "张文哲");
			}else if(list.get(position).name.equals("王思羽 ")){
				RongIM.getInstance().startPrivateChat(context, "fw_wangsy", "王思羽");
			}else if(list.get(position).name.equals("满爽 ")){
				RongIM.getInstance().startPrivateChat(context, "fw_mans", "蛮爽");
			}
		}
	}

	private void refreshData() {
		ArrayList<ZhanGuan> fuwudiaodu = new ArrayList<ZhanGuan>();
		ZhanGuan z1 = new ZhanGuan();
		z1.setName("宾客迎送组");
		ZhanGuan z2 = new ZhanGuan();
		z2.setName("报到及日程安排组");
		ZhanGuan z3 = new ZhanGuan();
		z3.setName("食宿安排组");
		ZhanGuan z4 = new ZhanGuan();
		z4.setName("交通保障组");
		ZhanGuan z5 = new ZhanGuan();
		z5.setName("会场服务组");
		ZhanGuan z6 = new ZhanGuan();
		z6.setName("证件印制组");
		ZhanGuan z7 = new ZhanGuan();
		z7.setName("材料编印组");
		ZhanGuan z8 = new ZhanGuan();
		z8.setName("会议礼品组");

		fuwudiaodu.add(z1);
		fuwudiaodu.add(z2);
		fuwudiaodu.add(z3);
		fuwudiaodu.add(z4);
		fuwudiaodu.add(z5);
		fuwudiaodu.add(z6);
		fuwudiaodu.add(z7);
		fuwudiaodu.add(z8);

		list.clear();
		list.addAll(fuwudiaodu);
		this.notifyDataSetChanged();
	}

	private void refreshData1() {
		ArrayList<ZhanGuan> fuwudiaodu = new ArrayList<ZhanGuan>();
		ZhanGuan z1 = new ZhanGuan();
		z1.setName("赵明");
		ZhanGuan z2 = new ZhanGuan();
		z2.setName("李彪");
		ZhanGuan z3 = new ZhanGuan();
		z3.setName("毛颖辉");
		ZhanGuan z4 = new ZhanGuan();
		z4.setName("王思羽");
		ZhanGuan z5 = new ZhanGuan();
		z5.setName("满爽");
		ZhanGuan z6 = new ZhanGuan();
		z6.setName("牛惠勇");
		ZhanGuan z7 = new ZhanGuan();
		z7.setName("马春龙");
		ZhanGuan z8 = new ZhanGuan();
		z8.setName("张文哲");

		fuwudiaodu.add(z1);
		fuwudiaodu.add(z2);
		fuwudiaodu.add(z3);
		fuwudiaodu.add(z4);
		fuwudiaodu.add(z5);
		fuwudiaodu.add(z6);
		fuwudiaodu.add(z7);
		fuwudiaodu.add(z8);

		list.clear();
		list.addAll(fuwudiaodu);
		this.notifyDataSetChanged();
	}

	private void refreshData2() {
		ArrayList<ZhanGuan> fuwudiaodu = new ArrayList<ZhanGuan>();
		ZhanGuan z1 = new ZhanGuan();
		z1.setName("李彪");
		ZhanGuan z2 = new ZhanGuan();
		z2.setName("牛惠勇");
		ZhanGuan z3 = new ZhanGuan();
		z3.setName("殷萌豪");
		ZhanGuan z4 = new ZhanGuan();
		z4.setName("赵跃俊");

		fuwudiaodu.add(z1);
		fuwudiaodu.add(z2);
		fuwudiaodu.add(z3);
		fuwudiaodu.add(z4);

		list.clear();
		list.addAll(fuwudiaodu);
		this.notifyDataSetChanged();
	}
}
