package com.coolweather.app.fragment;

import com.coolweather.app.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class FirstWeatherFragment extends Fragment {
	private  TextView weekTv1, weekTv2, weekTv3;
	//private static ImageView weather_imgIv1, weather_imgIv2, weather_imgIv3;
	private  TextView type1, type2, type3;
	private  TextView climateTv1, climateTv2, climateTv3;
	private  TextView windTv1, windTv2, windTv3;
	private  TextView fx1,fx2,fx3;
	private  SharedPreferences prefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		View view = inflater.inflate(R.layout.weather_item,
				container, false);
		View view1 = view.findViewById(R.id.subitem1);
		View view2 = view.findViewById(R.id.subitem2);
		View view3 = view.findViewById(R.id.subitem3);

		weekTv1 = (TextView) view1.findViewById(R.id.week);
		weekTv2 = (TextView) view2.findViewById(R.id.week);
		weekTv3 = (TextView) view3.findViewById(R.id.week);

		/*weather_imgIv1 = (ImageView) view1.findViewById(R.id.weather_img);
		weather_imgIv2 = (ImageView) view2.findViewById(R.id.weather_img);
		weather_imgIv3 = (ImageView) view3.findViewById(R.id.weather_img);*/
		
		type1 = (TextView) view1.findViewById(R.id.temperature);
		type2 = (TextView) view2.findViewById(R.id.temperature);
		type3 = (TextView) view3.findViewById(R.id.temperature);

		climateTv1 = (TextView) view1.findViewById(R.id.climate);
		climateTv2 = (TextView) view2.findViewById(R.id.climate);
		climateTv3 = (TextView) view3.findViewById(R.id.climate);

		windTv1 = (TextView) view1.findViewById(R.id.wind);
		windTv2 = (TextView) view2.findViewById(R.id.wind);
		windTv3 = (TextView) view3.findViewById(R.id.wind);
		
		fx1 = (TextView) view1.findViewById(R.id.fx);
		fx2 = (TextView) view2.findViewById(R.id.fx);
		fx3 = (TextView) view3.findViewById(R.id.fx);
		
		updateWeather1();
		
		return view;
	}
	
	public void updateWeather1(){
		
		// TODO Auto-generated method stub
		weekTv1.setText(prefs.getString("date1", ""));
		weekTv2.setText(prefs.getString("date", ""));
		weekTv3.setText(prefs.getString("date_1", ""));
		
		climateTv1.setText(prefs.getString("low1", "").substring(3)+"/"+prefs.getString("high1", "").substring(3));
		climateTv2.setText(prefs.getString("low", "").substring(3)+"/"+prefs.getString("high", "").substring(3));
		climateTv3.setText(prefs.getString("low_1", "").substring(3)+"/"+prefs.getString("high_1", "").substring(3));


		type1.setText(prefs.getString("type1", ""));
		type2.setText(prefs.getString("type", ""));
		type3.setText(prefs.getString("type_1", ""));

		windTv1.setText(prefs.getString("fl", ""));
		windTv2.setText(prefs.getString("fengli", ""));
		windTv3.setText(prefs.getString("fengli_1", ""));
		
		fx1.setText(prefs.getString("fx", ""));
		fx2.setText(prefs.getString("fengxiang", ""));
		fx3.setText(prefs.getString("fengxiang_1", ""));

	}
}
