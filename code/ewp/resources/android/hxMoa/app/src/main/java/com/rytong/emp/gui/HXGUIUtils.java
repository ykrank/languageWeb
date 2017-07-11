package com.rytong.emp.gui;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.rytong.emp.android.AndroidEMPBuilder;
import com.rytong.emp.data.AndroidResources;
import com.rytong.emp.gui.atom.InputText;
import com.rytong.emp.tool.ControlConfig;

public class HXGUIUtils {

	private static int mCurViewID = 0;
	
	static int getRandomViewID() {
		mCurViewID++;
		return mCurViewID;
	}
	
	public static void resetViewID() {
		mCurViewID = 0;
	}
	
	/**
	 * TextView UI线程中 设置 图片的位置 
	 * @param view
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public static void setCompoundDrawables(final TextView view, final Drawable left,
			final Drawable top, final Drawable right, final Drawable bottom) {

		if (Thread.currentThread().getId() == 1) {
			setCompoundDrawables(left, top, right, bottom, view);
		} else {
			AndroidEMPBuilder.getActivity(AndroidResources.getInstance().getEMPRender())
					.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							setCompoundDrawables(left, top, right, bottom, view);
							view.postInvalidate();
						}
					});
		}
	}
	
	private static void setCompoundDrawables(Drawable left, Drawable top, Drawable right,
			Drawable bottom, TextView view) {
		if (view instanceof InputText && ControlConfig.InputText.DEF_PADDING_LEFT == 0) {
			view.setPadding(left != null ? 0 : view.getPaddingLeft(), view.getPaddingTop(),
					view.getPaddingRight(), view.getPaddingBottom());
		}
		view.setCompoundDrawables(left, top, right, bottom);
	}
}
