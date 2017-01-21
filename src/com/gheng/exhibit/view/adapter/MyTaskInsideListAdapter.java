package com.gheng.exhibit.view.adapter;

import java.util.List;

import com.gheng.exhibit.http.body.response.MyTaskContent.GroupcontentBean;
import com.smartdot.wenbo.huiyi.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MyTaskInsideListAdapter extends BaseAdapter{
	private Context context;
	private List<GroupcontentBean> data;
	
	public  MyTaskInsideListAdapter(Context context,List<GroupcontentBean> data) {
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
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.mytask_list_item, null);
			holder.tv_num = (TextView) convertView.findViewById(R.id.mytask_list_tv_num);
			holder.tv_content = (TextView) convertView.findViewById(R.id.mytask_list_tv_content);
			holder.cb = (CheckBox) convertView.findViewById(R.id.mytask_list_cb);
		    convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ViewHolder viewHolder = holder;
		holder.tv_num.setText((position+1)+".");
		holder.tv_content.setText(data.get(position).getContent());
		if (data.get(position).isChecked()){
            holder.cb.setChecked(true);
            holder.cb.setEnabled(false);
        }else {
            holder.cb.setChecked(false);
        }
		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//              Toast.makeText(context, "选中了："+position+",内容："+data.get(position).getContent(), Toast.LENGTH_LONG).show();
				if (isChecked) {
					showCustomDialog(context,position,viewHolder);
				}
				
			}
		});
		
		return convertView;
	}
	public void showCustomDialog(final Context context,final int position,final ViewHolder holder) {
		 //将布局文件转为View
		View customDialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
		//对话框
		final Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.getWindow().setContentView(customDialogView);
		//确认button
		Button btn_ok = (Button) customDialogView.findViewById(R.id.dialog_ok);
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "确认完成该任务-->"+data.get(position).getContent(), Toast.LENGTH_LONG).show();
				holder.cb.setChecked(true);
				data.get(position).setChecked(true);
				holder.cb.setEnabled(false);
				dialog.dismiss();
			}
		});
		//取消Button
		Button btn_cancel = (Button) customDialogView.findViewById(R.id.dialog_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.cb.setChecked(false);
				dialog.dismiss();
			}
		});
	}
   class ViewHolder{
	   TextView tv_num,tv_content;
	   CheckBox cb;
	   
   }
}
