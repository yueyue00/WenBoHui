package com.gheng.exhibit.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.utils.DipUtils;
import com.smartdot.wenbo.huiyi.R;

/**
 * Tab页面
 * @author lileixing
 */
public class TabPager extends LinearLayout {

	private int lineColor;
	
	private int lineHeight;
	
	private int textSelectColor;
	
	private int textUnSelectColor;
	
	private int tabTextSize;
	
	private LinearLayout tab ;
	
	private List<String> texts = new ArrayList<String>();
	/**
	 * 活动图片
	 */
	private CustomViewPager viewPager;
	
	private OnPageSelectListener onPageSelectListener;
	
	private boolean smoothScroll = false;
	
	private ViewPapgerAdapter adapter;
	
	private int pageHeight;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public TabPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
		if(texts.size() > 0)
			initView();
	}
	
	private void init(Context context, AttributeSet attrs){
		int color = getResources().getColor(R.color.common_red_font);
		int appBlack = getResources().getColor(R.color.main_schedule_font);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.TabPager);
		lineColor = a.getColor(R.styleable.TabPager_lineColor, color);
		tabTextSize = a.getDimensionPixelSize(R.styleable.TabPager_tabTextSize,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, context.getResources().getDisplayMetrics()));
		lineHeight = a.getDimensionPixelSize(R.styleable.TabPager_lineHeight, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()));
		textSelectColor = a.getColor(R.styleable.TabPager_textSelectColor, color);
		textUnSelectColor = a.getColor(R.styleable.TabPager_textUnSelectColor, appBlack);
		CharSequence[] ts = a.getTextArray(R.styleable.TabPager_texts);
		if(ts != null){
			for (CharSequence charSequence : ts) {
				texts.add(String.valueOf(charSequence));
			}
		}
		pageHeight = a.getLayoutDimension(R.styleable.TabPager_pageHeight, -1);
		a.recycle(); 
	}

	/**
	 * @param pageHeight the pageHeight to set
	 */
	public void setPageHeight(int pageHeight) {
		this.pageHeight = pageHeight;
		android.view.ViewGroup.LayoutParams params = viewPager.getLayoutParams();
		params.height = pageHeight;
		viewPager.setLayoutParams(params);
	}
	
	private void initView(){
		setOrientation(VERTICAL);
		tab = new LinearLayout(getContext());
		tab.setOrientation(HORIZONTAL);
		LinearLayout.LayoutParams llp = new LayoutParams(-1, DipUtils.dip2px(getContext(), 50));
		tab.setLayoutParams(llp);
		addView(tab);
		addTabText();
		viewPager = new CustomViewPager(getContext());
		LinearLayout.LayoutParams vpParam = new LayoutParams(-1, pageHeight);
		viewPager.setLayoutParams(vpParam);
		addView(viewPager);
		viewPager.setBackgroundColor(Color.TRANSPARENT);
		adapter = new ViewPapgerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if(onPageSelectListener != null){
					onPageSelectListener.onPageSelected(position);
				}
			}
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}
	
	private void addTabText(){
		for(int i = 0;i < texts.size(); i ++){
			RelativeLayout rLayout = new RelativeLayout(getContext());
			LayoutParams lp = new LayoutParams(0, -1);
			lp.weight = 1;
			rLayout.setLayoutParams(lp);
			tab.addView(rLayout);
			
			rLayout.setOnClickListener(new OnClickListenerImpl(i));
			
			TextView tv = new TextView(getContext());
			RelativeLayout.LayoutParams tlp = new RelativeLayout.LayoutParams(-2,-2);
			tlp.addRule(RelativeLayout.CENTER_IN_PARENT);
			tv.setLayoutParams(tlp);
			tv.setText(texts.get(i));
			tv.setTextColor(textUnSelectColor);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,tabTextSize);
			rLayout.addView(tv);
			
			int margin = DipUtils.dip2px(getContext(), 10);
			if(i < texts.size() - 1){
				LinearLayout ll = new LinearLayout(getContext());
				RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(DipUtils.dip2px(getContext(), 1),-1);
				rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				ll.setLayoutParams(rlp);
				ll.setOrientation(HORIZONTAL);
				ll.setPadding(0, margin, 0, margin);
				
				View splitView = new View(getContext());
				splitView.setLayoutParams(new LayoutParams(-1, -1));
				splitView.setBackgroundResource(android.R.color.darker_gray);
				ll.addView(splitView);
				rLayout.addView(ll);
			}
			
			View line2 = new View(getContext());
			RelativeLayout.LayoutParams lineParam2 = new RelativeLayout.LayoutParams(-1, DipUtils.dip2px(getContext(), 1));
			lineParam2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			line2.setLayoutParams(lineParam2);
			line2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
			rLayout.addView(line2);
			
			int lineMargin = DipUtils.dip2px(getContext(), 20);
			View line = new View(getContext());
			RelativeLayout.LayoutParams lineParam = new RelativeLayout.LayoutParams(-1, lineHeight);
			lineParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			lineParam.setMargins(lineMargin, 0, lineMargin, 0);
			line.setLayoutParams(lineParam);
			line.setBackgroundColor(lineColor);
			line.setTag(1000);
			rLayout.addView(line);
		}
	}
	
	public void setViews(List<View> views){
		if(adapter == null)
			initView();
		adapter.setViews(views);
	}
	
	/**
	 * @param onPageSelectListener the onPageSelectListener to set
	 */
	public void setOnPageSelectListener(OnPageSelectListener onPageSelectListener) {
		this.onPageSelectListener = onPageSelectListener;
	}
	
	public void setTexts(List<String> texts) {
		this.texts = texts;
		if(tab == null){
			initView();
		}
		int count = tab.getChildCount();
		if(count == 0){
			addTabText();
		}
		for(int i = 0; i < count;i ++){
			RelativeLayout child = (RelativeLayout) tab.getChildAt(i);
			TextView tv = (TextView) child.getChildAt(0);
			tv.setText(texts.get(i));
		}
	}
	
	public void setTexts(String[] texts){
		List<String> ts = new ArrayList<String>();
		for (String s : texts) {
			ts.add(s);
		}
		setTexts(ts);
	}
	/**
	 * 点击Tab
	 */
	public void select(int index){
		int count = tab.getChildCount();
		int rIndex = -1;
		for(int i = 0; i < count;i ++){
			View v = tab.getChildAt(i);
			if(v instanceof RelativeLayout){
				rIndex ++;
				RelativeLayout child = (RelativeLayout) v;
				TextView tv = (TextView) child.getChildAt(0);
				View line =  child.getChildAt(2);
				if(line.getTag() == null)
					line = child.getChildAt(3);
				if(index == rIndex){
					tv.setTextColor(textSelectColor);
					line.setVisibility(View.VISIBLE);
				}else{
					tv.setTextColor(textUnSelectColor);
					line.setVisibility(View.GONE);
				}
			}
		}
		viewPager.setCurrentItem(index, smoothScroll);
	}
	
	/**
	 * 是否启用页面间切换的动画效果，默认不启用
	 */
	public void setSmoothScroll(boolean smoothScroll) {
		this.smoothScroll = smoothScroll;
	}
	/**
	 * 是否可以手指滑动页面,默认不可以
	 */
    public void setScrollable(boolean isScrollable) {
    	viewPager.setScrollable(isScrollable);
    }
    
    public int getPageCount(){
    	return texts.size();
    }
	
	public interface OnPageSelectListener{
		public void onPageSelected(int position);
	}
	
	class CustomViewPager extends ViewPager {

	    private boolean isScrollable = false;

	    public CustomViewPager(Context context) {
	        super(context);
	    }

	    public CustomViewPager(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	    @Override
	    public boolean onTouchEvent(MotionEvent ev) {
	        if (isScrollable == false) {
	            return false;
	        } else {
	            return super.onTouchEvent(ev);
	        }
	    }

	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent ev) {
	        if (isScrollable == false) {
	            return false;
	        } else {
	            return super.onInterceptTouchEvent(ev);
	        }

	    }

	    public boolean isScrollable() {
	        return isScrollable;
	    }

	    public void setScrollable(boolean isScrollable) {
	        this.isScrollable = isScrollable;
	    }

	}
	
	class ViewPapgerAdapter extends PagerAdapter {

		private List<View> mListViews;  
		
		public ViewPapgerAdapter(){}
		
		public ViewPapgerAdapter(List<View> lists){
			if(lists == null){
				this.mListViews = new ArrayList<View>();
			}else{
				this.mListViews = lists;
			}
		}
		
		public void setViews(List<View> lists){
			if(lists == null){
				this.mListViews = new ArrayList<View>();
			}else{
				this.mListViews = lists;
			}
			notifyDataSetChanged();
		}
		
		public void addView(View v){
			mListViews.add(v);
			notifyDataSetChanged();
		}
		
		@Override  
	    public int getCount() {  
			if(mListViews == null)
				return 0;
	        return mListViews.size();  
	    }  

	    @Override  
	    public Object instantiateItem(View collection, int position) {  
	        ((ViewPager) collection).addView(mListViews.get(position),0);  
	        return mListViews.get(position);  
	    }  

	    @Override  
	    public void destroyItem(View collection, int position, Object view) {  
	        ((ViewPager) collection).removeView(mListViews.get(position));  
	    }  
	      
	    @Override  
	    public boolean isViewFromObject(View view, Object object) {  
	        return view==object;  
	    }  

	    @Override  
	    public void finishUpdate(View arg0) {}  
	    
	    @Override  
	    public int getItemPosition(Object object) {  
	        return super.getItemPosition(object);  
	    }  
	    
	    @Override  
	    public void restoreState(Parcelable arg0, ClassLoader arg1) {}  

	    @Override  
	    public Parcelable saveState() {  
	        return null;  
	    }  

	    @Override  
	    public void startUpdate(View arg0) {}  
	      
	} 
	
	class OnClickListenerImpl implements OnClickListener{

		private int index ;
		
		public OnClickListenerImpl(int index){
			this.index = index;
		}
		
		@Override
		public void onClick(View v) {
			select(index);
		}
	}
}
