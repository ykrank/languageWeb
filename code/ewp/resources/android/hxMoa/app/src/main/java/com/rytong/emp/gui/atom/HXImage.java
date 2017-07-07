      package com.rytong.emp.gui.atom;

import android.app.Activity;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Element;
import org.xbill.DNS.utils.base64;

import com.ghbank.moas.R;
import com.rytong.emp.dom.css.Layout;
import com.rytong.emp.render.EMPThreadPool;
import com.rytong.emp.security.Base64;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class HXImage extends Img{
	
	Context context_;
	private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_img/"; 
	private Bitmap bitmap_;
	private String fileName_;
	Timer mTimer;
	public HXImage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context_ = context;		
	}	
	@Override
	public void init(Element element) {
		// TODO Auto-generated method stub
		super.init(element);
		String str = Base64.decodeToString(element.getAttribute("url"));//下载地址
		String fileName = element.getAttribute("fileName");//文件名
		if(fileName != null && !"".equals(fileName)){
			fileName_ = fileName;	
		}else if(fileName !=null && "".equals(fileName)){//
			
			user_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_icon);
		}
		//
		if(isHasBitMap()){
			File file = new File(ALBUM_PATH + fileName_); 
			file.delete();
		}
		setUserBitmap(str);
	}
	/**
	 * 异步下载图片
	 * @param filePath
	 */
	public Bitmap downImage(final String filePath) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputStream is = null;
				try {
					URL url = new URL(filePath);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5 * 1000);
					conn.setRequestMethod("GET");
					int count  =conn.getContentLength();
//					if(count == 0){
//						 bitmap_ = BitmapFactory.decodeResource(getResources(), R.drawable.user_icon);
//						 return bitmap_;
//					}
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
						is = conn.getInputStream();
					}
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();     
			        byte[] buffer = new byte[1024];     
			        int len = 0;     
			        while( (len=is.read(buffer)) != -1){     
			            outStream.write(buffer, 0, len);     
			        }     
			        byte[] data = outStream.toByteArray();
			        if(data.length == 0){
			        	bitmap_ = BitmapFactory.decodeResource(getResources(), R.drawable.user_icon);
			        }else{
			        	bitmap_ = BitmapFactory.decodeByteArray(data, 0, data.length);
			        }
			        saveFile(bitmap_,fileName_);
			        outStream.close();     
					is.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					bitmap_ = BitmapFactory.decodeResource(getResources(), R.drawable.user_icon);			
				}	
			}
		}).start();
		
		return bitmap_;
	} 
    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */  
    public void saveFile(Bitmap bm, String fileName) throws IOException {      	
        File dirFile = new File(ALBUM_PATH);  
        if(!dirFile.exists()){  
            dirFile.mkdir();  
        }  
        File myCaptureFile = new File(ALBUM_PATH + fileName);  
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();  
        bos.close();  
    }  
    boolean isNeedDown;
    private boolean isHasBitMap(){
    	boolean hasPhoto = false;
        File file = new File(ALBUM_PATH + fileName_); 
        //判断本地是否存在图片或图片文件大小为0KB
        if(!file.exists()){
        	isNeedDown = true;
        	hasPhoto =  false;
        } else if(file.exists() && file.length() != 0){
        	hasPhoto = true;
        }else if(file.exists() && file.length() == 0){
        	hasPhoto = false;
        }
    	return hasPhoto;
    }
    Bitmap user_bitmap;
    private void setUserBitmap(String url){
    	boolean hasBitmap = isHasBitMap();
		if(hasBitmap){//判断是否有本地图片
			user_bitmap = bitmap_;
		}else if(isNeedDown){//需要下载
			user_bitmap = downImage(url);
		}else if(!isHasBitMap()){
			user_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_icon);
		}
		//设置头像
		mTimer = new Timer();
		mTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mTimer.cancel();
				mTimer = null;
				
				((Activity)context_).runOnUiThread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setImageBitmap(user_bitmap);
					}							
				});
			}
			
		}, 1000);
	
    }
}
