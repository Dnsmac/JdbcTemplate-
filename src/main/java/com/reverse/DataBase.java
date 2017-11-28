package com.reverse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBase {
	
	List<Table> tableArray = new ArrayList();
	
	
	List<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
	
	
	
	public List<HashMap<String, String>> getMap() {
		return map;
	}

	public void setMap(List<HashMap<String, String>> map) {
		this.map = map;
	}

	public List<Table> getTableArray() {
		return tableArray;
	}

	public void setTableArray(List<Table> tableArray) {
		this.tableArray = tableArray;
	}

	@Override
	public String toString() {
		return "DataBase [tableArray=" + tableArray + "]";
	}
}
