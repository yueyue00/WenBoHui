package com.gheng.exhibit.view.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VipBaseInfoAdapter extends BaseAdapter{
	
	private Context context;
	private List<String> kvInfoList;
	private List<String> jbxxInfoList;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	

	public VipBaseInfoAdapter(Context context, List<String> kvInfoList,
			List<String> jbxxInfoList) {
		super();
		this.context = context;
		this.kvInfoList = kvInfoList;
		this.jbxxInfoList = jbxxInfoList;
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.a_huiyiricheng_guesticon)
		.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
		.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return kvInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return kvInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VipBaseInfoHolder holder = null;
		if (convertView == null) {
			holder = new VipBaseInfoHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.vipinfo_list_item, null);
			holder.tv_left = (TextView) convertView.findViewById(R.id.vipinfo_list_item_tvLeft);
			holder.tv_right = (TextView) convertView.findViewById(R.id.vipinfo_list_item_tvRight);
			holder.linear_container = (LinearLayout) convertView.findViewById(R.id.vipinfo_list_item_linear);
		    convertView.setTag(holder);
		}else {
			holder = (VipBaseInfoHolder) convertView.getTag();
		}
		    String rightString = jbxxInfoList.get(position);
		
			holder.tv_left.setText(kvInfoList.get(position)+":");
			holder.tv_right.setText(jbxxInfoList.get(position));
		
		final int index = position;
		holder.tv_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			if (jbxxInfoList.get(index).matches("1[3578]\\d{9}")) {
					   Intent intent = new Intent();
					   intent.setAction(Intent.ACTION_DIAL);
			           intent.setData(Uri.parse("tel:"+jbxxInfoList.get(index)));
			           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			           context.startActivity(intent);
				}
				
			}
		});
		
		 
		
		return convertView;
	}
	class VipBaseInfoHolder{
	TextView tv_left,tv_right;
	LinearLayout linear_container;
	}

}
