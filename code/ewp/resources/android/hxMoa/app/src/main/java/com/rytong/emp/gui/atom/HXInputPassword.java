package com.rytong.emp.gui.atom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

import com.rytong.emp.dom.Entity;
import com.rytong.emp.tool.HXUtils;
import com.rytong.emp.tool.Utils;
import org.w3c.dom.Element;
import com.rytong.emp.tool.HXControlConfig;
import com.rytong.emp.gui.atom.keyboard.HXEMPSecurityView;
import com.rytong.emp.gui.atom.keyboard.HXSecurityButton;
import com.rytong.emp.gui.atom.keyboard.HXSecurityKeyboard.KEYBOARD_TYPE;
import com.rytong.emp.gui.atom.keyboard.helper.HXEMPKeyboardHelper;
import com.rytong.emp.gui.atom.keyboard.helper.HXEMPKeyboardStateListener;
import com.rytong.emp.gui.atom.keyboard.helper.HXKeyBoardUtils;
import com.rytong.emp.gui.atom.keyboard.helper.HXRYTKeyboardManager;
import com.rytong.emp.gui.atom.keyboard.helper.HXSecurityKeyboardHelper;
import com.rytong.emp.gui.atom.keyboard.helper.RYTKeyboardManager;

import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * <p>
 * {@literal <input type="password">}标签。<br>
 * 密码输入框。
 * </p>
 */
public class HXInputPassword extends InputText implements HXEMPSecurityView{
	/** 密码键盘管理器 */
	protected HXSecurityKeyboardHelper mSecurityKeyBoardHelper;
	
	/** 键盘管理类 **/
	protected HXRYTKeyboardManager mKeyBoardManager = HXRYTKeyboardManager.getInstance();

	/** 密码输入框对应节点 */
	protected Element mElement = null;

	/** 最大输入的字符数量 */
	private int mMaxLength = 99;
	private int mMinLength = 0;
	/** 用于密码内容的字符 */
	private String mSubstitute = HXControlConfig.HXInputPassword.DEF_SUBSTITUTE;
	/** 加密信息 */
	private List<String> mContent = null;
	/** 关闭键盘时是否需要清理焦点 */
	private boolean mIsOnFocusChange = false;

