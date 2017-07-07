package com.rytong.emp.tool;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import com.rytong.emp.android.AndroidEMPBuilder;
import com.rytong.emp.data.FileManager;
import com.rytong.emp.data.SqlDB;
import com.rytong.emp.net.ClientHello;
import com.rytong.emp.security.RSACipher;

public class HXUtils {
	private static boolean mIsTestDevelope; 
	/** 测试证书**/
	private static String CERTIFICATE_PATH_TEST = "xx_ca_test.png";
	/** 生产证书 **/
	private static String CERTIFICATE_PATH = "xx_ca.png";
	
	
	private static String attachmentDBKey = "attachmentDBKey";
	private static SqlDB mDBHelper;
	private static String  allAttachmentPathSplitFlag = "000011110000";
	public static String attachmentEnvironmentPath = "/attach";
	
	/** RSA 16进制字符串加密公钥 **/
	private static String publicKey_ = "B47EFC101FE435FA1B51BF632CC286C28BBCE177142F944FE8F8C4D2B4889F370D613653C3400DA3A98A942793D826CB1B4722A7B41C8A40F36F274AF0FC0B01FE9D7A29C74F924CC52316947352E014FCD52E9586088E9908007EEC99137F72C05CDFFF98D2F245C9F5B456B883E29DFD2DC2D4FEB62CAF030A9769A6B194D3";

	/** DES 加密类型 **/
	private static final String Algorithm = "DESede";
	private static final String AlgorithmIns = "DESede/ECB/NoPadding";
	
	/**
	 * RSA加密
	 * @param value	加密明文数据
	 * @return 返回RSA加密后的256位16进制字符串密文
	 */
	public static String doRsa(byte[] values) {
		return doRsa_private(values);
	}
	
	public static String doRsa_private(byte[] values) {
		try {
			BigInteger b1=new BigInteger(publicKey_, 16);
			RSAPublicKeySpec rsaPubKS=new RSAPublicKeySpec(b1,new BigInteger("10001",16));
			KeyFactory kf=KeyFactory.getInstance("RSA");
			RSAPublicKey pbk=(RSAPublicKey) kf.generatePublic(rsaPubKS);
			byte[] cipherTextByte = RSACipher.doEncrypt(values, pbk, RSACipher.TRANSFORMATION_RSA_ECB_PKCS1);
			String cipherTextHexStr = HXUtils.byte2HexStr(cipherTextByte);	//将密文转换成16进制字符串
			
			return cipherTextHexStr;
		} catch (Exception ev) {
			ev.printStackTrace();
		}
		return null;
	}
	
	/**
	 * DES加密
	 * @param key DES加密密钥
	 * @param value	加密明文数据
	 * @return 返回DES加密后的16位16进制字符串密文
	 */
	public static String doDes(String key, String value) {
		return doDes_private(key, value);
	}
	
	public static String doDes_private(String key, String value) {
		if (value==null) return "";
		
		if (value.length()<16) {
			for (int i=value.length(); i<16; i++) {
				value = value+"F";
			}
		}
		byte[] keys = HXUtils.hexStr2Bytes(key);
		
		//加密
		byte[] pTemp = HXUtils.hexStr2Bytes(value);
		byte[] enModel = HXUtils.desEncryptMode(keys, pTemp);
		String enModelHexStr = HXUtils.byte2HexStr(enModel);
		//解密
//		byte[] temp = HXUtils.hexStr2Bytes(enModelHexStr);
//		byte[] deModel = HXUtils.desDecryptMode(keys, temp);
//		String deModelStr = HXUtils.byte2HexStr(deModel);
		
		return enModelHexStr;
	}
	
	/**
	 * 16进制字符串转byte[]
	 * @param value	要转换的值
	 * @return	返回转换后的byte[]数据
	 */
	public static byte[] hexStr2Bytes(String value) {  
	    return hexStr2Bytes_private(value);  
	}  
	
	public static byte[] hexStr2Bytes_private(String value) {  
	    int m = 0, n = 0;  
	    int l = value.length() / 2;  
	    byte[] ret = new byte[l];  
	    for (int i = 0; i < l; i++) {  
	        m = i * 2 + 1;  
	        n = m + 1;  
	        ret[i] = uniteBytes(value.substring(i * 2, m), value.substring(m, n));  
	    }  
	    return ret;  
	} 
	
	/**
	 * byte[]数据转换成16进制字符串
	 * @param value	要转换的值
	 * @return	返回转换后的16进制字符串数据
	 */
	public static String byte2HexStr(byte[] b) {  
	    return byte2HexStr_private(b);
	}
	
	public static String byte2HexStr_private(byte[] b) {  
	    String hs = "";  
	    String stmp = "";  
	    for (int n = 0; n < b.length; n++) {  
	        stmp = (Integer.toHexString(b[n] & 0XFF));  
	        if (stmp.length() == 1)  
	            hs = hs + "0" + stmp;  
	        else  
	            hs = hs + stmp;  
	    }
	    return hs.toUpperCase();
	}
	
	/**
	 * DES解密
	 * @param keybyte DES解密密钥
	 * @param value	DES密文
	 * @return	返回DES解密后的明文
	 */
    private static byte[] desDecryptMode(byte[] keybyte, byte[] value) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(AlgorithmIns);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(value);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    /**
	 * DES加密
	 * @param keybyte DES加密密钥
	 * @param value	DES明文
	 * @return	返回DES解密后的密文
	 */
	public static byte[] desEncryptMode(byte[] keybyte, byte[] txt) {
		return desEncryptMode_private(keybyte, txt);
	}
	
