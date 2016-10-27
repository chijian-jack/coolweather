package com.coolweather.app.util;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WeatherPagerAdapter extends FragmentPagerAdapter {
	
	
	private List<Fragment> fragments;
	/**
	 * ҳ�����ݼ���
	 */
	//private List<Fragment> fgs = null;
	private FragmentManager mFragmentManager;
	/**
	 * �����ݷ����ı�ʱ�Ļص��ӿ�
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
	 * ��������ҳ������
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
	 *��ҳ�����ݷ����ı�ʱ����Ե��ô˷���
	 * 
	 * �����������ݣ�����������Ϣ�ɻص�����ʵ��
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
	 *�ص��ӿ�
	 */
	public interface OnReloadListener
	{
		public void onReload();
	}
	

}
