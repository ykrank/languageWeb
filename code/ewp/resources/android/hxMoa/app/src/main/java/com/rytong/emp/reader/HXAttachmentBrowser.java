package com.rytong.emp.reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.artifex.mupdf.MuPDFActivity;
import com.read.image.ReadImage;
import com.rytong.emp.data.FileManager;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.tool.Utils;
import com.wordandexcel.DocFileHandler;
import com.wordandexcel.ExcelRead;
import com.wordandexcel.ReadWordOrXlsx;
import com.wordandexcel.WordRead;

/**
 * <p>
 * 附件浏览器类<br>
 * </p>
 * 
 * @author Zhoucj
 * 
 */
public class HXAttachmentBrowser {

	private Activity mActivity = null;
	private EMPRender mEmpRender = null;

	private ProgressDialog mProgressDialog;

	// 临时缓存doc转换的html文件的目录文件夹
	private static String tmpDocPath;
	// key:doc本地文件路径 ; value:doc文件转换为html后的缓存路径
	private static HashMap<String, String> fileCache = new HashMap<String, String>();

	public String htmlPath;
	public String picturePath;
	public int presentPicture = 0;
	public int screenWidth;
	public FileOutputStream output;
	public File myFile;
	StringBuffer lsb = new StringBuffer();
	String returnPath = "";
	static final int BUFFER = 2048;

	public HXAttachmentBrowser(Activity activity, EMPRender empRender) {
		mActivity = activity;
		mEmpRender = empRender;
	}

	/**
	 * 删除缓存文件夹下所有临时html文件
	 */
	public static void clearCache() {
		if (tmpDocPath != null) {
			// 删除缓存文件夹下所有文件
			FileManager.deleteFile(tmpDocPath);
		}
		fileCache.clear();
	}

	/**
	 * 展示loadingDialog
	 */
	private void showLoading() {
		mEmpRender.getGUIFactory().addGUITask(new Runnable() {
			@Override
			public void run() {
				mProgressDialog = ProgressDialog.show(mActivity, "loading",
						"Please wait...", true, false);
			}
		});
	}

