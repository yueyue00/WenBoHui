package com.gheng.exhibit.view.noused;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gheng.exhibit.utils.DateTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.SeekBarPressure;
import com.gheng.exhibit.widget.SeekBarPressure.OnRangeSeekBarChangeListener;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

/**
 *	会议搜索
 * @author lileixing
 */
public class ScheduleSearchActivity extends BaseActivity implements OnClickListener{

	@ViewInject(R.id.titleBar)
	private TitleBar titleBar;
	
	private SeekBarPressure<Integer> seekBarPressure;
	
	@ViewInject(R.id.barContainer)
	private LinearLayout barContaner;
	
	@ViewInject(R.id.checkBoxContainer)
	private LinearLayout checkBoxContainer;
	
	private String[] values = {"2015-05-15","2015-05-16","2015-05-17","2015-05-18"};
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	@ViewInject(R.id.edt_name)
	private EditText edt_name;
	
	@ViewInject(R.id.tv_date_label)
	private TextView tv_date_label;
	
	@ViewInject(R.id.tv_time_label)
	private TextView tv_time_label;
	
	@ViewInject(R.id.tv_search)
	private TextView tv_search;
	
	@ViewInject(R.id.tv_start_time)
	private TextView tv_start_time;

	@ViewInject(R.id.tv_end_time)
	private TextView tv_end_time;
	
	//类型选择
	@ViewInject(R.id.tv_sync)
	private TextView tv_sync;
	@ViewInject(R.id.tv_close)
	private TextView tv_close;
	
	@ViewInject(R.id.type_layout)
	private LinearLayout type_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_search);
	}
	
	@Override
	protected void setI18nValue() {
		titleBar.setText(getLanguageString("会议搜索"));
		tv_date_label.setText(getLanguageString("选择日期(可多选)"));
		tv_time_label.setText(getLanguageString("选择时间(滑动)"));
		edt_name.setHint(getLanguageString("请输入关键字"));
		tv_search.setText(getLanguageString("搜索"));
		
		tv_start_time.setText("8:00");
		tv_end_time.setText("18:00");
		setCheckBoxValue();
		
		tv_close.setText(getLanguageString("闭门会"));
		tv_sync.setText(getLanguageString("同传"));
	}
	
	private void setCheckBoxValue(){
		int childCount = checkBoxContainer.getChildCount();
		for (int i = 0; i < childCount; i++) {
			CheckBox checkBox = (CheckBox) checkBoxContainer.getChildAt(i);
			try {
				Date date = format.parse(values[i]);
				String formatNoYear = DateTools.formatNoYear(date);
				checkBox.setText(formatNoYear);
			} catch (ParseException e) {
			}
		}
		tv_search.setOnClickListener(this);
	}
	
	@Override
	protected void init() {
		titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
			}
			
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
		seekBarPressure = new SeekBarPressure<Integer>(8, 18, this);
		seekBarPressure.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(SeekBarPressure<?> bar,
                    Integer minValue, Integer maxValue) {
            	 tv_start_time.setText(String.valueOf(minValue)+":00");
                 tv_end_time.setText(String.valueOf(maxValue)+":00");
            }
        });
		barContaner.addView(seekBarPressure);
		setTypeClick();
	}
	
	private void setTypeClick(){
		int childCount = type_layout.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childAt = type_layout.getChildAt(i);
			if(childAt instanceof ViewGroup){
				childAt.setTag(0);
				childAt.setOnClickListener(new OnItemClick());
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_search:
			subSearch();
			break;

		}
	}
	//点击搜索触发
	private void subSearch(){
		
		String startTime = tv_start_time.getText().toString();
		String endTime = tv_end_time.getText().toString();
		if(startTime.equals(endTime)){
			toastShort(getLanguageString("开始时间不能和结束时间相同"));
			return;
		}
		
		String keyword = edt_name.getText().toString();
		ArrayList<String> select = new ArrayList<String>();
		int childCount = checkBoxContainer.getChildCount();
		for (int i = 0; i < childCount; i++) {
			CheckBox checkBox = (CheckBox) checkBoxContainer.getChildAt(i);
			if(checkBox.isChecked()){
				select.add(values[i]);
			}
		}
		Intent data = new Intent(this, ScheduleListActivity.class);
		data.putStringArrayListExtra("date", select);
		data.putExtra("keyword", keyword);
		data.putExtra("startTime", startTime);
		data.putExtra("endTime", endTime);
		data.putIntegerArrayListExtra("array", getSelect());
		setResult(100, data);
		finish();
	}
	
	//获取开关选择
	private ArrayList<Integer> getSelect(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		int childCount = type_layout.getChildCount();
		int llChildIndex = -1;
		for (int i = 0; i < childCount; i++) {
			View childAt = type_layout.getChildAt(i);
			if(!(childAt instanceof ViewGroup)){
				continue;
			}
			++llChildIndex;
			int tag = (Integer) childAt.getTag();
			if(tag == 1){
				list.add(llChildIndex);
			}
		}
		return list;
	}
	
	class OnItemClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			int tag = (Integer) v.getTag();
			if(tag == 0){
				v.setTag(1);
			}else{
				v.setTag(0);
			}
			ViewGroup childAt = (ViewGroup)v;
			ImageView switchIv = (ImageView) childAt.getChildAt(1);
			tag = (Integer) v.getTag();
			if(tag == 0){
				switchIv.setImageResource(R.drawable.vote_submit_normal);
			}else{
				switchIv.setImageResource(R.drawable.vote_submit_selected);
			}
		}
	}
}
