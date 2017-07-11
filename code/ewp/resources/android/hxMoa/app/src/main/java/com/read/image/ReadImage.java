package com.read.image;

import java.io.File;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

/*import org.apache.poi.POIXMLDocument;
 import org.apache.poi.POIXMLTextExtractor;
 import org.apache.poi.openxml4j.opc.OPCPackage;
 import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
 import org.apache.poi.xwpf.usermodel.XWPFDocument;*/

public class ReadImage extends Activity {
	private String nameStr = null;
	private WebView view;
	ReadImageFR fr = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getResources().getIdentifier("activity_read_word",
				"layout", getPackageName()));
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		nameStr = bundle.getString("name");
		if (nameStr.startsWith("attach")) {// 
			nameStr = this.getFilesDir().getAbsolutePath() + File.separator
					+ nameStr;
		}
		try {
			this.view = (WebView) findViewById(getResources().getIdentifier(
					"wv_view", "id", getPackageName()));

			this.view.getSettings().setJavaScriptEnabled(true);
			this.view.setInitialScale(300);
			this.view.getSettings().setSupportZoom(true);
			this.view.getSettings().setBuiltInZoomControls(true);
			this.view.getSettings().setCacheMode(
					WebSettings.LOAD_CACHE_ELSE_NETWORK);
			fr = new ReadImageFR(nameStr, this);
			this.view.loadUrl(fr.returnPath);
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialog alert = new AlertDialog.Builder(this).create();
			alert.setTitle(getResources().getIdentifier("open_failed",
					"string", getPackageName()));
			alert.setButton(AlertDialog.BUTTON_POSITIVE, "",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			alert.show();
		}
	}
}
