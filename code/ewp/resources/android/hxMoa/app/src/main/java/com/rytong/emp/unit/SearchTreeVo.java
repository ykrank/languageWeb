package com.rytong.emp.unit;

import java.io.Serializable;
import java.util.List;


public class SearchTreeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	private List<TreeVo> list3;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<TreeVo> getList3() {
		return list3;
	}
	public void setList3(List<TreeVo> list3) {
		this.list3 = list3;
	}


//	public String getUnit_name() {
//		return unit_name;
//	}
//
//	public void setUnit_name(String unit_name) {
//		this.unit_name = unit_name;
//	}
//
//	public List<TreeVo> getRoom_nmber_list() {
//		return room_nmber_list;
//	}
//
//	public void setRoom_nmber_list(List<TreeVo> room_nmber_list) {
//		this.room_nmber_list = room_nmber_list;
//	}

}
