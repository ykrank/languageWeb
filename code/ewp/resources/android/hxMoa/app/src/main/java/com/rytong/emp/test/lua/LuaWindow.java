package com.rytong.emp.test.lua;


import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Element;

import android.app.Activity;

import com.rytong.emp.android.AndroidEMPBuilder;
import com.rytong.emp.gui.GUIFactory.GUIEventListener;
import com.rytong.emp.lua.EMPLua;
import com.rytong.emp.lua.EMPLuaFactory;
import com.rytong.emp.lua.LuaMetatable;
import com.rytong.emp.lua.java.CLuaFunction;
import com.rytong.emp.reader.AttachmentBrowser;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.render.EMPThreadPool;
import com.rytong.emp.tool.Utils;

public class LuaWindow {
	
	Activity mActivity;
	/** empRender对象。 */
	static EMPRender mEMPRender = null;
	
	public static boolean mIsShowLoading;
	static CLuaFunction mCallLua;
	static String mKeyCode;
	
	/**
	 * 构造方法。
	 * @param empRender empRender对象。
	 */
	public LuaWindow(EMPRender empRender) {
		mEMPRender = empRender;
	}
	
	public static void alert(Object[] obj, CLuaFunction callLua) {
		int _callIndex = 0;
		if(callLua!=null) {
			_callIndex = callLua.mFunctionIndex;
		}
		final int callIndex = _callIndex;
		final EMPLua empLua = EMPLuaFactory.getEMPLua(0);
		GUIEventListener listener = new GUIEventListener() {

			@Override
			public void onClick(Object object, int which) {
				if (callIndex != 0) {
					final Object[] param = new Object[] { Integer.valueOf(Math.abs(which) - 1) };
					mEMPRender.runTask(new EMPThreadPool.Task(0) {

						@Override
						public void doRun() throws Exception {
							empLua.callbackAndDispose(callIndex, param);
						}
						
					});
				}
			}
		};
		String[] content = new String[obj.length];
		for (int i = 0; i < obj.length; i++) {
			if (obj[i] != null) {
				content[i] = obj[i].toString();
			} else {
				content[i] = "";
			}
		}
		mEMPRender.alert(listener, content);
	}
	public static void alert(String info1) {
		Object[] obj = new Object[1];
		obj[0] = info1;
		alert(obj, null);
	}
	
	public static void alert(String info1, CLuaFunction callLua) {
		Object[] obj = new Object[1];
		obj[0] = info1;
		alert(obj, callLua);
	}
	
	public static void alert(String info1, String info2, CLuaFunction callLua) {
		Object[] obj = new Object[2];
		obj[0] = info1;
		obj[1] = info2;
		alert(obj, callLua);
	}
	
	public static void alert(String info1, String info2, String info3, CLuaFunction callLua) {
		Object[] obj = new Object[3];
		obj[0] = info1;
		obj[1] = info2;
		obj[2] = info3;
		alert(obj, callLua);
	}
	
	public static void close() {
		mEMPRender.destroy();
	}
	
	public void open(String content) {
		Activity activity = AndroidEMPBuilder.getActivity(mEMPRender);
		AttachmentBrowser browser = new AttachmentBrowser(activity, mEMPRender);
		String errorCode = browser.open(content);
		if (!Utils.isEmpty(errorCode)) {
			mEMPRender.errorAlert(errorCode);
		}
	}
	
	public static void setPhysicalkeyListener(String keyCode, CLuaFunction callLua) {
		mCallLua = callLua;
		mKeyCode = keyCode;
		
		if(mIsShowLoading) 
			return;
		
		if(mCallLua==null || mKeyCode==null) {
			return;
		}
		final int callIndex = mCallLua.mFunctionIndex;
		final EMPLua empLua = EMPLuaFactory.getEMPLua(0);
		AndroidEMPBuilder empBuilder = (AndroidEMPBuilder) mEMPRender.getEMPBuilder();
		if (callIndex != 0) {
			empBuilder.addPhysicalkey(keyCode, new GUIEventListener() {

				@Override
				public void onClick(Object object, int which) {
					empLua.callback(callIndex);
				}
			});
		} else {
			empBuilder.addPhysicalkey(keyCode, null);
		}
	}
	
	public static void setOnPhysKeyListener(CLuaFunction callLua) {
		int _callIndex = 0;
		if(callLua!=null) {
			_callIndex = callLua.mFunctionIndex;
		}
		final int callIndex = _callIndex;
		
		final EMPLua empLua = EMPLuaFactory.getEMPLua(0);
		AndroidEMPBuilder empBuilder = (AndroidEMPBuilder) mEMPRender.getEMPBuilder();
		if (callIndex != 0) {
			empBuilder.setOnPhysKeyListener(new GUIEventListener() {

				/**
				 * object参数在此当做回传的回调参数处理，值为"backspace"，"home"
				 */
				@Override
				public void onClick(Object object, int which) {
					empLua.callback(callIndex, object);
				}
			});
		} else {
			empBuilder.setOnPhysKeyListener(null);
		}
	}
	
	/**
	 * <p>
	 * 一般用于显示loading页面或菜单页,与window:hide()配合使用 <br>
	 * 弹出的界面为模态，即用户只能在当前弹出的界面操作 <br>
	 * </p>
	 * <p>
	 * Syntax: window:showContent(content/path, tag, transitionType)<br>
	 * </p>
	 * 
	 * @param luaIndex
	 * @param content
	 *            可以接受两种参数：<br>
	 *            1.待显示的页面报文内容;<br>
	 *            2.本地报文路径<br>
	 * @param tag
	 *            待显示页面标记
	 * @param transitionType
	 *            页面切换动画类型
	 */
	public static void showContent(String content, int tag, int transitionType, int sltParams) {
		mEMPRender.popupWindow(content, tag, transitionType, true, sltParams);
		
		if(content.contains("FullSLoading1.xml")) {
			if(mTime!=null) {
				mTime.cancel();
				mTime = null;
			}
			mIsShowLoading = true;
		}
	}
	
	public static void showContent(String content, int tag, int transitionType) {
		showContent(content, tag, transitionType, 0);
	}
	
	public static void showContent(String content, int tag) {
		showContent(content, tag, 0, 0);
	}
	
	public static void showControl(LuaMetatable metatable, int tag,
			int transitionType, String isModal) {
		final Element element = metatable.getElement();
		boolean ismodal = true;
		if (isModal != null && isModal.equalsIgnoreCase("false")) {
			ismodal = false;
		}
		mEMPRender.popupWindow(element, tag, transitionType, ismodal);
	}
	
	public static void showControl(LuaMetatable metatable, int tag, int transitionType) {
		showControl(metatable, tag, transitionType, null);
	}

	static Timer mTime;
	public static void hide(int tag, int transitionType) {
		mEMPRender.hide(tag, transitionType);
		
		
		if(mIsShowLoading) {
			if(mTime!=null) {
				mTime.cancel();
				mTime = null;
			}
			mTime = new Timer();
			mTime.schedule(new TimerTask(){
				@Override
				public void run() {
					mTime.cancel();
					mTime = null;
					
					mIsShowLoading = false;
					setPhysicalkeyListener(mKeyCode, mCallLua);
					mKeyCode = null;
					mCallLua = null;
				}
			}, 2000);
		}
	}
	
	public static void hide(int tag) {
		hide(tag, 0);
	}
	
	
	public static void closeKeyboard() {
		mEMPRender.closeKeyboard();
	}
	
}
