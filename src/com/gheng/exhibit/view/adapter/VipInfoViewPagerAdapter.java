package com.gheng.exhibit.view.adapter;

import java.util.List;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class VipInfoViewPagerAdapter extends FragmentPagerAdapter{
	
	private List<Fragment> fragments;

	public VipInfoViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}
	
	public void setFragments(List<Fragment> fragments) {
		this.fragments  = fragments;
	}

}
