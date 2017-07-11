package com.rytong.emp.test.lua;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import cn.wps.moffice.WPS;

import com.rytong.emp.android.AndroidEMPBuilder;
import com.rytong.emp.data.FileManager;
import com.rytong.emp.data.SqlDB;
import com.rytong.emp.lua.java.CLEntity;
import com.rytong.emp.lua.java.CLuaFunction;
import com.rytong.emp.net.HttpManager;
import com.rytong.emp.reader.HXAttachmentBrowser;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.render.EMPThreadPool;
import com.rytong.emp.test.MainActivity;
import com.rytong.emp.tool.HXUtils;
import com.rytong.emp.tool.Utils;

/**
 * 附件处理接口
 * @author tanguozhi
 */
public class LuaAttachment {
	
	Activity mActivity;
	/** empRender对象。 */
	EMPRender mEMPRender = null;
	
	final int ATTACHMENT_FLAG = 2016;
	private HashMap<String, String> mConnectionMap = new HashMap<String, String>();
	private ArrayList<String> mFileNameList = new ArrayList<String>();
	/**
	 * 构造方法。
	 * @param empRender empRender对象。
	 */
	public LuaAttachment(Activity activity, EMPRender empRender) {
		mActivity = activity;
		mEMPRender = empRender;
		
		HXUtils.remollAllAttachment();
	}

	
	public void cancel(String fileName) {
		//进度条
		MainActivity.mEmpLua.loadString("window:hide("+ATTACHMENT_FLAG+");");
		
		if(fileName==null) {
			mFileNameList.removeAll(mFileNameList);
			mConnectionMap.clear();
		} else {
			mFileNameList.remove(fileName);
			Iterator<String> it = mConnectionMap.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String value = mConnectionMap.get(key);
				if(fileName.equals(value)) {
					mConnectionMap.remove(key);
					break;
				}
			}
		}
	}
	public void cancel() {
		cancel(null);
	}
	
	/**
	 * 附件下载
	 * @param url		下载地址
	 * @param callLua	回调方法
	 */
	public void download(final String url, final String fileName, final CLuaFunction callLua){
		if(mFileNameList.contains(fileName))
			return;
		
		mFileNameList.add(fileName);
		
		mEMPRender.runTask(new EMPThreadPool.Task(100) {
			HttpURLConnection c = null;
			byte[] buffer = new byte[1024];
			boolean isDownloadSuccess;

			@Override
			public void doRun() throws Exception {
				
				try {  
	                //连接地址  
	                URL u = new URL(url);  
	                c = (HttpURLConnection) u.openConnection();  
	                
	                //添加会话
	                if(HttpManager.EMP_COOKIE!=null && !"".equals(HttpManager.EMP_COOKIE)) {
	                	c.addRequestProperty("Cookie", HttpManager.EMP_COOKIE);
		                c.addRequestProperty("X-Emp-Cookie", HttpManager.EMP_COOKIE);
	                }
	                
	                
	                mConnectionMap.put(c.toString(), fileName);
	                
	                c.setRequestMethod("GET");
	                c.setDoOutput(true);
	                c.connect();
	                
	                //计算文件长度  
	                int lenghtOfFile = c.getContentLength();
	                
	                if(lenghtOfFile==0) {
	                	return;
	                }
	                
	                  
	                InputStream in = c.getInputStream();
	  
	               //下载的代码  
	                  
	                int len1 = 0;  
	                long total = 0;  
	                  
	                ByteArrayOutputStream outSteam = new ByteArrayOutputStream(); 
	                
	                while ((len1 = in.read(buffer)) > 0) {  
	                	if(mConnectionMap.get(c.toString())==null) {
	                		break;
	                	}
	                	
	                    total += len1; //total = total + len1  
	                    String process = "" + (int)((total*100)/lenghtOfFile);
	                    
	                    outSteam.write(buffer, 0, len1);  
	                    
	                    //进度条
	    				MainActivity.mEmpLua.loadString("downLoadProcess('"+fileName+"', '"+process+"');");
	                }  
	                buffer = outSteam.toByteArray();
	                outSteam.close();
	                
	                isDownloadSuccess = true;
	            } catch (Exception e) {
					CLEntity entity = new CLEntity();
	            	if(mConnectionMap.get(c.toString())==null) {
	            		entity.put("code", "2");
	            	} else {
	            		entity.put("code", "0");
	            	}
					
					entity.put("fileName", fileName);
					
					MainActivity.mEmpLua.callback(callLua.mFunctionIndex, new Object[] { entity });
	            }
			}

			@Override
			public void onSuccess() {
				CLEntity entity = new CLEntity();
				entity.put("fileName", fileName);
				if(mConnectionMap.get(c.toString())==null) {
					entity.put("code", "2");
					MainActivity.mEmpLua.callback(callLua.mFunctionIndex, new Object[] { entity });
					return;
            	}
				
				mConnectionMap.remove(c.toString());
				mFileNameList.remove(fileName);
				
				if (buffer != null && isDownloadSuccess) {
					try {
						String path = FileManager.FILEROOT.concat(FileManager.WRITEROOT).concat(fileName);
						//保存附件
						FileManager.saveFile(path, buffer);
						
						//保存附件路径到数据库
						HXUtils.saveOrUpdateAttachment(path);
						
						entity.put("code", "1");
					} catch (Exception e) {
						entity.put("code", "0");
						Utils.printException(e);
					}
				} else {
					entity.put("code", "0");
				}
				MainActivity.mEmpLua.callback(callLua.mFunctionIndex, new Object[] { entity });
			}

			@Override
			public void onFailure() {
				
				CLEntity entity = new CLEntity();
				if(mConnectionMap.get(c.toString())==null) {
					entity.put("code", "2");
					MainActivity.mEmpLua.callback(callLua.mFunctionIndex, new Object[] { entity });
					return;
            	}
				
				mConnectionMap.remove(c.toString());
				mFileNameList.remove(fileName);
				
				entity.put("code", "0");
				entity.put("fileName", fileName);
				MainActivity.mEmpLua.callback(callLua.mFunctionIndex, new Object[] { entity });
			}
		});
	}
	
	/**
	 * 判断附件是否已经存在
	 * @param fileName	附件名称
	 * @return	true为已存在，false为不存在
	 */
	public boolean isExit(String fileName){
		String path = FileManager.FILEROOT.concat(FileManager.WRITEROOT).concat(fileName);
		
		String value = HXUtils.getAttachment();
		if(value!=null && value.contains(path)) {
			return true;
		}
		
		File file = new File(path);
		if (file.exists()) {
			FileManager.deleteFile(path);
		} 
		
		return false;
	}
	/**
	 * 获取附件路径
	 * @param fileName	附件名称
	 * @return	返回附件路径
	 */
	public String getAttachmentPath (String fileName) {
		return FileManager.FILEROOT.concat(FileManager.WRITEROOT).concat(fileName);
	}
	
	/**
	 * <p>
	 * 1.浏览器打开网页链接 2.打开附件3.打开报文页面(5.3废弃功能3)
	 * </p>
	 * <p>
	 * Syntax: window:open(url/path);<br>
	 * </p>
	 * 
	 * @param luaIndex
	 * @param content
	 *            支持以下三种类型 :<br>
	 *            1.URL : <strong>http://... or https://...</strong> 浏览器打开网页链接<br>
	 *            2.File : <strong>file://... </strong>使用新窗口打开附件 <br>
	 *            3.Content: <strong>报文内容</strong>以多任务的方式打开另一个页面<br>
	 */
	public void openRead(String content) {
		Activity activity = AndroidEMPBuilder.getActivity(mEMPRender);
		
		boolean isInstallWPS = WPS.getInstances(activity).isInstallWPS();
		if(isInstallWPS) {	//打开WPS阅读
			String contentTemp = content.replace("file://", "");
			//将文件拷贝到外部存储
			String environmentPath = Environment.getExternalStorageDirectory() + HXUtils.attachmentEnvironmentPath+"/"+ contentTemp;
			
			String path = getAttachmentPath(contentTemp);//activity.getFilesDir().getAbsolutePath()+ HXUtils.attachmentEnvironmentPath+"/"+contentTemp;//
			File file = new File(path);
			try {
				InputStream in = new FileInputStream(file);
				FileManager.copyFile(in, environmentPath);
				
				boolean bFlag = WPS.getInstances(activity).openFile(activity, environmentPath);
				//打开成功
				if(bFlag)
					return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//打开本地阅读器阅读
		HXAttachmentBrowser browser = new HXAttachmentBrowser(activity, mEMPRender);
		String errorCode = browser.open(content);
		if (!Utils.isEmpty(errorCode)) {
			mEMPRender.errorAlert(errorCode);
		}
	}
}