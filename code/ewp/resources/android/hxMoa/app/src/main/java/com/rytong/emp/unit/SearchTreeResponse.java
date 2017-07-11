package com.rytong.emp.unit;

import java.io.Serializable;
import java.util.List;


public class SearchTreeResponse implements Serializable {

	private static final long serialVersionUID = 1L;
//	private int code;
//	private String error_text;
	private List<SearchTreeChild> list1;
	public List<SearchTreeChild> getList1() {
		return list1;
	}
	public void setList1(List<SearchTreeChild> list1) {
		this.list1 = list1;
	}

//	public int getCode() {
//		return code;
//	}
//
//	public void setCode(int code) {
//		this.code = code;
//	}

//	public String getError_text() {
//		return error_text;
//	}
//
//	public void setError_text(String error_text) {
//		this.error_text = error_text;
//	}

//	public List<SearchTreeChild> getBuilding_list() {
//		return building_list;
//	}
//
//	public void setBuilding_list(List<SearchTreeChild> building_list) {
//		this.building_list = building_list;
//	}

}
