package com.rytong.emp.test.multitask;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AbsoluteLayout;

import com.rytong.emp.dom.css.BgStyle;
import com.rytong.emp.dom.css.ComplexLayout;
import com.rytong.emp.dom.css.Layout;
import com.rytong.emp.gui.GUIView;
import com.rytong.emp.gui.atom.property.PropertyBorder;


/**
 * 扩展控件，用于装载及管理子任务
 * 
 * @author Zhoucj
 *
 */
public class IFrame extends AbsoluteLayout implements GUIView {
	
	/** 多任务控件标签名 */
	public static final String IFRAME = "iframe";
	
	/** 与控件绑定的element **/
	protected Element mElement = null;
	/** 与控件绑定的布局 **/
	protected ComplexLayout mLayout = null;
	/** 与控件绑定的背景样式 **/
	private BgStyle mBgStyle = null;
	
	/** 任务列表 */
	private Map<String, View> mTaskMap = new HashMap<String, View>();
	
	public IFrame(Context context) {
		super(context);
		setBackgroundColor(Color.TRANSPARENT);

		// 增加边框
		mBgStyle = new BgStyle();
		mBgStyle.addDecorate(BgStyle.BG_BORDER | BgStyle.BG_ROUNDED);
	}
	
	public Map<String, View> getTaskMap() {
		return mTaskMap;
	}

	@Override
	public void onBindElement(Element element) {
		// 处理border属性
		(new PropertyBorder(mBgStyle)).checkBorderProperty(element);
	}

	@Override
	public Layout onBuildLayout() {
		mLayout = new ComplexLayout(Layout.MATCH_PARENT, Layout.MATCH_PARENT);
		mLayout.setStyle(mBgStyle);
		return mLayout;
	}
}
