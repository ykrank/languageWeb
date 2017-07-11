package com.rytong.emp.test.lua;

import com.rytong.emp.lua.java.CLua;

public class APPLua {

	/** .so库名称  */
	public final static String APPLUA_LIB = "applua";
	
	/**
	 * 注册lua 接口\方法
	 * 
	 * @param clua	CLua
	 */
	public native static void addLua(CLua clua);
}
