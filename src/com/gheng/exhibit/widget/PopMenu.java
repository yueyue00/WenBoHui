package com.gheng.exhibit.widget;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gheng.exhibit.utils.DipUtils;
import com.smartdot.wenbo.huiyi.R;


/**
 * 
 * @author ly
 * 
 */
public class PopMenu {

	private ArrayList<String> itemList;
	private ArrayList<Integer> icons;
	
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;

	@SuppressWarnings("deprecation")
	public PopMenu(Context context) {
		this.context = context;

		itemList = new ArrayList<String>(5);
		icons = new ArrayList<Integer>(5);

		View view = LayoutInflater.from(context)
				.inflate(R.layout.popmenu, null);
		view.getBackground().setAlpha(240);

		listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(new PopAdapter());
		listView.setFocusableInTouchMode(true);
		listView.setFocusable(true);

		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
	}

	public void addItem(String item,int icon) {
		itemList.add(item);
		icons.add(icon);
	}

	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent,
				10,
				DipUtils.dip2px(parent.getContext(), 5));
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	private final class PopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.pomenu_item, null);
				holder = new ViewHolder();

				convertView.setTag(holder);

				holder.groupItem = (TextView) convertView
						.findViewById(R.id.textView);
				holder.iv = (ImageView) convertView.findViewById(R.id.iv);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.iv.setImageResource(icons.get(position));
			holder.groupItem.setText(itemList.get(position));
			return convertView;
		}
		
		private final class ViewHolder {
			TextView groupItem;
			ImageView iv;
		}
	}
}
