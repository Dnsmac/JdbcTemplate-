package com.reverse;

public enum TypeMapper {
	
	VARCHAR("VARCHAR","java.lang.String","String"),   
	INT("INT","java.lang.Integer","Integer"),
	INT_UNSIGNED("INT UNSIGNED","java.lang.Integer","Integer"),
	TIMESTAMP("TIMESTAMP","java.sql.Timestamp","Timestamp"),
	DATETIME("DATETIME","java.sql.Timestamp","Timestamp"),
	DECIMAL("DATETIME","java.math.BigDecimal","Long"),
	TEXT("TEXT","java.lang.String","String");
	
	private String DataBaseTypeName;
	private String javaTypeName;
	private String javaSimpleType;
	

	private TypeMapper(String dataBaseTypeName, String javaTypeName, String javaSimpleType) {
		DataBaseTypeName = dataBaseTypeName;
		this.javaTypeName = javaTypeName;
		this.javaSimpleType = javaSimpleType;
	}

	public String getDataBaseTypeName() {
		return DataBaseTypeName;
	}

	public void setDataBaseTypeName(String dataBaseTypeName) {
		DataBaseTypeName = dataBaseTypeName;
	}

	public String getJavaTypeName() {
		return javaTypeName;
	}

	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
	}

	public String getJavaSimpleType() {
		return javaSimpleType;
	}

	public void setJavaSimpleType(String javaSimpleType) {
		this.javaSimpleType = javaSimpleType;
	}
}
