package com.rytong.emp.gui.animation;

import android.graphics.Camera;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * <p>3D旋转动画。<br>
 * 这个动画可以将对象沿x、y或者z轴进行旋转。</p>
 * <p>用法参考：
 * <pre>
 * View vAnim = <已经初始化好了的View对象>;
 * Rotate3dAnimation r3 = new Rotate3dAnimation(
 *     0, 720, // 旋转两周。
 *     240, 120, // 以(240, 120)为中心进行旋转。
 *     true, 1000, // 采用由近及远的景深，景深距离1000。
 *     Rotate3dAnimation.AXIS_TYPE.Y); // 沿Y轴进行旋转。
 * vAnim.setAnimation(r3); // 绑定动画对象。
 * r3.setDuration(5000); // 设置动画持续时间为5秒。
 * vAnim.startAnimation(r3); // 开始播放动画。
 * </pre>
 * </p>
 */
public class HXRotate3dAnimation extends Animation {

	/**
	 * <p>旋转过程中需要使用的摄像机对象。</p>
	 */
	private Camera camera = null; // 这个摄像机对象调用的是图形包里面的那个类，而不是硬件camera。
	/**
	 * <p>旋转的起始角度。</p>
	 */
	private float fromDegrees ;
	/**
	 * <p>旋转的终止角度。</p>
	 */
    private float toDegrees ;
    /**
	 * <p>旋转的类型的终止角度。</p>
	 */
    private int mPivotType;
    /**
	 * <p>旋转的中心点x坐标。</p>
	 */
    private float mPivotXValue ;
    /**
	 * <p>旋转的中心点y坐标。</p>
	 */
    private float mPivotYValue ;
    /**
     * <p>旋转的中心点x坐标。</p>
     */
    private float centerX ;
    /**
     * <p>旋转的中心点y坐标。</p>
     */
    private float centerY ;
    /**
     * <p>景深效果是否应该采用由近及远。</p>
     */
    private boolean isReverse ;
    /**
     * <p>景深设置。</p>
     */
    private float depthZ ;
    /**
     * <p>主轴种类。</p>
     */
    public static enum AXIS_TYPE{
    	/**
    	 * <p>水平旋转。</p>
    	 */
    	X, 
    	/**
    	 * <p>垂直旋转。</p>
    	 */
    	Y,
    	/**
    	 * <p>沿Z轴旋转。</p>
    	 */
    	Z};
    /**
     * <p>旋转时候的主轴。</p>
     */
    private AXIS_TYPE axis;
    
    /**
     * <p>3D旋转动画的构造方法。</p>
     * @param fromDegrees 开始的角度。
     * @param toDegrees 结束的角度。
     * @param pivotXValue 旋转的中心处x坐标。
     * @param pivotYValue 旋转的中心处y坐标。
     * @param reverse 景深效果是否采用由近及远，当depthZ被设置为0时无效。
     * @param depthZ 景深。
     * @param pivotType 选转类型。可采用值：
     *                  Amimation.ABSOLUTE,Amimation.RELATIVE_TO_SELF,Amimation.RELATIVE_TO_PARENT
     * @param axis 旋转轴。可取的值有：AXIS_TYPE.X、AXIS_TYPE.Y、AXIS_TYPE.Z
     * @see AXIS_TYPE#X
     * @see AXIS_TYPE#Y
     * @see AXIS_TYPE#Z
     */
    public HXRotate3dAnimation(float fromDegrees, float toDegrees, float pivotXValue, float pivotYValue,
            boolean reverse, float depthZ, int  pivotType,AXIS_TYPE axis){
    	this.fromDegrees = fromDegrees;
    	this.toDegrees = toDegrees;
    	this.mPivotXValue = pivotXValue;
    	this.mPivotYValue = pivotYValue;
    	this.mPivotType = pivotType;
    	this.isReverse = reverse;
    	this.depthZ = depthZ;
    	this.axis = axis;
    	this.camera = new Camera();
    }
    /**
     * <p>3D旋转动画的构造方法。</p>
     * @param fromDegrees 开始的角度。
     * @param toDegrees 结束的角度。
     * @param pivotXValue 旋转的中心处x坐标。
     * @param pivotYValue 旋转的中心处y坐标。
     * isReverse ： false。
     * depthZ ：0。
     * pivotType :RELATIVE_TO_SELF
     * @param axis 旋转轴。可取的值有：AXIS_TYPE.X、AXIS_TYPE.Y、AXIS_TYPE.Z
     * @see AXIS_TYPE#X
     * @see AXIS_TYPE#Y
     * @see AXIS_TYPE#Z
     */
    public HXRotate3dAnimation(float fromDegrees, float toDegrees, float pivotXValue, float pivotYValue,
            AXIS_TYPE axis){
    	this.fromDegrees = fromDegrees;
    	this.toDegrees = toDegrees;
    	this.mPivotXValue = pivotXValue;
    	this.mPivotYValue = pivotYValue;
    	this.mPivotType = RELATIVE_TO_SELF;
    	this.isReverse = false;
    	this.depthZ = 0;
    	this.axis = axis;
    	this.camera = new Camera();
    }
    
    /**
     * <p>3D旋转动画的构造方法。</p>
     * @param fromDegrees 开始的角度。
     * @param toDegrees 结束的角度。
     * pivotXValue:0。
     * pivotYValue :0。
     * isReverse :false。
     * depthZ ：0。
     * pivotType :RELATIVE_TO_SELF
     * @param axis 旋转轴。可取的值有：AXIS_TYPE.X、AXIS_TYPE.Y、AXIS_TYPE.Z
     * @see AXIS_TYPE#X
     * @see AXIS_TYPE#Y
     * @see AXIS_TYPE#Z
     */
    public HXRotate3dAnimation(float fromDegrees, float toDegrees, AXIS_TYPE axis){
    	this.fromDegrees = fromDegrees;
    	this.toDegrees = toDegrees;
    	this.mPivotXValue =0;
    	this.mPivotYValue = 0;
    	this.mPivotType = RELATIVE_TO_SELF;
    	this.isReverse = false;
    	this.depthZ = 0;
    	this.axis = axis;
    	this.camera = new Camera();
    }
    
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
//		super.applyTransformation(interpolatedTime, t);
        float degrees = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime);
        camera.save();
        if (isReverse) {
        	camera.translate(0.0f, 0.0f, depthZ * interpolatedTime);
        } else {
        	camera.translate(0.0f, 0.0f, depthZ * (1.0f - interpolatedTime));
        }
        if (AXIS_TYPE.Y.equals(axis)){
        	camera.rotateY(degrees);
        } else if(AXIS_TYPE.X.equals(axis)){
        	camera.rotateX(degrees);
        } else if(AXIS_TYPE.Z.equals(axis)){
        	camera.rotateZ(degrees);
        }
        camera.getMatrix(t.getMatrix());
        camera.restore();
        t.getMatrix().preTranslate(-centerX, -centerY);
        t.getMatrix().postTranslate(centerX, centerY);
	}
	
	   @Override
	    public void initialize(int width, int height, int parentWidth, int parentHeight) {
	        super.initialize(width, height, parentWidth, parentHeight);
	        centerX = resolveSize(mPivotType, mPivotXValue, width, parentWidth);
	        centerY = resolveSize(mPivotType, mPivotYValue, height, parentHeight);
	    }
	
}
