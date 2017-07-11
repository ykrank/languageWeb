package com.rytong.emp.gui.atom;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;

import com.ghbank.moas.R;
import com.rytong.emp.dom.Entity;
import com.rytong.emp.dom.css.BgStyle;
import com.rytong.emp.dom.css.ComplexLayout;
import com.rytong.emp.dom.css.Layout;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.tool.Utils;
import org.w3c.dom.Element;

import com.rytong.emp.gui.GUIPropertyAdjustment;
import com.rytong.emp.gui.GUIView;
import com.rytong.emp.gui.atom.property.PropertyBorder;
import com.rytong.emp.gui.atom.property.PropertyDelay;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
/**
 * <p>
 * {@literal <silder/>}标签。
 * 进度条
 * </p>
 */
public class HXSeekBar extends Div implements GUIView, PropertyDelay {
	/** 与控件绑定的element */
	/** 与控件绑定的背景样式 */
	private BgStyle mBgStyle = null;
	private Bitmap bitmapPoint;//拖动按钮
	private int currentNumber = 0;
	private List<Point> points = new ArrayList<Point>();
	private int index = -1;
	private boolean upFlag = false;
	private int leftMargin =0;//左边距
	private int topMargin = 0;//上边距
	private int width_ = 0;//长度
	private int height_ = 0;//高度
	private int startColor_ = 0;//初始颜色
	private int centerColor_ = 0;//渐变颜色
	private int endColor_ = 0;//最终颜色
	private boolean enable_  = true;//是否可拖动
	private int progress_ = 0;//进度
	
	public HXSeekBar(Context context) {
		super(context);
		bitmapPoint = BitmapFactory.decodeResource(getResources(), R.drawable.sliderbtn);
	}

	
	public Element getElement() {
	    return mElement;
	}
	
	public void addSubView(View child) {
		this.addView(child);
	}
	
	@Override
	public void initRealView(Context context) {
		super.initRealView(context);

		//判断是否可滑动
		String enable = mElement.getAttribute("enable");
		if(enable != null && !"".equals(enable) && "true".equals(enable)){
				enable_ = true;
		}else{
				enable_ = false;
		}
		//设置进度初始值
		String value = mElement.getAttribute("value");		
		if(value != null && !"".equals(value)){
			float f_value = Float.parseFloat(value);
			progress_ = (int)(f_value *100);
		}
		//获取进度条背景颜色、二级进度条背景颜色、拖动按钮图片
		String background_width = (String) mLayout.getRepository().get(Entity.NODE_ATTRIBUTE_WIDTH);
		String background_height = (String) mLayout.getRepository().get(Entity.NODE_ATTRIBUTE_HEIGHT);
		//设置背景长度
		if(background_width!=null && !"".equals(background_width)){					
			width_ =Utils.defaultToScreen(background_width);
		}else{
			width_ =Utils.defaultToScreen(320);//满屏
		}
		//设置背景高度
		if(background_height!=null && !"".equals(background_height)){
			height_ = Utils.defaultToScreen(background_height);
			topMargin = (height_ - height_ /2 )/2;//上边距设定
			height_ = height_ /2;//拖动按钮设定
			bitmapPoint = Bitmap.createScaledBitmap(bitmapPoint, height_, height_, true);
		}else{
			
		}
		//初始、渐变、最终颜色设置
		String startColor = mElement.getAttribute("startColor");
		String centerColor = mElement.getAttribute("centerColor");
		String endColor = mElement.getAttribute("endColor");
		if(startColor!=null && !"".equals(startColor) && endColor!=null && !"".equals(endColor) && centerColor!= null && !"".equals(centerColor)) {
			startColor_ = Color.parseColor(startColor);
			centerColor_ = Color.parseColor(centerColor);
			endColor_ = Color.parseColor(endColor);
		}else{
			startColor_ = Color.parseColor("#fabf00");
			centerColor_ = Color.parseColor("#ccbbcc");
			endColor_ = Color.parseColor("#00dd00");
		}
		//左边距
		String left = (String) mLayout.getRepository().get(Entity.NODE_ATTRIBUTE_LEFT);
		if(left != null && !"".equals(left)){
			leftMargin = Integer.parseInt(left);
		}		
	}
	
	@Override
	public void onBindElement(Element element) {
	}

