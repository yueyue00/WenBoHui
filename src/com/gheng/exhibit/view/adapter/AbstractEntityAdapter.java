package com.gheng.exhibit.view.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gheng.exhibit.http.BitmapHelper;
import com.gheng.exhibit.utils.TypeFaceUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;

/**
 * 抽象适配器 适用于List<T>类型存储数据的
 * 
 * @author Administrator
 * 
 */
public abstract class AbstractEntityAdapter<T> extends BaseAdapter {

	protected static SimpleDateFormat format_timstamp = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	protected static SimpleDateFormat format_date = new SimpleDateFormat(
			"yyyy-MM-dd");
	protected BitmapUtils bitmapUtils;
	/**
	 * 适配器要展示的数据
	 */
	protected List<T> data;
	/**
	 * 调用Listview的每一项展示
	 */
	protected LayoutInflater inflater;
	/**
	 * 上下文环境，主要是Activity
	 */
	protected Context context;

	public AbstractEntityAdapter(Context context, List<T> data) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		if (data == null) {
			this.data = new ArrayList<T>();
		} else {
			this.data = data;
		}
		bitmapUtils = BitmapHelper.createBitmapUtils(context);
	}

	public AbstractEntityAdapter(Context context, T[] data) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.data = new ArrayList<T>();
		if(data != null){
			for (T t : data) {
				this.data.add(t);
			}
		}
		bitmapUtils = BitmapHelper.createBitmapUtils(context);
	}
	
	public AbstractEntityAdapter(Context context) {
		this(context, new ArrayList<T>());
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object holder = null;
		if(convertView == null){
			convertView = makeView(position);
			holder = makeHolder();
			ViewUtils.inject(holder,convertView);
			TypeFaceUtils.setTypeValue(holder);
			convertView.setTag(holder);
		}else{
			holder = convertView.getTag();
		}
		setHolderValue(holder,getItem(process(position)));
		return convertView;
	}
	/**
	 * 位置处理
	 * 		便于循环时使用
	 */
	protected int process(int position){
		return position;
	}
	/**
	 * 构造view
	 */
	protected abstract View makeView(int position);
	/**
	 * 构造holder
	 */
	protected abstract Object makeHolder();
	/**
	 * holder里面设置值
	 */
	protected abstract void setHolderValue(Object holder,T data);

	/**
	 * 添加Listview数据
	 * 
	 * @param item
	 */
	public void add(T item) {
		data.add(item);
		notifyDataSetChanged();
	}

	public void add(List<T> data) {
		this.data.addAll(data);
		notifyDataSetChanged();
	}

	public void add(T[] data) {
		for (T t : data) {
			this.data.add(t);
		}
		notifyDataSetChanged();
	}

	public void setData(List<T> data) {
		this.data.clear();
		this.data.addAll(data);
		notifyDataSetChanged();
	}

	public void setData(T[] data) {
		this.data.clear();
		for (T t : data) {
			this.data.add(t);
		}
		notifyDataSetChanged();
	}

	/**
	 * 删除Listview数据
	 * 
	 * @param item
	 */
	public void remove(T item) {
		data.remove(item);
		notifyDataSetChanged();
	}

	/**
	 * 根据位置删除数据
	 * 
	 * @param position
	 */
	public void remove(int position) {
		data.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * 清空数据
	 */
	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}

	public List<T> getData() {
		return data;
	}
}
