package com.gheng.exhibit.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gheng.exhibit.view.BaseActivity;
import com.smartdot.wenbo.huiyi.R;

/**
 * 会议新闻Viewpage里面会议新闻列表Adapter的内容
 * 
 * @author renzhihua
 */
public class PchiNewsAdapter extends BaseAdapter {

	

	private BaseActivity a;

	public PchiNewsAdapter(BaseActivity a) {
		this.a = a;
	}

	class ViewHolderItem {
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolderItem holder = null;
		if (convertView == null) {
			holder = new ViewHolderItem();
			convertView = a.getLayoutInflater().inflate(R.layout.item_pchinews_relist_details, null, false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderItem) convertView.getTag();
		}
		return convertView;
	}

}