	/**
	 * 隐藏loadingDiolog
	 */
	private void hideLoading() {
		mEmpRender.getGUIFactory().addGUITask(new Runnable() {
			@Override
			public void run() {
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
		});
	}

	/**
	 * <p>
	 * 打开附件。
	 * </p>
	 * 
	 * @param content
	 *            支持以下三种类型 :<br>
	 *            1.URL : <strong>http://... or https://...</strong> 浏览器打开网页链接<br>
	 *            2.File : <strong>file://... </strong>使用新窗口打开附件 <br>
	 *            3.Content: <strong>报文内容</strong>以多任务的方式打开另一个页面(5.3废弃此功能)<br>
	 * 
	 * @return 错误代码
	 */
	public String open(final String content) {
		String errorCode = "";
		if (null != content && !content.equals("")) {
			if (content.startsWith("http") || content.startsWith("https")) {
				// 如果是地址，则用浏览器打开
				errorCode = openHttp(content);
			} else if (content.startsWith("file://")) {
				// 如果是附件，则使用新窗口打开
				errorCode = openFile(content);
			} else {
				// 如果是报文，则解析打开
				errorCode = openXML(content);
			}
		} else {
			errorCode = "请设置要打开的内容!";
		}

		return errorCode;
	}

	/**
	 * <p>
	 * 使用内部浏览器打开网页。
	 * </p>
	 * 
	 * @param content
	 * 
	 * @return 错误代码
	 */
	private String openHttp(final String content) {
		try {
			Uri uri = Uri.parse(content);
			Intent viewIntent = new Intent("android.intent.action.VIEW", uri);
			viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mActivity.startActivity(viewIntent);
		} catch (Exception e) {
			Utils.printException(e);
		}

		String errorCode = "";
		return errorCode;
	}

	private String getMIMEType(String paramString) {
	    String str1 = "";
	    String str2 = paramString.substring(paramString.lastIndexOf(".") + 1, paramString.length()).toLowerCase();
	    if ((str2.equals("m4a")) || (str2.equals("mp3")) || (str2.equals("mid")) || (str2.equals("xmf")) || (str2.equals("ogg")) || (str2.equals("wav")))
	      str1 = "audio";
	    else if ((str2.equals("3gp")) || (str2.equals("mp4")))
	      str1 = "video";
	    else if ((str2.equals("jpg")) || (str2.equals("gif")) || (str2.equals("png")) || (str2.equals("jpeg")) || (str2.equals("bmp")))
	      str1 = "image";
	    else if (str2.equals("apk"))
	      str1 = "application/vnd.android.package-archive";
	    else if (str2.equals("zip"))
	      str1 = "zip";
	    else if (str2.equals("pdf"))
	      str1 = "pdf";
	    else if (str2.equals("doc"))
	      str1 = "doc";
	    else if (str2.equals("docx"))
		      str1 = "docx";
	    else if (str2.equals("xls"))
	      str1 = "xls";
	    else if (str2.equals("xlsx"))
		      str1 = "xlsx";
	    else if ((str2.equals("ppt")) || (str2.equals("pptx")))
	      str1 = "ppt";
	    else
	      str1 = str1 + "/*";
	    return str1;
	}
	
	public String getMIMEType(File paramFile){
	    return getMIMEType(paramFile.getName());
	}
	/**
	 * <p>
	 * 使用新窗口打开附件。
	 * </p>
	 * 
	 * @param content
	 * 
	 * @return 错误代码
	 */
	private String openFile(final String content) {
		String errorCode = "";

		String path = content.substring("file://".length());
		// 获取文件类型
		String mimeType = getMIMEType(new File(path));
		if (!Utils.isEmpty(mimeType)) {
			// 获取文件路径
			String localPath = mEmpRender.getResources().getLocalFilePathToStr(
					path);
			// 文件存在
			if (!Utils.isEmpty(localPath)) {
				if ("ppt".equalsIgnoreCase(mimeType)) {
					errorCode = "暂不支持PPT文件阅读!";
				} else {
					if ("doc".equalsIgnoreCase(mimeType)) {
						showLoading();
						String htmlPath;
						// 有缓存的时候优先使用缓存
						htmlPath = fileCache.get(localPath);
						if (htmlPath == null) {
							DocFileHandler fileHandler = new DocFileHandler(
									localPath, mActivity);
							tmpDocPath = fileHandler.getTmpPath();
							htmlPath = fileHandler.preHandleFile();
							fileCache.put(localPath, htmlPath);
						} else {
							Utils.printLog("AttachmentBrower",
									"-----------use cache-----htmlPath:"
											+ htmlPath);
						}
						hideLoading();
						Intent intent = new Intent();
						intent.setClass(mActivity, WordRead.class);
						// intent.setClass(mActivity, ReadWord2.class);
						Bundle bundle = new Bundle();
						bundle.putString("name", htmlPath);
						intent.putExtras(bundle);
						mActivity.startActivity(intent);
						mActivity
								.overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
					} else if ("xls".equalsIgnoreCase(mimeType)) {
						Intent intent = new Intent();
						intent.setClass(mActivity, ExcelRead.class);
						Bundle bundle = new Bundle();
						bundle.putString("name", localPath);
						intent.putExtras(bundle);
						mActivity.startActivity(intent);
					} else if ("pdf".equalsIgnoreCase(mimeType)) {
						if ((localPath.startsWith("offline/")) || (localPath.startsWith("attach/")))
				              try
				              {
				                InputStream in = this.mActivity.getAssets().open(localPath);
				                localPath = FileManager.FILEROOT.concat("temp-resources/").concat(path);
				                FileManager.copyFile(in, localPath);
				                in.close();
				              } catch (Exception e) {
				                errorCode = "文件不存在!";
				                return errorCode;
				              }

				            Uri uri = Uri.parse(localPath);
				            Intent intent = new Intent();
				            intent = new Intent(this.mActivity, MuPDFActivity.class);
				            intent.setAction("android.intent.action.VIEW");
				            intent.setData(uri);
				            this.mActivity.startActivity(intent);
					} else if ("docx".equalsIgnoreCase(mimeType)) {
						Intent intent = new Intent();
						// intent.setClass(mActivity, WordRead.class);
						intent.setClass(mActivity, ReadWordOrXlsx.class);
						Bundle bundle = new Bundle();
						bundle.putString("name", localPath);
						intent.putExtras(bundle);
						mActivity.startActivity(intent);
						mActivity
								.overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
					}else if ("xlsx".equalsIgnoreCase(mimeType)) {
						Intent intent = new Intent();
						intent.setClass(mActivity, ReadWordOrXlsx.class);
						Bundle bundle = new Bundle();
						bundle.putString("name", localPath);
						intent.putExtras(bundle);
						mActivity.startActivity(intent);
						mActivity
								.overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
					} else if ("image".equalsIgnoreCase(mimeType)) {
						Intent intent = new Intent();
						intent.setClass(mActivity, ReadImage.class);
						Bundle bundle = new Bundle();
						bundle.putString("name", localPath);
						intent.putExtras(bundle);
						mActivity.startActivity(intent);
						mActivity
								.overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
					}
				}
			} else {
				errorCode = "文件不存在!";
			}
		} else {
			errorCode = "不支持的附件类型!";
		}
		return errorCode;
	}

	/**
	 * <p>
	 * 解析报文，打开另一个页面。
	 * </p>
	 * 
	 * @param content
	 * 
	 * @return 错误代码
	 */
	private String openXML(final String content) {
		String errorCode = "";
		String reply = Utils.unescapeHTML(content);
		reply = Utils.insteadOfSpecillCharacter(reply, true);
		mEmpRender.load(reply);
		return errorCode;
	}

}
