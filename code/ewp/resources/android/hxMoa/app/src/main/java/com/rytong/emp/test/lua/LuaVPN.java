package com.rytong.emp.test.lua;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.rytong.emp.lua.java.CLEntity;
import com.rytong.emp.lua.java.CLuaFunction;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.render.EMPThreadPool;
import com.rytong.emp.test.MainActivity;
import com.sangfor.ssl.IVpnDelegate;
import com.sangfor.ssl.SFException;
import com.sangfor.ssl.SangforAuth;
import com.sangfor.ssl.common.VpnCommon;
import com.sangfor.ssl.service.setting.SystemConfiguration;

/**
 * 调用第三方原生接口
 * @author tanguozhi
 */
public class LuaVPN {
//	static MainActivity mActivity;
//	/** empRender对象。 */
//	static EMPRender mEMPRender = null;
//
//	static CLuaFunction mCallLua;
//	
//	private static final String VPN_IP = "183.63.131.102";	//内网VPN:10.1.202.20  外网VPN:183.63.131.102
//	private static final int VPN_PORT = 443;
//	private static final String USER = "moa";
//	private static final String PASSWD = "password123?";
//	private static final String HTTP_RES = "http://abc.hml.com";
//	private static final String CERT_PATH = "";//"/sdcard/hml_test.p12";
//	private static final String CERT_PASS = "";//"1";
//	
//	private static InetAddress m_iAddr = null;
//	private static NetWorkBroadcastReceiver mNetWorkReceiver = null;
//	
//	/**
//	 * 构造方法。
//	 * @param empRender empRender对象。
//	 */
//	public LuaVPN(MainActivity activity, EMPRender empRender) {
//		mActivity = activity;
//		mEMPRender = empRender;
//	}
//	
//	/**
//	 * 打开人脸识别
//	 * @param callLua 回调
//	 */
//	public void connect(final CLuaFunction callLua){
//		if(mCallLua!=null) {
//			return;
//		}
//		//防止多次调用
//		mCallLua = callLua;
//		
//		mActivity.runOnUiThread(new Runnable(){
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				SangforAuth sfAuth = SangforAuth.getInstance();
//				try {
//				
//				    sfAuth.init(mActivity, mActivity, SangforAuth.AUTH_MODULE_EASYAPP);
////					sfAuth.init(this, this, SangforAuth.AUTH_MODULE_L3VPN);
//					sfAuth.setLoginParam(mActivity.AUTH_CONNECT_TIME_OUT, String.valueOf(5));
//					
//					initSslVpn();
//				} catch (SFException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//	
//	/**
//	 * 开始初始化VPN，该初始化为异步接口，后续动作通过回调函数通知结果
//	 * 
//	 * @return 成功返回true，失败返回false，一般情况下返回true
//	 */
//	private static boolean initSslVpn() {
//		SangforAuth sfAuth = SangforAuth.getInstance();
//
//		m_iAddr = null;
//
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					m_iAddr = InetAddress.getByName(VPN_IP);
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		t.start();
//		try {
//			t.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		if (m_iAddr == null || m_iAddr.getHostAddress() == null) {
//			return false;
//		}
//		long host = VpnCommon.ipToLong(m_iAddr.getHostAddress());
//		int port = VPN_PORT;
//
//		if (sfAuth.vpnInit(host, port) == false) {
//			return false;
//		}
//
//		return true;
//	}
//	
//	public static void reloginCallback(int status, int result) {
//		switch (status){
//		
//		case IVpnDelegate.VPN_START_RELOGIN:
//			System.out.println("vpn===relogin callback start relogin start ...");
//			break;
//		case IVpnDelegate.VPN_END_RELOGIN:
//			System.out.println("vpn===relogin callback end relogin ...");
//			if (result == IVpnDelegate.VPN_RELOGIN_SUCCESS){
//				System.out.println("vpn===relogin callback, relogin success!");
//			} else {
//				System.out.println("vpn===relogin callback, relogin failed");
//			}
//			break;
//		}
//		
//	}
//
//	public static void vpnCallback(int vpnResult, int authType) {
//		SangforAuth sfAuth = SangforAuth.getInstance();
//
//		switch (vpnResult) {
//			case IVpnDelegate.RESULT_VPN_INIT_FAIL:
//				/**
//				 * 初始化vpn失败
//				 */
//				displayToast("RESULT_VPN_INIT_FAIL, error is " + sfAuth.vpnGeterr());
//				connectDone(IVpnDelegate.RESULT_VPN_INIT_FAIL);
//				break;
//
//			case IVpnDelegate.RESULT_VPN_INIT_SUCCESS:
//				/**
//				 * 初始化vpn成功，接下来就需要开始认证工作了
//				 */
////				displayToast("RESULT_VPN_INIT_SUCCESS, current vpn status is " + sfAuth.vpnQueryStatus());
//				// 初始化成功，进行认证操作
//				doVpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
//				
//				connectDone(IVpnDelegate.RESULT_VPN_INIT_SUCCESS);
//				break;
//
//			case IVpnDelegate.RESULT_VPN_AUTH_FAIL:
//				/**
//				 * 认证失败，有可能是传入参数有误，具体信息可通过sfAuth.vpnGeterr()获取
//				 */
//				displayToast("RESULT_VPN_AUTH_FAIL, error is " + sfAuth.vpnGeterr());
//				connectDone(IVpnDelegate.RESULT_VPN_AUTH_FAIL);
//				break;
//
//			case IVpnDelegate.RESULT_VPN_AUTH_SUCCESS:
//				/**
//				 * 认证成功，认证成功有两种情况，一种是认证通过，可以使用sslvpn功能了，另一种是前一个认证（如：用户名密码认证）通过，
//				 * 但需要继续认证（如：需要继续证书认证）
//				 */
//				if (authType == IVpnDelegate.AUTH_TYPE_NONE) {
////					displayToast("welcom to sangfor sslvpn!");
//
//                    // 若为L3vpn流程，认证成功后开启自动开启l3vpn服务
//                    if (SangforAuth.getInstance().getModuleUsed() == SangforAuth.AUTH_MODULE_EASYAPP) {
//                        // EasyApp流程，认证流程结束，可访问资源。
//                        doResourceRequest();
//                    }
//                    connectDone(IVpnDelegate.RESULT_VPN_AUTH_SUCCESS);
//				} else {
//					displayToast("auth success, and need next auth, next auth type is " + authType);
//
//					if (authType == IVpnDelegate.AUTH_TYPE_SMS) {
//						// 输入短信验证码
//						Toast.makeText(mActivity, "you need send sms code.", Toast.LENGTH_LONG).show();
//					} else {
//						doVpnLogin(authType);
//					}
//				}
//				break;
//			case IVpnDelegate.RESULT_VPN_AUTH_CANCEL:
//				displayToast("RESULT_VPN_AUTH_CANCEL");
//				connectDone(IVpnDelegate.RESULT_VPN_AUTH_CANCEL);
//				break;
//			case IVpnDelegate.RESULT_VPN_AUTH_LOGOUT:
//				/**
//				 * 主动注销（自己主动调用logout接口）或者被动注销（通过控制台把用户踢掉）均会调用该接口
//				 */
//				displayToast("RESULT_VPN_AUTH_LOGOUT");
//				connectDone(IVpnDelegate.RESULT_VPN_AUTH_LOGOUT);
//				break;
//			case IVpnDelegate.RESULT_VPN_L3VPN_FAIL:
//				/**
//				 * L3vpn启动失败，有可能是没有l3vpn资源，具体信息可通过sfAuth.vpnGeterr()获取
//				 */
//				displayToast("RESULT_VPN_L3VPN_FAIL, error is " + sfAuth.vpnGeterr());
//				connectDone(IVpnDelegate.RESULT_VPN_L3VPN_FAIL);
//				break;
//			case IVpnDelegate.RESULT_VPN_L3VPN_SUCCESS:
//				/**
//				 * L3vpn启动成功
//				 */
//				registerNetWorkBroadcasts(); //注册网络监听广播
////				displayToast("RESULT_VPN_L3VPN_SUCCESS");
//                // L3vpn流程，认证流程结束，可访问资源。
//                doResourceRequest();
//                connectDone(IVpnDelegate.RESULT_VPN_L3VPN_SUCCESS);
//				break;
//			case IVpnDelegate.VPN_STATUS_ONLINE:
//				/**
//				 * 与设备连接建立
//				 */
//				displayToast("online");
//				break;
//			case IVpnDelegate.VPN_STATUS_OFFLINE:
//				/**
//				 * 与设备连接断开
//				 */
//				displayToast("offline");
//				break;
//			default:
//				/**
//				 * 其它情况，不会发生，如果到该分支说明代码逻辑有误
//				 */
////				displayToast("default result, vpn result is " + vpnResult);
//				break;
//		}
//	}
//	
//	/**
//	 * 认证过程若需要图形校验码，则回调通告图形校验码位图，
//	 * 
//	 * @param data
//	 *            图形校验码位图
//	 */
//	public static void vpnRndCodeCallback(byte[] data) {
//		if (data != null) {
////			Drawable drawable = Drawable.createFromStream(new ByteArrayInputStream(data),
////					"rand_code");
////			imgbtn_rnd_code.setBackgroundDrawable(drawable);
//		}
//	}
//	
//	private static void displayToast(String str) {
//		Toast.makeText(mActivity, str, Toast.LENGTH_LONG).show();
//	}
//	
//	private static void doResourceRequest() {
//        // TODO Auto-generated method stub
//        // 认证结束，可访问资源。
//    }
//	
//	/**
//	 * 注册网络状态变化广播接收器
//	 */
//	private static void registerNetWorkBroadcasts() {
//		// 注册网络广播接收器
//		if (mNetWorkReceiver == null) {
//			// 创建IntentFilter对象
//			IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//			// 注册Broadcast Receiver
//			mNetWorkReceiver = new NetWorkBroadcastReceiver();
//			mActivity.registerReceiver(mNetWorkReceiver, networkFilter);
//		}
//	}
//	
//	/**
//	 * 取消注册网络状态变化广播接收器
//	 */
//	private static void unRegisterNetWorkBroadcasts() {
//		// 取消注册Broadcast Receiver
//		if (mNetWorkReceiver != null) {
//			mActivity.unregisterReceiver(mNetWorkReceiver);
//			mNetWorkReceiver = null;
//		}
//	}
//	
//	
//	/**
//	 * 处理认证，通过传入认证类型（需要的话可以改变该接口传入一个hashmap的参数用户传入认证参数）.
//	 * 也可以一次性把认证参数设入，这样就如果认证参数全满足的话就可以一次性认证通过，可见下面屏蔽代码
//	 * 
//	 * @param authType
//	 *            认证类型
//	 * @throws SFException
//	 */
//	private static void doVpnLogin(int authType) {
//		boolean ret = false;
//		SangforAuth sfAuth = SangforAuth.getInstance();
//
//		switch (authType) {
//			case IVpnDelegate.AUTH_TYPE_CERTIFICATE:
//				String certPasswd = PASSWD;
//				String certName = USER;
//				sfAuth.setLoginParam(IVpnDelegate.CERT_PASSWORD, certPasswd);
//				sfAuth.setLoginParam(IVpnDelegate.CERT_P12_FILE_NAME, certName);
//				ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_CERTIFICATE);
//				break;
//			case IVpnDelegate.AUTH_TYPE_PASSWORD:
//				String user = USER;
//				String passwd = PASSWD;
//				String rndcode = "";
//				sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_USERNAME, user);
//				sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_PASSWORD, passwd);
//				sfAuth.setLoginParam(IVpnDelegate.SET_RND_CODE_STR, rndcode);
//				ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
//				break;
//			case IVpnDelegate.AUTH_TYPE_SMS:
//				String smsCode = "";
//				sfAuth.setLoginParam(IVpnDelegate.SMS_AUTH_CODE, smsCode);
//				ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS);
//				break;
//			case IVpnDelegate.AUTH_TYPE_SMS1:
//				ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS1);
//				break;
//			default:
//				System.out.println("vpn===default authType " + authType);
//				break;
//		}
//
//		if (ret == true) {
////			System.out.println("vpn===success to call login method");
//		} else {
////			System.out.println("vpn===fail to call login method");
//		}
//
//	}
//	
//	public static void connectDone(final int code) {
//		mEMPRender.runTask(new EMPThreadPool.Task(0) {
//
//			@Override
//			public void doRun() throws Exception {
//				// TODO Auto-generated method stub
//				
//				CLEntity entity = new CLEntity();
//				entity.put("code", code);
//				MainActivity.mEmpLua.callback(mCallLua.mFunctionIndex, new Object[] { entity });
//			}
//			
//		});
//	}
//	
//	/** 接收网络状态广播消息 **/
//	private static class NetWorkBroadcastReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			ConnectivityManager connManager = (ConnectivityManager) context
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo mobNetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//			NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//			if ((mobNetInfo == null || !mobNetInfo.isConnected()) && (wifiInfo == null || !wifiInfo.isConnected())) {
//				// 网络断开
//				onEthStateChanged(false);   //再此函数里面做判断，如果网络断开做注销操作
//				System.out.println("vpn===Network is disconnected.");
//			} else if ((mobNetInfo != null && mobNetInfo.isConnected()) || (wifiInfo != null && wifiInfo.isConnected())) {
//				// 网络恢复
//				onEthStateChanged(true);  //判断正常的话，重新登陆
//				System.out.println("vpn===Network is connected.");
//			}
//		}
//	}
//	
//	/**
//	 * 当网络发生变化时通告函数，这个地方无需处理离线情况，因为离线情况下不会注册监听网络的广播接收器
//	 */
//	private static void onEthStateChanged(boolean connected) {
//		if (connected) {
//			initSslVpn();//登录
//		} else {
//			SangforAuth.getInstance().vpnLogout(); //注销
//		}
//	}
}