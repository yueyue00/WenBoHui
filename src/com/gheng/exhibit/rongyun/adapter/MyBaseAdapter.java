package com.gheng.exhibit.rongyun.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected LayoutInflater inflater;
	protected List<T> itemList = new ArrayList<T>();

	public MyBaseAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	/**
	 * 判断数据是否为空
	 * 
	 * @return 为空返回true，不为空返回false
	 */
	public boolean isEmpty() {
		return itemList.isEmpty();
	}

	/**
	 * 在原有的数据上添加新数据
	 * 
	 * @param itemList
	 */
	public void addItems(List<T> itemList) {
		this.itemList.addAll(itemList);
		notifyDataSetChanged();
	}

	/**
	 * 设置为新的数据，旧数据会被清空
	 * 
	 * @param itemList
	 */
	public void changeData(List<T> itemList) {
		this.itemList.clear();
		this.itemList = itemList;
		notifyDataSetChanged();
	}

	/**
	 * 清空数据
	 */
	public void clearItems() {
		itemList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	@Override
	public Object getItem(int i) {
		return itemList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	abstract public View getView(int position, View contentView,
			ViewGroup viewGroup);

}
