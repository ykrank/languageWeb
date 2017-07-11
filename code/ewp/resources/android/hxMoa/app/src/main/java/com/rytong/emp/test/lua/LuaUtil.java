package com.rytong.emp.test.lua;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ghbank.moas.R;
import com.google.gson.Gson;
import com.rytong.emp.lua.java.CLEntity;
import com.rytong.emp.lua.java.CLuaFunction;
import com.rytong.emp.lua.unit.LuaSearchBuildingAdapter;
import com.rytong.emp.lua.unit.LuaSearchRoomAdapter;
import com.rytong.emp.lua.unit.LuaSearchUnitAdapter;
import com.rytong.emp.render.EMPRender;
import com.rytong.emp.test.MainActivity;
import com.rytong.emp.unit.MGsonBuilder;
import com.rytong.emp.unit.MoasCustomDialog;
import com.rytong.emp.unit.SearchBuildingAdapter;
import com.rytong.emp.unit.SearchRoomAdapter;
import com.rytong.emp.unit.SearchTreeChild;
import com.rytong.emp.unit.SearchTreeResponse;
import com.rytong.emp.unit.SearchTreeVo;
import com.rytong.emp.unit.SearchUnitAdapter;
import com.rytong.emp.unit.SelectWorkAdapter;
import com.rytong.emp.unit.TreeVo;

public class LuaUtil {
	/** empRender对象。 */
	EMPRender mEMPRender = null;
	/** Context */
	Context context_;
	Activity activity_;
	//
	private SelectWorkAdapter workAdapter;

	TextView back_, finish_,title_;
	
	private int Tag = -1;
	
	private View dialogView, search_btitle;
	
	private MoasCustomDialog treeDialog;
	
	private ListView bListView;

	public static List<String> choose_kData = new ArrayList<String>();
	
	public static List<String> choose_VData = new ArrayList<String>();
	/** 背景长度 */
	public static int width_ = 0;
	/** 背景高度 */
	int heght_ = 0;

	public static int choose_build = 0;
	
	public static int choose_unit = 0;
	
	public static int choose_room = 0;
	
	public static boolean BACKFLAG_ = false;
	
	private LuaSearchBuildingAdapter buildAdapter;
	private LuaSearchRoomAdapter roomAdapter;
	private LuaSearchUnitAdapter unitAdapter;

	public static StringBuffer callBackBuff = null;// 显示值
	

	private List<SearchTreeChild> treeData = new ArrayList<SearchTreeChild>();
	private List<SearchTreeVo> unitData = new ArrayList<SearchTreeVo>();
	private List<TreeVo> roomData = new ArrayList<TreeVo>();

	private String put_ = "", titile = "", jsonStr = "";

	static CLuaFunction mCallLua;
	public static boolean isLua = false;
	
