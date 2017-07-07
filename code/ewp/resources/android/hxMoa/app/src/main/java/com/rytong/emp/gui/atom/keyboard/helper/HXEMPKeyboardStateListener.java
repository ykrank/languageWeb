package com.rytong.emp.gui.atom.keyboard.helper;


/**
 * 自定义 键盘状态监听类<br>
 * <p>
 * 预使用该类需要自定义键盘需要在正确的时机调用<br>
 * {@link EMPKeyBoardHelper#onOpen()} <br>
 * {@link EMPKeyBoardHelper#onClose()} <br>
 * </p>
 */
public interface HXEMPKeyboardStateListener {

	/**
	 * 键盘被打开
	 */
	public void onOpenListener();

	/**
	 * 键盘被关闭
	 */
	public void onCloseListener();
	
	/**
	 * 键盘按键被按下
	 */
	public void onKeyDown(String key);

}
