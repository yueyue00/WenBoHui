package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.ProductType;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 产品类别二级分类
 * 
 * @author lileixing
 */
public class ProductTypeAdapter extends BaseExpandableListAdapter {

	private List<ProductType> datas = new ArrayList<ProductType>();

	private BaseActivity a;

	public ProductTypeAdapter(BaseActivity a) {
		this.a = a;
	}

	@Override
	public int getGroupCount() {
		return datas.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition >= datas.size())
			return 0;
		return datas.get(groupPosition % datas.size()).children.size();
	}

	@Override
	public ProductType getGroup(int groupPosition) {
		return datas.get(groupPosition);
	}

	@Override
	public ProductType getChild(int groupPosition, int childPosition) {
		return datas.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		groupPosition %= datas.size();
		return datas.get(groupPosition).getId();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return datas.get(groupPosition).children.get(childPosition).getId();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolderTitle holder = null;
		if (convertView == null) {
			holder = new ViewHolderTitle();
			convertView = a.getLayoutInflater().inflate(
					R.layout.item_product_type_title, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderTitle) convertView.getTag();
		}

		if (isExpanded) {
			holder.expandView.setImageResource(R.drawable.expand);
		} else {
			holder.expandView.setImageResource(R.drawable.unexpand);
		}
		convertView.setBackgroundColor(Color.TRANSPARENT);
		groupPosition %= datas.size();
		ProductType model = datas.get(groupPosition);
		holder.tv_name.setText((String) I18NUtils.getValue(model, "name"));
		holder.tv_new.setVisibility(View.VISIBLE);
		holder.tv_new.setText(model.getTypecount() + "");
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		groupPosition %= datas.size();
		ViewHolderItem holder = null;
		if (convertView == null) {
			holder = new ViewHolderItem();
			convertView = a.getLayoutInflater().inflate(
					R.layout.item_product_type_child, null);
//			convertView.setLayoutParams(new ListView.LayoutParams(-1, a
//					.getResources().getDimensionPixelSize(
//							R.dimen.pchi_item_height)));
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderItem) convertView.getTag();
		}
		ProductType model = datas.get(groupPosition).children
				.get(childPosition);
		holder.tv.setText((String) I18NUtils.getValue(model, "name"));
		holder.tv_new.setVisibility(View.VISIBLE);
		holder.tv_new.setText(model.getTypecount() + "");
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(List<ProductType> datas) {
		this.datas = datas;
		for(ProductType pt : datas) {
			int total = 0;
			for(ProductType child : pt.children) {
				total = total + child.getTypecount();
			}
			pt.setTypecount(total);
		}
		notifyDataSetChanged();
	}

	class ViewHolderTitle {

		@ViewInject(R.id.iv_row)
		ImageView expandView;
		@ViewInject(R.id.tv_name)
		TextView tv_name;
		@ViewInject(R.id.tv_new)
		TextView tv_new;
	}

	class ViewHolderItem {

		@ViewInject(R.id.tv)
		TextView tv;

		@ViewInject(R.id.tv_new2)
		TextView tv_new;
	}

}