	public static String deleteStr = null;
	public static List<String> deleteList = new ArrayList<String>();
	//
	/**
	 * 构造方法。
	 * @param empRender empRender对象。
	 */
	public LuaUtil(EMPRender empRender,Context context) {
		// TODO Auto-generated constructor stub
		mEMPRender = empRender;
		context_ = context;
		activity_ = (Activity)context;
		isLua = true;
	}
	/**
	 * 判断是否有虚拟物理按键
	 * @return
	 */
	public String checkHasBar(){

		boolean hasBar_ = false;
		Resources rs = context_.getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if(id > 0){
			hasBar_ = rs.getBoolean(id);
		}
		try {
			Class systemClass = Class.forName("android.os.SystemProperties");
			Method method = systemClass.getMethod("get", String.class);
			String navBarOverride = (String)method.invoke(systemClass, "qemu.hw.mainkeys");
			if("1".equals(navBarOverride)){
				hasBar_ = false;
			}else if("0".equals(navBarOverride)){
				hasBar_ = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return String.valueOf(hasBar_);
	}
	/**
	 * 获取虚拟物理按键高度
	 * @return 
	 */
	public String getBarHeight(){
		int barHeight = 0;
		Resources rs = context_.getResources();
		int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
		if(id > 0 && "true".equals(this.checkHasBar())){
			barHeight = rs.getDimensionPixelSize(id);	
		}
		return String.valueOf(barHeight);
	}
	
	public void openUnit(String json, String title, String put, String showValue, final CLuaFunction callLua){
		callBackBuff = new StringBuffer();
		mCallLua = callLua;
		this.jsonStr = json;
		this.titile = title;
		this.put_ = put;
		readList(showValue, put_);
		readJsonStr();
		activity_.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MoasCustomDialog.Builder builder = new MoasCustomDialog.Builder(context_);
				dialogView = activity_.getLayoutInflater().inflate(R.layout.listview_dialog, null);
				title_ = (TextView) dialogView.findViewById(R.id.custom_title);
				search_btitle = dialogView.findViewById(R.id.search_btitle);
				back_ = (TextView) dialogView.findViewById(R.id.custom_back);
				finish_ = (TextView) dialogView.findViewById(R.id.custom_finish);
				bListView = (ListView) dialogView.findViewById(R.id.buildings_listview);
				treeDialog = builder.listDialog(dialogView);
				treeDialog.show();
				initWork();				
				back_.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						removeMoreThan();
						BACKFLAG_ = true;
						switch (Tag) {
						case -1:
							Tag = 2;
							if (choose_build != 0) {
								for (int i = 0; i < choose_build; i++) {
									choose_kData.remove(choose_kData.size() - 1);
								}
								choose_build = 0;
							}
							initWork();
							break;

						case 0:
							Tag = -1;
							if (choose_unit != 0) {
								for (int i = 0; i < choose_unit; i++) {
									choose_kData.remove(choose_kData.size() - 1);
								}
								choose_unit = 0;
							}
							initBuilds();
							break;

						case 1:
							Tag = 0;
							if (choose_room != 0) {
								for (int i = 0; i < choose_room; i++) {
									choose_kData.remove(choose_kData.size() - 1);
									choose_VData.remove(choose_VData.size() - 1);
								}
								choose_room = 0;
							}
							initUnits();
							break;
						case 2:
							if (treeDialog != null && treeDialog.isShowing()) {
								String text = null;      // 返回显示
								if (!choose_kData.isEmpty()) {
									for (int i = 0; i < choose_kData.size(); i++) {
										callBackBuff.append(choose_kData.get(i)+put_);
									}
									callBackBuff.deleteCharAt(callBackBuff.length() - 1);//去除分隔符
									text = callBackBuff.toString();
									callBackBuff.delete(0, callBackBuff.length());
								
								} else {
									text = "";
								}
								
								CLEntity entity = new CLEntity();
								if(entity.next()){
									entity.removeAllKV();
								}
								entity.put("value", text);
								// 调用回调函数 scanResult
								MainActivity.mEmpLua.callbackAndDispose(mCallLua.mFunctionIndex, new Object[] { entity });
								cleanJson();
								treeDialog.dismiss();
							}
							break;
						default:
							break;
						}
					}
				});
				finish_.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						removeMoreThan();
						
