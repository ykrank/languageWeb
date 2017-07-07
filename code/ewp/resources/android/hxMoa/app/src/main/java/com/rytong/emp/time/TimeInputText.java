package com.rytong.emp.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.w3c.dom.Element;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.ghbank.moas.R;
import com.rytong.emp.gui.atom.InputText;
import com.rytong.emp.tool.Utils;

/**
 * <p>
 * {@literal <input type="text">}标签。
 * </p>
 */
public class TimeInputText extends InputText{
	
	Context context_;
	Activity activity_;
	int hour, min;
	WheelMain wheelMain;
	String timeStr = "";
	public TimeInputText(Context context) {
		super(context);
		setEnabled(false);
		this.context_ = context;
		this.activity_ = (Activity)context;
		String yyyy = null;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		yyyy = formatter.format(curDate);
		Calendar calendar = Calendar.getInstance();
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		min = calendar.get(Calendar.MINUTE);
		initTime("HH:mm");
	}
	private void initTime(String format) {
		// 如果没有初始化显示，就显示当前日期
		String valueAttr = getText().toString();
		Date date = null;
		if (!Utils.isEmpty(valueAttr)) {
			SimpleDateFormat valueDateFormat = new SimpleDateFormat(format, Locale.getDefault());
			try {
				date = valueDateFormat.parse(valueAttr);
			} catch (ParseException e) {
				Utils.printException(e);
			}
		}
		if (date == null) {
			date = new Date();
		}
		// 设置text
		SimpleDateFormat showDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		setText(showDateFormat.format(date));
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			LayoutInflater inflater = LayoutInflater
					.from(activity_);
			final View timepickerview = inflater.inflate(
					R.layout.timepicker, null);
			ScreenInfo screenInfo = new ScreenInfo(activity_);

			wheelMain = new WheelMain(timepickerview);
			wheelMain.screenheight = screenInfo.getHeight();
			wheelMain.initDateTimePicker(hour, min);

			new AlertDialog.Builder(activity_)
					.setTitle("选择时间")
					.setView(timepickerview)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									setText(wheelMain.getTime());
									timeStr = wheelMain.getTime();
									
								}
							}).setNegativeButton("取消", null).show();

		
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return super.onTouchEvent(event);
	}
	@Override
	public void init(Element element) {
		// TODO Auto-generated method stub
		super.init(element);
		if(element.getAttribute("showFormat") != null && !"".equals(element.getAttribute("showFormat"))){
			
		}else{
			
		}
		if(element.getAttribute("showFormat") != null && !"".equals(element.getAttribute("showFormat"))){
			
		}
	}
	@Override
	public void onBindElement(Element element) {
		// TODO Auto-generated method stub
		super.onBindElement(element);
		if(timeStr != null){
			element.setAttribute("valueFormat", timeStr);
		}
	}
}
