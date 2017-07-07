package com.rytong.emp.unit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ghbank.moas.R;
import com.rytong.emp.test.lua.LuaUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 搜索单元适配
 * 
 * @author Austriee
 * 
 */
public class SearchUnitAdapter extends BaseAdapter {
	private List<SearchTreeVo> mList;
	private Context mContext;
	public static Map<Integer, Boolean> isSelected;
	public static Map<Integer , String> selectedKey;
	public static Map<Integer , String> selectedValue;
	public SearchUnitAdapter(Context context) {
		this.mContext = context;
	}

	public void setData(List<SearchTreeVo> list) {
		this.mList = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mList != null && mList.size() > 0) {
			return mList.size();
		} else {
			return 0;
		}
	}

	@Override
	public SearchTreeVo getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.listdialog_item, null);

			holder.content = (TextView) convertView
					.findViewById(R.id.pop_item_content);
			holder.choose = (CheckBox) convertView
					.findViewById(R.id.check_choose);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(isSelected  == null){
			//这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。    
	        isSelected = new HashMap<Integer, Boolean>();  
			for (int i = 0; i < mList.size(); i++) {    
	            isSelected.put(i, false);    
	        }
		}
		if(selectedKey == null){
			selectedKey = new HashMap<Integer, String>();
		}
		if(selectedValue == null){
			selectedValue = new HashMap<Integer, String>();
		}
		holder.choose.setChecked(isSelected.get(position));
		holder.content.setText(mList.get(position).getValue());
		holder.choose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isSelected.get(position)) {
					isSelected.put(position, false);		
					selectedKey.remove(position);
					selectedValue.remove(position);
				} else if (!isSelected.get(position)) {
					isSelected.put(position, true);
					selectedKey.put(position, mList.get(position).getValue());
					selectedValue.put(position, mList.get(position).getKey());
				}
			}
		});
		return convertView;
	}
	private class ViewHolder {
		TextView content;
		CheckBox choose;
	}
}
