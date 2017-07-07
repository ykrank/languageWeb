package com.rytong.emp.test.lua;


import android.app.Activity;
import android.text.TextUtils;

import com.rytong.emp.android.AndroidEMPBuilder;
import com.rytong.emp.data.AndroidPreferenceDB;
import com.rytong.emp.data.FileManager;
import com.rytong.emp.lua.EMPLua;
import com.rytong.emp.lua.EMPLuaFactory;
import com.rytong.emp.net.ClientHello;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.security.Encryptor;
import com.rytong.emp.tool.EMPConfig;

/**
 * <p>
 * 定义一些android特有的lua方法，以方便开发阶段的测试为目的
 * </p>
 * 
 * @author song_danqi
 * 
 */
public class LuaAndroidDevelop {

	// 该方法直接映射到EMPConfig
	// 使用方式--androiddevelop:setConfig(String methodName,String methodParameter)
	// public static void setConfig(String methodName,String methodParameter);

	/**
	 * <p>
	 * 该函数为android自定义函数，仅供开发阶段使用<br>
	 * 作用:在app的首页面调用，跳转页面
	 * 
	 * @return
	 */
	public static void goPage(int luaIndex, String url) {
		url = url.toLowerCase().trim();

		EMPConfig empConfig = EMPConfig.newInstance();
		if (!TextUtils.isEmpty(url) && url.indexOf(":") != -1) {
			// 更改ip
			if (!url.startsWith("http://")) {
				url = "http://" + url;
			}
			// 比较与上次是否登录同台服务器地址
			String lastServer = AndroidPreferenceDB.ANDROIDDB.get("LastServer");
			if (!TextUtils.isEmpty(lastServer) && !lastServer.equals(url)) {
				// 上次登录的服务地址与本次不同, 删除记录的信道证书。走完整信道流程。--测试使用
				FileManager.deleteFile(FileManager.FILEROOT.concat(ClientHello.CER_FILENAME));
				FileManager.deleteFile(FileManager.FILEROOT.concat(ClientHello.RNS2_FILENAME));
				FileManager.deleteFile(FileManager.FILEROOT.concat(url.substring(7)));
			}
			// 保存本次登录服务器地址
			AndroidPreferenceDB.ANDROIDDB.save("LastServer", url);

			empConfig.setServerUri(url);
			String urlNoPort = url.substring(0, url.lastIndexOf(":"));
			empConfig.setServerUriNoPort(urlNoPort);

			// 启动系统首页
			final EMPLua empLua = EMPLuaFactory.getEMPLua(luaIndex);
			final EMPRender empRender = empLua.getEMPRender();
			empRender.loadLocalFile("assets:main.xml");
		}
	}

	/**
	 * <p>
	 * 该函数为android自定义函数，仅供开发阶段使用<br>
	 * 作用:在app的首页面调用，可以获取上一次输入的url.
	 * 
	 * @return
	 */
	public static String getLastURL() {
		String lastServer = AndroidPreferenceDB.ANDROIDDB.get("LastServer");
		if (TextUtils.isEmpty(lastServer)) {
			lastServer = null;
		}
		return lastServer;
	}
	
	/**
	 * 该函数为android自定义函数，仅供开发阶段使用。 <br />
	 * 作用:在app的首页面调用，可以获取上一次输入的editor的地址。
	 * 
	 * @return 上一次输入的editor的地址。
	 */
	public static String getLastEditorURL() {
		String lastEditorServer = AndroidPreferenceDB.ANDROIDDB.get("lastEditorServer");
		if (TextUtils.isEmpty(lastEditorServer)) {
			lastEditorServer = null;
		}
		return lastEditorServer;
	}
	
	/**
	 * 该函数为android自定义函数，仅供开发阶段使用。 <br />
	 * 作用:在app的首页面调用，记忆editor输入框的内容。
	 * 
	 * @param luaIndex lua状态机。
	 * @param url editor的地址。
	 */
	public static void rememberEditorUrl(int luaIndex, String url){
		if (!TextUtils.isEmpty(url)) {
			AndroidPreferenceDB.ANDROIDDB.save("lastEditorServer", url);
		}
	}
	
	public static void resetCustomerSecretKey(int luaIndex){
		final EMPLua empLua = EMPLuaFactory.getEMPLua(luaIndex);
		final EMPRender empRender = empLua.getEMPRender();
		final Activity activity=((AndroidEMPBuilder)(empRender.getEMPBuilder())).mActivity;
		Encryptor.initCustomerSecretKey(activity);
	}

}
