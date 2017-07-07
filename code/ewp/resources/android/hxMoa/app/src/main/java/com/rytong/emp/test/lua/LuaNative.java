package com.rytong.emp.test.lua;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rytong.emp.face.ac.FaceActivity;
import com.rytong.emp.lua.EMPLua;
import com.rytong.emp.lua.EMPLuaFactory;
import com.rytong.emp.lua.java.CLEntity;
import com.rytong.emp.lua.java.CLuaFunction;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.render.EMPThreadPool;
import com.rytong.emp.test.MainActivity;

/**
 * 调用第三方原生接口
 * @author tanguozhi
 */
public class LuaNative {
	Activity mActivity;
	/** empRender对象。 */
	static EMPRender mEMPRender = null;
	
	public static int REQUEST_CODE_FaceCode = 1002;

	static CLuaFunction mCallLua;
	static boolean isOpenFace;
	
	/**
	 * 构造方法。
	 * @param empRender empRender对象。
	 */
	public LuaNative(Activity activity, EMPRender empRender) {
		mActivity = activity;
		mEMPRender = empRender;
	}
	
	/**
	 * 打开人脸识别
	 * @param callLua 回调
	 */
	public void openFace(final CLuaFunction callLua){
		//防止多次调用
		if(isOpenFace)
			return;
		isOpenFace = true;
		
		mCallLua = callLua;
		
		mActivity.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.setClass(mActivity, FaceActivity.class);
		        mActivity.startActivityForResult(intent, REQUEST_CODE_FaceCode);
			}
		});
	}
	
	public static void onActivityResult(int requestCode, int resultCode, final Intent intent) {
		if (REQUEST_CODE_FaceCode == requestCode) {	//人脸识别回调
			isOpenFace = false;
			if (resultCode == Activity.RESULT_OK) {
				mEMPRender.runTask(new EMPThreadPool.Task(0) {

					@Override
					public void doRun() throws Exception {
						// TODO Auto-generated method stub
						Bundle bundle = intent.getExtras();
						String snapData = bundle.getString("SNAP_DATA");	//快照
						String value = bundle.getString("VALUE");			//代码
						String data = bundle.getString("DATA");				//提示信息
						
						CLEntity entity = new CLEntity();
						if(snapData!=null) {
							entity.put("baowen", snapData);
						}
						entity.put("value", value);
						entity.put("data", data);
						MainActivity.mEmpLua.callbackAndDispose(mCallLua.mFunctionIndex, new Object[] { entity });
					}
					
				});
			}
		}
	}
}