package com.rytong.emp.test.config;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.rytong.emp.data.AndroidPreferenceDB;
import com.rytong.emp.data.FileManager;
import com.rytong.emp.gui.atom.property.PropertyImg.IMGMODE;
import com.rytong.emp.gui.atom.property.handle.TextEffectHandler.BreakMode;
import com.rytong.emp.net.ClientHello;
import com.rytong.emp.test.MainActivity;
import com.rytong.emp.tool.Utils;

/**
 * 系统配置页,替换原来的ip.xml 与系统框架独立出来
 */
public class ConfigActivity extends Activity {

	private Activity mActivity;
	private RadioGroup mIsUnitTestRg;
	private Spinner mNameSp, mChannelSp, mOfflineSp;
	private ToggleButton mTrackTob, mPerfTestTob, mPushTob, mSMTob,
			mDevModeTob, mErrorAlertTob, mLogTob, mCacheModeTob;
	private Button mAXmlBtn, mExplainBtn, mTutorialBtn, mConfirmBtn, mClearDataBtn;
	private CheckBox mEmpEditorCbx;
	private EditText mEmpEditorHostEdt, mHostEdt;
	private LinearLayout mDevModeLayout;

	private Button[] mIpBtn = new Button[2]; // 共2个ip按钮

	private View.OnClickListener mClickListedner, mSubmitListener;
	private CompoundButton.OnCheckedChangeListener mCheckedChangeListener;

	private ConfigInfo mConfig;
	private String mLastHost, mLastEditorHost;

	/****** sdk全局属性配置相关 ********/
	private SDKConfig sdkConfig;

	private ToggleButton mSDKFullConfigTob;

	private LinearLayout mConfigSDKFullConfigLayout;

	// label
	private Spinner mLineBreakMode;
	private EditText mShadowColor;
	private EditText mShadowOffset;

	// password
	private ToggleButton mPwdIsOrder;
	private EditText mPwdSubstitute;

	// button
	private EditText mBtnDelayTime;

	// input_text
	private Spinner mClearMode;

	// image
	private EditText mLoadingImg;
	private EditText mLoadFailed;

	// link
	private Spinner mLinkBreakMode;

	// checkbox
	private CheckBox mChLabelBreakMode, mChLinkBreakMode;
	
	//OptimalWidth
	private EditText mOptimalWidth;

	/**********控件间距及边距配置相关************/
	//toggle
	private ToggleButton mControlPaddingToggle;
	private LinearLayout mControlPanal;
	
	//ControlPaddingConfig
	private ControlPaddingConfig mControlPaddingConfig;
	//div
	private EditText mControlPaddingDivLeft;
	private EditText mControlPaddingDivUp;
	private EditText mControlPaddingDivRight;
	private EditText mControlPaddingDivDown;
	
	private EditText mControlPaddingDivChildCol;
	private EditText mControlPaddingDivChildRow;
	
	//body
	private EditText mControlPaddingBodyLeft;
	private EditText mControlPaddingBodyUp;
	private EditText mControlPaddingBodyRight;
	private EditText mControlPaddingBodyDown;
	
	private EditText mControlPaddingBodyChildCol;
	private EditText mControlPaddingBodyChildRow;
	
	//form
	private EditText mControlPaddingFormLeft;
	private EditText mControlPaddingFormUp;
	private EditText mControlPaddingFormRight;
	private EditText mControlPaddingFormDown;
	
	private EditText mControlPaddingFormChildCol;
	private EditText mControlPaddingFormChildRow;
	
	//table>td
	private EditText mControlPaddingTdLeft;
	private EditText mControlPaddingTdUp;
	private EditText mControlPaddingTdRight;
	private EditText mControlPaddingTdDown;
	
	private EditText mControlPaddingTdChildCol;
	private EditText mControlPaddingTdChildRow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏系统标题栏
		setContentView(Utils.getResourcesId(this, "config", "layout"));
		mActivity = this;
		AndroidPreferenceDB.initialize(mActivity);

		initView();

