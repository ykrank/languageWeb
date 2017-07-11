package com.rytong.emp.gui.atom.keyboard.helper;

import java.lang.reflect.Method;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.rytong.emp.dom.Entity;
import com.rytong.emp.dom.Screen;
import com.rytong.emp.dom.css.Layout;
import com.rytong.emp.gui.atom.Body;
import com.rytong.emp.gui.atom.InputText;
import com.rytong.emp.gui.atom.keyboard.HXSoftKeyboardStateHelper;
import com.rytong.emp.gui.dialog.HXGUIWindow;
import com.rytong.emp.gui.dialog.HXGUIWindowManager;
import com.rytong.emp.tool.Utils;

/**
 * <p>
 * 键盘工具类<br>
 * </p>
 * <p>
 * 功能:<br>
 * 1.系统键盘的一些操作方法在改类中<br>
 * 2.提供自定义键盘的一些公共方法，如计算键盘移动距离等<br>
 * </p>
 * 
 */
public class HXKeyBoardUtils {
	/* 记录系统键盘高度 */
	private static int mSystemKeyBoardHeight = 0;

	/**
	 * 为系统键盘添加监听
	 * 
	 * @param view
	 * @param onSoftKeyboardOpened
	 * @param onSoftKeyboardClosed
	 */
	public static void addListenerToSoftKeyboard(final Element element,
			final Activity activity) {
		final TextView view = (TextView) element.getUserData(Entity.NODE_USER_VIEW);
		if (element == null || view == null) {
			return;
		}
//		SoftKeyboardStateHelper mSoftkeyboardSateHelper = new SoftKeyboardStateHelper(view);
		final HXSoftKeyboardStateHelper mSoftkeyboardSateHelper = new HXSoftKeyboardStateHelper(element);
		mSoftkeyboardSateHelper.addSoftKeyboardStateListener(new HXSoftKeyboardStateHelper.SoftKeyboardStateListener() {
			@Override
			public void onSoftKeyboardOpened(int keyboardHeightInPx) {
				if (view.isFocused()) {
					// 系统键盘的打开由系统控制，因此这里只是为了让keyboardManager获知系统键盘被打开
					// KeyBoardManager.getInstance().openKeyBoard(SystemKeyBoardHelper.getInstance(activity, element, view));
				}
			}
			
			@Override
			public void onSoftKeyboardClosed() {
				// 关闭键盘是清除焦点
				if (view.isFocused() && view instanceof InputText) {
					((InputText) view).clearOwnFocus();
					// 系统键盘的关闭由系统控制，因此这里只是为了让keyboardManager获知系统键盘被关闭
					// KeyBoardManager.getInstance().closeKeyBoard(SystemKeyBoardHelper.getInstance(activity, element, view));
				}
			}
			
			@Override
			public void onSoftKeyboardFoucusChanged(View oldFocus, View newFocus) {
			}
		});
	}
	
	/**
	 * 为系统键盘添加监听。
	 * @param element 待监听的控件。
	 * @param activity 影响到的activity。
	 * @param softKeyboardStateListener 特别指定的键盘状态改变回调。
	 */
	public static void addListenerToSoftKeyboard(final Element element,
			final Activity activity, final HXSoftKeyboardStateHelper.SoftKeyboardStateListener softKeyboardStateListener){
		final TextView view = (TextView) element.getUserData(Entity.NODE_USER_VIEW);
		if (element == null || view == null || softKeyboardStateListener == null) {
			return;
		}
		final HXSoftKeyboardStateHelper mSoftkeyboardSateHelper = new HXSoftKeyboardStateHelper(element);
		mSoftkeyboardSateHelper.addSoftKeyboardStateListener(softKeyboardStateListener);
	}

	/**
	 * 检测系统键盘是否打开着
	 */
	public static boolean isSoftKeyboardOn(Activity activity) {
		InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		return mInputMethodManager.isActive();
	}

	/**
	 * 打开系统键盘
	 */
	public static void showSoftKeyboard(Activity activity) {
		InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 关闭系统软键盘
	 */
	public static void closeSoftKeyboard(Activity activity) {
		InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() != null) {
			mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 当输入框有焦点时，阻止系统键盘弹出
	 */
	public static void hideSoftInput(TextView view, Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			view.setInputType(InputType.TYPE_NULL);
		} else {
			activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(view, false);
			} catch (NoSuchMethodException e) {
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				try {
					setShowSoftInputOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
					setShowSoftInputOnFocus.setAccessible(true);
					setShowSoftInputOnFocus.invoke(view, false);
				} catch (Exception e1) {
					Utils.printException(e1);
				}
			} catch (Exception e) {
				Utils.printException(e);
			}
		}
	}

