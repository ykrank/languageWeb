package com.rytong.emp.test;


import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;
import org.androidpn.client.ServiceManager.ConnectServiceCallback;
import org.androidpn.client.ServiceManager.RegisterCallback;
import org.androidpn.client.base.model.BaseMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.android.debug.hv.ViewServer;
import com.rytong.emp.android.EMPActivity;
import com.rytong.emp.chart.atom.DivPlot;
import com.rytong.emp.chart.atom.DivTips;
import com.rytong.emp.chart.atom.InputCake;
import com.rytong.emp.chart.atom.Li;
import com.rytong.emp.chart.atom.ULBallPlot;
import com.rytong.emp.chart.atom.ULBarPlot;
import com.rytong.emp.chart.atom.ULColumnPlot;
import com.rytong.emp.chart.atom.ULCubePlot;
import com.rytong.emp.chart.atom.ULLinePlot;
import com.rytong.emp.chart.atom.ULXscale;
import com.rytong.emp.chart.atom.ULYscale;
import com.rytong.emp.data.AndroidPreferenceDB;
import com.rytong.emp.dom.Entity;
import com.rytong.emp.gui.GUIRepository;
import com.rytong.emp.gui.atom.HXImage;
import com.rytong.emp.gui.atom.HXInputPassword;
import com.rytong.emp.gui.atom.HXLoading;
import com.rytong.emp.gui.atom.HXSeekBar;
import com.rytong.emp.lua.EMPLua;
import com.rytong.emp.qrcode.lua.LuaQRCode;
import com.rytong.emp.qrcode.lua.QrcodeLua;
import com.rytong.emp.reader.lua.ReaderLua;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.test.config.ConfigInfo;
import com.rytong.emp.test.config.ControlPaddingConfig;
import com.rytong.emp.test.config.SDKConfig;
import com.rytong.emp.test.lua.APPLua;
import com.rytong.emp.test.lua.LuaAttachment;
import com.rytong.emp.test.lua.LuaCopyAssets;
import com.rytong.emp.test.lua.LuaEmpEditor;
import com.rytong.emp.test.lua.LuaEncrypt;
import com.rytong.emp.test.lua.LuaNative;
import com.rytong.emp.test.lua.LuaPush;
import com.rytong.emp.test.lua.LuaUtil;
import com.rytong.emp.test.lua.LuaWindow;
import com.rytong.emp.test.multitask.IFrame;
import com.rytong.emp.test.multitask.LuaMultiTask;
import com.rytong.emp.time.TimeInputText;
import com.rytong.emp.tool.ControlConfig;
import com.rytong.emp.tool.EMPConfig;
import com.rytong.emp.tool.HXUtils;
import com.rytong.emp.tool.Utils;
import com.rytong.emp.unit.MoasUnit;
import com.rytong.empeditor.EditorDebugManager;
import com.rytong.push.receiver.MessageHandler;
import com.rytong.push.receiver.ViewListener;
import com.rytong.track.TrackAgent;
import com.sangfor.ssl.IVpnDelegate;

public class MainActivity extends EMPActivity implements RegisterCallback, ViewListener {
	public static EMPLua mEmpLua;
	public static EMPRender mEmpRender;
	public static Activity mActivity;
	
	private ConfigInfo mConfigInfo;
	
	private ServiceManager serviceManager;//
	private MessageHandler messageHandler;
	public static String registID;
	/** true为开发模式 false为生产模式 **/
	public static boolean mIsTestDevelope = false; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		
		ViewServer.get(this).addWindow(this);
		serviceManager = ServiceManager.getInstance(this);

		messageHandler = new MessageHandler(this, serviceManager);
		if (!serviceManager.isAppRegister()) {
			serviceManager.appRegister(this);
		} else {
			LuaPush.registId = serviceManager.getRegisterID();
		}
		/**
		 * 如果是通过点击通知启动的应用，需从intent中去获取messageId。如果messageId不为空，对message做处理
		 */
		String messageId = getIntent().getStringExtra(Constants.NOTIFICATION_ID);
		if (!TextUtils.isEmpty(messageId)) {
			messageHandler.showMessage(messageId);
		}
		/**
		 * 不管应用有亾注册成功都开启服务
		 */
		connectService();
		
