package chijian.coolweather.app.util;

import java.util.List;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WeatherPagerAdapter extends FragmentPagerAdapter{
	
	
	private List<Fragment> fragments;
	//private static final String TAG = "YiPageAdapter";
	/**
	 * ҳ�����ݼ���
	 */
	private FragmentManager mFragmentManager;
	/**
	 * �����ݷ����ı�ʱ�Ļص��ӿ�
	 */
	private OnReloadListener mListener;

	public WeatherPagerAdapter(android.support.v4.app.FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
		mFragmentManager = fm;
		
	}

	@Override
	public Fragment getItem(int index)
	{
		//Log.i(TAG,"ITEM CREATED...");
		return fragments.get(index);
	}

	@Override
	public int getCount()
	{
		return fragments.size();// ����ѡ�����
	}

	@Override
	public int getItemPosition(Object object)
	{
		return POSITION_NONE;
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
