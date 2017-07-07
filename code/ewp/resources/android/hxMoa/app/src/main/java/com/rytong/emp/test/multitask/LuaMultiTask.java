package com.rytong.emp.test.multitask;

import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.view.View;
import android.view.Window;

import com.rytong.emp.lua.LuaMetatable;
import com.rytong.emp.render.EMPRender;


/**
 * lua接口，用于新建、显示、移除一个子任务
 * 
 * @author Zhoucj
 *
 */
public class LuaMultiTask {

	private Activity mActivity;
	private EMPRender mEMPRender;

	public LuaMultiTask(Activity activity, EMPRender empRender) {
		mActivity = activity;
		mEMPRender = empRender;
	}
	
	/**
	 * 新建子任务
	 * 
	 * @param iFrameMeta
	 *            子任务显示的父控件
	 * @param content 
	 * @param id
	 *            子任务的id
	 */
	public void newTask(final LuaMetatable iFrameMeta, final String content, final String id) {
		final IFrame iFrame = (IFrame) iFrameMeta.getView();
		
		if (!iFrame.getTaskMap().containsKey(id)) {
			mEMPRender.getGUIFactory().addGUITask(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(mActivity, SubTaskActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					// 将父控件的宽高作为子Activity的显示大小，传给子Activity
					int width = iFrameMeta.getLayout().getDisplaySpace().width();
					int height = iFrameMeta.getLayout().getDisplaySpace().height();
					intent.putExtra("bodysize", new int[]{width, height});
					intent.putExtra("content", content);

					Window window = ((ActivityGroup) mActivity).getLocalActivityManager()
							.startActivity(id, intent);
					View taskView = window.getDecorView();
					iFrame.addView(taskView);
					
					// 把其余的子任务视图隐藏
					Iterator<Entry<String, View>> iter = iFrame.getTaskMap().entrySet().iterator();
					while (iter.hasNext()) {
						Entry entry = (Entry) iter.next();
						Object key = entry.getKey();
						View view = (View) entry.getValue();
						view.setVisibility(View.GONE);
					}
					// 在子任务列表中保存新的子任务
					iFrame.getTaskMap().put(id, taskView);
				}
			});
		}
	}
	
	/**
	 * 显示指定id的子任务视图
	 * 
	 * @param iFrameMeta
	 *            子任务显示的父控件
	 * @param id
	 *            子任务的id
	 */
	public void showTask(final LuaMetatable iFrameMeta, final String id) {
		final IFrame iFrame = (IFrame) iFrameMeta.getView();
		
		if (iFrame.getTaskMap().containsKey(id)) {
			mEMPRender.getGUIFactory().addGUITask(new Runnable() {

				@Override
				public void run() {
					// 遍历子任务列表中的所有子任务，将制定id的子任务显示，其余子任务隐藏
					Iterator<Entry<String, View>> iter = iFrame.getTaskMap().entrySet().iterator();
					while (iter.hasNext()) {
						Entry entry = (Entry) iter.next();
						Object key = entry.getKey();
						View view = (View) entry.getValue();
						if (key.equals(id)) {
							view.setVisibility(View.VISIBLE);
						} else {
							view.setVisibility(View.GONE);
						}
					}
				}
			});
		}
		
	}
	
	/**
	 * 移除子任务
	 * 
	 * @param iFrameMeta
	 *            子任务显示的父控件
	 * @param id
	 *            子任务的id
	 */
	public void removeTask(final LuaMetatable iFrameMeta, final String id) {
		final IFrame iFrame = (IFrame) iFrameMeta.getView();
		
		if (iFrame.getTaskMap().containsKey(id)) {
			mEMPRender.getGUIFactory().addGUITask(new Runnable() {

				@Override
				public void run() {
					View taskView = iFrame.getTaskMap().get(id);
					// 从子任务列表中移除对应子任务
					iFrame.getTaskMap().remove(id);
					// 将子任务视图从页面上remove
					iFrame.removeView(taskView);
					// 从ActivityGroup中销毁对应子Activity
					((ActivityGroup) mActivity).getLocalActivityManager().destroyActivity(id, true);
					// 显示剩余子任务中的第一个
					Iterator<Entry<String, View>> iter = iFrame.getTaskMap().entrySet().iterator();
					if (iter.hasNext()) {
						Entry entry = (Entry) iter.next();
						View view = (View) entry.getValue();
						view.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	}

}
