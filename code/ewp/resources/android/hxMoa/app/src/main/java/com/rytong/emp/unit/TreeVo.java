package com.rytong.emp.unit;

import java.io.Serializable;

public class TreeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String value;
	private String key;
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
	
//	public int getHouse_id() {
//		return house_id;
//	}
//
//	public void setHouse_id(int house_id) {
//		this.house_id = house_id;
//	}
//
//	public String getRoom_nmber() {
//		return room_nmber;
//	}
//
//	public void setRoom_nmber(String room_nmber) {
//		this.room_nmber = room_nmber;
//	}

}
