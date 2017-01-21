package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gheng.exhibit.model.databases.SurveyOptions;
import com.gheng.exhibit.utils.I18NUtils;
import com.gheng.exhibit.utils.StringTools;
import com.gheng.exhibit.view.BaseActivity;
import com.smartdot.wenbo.huiyi.R;

public class VoteSubmitListAdapter extends BaseAdapter {

	private List<SurveyOptions> dataItems;
	private BaseActivity mContext;
	private boolean isRadio = false;
	private int maxSelect = 999;
	private long questionId;

	public VoteSubmitListAdapter(BaseActivity context, List<SurveyOptions> dataItems, boolean isRadio) {
		mContext = context;
		this.dataItems = dataItems;
		this.isRadio = isRadio;
	}
	
	public VoteSubmitListAdapter(BaseActivity context, List<SurveyOptions> dataItems, boolean isRadio,int maxSelect,long questionId) {
		mContext = context;
		this.dataItems = dataItems;
		this.isRadio = isRadio;
		this.maxSelect = maxSelect;
		this.questionId = questionId;
	}
	
	public Map<String, String> getSelectResult(){
		if(dataItems != null && dataItems.size() > 0){
			Map<String,String> map = new HashMap<String, String>();
			StringBuffer selectIdBuffer = new StringBuffer();
			String value = null;
			
			for (SurveyOptions option : dataItems) {
				if(option.isSelected()){
					selectIdBuffer.append(option.getId()).append(",");
					if(StringTools.isNotBlank(option.getRemark())){
						value = option.getRemark();
					}
				}
			}
			if(selectIdBuffer.length() > 0){
				selectIdBuffer.deleteCharAt(selectIdBuffer.length()-1);
				map.put("qid", questionId+"");
				map.put("qvals", selectIdBuffer.toString());
				if(StringTools.isNotBlank(value))
					map.put("qtxt", value);
			}
			return map;
		}
		return null;
	}

	@Override
	public int getCount() {
		return dataItems.size();
	}
	//初始化选中的数据
	public void initSelectData(List<Map<String, Object>> list){
		if(dataItems != null && dataItems.size() > 0){
			Map<String, Object> model = getModelByQid(list, questionId);
			String[] valueArray = null;
			if(model != null){
				String values = (String) model.get("qvals");
				if(StringTools.isNotBlank(values)){
					valueArray = values.split(",");
				}
			}
			if(valueArray == null || valueArray.length == 0)
				return;
			for(int i = 0; i < dataItems.size(); i++ ){
				SurveyOptions option = dataItems.get(i);
				System.out.println(option.getId()+"............");
				option.setSelected(contanis(valueArray, option.getId()+""));
			}
			notifyDataSetChanged();
		}
	}
	
	private boolean contanis(String[] valueArray,String value){
		for (String v : valueArray) {
			if(value.equals(v)){
				return true;
			}
		}
		return false;
	}
	
	private Map<String,Object> getModelByQid(List<Map<String, Object>> list,long qid){
		System.out.println("========================");
		for (Map<String, Object> map : list) {
			long id = Long.valueOf((String) map.get("qid"));
			System.out.println("qid: "+qid+" , id: "+id);
			if(id  == qid){
				System.out.println(map+"...............");
				return map;
			}
		}
		return null;
	}
	
	private List<SurveyOptions> getSelectedItem(){
		List<SurveyOptions> list = new ArrayList<SurveyOptions>();
		for (SurveyOptions option : dataItems) {
			if(option.isSelected())
				list.add(option);
		}
		return list;
	}
	
	private int getSelectedCount(){
		return getSelectedItem().size();
	}

	public void select(int position){
		SurveyOptions surveyOptions = dataItems.get(position);
		if (isRadio) {
			for (int i = 0; i < dataItems.size(); i++) {
				if (i == position) {
					SurveyOptions surveyOption = dataItems.get(i);
					boolean isSelected = surveyOption.isSelected() ? false : true;
					surveyOption.setSelected(isSelected);
				} else {
					dataItems.get(i).setSelected(false);
				}
			}
		} else {
			if (surveyOptions.isSelected()) {
				surveyOptions.setSelected(false);
			} else {
				if(getSelectedCount() < maxSelect){
					surveyOptions.setSelected(true);
				}else{
					String message = "最多只能选择"+maxSelect+"项";
					mContext.toastShort(mContext.getLanguageString(message));
				}
			}
		}
		notifyDataSetChanged();
	}
	
	/**
	 * @param position
	 *            根据选中项位置刷新adapter
	 */
	public void updateIndex(int position, Map<Long, List<SurveyOptions>> multiAnswerMap) {
		SurveyOptions surveyOptions = dataItems.get(position);
		if (isRadio) {
			boolean select = false;
			for (int i = 0; i < dataItems.size(); i++) {
				if (i == position) {
					SurveyOptions surveyOption = dataItems.get(i);
					boolean isSelected = surveyOption.isSelected() ? false : true;
					surveyOption.setSelected(isSelected);
					select = isSelected;
				} else {
					dataItems.get(i).setSelected(false);
				}
			}
			if (select) {
				multiAnswerMap.put(questionId, dataItems);
			} else {
				multiAnswerMap.remove(questionId);
			}

		} else {
			if (surveyOptions.isSelected()) {
				surveyOptions.setSelected(false);
			} else {
				multiAnswerMap.put(questionId, dataItems);
				surveyOptions.setSelected(true);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return dataItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	ViewHolder holder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SurveyOptions surveyOptions = dataItems.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(R.layout.item_vote_submit_listview, null);
			holder.voteitem = (LinearLayout) convertView.findViewById(R.id.voteitem);
			holder.select_image = (ImageView) convertView.findViewById(R.id.vote_submit_select_image);
			holder.select_text = (TextView) convertView.findViewById(R.id.vote_submit_select_text);
			holder.valueEditText = (EditText) convertView.findViewById(R.id.value);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (surveyOptions.isSelected()) {
			if (isRadio) {
				holder.select_image.setImageResource(R.drawable.radio_select);
			} else {
				holder.select_image.setImageResource(R.drawable.vote_submit_selected);
			}
			if (surveyOptions.getOtype() == 4) {
				holder.voteitem.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						boolean isSelected = surveyOptions.isSelected() ? false : true;
						surveyOptions.setSelected(isSelected);
						notifyDataSetChanged();
					}
				});
				holder.valueEditText.setVisibility(View.VISIBLE);
				holder.valueEditText.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						surveyOptions.setRemark(s.toString());
					}
				});
			}
		} else {
			holder.valueEditText.setVisibility(View.GONE);
			if (isRadio) {
				holder.select_image.setImageResource(R.drawable.radio_nomal);
			} else {
				holder.select_image.setImageResource(R.drawable.vote_submit_normal);
			}
			holder.select_text.setTextColor(mContext.getResources().getColor(R.color.black));
		}
		holder.select_text.setText((String) I18NUtils.getValue(surveyOptions, "otitle"));
		return convertView;
	}

	/**
	 * @author wisdomhu 自定义类
	 */
	class ViewHolder {
		LinearLayout voteitem;
		ImageView select_image;
		TextView select_text;
		EditText valueEditText;
	}
}
