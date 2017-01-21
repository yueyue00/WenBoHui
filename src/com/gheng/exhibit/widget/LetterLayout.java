package com.gheng.exhibit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.utils.DipUtils;
import com.smartdot.wenbo.huiyi.R;

/**
 * ABCDEF条
 * @author lileixing
 */
public class LetterLayout extends LinearLayout{

	private final String letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private OnClickLetterListener onClickLetterListener;
	/**
	 * @param context
	 */
	public LetterLayout(Context context) {
		super(context);
		init(context,null);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public LetterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	private void init(Context context,AttributeSet attrs){
		int orientation = this.getOrientation();
		int length = letter.length();
		for(int i = 0;i < length ; i++){
			RelativeLayout group = new RelativeLayout(context);
			group.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			
			TextView tv = createTextViewByOrientation(context, orientation);
			tv.setText(letter.charAt(i)+"");
			group.addView(tv);
			
			group.addView(createMyView(context,tv));
			
			addView(group);
			
			group.setOnClickListener(new OnClickImpl(i));
		}
	}
	
	private View createMyView(Context context,View view){
		View v = new View(context);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,DipUtils.dip2px(context, 10));
		rlp.addRule(RelativeLayout.ALIGN_BOTTOM,view.getId());
		v.setBackgroundColor(getResources().getColor(R.color.common_red_font));
		rlp.setMargins(0, 0, 0, 0);
		v.setLayoutParams(rlp);
		return v;
	}
	
	/**
	 * 根据方向不同生成不同的TextView
	 */
	private TextView createTextViewByOrientation(Context context,int orientation){
		int padding = DipUtils.dip2px(context, 15);
		TextView tv = new TextView(context);
		switch (orientation) {
		case HORIZONTAL:	//横向
			tv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
			tv.setPadding(padding, 0, padding,0);
			break;

		default:			//竖向
			tv.setPadding(0,padding,0,padding);
			tv.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			break;
		}
		tv.setGravity(Gravity.CENTER);
//		tv.setBackgroundColor(Color.WHITE);
		tv.setTextColor(getResources().getColor(R.color.title_font));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		return tv;
	}
	
	class OnClickImpl implements View.OnClickListener{

		int index ;
		
		public OnClickImpl(int index) {
			this.index = index;
		}
		
		@Override
		public void onClick(View v) {
			int childCount = LetterLayout.this.getChildCount();
			for(int i = 0 ; i < childCount; i ++){
				RelativeLayout relativeLayout = (RelativeLayout) LetterLayout.this.getChildAt(i);
				relativeLayout.getChildAt(1).setVisibility(View.GONE);
				TextView tv = (TextView)relativeLayout.getChildAt(0);
				tv.setTextColor(getResources().getColor(R.color.title_font));
			}
			RelativeLayout vg =  (RelativeLayout) LetterLayout.this.getChildAt(index);
			vg.getChildAt(1).setVisibility(View.VISIBLE);
			
			if(onClickLetterListener != null){
				TextView tv = (TextView)vg.getChildAt(0);
				tv.setTextColor(getResources().getColor(R.color.common_red_font));
				onClickLetterListener.onClick(tv);
			}
		}
	}
	
	public void clearSelected() {
		int childCount = LetterLayout.this.getChildCount();
		for(int i = 0 ; i < childCount; i ++){
			RelativeLayout relativeLayout = (RelativeLayout) LetterLayout.this.getChildAt(i);
			relativeLayout.getChildAt(1).setVisibility(View.GONE);
			TextView tv = (TextView)relativeLayout.getChildAt(0);
			tv.setTextColor(getResources().getColor(R.color.title_font));
		}
	}
	
	/**
	 * @param onClickLetterListener the onClickLetterListener to set
	 */
	public void setOnClickLetterListener(
			OnClickLetterListener onClickLetterListener) {
		this.onClickLetterListener = onClickLetterListener;
	}
	
	public interface OnClickLetterListener{
		void onClick(TextView tv);
	}
}
