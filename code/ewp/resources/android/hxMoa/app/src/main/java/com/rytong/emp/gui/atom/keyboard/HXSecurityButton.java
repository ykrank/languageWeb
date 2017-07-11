package com.rytong.emp.gui.atom.keyboard;

import com.rytong.emp.gui.atom.atomrela.HXButtonStatesDrawable;
import com.rytong.emp.security.Base64;
import com.rytong.emp.tool.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * <p>密码键盘按钮。</p>
 */
public class HXSecurityButton extends Button {

	/**
	 * <p>按钮存储的信息。</p>
	 */
	private String info = null;
	/**
	 * <p>画笔。</p>
	 */
	private Paint mPaint = null;
	/**
	 * <p>文字的高度。</p>
	 */
	private int mTextHeight = 0;
	// 功能按钮常量。
    private final String NUMBER_KEYBOARD_BACK = "numberKeyboardBack";
    private final String STR_KEYBOARD_DELETE = "strKeyboardDelete";
    private final String STR_KEYBOARD_CHANGE = "strKeyboardChange";
	
	/**
	 * <p>获得按钮存储的信息，只返回有意义的信息，如OK或者回退键之类的按钮将返回空字符串。
	 * <strong>注意：将会得到按钮上存储信息的密文。</strong></p>
	 * @return 按钮存储的信息，已经转化成了密文。
	 */
	public String getInfo() {
		return info != null && info.length() == 1 ? getEncryptedInfo(info) : "";
	}

