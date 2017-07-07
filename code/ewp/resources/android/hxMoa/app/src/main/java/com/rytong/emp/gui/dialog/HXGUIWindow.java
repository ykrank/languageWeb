package com.rytong.emp.gui.dialog;

import org.w3c.dom.Element;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;

import com.rytong.emp.EMPBuilder;
import com.rytong.emp.android.AndroidEMPBuilder;
import com.rytong.emp.dom.Entity;
import com.rytong.emp.dom.Screen;
import com.rytong.emp.dom.css.Layout;
import com.rytong.emp.gui.animation.animationrela.HXGUIAnimation;
import com.rytong.emp.tool.Utils;

public class HXGUIWindow {

	private Element mElement = null;
	private Layout mLayout = null;
	private int mVisibility = -1; // 记录show 出的控件是否是隐藏的
	private ViewGroup mViewGroup =null;// 记录show出控件的父控件
	private int mViewPosition = -1;// 记录show 出控件在父控件中的position
	private int mWindowTag;
	private boolean mIsModal = false;

	private View mContentView = null;
	private View mParentView = null;
	private Rect mSpaceRect = null;

	private Context mContext = null;
	private ViewGroup mPopView = null;

	private Dialog mDialog = null;
	private Window mWindow = null;
	private WindowManager.LayoutParams mWindowLp;
	
	private int mTop = 0;
	
	public HXGUIWindow(Element element, boolean modal) {
		mElement = element;
		mIsModal = modal;
		mContentView = (View) element.getUserData(Entity.NODE_USER_VIEW);
		mLayout = (Layout) element.getUserData(Entity.NODE_USER_STYLE);
		mSpaceRect = mLayout.getDisplaySpace();
		mViewGroup = (ViewGroup) mContentView.getParent();
		if (mViewGroup != null) {
			mViewPosition = mViewGroup.indexOfChild(mContentView);
		}
		// 为了实现popWindow内对物理键的监听，增加一层view
		mPopView = new AbsoluteLayout(mContentView.getContext());
		mPopView.setBackgroundColor(Color.TRANSPARENT);
		mPopView.setEnabled(true);
		mPopView.setFocusable(true);
		mPopView.setFocusableInTouchMode(true);
	}

	public Element getElement() {
		return mElement;
	}
	
	/**
	 * 获取window的布局参数。
	 * @return window的布局参数。
	 */
	public WindowManager.LayoutParams getWindowLayoutParams(){
		return mWindowLp;
	}

	public void setParentView(View parentView) {
		mParentView = parentView;
		mContext = mParentView.getContext();
	}

