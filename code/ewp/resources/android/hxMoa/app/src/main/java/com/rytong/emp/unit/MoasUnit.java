package com.rytong.emp.unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.ghbank.moas.R;
import com.google.gson.Gson;
import com.rytong.emp.gui.atom.InputText;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyCharacterMap.KeyData;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MoasUnit extends InputText {
	Element element = null;
	/** 背景长度 */
	public static int width_ = 0;
	/** 背景高度 */
	int heght_ = 0;
	Context context_;
	private View dialogView, search_btitle;
	private List<SearchTreeChild> treeData = new ArrayList<SearchTreeChild>();
	private List<SearchTreeVo> unitData = new ArrayList<SearchTreeVo>();
	private List<TreeVo> roomData = new ArrayList<TreeVo>();
	private MoasCustomDialog treeDialog;
	private ListView bListView;
	private TextView title;
	private SearchBuildingAdapter buildAdapter;
	private SearchRoomAdapter roomAdapter;
	private SearchUnitAdapter unitAdapter;
	private SelectWorkAdapter workAdapter;
	private Activity activity_;
	private int Tag = -1;
	private String put_ = "", titile_ = "", jsonStr = "";
	public static StringBuffer strBuff = null;
	public static StringBuffer callBackBuff = null;// 显示值
	public static StringBuffer valueBuff = null;// 给后台的返回值
	public static List<String> choose_keyData = new ArrayList<String>();
	public static List<String> choose_ValueData = new ArrayList<String>();
	public static boolean BACKFLAG_ = false;
	public boolean showChoose = true;
	public static int choose_build = 0;
	public static int choose_unit = 0;
	public static int choose_room = 0;
	private TextView back_, finish_;

	public MoasUnit(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context_ = context;
		setEnabled(false);
		activity_ = (Activity) context;
		strBuff = new StringBuffer();
		callBackBuff = new StringBuffer();
		valueBuff = new StringBuffer();
	}

	@Override
	public boolean setPropertyByName(String arg0, String arg1) {
		// TODO Auto-generated method stub
		if ("showValue".equals(arg0)) {
			setText(arg1);
		}
		return super.setPropertyByName(arg0, arg1);
		
	}

	@Override
	public void init(Element arg0) {
		// TODO Auto-generated method stub
		super.init(arg0);
		// 后台给的分隔符
		if (arg0.getAttribute("symbol") != null
				&& !"".equals(arg0.getAttribute("symbol"))) {
			put_ = arg0.getAttribute("symbol");
		}
		// 标题
		if (arg0.getAttribute("title") != null
				&& !"".equals(arg0.getAttribute("title"))) {
			titile_ = arg0.getAttribute("title");
		}
		// json
		if (arg0.getAttribute("json") != null
				&& !"".equals(arg0.getAttribute("json"))) {
			jsonStr = arg0.getAttribute("json");
			readJsonStr();// 读取Json
		}
	}

	public Element getElement() {
		return mElement;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 返回给后台值
			String value_ = mElement.getAttribute("value1");
			if (value_ != null && !"".equals(value_)) {
				String a[] = value_.split(put_);
				for (int i = 0; i < a.length; i++) {
					choose_ValueData.add(a[i]);
				}
			}
			// 显示在界面的值
			String arg0 = mElement.getAttribute("showValue");
			if (arg0 != null && !"".equals(arg0)) {
				readList(arg0, put_);
			}
			//

			MoasCustomDialog.Builder builder = new MoasCustomDialog.Builder(
					context_);
			dialogView = activity_.getLayoutInflater().inflate(
					R.layout.listview_dialog, null);

			title = (TextView) dialogView.findViewById(R.id.custom_title);
			search_btitle = dialogView.findViewById(R.id.search_btitle);
			back_ = (TextView) dialogView.findViewById(R.id.custom_back);
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
								choose_keyData.remove(choose_keyData.size() - 1);
								choose_ValueData.remove(choose_ValueData.size() - 1);
							}
							choose_build = 0;
						}
						initWork();
						break;

					case 0:
						Tag = -1;
						if (choose_unit != 0) {
							for (int i = 0; i < choose_unit; i++) {
								choose_keyData.remove(choose_keyData.size() - 1);
								choose_ValueData.remove(choose_ValueData.size() - 1);
							}
							choose_unit = 0;
						}
						initBuilds();
						break;

					case 1:
						Tag = 0;
						if (choose_room != 0) {
							for (int i = 0; i < choose_room; i++) {
								choose_keyData.remove(choose_keyData.size() - 1);
								choose_ValueData.remove(choose_ValueData.size() - 1);
							}
							choose_room = 0;
						}
						initUnits();
						break;
					case 2:
						if (treeDialog != null && treeDialog.isShowing()) {
							// 返回显示
							String text = null;
							if (!choose_keyData.isEmpty()) {
								for (int i = 0; i < choose_keyData.size(); i++) {
									callBackBuff.append(choose_keyData.get(i)
											+ put_);
								}
								callBackBuff.deleteCharAt(callBackBuff.length() - 1);
								text = callBackBuff.toString();
								callBackBuff.delete(0, callBackBuff.length());
							} else {
								text = "";
							}
							mElement.setAttribute("showValue", text);
							setText(text);
							// 返回给后台值
							if (!choose_ValueData.isEmpty()) {
								for (int i = 0; i < choose_ValueData.size(); i++) {
									valueBuff.append(choose_ValueData.get(i)
											+ put_);
								}
								valueBuff.deleteCharAt(valueBuff.length() - 1);
								mElement.setAttribute("value1",
										valueBuff.toString());
								valueBuff.delete(0, valueBuff.length());
							}
							cleanJson();
							treeDialog.dismiss();
						}
						break;
					default:
						break;
					}
				}
			});
			finish_ = (TextView) dialogView.findViewById(R.id.custom_finish);
			finish_.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					removeMoreThan();
					addData();
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
											title.setText(titile_);
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
											title.setText(titile_);
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
			bListView = (ListView) dialogView
					.findViewById(R.id.buildings_listview);
			treeDialog = builder.listDialog(dialogView);
			treeDialog.show();
			initWork();
			Tag = 2;
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
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

	private void initBuilds() {
		if (buildAdapter == null) {
			buildAdapter = new SearchBuildingAdapter(activity_);
		}
		bListView.setAdapter(buildAdapter);
		bListView.setOnItemLongClickListener(null);
		buildAdapter.setData(treeData);
		title.setText(titile_);
		setWindowHeight();
	}

	private void initUnits() {
		if (unitAdapter == null) {
			unitAdapter = new SearchUnitAdapter(activity_);
		}
		bListView.setAdapter(unitAdapter);
		bListView.setOnItemLongClickListener(null);
		unitAdapter.setData(unitData);
		setWindowHeight();
	}

	private void initRooms() {
		if (roomAdapter == null) {
			roomAdapter = new SearchRoomAdapter(activity_);
		}
		bListView.setAdapter(roomAdapter);
		bListView.setOnItemLongClickListener(null);
		roomAdapter.setData(roomData);
		setWindowHeight();
	}

	private void initWork() {
		removeMoreThan();
		if (workAdapter == null) {
			workAdapter = new SelectWorkAdapter(activity_);
		}
		title.setText("已选列表");
		finish_.setText("添加");
		Tag = 2;
		bListView.setAdapter(workAdapter);
		workAdapter.setData(choose_keyData);
		// 已选列表--长按删除选项
		bListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				choose_keyData.remove(arg2);
				choose_ValueData.remove(arg2);
				initWork();
				return false;
			}
		});
		setWindowHeight();
	}

	public void setWindowHeight() {
		// 设置全屏
		WindowManager.LayoutParams p = treeDialog.getWindow().getAttributes();
		p.width = p.FILL_PARENT;
		p.height = p.FILL_PARENT;
		treeDialog.getWindow().setAttributes(p);
	}

	private void readList(String listStr, String symbol) {
		if (listStr != null) {
			String a[] = listStr.split(symbol);
			for (int i = 0; i < a.length; i++) {
				choose_keyData.add(a[i]);
			}
		}
	}

	private void removeMoreThan() {
		for (int i = 0; i < choose_keyData.size(); i++) {
			for (int j = choose_keyData.size() - 1; j > i; j--) {
				if (choose_keyData.get(i).equals(choose_keyData.get(j))) {
					choose_keyData.remove(j);
					choose_ValueData.remove(j);
				}
			}
		}
	}

	public void cleanJson() {
		if (jsonStr != null) {
			jsonStr = "";
		}
		if (!choose_keyData.isEmpty()) {
			choose_keyData.clear();
		}
		if (!choose_ValueData.isEmpty()) {
			choose_ValueData.clear();
		}
	}
	private void addData(){
		if(!choose_keyData.isEmpty()){
			choose_keyData.clear();
		}
		if(!choose_ValueData.isEmpty()){
			choose_ValueData.clear();
		}
		//
		if(buildAdapter != null){
			if(buildAdapter.selectedKey != null && !buildAdapter.selectedKey.isEmpty()){
				Iterator iter_key = buildAdapter.selectedKey.entrySet().iterator();	
				
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_keyData.add((String)val);
				}				
			}
			if(buildAdapter.selectedValue != null && !buildAdapter.selectedValue.isEmpty()){
				Iterator iter_key = buildAdapter.selectedValue.entrySet().iterator();	
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_ValueData.add((String)val);
				}		
			}
		}
		//
		if(unitAdapter != null){
			if(unitAdapter.selectedKey != null && !unitAdapter.selectedKey.isEmpty()){
				Iterator iter_key = unitAdapter.selectedKey.entrySet().iterator();	
				
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_keyData.add((String)val);
				}				
			}
			if(unitAdapter.selectedValue != null && !unitAdapter.selectedValue.isEmpty()){
				Iterator iter_key = buildAdapter.selectedValue.entrySet().iterator();	
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_ValueData.add((String)val);
				}		
			}
		}
		if(roomAdapter != null){
			if(roomAdapter.selectedKey != null && !roomAdapter.selectedKey.isEmpty()){
				Iterator iter_key = roomAdapter.selectedKey.entrySet().iterator();	
				
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_keyData.add((String)val);
				}				
			}
			if(roomAdapter.selectedValue != null && !roomAdapter.selectedValue.isEmpty()){
				Iterator iter_key = roomAdapter.selectedValue.entrySet().iterator();	
				while (iter_key.hasNext()) {
					Map.Entry entry = (Map.Entry) iter_key.next();
					Object val = entry.getValue();
					choose_ValueData.add((String)val);
				}		
			}
		}
	}
}