	/**
	 * <p>构造方法。按钮在密码键盘中采用RelativeLayout的布局参数。</p>
	 * @param context 应用程序上下文对象。
	 * @param surfaceInfo 在按钮上显示的文字。
	 * @param width 按钮的宽度。
	 * @param height 按钮的高度。
	 * @param leftMargin 按钮在父布局中的左边距。
	 * @param topMargin 按钮在父布局中的上边距。
	 */
	public HXSecurityButton(Context context, String surfaceInfo, int width, int height, int leftMargin, int topMargin) {
		super(context);
		this.info = surfaceInfo;
		// 设置画笔。
		mPaint = getPaint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xff000000);
		mTextHeight = getTextHeight(mPaint);
		// 设置布局。
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
		lp.alignWithParent = true;
		lp.leftMargin = leftMargin;
		lp.topMargin = topMargin;
		setLayoutParams(lp);
		// 设置背景图片。
		setBackgroundDrawable(new HXButtonStatesDrawable(
				new int[] {0xffe1e0e3, 0xffebe6ea, 0xfffbfafc}, 
				new int[] {0xffe1e0e3, 0xffebe6ea, 0xfffbfafc}, 
				new int[] {0xffe1e0e3, 0xffebe6ea, 0xfffbfafc}, 
				new int[] {0xff90b2b5, 0xffb8d2df, 0xffdee3f3}));
	}

	@Override
	public CharSequence getText() {
		return "";
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(info != null && mPaint != null){
			// 4.0系统下不知为何paint的值会变，此处重新设置颜色。
			mPaint.setColor(0xff000000);
			if(NUMBER_KEYBOARD_BACK.equals(info) || STR_KEYBOARD_DELETE.equals(info)){ // 向左的箭头。
				drawLeftArrow(canvas);
			} else if(STR_KEYBOARD_CHANGE.equals(info)){ // 向上的箭头。
				drawUpArrow(canvas);
			} else {
				int x = (int) ((getWidth() - mPaint.measureText(info)) / 2);
				int y = (getHeight() >> 1) + (mTextHeight >> 2);
				canvas.drawText(info, x, y, mPaint);
			}
		}
		super.onDraw(canvas);
	}
	
	/**
	 * <p>获得文字的高度。</p>
	 * @param paint 画笔。
	 * @return 文字的高度。
	 */
	private int getTextHeight(Paint paint){
		FontMetrics fm = paint.getFontMetrics();
		return (int) (Math.ceil(fm.descent - fm.ascent)); // 计算文字的高度。
	}
	
	/**
	 * <p>绘制向左的箭头。</p>
	 * @param canvas 画布。
	 */
	private void drawLeftArrow(Canvas canvas){
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(Utils.defaultToScreen(1));
		int padding = Math.min(getWidth(), getHeight()) >> 2;
		int lineWidth = getWidth() - (padding << 1); // 线条长度。
		int arrowHeight = Math.max(Utils.defaultToScreen(3), lineWidth >> 3);
		
		canvas.drawLine(padding, getHeight() >> 1, getWidth() - padding, getHeight() >> 1, mPaint);
		canvas.drawLine(padding, getHeight() >> 1, padding + arrowHeight, (getHeight() >> 1) - arrowHeight, mPaint);
		canvas.drawLine(padding, getHeight() >> 1, padding + arrowHeight, (getHeight() >> 1) + arrowHeight, mPaint);
	}
	
	/**
	 * <p>绘制向上的箭头。</p>
	 * @param canvas 画布。
	 */
	private void drawUpArrow(Canvas canvas){
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(Utils.defaultToScreen(1));
		int padding = Math.min(getWidth(), getHeight()) >> 2;
		int lineHeight = getHeight() - (padding << 1); // 线条高度。
		int arrowWidth = Math.max(Utils.defaultToScreen(3), lineHeight >> 3);
		
		canvas.drawLine(getWidth() >> 1, padding, getWidth() >> 1, getHeight() - padding, mPaint);
		canvas.drawLine(getWidth() >> 1, padding, (getWidth() >> 1) - arrowWidth, padding + arrowWidth, mPaint);
		canvas.drawLine(getWidth() >> 1, padding, (getWidth() >> 1) + arrowWidth, padding + arrowWidth, mPaint);
	}
	
	/**
	 * <p>
	 * 对按钮上的信息进行加密。
	 * </p>
	 * 
	 * @param sourceInfo 原始的待加密的字符串
	 * 
	 * @return 加密之后的密文。
	 */
	public static String getEncryptedInfo(String sourceInfo){
		byte[] sourceBytes = sourceInfo.getBytes();
		int offset = 5; // 左移五位。
		for (int i = 0; i < sourceBytes.length; i++) {
			sourceBytes[i] = getLeftMovedByteArray(sourceBytes[i], offset); // 进行左移移位加密。
		}
		String desInfo = Base64.encode(sourceBytes); // 进行Base64加密。
		return desInfo;
	}
	
	/**
	 * <p>
	 * 获得<strong>左移</strong>移位后的比特。
	 * </p>
	 * 
	 * @param originBytes 原比特。
	 * @param offset <strong>左移</strong>偏移量。
	 * 
	 * @return 进行移位之后的比特。
	 */
	private static byte getLeftMovedByteArray(byte originByte, int offset) {
		byte source = (byte) (originByte & 0xff); // 去掉左侧符号。
		byte pre = (byte) (source << offset); // 获得左移后的后面(8-offset)位。
		byte last = (byte) (source >>> (8 - offset)); // 获得右移(8-offset)位后左侧剩余的offset位。
		byte mid = (byte) (pre | last); // 进行按位或运算。
		return mid;
	}
	
	/**
	 * <p>对获取的信息进行解密。</p>
	 * 
	 * @param sourceInfo 加密之后的密文
	 * 
	 * @return 原始的待加密的字符串。
	 */
	public static String getDecryptedInfo(String sourceInfo) {
		byte[] desBytes = Base64.decodeToBytes(sourceInfo);
		int offset = 5; // 右移五位。
		for (int i = 0; i < desBytes.length; i++) {
			desBytes[i] = getRightMovedByteArray(desBytes[i], offset); // 进行右移移位解密。
		}
		String desInfo = new String(desBytes);
		return desInfo;
	}
	
	/**
	 * <p>
	 * 获得<strong>右移</strong>移位后的比特。
	 * </p>
	 * 
	 * @param originBytes 原比特。
	 * @param offset <strong>右移</strong>偏移量。
	 * 
	 * @return 进行移位之后的比特。
	 */
	private static byte getRightMovedByteArray(byte originByte, int offset) {
		byte last = (byte) ((originByte & 0xff) >>> offset); // 获得右移后的前面(8-offset)位。
		byte pre = (byte) ((originByte & 0xff) << (8 - offset)); // 获得左移(8-offset)位后右侧剩余的offset位。
		byte mid = (byte) (pre | last); // 进行按位或运算。
		return mid;
	}
}
