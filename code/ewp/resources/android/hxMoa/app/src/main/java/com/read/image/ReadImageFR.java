package com.read.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.util.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Xml;

public class ReadImageFR {
	private String nameStr;
	public String htmlPath;
	public String picturePath;
	public List pictures;
	public int presentPicture = 0;
	public int screenWidth;
	public FileOutputStream output;
	public File myFile;
	StringBuffer lsb = new StringBuffer();
	String returnPath = "";
	static final int BUFFER = 2048;

	private Activity mActivity;

	public ReadImageFR(String namepath, Activity mActivity) {
		this.nameStr = namepath;
		this.mActivity = mActivity;
		read();
	}

	public void read() {
		if ((this.nameStr.endsWith("jpg")) || (this.nameStr.endsWith("gif")) || (this.nameStr.endsWith("png")) || (this.nameStr.endsWith("jpeg")) || (this.nameStr.endsWith("bmp"))) {
			this.makeFile();
			this.readIMAGE();
			returnPath = "file:///" + this.htmlPath;
		}
	}

	public void makeFile() {
		String pathString = mActivity.getFilesDir().getPath() + File.separator
				+ Utils.TEMPROOT + File.separator + "loveReader"
				+ File.separator + "docx";
		File dirFile = new File(pathString);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		//截取文件名称，用于生成对应的html文件的想对应的名称。
		String htmlName = nameStr.substring(nameStr.lastIndexOf("/"),
				nameStr.length());
		htmlName = htmlName.substring(htmlName.indexOf("."));
		
		File myFile = new File(dirFile + File.separator + htmlName + ".html");//
		if (!myFile.exists()) {//
			try {
				myFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}//
		}
		this.htmlPath = myFile.getAbsolutePath();//
	}
	public void readIMAGE() {
		String river = "";
		try {
			this.myFile = new File(this.htmlPath);// new一个File,路径为html文件
			this.output = new FileOutputStream(this.myFile);// new一个流,目标为html文件
			String head = "<!DOCTYPE><html><meta charset=\"utf-8\"><body>";// 定义头文件,我在这里加了utf-8,不然会出现乱码
			String end = "</body></html>";
			String tableBegin = "<table style=\"border-collapse:collapse\" border=1 bordercolor=\"black\">";
			String tableEnd = "</table>";
			String rowBegin = "<tr>";
			String rowEnd = "</tr>";
			String colBegin = "<td>";
			String colEnd = "</td>";
			
			String image = "<img src='"+this.nameStr+"' />";
			this.output.write(head.getBytes());// 写如头部
			this.output.write(tableBegin.getBytes());// 写如头部
			this.output.write(rowBegin.getBytes());
			this.output.write(colBegin.getBytes());
			
			this.output.write(image.getBytes());
			
			this.output.write(colEnd.getBytes());
			this.output.write(rowEnd.getBytes());
			this.output.write(tableEnd.getBytes());
			this.output.write(end.getBytes());
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (river == null) {
			river = "解析文件出现问题";
		}
	}
}
