package com.gheng.exhibit.view.adapter;

import io.rong.imkit.RongIM;

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

import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.FWMainActivity;
import com.gheng.exhibit.view.MainActivity;
import com.gheng.exhibit.view.SiXinActivity;
import com.gheng.exhibit.view.checkin.HuiYiXiangQingActivity;
import com.gheng.exhibit.view.checkin.XingChengAnPaiActivity;
import com.gheng.exhibit.view.checkin.checkin.ShowWebViewActivity;
import com.gheng.exhibit.view.viplist.FuWuSiXingActivity;
import com.hebg3.mxy.utils.XiaoXiPojo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

public class AdapterForXiaoXiZhongXin extends RecyclerView.Adapter<ViewHolder> {

	public ArrayList<XiaoXiPojo> list;// 消息提醒数据集合
	public Context cont;
	public LayoutInflater lf;
	User user;

	public AdapterForXiaoXiZhongXin(Context cont, ArrayList<XiaoXiPojo> list) {
		this.cont = cont;
		this.list = list;
		this.lf = LayoutInflater.from(cont);
		// 查找
		try {
			DbUtils db = DbUtils.create(cont);
			user = db
					.findFirst(Selector.from(User.class).where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	// 这个方法返回item的view 不必考虑复用问题
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
		// TODO Auto-generated method stub
		View v = null;
		if (cont instanceof MainActivity || cont instanceof FWMainActivity) {// 主界面的消息
			v = this.lf.inflate(
					R.layout.item_xiaoxizhongxinlistrecyclerview_mainpage, vg,
					false);
		} else {
			v = this.lf.inflate(R.layout.item_xiaoxizhongxinlistrecyclerview,
					vg, false);
			// if (cont instanceof MainActivity) {
			// v.setBackgroundColor(cont.getResources().getColor(
			// R.color.mainactivitybg));
			// }
		}
		return new MyViewHolder(v);
	}

	@Override
	// 这个方法用来实现数据和item的捆绑
	public void onBindViewHolder(ViewHolder vh, int position) {
		// TODO Auto-generated method stub
		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder

		// 重要说明：为了保证布局美观，所以受到的数据，要进行截取
		String sx = "";
		if (list.get(position).messagetype.equals("0")) {
			mvh.xiaoxitypelogo.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.wuzhen_infocenter));
		}
		if (list.get(position).messagetype.equals("1")) {
			mvh.xiaoxitypelogo.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.wuzhen_infocenter_personalinfo));
			sx = list.get(position).servicename + ":";
		}
		if (list.get(position).messagetype.equals("2")) {
			mvh.xiaoxitypelogo.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.wuzhen_infocenter_remind));
		}
		if (list.get(position).messagetype.equals("3")) {// 其他消息
			mvh.xiaoxitypelogo.setImageDrawable(this.cont.getResources()
					.getDrawable(R.drawable.wuzhen_infocenter));
		}

		// if (list.get(position).messagetype.equals("1")) {// 私信不显示时间
		// mvh.time.setVisibility(View.INVISIBLE);
		// } else {
		// mvh.time.setVisibility(View.VISIBLE);
		// }
		// if (cont instanceof MainActivity) {// 属于从MainActivity设置的适配器
		// if (position == 0)
		// mvh.line.setVisibility(View.GONE);
		// }
		mvh.title.setText(sx + list.get(position).title);
		mvh.time.setText(list.get(position).time);
		if (list.get(position).state.equals("0")) {
			mvh.title.setTextColor(cont.getResources().getColor(
					R.color.zuijinsousuo));
		}
		if (list.get(position).state.equals("1")) {
			mvh.title.setTextColor(cont.getResources().getColor(
					R.color.searchhuiyirvfengexian));
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

		ImageView xiaoxitypelogo;// 消息类型图标
		TextView time; // 消息时间
		TextView title;// 消息标题
		View line;

		public MyViewHolder(View itemView) {
			super(itemView);
			xiaoxitypelogo = (ImageView) itemView
					.findViewById(R.id.xiaoxitypelogo);
			title = (TextView) itemView.findViewById(R.id.title);
			time = (TextView) itemView.findViewById(R.id.time);
			line = itemView.findViewById(R.id.xiaoxi_mainpage_line);
		}
	}

	/**
	 * item点击事件
	 */
	class itemClickLinstener implements OnClickListener {

		public int position;

		public itemClickLinstener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (list.get(position).messagetype.equals("0")) {// 会议详情
				list.get(position).state = "1";
				Intent i = new Intent();
				i.setClass(cont, HuiYiXiangQingActivity.class);
				i.putExtra("pchi_id", list.get(position).messageid);
				i.putExtra("suijima", list.get(position).suijima);
				i.putExtra("method", "getSummitWithJpush");
				cont.startActivity(i);
				notifyItemChanged(position);
			}
			if (list.get(position).messagetype.equals("1")) {// 私信
				// if (user.getUserjuese().equals("1")) {
				// Intent i = new Intent();
				// i.setClass(cont, SiXinActivity.class);
				// i.putExtra("serviceid", list.get(position).serviceid);
				// i.putExtra("servicename", list.get(position).servicename);
				// cont.startActivity(i);
				// } else {
				// Intent sixing = new Intent(cont, FuWuSiXingActivity.class);
				// sixing.putExtra("serviceid", list.get(position).serviceid);
				// sixing.putExtra("servicename",
				// list.get(position).servicename);
				// sixing.putExtra("servicephoto",
				// list.get(position).messageurl);
				// cont.startActivity(sixing);
				// }
				// zyj 新加跳转融云聊天界面的通道
				if (list.get(position).messageid.equals("0")) {
					if (list.get(position).serviceid != null
							&& list.get(position).servicename != null
							&& RongIM.getInstance() != null) {
						try {
							RongIM.getInstance().startPrivateChat(cont,
									list.get(position).serviceid,
									list.get(position).servicename);
						} catch (Exception e) {
						}
					}
				} else {
					if (list.get(position).serviceid != null
							&& list.get(position).servicename != null
							&& RongIM.getInstance() != null) {
						try {
							RongIM.getInstance().startGroupChat(cont,
									list.get(position).serviceid,
									list.get(position).servicename);
						} catch (Exception e) {
						}
					}
				}
				notifyItemChanged(position);
			}
			if (list.get(position).messagetype.equals("2")) {// vip行程
				list.get(position).state = "1";
				Intent i = new Intent();
				i.putExtra("tripid", list.get(position).messageid);
				i.setClass(cont, XingChengAnPaiActivity.class);
				cont.startActivity(i);
				notifyItemChanged(position);
			}
			if (list.get(position).messagetype.equals("3")) {// 其他消息 访问url地址
				list.get(position).state = "1";
				Intent i = new Intent();
				i.setClass(cont, ShowWebViewActivity.class);
				i.putExtra("title", BaseActivity.getLanguageString("通知详情"));
				i.putExtra(
						"url",
						list.get(position).messageurl.equals("") ? "http://www.baidu.com"
								: list.get(position).messageurl);
				String aaa = list.get(position).messageurl.equals("") ? "http://www.baidu.com"
						: list.get(position).messageurl;
				System.out.println("aaa:AdapterForXiaoXiZhongXin:url=" + aaa);
				cont.startActivity(i);
				notifyItemChanged(position);
			}
		}
	}
}
