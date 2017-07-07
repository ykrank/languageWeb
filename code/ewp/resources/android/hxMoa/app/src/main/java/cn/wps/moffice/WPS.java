/**
 *	 文件名：ListFile.java
 * 	创建者:fanguangcheng
 * 	创建时间:2013.7.18
 * 	作用：文件列表显示，并响应打开wps文件等一系列动作
 */
package cn.wps.moffice;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class WPS {
	private static Activity mActivity;
	
	private static WPS mWPS;
	
	public static WPS getInstances(Activity activity) {
		mActivity = activity;
		
		if(mWPS==null) {
			mWPS = new WPS();
		}
		return mWPS;
	}
	
	public boolean isInstallWPS() {
		if (checkPackage(Define.PACKAGENAME_ENT)){
			return true;
		}else if (checkPackage(Define.PACKAGENAME)){
			return true;
		}else if (checkPackage(Define.PACKAGENAME_ENG)){
			return true;
		}else if (checkPackage(Define.PACKAGENAME_KING_ENT)){
			return true;
		}else if (checkPackage(Define.PACKAGENAME_KING_PRO)){
			return true;
		}else if (checkPackage(Define.PACKAGENAME_KING_PRO_HW)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 如果是wps文件，则用wps打开，并对其设置一下参数
	 * @param path
	 * @return
  	 */
	public boolean openFile(Activity activity, String path) {
	  File file = new File(path);
	  if(!isWPSFile(file) || file == null || !file.exists()) {
		  return false;
	  }
	  
	  mActivity = activity;
	  
	  Intent intent = new Intent();
	  Bundle bundle = new Bundle();
	  bundle.putString(Define.OPEN_MODE, Define.READ_ONLY);		//打开模式 只读模式
		
	  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	  intent.setAction(android.content.Intent.ACTION_VIEW);
	  if (checkPackage(Define.PACKAGENAME_ENT)){
		  intent.setClassName(Define.PACKAGENAME_ENT, Define.CLASSNAME);
	  }else if (checkPackage(Define.PACKAGENAME)){
		  intent.setClassName(Define.PACKAGENAME, Define.CLASSNAME);
	  }else if (checkPackage(Define.PACKAGENAME_ENG)){
		  intent.setClassName(Define.PACKAGENAME_ENG, Define.CLASSNAME);
	  }else if (checkPackage(Define.PACKAGENAME_KING_ENT)){
		  intent.setClassName(Define.PACKAGENAME_KING_ENT, Define.CLASSNAME);
	  }else if (checkPackage(Define.PACKAGENAME_KING_PRO)){
		  intent.setClassName(Define.PACKAGENAME_KING_PRO, Define.CLASSNAME);
	  }else if (checkPackage(Define.PACKAGENAME_KING_PRO_HW)){
		  intent.setClassName(Define.PACKAGENAME_KING_PRO_HW, Define.CLASSNAME);
	  }else{
		  Toast.makeText(mActivity, "文件打开失败，移动wps可能未安装", Toast.LENGTH_LONG).show();
		  return false;
	  }
		
	  Uri uri = Uri.fromFile(file);
	  intent.setData(uri);
	  intent.putExtras(bundle);
		
	  try {
		  mActivity.startActivity(intent);
	  } catch (ActivityNotFoundException e) {
		  e.printStackTrace();
			
		  return false;
	  }
	  return true;
  	}
   
  	/**
  	 * 判断是否是wps能打开的文件
  	 * @param file
  	 * @return
  	 */
  	public boolean isWPSFile(File file) {
  		String end = file.getName().substring(file.getName().lastIndexOf(".") + 1,  
        		file.getName().length()).toLowerCase();  
  		if (end.equals("doc") || end.equals("docx") || end.equals("wps") 
			|| end.equals("dot") || end.equals("wpt") 
			|| end.equals("xls") || end.equals("xlsx") || end.equals("et") 
			|| end.equals("ppt") || end.equals("pptx") || end.equals("dps") 
			|| end.equals("txt") || end.equals("pdf"))
  			return true;
  		
  		return false;
  	}

	/**
  	 * 检测该包名所对应的应用是否存在
  	 * @param packageName
  	 * @return
  	 */
	private boolean checkPackage(String packageName) {  
	    final PackageManager packageManager = mActivity.getPackageManager();//获取packagemanager 
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息 
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名 
       //从pinfo中将包名字逐一取出，压入pName list中 
            if(pinfo != null){ 
            for(int i = 0; i < pinfo.size(); i++){ 
                String pn = pinfo.get(i).packageName; 
                pName.add(pn); 
            } 
        } 
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE 
	}  
}
