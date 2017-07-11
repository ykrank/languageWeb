package com.rytong.emp.test.config;

import java.io.Serializable;

public class ConfigInfo  implements Serializable{

	/** 单元测试 */
	public boolean mIsUnitTest = false; 
	/** 统计*/
	public boolean mIsTrack = false;
	/** 性能*/
	public boolean mIsPerfTest = false;
	/** 推送*/
	public boolean mIsPush = false;
	/** 国密 */
	public boolean mIsSM = false;
	/** 开发模式 */
	public boolean mIsDevMode = false;
	/** 错误提示*/
	public boolean mIsErrorAlert = true;
	/** 是否打印日志 */
	public boolean mIsLog = true;
	/** 缓存模式 */
	public boolean mIsCacheMode = true;
	/** 使用empeditor */
	public boolean mIsEmpEditor = false;
	
	/** 应用名称 */
	public String mAPPName = "ebank";
	/** 服务器地址 */
	public String mHost = "";
	/** 信道版本 */
	public float mChannel = 1.4f;
	/** 离线版本*/
	public float mOffline = 2.0f;
	/** 是否加载本地a.xml文件 */
	public boolean mIsAXml = false; 
	/** 是否点击了tutorial 按钮 */
	public boolean mIsTutorial = false;
	/** editor host */
	public String mEditorHost;
}