	/**
	 * 以下代码用于防止软键盘名字为[[下一个]]或者[[GO]]的按钮失效
	 */
	public static void makeNextButtonWork(TextView view) {
		view.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {

				}
				return false;
			}
		});
	}

	/**
	 * 计算滚动的距离
	 * 
	 * @param inputView
	 *            输入框view
	 * @param inputElement
	 *            输入框Element
	 * @param keyBoardHeight
	 *            键盘高度
	 * @return
	 */
	public static int computeScrollDistance(View inputView, Element inputElement, int keyBoardHeight) {
		int mScrollDistance = 0;
		int viewHeight = inputView.getHeight();
		int[] location = new int[2];
		inputView.getLocationOnScreen(location);
		int inputViewBottomLocation = location[1] + viewHeight; // 输入框的下边沿位置。
		int keyboardTopLocation = Screen.mHeight - keyBoardHeight; // 键盘的上边沿位置。

		// 在目前的实现中，系统键盘的遮挡问题是交由系统处理的。
		// 当系统输入框后跟密码框时，如果选中系统输入框，系统处理输入框上移，这个时候密码框获取location的值就和直接选中密码框不一样。
		// 因此在最后计算滚动距离时，还需要考虑密码框当前的位置，即要考虑body滚动的距离。
		int[] locBody = new int[2];
		int bodyHeight = Screen.mHeight - Screen.mStatusBarHeight; // body的可见高度。
		if (inputElement != null) {
			Element bodyElement = searchBody(inputElement);
			if (bodyElement != null) {
				Body body = (Body) bodyElement.getUserData(Entity.NODE_USER_VIEW);
				body.getLocationOnScreen(locBody);
				
				/*此处需要加上body 已滑动的距离
				 * (onSoftKeyboardOpened(){body滑动distance} - 此处 - onSoftKeyboardClosed(body滑动-distance))
				 * */ 
				int scrollY = body.getScrollDistance(); 
				inputViewBottomLocation += scrollY;
				bodyHeight = body.getHeight();
			}
		}
		// 有的设备上body下面会带有一条虚拟按键区域，此处将这个区域的高度剔除掉。
		int virtualBarHeight = Screen.mHeight - Screen.mStatusBarHeight - bodyHeight;
		inputViewBottomLocation += virtualBarHeight;
		
		// 如果输入框位于dialog内，那么此处需要考虑dialog的滚动距离。
		HXGUIWindow guiWindow = HXGUIWindowManager.checkOnWindow(inputElement);
		if (guiWindow != null) {
			int[] locWindow = new int[2];
			Element windowElement = guiWindow.getElement();
			if (windowElement != null) {
				View windowView = (View) windowElement.getUserData(Entity.NODE_USER_VIEW);
				windowView.getLocationOnScreen(locWindow); // dialog移动之后的坐标。
			}
			int windowTop = guiWindow.getWindowLayoutParams().y; // dialog本来的布局参数，未调用scroll，因此还保持移动之前的数据。
			int windowScrollDistance = windowTop - locWindow[1]; // dialog移动的距离。
			// 输入框会随着dialog而移动，导致计算错误。此处将输入框的移动距离还原，还原后将按照输入框原始的位置计算移动距离。
			int y = inputViewBottomLocation + windowScrollDistance;
			if (y > keyboardTopLocation) {
				mScrollDistance = Math.abs(keyBoardHeight - (Screen.mHeight - y));
			}
			return mScrollDistance;
		}
		
		// body的location计算是根据屏幕上边缘获取的，而实际显示body的上边缘是状态栏的下边缘，计算时需要考虑这个。
		int y = inputViewBottomLocation - locBody[1] + Screen.mStatusBarHeight;
		if (y > keyboardTopLocation) {
			// 需要滚动的距离
			mScrollDistance = Math.abs(keyBoardHeight - (Screen.mHeight - y));
		}
		return mScrollDistance;
	}

	/**
	 * 查找body节点
	 * 
	 * @return Body Element
	 */
	private static Element searchBody(Element element) {
		Node parentNode = (Element) element.getParentNode();

		while (parentNode != null) {
			short nodeType = parentNode.getNodeType();
			if (nodeType == Node.ELEMENT_NODE) {
				Element parentElement = (Element) parentNode;
				if (Entity.NODE_BODY.equals(parentElement.getTagName())) {
					return parentElement;
				} else {
					parentNode = parentElement.getParentNode();
				}
			}
		}
		return null;
	}

	/**
	 * 键盘弹出时滚动body,防止键盘遮挡输入框
	 * 
	 * @param distance
	 *            需要滚动的距离
	 * @param inputElement
	 *            输入框节点
	 */
	public static void scroll(int distance, Element inputElement) {
		// 如果当前输入框在popWinow上，window随之滑动
		if (HXGUIWindowManager.getWindowCacheList() != null) {
			for (HXGUIWindow window : HXGUIWindowManager.getWindowCacheList()) {
				if (HXGUIWindowManager.checkOnWindow(inputElement) != null) {
					window.scroll(distance);
					// 对于position:fixed控件，进行单独的位置移动。
					checkFixedElement(distance, inputElement);
					return;
				}
			}
		}
		// 滚动body
		if (inputElement != null) {
			Element bodyElement = searchBody(inputElement);
			if (bodyElement != null) {
				Body body = (Body) bodyElement.getUserData(Entity.NODE_USER_VIEW);
				body.scroll(distance);
				// 对于position:fixed控件，进行单独的位置移动。
				checkFixedElement(distance, inputElement);
			}
		}
	}
	
	/**
	 * 键盘弹出时滚动body,防止键盘遮挡输入框
	 * 
	 * @param distance
	 *            需要滚动的距离
	 * @param inputElement
	 *            输入框节点
	 * @param guiWindow
	 *            待检测的guiWindow。
	 */
	public static void scroll(int distance, Element inputElement, HXGUIWindow guiWindow){
		if (guiWindow != null) {
			Element windowElement = guiWindow.getElement();
			Element checkElement = inputElement;
			while (checkElement != null) {
				if (checkElement == windowElement) {
					guiWindow.scroll(distance); // 直接滚动guiWindow。
					// 对于position:fixed控件，进行单独的位置移动。
					checkFixedElement(distance, inputElement);
					return;
				} else {
					Node parentNode = checkElement.getParentNode();
					short nodeType = parentNode.getNodeType();
					if (nodeType == Node.ELEMENT_NODE) {
						checkElement = (Element) checkElement.getParentNode();
					} else {
						break;
					}
				}
			}
		}
		scroll(distance, inputElement);
	}
	
	/**
	 * 对带有position:fixed样式的控件进行检查，并单独执行位置移动操作。
	 * @param distance 需要移动的距离。
	 * @param inputElement 待检查的控件的element。
	 */
	private static void checkFixedElement(int distance, Element inputElement){
		Layout layout = (Layout) inputElement.getUserData(Entity.NODE_USER_STYLE);
		if (layout != null && layout.containsStyle(Entity.NODE_STYLE_POSITION)
				&& Entity.NODE_VALUE_FIXED.equals(layout.getStyleByName(Entity.NODE_STYLE_POSITION))) {
			View view = (View) inputElement.getUserData(Entity.NODE_USER_VIEW);
			LayoutParams layoutParams = view.getLayoutParams();
			if (layoutParams instanceof AbsoluteLayout.LayoutParams) {
				((AbsoluteLayout.LayoutParams) layoutParams).y -= distance;
			} else if (layoutParams instanceof LinearLayout.LayoutParams) {
				((LinearLayout.LayoutParams) layoutParams).topMargin -= distance;
			}
			view.setLayoutParams(layoutParams);
		}
	}
	
	/**
	 * 获取系统键盘的高度
	 * @return
	 */
	public static int getKeyBoardHeight() {
		return mSystemKeyBoardHeight;
	}
	
	/**
	 * 设置系统键盘的高度(必须为真实系统键盘高度)
	 * 
	 * @param keyboardHeight
	 */
	public static void setKeyBoardHeight(int keyboardHeight) {
		mSystemKeyBoardHeight = keyboardHeight;
	}
}
