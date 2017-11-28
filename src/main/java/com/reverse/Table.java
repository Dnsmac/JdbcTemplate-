package com.reverse;

import java.util.ArrayList;
import java.util.List;

public class Table {
	
	private String name;
	private String tableName;
	private String note;
	List<Field> fieldArray = new ArrayList();

	public List<Field> getFieldArray() {
		return fieldArray;
	}

	public void setFieldArray(List<Field> fieldArray) {
		this.fieldArray = fieldArray;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ", note=" + note + ", fieldArray=" + fieldArray + "]";
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
