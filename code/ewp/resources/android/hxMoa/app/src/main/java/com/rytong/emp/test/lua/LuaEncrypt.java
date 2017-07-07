package com.rytong.emp.test.lua;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.nq.enterprise.sdk.AppBridge;
import com.nq.enterprise.sdk.NqDeviceInfoService;
import com.nq.enterprise.sdk.NqDeviceInfoServiceListener;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.tool.HXUtils;
import com.rytong.emp.tool.User;
import com.rytong.emp.security.Base64;

/**
 * 以lua脚本的方式启动EmpEditor调试。
 * @author lihao85
 */
public class LuaEncrypt {
	/** empRender对象。 */
	EMPRender mEMPRender = null;
	Context context_;
	User user;
	String loginId = null;
	AppBridge appBridge;
	/**
	 * 构造方法。
	 * @param empRender empRender对象。
	 */
	public LuaEncrypt(EMPRender empRender) {
		mEMPRender = empRender;				
	}
	/**
	 * 构造方法。
	 * @param empRender empRender,Context context对象。
	 */
	public LuaEncrypt(EMPRender empRender,Context context) {
		mEMPRender = empRender;
		this.context_ = context;		
		loginID();
	}
	
	private void loginID() {
		user = new User();
		if(loginId == null){
			appBridge = new AppBridge(context_);
			appBridge.initService();
			appBridge.registerNqDeviceInfoServiceListener(new NqDeviceInfoServiceListener(){

				@Override
				public void onNQDeviceInfoServiceAvailable(NqDeviceInfoService service) {
					// TODO Auto-generated method stub
					if (service != null) {
						if (service.getErrorCode() == null) {
							String deviceInfo = service.getUdInfo(); // 获取device
							try {
								JSONObject object = new JSONObject(deviceInfo);
								user.setLoginId(object.getString("loginId"));
								loginId = user.getLoginId();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							String errorCode = service.getErrorCode(); // 返回错误码
						}
					}
				}			
			});
		}
	}
	/**
	 * DES加密+RSA加密
	 * @param value	加密明文数据
	 * @return	返回16位DES加密明文字符串+256位RSA加密DES密钥16进制字符串
	 */
	public String desRsa(String value) {
		String key = HXUtils.getRandomHexString(32);	//随机生成32位16进制字符串，并且每个字节的1是奇数个数
		String valueDES = des(key, value);
		key = "301C0410"+key+"04089999999999999999";
		String valueRSA = HXUtils.doRsa(HXUtils.hexStr2Bytes(key));
		
		return valueDES+valueRSA;
	}
	
	/**
	 * DES加密
	 * @param value	加密明文数据
	 * @return	返回DES加密后的16位16进制字符串密文
	 */
	public String des(String value) {
		String key = HXUtils.getRandomHexString(32);	//随机生成32位16进制字符串，并且每个字节的1是奇数个数
		String valueDES = HXUtils.doDes(key, value);
		
		return valueDES;
	}
	
	/**
	 * DES加密
	 * @param key DES加密密钥
	 * @param value	加密明文数据
	 * @return 返回DES加密后的16位16进制字符串密文
	 */
	public String des(String key, String value) {
		String valueDES = HXUtils.doDes(key, value);
		
		return valueDES;
	}
	
	/**
	 * RSA加密
	 * @param value	加密明文数据
	 * @return 返回RSA加密后的256位16进制字符串密文
	 */
	public String rsa(String value) {
		try {
			return HXUtils.doRsa(value.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * Base64转码
	 * @param value
	 * @return String 转码后的Base64
	 */
	public String base64(String value){
		byte[] sourceBytes = value.getBytes();
		String desInfo = Base64.encode(sourceBytes); // 进行Base64加密。		
		return desInfo;
	}
	/**
	 * 获取用户唯一标识
	 * @return
	 */
	public String getUserName() {
		return loginId;
	}	

}