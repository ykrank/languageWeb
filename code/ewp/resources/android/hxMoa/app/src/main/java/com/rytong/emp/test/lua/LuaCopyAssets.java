package com.rytong.emp.test.lua;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.rytong.emp.render.EMPRender;
import com.rytong.emp.tool.HXUtils;

import android.content.Context;

public class LuaCopyAssets {
	private Context context;
	private EMPRender empRender;

	public LuaCopyAssets(Context context, EMPRender empRender) {
		this.context = context;
		this.empRender = empRender;
	}

	public void copy(final String assets_name) {
		empRender.getGUIFactory().addGUITask(new Runnable() {

			@Override
			public void run() {
				copyTask(assets_name);
			}
		});
	}

	/**
	 * 拷贝文件到内存中的attach文件夹下
	 * 
	 * @param assets_name
	 */
	private void copyTask(String assets_name) {// 2222.docx
		String new_assets_name=null;
		if (assets_name.startsWith("attach")) {
			new_assets_name=assets_name.substring(assets_name.lastIndexOf("/")+1,
					assets_name.length());
		}
		
		String savePath = context.getFilesDir().getAbsolutePath() + HXUtils.attachmentEnvironmentPath;// data/data/files
		String filename = savePath + "/" + new_assets_name;

		File dir = new File(savePath);
		// 如果目录不中存在，创建这个目录
		if (!dir.exists())
			dir.mkdir();
		try {
			if (!(new File(filename)).exists()) {
				InputStream is = context.getResources().getAssets()
						.open(assets_name);
				FileOutputStream fos = new FileOutputStream(filename);
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
