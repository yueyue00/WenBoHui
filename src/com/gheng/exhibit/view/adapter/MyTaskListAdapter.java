package com.gheng.exhibit.view.adapter;

import java.util.List;

import com.gheng.exhibit.http.body.response.CommonVip;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean.InfoBean.JiabinBean;
import com.gheng.exhibit.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyTaskListAdapter extends BaseAdapter {

	private Context context;
	private List<JiabinBean> data;

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = null;

	public MyTaskListAdapter(Context context, List<JiabinBean> data) {
		super();
		this.context = context;
		this.data = data;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer()).build();
	}

	@Override
	public int getCount() {
		if (data != null) {

			return data.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder holder = null;
		if (convertView == null) {
			holder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.mytasklist_item, null);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.mtl_tv_name);
			holder.iv_icon = (ImageView) convertView
					.findViewById(R.id.mtl_iv_icon);

			holder.iv_yuzhuce = (ImageView) convertView
					.findViewById(R.id.mtl_iv_yuzhuce);
			holder.iv_jieji = (ImageView) convertView
					.findViewById(R.id.mtl_iv_jieji);
			holder.iv_qianbaodao = (ImageView) convertView
					.findViewById(R.id.mtl_iv_qbd);
			holder.iv_banruzhu = (ImageView) convertView
					.findViewById(R.id.mtl_iv_brz);
			holder.iv_lingziliao = (ImageView) convertView
					.findViewById(R.id.mtl_iv_lzl);
			holder.iv_kanzhanlan = (ImageView) convertView
					.findViewById(R.id.mtl_iv_kzl);
			holder.iv_songji = (ImageView) convertView
					.findViewById(R.id.mtl_iv_songji);

			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}
		JiabinBean jiabinBean = data.get(position);
		imageLoader.displayImage(jiabinBean.getSmall_icon(), holder.iv_icon,
				options);
		holder.tv_name.setText(jiabinBean.getName());
		// 预注册
		if (jiabinBean.getYuzhuce().equals("1")) {
			holder.iv_yuzhuce.setImageResource(R.drawable.mytask_btn_yzc_sel);
		} else {
			holder.iv_yuzhuce.setImageResource(R.drawable.mytask_btn_yzc_nor);
		}
		// 接机
		if (jiabinBean.getJieji().equals("1")) {
			holder.iv_jieji.setImageResource(R.drawable.ic_mt_jieji_highlight);
		} else {
			holder.iv_jieji.setImageResource(R.drawable.ic_mt_jieji);
		}
		// 签报到
		// if (jiabinBean.getQianbaodao().equals("1")) {
		// holder.iv_qianbaodao.setImageResource(R.drawable.ic_mt_baodao_highlight);
		// }else {
		// holder.iv_qianbaodao.setImageResource(R.drawable.ic_mt_baodao);
		// }
		// 办入住
		if (jiabinBean.getBanruzhu().equals("1")) {
			holder.iv_banruzhu
					.setImageResource(R.drawable.ic_mt_ruzhu_highlight);
		} else {
			holder.iv_banruzhu.setImageResource(R.drawable.ic_mt_ruzhu);
		}
		// //领资料
		// if (jiabinBean.getLingziliao().equals("1")) {
		// holder.iv_lingziliao.setImageResource(R.drawable.ic_mt_data_highlight);
		// }else {
		// holder.iv_lingziliao.setImageResource(R.drawable.ic_mt_data);
		// }

		// 看展览
		if (jiabinBean.getKanzhanlan().equals("1")) {
			holder.iv_kanzhanlan
					.setImageResource(R.drawable.btn_mytask_kanzhanlan_hlghlight);
		} else {
			holder.iv_kanzhanlan
					.setImageResource(R.drawable.btn_mytask_kanzhanlan_noaml);
		}
		// 送机
		if (jiabinBean.getSongji().equals("1")) {
			holder.iv_songji
					.setImageResource(R.drawable.ic_mt_songji_highlight);
		} else {
			holder.iv_songji.setImageResource(R.drawable.ic_mt_songji);
		}

		return convertView;
	}

	class Viewholder {
		TextView tv_name;
		ImageView iv_icon, iv_jieji, iv_qianbaodao, iv_lingziliao, iv_banruzhu,
				iv_songji, iv_yuzhuce;
		ImageView iv_kanzhanlan;
	}
}
