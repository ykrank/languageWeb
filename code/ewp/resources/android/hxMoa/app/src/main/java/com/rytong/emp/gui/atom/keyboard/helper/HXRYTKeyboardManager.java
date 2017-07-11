package com.rytong.emp.gui.atom.keyboard.helper;

import com.rytong.emp.gui.dialog.HXGUIWindow;

import android.app.Activity;

/**
 * <p>
 * 键盘管理器<br>
 * </p>
 * <p>
 * 功能:<br>
 * 1.存储当前被打开的键盘<br>
 * 2.打开/关闭键盘的入口<br>
 * 3.该类为单例模式，使用时请通过{@link HXRYTKeyboardManager#getInstance()}方法获取其实例对象<br>
 * </p>
 * 
 * @author song_danqi
 */
public class HXRYTKeyboardManager {

	private static HXRYTKeyboardManager mKeyBoardManager = new HXRYTKeyboardManager();

	// 当前正在被操作的键盘
	private static HXEMPKeyboardHelper mCurKeyboard = null;

	private HXRYTKeyboardManager() {
	}

	/**
	 * 获取KeyBoardManager实例
	 * 
	 * @return
	 */
	public static HXRYTKeyboardManager getInstance() {
		return mKeyBoardManager;
	}

	/**
	 * 获取当前键盘
	 */
	public HXEMPKeyboardHelper getCurKeyBoard() {
		return mCurKeyboard;
	}

	/**
	 * 设置当前键盘
	 */
	public void setCurKeyBoard(HXEMPKeyboardHelper helper) {
		mCurKeyboard = helper;
	}

	/**
	 * 键盘是否处于打开状态
	 */
	public boolean isKeyBoardOpened() {
		if (mCurKeyboard != null) {
			return mCurKeyboard.isOpened();
		}
		return false;
	}

	/**
	 * 关闭所有键盘（包括系统键盘）<br>
	 * 仅关闭自定义键盘可以用{@link #closeKeyBoard()}<br>
	 */
	public void closeAllKeyBoard(final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 强行关闭系统键盘
				HXKeyBoardUtils.closeSoftKeyboard(activity);
				if (mCurKeyboard != null) {
					closeKeyBoard();
				}
			}
		});
	}
	
	/**
	 * 关闭所有键盘（包括系统键盘），并进行弹出框的检测。
	 * @param activity 影响到的activity。
	 * @param guiWindow 进行弹框检测的guiWindow。
	 * @see #closeAllKeyBoard(Activity)
	 */
	public void closeAllKeyBoard(final Activity activity, final HXGUIWindow guiWindow){
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 强行关闭系统键盘
				HXKeyBoardUtils.closeSoftKeyboard(activity);
				if (mCurKeyboard != null) {
					mCurKeyboard.mCheckGUIWindow = guiWindow;
					closeKeyBoard();
				}
			}
		});
	}

	/**
	 * 关闭当前自定义键盘<br>
	 * 该方法不适用与系统键盘{@link #closeAllKeyBoard()}<br>
	 */
	public void closeKeyBoard() {
		if (mCurKeyboard != null) {
			mCurKeyboard.closeKeyBoard();
			mCurKeyboard = null;
		}
	}

	/**
	 * 关闭某一特定键盘
	 * 
	 * @param keyboard
	 */
	public void closeKeyBoard(HXEMPKeyboardHelper keyboard) {
		if (mCurKeyboard != null && mCurKeyboard.equals(keyboard)) {
			mCurKeyboard.closeKeyBoard();
			mCurKeyboard = null;
		}
	}

	/**
	 * 检测某一特定键盘是否打开
	 * 
	 * @param keyboard
	 * @return
	 */
	public boolean isKeyBoardOpened(HXEMPKeyboardHelper keyboard) {
		return keyboard.isOpened();
	}

	/**
	 * 打开新的键盘<br>
	 * 该方法不适用与系统键盘<br>
	 */
	public void openKeyBoard(HXEMPKeyboardHelper keyboardHelper) {
		// 强行关闭系统键盘
		HXKeyBoardUtils.closeSoftKeyboard(keyboardHelper.getActivity());
		if (mCurKeyboard != null) {
			closeKeyBoard();
		}
		mCurKeyboard = keyboardHelper;
		mCurKeyboard.openKeyboard();
	}

}
