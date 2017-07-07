package com.rytong.emp.face.ac;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.aeye.android.facerecog.AERecogInterface;
import com.aeye.android.facerecog.AERecogManager;

public class FaceActivity extends Activity implements AERecogInterface {
	
	private Intent intent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		intent = new Intent();

		AERecogManager.getAERecogManager().AEYE_Init(this);
		
		startRecog();
	}

	@Override
	public void onSnapFinish(int value, String data, Rect rect) {
		Log.e("ZDX", "onSnapFinish" + data);

		intent.putExtra("SNAP_DATA", data);
	}

	@Override
	public void onRecogFinish(int value, String data) {
		Log.e("ZDX", "onRecogFinish" + data);

		intent.putExtra("VALUE", value+"");
		intent.putExtra("DATA", data);

		setResult(Activity.RESULT_OK, intent);
        finish();
	}

	private void startRecog() {
		Bundle paras = new Bundle();
		paras.putInt("overtime", 8);	//biotype_algversion_time_perdetmotion
		paras.putString("timemark", "2015040512345098779900");//timemark
		paras.putInt("recogtime", 8);
		paras.putInt("alivetimes", 1);//biotype_algversion_det_times
		paras.putString("random", "2132131231");
		paras.putInt("bottombackgroundcolor", 0x00000000);//bottombackgroundcolor
		paras.putInt("topbackgroundcolor", 0xEE222222);//topbackgroundcolor
		paras.putInt("showbackbtn", 1);//showbackbtn
		paras.putInt("AliveLevel", 5);
		paras.putInt("voiceoff", 1);
		paras.putInt("picnum", 1);//biotype_algversion_recogmaxfeatnum
		paras.putInt("displayOri", 90);
		paras.putInt("captureOri", -90);

		AERecogManager.getAERecogManager().AEYE_SetListener(this);
		AERecogManager.getAERecogManager().AEYE_SetParameter(paras);
		AERecogManager.getAERecogManager().AEYE_BeginRecog(this);
	}

}
