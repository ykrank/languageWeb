package com.rytong.emp.unit;

import java.io.Serializable;
import java.util.List;


public class SearchTreeChild implements Serializable {

	private static final long serialVersionUID = 1L;
	private String  value;
	private String key;
	private List<SearchTreeVo> list2;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<SearchTreeVo> getList2() {
		return list2;
	}
	public void setList2(List<SearchTreeVo> list2) {
		this.list2 = list2;
	}

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getBuilding_name() {
//		return building_name;
//	}
//
//	public void setBuilding_name(String building_name) {
//		this.building_name = building_name;
//	}
//
//	public List<SearchTreeVo> getUnit_list() {
//		return unit_list;
//	}
//
//	public void setUnit_list(List<SearchTreeVo> unit_list) {
//		this.unit_list = unit_list;
//	}

}
