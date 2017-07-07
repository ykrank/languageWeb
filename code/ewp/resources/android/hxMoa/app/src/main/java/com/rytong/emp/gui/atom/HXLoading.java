package com.rytong.emp.gui.atom;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;

import com.rytong.emp.dom.css.Layout;

import org.w3c.dom.Element;

import com.rytong.emp.gui.GUIFactory;
import com.rytong.emp.gui.GUIView;
import com.rytong.emp.gui.animation.HXFrameAnimation;
import com.rytong.emp.gui.atom.property.PropertyDelay;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.test.MainActivity;

import android.view.View;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
/**
 * <p>
 * {@literal <silder/>}标签。
 * 进度条
 * </p>
 */
public class HXLoading extends Div implements GUIView, PropertyDelay {
	Context mContext;
	
	public HXLoading(Context context) {
		super(context);
		mContext = context;
	}

	
	public Element getElement() {
	    return mElement;
	}
	
	public void addSubView(View child) {
		this.addView(child);
	}
	HXFrameAnimation animation;
	AbsoluteLayout parasitic;
	@Override
	public void initRealView(Context context) {
		super.initRealView(context);
		animation = new HXFrameAnimation(); // 准备好的动画序列备用。
		ArrayList<String> frames = new ArrayList<String>();
		for(int i=1; i<9; i++) {
			String fileName = "channel_load"+i+".png";
			frames.add(fileName);
		}
		animation.setFrameImage(frames);
		animation.setFrameSize(0, 0, mLayout.getCssScreenWidth(), mLayout.getCssScreenHeight());
		
		AbsoluteLayout.LayoutParams l = new AbsoluteLayout.LayoutParams(mLayout.getCssScreenWidth(), mLayout.getCssScreenHeight(), 0, 0);
		parasitic = new AbsoluteLayout(getContext());
		parasitic.setLayoutParams(l);
		parasitic.setBackgroundDrawable(animation);
		
		addView(parasitic);
		animation.setDuration(MainActivity.mEmpLua.getEMPRender(), 2);
		animation.setRepeatCount(50);
		final GUIFactory guiFactory = EMPRender.get(0).getGUIFactory();
		guiFactory.addGUITask(new Runnable() {
			@Override
			public void run() {
				parasitic.post(new Runnable() {
					@Override
					public void run() {
						animation.start();
					}
				});
			}
		});
		
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
}