	public static byte[] desEncryptMode_private(byte[] keybyte, byte[] txt) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(AlgorithmIns);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(txt);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}
	
	private static byte uniteBytes(String src0, String src1) {  
	    byte b0 = Byte.decode("0x" + src0).byteValue();  
	    b0 = (byte) (b0 << 4);  
	    byte b1 = Byte.decode("0x" + src1).byteValue();  
	    byte ret = (byte) (b0 | b1);  
	    return ret;  
	}

	private static byte getRandomByte(){
	    int temp=0;
	    byte num = 0;
	    Random random = new Random();
	    while(temp%2==0) {
	        temp=0;
	        int ran = random.nextInt(255);
	        if(ran<0) {
	        	ran = -ran;
	        }
	        Integer iO = new Integer(ran);
	        num = iO.byteValue();;
	        for(int i=0;i<8;i++){
	            if ((num>>i & 1)==1) {
	                temp++;
	            }
	        }
	    }
	    return num;
	}
	
	public static void checkOnForceground(Context context) {
		if (isAppOnForeground(context)) {
			Toast.makeText(context, "华兴移动统一门户已切换到后台，请确认是否为本人操作。", Toast.LENGTH_LONG).show();
		}
	}
	
	public static void initDefault(Context context, boolean isTestDevelope) {
		initDefault_private(context, isTestDevelope);
	}
	
	private static void initDefault_private(Context context, boolean isTestDevelope) {
		mIsTestDevelope = isTestDevelope;
		if(isTestDevelope) {
			ClientHello.CERTIFICATE_PATH = CERTIFICATE_PATH_TEST;
		} else {
			ClientHello.CERTIFICATE_PATH = CERTIFICATE_PATH;
		}
		HXUtils.checkRoot(context);
	}
	
	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	private static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}
	
	public static void checkRoot(Context context) {
		checkRoot_private(context);
	}
	
	private static void checkRoot_private(Context context) {
		if(checkSU())
			Toast.makeText(context, "提醒：该手机已经被越狱了，请注意安全！", Toast.LENGTH_LONG).show();
	}
	
	private static boolean checkSU() {
		try {  
            Process process = Runtime.getRuntime().exec("su");// (这里执行是系统已经开放了root权限，而不是说通过执行这句来获得root权限)  
            DataOutputStream os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes("exit\n");  
            os.flush();  
            return true;
        } catch (IOException e) {  
            e.printStackTrace();  
            return false;
        }  
	}
	
	/**
	 * 随机生成指定长度的16进制字符串，并且每个字节的1是奇数个数
	 * @param length 指定生成的长度
	 * @return 返回随机生成的16进制字符串数据
	 */
	public static String getRandomHexString(int length) {
		return getRandomHexString_private(length);
	}
	
	private static String getRandomHexString_private(int length) {
		byte[] randomBytes = new byte[16];
		randomBytes[0] = getRandomByte();
		randomBytes[1] = getRandomByte();
		randomBytes[2] = getRandomByte();
		randomBytes[3] = getRandomByte();
		randomBytes[4] = getRandomByte();
		randomBytes[5] = getRandomByte();
		randomBytes[6] = getRandomByte();
		randomBytes[7] = getRandomByte();
		randomBytes[8] = getRandomByte();
		randomBytes[9] = getRandomByte();
		randomBytes[10] = getRandomByte();
		randomBytes[11] = getRandomByte();
		randomBytes[12] = getRandomByte();
		randomBytes[13] = getRandomByte();
		randomBytes[14] = getRandomByte();
		randomBytes[15] = getRandomByte();
		
		String randomStr = HXUtils.byte2HexStr(randomBytes);
		return randomStr;
	}
	
	public static boolean isEmpty(String value) {
		if (value!=null && !"".equals(value)) {
			return false;
		}
		return true;
	}

	public static void remollAllAttachment() {
		if (mDBHelper == null) {
			final Context context = AndroidEMPBuilder.mContext;
	    	mDBHelper = new SqlDB(context, "database.sql");
		}
		
		String value = mDBHelper.getData(attachmentDBKey);
		if (!isEmpty(value)) {
			
			String[] values = value.split(allAttachmentPathSplitFlag);
			for (int i=0; i<values.length; i++) {
				String v = values[i];
				if(!v.equals("")){
					FileManager.deleteFile(v);
				}
			}
			mDBHelper.deleteData(attachmentDBKey);
		}
		
		FileManager.deleteFile(Environment.getExternalStorageDirectory()+attachmentEnvironmentPath);
	}
	
	public static String getAttachment() {
		if (mDBHelper == null) {
			final Context context = AndroidEMPBuilder.mContext;
	    	mDBHelper = new SqlDB(context, "database.sql");
		}
		return mDBHelper.getData(attachmentDBKey);
	}
	
	public static int saveOrUpdateAttachment(String value) {
		if (mDBHelper == null) {
			final Context context = AndroidEMPBuilder.mContext;
	    	mDBHelper = new SqlDB(context, "database.sql");
		}
    	
    	boolean isSuccess = false;
		if (mDBHelper != null && attachmentDBKey != null) {
			String _value = mDBHelper.getData(attachmentDBKey);
			
			if (_value == null) {
				isSuccess = mDBHelper.insertData(attachmentDBKey, value, true);
			} else if(!_value.contains(value)) {
				_value += allAttachmentPathSplitFlag+value;
				isSuccess = mDBHelper.updateData(attachmentDBKey, _value, true);
			}
		}
		return isSuccess ? 0 : 1;
    }
}
