package com.rytong.emp.gui.atom.atomrela;

import com.rytong.emp.tool.Utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

/**
 * <p>
 * 按钮的状态图片
 * </p>
 */
public class HXButtonStatesDrawable extends StateListDrawable {

	/**
	 * <p>
	 * 构造方法。以给定的drawable来标明按钮不同状态。
	 * </p>
	 * 
	 * @param normalDrawable 普通图片。
	 * @param disabledDrawable 按钮不可用时候的图片。
	 * @param focusedDrawable 按钮获得焦点时候的图片。
	 * @param pressedDrawable 按下时候的图片。
	 */
	public HXButtonStatesDrawable(Drawable normalDrawable, Drawable disabledDrawable, Drawable focusedDrawable, Drawable pressedDrawable) {
		setDrawable(normalDrawable, disabledDrawable, focusedDrawable, pressedDrawable);
	}

	/**
	 * <p>
	 * 构造方法。这种方式将创建默认样式的按钮图片。
	 * </p>
	 */
	public HXButtonStatesDrawable() {
		this(Utils.defaultToScreen(3));
	}

	/**
	 * <p>
	 * 构造方法。这种方式将创建默认样式的按钮图片。
	 * </p>
	 * 
	 * @param cornerRadius 圆角弧度。
	 */
	public HXButtonStatesDrawable(int cornerRadius) {
		// 创建对应的图片。
		Drawable normalDrawable = createDrawable(new int[] { 0x002c7be5, 0x002775e3, 0x0088b0f7 }, 1, 0xff696a69, cornerRadius); // 普通图片。
		Drawable disabledDrawable = createDrawable(new int[] { 0x00dfdfdf, 0x00dedede }, 1, 0xff696a69, cornerRadius); // 按钮不可用时候的图片。
		Drawable focusedDrawable = createDrawable(new int[] { 0x002c7be5, 0x002775e3, 0x0088b0f7 }, Utils.defaultToScreen(2), 0xffeaa524, cornerRadius); // 按钮获得焦点时候的图片。
		Drawable pressedDrawable = createDrawable(new int[] { 0x00205aa6, 0x001f58a3, 0x006f9bec }, 1, 0xff696a69, cornerRadius); // 按下时候的图片。
		setDrawable(normalDrawable, disabledDrawable, focusedDrawable, pressedDrawable);
	}

	/**
	 * <p>
	 * 构造方法。这种方式将创建纯色的按钮图片。
	 * </p>
	 * 
	 * @param normalDrawableColor 普通图片颜色。
	 * @param disabledDrawableColor 按钮不可用时候的图片颜色。
	 * @param focusedDrawableColor 按钮获得焦点时候的图片颜色。
	 * @param pressedDrawableColor 按下时候的图片颜色。
	 */
	public HXButtonStatesDrawable(int normalDrawableColor, int disabledDrawableColor, int focusedDrawableColor, int pressedDrawableColor) {
		this(normalDrawableColor, disabledDrawableColor, focusedDrawableColor, pressedDrawableColor, Utils.defaultToScreen(3));
	}

	/**
	 * <p>
	 * 构造方法。这种方式将创建纯色的按钮图片。
	 * </p>
	 * 
	 * @param normalDrawableColor 普通图片颜色。
	 * @param disabledDrawableColor 按钮不可用时候的图片颜色。
	 * @param focusedDrawableColor 按钮获得焦点时候的图片颜色。
	 * @param pressedDrawableColor 按下时候的图片颜色。
	 * @param cornerRadius 圆角弧度。
	 */
	public HXButtonStatesDrawable(int normalDrawableColor, int disabledDrawableColor, int focusedDrawableColor, int pressedDrawableColor, int cornerRadius) {
		// 创建对应的图片。
		Drawable normalDrawable = createDrawable(new int[] { normalDrawableColor, normalDrawableColor }, 1, 0xff696a69, cornerRadius); // 普通图片。
		Drawable disabledDrawable = createDrawable(new int[] { disabledDrawableColor, disabledDrawableColor }, 1, 0xff696a69, cornerRadius); // 按钮不可用时候的图片。
		Drawable focusedDrawable = createDrawable(new int[] { focusedDrawableColor, focusedDrawableColor }, Utils.defaultToScreen(2), 0xffeaa524, cornerRadius); // 按钮获得焦点时候的图片。
		Drawable pressedDrawable = createDrawable(new int[] { pressedDrawableColor, pressedDrawableColor }, 1, 0xff696a69, cornerRadius); // 按下时候的图片。
		setDrawable(normalDrawable, disabledDrawable, focusedDrawable, pressedDrawable);
	}

