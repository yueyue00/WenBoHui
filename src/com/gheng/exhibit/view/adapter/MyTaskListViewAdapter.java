package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.Text;
import com.gheng.exhibit.http.body.response.MyTaskContent;
import com.gheng.exhibit.http.body.response.MyTaskContent.GroupcontentBean;
import com.smartdot.wenbo.huiyi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyTaskListViewAdapter extends BaseAdapter{
	private Context context;
	private List<MyTaskContent> data;
	
	
	/**
	 * 构造函数
	 */
	public  MyTaskListViewAdapter (Context context,List<MyTaskContent> data) {
		this.context = context;
		this.data = data;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		 ViewHolder holder = null;
		 if (convertView == null) {
			holder  = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.mytask_list_inside_item, null);
			holder.tv_groupName = (TextView) convertView.findViewById(R.id.mytask_list_tv_groupname);
			holder.listView = (ListView) convertView.findViewById(R.id.mytask_inside_list);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		 holder.tv_groupName.setText(data.get(position).getGroupname());
		 System.out.println("=====size:"+data.get(position).getGroupcontent().size()+data.get(position).getGroupcontent().toString());
		 MyTaskInsideListAdapter adapter = new MyTaskInsideListAdapter(context, data.get(position).getGroupcontent());
		 holder.listView.setAdapter(adapter);
		 setListViewHeightBasedOnChildren(holder.listView);
         return convertView;
	}
   class ViewHolder{
	   TextView tv_groupName;
	   ListView listView;
   }
       /**
        * 解决listview嵌套listview，子listview不显示或者显示不全的问题
        * 需要子listview在设置完adapter后，再调用该方法
        * 并且子listview的item必须是LinearLayout，因为其他layout没有重写onMeasure（）方法
        * @param listView
        */
	    public void setListViewHeightBasedOnChildren(ListView listView) {    
	        ListAdapter listAdapter = listView.getAdapter();     
	        if (listAdapter == null) {    
	               
	            return;    
	        }    
	    
	        int totalHeight = 0;    
	        for (int i = 0; i < listAdapter.getCount(); i++) {    
	            View listItem = listAdapter.getView(i, null, listView);    
	            listItem.measure(0, 0);    
	            totalHeight += listItem.getMeasuredHeight();    
	        }    
	    
	        ViewGroup.LayoutParams params = listView.getLayoutParams();    
	        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));    
	        listView.setLayoutParams(params);    
	    }    
	  
}