	/**
	 * 展示guiWindow<br>
	 * 当window.isShowing=true时客户端不做任何处理
	 * 
	 * @param animation
	 * @param empBuilder
	 */
	public void onShow(int animation, final EMPBuilder empBuilder) {
		
		AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(mSpaceRect.width(), mSpaceRect.height(), 0, 0);
		mContentView.setLayoutParams(lp);
		if (mDialog == null || mWindow == null) {
			
			if (mViewGroup != null) {
				mViewGroup.removeView(mContentView);
			}
			mPopView.addView(mContentView);
			
			//
			mDialog = new Dialog(mContext, Utils.getResourcesId(mContext, "dialogWindow", "style"));
			mDialog.setCancelable(false);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setContentView(mPopView);
			
			mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN && empBuilder != null && empBuilder instanceof AndroidEMPBuilder 
							&& ((AndroidEMPBuilder) empBuilder).onKeyDown(keyCode, event)) {
						return true;
					}
					return false;
				}
			});
			
			mWindow = mDialog.getWindow();
			// 面板窗口，显示于宿主窗口上层( 弹出对话框dialog 和 密码键盘popupwindow之下)
			mWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
			mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
			mWindowLp = mWindow.getAttributes();
			mWindowLp.gravity = Gravity.TOP | Gravity.LEFT;

			if (!mIsModal) { //非模态
				mWindow.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
			}
		} 
		
		mTop = Screen.mStatusBarHeight + mSpaceRect.top;
		
		mWindowLp.x = mSpaceRect.left;
		mWindowLp.y = mTop;
		
		mWindowLp.width = mSpaceRect.width();
		mWindowLp.height = mSpaceRect.height();
		
		mWindow.setWindowAnimations(setDialogAnimationIn(animation));
		
		// 确保控件是可见的
		mVisibility = mContentView.getVisibility();
		if (mVisibility != View.VISIBLE) {
			mContentView.setVisibility(View.VISIBLE);
			mLayout.changeStyle(Entity.NODE_STYLE_DISPLAY, false);
		}
		
		if (mSpaceRect.top > Screen.mHeight * 0.5) {
			mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}
		if (mSpaceRect.height() > Screen.mHeight * 0.5) {
			mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		}

		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	/**
	 * 隐藏guiWindow<br>
	 * 
	 * @param animation
	 */
	public void onHide(int animation) {
		mWindow.setWindowAnimations(setDialogAnimationOut(animation));
		mParentView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mDialog.dismiss();
				mDialog = null;
				
				if (mVisibility != View.INVISIBLE) {
					mContentView.setVisibility(View.INVISIBLE);
					mLayout.changeStyle(Entity.NODE_STYLE_DISPLAY, true);
					mVisibility = View.INVISIBLE;
				}
				// 恢复控件的属性(所属父控件 、 display样式)
				mPopView.removeView(mContentView);
				if (mViewGroup != null) {
					if (mViewPosition != -1) {
						mViewGroup.addView(mContentView, mViewPosition);
					} else {
						mViewGroup.addView(mContentView);
					}
				}
				// 如果hide的页面是通过showContent显示出来的，则hide的时候需要清除老页面上的id。
				if (mViewGroup == null) {
//					mLayout.getEMPRender().getEMPDocument().clearSearchCache(mElement, true);
				}
			}
		}, 10);
	}

	private int setDialogAnimationIn(int type) {
		int animation = 0;
		switch (type) {
		case HXGUIAnimation.SLIDE_FROM_LEFT:
			animation = Utils.getResourcesId(mContext, "dialogWindowLeft", "style");
			break;
		case HXGUIAnimation.SLIDE_FROM_RIGHT:
			animation = Utils.getResourcesId(mContext, "dialogWindowRight", "style");
			break;
		case HXGUIAnimation.SLIDE_FROM_UP:
			animation = Utils.getResourcesId(mContext, "dialogWindowUp", "style");
			break;
		case HXGUIAnimation.SLIDE_FROM_DOWN:
			animation = Utils.getResourcesId(mContext, "dialogWindowDown", "style");
			break;
		}
		return animation;
	}

	private int setDialogAnimationOut(int type) {
		int animation = 0;
		switch (type) {
		case HXGUIAnimation.SLIDE_FROM_LEFT:
			animation = Utils.getResourcesId(mContext, "dialogWindowLeftOut", "style");
			break;
		case HXGUIAnimation.SLIDE_FROM_RIGHT:
			animation = Utils.getResourcesId(mContext, "dialogWindowRightOut", "style");
			break;
		case HXGUIAnimation.SLIDE_FROM_UP:
			animation = Utils.getResourcesId(mContext, "dialogWindowUpOut", "style");
			break;
		case HXGUIAnimation.SLIDE_FROM_DOWN:
			animation = Utils.getResourcesId(mContext, "dialogWindowDownOut", "style");
			break;
		}
		return animation;
	}

	/**
	 * 滚动guiWinow
	 * 
	 * @param distance
	 */
	public void scroll(int distance) {
		if (distance > 0) {
			// 允许窗口扩展到屏幕之外
			mWindow.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			// setClippingEnabled(false)会导致android不处理系统键盘挡住输入框的情况，因此自定义键盘回滚以后恢复为true
			mWindow.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
		if (distance != 0) {
			mTop = mTop - distance;
			mWindowLp.y = mTop;
			mWindow.setAttributes(mWindowLp);
		}
	}

	/**
	 * 设置window的tag
	 * 
	 * @param tag
	 */
	public void setWindowTag(int tag) {
		this.mWindowTag = tag;
	}

	/**
	 * 获取window的tag
	 * 
	 * @return
	 */
	public int getWindowTag() {
		return mWindowTag;
	}
	
	public boolean isModal(){
		return mIsModal;
	}

	public void refresh() {
		mSpaceRect = mLayout.getDisplaySpace();
	}
}