						choose_build = 0;
						choose_room = 0;
						choose_unit = 0;
						switch (Tag) {
						case 2:
							// 一级
							finish_.setText("完成");
							initBuilds();
							Tag = -1;
							bListView
									.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> parent, View view,
												int position, long id) {
											// TODO Auto-generated method stub
											switch (Tag) {
											case -1:
												SearchTreeChild vo1 = buildAdapter
														.getItem(position);
												title_.setText("抄送单位");
												unitData = vo1.getList2();
												if (unitData.size() == 0) {
													Tag = -1;
												} else {
													Tag = 0;
													initUnits();
												}
												break;
											case 0:

												SearchTreeVo vo2 = unitAdapter
														.getItem(position);
												title_.setText("抄送单位");
												roomData = vo2.getList3();
												if (roomData.size() == 0) {
													Tag = 0;
												} else {
													Tag = 1;
													initRooms();
												}
												break;

											case 1:
												TreeVo vo3 = roomAdapter
														.getItem(position);
												Tag = 2;
												break;
											default:
												break;
											}
										}
									});
							break;
						case -1:
							initWork();
							break;
						case 0:
							initWork();
							break;
						case 1:
							initWork();
							break;
						default:
							break;
						}
					}
				});
			}
		});
}
	// 读取Json
		private void readJsonStr() {
			if (jsonStr != null) {
				Gson gson = new MGsonBuilder().createGson();
				SearchTreeResponse data = gson.fromJson(jsonStr,
						SearchTreeResponse.class);
				treeData = data.getList1();
			}
		}
		
	private void initWork() {
		removeMoreThan();
		if (workAdapter == null) {
			workAdapter = new SelectWorkAdapter(activity_);
		}
		title_.setText("已选列表");
		finish_.setText("添加");
		Tag = 2;
		bListView.setAdapter(workAdapter);
		workAdapter.setData(choose_kData);
		// 已选列表--长按删除选项
		bListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				choose_kData.remove(arg2);
				initWork();
				return false;
			}
		});
		setWindowHeight();
	}
	private void initBuilds() {
		if (buildAdapter == null) {
			buildAdapter = new LuaSearchBuildingAdapter(activity_);
		}
		bListView.setAdapter(buildAdapter);
		bListView.setOnItemLongClickListener(null);
		buildAdapter.setData(treeData);
		title_.setText("抄送单位");
		setWindowHeight();
	}

	private void initUnits() {
		if (unitAdapter == null) {
			unitAdapter = new LuaSearchUnitAdapter(activity_);
		}
		bListView.setAdapter(unitAdapter);
		bListView.setOnItemLongClickListener(null);
		unitAdapter.setData(unitData);
		setWindowHeight();
	}

	private void initRooms() {
		if (roomAdapter == null) {
			roomAdapter = new LuaSearchRoomAdapter(activity_);
		}
		bListView.setAdapter(roomAdapter);
		bListView.setOnItemLongClickListener(null);
		roomAdapter.setData(roomData);
		setWindowHeight();
	}
	public void setWindowHeight() {
		// 设置全屏
		WindowManager.LayoutParams p = treeDialog.getWindow().getAttributes();
		p.width = p.FILL_PARENT;
		p.height = p.FILL_PARENT;
		treeDialog.getWindow().setAttributes(p);
	}
	private void readList(String listValue, String symbol) {
		if (listValue != null &&!"".equals(listValue)) {
			String a[] = listValue.split(symbol);
			for (int i = 0; i < a.length; i++) {
				choose_kData.add(a[i]);
			}
		}
	}
	private void removeMoreThan() {
		for (int i = 0; i < choose_kData.size(); i++) {
			for (int j = choose_kData.size() - 1; j > i; j--) {
				if (choose_kData.get(i).equals(choose_kData.get(j))) {
					choose_kData.remove(j);
				}
			}
		}
	}
	public void cleanJson() {
		if (jsonStr != null) {
			jsonStr = "";
		}
		if (choose_kData != null && !choose_kData.isEmpty()) {
			choose_kData.clear();
		}
	}
	private void addData(){
		if(!choose_kData.isEmpty()){
			choose_kData.clear();
		}
		//
		if(buildAdapter != null){
			if(buildAdapter.selectedValue != null && !buildAdapter.selectedValue.isEmpty()){
				Iterator iter_key = buildAdapter.selectedValue.entrySet().iterator();	
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_kData.add((String)val);
				}		
			}
		}
		//
		if(unitAdapter != null){
			if(unitAdapter.selectedValue != null && !unitAdapter.selectedValue.isEmpty()){
				Iterator iter_key = unitAdapter.selectedValue.entrySet().iterator();	
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_kData.add((String)val);
				}		
			}
		}
		//
		if(roomAdapter != null){
			if(roomAdapter.selectedValue != null && !roomAdapter.selectedValue.isEmpty()){
				Iterator iter_key = roomAdapter.selectedValue.entrySet().iterator();	
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_kData.add((String)val);
				}		
			}
		}
	}
}
