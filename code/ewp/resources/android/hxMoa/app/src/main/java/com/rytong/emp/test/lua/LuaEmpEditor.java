package com.rytong.emp.test.lua;

import com.rytong.emp.render.EMPRender;
import com.rytong.emp.tool.EMPConfig;
import com.rytong.emp.tool.Utils;
import com.rytong.empeditor.EditorDebugManager;

/**
 * 以lua脚本的方式启动EmpEditor调试。
 * @author lihao85
 */
public class LuaEmpEditor {
	
	/** EmpEditor服务器地址设置有问题。 */
	private final String ADDRESS_ERROR = "EmpEditor服务器地址设置有问题。";
	
	/** empRender对象。 */
	EMPRender mEMPRender = null;

	/**
	 * 构造方法。
	 * @param empRender empRender对象。
	 */
	public LuaEmpEditor(EMPRender empRender) {
		mEMPRender = empRender;
	}
	
	/**
	 * 初始化EmpEditor服务器。
	 * @param addressStr EmpEditor服务器地址。
	 * @return 出错信息。
	 */
	public String initEmpEditor(String addressStr){
		String error = ADDRESS_ERROR;
		if(!Utils.isEmpty(addressStr) && addressStr.indexOf(":") != -1){	 // 解析地址。
			addressStr = addressStr.startsWith("http://") ? addressStr.replaceAll("http://", "") : addressStr; // 去掉http前缀。
			try{
				int lastSeparator = addressStr.lastIndexOf(':');
				String host = addressStr.substring(0, lastSeparator); // 获得主机地址。
				int port = Integer.parseInt(addressStr.substring(lastSeparator + 1)); // 获得端口号。
				EMPConfig.newInstance().setEmpEditor(true); // 标记EmpEditor调试开始。
				EditorDebugManager edm = EditorDebugManager.getInstance(host, port); // 获得一个EmpEditor调试对象。
				edm.mUseNewProtocol = true; // 指定使用新的数据传输协议。
				EMPConfig.newInstance().setEditorHandler(edm); // 登记EditorHandler实例。
				error = edm.startEditorDebug(); // 开始EmpEditor调试。
			} catch (Exception e){
				error = "EmpEditor服务启动故障：\n".concat(e.getMessage());
			}
		}
		return error;
	}
}