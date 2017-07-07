package com.rytong.emp.gui.atom.keyboard.helper;

import org.w3c.dom.Element;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rytong.emp.gui.atom.HXInputPassword;
import com.rytong.emp.gui.atom.keyboard.HXEMPSecurityView;
import com.rytong.emp.gui.atom.keyboard.HXSecurityKeyboard;
import com.rytong.emp.gui.atom.keyboard.HXSecurityKeyboard.KEYBOARD_TYPE;
import com.rytong.emp.gui.atom.keyboard.HXSecurityKeyboard.OnKeyDownListener;
import com.rytong.emp.tool.Utils;

/**
 * 密码键盘管理类<br>
 * 说明：该类对外开放，而键盘的真正实现细节需要封装起来<br>
 * 如果项目需要开放键盘的某些功能，请通过该类开放<br>
 */
public class HXSecurityKeyboardHelper extends HXEMPKeyboardHelper {

	private static HXSecurityKeyboard mSecurityKeyboard = null;

	private KEYBOARD_TYPE mKeyBoardType = KEYBOARD_TYPE.LITTLE_STRING;
	private boolean mTypeChangeLock = false;
	private boolean mIsOrder = false;

	public HXSecurityKeyboardHelper(Activity activity, Element element, HXEMPSecurityView view) {
		Context context = activity.getApplicationContext();
		mSecurityKeyboard = HXSecurityKeyboard.getInstance(context);
		if (view instanceof TextView) {
			mActivity = activity;
			mView = (TextView) view;
			mElement = element;
		} else {
			Utils.printException(new Exception("view must be instanceof TextView"));
		}
	}
	

	/**
	 * 密码键盘是否乱序
	 * 
	 * @param mIsOrder
	 */
	public void setIsOrder(boolean mIsOrder) {
		this.mIsOrder = mIsOrder;
	}

	/**
	 * 密码键盘类型
	 * 
	 * @param keyboardType
	 */
	public void setKeyBoardType(KEYBOARD_TYPE keyboardType) {
		mKeyBoardType = keyboardType;
	}

	/**
	 * 密码键盘是否可以转换类型
	 * 
	 * @param typeChangeLock
	 */
	public void setTypeChangeLock(boolean typeChangeLock) {
		mTypeChangeLock = typeChangeLock;
	}

	/********************** 以下方法仅供KeyBoardMageger调用,请不要开放给项目,也不建议自行使用 ，如需使用请在KeyBoardMageger中二次封装 ***********************************/

	@Override
	public boolean isOpened() {
		return mSecurityKeyboard.isShowing();
	}

	@Override
	public void openKeyboard() {
		if (mElement == null || mView == null || mActivity == null) {
			return;
		}
		// 关闭系统键盘
		HXKeyBoardUtils.hideSoftInput((TextView) mView, mActivity);
		mSecurityKeyboard.setPasswordTextView((HXEMPSecurityView) mView);
		mSecurityKeyboard.setIsOrder(mIsOrder);
		mSecurityKeyboard.setKeyboardType(mKeyBoardType);
		mSecurityKeyboard.setTypeChangeLock(mTypeChangeLock);
		mSecurityKeyboard.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// 点击物理返回 \ hide \ ok 均需要清空焦点否则之前存在普通输入框时 , 系统键盘的[下一步]失效
				// 弹出键盘后，控件握有焦点，当收回键盘后，如果mView.isFocused()依然为true，则表示焦点并未改变，否则焦点已经改变。此处参数为mView.isFocused()的非。
				((HXInputPassword)mView).setOnFocusChange(!mView.isFocused());
				
				if (mSecurityKeyboard.getScrollDistance() != 0) {
					HXKeyBoardUtils.scroll(-mSecurityKeyboard.getScrollDistance(), mElement, mCheckGUIWindow);
					mSecurityKeyboard.setScrollDistance(0);
				}
				onClose();
			}
		});
		mSecurityKeyboard.setOnKeyDownListener(new OnKeyDownListener() {
			@Override
			public void onSecurityKeyDown(String tag) {
				if (tag.equals(mSecurityKeyboard.HIDE_BUTTON)) {
					onKeyDown(KEY_CANCLE);
				}
			}
		});
		// 滚动键盘
		int distance = HXKeyBoardUtils.computeScrollDistance((View) mView, mElement, mSecurityKeyboard.getHeight());
		if (distance != 0) {
			HXKeyBoardUtils.scroll(distance, mElement);
		}
		mSecurityKeyboard.setScrollDistance(distance);

		// 展示键盘
		mSecurityKeyboard.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER | Gravity.BOTTOM, 0, 0);
		onOpen();
	}

	@Override
	public void closeKeyBoard() {
		mSecurityKeyboard.closeSecurityKeyboard();
	}

	@Override
	public String getKeyBoardName() {
		return "EMPPasswordKeyBoard";
	}

}
