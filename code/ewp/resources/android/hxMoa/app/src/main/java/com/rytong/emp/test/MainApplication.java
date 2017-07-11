package com.rytong.emp.test;

import com.rytong.emp.android.EMPApplication;
import com.rytong.emp.tool.Utils;

public class MainApplication extends EMPApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		setExtraHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				Utils.printLog("uncaughtException", "额外的全局异常处理。");
			}
		});
	}
}
