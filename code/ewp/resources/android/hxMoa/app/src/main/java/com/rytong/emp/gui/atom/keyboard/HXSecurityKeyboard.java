package com.rytong.emp.gui.atom.keyboard;

import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.ghbank.moas.R;
import com.rytong.emp.dom.Screen;
import com.rytong.emp.gui.atom.atomrela.HXButtonStatesDrawable;
import com.rytong.emp.tool.Utils;

/**
 * <p>
 * 密码键盘实现类<br>
 * 该类不对外开放，所有接口通过 {@link SecurityKeyboardHelper}开放
 * </p>
 */
@SuppressLint("DefaultLocale")
public class HXSecurityKeyboard extends PopupWindow implements OnClickListener, OnTouchListener {

	private static HXSecurityKeyboard mInstance = null;

	private Context mContext = null;

	/** 密码键盘种类 */
	public enum KEYBOARD_TYPE {
		/** 数字键盘 */
		NUMBER,
		/** 大写字符键盘 */
		CAPITAL_STRING,
		/** 小写字符键盘 */
		LITTLE_STRING,
		/** 符号键盘 */
		SYMBOL;
	};

	/** 默认的键盘整体布局宽度 */
	private final int TOTAL_WIDTH = Screen.mWidth;
	/** 默认的键盘整体布局高度 */
	private final int TOTAL_HEIGHT = Screen.mHeight / 3;
	// 数字集
	private final int[] NUMS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	// 字母集
	private final int[] CHARS = new int[] { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };
	
