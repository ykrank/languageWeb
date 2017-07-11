package com.rytong.emp.gui.atom.keyboard.helper;

import org.w3c.dom.Element;

import com.rytong.emp.gui.dialog.HXGUIWindow;

import android.app.Activity;
import android.view.View;

/**
 * <p>
 * 自定义键盘抽象类，所有自定义键盘需要实现该接口<br>
 * 说明：<br>
 * 1. 如果项目需要系定义键盘，请参考{@link SecurityKeyboardHelper}为自定义键盘建立helper并实现该接口<br>
 * 2. 在使用自定义键盘时强烈建议通过{@link HXRYTKeyboardManager}开启关闭键盘，以便对键盘的开启关闭有统一的管理 <br>
 * 3. 预使用键盘的监听请在正确的时机调用<br>
 * {@link EMPKeyBoardHelper#onOpen()} <br>
 * {@link EMPKeyBoardHelper#onClose()} <br>
 * <p>
 */
public abstract class HXEMPKeyboardHelper {
	
	public static final String KEY_CANCLE = "cancle";
	public static final String KEY_CONFIRM = "confirm";

	protected HXRYTKeyboardManager mKeyBoardManager = HXRYTKeyboardManager.getInstance();
	protected HXEMPKeyboardStateListener mEmpKeyBoardStateListener = null;

	protected Activity mActivity;
	protected Element mElement;
	protected View mView;
	
	/** 进行弹框检测的guiWindow。 */
	protected HXGUIWindow mCheckGUIWindow = null;

	/**
	 * 为键盘的打开关闭设置监听
	 * 
	 * @param mEmpKeyBoardStateListener
	 */
	public void setEmpKeyBoardStateListener(HXEMPKeyboardStateListener empKeyBoardStateListener) {
		this.mEmpKeyBoardStateListener = empKeyBoardStateListener;
	}

	/**
	 * 键盘是否处于打开状态
	 * 
	 * @return
	 */
	public abstract boolean isOpened();

	/**
	 * 打开键盘
	 */
	public abstract void openKeyboard();

	/**
	 * 关闭键盘
	 */
	public abstract void closeKeyBoard();

	/**
	 * 子类需要在打开键盘时调用该方法
	 */
	public void onOpen() {
		mKeyBoardManager.setCurKeyBoard(this);
		if (mEmpKeyBoardStateListener != null) {
			mEmpKeyBoardStateListener.onOpenListener();
		}
	}

	/**
	 * 子类需要在打开键盘关闭时调用该方法
	 */
	public void onClose() {
		mKeyBoardManager.setCurKeyBoard(null);
		if (mEmpKeyBoardStateListener != null) {
			mEmpKeyBoardStateListener.onCloseListener();
		}
	}

	/**
	 * 子类需要在键盘按键被点击时调用该方法
	 */
	public void onKeyDown(String key) {
		if (mEmpKeyBoardStateListener != null) {
			mEmpKeyBoardStateListener.onKeyDown(key);
		}
	}

	/**
	 * 获取与键盘绑定的activity
	 */
	public Activity getActivity() {
		return mActivity;
	}

	/**
	 * 获取与键盘绑定的节点
	 */
	public Element getElement() {
		return mElement;
	}

	/**
	 * 获取与键盘绑定的view
	 */
	public View getView() {
		return mView;
	}

	/**
	 * 键盘类型标示，同一种键盘应该返回相同标示
	 * 
	 * @return
	 */
	public abstract String getKeyBoardName();

	/**
	 * 键盘类型标示一致的EMPKeyboardHelper则认为这两个对象相等
	 * 
	 * @return
	 */
	public boolean equals(HXEMPKeyboardHelper helper) {
		return helper.getKeyBoardName().equals(getKeyBoardName());
	}

}
