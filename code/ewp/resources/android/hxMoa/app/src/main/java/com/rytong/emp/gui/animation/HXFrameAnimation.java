package com.rytong.emp.gui.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.rytong.emp.data.Resources;
import com.rytong.emp.gui.GUIFactory;
import com.rytong.emp.gui.atom.atomrela.SetBg;
import com.rytong.emp.render.EMPRender;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;

public class HXFrameAnimation extends AnimationDrawable {

	/** 保存当前页面所有帧动画，用于切换页面时清楚所有动画 */
	public static List<HXFrameAnimation> mFrameAnimationList = new ArrayList<HXFrameAnimation>();
	
	public int mX = 0;
	public int mY = 0;
	public int mW = 0;
	public int mH = 0;
	private List<String> mFrames = null;
	private int mSize = 0;
	private boolean mTimes = false;
	private int mRepeatCount = -1;
	
	private int mLuaIndex = -1;
	private int mStartCallIndex = 0;
	private int mStopCallIndex = 0;
	
	@Override
	public void start() {
		super.start();
	}
	
	@Override
	public void stop() {
		super.stop();
	}
	
	public void setFrameImage(List<String> list) {
		mFrames = list;
	}

	public void setFrameSize(float x, float y, float w, float h) {
		mX = (int) x;
		mY = (int) y;
		mW = (int) w;
		mH = (int) h;
	}

	public void setDuration(EMPRender empRender, int duration) {
		if (mFrames != null && !mFrames.isEmpty()) {
			Resources resources = empRender.getResources();
			final GUIFactory guiFactory = empRender.getGUIFactory();
			mSize = mFrames.size();
			final int time = (duration * 1000) / mSize;
			String pngName = null; // 图片的名字。
			for (int i = 0; i < mSize; i++) {
				pngName = mFrames.get(i).trim();
				resources.getStyleImage(pngName, new SetBg() {

					@Override
					public void setBgDrawable(boolean isNeedUIThread, final Bitmap bitmap) {
						guiFactory.addGUITask(new Runnable() {
							
							@Override
							public void run() {
								if (bitmap == null) {
									addFrame(new ColorDrawable(), time);
								} else {
									addFrame(new BitmapDrawable(bitmap), time);
								}
							}
						});
					}
				});
			}
		}
	}

	public void setRepeatCount(int repeatCount) {
		mTimes = false;
		if (repeatCount > 0) {
			mTimes = true;
			mRepeatCount = repeatCount;
			setOneShot(false);
		} else if (repeatCount == -1) {
			setOneShot(false);
		} else {
			setOneShot(true);
		}
	}

	@Override
	public boolean selectDrawable(int idx) {
		if (mTimes) {
			if (mRepeatCount <= 0) {
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						stop();
					}
				}, 10);
				return true;
			}
			if (mSize - 1 <= idx) {
				mRepeatCount -= 1;
			}
		}
		return super.selectDrawable(idx);
	}
}