		initData();
	}

	/**
	 * 启动系统
	 */
	private void startAPP() {

		mConfig.mAPPName = (String) mNameSp.getSelectedItem();
		mConfig.mChannel = (Float) mChannelSp.getSelectedItem();
		mConfig.mOffline = (Float) mOfflineSp.getSelectedItem();

		// SDK全局属性
		if (mLineBreakMode.isEnabled()) {
			sdkConfig.mbreakMode = (BreakMode) mLineBreakMode.getSelectedItem();
		} else {
			sdkConfig.mbreakMode = null;
		}
		if (mLinkBreakMode.isEnabled()) {
			sdkConfig.mLinkBreakMode = (BreakMode) mLinkBreakMode.getSelectedItem();
		} else {
			sdkConfig.mLinkBreakMode = null;
		}
		if(!Utils.isEmpty(mShadowColor.getText().toString())){
			sdkConfig.mShadowColor = mShadowColor.getText().toString();
		}
		sdkConfig.mClearMode = (IMGMODE) mClearMode.getSelectedItem();
		if (mShadowOffset.getText().toString() != null && !"".equals(mShadowOffset.getText().toString())) {
			String[] offset = new String[2];
			offset = mShadowOffset.getText().toString().split(",");
			sdkConfig.mShadowOffsetX = Integer.parseInt(offset[0]);
			sdkConfig.mShadowOffsetY = Integer.parseInt(offset[1]);
		}
		if (mPwdSubstitute.getText().toString() != null && !"".equals(mPwdSubstitute.getText().toString())) {
			sdkConfig.mSubstitute = mPwdSubstitute.getText().toString();
		}
		if (mBtnDelayTime.getText().toString() != null && !"".equals(mBtnDelayTime.getText().toString())) {
			sdkConfig.mDelayTime = Integer.parseInt(mBtnDelayTime.getText()
					.toString());
		}
		if (mLoadingImg.getText().toString() != null && !"".equals(mLoadingImg.getText().toString())) {
			sdkConfig.mLoadingImg = mLoadingImg.getText().toString();
		}
		if (mLoadFailed.getText().toString() != null && !"".equals(mLoadFailed.getText().toString())) {
			sdkConfig.mLoadFailed = mLoadFailed.getText().toString();
		}
		if (mOptimalWidth.getText().toString() != null
				&& !"".equals(mOptimalWidth.getText().toString())) {
			sdkConfig.mOptimalWidth = Integer.parseInt(mOptimalWidth.getText().toString());
		}
		
		//控件边距全局属性配置
		//div
		if(TextUtils.isDigitsOnly(mControlPaddingDivLeft.getText().toString())){
			
			mControlPaddingConfig.DIV_DEF_PADDING_LEFT = Integer.parseInt(mControlPaddingDivLeft.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingDivUp.getText().toString())){
			mControlPaddingConfig.DIV_DEF_PADDING_TOP = Integer.parseInt(mControlPaddingDivUp.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingDivRight.getText().toString())){
			mControlPaddingConfig.DIV_DEF_PADDING_RIGHT = Integer.parseInt(mControlPaddingDivRight.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingDivDown.getText().toString())){
			mControlPaddingConfig.DIV_DEF_PADDING_BOTTOM = Integer.parseInt(mControlPaddingDivDown.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingDivChildCol.getText().toString())){
			mControlPaddingConfig.DIV_DEF_LINESPACING = Integer.parseInt(mControlPaddingDivChildCol.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingDivChildRow.getText().toString())){
			mControlPaddingConfig.DIV_DEF_ROWSPACING = Integer.parseInt(mControlPaddingDivChildRow.getText().toString());
		}
		//body
		if(TextUtils.isDigitsOnly(mControlPaddingBodyLeft.getText().toString())){
			
			mControlPaddingConfig.BODY_DEF_PADDING_LEFT = Integer.parseInt(mControlPaddingBodyLeft.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingBodyUp.getText().toString())){
			mControlPaddingConfig.BODY_DEF_PADDING_TOP = Integer.parseInt(mControlPaddingBodyUp.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingBodyRight.getText().toString())){
			mControlPaddingConfig.BODY_DEF_PADDING_RIGHT = Integer.parseInt(mControlPaddingBodyRight.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingBodyDown.getText().toString())){
			mControlPaddingConfig.BODY_DEF_PADDING_BOTTOM = Integer.parseInt(mControlPaddingBodyDown.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingBodyChildCol.getText().toString())){
			mControlPaddingConfig.BODY_DEF_LINESPACING = Integer.parseInt(mControlPaddingBodyChildCol.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingBodyChildRow.getText().toString())){
			mControlPaddingConfig.BODY_DEF_ROWSPACING = Integer.parseInt(mControlPaddingBodyChildRow.getText().toString());
		}
		//form
		if(TextUtils.isDigitsOnly(mControlPaddingFormLeft.getText().toString())){
			
			mControlPaddingConfig.FORM_DEF_PADDING_LEFT = Integer.parseInt(mControlPaddingFormLeft.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingFormUp.getText().toString())){
			mControlPaddingConfig.FORM_DEF_PADDING_TOP = Integer.parseInt(mControlPaddingFormUp.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingFormRight.getText().toString())){
			mControlPaddingConfig.FORM_DEF_PADDING_RIGHT = Integer.parseInt(mControlPaddingFormRight.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingFormDown.getText().toString())){
			mControlPaddingConfig.FORM_DEF_PADDING_BOTTOM = Integer.parseInt(mControlPaddingFormDown.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingFormChildCol.getText().toString())){
			mControlPaddingConfig.FORM_DEF_LINESPACING = Integer.parseInt(mControlPaddingFormChildCol.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingFormChildRow.getText().toString())){
			mControlPaddingConfig.FORM_DEF_ROWSPACING = Integer.parseInt(mControlPaddingFormChildRow.getText().toString());
		}
		//td
		if(TextUtils.isDigitsOnly(mControlPaddingTdLeft.getText().toString())){
			
			mControlPaddingConfig.TD_DEF_PADDING_LEFT = Integer.parseInt(mControlPaddingTdLeft.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingTdUp.getText().toString())){
			mControlPaddingConfig.TD_DEF_PADDING_TOP = Integer.parseInt(mControlPaddingTdUp.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingTdRight.getText().toString())){
			mControlPaddingConfig.TD_DEF_PADDING_RIGHT = Integer.parseInt(mControlPaddingTdRight.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingTdDown.getText().toString())){
			mControlPaddingConfig.TD_DEF_PADDING_BOTTOM = Integer.parseInt(mControlPaddingTdDown.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingTdChildCol.getText().toString())){
			mControlPaddingConfig.TD_DEF_LINESPACING = Integer.parseInt(mControlPaddingTdChildCol.getText().toString());
		}
		if(TextUtils.isDigitsOnly(mControlPaddingTdChildRow.getText().toString())){
			mControlPaddingConfig.TD_DEF_ROWSPACING = Integer.parseInt(mControlPaddingTdChildRow.getText().toString());
		}

		if (!mConfig.mIsDevMode) { // 以下设置仅在开发模式下有效
			mConfig.mIsErrorAlert = false;
			mConfig.mIsCacheMode = false;
		}

		Intent intent = new Intent(mActivity, MainActivity.class);
		intent.putExtra("ConfigInfo", mConfig);

		intent.putExtra("SDKConfig", sdkConfig);
		intent.putExtra("ControlPaddingConfig", mControlPaddingConfig);
		startActivity(intent);
		finish();
	}

	private void initView() {
		// 点击ip设置按钮
		mSubmitListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mConfig.mIsEmpEditor) {// 如果使用empedtor
					mConfig.mEditorHost = mEmpEditorHostEdt.getText().toString();
					if (TextUtils.isEmpty(mConfig.mEditorHost)) {
						toast("请检查 Editor 地址!");
					}
				}

				if (v == mTutorialBtn) {
					mConfig.mIsTutorial = true;
					startAPP();
				} else {
					String host;
					if (v == mConfirmBtn) { // 确定按钮取输入框
						host = mHostEdt.getText().toString().trim();
					} else {
						host = ((Button) v).getText().toString().trim();
					}
					// 先验证host地址的合法性
					boolean flag = hostVerification(host);
					if (flag) {
						startAPP();
					} else {
						toast("请检查服务器地址!");
					}
				}
			}
		};

		mClickListedner = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == mExplainBtn) { // 点击说明按钮
					String msg = "\tEmpEditor是辅助模板人员进行开发的一套工具。\n\t默认情况下不开启，当选中左侧checkbox后，"
							+ "下方地址栏中可以输入EmpEditor服务器的地址，此时进入任何EWP页面，都会同时开启EmpEditor进行页面的调试。\n"
							+ "\t如果选中左侧checkbox后点击了\"去Tutorial\"按钮，则不会进入EWP页面，而是和左侧EmpEditor服务器地址建立socket连接，"
							+ "之后就可以和网页上的教程进行交互了。";

					alert(msg);
				} else if (v == mAXmlBtn) { // 点击[本地a.xml] 按钮
					mConfig.mIsAXml = true;
					startAPP();
				} else if (v == mClearDataBtn) {// 点击[清除数据]按钮
					clearData();
				}
			}
		};

		mCheckedChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (buttonView == mTrackTob) {
					mConfig.mIsTrack = isChecked;
				} else if (buttonView == mPerfTestTob) {
					mConfig.mIsPerfTest = isChecked;
				} else if (buttonView == mPushTob) {
					mConfig.mIsPush = isChecked;
				} else if (buttonView == mSMTob) {
					mConfig.mIsSM = isChecked;
				} else if (buttonView == mDevModeTob) {
					if (isChecked) {
						mDevModeLayout.setVisibility(View.VISIBLE);
					} else {
						mDevModeLayout.setVisibility(View.GONE);
					}
					mConfig.mIsDevMode = isChecked;
				} else if (buttonView == mErrorAlertTob) {
					mConfig.mIsErrorAlert = isChecked;
				} else if (buttonView == mLogTob) {
					mConfig.mIsLog = isChecked;
				} else if (buttonView == mCacheModeTob) {
					mConfig.mIsCacheMode = isChecked;
				} else if (buttonView == mEmpEditorCbx) {
					mConfig.mIsEmpEditor = isChecked;

					mEmpEditorHostEdt.setEnabled(isChecked);
					mTutorialBtn.setEnabled(isChecked);
				} else if (buttonView == mSDKFullConfigTob) {// SDK配置
					if (isChecked) {
						mConfigSDKFullConfigLayout.setVisibility(View.VISIBLE);
					} else {
						mConfigSDKFullConfigLayout.setVisibility(View.GONE);
					}
				} else if (buttonView == mPwdIsOrder) {
					if (isChecked) {
						sdkConfig.mIsOrder = true;
					} else {
						sdkConfig.mIsOrder = false;
					}
				} else if (buttonView == mChLabelBreakMode) {
					if (isChecked) {
						mLineBreakMode.setEnabled(true);
						mLineBreakMode.setBackgroundColor(Color.parseColor("#550000FF"));
					} else {
						mLineBreakMode.setBackgroundColor(getResources().getColor(Utils.getResourcesId(ConfigActivity.this, "grgray", "color")));
						mLineBreakMode.setEnabled(false);
					}
				} else if (buttonView == mChLinkBreakMode) {
					if (isChecked) {
						mLinkBreakMode.setEnabled(true);
						mLinkBreakMode.setBackgroundColor(Color.parseColor("#550000FF"));
					} else {
						mLinkBreakMode.setBackgroundColor(getResources().getColor(Utils.getResourcesId(ConfigActivity.this, "grgray", "color")));
						mLinkBreakMode.setEnabled(false);
					}
				} else if(buttonView == mControlPaddingToggle) {
					if (isChecked){
						mControlPanal.setVisibility(View.VISIBLE);
					}else{
						mControlPanal.setVisibility(View.GONE);
					}
				}
			}
		};

		mNameSp = (Spinner) findView("config_NameSp");
		mChannelSp = (Spinner) findView("config_ChannelSp");
		mOfflineSp = (Spinner) findView("config_OfflineSp");

		mIsUnitTestRg = (RadioGroup) findView("config_UnitRg");

		mTrackTob = (ToggleButton) findView("config_TrackTob");
		mPerfTestTob = (ToggleButton) findView("config_PerfTestTob");
		mPushTob = (ToggleButton) findView("config_PushTob");
		mSMTob = (ToggleButton) findView("config_SMTob");
		mDevModeTob = (ToggleButton) findView("config_DevModeTob");
		mErrorAlertTob = (ToggleButton) findView("config_ErrorAlertTob");
		mLogTob = (ToggleButton) findView("config_LogTob");
		mCacheModeTob = (ToggleButton) findView("config_CacheModeTob");

		mEmpEditorCbx = (CheckBox) findView("config_EmpEditorCbx");

		mConfirmBtn = (Button) findView("config_ConfirmBtn");
		mExplainBtn = (Button) findView("config_ExplainBtn");
		mAXmlBtn = (Button) findView("config_AXmlBtn");
		mTutorialBtn = (Button) findView("config_TutorialBtn");
		mClearDataBtn = (Button)findView("config_ClearDataBtn");

		mDevModeLayout = (LinearLayout) findView("config_DevModeLayout");

		mHostEdt = (EditText) findView("config_HostEdt");
		mEmpEditorHostEdt = (EditText) findView("config_EmpHostEdt");

		// SDK全局属性
		mSDKFullConfigTob = (ToggleButton) findView("SDK_full_config");
		mConfigSDKFullConfigLayout = (LinearLayout) findView("config_SDKFullConfigLayout");
		mLineBreakMode = (Spinner) findView("sdk_config_linebreakmode");
		mShadowColor = (EditText) findView("sdk_config_shadow_color");
		mShadowOffset = (EditText) findView("sdk_config_shadowoffset");
		mPwdIsOrder = (ToggleButton) findView("sdk_pwd_is_order");
		mPwdSubstitute = (EditText) findView("sdk_pwd_substitute");
		mBtnDelayTime = (EditText) findView("sdk_et_delay_time");
		mClearMode = (Spinner) findView("sdk_input_clearmode");
		mLoadingImg = (EditText) findView("sdk_et_img_loading");
		mLoadFailed = (EditText) findView("sdk_et_img_failed");
		mLinkBreakMode = (Spinner) findView("sdk_link_linebreakmode");
		mChLabelBreakMode = (CheckBox) findView("sdk_ch_break_mode_on");
		mChLinkBreakMode = (CheckBox) findView("sdk_ch_link_break_mode_on");
		mOptimalWidth= (EditText) findView("sdk_optimalwidth");
		
		//控件间距全局属性配置
		mControlPaddingToggle = (ToggleButton) findView("control_padding_togg");
		mControlPanal = (LinearLayout) findView("control_padding_layout");
		
		mControlPaddingBodyLeft = (EditText) findView("control_padding_body_left");
		mControlPaddingBodyUp = (EditText) findView("control_padding_body_up");
		mControlPaddingBodyRight = (EditText) findView("control_padding_body_right");
		mControlPaddingBodyDown = (EditText) findView("control_padding_body_down");
		mControlPaddingBodyChildCol = (EditText) findView("control_padding_et_body_child_col");
		mControlPaddingBodyChildRow = (EditText) findView("control_padding_et_body_child_row");
		
		mControlPaddingDivLeft = (EditText) findView("control_padding_div_left");
		mControlPaddingDivUp = (EditText) findView("control_padding_div_up");
		mControlPaddingDivRight = (EditText) findView("control_padding_div_right");
		mControlPaddingDivDown = (EditText) findView("control_padding_div_down");
		mControlPaddingDivChildCol = (EditText) findView("control_padding_et_div_child_col");
		mControlPaddingDivChildRow = (EditText) findView("control_padding_et_div_child_row");
		
		mControlPaddingFormLeft = (EditText) findView("control_padding_form_left");
		mControlPaddingFormUp = (EditText) findView("control_padding_form_up");
		mControlPaddingFormRight = (EditText) findView("control_padding_form_right");
		mControlPaddingFormDown = (EditText) findView("control_padding_form_down");
		mControlPaddingFormChildCol = (EditText) findView("control_padding_et_form_child_col");
		mControlPaddingFormChildRow = (EditText) findView("control_padding_et_form_child_row");
		
		mControlPaddingTdLeft = (EditText) findView("control_padding_td_left");
		mControlPaddingTdUp = (EditText) findView("control_padding_td_up");
		mControlPaddingTdRight = (EditText) findView("control_padding_td_right");
		mControlPaddingTdDown = (EditText) findView("control_padding_td_down");
		mControlPaddingTdChildCol = (EditText) findView("control_padding_et_td_child_col");
		mControlPaddingTdChildRow = (EditText) findView("control_padding_et_td_child_row");
		
		//一开始lineBreakMode与linkBreakMode下拉菜单处于禁用状态
		mLineBreakMode.setEnabled(false);
		mLinkBreakMode.setEnabled(false);
		
		for (int i = 0; i < mIpBtn.length; i++) {
			mIpBtn[i] = (Button) findView("config_" + i + "Btn");

			mIpBtn[i].setOnClickListener(mSubmitListener);
		}
		mConfirmBtn.setOnClickListener(mSubmitListener);
		mTutorialBtn.setOnClickListener(mSubmitListener);

		mNameSp.setAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, new String[] { "moas", "ebank", "emas", "helloapp" }));

		mChannelSp.setAdapter(new ArrayAdapter<Float>(this, 
				android.R.layout.simple_spinner_item, new Float[] { 1.4f, 1.3f, 1.2f, 1.1f, 1.0f }));

		mOfflineSp.setAdapter(new ArrayAdapter<Float>(this, 
				android.R.layout.simple_spinner_item, new Float[] {3f, 2.1f, 2f, 1f, 0f }));

		// SDK全局属性
		mLineBreakMode.setAdapter(new ArrayAdapter<BreakMode>(this, 
				android.R.layout.simple_spinner_item, BreakMode.values()));
		mClearMode.setAdapter(new ArrayAdapter<IMGMODE>(this, 
				android.R.layout.simple_spinner_item, IMGMODE.values()));
		mLinkBreakMode.setAdapter(new ArrayAdapter<BreakMode>(this, 
				android.R.layout.simple_spinner_item, BreakMode.values()));
	}

	private void initData() {
		mConfig = new ConfigInfo();
		sdkConfig = new SDKConfig();// SDK属性配置
		mControlPaddingConfig = new ControlPaddingConfig();//控件边距全局属性配置
		
		getSDKInfo();
		mIsUnitTestRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton rbtn = (RadioButton) group.findViewById(checkedId);
						String text = rbtn.getText().toString();
						if (!TextUtils.isEmpty(text) && text.equals("单元测试")) {
							mConfig.mIsUnitTest = true;
						} else {
							mConfig.mIsUnitTest = false;
						}
					}
				});

		mTrackTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mPerfTestTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mPushTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mSMTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mDevModeTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mErrorAlertTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mLogTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mCacheModeTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mEmpEditorCbx.setOnCheckedChangeListener(mCheckedChangeListener);

		mExplainBtn.setOnClickListener(mClickListedner);
		mAXmlBtn.setOnClickListener(mClickListedner);
		mClearDataBtn.setOnClickListener(mClickListedner);

		// SDK全局属性
		mSDKFullConfigTob.setOnCheckedChangeListener(mCheckedChangeListener);
		mPwdIsOrder.setOnCheckedChangeListener(mCheckedChangeListener);

		mChLabelBreakMode.setOnCheckedChangeListener(mCheckedChangeListener);
		mChLinkBreakMode.setOnCheckedChangeListener(mCheckedChangeListener);
		
		//控件边距全局属性设置
		mControlPaddingToggle.setOnCheckedChangeListener(mCheckedChangeListener);
	}

	private void alert(String msg) {
		if (msg == null) {
			msg = "";
		}

		new AlertDialog.Builder(this).setTitle("提示").setMessage(msg)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	/**
	 * 地址校验
	 */
	private boolean hostVerification(String host) {

		if (!TextUtils.isEmpty(host) && host.indexOf(":") != -1) {
			if (!host.startsWith("http")) {
				host = "http://" + host;
			}
			mConfig.mHost = host;
			mHostEdt.setText(mConfig.mHost);
			// 比较与上次是否登录同台服务器地址
			if (!TextUtils.isEmpty(mLastHost) && !mLastHost.equals(host)) {
				// 上次登录的服务地址与本次不同, 删除记录的信道证书。走完整信道流程。--测试使用
				String fileRoot = mActivity.getFilesDir().getPath().concat("/");
				FileManager.deleteFile(fileRoot.concat(ClientHello.CER_FILENAME));
				FileManager.deleteFile(fileRoot.concat(ClientHello.RNS2_FILENAME));
				FileManager.deleteFile(fileRoot.concat(host.substring(7)));
			}
			// 保存本次登录服务器地址
			saveSDKInfo();
			return true;
		} else {
			return false;
		}
	}

	private void toast(String msg) {
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
	}

	private View findView(String id) {
		return this.findViewById(Utils.getResourcesId(this, id, "id"));
	}

	/**
	 * 存储本机信息
	 */
	private void saveSDKInfo() {
		// 存储SDK相关信息
		AndroidPreferenceDB.ANDROIDDB.putString("shadowColor", mShadowColor.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("shadowOffset", mShadowOffset.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("pwdIsOrder", mPwdIsOrder.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("pwdSubstitute", mPwdSubstitute.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("loadingImge", mLoadingImg.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("loadFailed", mLoadFailed.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("btnDelayTime", mBtnDelayTime.getText().toString());

		AndroidPreferenceDB.ANDROIDDB.putString("LastServer", mConfig.mHost);
		
		//存储控件边距全局属性配置相关信息
		//div
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingDivLeft", mControlPaddingDivLeft.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingDivRight", mControlPaddingDivRight.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingDivUp", mControlPaddingDivUp.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingDivDown", mControlPaddingDivDown.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingDivChildCol", mControlPaddingDivChildCol.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingDivChildRow", mControlPaddingDivChildRow.getText().toString());
		//body
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingBodyLeft", mControlPaddingBodyLeft.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingBodyRight", mControlPaddingBodyRight.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingBodyUp", mControlPaddingBodyUp.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingBodyDown", mControlPaddingBodyDown.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingBodyChildCol", mControlPaddingBodyChildCol.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingBodyChildRow", mControlPaddingBodyChildRow.getText().toString());
		//form
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingFormLeft", mControlPaddingFormLeft.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingFormRight", mControlPaddingFormRight.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingFormUp", mControlPaddingFormUp.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingFormDown", mControlPaddingFormDown.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingFormChildCol", mControlPaddingFormChildCol.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingFormChildRow", mControlPaddingFormChildRow.getText().toString());
		//td
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingTdLeft", mControlPaddingTdLeft.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingTdRight", mControlPaddingTdRight.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingTdUp", mControlPaddingTdUp.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingTdDown", mControlPaddingTdDown.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingTdChildCol", mControlPaddingTdChildCol.getText().toString());
		AndroidPreferenceDB.ANDROIDDB.putString("controlPaddingTdChildRow", mControlPaddingTdChildRow.getText().toString());
	}

	/**
	 * 获取本机信息
	 */
	private void getSDKInfo() {
		String shadowColor = AndroidPreferenceDB.ANDROIDDB.getString("shadowColor");
		String shadowOffset = AndroidPreferenceDB.ANDROIDDB.getString("shadowOffset");
		String pwdIsOrder = AndroidPreferenceDB.ANDROIDDB.getString("pwdIsOrder");
		String pwdSubstitute = AndroidPreferenceDB.ANDROIDDB.getString("pwdSubstitute");
		String loadingImge = AndroidPreferenceDB.ANDROIDDB.getString("loadingImge");
		String loadFailed = AndroidPreferenceDB.ANDROIDDB.getString("loadFailed");
		String btnDelayTime = AndroidPreferenceDB.ANDROIDDB.getString("btnDelayTime");
		mLastHost = AndroidPreferenceDB.ANDROIDDB.getString("LastServer");
		mLastEditorHost = AndroidPreferenceDB.ANDROIDDB.getString("lastEditorServer");

		if (!TextUtils.isEmpty(shadowColor)) {
			mShadowColor.setText(shadowColor);
		}
		if (!TextUtils.isEmpty(shadowOffset)) {
			mShadowOffset.setText(shadowOffset);
		}
		if (!TextUtils.isEmpty(pwdIsOrder)) {
			mPwdIsOrder.setText(pwdIsOrder);
		}
		if (!TextUtils.isEmpty(pwdSubstitute)) {
			mPwdSubstitute.setText(pwdSubstitute);
		}
		if (!TextUtils.isEmpty(loadingImge)) {
			mLoadingImg.setText(loadingImge);
		}
		if (!TextUtils.isEmpty(loadFailed)) {
			mLoadFailed.setText(loadFailed);
		}
		if (!TextUtils.isEmpty(btnDelayTime)) {
			mBtnDelayTime.setText(btnDelayTime);
		}
		if (!TextUtils.isEmpty(mLastHost)) {
			mHostEdt.setText(mLastHost);
		}
		if (!TextUtils.isEmpty(mLastEditorHost)) {
			mEmpEditorHostEdt.setText(mLastEditorHost);
		}
		
		//获取控件边距全局属性相关配置
		//div
		String controlPaddingDivLeft = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingDivLeft");
		String controlPaddingDivRight = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingDivRight");
		String controlPaddingDivUp = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingDivUp");
		String controlPaddingDivDown = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingDivDown");
		String controlPaddingDivChildCol = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingDivChildCol");
		String controlPaddingDivChildRow = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingDivChildRow");
		if(!TextUtils.isEmpty(controlPaddingDivLeft)){
			mControlPaddingDivLeft.setText(controlPaddingDivLeft);
		}
		if(!TextUtils.isEmpty(controlPaddingDivRight)){
			mControlPaddingDivRight.setText(controlPaddingDivRight);
		}
		if(!TextUtils.isEmpty(controlPaddingDivUp)){
			mControlPaddingDivUp.setText(controlPaddingDivUp);
		}
		if(!TextUtils.isEmpty(controlPaddingDivDown)){
			mControlPaddingDivDown.setText(controlPaddingDivDown);
		}
		if(!TextUtils.isEmpty(controlPaddingDivChildCol)){
			mControlPaddingDivChildCol.setText(controlPaddingDivChildCol);
		}
		if(!TextUtils.isEmpty(controlPaddingDivChildRow)){
			mControlPaddingDivChildRow.setText(controlPaddingDivChildRow);
		}
		//body
		String controlPaddingBodyLeft = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingBodyLeft");
		String controlPaddingBodyRight = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingBodyRight");
		String controlPaddingBodyUp = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingBodyUp");
		String controlPaddingBodyDown = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingBodyDown");
		String controlPaddingBodyChildCol = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingBodyChildCol");
		String controlPaddingBodyChildRow = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingBodyChildRow");
		if(!TextUtils.isEmpty(controlPaddingBodyLeft)){
			mControlPaddingBodyLeft.setText(controlPaddingBodyLeft);
		}
		if(!TextUtils.isEmpty(controlPaddingBodyRight)){
			mControlPaddingBodyRight.setText(controlPaddingBodyRight);
		}
		if(!TextUtils.isEmpty(controlPaddingBodyUp)){
			mControlPaddingBodyUp.setText(controlPaddingBodyUp);
		}
		if(!TextUtils.isEmpty(controlPaddingBodyDown)){
			mControlPaddingBodyDown.setText(controlPaddingBodyDown);
		}
		if(!TextUtils.isEmpty(controlPaddingBodyChildCol)){
			mControlPaddingBodyChildCol.setText(controlPaddingBodyChildCol);
		}
		if(!TextUtils.isEmpty(controlPaddingBodyChildRow)){
			mControlPaddingBodyChildRow.setText(controlPaddingBodyChildRow);
		}
		//form
		String controlPaddingFormLeft = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingFormLeft");
		String controlPaddingFormRight = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingFormRight");
		String controlPaddingFormUp = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingFormUp");
		String controlPaddingFormDown = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingFormDown");
		String controlPaddingFormChildCol = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingFormChildCol");
		String controlPaddingFormChildRow = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingFormChildRow");
		if(!TextUtils.isEmpty(controlPaddingFormLeft)){
			mControlPaddingFormLeft.setText(controlPaddingFormLeft);
		}
		if(!TextUtils.isEmpty(controlPaddingFormRight)){
			mControlPaddingFormRight.setText(controlPaddingFormRight);
		}
		if(!TextUtils.isEmpty(controlPaddingFormUp)){
			mControlPaddingFormUp.setText(controlPaddingFormUp);
		}
		if(!TextUtils.isEmpty(controlPaddingFormDown)){
			mControlPaddingFormDown.setText(controlPaddingFormDown);
		}
		if(!TextUtils.isEmpty(controlPaddingFormChildCol)){
			mControlPaddingFormChildCol.setText(controlPaddingFormChildCol);
		}
		if(!TextUtils.isEmpty(controlPaddingFormChildRow)){
			mControlPaddingFormChildRow.setText(controlPaddingFormChildRow);
		}
		//td
		String controlPaddingTdLeft = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingTdLeft");
		String controlPaddingTdRight = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingTdRight");
		String controlPaddingTdUp = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingTdUp");
		String controlPaddingTdDown = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingTdDown");
		String controlPaddingTdChildCol = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingTdChildCol");
		String controlPaddingTdChildRow = AndroidPreferenceDB.ANDROIDDB.getString("controlPaddingTdChildRow");
		if(!TextUtils.isEmpty(controlPaddingTdLeft)){
			mControlPaddingTdLeft.setText(controlPaddingTdLeft);
		}
		if(!TextUtils.isEmpty(controlPaddingTdRight)){
			mControlPaddingTdRight.setText(controlPaddingTdRight);
		}
		if(!TextUtils.isEmpty(controlPaddingTdUp)){
			mControlPaddingTdUp.setText(controlPaddingTdUp);
		}
		if(!TextUtils.isEmpty(controlPaddingTdDown)){
			mControlPaddingTdDown.setText(controlPaddingTdDown);
		}
		if(!TextUtils.isEmpty(controlPaddingTdChildCol)){
			mControlPaddingTdChildCol.setText(controlPaddingTdChildCol);
		}
		if(!TextUtils.isEmpty(controlPaddingTdChildRow)){
			mControlPaddingTdChildRow.setText(controlPaddingTdChildRow);
		}
	}
	
	/**
	 * 清除应用数据  -- 应用目录下除了lib文件夹
	 * 包含 cache \ database \ shared_prefs \ files等
	 */
	private void clearData() {
		String appPath = getFilesDir().getAbsolutePath();
		appPath = appPath.substring(0, appPath.lastIndexOf(File.separator) + 1); // 获取应用程序目录
		
		delete(appPath);
		toast("应用数据已清除!");
		
		// 清除数据后重启下Activity - 重置当前界面内容
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	
	private boolean delete(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}
		if (file.isFile()) {
			// 删除SharedPreferences
			if (file.getParent().endsWith(File.separator + "shared_prefs")) {
				// 去掉.xml后缀
				String name = file.getName();
				name = name.substring(0, name.length() - 4);
				SharedPreferences sf = mActivity.getSharedPreferences(name, Context.MODE_PRIVATE);
				sf.edit().clear().commit();
			}
			return file.delete();
		} else if (file.isDirectory() && !file.getName().equals("lib")) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (File tempF : files) {
					delete(tempF.getPath());
				}
			}
			// 删除file 本身
			return file.delete();
		}
		return false;
	}
	
    @Override 
    public void onBackPressed() {  
    	AndroidPreferenceDB.ANDROIDDB = null;
        super.onBackPressed();  
    }
}
