package com.reverse;

/**
 * 数据库表的字段属性
 * fieldName:字段名
 * type:属性
 * @author Aaron
 * @since 1.0
 *
 */
public class Field {
	
	private String fieldName;
	private String type;
	private String note;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "Field [fieldName=" + fieldName + ", type=" + type + ", note=" + note + "]";
	}
	
}
