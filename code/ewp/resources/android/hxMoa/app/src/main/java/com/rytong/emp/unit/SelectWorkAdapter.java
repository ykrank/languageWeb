package com.rytong.emp.unit;

import java.util.List;

import com.ghbank.moas.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SelectWorkAdapter extends BaseAdapter {
	private List<String> mList;
	private Context mContext;
	public SelectWorkAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}
	public void setData(List<String> list) {
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			//清空数据
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.listdialog_item, null);

			holder.content = (TextView) convertView
					.findViewById(R.id.pop_item_content);
			//已选列表不显示复选框
			holder.choose = (CheckBox) convertView
					.findViewById(R.id.check_choose);
			if(mList.isEmpty()){
				
			}else{
				holder.choose.setVisibility(View.GONE);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.content.setText(mList.get(position));
		return convertView;
	}
	private class ViewHolder {
		TextView content;
		CheckBox choose;
	}
}