	/**
	 * <p>
	 * 构造方法。这种方式将创建由下向上过渡色的按钮图片。
	 * </p>
	 * 
	 * @param normalDrawableColors 普通图片颜色数组。
	 * @param disabledDrawableColors 按钮不可用时候的图片颜色数组。
	 * @param focusedDrawableColors 按钮获得焦点时候的图片颜色数组。
	 * @param pressedDrawableColors 按下时候的图片颜色数组。
	 */
	public HXButtonStatesDrawable(int[] normalDrawableColors, int[] disabledDrawableColors, int[] focusedDrawableColors, int[] pressedDrawableColors) {
		this(normalDrawableColors, disabledDrawableColors, focusedDrawableColors, pressedDrawableColors, Utils.defaultToScreen(3));
	}

	/**
	 * <p>
	 * 构造方法。这种方式将创建由下向上过渡色的按钮图片。
	 * </p>
	 * 
	 * @param normalDrawableColors 普通图片颜色数组。
	 * @param disabledDrawableColors 按钮不可用时候的图片颜色数组。
	 * @param focusedDrawableColors 按钮获得焦点时候的图片颜色数组。
	 * @param pressedDrawableColors 按下时候的图片颜色数组。
	 * @param cornerRadius 圆角弧度。
	 */
	public HXButtonStatesDrawable(int[] normalDrawableColors, int[] disabledDrawableColors, int[] focusedDrawableColors, int[] pressedDrawableColors, int cornerRadius) {
		// 创建对应的图片。
		Drawable normalDrawable = createDrawable(normalDrawableColors, 1, 0xff696a69, cornerRadius); // 普通图片。
		Drawable disabledDrawable = createDrawable(disabledDrawableColors, 1, 0xff696a69, cornerRadius); // 按钮不可用时候的图片。
		Drawable focusedDrawable = createDrawable(focusedDrawableColors, Utils.defaultToScreen(2), 0xffeaa524, cornerRadius); // 按钮获得焦点时候的图片。
		Drawable pressedDrawable = createDrawable(pressedDrawableColors, 1, 0xff696a69, cornerRadius); // 按下时候的图片。
		setDrawable(normalDrawable, disabledDrawable, focusedDrawable, pressedDrawable);
	}

	/**
	 * <p>
	 * 创建图片。
	 * </p>
	 * 
	 * @param innerColors 按钮内部颜色数组，注意是从下往上进行过度的。
	 * @param borderWidth 边线宽度。
	 * @param borderColor 边框线颜色。
	 * @param cornerRadius 圆角弧度。
	 */
	private Drawable createDrawable(int[] innerColors, int borderWidth, int borderColor, int cornerRadius) {
		GradientDrawable drawable = new GradientDrawable(Orientation.BOTTOM_TOP, innerColors);
		drawable.setCornerRadius(cornerRadius); // 设置圆角弧度。
		drawable.setStroke(borderWidth, borderColor); // 绘制边线。
		return drawable;
	}

	/**
	 * <p>
	 * 设置图片。
	 * </p>
	 */
	private void setDrawable(Drawable normalDrawable, Drawable disabledDrawable, Drawable focusedDrawable, Drawable pressedDrawable) {
		addState(new int[] { -android.R.attr.state_window_focused, android.R.attr.state_enabled }, normalDrawable);
		addState(new int[] { -android.R.attr.state_enabled }, disabledDrawable);
		addState(new int[] { android.R.attr.state_pressed }, pressedDrawable);
		addState(new int[] { android.R.attr.state_focused, android.R.attr.state_enabled }, focusedDrawable);
		addState(new int[] { android.R.attr.state_enabled }, normalDrawable);
		addState(new int[] { android.R.attr.state_focused }, focusedDrawable);
		addState(new int[] {}, normalDrawable);
	}
}
