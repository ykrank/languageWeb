package com.rytong.emp.test.multitask;

import android.content.Intent;
import android.view.KeyEvent;

import com.rytong.emp.render.EMPRender;
import com.rytong.emp.test.MainActivity;


/**
 * 子任务Activity
 * 
 * @author Zhoucj
 *
 */
public class SubTaskActivity extends MainActivity {
	
	@Override
	public void onAdjustControlConfig() {
		super.onAdjustControlConfig();
		
		// 获取Activity显示范围
		Intent intent = getIntent();
		int[] size = (int[]) intent.getSerializableExtra("bodysize");
		int width = size[0];
		int height = size[1];
		mAndroidEMPBuilder.setConfigBodySize(width, height);
	}
	
	@Override
	public void onLoadStartPage(EMPRender empRender) {
		Intent intent = getIntent();
		String content = (String) intent.getSerializableExtra("content");
		empRender.load(content);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 如果是子Activity，则关闭物理返回键响应，由主Activity处理
			return true;
		} else {
			// 除了捕获你想要的返回键之外，其余的应该交给他的父类去处理
			return super.onKeyDown(keyCode, event);
		}
	}

}