	@Override
	public Layout onBuildLayout() {
		return mLayout;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setTextSize(20);
		paint.setStyle(Paint.Style.FILL);
		//一级进度条背景颜色
		paint.setColor(Color.parseColor("#d0d0d0"));
		RectF r = new RectF();
		r.left = leftMargin;
		r.top = topMargin+(height_ /3);
		r.right = width_;
		r.bottom = topMargin+(height_/3)*2;
		canvas.drawRoundRect(r, 5, 5, paint);		
		for (int i = 0; i <= 100; i++) {
			Point pt = new Point();
			pt.set(i * (width_ / 100), (50 + height_) / 2);
			points.add(pt);
		}
		int pointWidth = bitmapPoint.getHeight();
		int pointHeight = bitmapPoint.getHeight();
		for(int i = 0; i <= 100; i++){
			if(index == i){
				if(upFlag ){
					//设置背景图颜色
					int[] color_arr = new int[]{startColor_,centerColor_,endColor_};
					float[] location_arr = new float[]{0,0.5f,1.0f};
					float f = ((float)width_/100);
					LinearGradient shader = new LinearGradient(0, 0,f*i,0,color_arr,location_arr,Shader.TileMode.MIRROR);
					paint.setShader(shader);
					RectF rf = new RectF();
					rf.left = leftMargin;
					rf.top = topMargin+(height_ /3);
					rf.right =f*i - leftMargin; 
					rf.bottom = topMargin+(height_/3)*2; 
					canvas.drawRoundRect(rf, 5, 5, paint);
					}
				if(i != 0 ){
					canvas.drawBitmap(bitmapPoint, i*((float)width_/100) - bitmapPoint.getWidth(), topMargin +(pointHeight - height_) / 2, paint);
				}else if(i == 0){
					canvas.drawBitmap(bitmapPoint, leftMargin,topMargin + (pointHeight - height_) / 2, paint);
				}
			}
		}
		//拖动按钮初始化
		if (!upFlag) {
			if(progress_ != 0){
				//设置背景图颜色
				int[] color_arr = new int[]{startColor_,centerColor_,endColor_};
				float[] location_arr = new float[]{0,0.5f,1.0f};
				float f = ((float)width_/100);
				LinearGradient shader = new LinearGradient(0, 0,f*progress_,0,color_arr,location_arr,Shader.TileMode.MIRROR);
				paint.setShader(shader);
				RectF rf = new RectF();
				rf.left = leftMargin;
				rf.top = topMargin+(height_ /3);
				rf.right =f*progress_; 
				rf.bottom = topMargin+(height_/3)*2; 
				canvas.drawRoundRect(rf, 10, 10, paint);
				canvas.drawBitmap(bitmapPoint, progress_*((float)width_/100) - (bitmapPoint.getWidth()/2), topMargin +(pointHeight - height_) / 2, paint);			
			}else{
				canvas.drawBitmap(bitmapPoint, leftMargin + (pointWidth / 2) - (pointWidth / 2),topMargin +(pointHeight - height_) / 2, paint);									
			}
		}
}


	@Override
	public boolean setPropertyByName(String name, String value) {		
		return false;
	}

	@Override
	public String getPropertyValue(String name) {
		return mElement.getAttribute(name);
	}


	@Override
	public boolean isFastClick() {
		// TODO Auto-generated method stub
		return false;
	}
		
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
//		return super.onTouchEvent(event);
		if(enable_ || "".equals(mElement.getAttribute("enable"))){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				upFlag = false;
				float dx = event.getX();
				float dy = event.getY();
				index = getTheMinPoint(points, dx, dy);
				onStartTrackingTouch(index);
				break;
			case MotionEvent.ACTION_MOVE:
				upFlag = true;
				dx = event.getX();
				dy = event.getY();
				index = getTheMinPoint(points, dx, dy);
				onProgressChanged(index);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				onStopTrackingTouch(index);
				break;
			default:
				break;
			}
		}		
		return true;	
	}
	
	private int getTheMinPoint(List<Point> points2, float dx, float dy) {
		int currentIndex = 0;
		float min = Math.abs(dx - points2.get(0).x);
		for (int i = 1; i < points2.size(); i++) {
			float temp = Math.abs(dx - points2.get(i).x);
			if (min > temp) {
				min = temp;
				currentIndex = i;
			}
		}
		return currentIndex;
	}
	
	public int getTheNumber() {
		return currentNumber;
	}
	//触发操作，拖动
		public void onProgressChanged(int index) {
				String onProgressChanged = mElement.getAttribute("onProgressChanged");
				if(onProgressChanged!=null && !"".equals(onProgressChanged)) {
					onProgressChanged = onProgressChanged.replace("()", "("+index+")");
					mLayout.getEMPRender().doLua(onProgressChanged);
				}
		}
	//表示进度条刚开始拖动，开始拖动时候触发的操作
	public void onStartTrackingTouch(int index) {
			String onStartTrackingTouch = mElement.getAttribute("onStartTrackingTouch");
			if(onStartTrackingTouch!=null && !"".equals(onStartTrackingTouch)) {
					onStartTrackingTouch = onStartTrackingTouch.replace("()", "("+index+")");
					mLayout.getEMPRender().doLua(onStartTrackingTouch);
			}
	}
	//停止拖动时候
	private void onStopTrackingTouch(int index){
		String onchange = mElement.getAttribute("onchange");
		if(onchange!=null && !"".equals(onchange)) {
			float i = ((float)index/100);
			mElement.setAttribute("value", String.valueOf(i));
			mLayout.getEMPRender().doLua(onchange);
		}
	}

}
