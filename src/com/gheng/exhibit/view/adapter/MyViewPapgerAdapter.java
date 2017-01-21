package com.gheng.exhibit.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyViewPapgerAdapter extends PagerAdapter {

	private List<View> mListViews;  
	
	public MyViewPapgerAdapter(List<View> lists){
		if(lists == null){
			this.mListViews = new ArrayList<View>();
		}else{
			this.mListViews = lists;
		}
	}
	
	public void addView(View v){
		mListViews.add(v);
		notifyDataSetChanged();
	}

	@Override  
    public int getCount() {  
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
        return view==(object);  
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