	private final int[] SYMBOL = new int[] { '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '{', '}', '[', ']', '|', '\\', ':', ';', '"', '\'', '<', ',', '>', '.', '?', '/', '`' };

	/** 记录view滚动的距离 */
	private int mScrollDistance = 0;
	/** 现在是何种键盘。默认是小写字符键盘 */
	private KEYBOARD_TYPE mKeyboardType = KEYBOARD_TYPE.LITTLE_STRING;
	/** 调用此密码键盘的密码输入框 */
	private HXEMPSecurityView mSecurityView = null;
	/** 是否乱序 */
	private boolean mIsOrder = false;
	/** 键盘显示的数字 */
	private int[] mNUMS = new int[NUMS.length];
	/** 键盘显示的字母 */
	private int[] mCHARS = new int[CHARS.length];
	/** 键盘显示的符号 */
	private int[] mSYMBOL = new int[SYMBOL.length];
	/** 按键被按下的监听 */
	private OnKeyDownListener mOnKeyDownListener = null;
	/** 键盘区 */
	private RelativeLayout mKeyboardPanel = null;

	// 以下是一些按钮tag的常量。
	private final String TYPE_CHOOSE_BUTTON_ABC = "typeChooseButton";
	private final String TYPE_CHOOSE_BUTTON_123 = "typeChooseButton2";
	private final String TYPE_CHOOSE_BUTTON_SYMBOL = "typeChooseButton3";
	public final String HIDE_BUTTON = "hideButton";
	private final String NUMBER_KEYBOARD_NUMBER = "numberKeyboardNumber";
	private final String STR_KEYBOARD_STR = "strKeyboardStr";
	private final String NUMBER_KEYBOARD_BACK = "numberKeyboardBack";
	private final String STR_KEYBOARD_DELETE = "strKeyboardDelete";
	private final String STR_KEYBOARD_CHANGE = "strKeyboardChange";

	public static HXSecurityKeyboard getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new HXSecurityKeyboard(context);
		}
		return mInstance;
	}

	private HXSecurityKeyboard(Context context) {
		super(context);
		this.mContext = context;
		init(context);
	}

	/**
	 * <p>
	 * 初始化方法。
	 * </p>
	 * 
	 * @param context 应用程序上下文对象。
	 */
	private void init(Context context) {

		// 弹出的popupwindow的基本属性设置。
		setBackgroundDrawable(new ColorDrawable());
		// setOutsideTouchable(true);
		setFocusable(false);

		// 创建背景布局。
		LinearLayout backLayout = createLayout(context);
		// 为popupwindow设置内在的布局。
		setContentView(backLayout);
		setWidth(TOTAL_WIDTH);
		setHeight(TOTAL_HEIGHT);
		// 指定像输入法那样弹出和收回。
		setAnimationStyle(android.R.style.Animation_InputMethod);
		// 初始化键盘。
		initKeyboard(context);
	}

	/**
	 * 关闭密码键盘
	 */
	public void closeSecurityKeyboard() {
		if (mInstance != null && mInstance.isShowing()) {
			mInstance.dismiss();
		}
	}

	/**
	 * 设置键盘是否乱序
	 * 
	 * @param mIsOrder
	 */
	public void setIsOrder(boolean mIsOrder) {
		this.mIsOrder = mIsOrder;
		initKeyboard(mContext);
	}

	/**
	 * 设置键盘类型
	 * 
	 * @param keyboardType
	 */
	public void setKeyboardType(KEYBOARD_TYPE keyboardType) {
		mKeyboardType = keyboardType;
		initKeyboard(mContext);
	}

	/**
	 * 设置和键盘绑定的输入框
	 * 
	 * @param mPasswordTextView
	 */
	public void setPasswordTextView(HXEMPSecurityView view) {
		mSecurityView = view;
	}

	/**
	 * <p>
	 * 创建布局。
	 * </p>
	 * 
	 * @param context 应用程序上下文对象。
	 * @return dialog的背景布局。
	 */
	private LinearLayout createLayout(Context context) {
		LinearLayout backLayout = new LinearLayout(context);
		backLayout.setGravity(Gravity.CENTER);
		backLayout.setLayoutParams(new LayoutParams(TOTAL_WIDTH, TOTAL_HEIGHT));
		backLayout.setOrientation(LinearLayout.VERTICAL);

		// 添加控制横条。
		RelativeLayout controlPanel = new RelativeLayout(context);
		controlPanel.setGravity(Gravity.CENTER);

		// 设置按钮横条的背景。
		Drawable bottomDrawable = new GradientDrawable(Orientation.BOTTOM_TOP, new int[] { 0xff000000, 0xffdddddd });
		Drawable surfaceDrawable = new GradientDrawable(Orientation.BOTTOM_TOP, new int[] { 0xff7e9de2, 0xff96b7f8 });
		LayerDrawable bgDrawable = new LayerDrawable(new Drawable[] { bottomDrawable, surfaceDrawable });
		bgDrawable.setLayerInset(1, 2, 2, 2, 2);
		controlPanel.setBackgroundDrawable(bgDrawable);

		// 隐藏dialog按钮。
		RelativeLayout.LayoutParams hidebuttonLp = new RelativeLayout.LayoutParams(100, (TOTAL_HEIGHT >> 2) - 4);
		hidebuttonLp.alignWithParent = true;
		hidebuttonLp.setMargins(TOTAL_WIDTH-100, 2, 2, 2);
		Button hideButton = new Button(context);
		hideButton.setGravity(Gravity.CENTER);
		hideButton.setPadding(0, 0, 0, 0);
		hideButton.setLayoutParams(hidebuttonLp);
		hideButton.setText("确定");
		hideButton.setTextColor(0xff000000);
		hideButton.setBackgroundColor(Color.TRANSPARENT);
		hideButton.setTag(HIDE_BUTTON);
		hideButton.setOnClickListener(this);
		
		// 隐藏dialog按钮。
		RelativeLayout.LayoutParams hidebuttonbarLp = new RelativeLayout.LayoutParams(TOTAL_WIDTH, (TOTAL_HEIGHT >> 2) - 4);
		hidebuttonbarLp.alignWithParent = true;
		Button hideButtonBar = new Button(context);
		hideButtonBar.setGravity(Gravity.CENTER);
		hideButtonBar.setPadding(0, 0, 0, 0);
		hideButtonBar.setLayoutParams(hidebuttonbarLp);
		hideButtonBar.setBackgroundDrawable(new HXButtonStatesDrawable(new int[] { 0xffe1e0e3, 0xffebe6ea, 0xfffbfafc }, new int[] { 0xffe1e0e3, 0xffebe6ea, 0xfffbfafc }, new int[] { 0xffe1e0e3,
				0xffebe6ea, 0xfffbfafc }, new int[] { 0xff90b2b5, 0xffb8d2df, 0xffdee3f3 }));

		controlPanel.addView(hideButtonBar);
		controlPanel.addView(hideButton);

		// 添加键盘部分。
		mKeyboardPanel = new RelativeLayout(context);
		mKeyboardPanel.setGravity(Gravity.CENTER);
		mKeyboardPanel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mKeyboardPanel.setBackgroundColor(0xff7e7e7e);

		// 添加进整体视图。
		backLayout.addView(controlPanel);
		backLayout.addView(mKeyboardPanel);

		return backLayout;
	}

	/**
	 * <p>
	 * 初始化密码键盘部分。此方法在第一次弹出密码键盘的时候进行首次初始化。
	 * </p>
	 * 
	 * @param context 应用程序上下文对象。
	 */
	private void initKeyboard(Context context) {
		System.arraycopy(NUMS, 0, mNUMS, 0, NUMS.length);
		System.arraycopy(CHARS, 0, mCHARS, 0, CHARS.length);
		System.arraycopy(SYMBOL, 0, mSYMBOL, 0, SYMBOL.length);
		if (false){
			// 初始化的时候先进行一次按钮随机的排列。
			mNUMS = randomArray(mNUMS);
			mCHARS = randomArray(mCHARS);
			mSYMBOL = randomArray(mSYMBOL);
		}
		// 分别初始化纯数字的密码键盘和字符密码键盘。
		mKeyboardPanel.removeAllViews();
		if (KEYBOARD_TYPE.NUMBER.equals(mKeyboardType)) { // 初始化数字键盘。
			initNumKeyboard(context);
		} else if (KEYBOARD_TYPE.CAPITAL_STRING.equals(mKeyboardType)) { // 初始化大写字符键盘。
			initStrKeyboard(context, true);
		} else if (KEYBOARD_TYPE.LITTLE_STRING.equals(mKeyboardType)) { // 初始化小写字符键盘。
			initStrKeyboard(context, false);
		} else if (KEYBOARD_TYPE.SYMBOL.equals(mKeyboardType)) { // 初始化符号键盘。
			initSymbolKeyboard(context);
		}
	}

	/**
	 * <p>
	 * 初始化数字键盘。三行四列。
	 * </p>
	 * 
	 * @param context 应用程序上下文对象。
	 */
	private void initNumKeyboard(Context context) {
		int numRow = 4; // 三行。
		int numCol = 3; // 四列。
		int buttonWidth = TOTAL_WIDTH / numCol;
		int buttonHeight = (TOTAL_HEIGHT - (TOTAL_HEIGHT >> 2)) / numRow;
		for (int i = 0; i < numRow; i++) {
			int rowTopMargin = i * buttonHeight; // 每行与上一行的间距。
			for (int j = 0; j < numCol; j++) {
				int leftMargin = j * buttonWidth; // 按钮与左边按钮的间距。
				HXSecurityButton button = null;
				
				if(i==numRow-1) {
					if(j==0) {
						button = new HXSecurityButton(context, "", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
					} else if(j==numCol-1) {
						button = new HXSecurityButton(context, "", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
						button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_delete_bg));
						button.setTag(STR_KEYBOARD_DELETE);
						button.setOnClickListener(this);
						mKeyboardPanel.addView(button);
						
						int w = 40;
						int h = 30;
						HXSecurityButton _button = new HXSecurityButton(context, "", w, h, leftMargin+(buttonWidth-w)/2, rowTopMargin+(buttonHeight-h)/2);
						_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_delete));
						_button.setTag(STR_KEYBOARD_DELETE);
						_button.setOnClickListener(this);
						mKeyboardPanel.addView(_button);
					} else {
						button = new HXSecurityButton(context, "0", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
						button.setTag(NUMBER_KEYBOARD_NUMBER);
						button.setOnClickListener(this);
						mKeyboardPanel.addView(button);
					}
				} else {
					button = new HXSecurityButton(context, (i*numCol+j+1)+"", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
					button.setTag(NUMBER_KEYBOARD_NUMBER);
					button.setOnClickListener(this);
					mKeyboardPanel.addView(button);
				}
			}
		}
	}

	/**
	 * <p>
	 * 初始化字符键盘。三行十列。
	 * </p>
	 * 
	 * @param context 应用程序上下文对象。
	 * @param isCapital 是否为大写键盘。
	 */
	private void initStrKeyboard(final Context context, boolean isCapital) {
		int numRow = 3; // 三行。
		int numCol = 10; // 十列。
		int buttonWidth = TOTAL_WIDTH / numCol;
		int buttonHeight = (TOTAL_HEIGHT - (TOTAL_HEIGHT >> 2)) / numRow;
		int buttonSpe = 4;
		int buttonRowSpe = 5;
		
		int index = 0;
		int colIndex = 0;
		
		//第一行 10列 布局开始
		numCol = 10;
		for (int i = 0; i < numCol; i++) {
			buttonWidth = (TOTAL_WIDTH- ((numCol+1) * buttonSpe)) / numCol;
			int leftMargin = i * buttonWidth + i * buttonSpe; // 按钮与左边按钮的间距。
			int rowTopMargin = colIndex * buttonHeight; // 每行与上一行的间距。
			
			String text = String.valueOf((char)mCHARS[index]);
			if(isCapital) {
				text =  text.toUpperCase();
			}
			HXSecurityButton button = new HXSecurityButton(context, text, buttonWidth, buttonHeight, leftMargin, rowTopMargin);
			button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_unselected));
			button.setTag(STR_KEYBOARD_STR);
			button.setOnClickListener(this);
			button.setOnTouchListener(this);
			
			mKeyboardPanel.addView(button);
			index++;
		}
		colIndex++;
		//第一行 10列 布局结束
		
		//第二行 10列 布局开始
		numCol = 10;
		for (int i = 0; i < numCol; i++) {
			buttonWidth = (TOTAL_WIDTH- ((numCol+1) * buttonSpe)) / numCol;
			int leftMargin = i * buttonWidth + i * buttonSpe; // 按钮与左边按钮的间距。
			int rowTopMargin = colIndex * buttonHeight + buttonRowSpe; // 每行与上一行的间距。
			
			HXSecurityButton button = null;
			
			if(i==0) {
				button = new HXSecurityButton(context, "", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_selected));
				button.setTag(STR_KEYBOARD_CHANGE);
				button.setOnClickListener(this);
				mKeyboardPanel.addView(button);
				
				int w = 30;
				int h = 30;
				HXSecurityButton _button = new HXSecurityButton(context, "", w, h, leftMargin+(buttonWidth-w)/2, rowTopMargin+(buttonHeight-h)/2);
				_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_shift));
				if(isCapital) {
					_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_shift_selected));
				}
				_button.setTag(STR_KEYBOARD_CHANGE);
				_button.setOnClickListener(this);
				mKeyboardPanel.addView(_button);
			} else {
				String text = String.valueOf((char)mCHARS[index]);
				if(isCapital) {
					text =  text.toUpperCase();
				}
				button = new HXSecurityButton(context, text, buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_unselected));
				button.setTag(STR_KEYBOARD_STR);
				button.setOnClickListener(this);
				button.setOnTouchListener(this);
				
				index++;
				
				mKeyboardPanel.addView(button);
			}
			
			
		}
		colIndex++;
		//第二行 10列 布局结束
		
		//第三行 列 布局开始
		numCol = 9;
		int leftMargin = 0;
		for (int i = 0; i < numCol; i++) {
			buttonWidth = (TOTAL_WIDTH- ((numCol+1) * buttonSpe)) / numCol;
			int rowTopMargin = colIndex * buttonHeight + colIndex * buttonRowSpe; // 每行与上一行的间距。
			
			HXSecurityButton button = null;
			
			if(i==0) {
				buttonWidth = (TOTAL_WIDTH- ((numCol+2) * buttonSpe)) / (numCol + 1);
				buttonWidth = buttonWidth*2-buttonWidth/2+buttonSpe;
				
				button = new HXSecurityButton(context, "123", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_delete_bg));
				button.setTag(TYPE_CHOOSE_BUTTON_123);
				button.setOnClickListener(this);
				
				leftMargin += buttonWidth;
				 
				mKeyboardPanel.addView(button);
			} else if (i==8) {
				leftMargin += buttonSpe; // 按钮与左边按钮的间距。
				
				buttonWidth = (TOTAL_WIDTH- ((numCol+2) * buttonSpe)) / (numCol + 1);
				buttonWidth = buttonWidth*2-buttonWidth/2;
				
				button = new HXSecurityButton(context, "", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_delete_bg));
				button.setTag(STR_KEYBOARD_DELETE);
				button.setOnClickListener(this);
				mKeyboardPanel.addView(button);
				
				int w = 40;
				int h = 30;
				HXSecurityButton _button = new HXSecurityButton(context, "", w, h, leftMargin+(buttonWidth-w)/2, rowTopMargin+(buttonHeight-h)/2);
				_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_delete));
				_button.setTag(STR_KEYBOARD_DELETE);
				_button.setOnClickListener(this);
				mKeyboardPanel.addView(_button);
				
				leftMargin += buttonWidth;
			}else {
				leftMargin += buttonSpe; // 按钮与左边按钮的间距。
				
				buttonWidth = (TOTAL_WIDTH- ((numCol+2) * buttonSpe)) / (numCol + 1);
				
				String text = String.valueOf((char)mCHARS[index]);
				if(isCapital) {
					text =  text.toUpperCase();
				}
				button = new HXSecurityButton(context, text, buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_unselected));
				button.setTag(STR_KEYBOARD_STR);
				button.setOnClickListener(this);
				button.setOnTouchListener(this);
				
				mKeyboardPanel.addView(button);
				
				leftMargin += buttonWidth;
				
				index++;
			}
		}
		colIndex++;
		//第三行 9列 布局结束
	}
	
	private void initSymbolKeyboard(Context context) {
		int numRow = 3; // 三行。
		int numCol = 10; // 四列。
		int buttonWidth = TOTAL_WIDTH / numCol;
		int buttonHeight = (TOTAL_HEIGHT - (TOTAL_HEIGHT >> 2)) / numRow;
		int buttonSpe = 4;
		int buttonRowSpe = 5;
		
		int index = 0;
		int colIndex = 0;
		
		if(mIsSymbol) {
			index = mSYMBOL.length/2;
		}
		
		//第一行 10列 布局开始
		numCol = 10;
		for (int i = 0; i < numCol; i++) {		//顶部数字
			buttonWidth = (TOTAL_WIDTH- ((numCol+1) * buttonSpe)) / numCol;
			int leftMargin = i * buttonWidth + i * buttonSpe; // 按钮与左边按钮的间距。
			int rowTopMargin = colIndex * buttonHeight; // 每行与上一行的间距。
			
			String text = (i+1)+"";
			if(i==numCol-1) {
				text = 0+"";
			}
			HXSecurityButton button = new HXSecurityButton(context, text, buttonWidth, buttonHeight, leftMargin, rowTopMargin);
			button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_unselected));
			button.setTag(STR_KEYBOARD_STR);
			button.setOnClickListener(this);
			button.setOnTouchListener(this);
			
			mKeyboardPanel.addView(button);
		}
		colIndex++;
		//第一行 10列 布局结束
		
		//第二行 10列 布局开始
		numCol = 10;
		for (int i = 0; i < numCol; i++) {
			buttonWidth = (TOTAL_WIDTH- ((numCol+1) * buttonSpe)) / numCol;
			int leftMargin = i * buttonWidth + i * buttonSpe; // 按钮与左边按钮的间距。
			int rowTopMargin = colIndex * buttonHeight + buttonRowSpe; // 每行与上一行的间距。
			
			HXSecurityButton button = null;
			
			if(i==0) {
				button = new HXSecurityButton(context, "#*!", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_selected));
				button.setTag(TYPE_CHOOSE_BUTTON_SYMBOL);
				button.setOnClickListener(this);
				mKeyboardPanel.addView(button);
			} else {
				String text = String.valueOf((char)mSYMBOL[index]);
				button = new HXSecurityButton(context, text, buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_unselected));
				button.setTag(STR_KEYBOARD_STR);
				button.setOnClickListener(this);
				button.setOnTouchListener(this);
				
				index++;
				
				mKeyboardPanel.addView(button);
			}
			
			
		}
		colIndex++;
		//第二行 10列 布局结束
		
		//第三行 列 布局开始
		numCol = 9;
		int leftMargin = 0;
		for (int i = 0; i < numCol; i++) {
			buttonWidth = (TOTAL_WIDTH- ((numCol+1) * buttonSpe)) / numCol;
			int rowTopMargin = colIndex * buttonHeight + colIndex * buttonRowSpe; // 每行与上一行的间距。
			
			HXSecurityButton button = null;
			
			if(i==0) {
				buttonWidth = (TOTAL_WIDTH- ((numCol+2) * buttonSpe)) / (numCol + 1);
				buttonWidth = buttonWidth*2-buttonWidth/2+buttonSpe;
				
				button = new HXSecurityButton(context, "ABC", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_delete_bg));
				button.setTag(TYPE_CHOOSE_BUTTON_ABC);
				button.setOnClickListener(this);
				
				leftMargin += buttonWidth;
				
				mKeyboardPanel.addView(button);
			} else if (i==8) {
				leftMargin += buttonSpe; // 按钮与左边按钮的间距。
				
				buttonWidth = (TOTAL_WIDTH- ((numCol+2) * buttonSpe)) / (numCol + 1);
				buttonWidth = buttonWidth*2-buttonWidth/2;
				
				button = new HXSecurityButton(context, "", buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_delete_bg));
				button.setTag(STR_KEYBOARD_DELETE);
				button.setOnClickListener(this);
				mKeyboardPanel.addView(button);
				
				int w = 40;
				int h = 30;
				HXSecurityButton _button = new HXSecurityButton(context, "", w, h, leftMargin+(buttonWidth-w)/2, rowTopMargin+(buttonHeight-h)/2);
				_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_delete));
				_button.setTag(STR_KEYBOARD_DELETE);
				_button.setOnClickListener(this);
				mKeyboardPanel.addView(_button);
				
				leftMargin += buttonWidth;
			}else {
				leftMargin += buttonSpe; // 按钮与左边按钮的间距。
				
				buttonWidth = (TOTAL_WIDTH- ((numCol+2) * buttonSpe)) / (numCol + 1);
				
				String text = String.valueOf((char)mSYMBOL[index]);
				button = new HXSecurityButton(context, text, buttonWidth, buttonHeight, leftMargin, rowTopMargin);
				button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_unselected));
				button.setTag(STR_KEYBOARD_STR);
				button.setOnClickListener(this);
				button.setOnTouchListener(this);
				
				mKeyboardPanel.addView(button);
				
				leftMargin += buttonWidth;
				
				index++;
			}
		}
		colIndex++;
		//第三行 9列 布局结束
	}
	
	/**
	 * <p>
	 * 将一个数组中的元素进行顺序的打乱，并再次返回这个数组。
	 * </p>
	 * 
	 * @param source 待打乱顺序的数组。
	 * @return 元素顺序已被打乱的数组。
	 */
	private int[] randomArray(int[] source) {
		Random random = new Random();
		int temp = 0; // 用来存储待交换位置的对象。
		for (int i = 0; i < source.length; i++) {
			int rnd = Math.abs(random.nextInt(source.length - i));
			// 交换随机下标元素与(source.length - i - 1)位置元素的值。
			temp = source[rnd];
			source[rnd] = source[source.length - i - 1];
			source[source.length - i - 1] = temp;
		}
		return source;
	}

	/**
	 * <p>
	 * 密码输入框的文本加入一个新的字符。
	 * </p>
	 * 
	 * @param newChar 新的字符。
	 */
	private void insertNewChar(String newChar) {
		if (mSecurityView != null) {
			mSecurityView.addOneInfo(newChar);
		}
	}

	/**
	 * <p>
	 * 密码输入框的文本回退一个字符。
	 * </p>
	 */
	private void backspace() {
		if (mSecurityView != null) {
			mSecurityView.deleteOneInfo();
		}
	}

	/**
	 * <p>
	 * 检查类型转换锁。<br>
	 * 如果类型转换锁被设置为true，则不允许进行键盘的类型转换，反之允许。
	 * </p>
	 */
	public void setTypeChangeLock(boolean typeLock) {
	}

	private boolean mIsSymbol;
	@Override
	public void onClick(View v) {
		try {
			if (TYPE_CHOOSE_BUTTON_ABC.equals(v.getTag())) {
				mKeyboardType = KEYBOARD_TYPE.LITTLE_STRING;
				setKeyboardType(mKeyboardType);
			} else if (TYPE_CHOOSE_BUTTON_123.equals(v.getTag())) {
				mKeyboardType = KEYBOARD_TYPE.SYMBOL;
				setKeyboardType(mKeyboardType);
			} else if (TYPE_CHOOSE_BUTTON_SYMBOL.equals(v.getTag())) {
				if(mIsSymbol) {
					mIsSymbol = false;
				} else {
					mIsSymbol = true;
				}
				setKeyboardType(mKeyboardType);
			} else if (HIDE_BUTTON.equals(v.getTag())) {
				if (isShowing()) {
					mInstance.dismiss();
				}
			} else if (NUMBER_KEYBOARD_NUMBER.equals(v.getTag()) || STR_KEYBOARD_STR.equals(v.getTag())) {
				insertNewChar(((HXSecurityButton) v).getInfo());
			} else if (NUMBER_KEYBOARD_BACK.equals(v.getTag()) || STR_KEYBOARD_DELETE.equals(v.getTag())) {
				backspace();
			} else if (STR_KEYBOARD_CHANGE.equals(v.getTag())) {
				if (KEYBOARD_TYPE.CAPITAL_STRING.equals(mKeyboardType)) {
					mKeyboardType = KEYBOARD_TYPE.LITTLE_STRING;
				} else if (KEYBOARD_TYPE.LITTLE_STRING.equals(mKeyboardType)) {
					mKeyboardType = KEYBOARD_TYPE.CAPITAL_STRING;
				}
				initKeyboard(mContext);
			}
			if (mOnKeyDownListener != null) {
				mOnKeyDownListener.onSecurityKeyDown(v.getTag().toString());
			}
		} catch (Exception e) {
			Utils.printException(e);
		}
	}

	/**
	 * 获取view滚动的距离
	 * 
	 * @return
	 */
	public int getScrollDistance() {
		return mScrollDistance;
	}

	/**
	 * 设置view滚动的距离
	 * 
	 * @return
	 */
	public void setScrollDistance(int scrollDistance) {
		mScrollDistance = scrollDistance;
	}

	/**
	 * 设置键盘被按下的监听
	 */
	public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener) {
		mOnKeyDownListener = onKeyDownListener;
	}

	public interface OnKeyDownListener {
		public void onSecurityKeyDown(String tag);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if (v instanceof HXSecurityButton && STR_KEYBOARD_STR.equals(v.getTag())) {
				((HXSecurityButton)v).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_selected));
				
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (v instanceof HXSecurityButton && STR_KEYBOARD_STR.equals(v.getTag())) {
				((HXSecurityButton)v).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.keyboard_btn_unselected));
			}
		}
		
		return false;
	}

}