	public HXInputPassword(Context context) {
		super(context);
		mInputText = this;
		mContext = context;
		mActivity = (Activity) context;

		// setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

	@Override
	public void init(Element element) {
		super.init(element);
		mElement=element;
		mSecurityKeyBoardHelper = new HXSecurityKeyboardHelper(mActivity, element, this);
		mSecurityKeyBoardHelper.setKeyBoardType(HXControlConfig.HXInputPassword.DEF_KEYBOARD_TYPE);
		mSecurityKeyBoardHelper.setIsOrder(HXControlConfig.HXInputPassword.DEF_IS_ORDER);
		mSecurityKeyBoardHelper.setEmpKeyBoardStateListener(new HXEMPKeyboardStateListener() {
			@Override
			public void onOpenListener() {
			}

			@Override
			public void onCloseListener() {
				// 安全键盘消失时，密码框失去焦点
				if (!isOnFocusChange()) {
					clearOwnFocus();
				}
				setOnFocusChange(false);
			}

			@Override
			public void onKeyDown(String key) {
				if (mReturnKeyAction != null && key.equals(HXEMPKeyboardHelper.KEY_CONFIRM)) {
					// mLayout.getEMPRender().doLua(mReturnKeyAction);
					mLayout.getEMPRender().doLua(mReturnKeyAction, 0, 0);
				}
			}
		});

		mElement = element;
		// 设置输入框的加密方式。
		if (element.hasAttribute(Entity.NODE_ATTRIBUTE_ENCRYPT_MODE)) {
			//String encryptMode = element.getAttribute(Entity.NODE_ATTRIBUTE_ENCRYPT_MODE);
			/* 进行加密。 */
		}

		// 设置输入框的限制类型。
		if (element.hasAttribute(Entity.NODE_ATTRIBUTE_STYLE)) {
			String styleString = element.getAttribute(Entity.NODE_ATTRIBUTE_STYLE);
			if (!Utils.isEmpty(styleString)) {
				if (styleString.contains(WAP_INPUT_FORMAT)) {
					if (styleString.indexOf("'n'") != -1 || styleString.indexOf("'N'") != -1) {
						// 表示密码只能是数字
						mSecurityKeyBoardHelper.setKeyBoardType(KEYBOARD_TYPE.NUMBER);
						mSecurityKeyBoardHelper.setTypeChangeLock(true);
					}
					setFilters(new InputFilter[0]);
				}
			}
		}
		
		String type = element.getAttribute(Entity.NODE_ATTRIBUTE_TYPE);
		if ("password-num".equals(type)) {
			mSecurityKeyBoardHelper.setKeyBoardType(KEYBOARD_TYPE.NUMBER);
			mSecurityKeyBoardHelper.setTypeChangeLock(true);
		}

		if (element.hasAttribute(Entity.NODE_ATTRIBUTE_PASSWORD_IS_ORDER)) {
			String is = element.getAttribute(Entity.NODE_ATTRIBUTE_PASSWORD_IS_ORDER);
			mSecurityKeyBoardHelper.setIsOrder(Boolean.parseBoolean(is));
		}

		if (element.hasAttribute(Entity.NODE_ATTRIBUTE_PASSWORD_SUBSTITUTE)) {
			String mChar = element.getAttribute(Entity.NODE_ATTRIBUTE_PASSWORD_SUBSTITUTE);
			mSubstitute = Utils.isEmpty(mChar) ? mSubstitute : mChar.substring(0, 1);
		}

		String minleng = element.getAttribute(Entity.NODE_ATTRIBUTE_MINLENG);
		if (!Utils.isEmpty(minleng)) {
			mMinLength = (int) Utils.objectToFloat(minleng);
		}
		String maxleng = element.getAttribute(Entity.NODE_ATTRIBUTE_MAXLENG);
		if (!Utils.isEmpty(maxleng)) {
			mMaxLength = (int) Utils.objectToFloat(maxleng);
		}

		if (element.hasAttribute(Entity.NODE_ATTRIBUTE_VALUE)) {
			if (mContent == null) {
				mContent = new ArrayList<String>();
			}
			String text = element.getAttribute(Entity.NODE_ATTRIBUTE_VALUE);
			addStringInfo(text);
		}
		// 处理clear-mode
		if (element.hasAttribute(Entity.NODE_ATTRIBUTE_TEXT_CLEAR_MODE)) {
			if (mClearButton != null) {
				mClearButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						addStringInfo("");// 清空缓存里保存的密码
					}
				});
			}
		}
	}

	/**
	 * 获取焦点是否改变
	 * 
	 * @return
	 */
	public boolean isOnFocusChange() {
		return mIsOnFocusChange;
	}

	/**
	 * 设置mIsOnFocusChange
	 * 
	 * @param changed
	 */
	public void setOnFocusChange(boolean changed) {
		mIsOnFocusChange = changed;
	}

	/**
	 * <p>
	 * 获得密码信息。
	 * </p>
	 * 
	 * @return 密码信息。
	 */
	public String getResult() {
		if (mContent == null) {
			mContent = new ArrayList<String>();
		}
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < mContent.size(); i++) {
			result.append(HXSecurityButton.getDecryptedInfo(mContent.get(i)));
		}
		return result.toString();
	}

	private void addStringInfo(String text) {
		setText("");
		mContent.clear();
		int length = text.length() > mMaxLength ? mMaxLength : text.length();
		text = text.substring(0, length);
		for (int i = 0; i < length; i++) {
			// 结果集中添加一条记录
			String msg = HXSecurityButton.getEncryptedInfo(text.substring(i, i + 1));
			addOneInfo(msg);
		}
	}

	/**
	 * <p>
	 * 向结果集中添加一条信息。
	 * </p>
	 * 
	 * @param infoItem 待添加的信息。
	 */
	@Override
	public void addOneInfo(String infoItem) {
		try {
			if (mContent == null) {
				mContent = new ArrayList<String>();
			}
			// 获得光标的位置。
			int currentLocation = getSelectionStart();
			// 判断是否超过了最大输入字符限制。
			String oldText = getText() != null ? getText().toString() : "";
			if (oldText.length() < mMaxLength) { // 未达到输入字符上限，还可以进行输入。
				// 文本框中添加一个字符。
				String oldTextHead = oldText.substring(0, currentLocation);
				String oldTextTail = oldText.substring(currentLocation);
				setText(oldTextHead.concat(mSubstitute).concat(oldTextTail));
				// 结果集中添加一条记录。
				mContent.add(currentLocation, infoItem);
				// 重设光标的位置。
				setSelection(currentLocation + 1);
			}
		} catch (Exception e) {
			Utils.printException(e);
		}
	}

	/**
	 * <p>
	 * 从结果集中删除一条信息。
	 * </p>
	 */
	@Override
	public void deleteOneInfo() {
		// 获得光标的位置。
		int currentLocation = getSelectionStart();
		if (currentLocation > 0) {
			// 文本框中删除一个字符。
			String oldText = getText() != null ? getText().toString() : "";
			String oldTextHead = oldText.substring(0, currentLocation - 1);
			String oldTextTail = oldText.substring(currentLocation);
			setText(oldTextHead.concat(oldTextTail));
			// 重设光标位置。
			setSelection(currentLocation - 1);
			if (mContent != null && mContent.size() > 0) {
				// 结果集中删除一条记录。
				mContent.remove(currentLocation - 1);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isEnabled()) {
			int action = event.getAction();
			switch (action) {
				case MotionEvent.ACTION_DOWN:
					// 屏蔽系统输入框
					HXKeyBoardUtils.hideSoftInput(this, mActivity);
					requestFocus();
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
			}
		} else {
			// enable = false 不需要拦截事件
			return false;
		}
        return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if (isEnabled() && focused) {
			mKeyBoardManager.openKeyBoard(mSecurityKeyBoardHelper);
//			mPropertyImg.setEditing(getText().length() >= 0);
//			mPropertyImg.applyImage(mInputText, getWidth() - mBgStyle.getBorderWidths()[0]- mBgStyle.getBorderWidths()[2], getHeight() - mBgStyle.getBorderWidths()[1] - mBgStyle.getBorderWidths()[3]);
			handleClearButton();
		} else {
			mKeyBoardManager.closeKeyBoard(mSecurityKeyBoardHelper);
//			mPropertyImg.setEditing(false);
//			mPropertyImg.applyImage(mInputText, getWidth() - mBgStyle.getBorderWidths()[0]- mBgStyle.getBorderWidths()[2], getHeight() - mBgStyle.getBorderWidths()[1] - mBgStyle.getBorderWidths()[3]);
			handleClearButton();
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mKeyBoardManager.isKeyBoardOpened()) {
			mKeyBoardManager.closeKeyBoard(mSecurityKeyBoardHelper);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onTextContextMenuItem(int id) {
		// 禁止密码输入框的复制粘贴功能
		final int ID_SELECT_ALL = android.R.id.selectAll;
		final int ID_CUT = android.R.id.cut;
		final int ID_COPY = android.R.id.copy;
		final int ID_PASTE = android.R.id.paste;
		if (id == ID_SELECT_ALL || id == ID_CUT || id == ID_COPY || id == ID_PASTE) {
			return false;
		}
		return super.onTextContextMenuItem(id);
	}
	
	@Override
	public String getPropertyValue(String name) {
		if (name.equals(Entity.NODE_ATTRIBUTE_VALUE)) {
			
			String value = getResult();
			value = pwdEncrypt(value);
			
			return value;
		}
		return super.getPropertyValue(name);
	}
	
	private String pwdEncrypt(String value) {
		try {
			String encryptModel = mElement.getAttribute("encryptModel");
			if("DES:RSA".equals(encryptModel)) {
				String key = HXUtils.getRandomHexString(32);
				String valueDES = HXUtils.doDes(key, value);
				key = "301C0410"+key+"04089999999999999999";
				String valueRSA = HXUtils.doRsa(HXUtils.hexStr2Bytes(key));
				
				return valueDES+valueRSA;
			} else if ("RSA".equals(encryptModel)) {
				return HXUtils.doRsa(value.getBytes("utf-8"));
			}
		} catch (Exception ev) {
			ev.printStackTrace();
		}
		return value;
	}

}
