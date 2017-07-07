package com.rytong.emp.test.lua;

import android.app.Activity;
import android.content.Context;

import com.aeye.face.detection.MainActivity;
import com.rytong.emp.lua.java.CLuaFunction;
import com.rytong.emp.render.EMPRender;

public class LuaPush {
	/** empRender对象。 */
	static EMPRender mEMPRender = null;
	Context mContext;
	
	public static String registId = null; 
		/**
	 * 构造方法。
	 * @param empRender empRender对象。
	 */
	public LuaPush(Context context, EMPRender empRender) {
		mContext = context;
		mEMPRender = empRender;
	}
	
	public String getRegisterID(){
		if(registId != null){
			return registId;
		}
		return null;
	}
}