		/**
		 * 开启日志，项目上投产需要关闭
		 */
		serviceManager.isPrintLg(false);
	}
	
	public void onResume() {
		super.onResume();
		ViewServer.get(this).setFocusedWindow(this);
	}
	
	@Override
	public void onInitActivity() {
		//初始化默认配置
		HXUtils.initDefault(this, mIsTestDevelope);
		
		// 隐藏系统标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public void onAdjustControlConfig() {
		super.onAdjustControlConfig();
		ControlConfig.InputButton.DEF_DELAY = 2000;
	}
	
	@Override
	public void onAdjustEMPConfig(EMPConfig empConfig) {
		super.onAdjustEMPConfig(empConfig);
		//生产环境
//		empConfig.setServerUri("http://dev.rytong.me:9122");
//		empConfig.setServerUriNoPort("http://dev.rytong.me");
		//测试环境内网
//		empConfig.setServerUri("http://10.1.87.71:4002");
//		empConfig.setServerUriNoPort("http://10.1.87.71");
		//uat
		empConfig.setServerUri("http://10.1.87.99:4002");
		empConfig.setServerUriNoPort("http://10.1.87.99");
		//测试环境外网
//		empConfig.setServerUri("http://183.63.131.106:4002");
//		empConfig.setServerUriNoPort("http://183.63.131.106");
		//廖立新
//		empConfig.setServerUri("http://10.1.91.212:4002");
//		empConfig.setServerUriNoPort("http://10.1.91.212");
		
//		empConfig.setServerUri("http://10.1.87.100:4002");
//		empConfig.setServerUriNoPort("http://10.1.87.100");
		
		empConfig.setAppName("moas");
		empConfig.setOfflineVersion(3);				//离线资源版本
		empConfig.setTlsVersion(1.4f);				//加密信道版本
		empConfig.setPrintLog(false);				//是否打印输出
		empConfig.setDevelopmentMode(true);			//是否打开开发模式
		empConfig.setCacheForDevelopment(false);	//是否打开开发缓存模式
		empConfig.setNoHttpsCertificate(true);		//是否默认所有HTTPS请求为安全
		empConfig.setEmpMap(false);					//是否调用本地地图
		
		//当mIsTestDevelope为true时会应用这里的生产配置
		initProductModeConfig(empConfig);
				
		initDevModeConfig(empConfig);
	}
	
	private void initProductModeConfig(EMPConfig empConfig) {
		if (!mIsTestDevelope) {
			//生产环境
			empConfig.setServerUri("http://183.63.131.123:4002");
			empConfig.setServerUriNoPort("http://183.63.131.123");
//			empConfig.setServerUri("http://10.1.90.6:4002");
//			empConfig.setServerUriNoPort("http://10.1.90.6");
			empConfig.setPrintLog(false);				//是否打印输出
			empConfig.setDevelopmentMode(false);		//是否打开开发模式
			empConfig.setCacheForDevelopment(false);	//是否打开开发缓存模式
		}
	}
	
	/**
	 * 开发模式下初始化系统配置
	 */
	private void initDevModeConfig(EMPConfig empConfig) {
		Intent intent = getIntent();
		mConfigInfo = (ConfigInfo) intent.getSerializableExtra("ConfigInfo");

		if (mConfigInfo != null) {
			empConfig.setUnitTest(mConfigInfo.mIsUnitTest);
			empConfig.setAppName(mConfigInfo.mAPPName);
			empConfig.setTlsVersion(mConfigInfo.mChannel);
			empConfig.setOfflineVersion(mConfigInfo.mOffline);
			empConfig.setTrack(mConfigInfo.mIsTrack);
			empConfig.setPerfTestOn(mConfigInfo.mIsPerfTest);
			empConfig.setUsePush(mConfigInfo.mIsPush);
			empConfig.setUseSM(mConfigInfo.mIsSM);
			empConfig.setDevelopmentMode(mConfigInfo.mIsDevMode);
			empConfig.setAlertPrompt(mConfigInfo.mIsErrorAlert);
			empConfig.setPrintLog(mConfigInfo.mIsLog);
			empConfig.setCacheForDevelopment(mConfigInfo.mIsCacheMode);
			empConfig.setEmpEditor(mConfigInfo.mIsEmpEditor);

			if (!mConfigInfo.mIsAXml && !mConfigInfo.mIsTutorial) {
				String host = mConfigInfo.mHost;
				empConfig.setServerUri(host);
				String urlNoPort = host.substring(0, host.lastIndexOf(":"));
				empConfig.setServerUriNoPort(urlNoPort);
			}
		}
		
		//sdk属性
		SDKConfig sdkConfig = null;
		sdkConfig = (SDKConfig)intent.getSerializableExtra("SDKConfig");
		if(null != sdkConfig){
			ControlConfig.Label.DEF_BREAK_MODE  = sdkConfig.mbreakMode;
			ControlConfig.Label.DEF_SHADOW_COLOR = sdkConfig.mShadowColor;
			ControlConfig.Label.DEF_SHADOW_OFFSET_X = sdkConfig.mShadowOffsetX;
			ControlConfig.Label.DEF_SHADOW_OFFSET_Y = sdkConfig.mShadowOffsetY;
			
			ControlConfig.InputPassword.DEF_IS_ORDER = sdkConfig.mIsOrder;
			ControlConfig.InputPassword.DEF_SUBSTITUTE = sdkConfig.mSubstitute;
			
			ControlConfig.InputButton.DEF_DELAY = sdkConfig.mDelayTime;
			ControlConfig.InputText.DEF_CLEAR_MODE = sdkConfig.mClearMode;
			
			ControlConfig.Img.DEF_LOADING_IMG = sdkConfig.mLoadingImg;
			ControlConfig.Img.DEF_FAILED_IMG = sdkConfig.mLoadFailed;

			ControlConfig.A.DEF_BREAK_MODE = sdkConfig.mLinkBreakMode;
			
			EMPConfig.mImgOptimalBasicWidth = sdkConfig.mOptimalWidth;
		}
		
		//控件边距全局属性配置
		ControlPaddingConfig paddingConfig = null;
		paddingConfig = (ControlPaddingConfig)intent.getSerializableExtra("ControlPaddingConfig");
		if(null != paddingConfig){
			//div
			ControlConfig.Div.DEF_PADDING_LEFT = paddingConfig.DIV_DEF_PADDING_LEFT;
			ControlConfig.Div.DEF_PADDING_TOP = paddingConfig.DIV_DEF_PADDING_TOP;
			ControlConfig.Div.DEF_PADDING_RIGHT = paddingConfig.DIV_DEF_PADDING_RIGHT;
			ControlConfig.Div.DEF_PADDING_BOTTOM = paddingConfig.DIV_DEF_PADDING_BOTTOM;
			ControlConfig.Div.DEF_LINESPACING = paddingConfig.DIV_DEF_LINESPACING;
			ControlConfig.Div.DEF_ROWSPACING = paddingConfig.DIV_DEF_ROWSPACING;
			//body
			ControlConfig.Body.DEF_PADDING_LEFT = paddingConfig.BODY_DEF_PADDING_LEFT;
			ControlConfig.Body.DEF_PADDING_TOP = paddingConfig.BODY_DEF_PADDING_TOP;
			ControlConfig.Body.DEF_PADDING_RIGHT = paddingConfig.BODY_DEF_PADDING_RIGHT;
			ControlConfig.Body.DEF_PADDING_BOTTOM = paddingConfig.BODY_DEF_PADDING_BOTTOM;
			ControlConfig.Body.DEF_LINESPACING = paddingConfig.BODY_DEF_LINESPACING;
			ControlConfig.Body.DEF_ROWSPACING = paddingConfig.BODY_DEF_ROWSPACING;
			//form
			ControlConfig.Form.DEF_PADDING_LEFT = paddingConfig.FORM_DEF_PADDING_LEFT;
			ControlConfig.Form.DEF_PADDING_TOP = paddingConfig.FORM_DEF_PADDING_TOP;
			ControlConfig.Form.DEF_PADDING_RIGHT = paddingConfig.FORM_DEF_PADDING_RIGHT;
			ControlConfig.Form.DEF_PADDING_BOTTOM = paddingConfig.FORM_DEF_PADDING_BOTTOM;
			ControlConfig.Form.DEF_LINESPACING = paddingConfig.FORM_DEF_LINESPACING;
			ControlConfig.Form.DEF_ROWSPACING = paddingConfig.FORM_DEF_ROWSPACING;
			//TD
			ControlConfig.Td.DEF_PADDING_LEFT = paddingConfig.TD_DEF_PADDING_LEFT;
			ControlConfig.Td.DEF_PADDING_TOP = paddingConfig.TD_DEF_PADDING_TOP;
			ControlConfig.Td.DEF_PADDING_RIGHT = paddingConfig.TD_DEF_PADDING_RIGHT;
			ControlConfig.Td.DEF_PADDING_BOTTOM = paddingConfig.TD_DEF_PADDING_BOTTOM;
			ControlConfig.Td.DEF_LINESPACING = paddingConfig.TD_DEF_LINESPACING;
			ControlConfig.Td.DEF_ROWSPACING = paddingConfig.TD_DEF_ROWSPACING;
		}
	}
	
	private String initEmpEditor(EMPRender empRender) {
		AndroidPreferenceDB.ANDROIDDB.putString("lastEditorServer", mConfigInfo.mEditorHost);
		LuaEmpEditor editor = new LuaEmpEditor(empRender);
		String error = editor.initEmpEditor(mConfigInfo.mEditorHost);
		if (!Utils.isEmpty(error)) {
			empRender.alert(null, error);
		}
		return error;
	}

	private void toTutorial(EMPRender empRender) {
		String error = initEmpEditor(empRender);
		if (Utils.isEmpty(error)) {
			String content = "<?xml version='1.0' encoding='UTF-8' ?><content>"
					+ "<head><style>.body1 {background-color:#000000;}.label1 {left:10px;top:10px;color:#ffffff;}</style></head><body class='body1'>"
					+ "<label class='label1'>Tutorial开始！</label></body></content>";
			empRender.load(content);
		}
	}
	
	@Override
	public void onLoadStartPage(EMPRender empRender) {
		// 加载assets下页面
		
		if (mConfigInfo != null) {
			if (mConfigInfo.mIsAXml) { // 加载本地a.xml
				empRender.loadLocalFile("assets:test1.xml");
			} else if (mConfigInfo.mIsTutorial) {// 点击了tutorial按钮
				toTutorial(empRender);
			} else { // 启动系统首页
				if (mConfigInfo.mIsEmpEditor) {
					initEmpEditor(empRender);
				}
				empRender.loadLocalFile("assets:main.xml");
			}
		} else {
			empRender.loadLocalFile("assets:main.xml");
		}
	}

	@Override
	public void onAddLua(EMPLua empLua) {
		mEmpLua = empLua;
		// 加载.so文件
		try {
			System.loadLibrary(APPLua.APPLUA_LIB);
			System.loadLibrary(ReaderLua.READER_LIB);
			System.loadLibrary(QrcodeLua.READER_LIB);
		} catch (UnsatisfiedLinkError e) {
			Utils.printException(e);
		}
		// 注册lua 接口\方法
		APPLua.addLua(empLua.getCLua());
		ReaderLua.addLua(empLua.getCLua());
		QrcodeLua.addLua(empLua.getCLua());
		EMPRender empRender = empLua.getEMPRender();
		empLua.addJavaInterface("luaempeditor", new LuaEmpEditor(empRender));
		// 注册多任务lua方法
		empLua.addJavaInterface("luamultitask", new LuaMultiTask(this, empRender));
		//
		empLua.addJavaInterface("native", new LuaNative(this, empRender));
		//加密接口
		empLua.addJavaInterface("encrypt", new LuaEncrypt(empRender,this));
		//附件接口
		empLua.addJavaInterface("attachment", new LuaAttachment(this, empRender));
		//附件接口
		empLua.addJavaInterface("window", new LuaWindow(empRender));
		empLua.addJavaInterface("util", new LuaUtil(empRender,this));
		//vpn接口
//		empLua.addJavaInterface("vpn", new LuaVPN(this, empRender));
		//文件拷贝接口
		empLua.addJavaInterface("copyAssets", new LuaCopyAssets(this, empRender));
		//推送消息ID
		empLua.addJavaInterface("push", new LuaPush(this, empRender));
	}
	
	@Override
	public void onAddGUIView() {
		// 图表
		GUIRepository.addGUIView(Entity.NODE_INPUT, "cake", InputCake.class);
		GUIRepository.addGUIView(Entity.NODE_DIV, "plot", DivPlot.class);
		GUIRepository.addGUIView(Entity.NODE_UL, "barPlot", ULBarPlot.class);
		GUIRepository.addGUIView(Entity.NODE_UL, "cubePlot", ULCubePlot.class);
		GUIRepository.addGUIView(Entity.NODE_UL, "columnPlot", ULColumnPlot.class);
		GUIRepository.addGUIView(Entity.NODE_UL, "linePlot", ULLinePlot.class);
		GUIRepository.addGUIView(Entity.NODE_UL, "ballPlot", ULBallPlot.class);
		GUIRepository.addGUIView(Entity.NODE_DIV, "tips", DivTips.class);
		GUIRepository.addGUIView(Entity.NODE_UL, "xscale", ULXscale.class);
		GUIRepository.addGUIView(Entity.NODE_UL, "yscale", ULYscale.class);
		GUIRepository.addGUIView(Entity.NODE_LI, Li.class);
		// 多任务
		GUIRepository.addGUIView(IFrame.IFRAME, IFrame.class);
		
		//密码控件
		GUIRepository.addGUIView(Entity.NODE_INPUT, "password", HXInputPassword.class);
		GUIRepository.addGUIView(Entity.NODE_INPUT, "password-num", HXInputPassword.class);
		GUIRepository.addGUIView(Entity.NODE_INPUT, "password-ASCII", HXInputPassword.class);
		//滑动控件
		GUIRepository.addGUIView("slider", HXSeekBar.class);
		GUIRepository.addGUIView(Entity.NODE_INPUT, "unit",MoasUnit.class);
		//时间控件
		GUIRepository.addGUIView(Entity.NODE_INPUT, "date",TimeInputText.class);
		//Loading控件
		GUIRepository.addGUIView(Entity.NODE_DIV, "load", HXLoading.class);
		
		
		GUIRepository.addGUIView("image",HXImage.class);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (EMPConfig.newInstance().isTrack()) {
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (EMPConfig.newInstance().isTrack()) {
			TrackAgent.onPauseSession();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		HXUtils.checkOnForceground(this);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (EMPConfig.newInstance().isTrack()) {
			TrackAgent.onResumeSession();
		}
	}
	
	@Override
	public void onDestroy() {
		EditorDebugManager.getInstance().stopEditorDebug();
		// 结束统计。
		if (EMPConfig.newInstance().isTrack()) {
			TrackAgent.onEndSession();
		}
		super.onDestroy();
	}
	
	/**
	 * 系统相机返回处理
	 * 系统二维码扫描处理
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 系统二维码扫描接口
		LuaQRCode.onActivityResult(requestCode, resultCode, data);
		LuaNative.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!LuaWindow.mIsShowLoading)
			return super.onKeyDown(keyCode, event);
		
		return false;
	}

	private void connectService() {
		serviceManager.connectService(new ConnectServiceCallback() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MainActivity.this, "服务链接成功", Toast.LENGTH_SHORT).show();
					};
				});

			}

			@Override
			public void onError(final String errorMsg) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
						;
					};
				});
			}
		});
	}
	@Override
	public void onError(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(final String regId) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				LuaPush.registId = regId;				
			}
		});
	}

	@Override
	public void updateMessageSuccess(BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMessageError(String errorMsg) {
		
	}
	
}
