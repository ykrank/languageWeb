package com.rytong.emp.gui.atom.keyboard;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;

import com.rytong.emp.dom.Entity;
import com.rytong.emp.tool.Utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 系统键盘弹出，隐藏监听方法
 * 
 * @author liu.dm
 * 
 */
public class HXSoftKeyboardStateHelper implements ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalFocusChangeListener {

	/**
	 * 键盘打开关闭监听接口
	 *
	 */
	public interface SoftKeyboardStateListener {
		/**
		 * 系统键盘打开
		 * @param keyboardHeightInPx	键盘高度(单位 px)
		 */
		void onSoftKeyboardOpened(int keyboardHeightInPx);
		
		void onSoftKeyboardFoucusChanged(View oldFocus, View newFocus);
		
		/**
		 * 系统键盘关闭
		 */
		void onSoftKeyboardClosed();
	}

	private final List<SoftKeyboardStateListener> mListeners = new LinkedList<SoftKeyboardStateListener>();
	private final View mView; // 设置该监听的View
	/* 上次系统键盘打时其高度值(单位 px) */
	private int mLastSoftKeyboardHeightInPx;
	/* 系统键盘是否已打开(关闭时使用) */
	private boolean mIsSoftKeyboardOpened;

	public HXSoftKeyboardStateHelper(View activityRootView) {
		this(activityRootView, false);
	}

	public HXSoftKeyboardStateHelper(View activityRootView, boolean isSoftKeyboardOpened) {
		this.mView = activityRootView;
		this.mIsSoftKeyboardOpened = isSoftKeyboardOpened;
		this.mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
		this.mView.getViewTreeObserver().addOnScrollChangedListener(this);
		this.mView.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
	}
	
	public HXSoftKeyboardStateHelper(Element element) {
		this(element, false);
	}
	
	public HXSoftKeyboardStateHelper(Element element, boolean isSoftKeyboardOpened) {
		this.mView = (View) element.getUserData(Entity.NODE_USER_VIEW);
		this.mIsSoftKeyboardOpened = isSoftKeyboardOpened;
		this.mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
		this.mView.getViewTreeObserver().addOnScrollChangedListener(this);
		this.mView.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
	}
	
	@Override
	public void onGlobalLayout() {
		
		final Rect r = new Rect();
		// r will be populated with the coordinates of your view that area still
		// visible.
		mView.getWindowVisibleDisplayFrame(r);
		final int heightDiff = mView.getRootView().getHeight() - r.top - r.height();
		
		if (!mIsSoftKeyboardOpened && heightDiff > 100) {
			// if more than 100 pixels, its probably a keyboard
			mIsSoftKeyboardOpened = true;
			notifyOnSoftKeyboardOpened(heightDiff);
		} else if (mIsSoftKeyboardOpened && heightDiff < 100) {
			mIsSoftKeyboardOpened = false;
			notifyOnSoftKeyboardClosed();
		}
	}

	@Override
	public void onGlobalFocusChanged(View oldFocus, View newFocus) {
		notifyOnSoftKeyboardFoucusChanged(oldFocus,newFocus);
	}

	public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
		this.mIsSoftKeyboardOpened = isSoftKeyboardOpened;
	}

	/**
	 * 判断系统键盘是否已打开
	 * @return
	 */
	public boolean isSoftKeyboardOpened() {
		return mIsSoftKeyboardOpened;
	}

	/**
	 * Default value is zero (0)
	 * 
	 * @return last saved keyboard height in px
	 */
	public int getLastSoftKeyboardHeightInPx() {
		return mLastSoftKeyboardHeightInPx;
	}

	/**
	 * 添加监听
	 * @param listener
	 */
	public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
		mListeners.add(listener);
	}

	
	/**
	 * 移除监听
	 * @param listener
	 */
	public void removeSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
		mListeners.remove(listener);
	}

	private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
		this.mLastSoftKeyboardHeightInPx = keyboardHeightInPx;

		for (SoftKeyboardStateListener listener : mListeners) {
			if (listener != null) {
				listener.onSoftKeyboardOpened(keyboardHeightInPx);
			}
		}
	}

	private void notifyOnSoftKeyboardClosed() {
		for (SoftKeyboardStateListener listener : mListeners) {
			if (listener != null) {
				listener.onSoftKeyboardClosed();
			}
		}
	}

	private void notifyOnSoftKeyboardFoucusChanged(View oldFocus, View newFocus) {
		for (SoftKeyboardStateListener listener : mListeners) {
			if (listener != null) {
				listener.onSoftKeyboardFoucusChanged(oldFocus,newFocus);
			}
		}
	}

	@Override
	public void onScrollChanged() {
	}

}
