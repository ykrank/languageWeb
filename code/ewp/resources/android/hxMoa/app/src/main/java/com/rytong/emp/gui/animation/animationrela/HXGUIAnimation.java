package com.rytong.emp.gui.animation.animationrela;

import java.util.HashMap;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.rytong.emp.gui.animation.HXRotate3dAnimation.AXIS_TYPE;
import com.rytong.emp.lua.EMPLua;
import com.rytong.emp.lua.EMPLuaFactory;
import com.rytong.emp.lua.LuaMetatable;

/**
 * <p>
 * 动画类用到的成员，提供一些变量的存储和其他功能。
 * </p>
 */
public class HXGUIAnimation {

	/**
	 * <p>
	 * 线性变换的种类。
	 * </p>
	 */
	public static enum CURVE {

		/**
		 * <p>
		 * 匀速动画。
		 * </p>
		 */
		LINEAR,
		/**
		 * <p>
		 * 动画先慢后快。
		 * </p>
		 */
		EASE_IN,
		/**
		 * <p>
		 * 动画先快后慢。
		 * </p>
		 */
		EASE_OUT,
		/**
		 * <p>
		 * 动画先慢后快，再变慢。
		 * </p>
		 */
		EASE_INOUT
	}

	/** 从左侧滑入 */
	public static final int SLIDE_FROM_LEFT = 4;
	/** 从右侧滑入 */
	public static final int SLIDE_FROM_RIGHT = 5;
	/** 从上边滑入 */
	public static final int SLIDE_FROM_UP = 6;
	/** 从下边滑入 */
	public static final int SLIDE_FROM_DOWN = 7;

	/** LuaMetatable */
	private LuaMetatable mLuaMetatable = null;

	// rotate,matrix,skew动画为系统动画，view.clearAnimation会导致动画效果消失
	private boolean needKeep = false;

	// ************** 对目前控件的动画信息的记录 ********************/
	/** lua状态机的序号 */
	private int mLuaIndex = -1;
	/** 动画开始时候方法的序号 */
	private int mStartAnimationIndex = -1;
	/** 动画结束时候方法的序号 */
	private int mStopAnimationIndex = -1;
	/** 动画重复次数 */
	private int mRepeatCount = 0;
	/** 动画速率插值 */
	private Interpolator mInterpolator = null;

	// ************** 对目前控件的动画状态的记录 ********************/
	/** 当前x轴旋转的角度 */
	private float mRotateAngleX = 0f;
	/** 当前y轴旋转的角度 */
	private float mRotateAngleY = 0f;
	/** 当前z轴旋转的角度 */
	private float mRotateAngleZ = 0f;
	/** 当前绕某个轴旋转 X Y Z */
	private AXIS_TYPE mCurrentAxis;
	/** 记录控件现在的透明度 */
	private float mAnimaAlpha = 1.0f; // 默认不透名。
	/** 记录控件现在X轴方向上的缩放比率 */
	private float mAnimaScaleX = 1.0f;
	/** 记录控件现在Y轴方向上的缩放比率 */
	private float mAnimaScaleY = 1.0f;
	/** 记录控件现在X轴方向上的拉伸比率 */
	private float mSkewAngleX = 0f;
	/** 记录控件现在Y轴方向上的拉伸比率 */
	private float mSkewAngleY = 0f;
	/** 记录动画matrix */
	private float[] mMatrix = new float[] { 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
	/** 记录动画from */
	private HashMap<String, Object> mFromHashMap = null;
	/** 记录动画to */
	private HashMap<String, Object> mToHashMap = null;

	public HXGUIAnimation(LuaMetatable luaMetatable) {
		mLuaMetatable = luaMetatable;
	}

	/**
	 * 回调方法
	 * 
	 * @param callIndex
	 */
	public void callback(int callIndex) {
		if (mLuaIndex == -1 || callIndex == -1) {
			return;
		}
		final EMPLua empLua = EMPLuaFactory.getEMPLua(mLuaIndex);
		empLua.callback(callIndex, mLuaMetatable);
	}

	// ******************************get and
	// set********************************************
	public float getSkewAngleX() {
		return mSkewAngleX;
	}

	public void setSkewAngleX(float angle) {
		this.mSkewAngleX = angle;
	}

	public float getSkewAngleY() {
		return mSkewAngleY;
	}

	public void setSkewAngleY(float angle) {
		this.mSkewAngleY = angle;
	}

	public int getLuaIndex() {
		return mLuaIndex;
	}

	public void setLuaIndex(int luaIndex) {
		this.mLuaIndex = luaIndex;
	}

	public int getStartAnimationIndex() {
		return mStartAnimationIndex;
	}

	public void setStartAnimationIndex(int startAnimationIndex) {
		this.mStartAnimationIndex = startAnimationIndex;
	}

	public int getStopAnimationIndex() {
		return mStopAnimationIndex;
	}

	public void setStopAnimationIndex(int stopAnimationIndex) {
		this.mStopAnimationIndex = stopAnimationIndex;
	}

	public float getRotateAngleX() {
		return mRotateAngleX;
	}

	public void setRotateAngleX(float angle) {
		this.mRotateAngleX = angle;
	}

	public float getRotateAngleY() {
		return mRotateAngleY;
	}

	public void setRotateAngleY(float angle) {
		this.mRotateAngleY = angle;
	}

	public float getRotateAngleZ() {
		return mRotateAngleZ;
	}

	public void setRotateAngleZ(float angle) {
		this.mRotateAngleZ = angle;
	}

	public void setCurrentAxis(AXIS_TYPE axis) {
		this.mCurrentAxis = axis;
	}

	public AXIS_TYPE getCurrentAxis() {
		return mCurrentAxis;
	}

	public float getAnimaAlpha() {
		return mAnimaAlpha;
	}

	public void setAnimaAlpha(float animaAlpha) {
		this.mAnimaAlpha = animaAlpha;
	}

	public float getAnimaScaleX() {
		return mAnimaScaleX;
	}

	public void setAnimaScaleX(float animaScaleX) {
		mAnimaScaleX = animaScaleX;
	}

	public float getAnimaScaleY() {
		return mAnimaScaleY;
	}

	public void setAnimaScaleY(float animaScaleY) {
		mAnimaScaleY = animaScaleY;
	}

	public float[] getMatrix() {
		return mMatrix;
	}

	public void setMatrix(float[] matrix) {
		this.mMatrix = matrix;
	}

	public void setFrom(HashMap<String, Object> from) {
		mFromHashMap = from;
	}

	public void setTo(HashMap<String, Object> to) {
		mToHashMap = to;
	}

	public HashMap<String, Object> getFrom() {
		return mFromHashMap;
	}

	public HashMap<String, Object> getTo() {
		return mToHashMap;
	}

	public void setRepeatCount(int repeatCount) {
		mRepeatCount = repeatCount;
	}

	public int getRepeatCount() {
		return mRepeatCount;
	}

	public Interpolator getInterpolator() {
		return mInterpolator == null ? new LinearInterpolator() : mInterpolator;
	}

	public void setInterpolator(Interpolator interpolator) {
		mInterpolator = interpolator;
	}

	public boolean isNeedKeep() {
		return needKeep;
	}

	public void setNeedKeep(boolean needKeep) {
		this.needKeep = needKeep;
	}

}
