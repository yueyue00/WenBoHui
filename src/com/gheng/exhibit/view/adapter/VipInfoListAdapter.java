package com.gheng.exhibit.view.adapter;

import io.rong.imkit.RongIM;

import java.util.List;

import com.gheng.exhibit.http.body.response.VipInfoListBaen;
import com.gheng.exhibit.http.body.response.VipInfoListBaen.InfoBean;
import com.gheng.exhibit.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VipInfoListAdapter extends BaseAdapter{
	
	private Context context;
	private List<InfoBean> data;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = null;
	
	

	public VipInfoListAdapter(Context context, List<InfoBean> data) {
		super();
		this.context = context;
		this.data = data;
		options = new DisplayImageOptions.Builder()
				  .showStubImage(R.drawable.a_huiyiricheng_guesticon)
				  .showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				  .showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				  .cacheInMemory(true)
				  .cacheOnDisc(true)
				  .bitmapConfig(Bitmap.Config.RGB_565)
				  .displayer(new CircleBitmapDisplayer())
		          .build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.vipinfolist_item,null);
			holder.tv_name  = (TextView) convertView.findViewById(R.id.vil_tv_name);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.vil_ivicon);
			holder.btn_call = (Button) convertView.findViewById(R.id.vil_btn_call);
			holder.btn_sendinfo = (Button) convertView.findViewById(R.id.vil_btn_sendinfo);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_name.setText(data.get(position).getUsername());
		imageLoader.displayImage(data.get(position).getPhotourl(), holder.iv_icon, options);
		final int index = position;
		holder.btn_call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            String mobile = data.get(index).getMobile();
            if (mobile != null && !mobile.equals("") ) {
            	intent.setData(Uri.parse("tel:"+mobile));
			}else {
				intent.setData(Uri.parse("tel:10086"));
			}
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
				
			}
		});
		
		holder.btn_sendinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                System.out.println("=========data.get(index).getMap().getRy_userId()==="+data.get(index).getMap().getRy_userId());
				/**
				 * 跳转融云单聊界面
				 */
				if (data.get(index).getMap().getRy_userId() != null
						&& RongIM.getInstance() != null) {
					try {
						RongIM.getInstance().startPrivateChat(context,
								data.get(index).getMap().getRy_userId(),
								data.get(index).getUsername());
					} catch (Exception e) {
					}
				}
			}
		});
		
		return convertView;
	}
class ViewHolder{
	TextView tv_name;
	ImageView iv_icon;
	Button btn_call , btn_sendinfo;
	
}
}
