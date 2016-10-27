package com.coolweather.app.util;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WeatherPagerAdapter extends FragmentPagerAdapter {
	
	
	private List<Fragment> fragments;
	/**
	 * 页面内容集合
	 */
	//private List<Fragment> fgs = null;
	private FragmentManager mFragmentManager;
	/**
	 * 当数据发生改变时的回调接口
	 */
	private OnReloadListener mListener;
	
	public WeatherPagerAdapter(android.support.v4.app.FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
		
		
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}
	
	/**
	 * 重新设置页面内容
	 * @param items
	 */
	public void setPagerItems(List<Fragment> items)
	{
		if (items != null)
		{
			for (int i = 0; i < fragments.size(); i++)
			{
				mFragmentManager.beginTransaction().remove(fragments.get(i)).commit();
			}
			fragments = items;
		}
	}
	/**
	 *当页面数据发生改变时你可以调用此方法
	 * 
	 * 重新载入数据，具体载入信息由回调函数实现
	 */
	public void reLoad()
	{
		if(mListener != null)
		{
			mListener.onReload();
		}
		this.notifyDataSetChanged();
	}
	public void setOnReloadListener(OnReloadListener listener)
	{
		this.mListener = listener;
	}
	/**
	 * @author Rowand jj
	 *回调接口
	 */
	public interface OnReloadListener
	{
		public void onReload();
	}
	

}
