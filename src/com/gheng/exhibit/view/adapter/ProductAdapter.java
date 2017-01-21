package com.gheng.exhibit.view.adapter;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.model.databases.TimeRecordType;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.ApiUtil.ApiCallBack;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.ProgressTools;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lenovo
 * 
 */
public class ProductAdapter extends AbstractEntityAdapter<Product> {

	private boolean isMine;
	
	/**
	 * @param context
	 */
	public ProductAdapter(Context context) {
		super(context);
	}

	@Override
	protected View makeView(int position) {
		return inflater.inflate(R.layout.item_product, null);
	}

	@Override
	protected Object makeHolder() {
		return new ViewHoder();
	}

	/**
	 * @param isMine the isMine to set
	 */
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	
	@Override
	protected void setHolderValue(Object holder, Product data) {
		ViewHoder obj = (ViewHoder) holder;
		obj.tv_name.setText((String)I18NUtils.getValue(data, "name"));
		if(data.company != null){
			obj.tv_company_name.setText((String)I18NUtils.getValue(data.company, "name"));
			obj.tv_exhibit_label.setText(((BaseActivity)context).getLanguageString("展位号")+" : ");
			obj.tv_exhibit.setText(data.company.getStandreferenceNew());
		}else{
			obj.tv_company_name.setText(null);
			obj.tv_exhibit_label.setText(null);
			obj.tv_exhibit.setText(null);
		}
		if(data.getIsfav() == 1){
			obj.iv_fav.setImageResource(R.drawable.fav2);
		}else{
			obj.iv_fav.setImageResource(R.drawable.fav);
		}
		obj.tv_fav.setText(((BaseActivity)context).getLanguageString("收藏"));
		obj.iv_fav.setOnClickListener(new OnClickImpl(data));
		obj.tv_fav.setOnClickListener(new OnClickImpl(data));
		bitmapUtils.display(obj.iv, AppTools.imageChange(data.getLogo()));
	}

	class ViewHoder {
		@ViewInject(R.id.iv)
		ImageView iv;
		@ViewInject(R.id.tv_name)
		TextView tv_name;

		@ViewInject(R.id.tv_company_name)
		TextView tv_company_name;

		@ViewInject(R.id.tv_exhibit_label)
		private TextView tv_exhibit_label;
		@ViewInject(R.id.tv_exhibit)
		private TextView tv_exhibit;
		
		@ViewInject(R.id.iv_fav)
		private ImageView iv_fav;
		@ViewInject(R.id.tv_fav)
		private TextView tv_fav;
	}
	
	class OnClickImpl implements OnClickListener{
		
		private Product product;
		
		private OnClickImpl(Product product){
			this.product = product;
		}

		@Override
		public void onClick(View v) {
			if(!AppTools.isLogin()){
//				UIUtils.startToRegister((BaseActivity)context);
				return;
			}
			int mode = 0;
			if(product.getIsfav() == 0){
				mode = 1;
			}else{
				mode = 0;
			}
			ProgressTools.showDialog(context);
			ApiUtil.postFav(product.getId(), Constant.TYPE_PRODUCT, mode, new ApiCallBack() {
				@Override
				public void callback(Map<TimeRecordType, Integer> map) {
				}
				@Override
				public void callback(boolean success, long id, int type,
						int mode, Object data) {
					ProgressTools.hide();
					String[] strs = {"取消收藏成功","收藏成功"};
					String message = ((BaseActivity)context).getLanguageString(strs[mode]);
					if(success){
						((BaseActivity)context).toastShort(message);
						product.setIsfav(mode);
						
						if(isMine && mode == 0){
							remove(product);
						}
						notifyDataSetChanged();
					}else{
						((BaseActivity)context).toastNetError();
					}
				}
			});
		}
		
	}
}
