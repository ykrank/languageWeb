package com.rytong.emp.test.config;

import java.io.Serializable;

import com.rytong.emp.gui.atom.property.PropertyImg.IMGMODE;
import com.rytong.emp.gui.atom.property.handle.TextEffectHandler.BreakMode;

/**
 * 这里存放的是SDK全局属性配置中用到的一些属性
 * @author xiaofei
 *
 */
public class SDKConfig implements Serializable{

	/**省略号模式*/
	public BreakMode mbreakMode = null;
	/**阴影颜色*/
	public String mShadowColor = null;
	/**文字阴影偏移量*/
	public int mShadowOffsetX = 5;
	public int mShadowOffsetY = 5;
	/**默认键盘顺序*/
	public boolean mIsOrder = false;
	/**默认替换符*/
	public String mSubstitute = "*";
	/**按钮默认延时时间*/
	public int mDelayTime = 0;
	/**默认输入框清空模式*/
	public IMGMODE mClearMode = IMGMODE.never;
	/**默认加载中图片*/
	public String mLoadingImg = "ryt_img_loading.jpg";
	/**加载失败图片*/
	public String mLoadFailed = "ryt_img_failed.jpg";
	/**link文本省略模式*/
	public BreakMode mLinkBreakMode = null;
	/**资源图片最优显示分辨率宽*/
	public int  mOptimalWidth = 480;
}
