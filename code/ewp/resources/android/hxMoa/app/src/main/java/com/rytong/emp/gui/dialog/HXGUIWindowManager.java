package com.rytong.emp.gui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.rytong.emp.dom.EMPDocument;
import com.rytong.emp.dom.StyleRepository;

public class HXGUIWindowManager {

	private static HashMap<Integer, HXGUIWindow> mWindowCache = new HashMap<Integer, HXGUIWindow>();
	private static HashMap<Integer, StyleRepository> mWindowStyleRepository= new HashMap<Integer, StyleRepository>();

		
	/**
	 * 创建HXGUIWindow<br>
	 * 
	 * @param empDocument
	 * @param element
	 * @param tag
	 * @param modal
	 * @return
	 */
	public static HXGUIWindow create(EMPDocument empDocument, Element element, final int tag, boolean modal) {
		HXGUIWindow HXGUIWindow = mWindowCache.get(Integer.valueOf(tag));
		if (HXGUIWindow == null) {
			HXGUIWindow = new HXGUIWindow(element, modal);
			HXGUIWindow.setWindowTag(tag);
			mWindowCache.put(Integer.valueOf(tag), HXGUIWindow);
		} 
		return HXGUIWindow;
	}

	/**
	 * 移除HXGUIWindow
	 * 
	 * @param empDocument
	 * @param tag
	 * @return
	 */
	public static HXGUIWindow remove(EMPDocument empDocument, int tag) {
		HXGUIWindow HXGUIWindow = mWindowCache.remove(Integer.valueOf(tag));
		if (HXGUIWindow != null) {
//			final Document document = empDocument.getDocument();
//			final Element element = HXGUIWindow.getElement();
//			Node parent = element.getParentNode();
//			if (parent != null && parent.equals(document.getDocumentElement())) {
//				//empDocument.revertDocument(element);
//				 parent.removeChild(element);
//				 mWindowStyleRepository.get(tag).clear();
//				 mWindowStyleRepository.remove(tag);
//			}
		}
		return HXGUIWindow;
	}

	/**
	 * 获取mWindowCache key：winow的Tag
	 * 
	 * @return
	 */
	public static HashMap<Integer, HXGUIWindow> getWindowCacheMap() {
		return mWindowCache;
	}
	
	/**
	 * 获取mWindowStyleRepository- key：winow的Tag
	 * 
	 * @return
	 */
	public static HashMap<Integer, StyleRepository> getWindowStyleRepository() {
		return mWindowStyleRepository;
	}

	/**
	 * 获取mWindowCache并转化为ArrayList
	 * 
	 * @return
	 */
	public static ArrayList<HXGUIWindow> getWindowCacheList() {
		ArrayList<HXGUIWindow> list = new ArrayList<HXGUIWindow>();
		Iterator<Entry<Integer, HXGUIWindow>> iter = mWindowCache.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, HXGUIWindow> entry = iter.next();
			HXGUIWindow value = entry.getValue();
			list.add(value);
		}
		return list;
	}
	
	/**
	 * 判断element是否在popWindow上
	 * 
	 * @param
	 * @param
	 * 
	 * @return HXGUIWindow
	 */
	public static HXGUIWindow checkOnWindow(Element element) {
		if (getWindowCacheList() != null) {
			for (HXGUIWindow window : getWindowCacheList()) {
				Element windowElement = window.getElement();
				Element checkElement = element;

				while (checkElement != null) {
					if (checkElement == windowElement) {
						return window;
					} else {
						Node parentNode = checkElement.getParentNode();
						short nodeType = parentNode.getNodeType();
						if (nodeType == Node.ELEMENT_NODE) {
							checkElement = (Element) checkElement.getParentNode();
						} else {
							return null;
						}
					}
				}
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 判断popwindow 是否正在显示
	 * @param tag
	 * @return
	 */
	public static boolean isShowing(int tag){
		 return mWindowCache.containsKey(Integer.valueOf(tag));
	}
}
