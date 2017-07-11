package com.rytong.emp.lua.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ghbank.moas.R;
import com.rytong.emp.test.lua.LuaUtil;
import com.rytong.emp.unit.TreeVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 搜索门号适配
 * 
 * @author Austriee
 * 
 */
public class LuaSearchRoomAdapter extends BaseAdapter {
	private List<TreeVo> mList;
	private Context mContext;
	public static List<String> delList;
	public static Map<Integer, Boolean> isSelected;
	public static Map<Integer , String> selectedValue;
	public LuaSearchRoomAdapter(Context context) {
		this.mContext = context;
		delList = new ArrayList<String>();
	}

	public void setData(List<TreeVo> list) {
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
	public TreeVo getItem(int position) {
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
		if (isSelected == null) {
			// 这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mList.size(); i++) {
				isSelected.put(i, false);
			}
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
				if(isSelected.get(position)){
					isSelected.put(position, false);
					selectedValue.remove(position);
				}else if(!isSelected.get(position)){
					isSelected.put(position, true);
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
