package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gheng.exhibit.view.BaseActivity;
import com.hebg3.mxy.utils.DatadownloadPojo;
import com.smartdot.wenbo.huiyi.R;

/**
 * 我的关注的会议详情页面中嘉宾RecyclerView显示内容
 * 
 * @author renzhihua
 */

public class AdapterForPchiDetailsDatadownload extends RecyclerView.Adapter<ViewHolder> {

	public ArrayList<DatadownloadPojo> datadownload_list;// 消息提醒数据集合
	public BaseActivity datadownload_cont;
	public LayoutInflater datadownload_lf;

	public AdapterForPchiDetailsDatadownload(BaseActivity datadownload_cont,
			ArrayList<DatadownloadPojo> datadownload_list) {
		this.datadownload_cont = datadownload_cont;
		this.datadownload_list = datadownload_list;
		this.datadownload_lf = LayoutInflater.from(datadownload_cont);
	}

	@Override
	public int getItemCount() {

		return datadownload_list.size();
	}

	@Override // 这个方法返回item的view 不必考虑复用问题
	public ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {

		View v = this.datadownload_lf.inflate(R.layout.item_pchi_details_datadownload_listrecyclerview, vg, false);
		return new MyViewHolder(v);
	}

	@Override // 这个方法用来实现数据和item的捆绑
	public void onBindViewHolder(ViewHolder vh, int position) {

		vh.itemView.setOnClickListener(new itemClickLinstener(position));// 为item添加点击item事件
		MyViewHolder mvh = (MyViewHolder) vh;// 强转为自定义viewHolder
		// 为控件赋值
		if (datadownload_list.get(position).datadownloadtype == 0) {
			for (int i = 0; i < position; i++) {
				mvh.pchi_details_ziliaotv.setText( i+ "物联网对浙江城市建设的适应性");
			}

			/*
			 * mvh.pchi_details_bv.setText(datadownload_cont.getLanguageString(
			 * "查看"));
			 */

		}

	}

	@Override // 重写这个方法，可以控制每一行item返回的view，可以实现header和footer
	public int getItemViewType(int position) {

		return super.getItemViewType(position);
	}

	/**
	 * 开发者需要自己创建ViewHolder类
	 * 并定义item中控件对象，并完成初始化，不同的item（比如header或footer，需要开发者另行自定义ViewHolder）
	 */
	class MyViewHolder extends RecyclerView.ViewHolder {

		/*
		 * TextView pchi_details_bv;// 资料查看
		 */ TextView pchi_details_ziliaotv;// 下载资料1

		public MyViewHolder(View itemView) {
			super(itemView);
			/*
			 * pchi_details_bv = (TextView)
			 * itemView.findViewById(R.id.pchi_details_bv);
			 */
			pchi_details_ziliaotv = (TextView) itemView.findViewById(R.id.pchi_details_ziliaotv);

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
			// 点击查看按钮后，调转到下载的方法中
			// 首先创建下载DownLoadTask监听事件

			/*
			 * DownLoadTask DownLoadTask_chakan = new DownLoadTask(new
			 * DownLoadListener() {
			 * 
			 * @Override public void onUpdate(int value) {
			 * 
			 * }
			 * 
			 * @Override public void onStart() {
			 * 
			 * }
			 * 
			 * @Override public void onError(String message) {
			 * 
			 * }
			 * 
			 * @Override public void onEnd(String path) { //下载完成后直接打开下载的文件
			 * Intent intent = FileOpenUtil.openFile(path); if(intent != null)
			 * datadownload_cont.startActivity(intent);
			 * 
			 * } }); DownLoadTask_chakan.setPath(
			 * "http://gislab.org.cn/tpl/simplebootx/Gislab/images/tu2.png");
			 * DownLoadTask_chakan.setOutPath(AppTools.getRootPath());
			 * DownLoadTask_chakan.execute(); //这里需要正在下载提示
			 * Toast.makeText(datadownload_cont, "正在下载，请稍等..." + position,
			 * Toast.LENGTH_LONG).show();
			 */

		}
	}
}
