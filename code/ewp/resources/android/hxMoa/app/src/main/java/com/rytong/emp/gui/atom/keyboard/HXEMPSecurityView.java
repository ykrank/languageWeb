package com.rytong.emp.gui.atom.keyboard;

/**
 * 与密码键盘绑定的密码输入框
 */
public interface HXEMPSecurityView {
	
	/**
	 * 插入一个字符
	 */
	public void addOneInfo(String newChar);
	
	/**
	 * 删除一个字符
	 */
	public void deleteOneInfo();

}
