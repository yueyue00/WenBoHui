package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.IfAttention;
import com.gheng.exhibit.http.body.response.MineAttentionListGet;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class MineAttentionListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	ArrayList<MineAttentionListGet> list;
	private BaseActivity activity;

	@ViewInject(R.id.attention_list_iv)
	private ImageView attention_list_iv;
	

	public MineAttentionListAdapter(BaseActivity activity,ArrayList<MineAttentionListGet> list) {
		mInflater = LayoutInflater.from(activity);
		this.activity = activity;
		this.list=list;

	}
	
	public int getCount() {

		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.item_attention_list, null);

			viewHolder = new ViewHolder();
			viewHolder.attention_tv = (TextView) convertView
					.findViewById(R.id.attention_tv);
			viewHolder.attention_list_iv = (ImageView) convertView
					.findViewById(R.id.attention_list_iv);
			viewHolder.attention_date = (TextView) convertView
					.findViewById(R.id.attention_date);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.attention_tv.setText(list.get(position).meettingtitle);
		viewHolder.attention_date.setText(list.get(position).meettingtime);
		viewHolder.attention_list_iv.setOnClickListener(new OnClickListener() {
			// 创建我的关注列表中image button 的点击事件
			@Override
			public void onClick(View v) {
				// 我的关注列表中image button 的点击之后实现取消的监听事件实现方法

				try {
					loadData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 跳转到请求网络数据方法中

				Toast.makeText(activity, "取消成功", 500);
			}

			/**
			 * 取消关注按钮实现方法，请求网络数据
			 * @throws Exception 
			 */
			private void loadData() throws Exception {
				
				// 获取数据库中保存的用户信息
				User Parent = null;
				try {
					DbUtils db = DbUtils.create(activity);
					Parent = db.findFirst(Selector.from(User.class).where("id", "=","1"));
					db.close();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 访问网络获取数据
				ClientParams client = new ClientParams(); // 创建一个新的Http请求
				client.url = "/meetings.do"; // Http 请求的地址 前面的域名封装好了
				StringBuffer strbuf = new StringBuffer(); // 封装需要请求的字段
				strbuf.append("method=executeAttention&userid="); // 请求的字段名
																	// 要和接口文档保持一致
				strbuf.append(Constant.decode(Constant.key,Parent.getUserId())); // 接口ID
				strbuf.append("&meettingid=");// 会议ID
				strbuf.append(list.get(position).meettingid);
				strbuf.append("&operation="); // 判断是否关注
				strbuf.append("0"); // 关注该会议

				String str = strbuf.toString(); // 转换成String类型
				client.params = str; // 把请求的参数封装到params 这个属性里面

				// 调用对象
				NetTask<IfAttention> net = new NetTask<IfAttention>(hand
						.obtainMessage(), client, IfAttention.class,
						new IfAttention(),activity);

				net.execute(); // 相当于线程的Star方法 开始运行

			}

			/**
			 * 请求网络获取数据成功后，将要进行的操作
			 */
			Handler hand = new Handler() {
				public void handleMessage(android.os.Message msg) {

					if (msg.what == 0) {
						//取消关注，并将本条关注记录从列表中移除
						
						list.remove(position);
						
						MineAttentionListAdapter.this.notifyDataSetChanged();

					} else if (msg.what == 1) {
						Toast.makeText(activity, R.string.network,
								Toast.LENGTH_SHORT).show();
					} else if (msg.what == 2) {
						Toast.makeText(activity, (String) msg.obj,
								Toast.LENGTH_SHORT).show();
					}else if(msg.what == 4){//cookie失效
						BaseActivity.gotoLoginPage(activity);
					}
				}
			};
		});
		return convertView;
	}

	static class ViewHolder {
		/**
		 * 设置时间
		 **/
		TextView attention_date;
		/**
		 * 设置关注图标
		 **/
		ImageView attention_list_iv;
		/**
		 * 设置会议名称
		 **/
		TextView attention_tv;
	}

}
