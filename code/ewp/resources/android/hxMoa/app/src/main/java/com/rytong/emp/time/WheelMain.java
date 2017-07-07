package com.rytong.emp.time;

import java.util.Arrays;
import java.util.List;

import com.ghbank.moas.R;

import android.view.View;

public class WheelMain {

	private View view;
	private WheelView wv_hours;
	private WheelView wv_mins;
	public int screenheight;

	private static int START_YEAR = 1990, END_YEAR = 2100;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}
	public WheelMain(View view) {
		super();
		this.view = view;
		setView(view);
	}

	public void initDateTimePicker() {
		this.initDateTimePicker(0, 0);
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(int h, int m) {
		wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_mins = (WheelView) view.findViewById(R.id.min);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);// 可循环滚动
		wv_hours.setLabel("时");// 添加文字
		wv_hours.setCurrentItem(h);
		
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
		wv_mins.setCyclic(true);// 可循环滚动
		wv_mins.setLabel("分");// 添加文字
		wv_mins.setCurrentItem(m);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		textSize = (screenheight / 100) * 3;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		String wv_hours_text = null;
		String wv_mins_text = null;
		if(wv_hours.getCurrentItem()<10 && wv_hours.getCurrentItem() >= 0){
			wv_hours_text = "0"+String.valueOf(wv_hours.getCurrentItem());
		}else{
			wv_hours_text = String.valueOf(wv_hours.getCurrentItem());
		}
		if(wv_mins.getCurrentItem()< 10 && wv_mins.getCurrentItem() >= 0){
			wv_mins_text = "0"+String.valueOf(wv_mins.getCurrentItem());
		}else{
			wv_mins_text = String.valueOf(wv_mins.getCurrentItem());
		}
		sb.append(wv_hours_text).append(":")
		.append(wv_mins_text);
		return sb.toString();
	}
}
