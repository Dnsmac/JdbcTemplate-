package com.reverse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class ReverseEngineering {
	
	private DataBase dataBase = new DataBase();
	
	
	public void execute(String url){
		readDataBaseTableInfo();
		createBeanFile(url);
//		createBeanFileImpl(url);
	}
	
	/**
	 * sql 拼接
	 * @param url
	 */
	private void createBeanFileImpl(String url) {
		
		for (int i = 0; i < dataBase.getTableArray().size(); i++) {

			Table table = dataBase.getTableArray().get(i);
			File file = new File(table.getName()+  "Impl.java");
			if (!file.exists()) {
				try {
					file.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					PrintWriter printWriter = new PrintWriter(fileOutputStream);
					String text = parseImpl(table, url);
					printWriter.write(text);
					printWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		
	}


	private String parseImpl(Table table, String packageUrl) {

		String simpleType = null;
		StringBuffer aimport = new StringBuffer();
		aimport.append("package " + packageUrl + ";\n\n");
		StringBuffer clazz = new StringBuffer();
		StringBuffer method = new StringBuffer();
		StringBuffer all = new StringBuffer();
		clazz.append("public class " + table.getName() + "Impl {\n");
		clazz.append("\n");
		
		List<Field> fieldArray = table.getFieldArray();
		HashMap<String, String> map = new HashMap();
		for (int i = 0; i < fieldArray.size(); i++) {

			Field field = fieldArray.get(i);
			if (field.getType().equals("VARCHAR")) {
				simpleType = TypeMapper.VARCHAR.getJavaSimpleType();
			} else if (field.getType().equals("INT")) {
				simpleType = TypeMapper.INT.getJavaSimpleType();
			} else if (field.getType().equals("TIMESTAMP")) {
				simpleType = TypeMapper.TIMESTAMP.getJavaSimpleType();
				map.put("TIMESTAMP", "import " + TypeMapper.TIMESTAMP.getJavaTypeName() + ";\n");
			} else if (field.getType().equals("DATETIME")) {
				simpleType = TypeMapper.DATETIME.getJavaSimpleType();
				map.put("DATETIME", "import " + TypeMapper.DATETIME.getJavaTypeName() + ";\n");
			} else if (field.getType().equals("TEXT")) {
				simpleType = TypeMapper.TEXT.getJavaSimpleType();
			} else if (field.getType().equals("INT UNSIGNED")) {
				simpleType = TypeMapper.INT_UNSIGNED.getJavaSimpleType();
			} else if (field.getType().equalsIgnoreCase("decimal")) {
				simpleType = TypeMapper.DECIMAL.getJavaSimpleType();
			}
			
			
			clazz.append("	private " + simpleType + " " + field.getFieldName() + ";	//" + field.getNote() + "\n");
			String param = field.getFieldName();
			method.append("    public void set" + toUpperCaseFirstOne(param) + "(" + simpleType + " " + param
					+ ") {this." + param + " = " + param + ";}\n\n");

			method.append("    public " + simpleType + " get" + toUpperCaseFirstOne(param) + "() {return this." + param
					+ ";}\n\n");
		}

		for (String key : map.keySet()) {
			String value = map.get(key);
			aimport.append(value);
		}

		clazz.append("\n");
		method.append("}");
		all.append(aimport.toString() + "\n" + clazz.toString()+method.toString());

		return all.toString();
	}

	/**
	 * 读取数据库中的表信息
	 */
	private void readDataBaseTableInfo(){
		
		List<Table> tableArray = dataBase.getTableArray();
		
		List<HashMap<String, String>> map = dataBase.getMap();
		
		Connection conn = JdbcUtil.getConnection();
		DatabaseMetaData databaseMeteData = null;
		try {
			databaseMeteData =  conn.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String[] types = {"TABLE"};
		ResultSet  tabs = null;
		try {
			tabs  = databaseMeteData.getTables(null, null, null, types);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			while(tabs.next()){  
				String tableName = tabs.getString("TABLE_NAME");
				String conversionTableNames = conversionTableNames(tableName);
				Table table = new Table();
				table.setName(conversionTableNames);
				table.setTableName(tableName);
				table.setNote(tabs.getString("REMARKS"));
				tableArray.add(table);
				
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put(conversionTableNames, tableName);
				map.add(hashMap);
			}   
		} catch (SQLException e) {
			e.printStackTrace();
		}    
		
		ResultSet COLUNM = null;
		for (int i = 0; i < tableArray.size(); i++) {
			Table table = tableArray.get(i);
			List<Field> fieldArray = table.getFieldArray();
			try {
				COLUNM = databaseMeteData.getColumns(conn.getCatalog(), "root", table.getTableName(), null);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				while(COLUNM.next()){    
					Field field = new Field();
					field.setFieldName(COLUNM.getString("COLUMN_NAME"));
					field.setType(COLUNM.getString("TYPE_NAME"));
					field.setNote(COLUNM.getString("REMARKS"));
					fieldArray.add(field);
				} 
	
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建bean文件
	 * @param url
	 */
	private void createBeanFile(String url) {

		for (int i = 0; i < dataBase.getTableArray().size(); i++) {

			Table table = dataBase.getTableArray().get(i);
			List<HashMap<String, String>> map = dataBase.getMap();
			System.out.println(map.get(i).get(""));
			
			
			File file = new File(table.getName() + ".java");
			if (!file.exists()) {
				try {
					file.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					PrintWriter printWriter = new PrintWriter(fileOutputStream);
					String text = parse(table, url);
					printWriter.write(text);
					printWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}
	
	/**
	 * 解析table信息
	 * @param table
	 * @param packageUrl
	 * @return
	 */
	private String parse(Table table, String packageUrl) {

		String simpleType = null;
		StringBuffer aimport = new StringBuffer();
		aimport.append("package " + packageUrl + ";\n\n");
		StringBuffer clazz = new StringBuffer();
		StringBuffer method = new StringBuffer();
		StringBuffer all = new StringBuffer();
		clazz.append("public class " + table.getName() + " {\n");
		clazz.append("\n");
		
		List<Field> fieldArray = table.getFieldArray();
		HashMap<String, String> map = new HashMap();
		for (int i = 0; i < fieldArray.size(); i++) {

			Field field = fieldArray.get(i);
			if (field.getType().equals("VARCHAR")) {
				simpleType = TypeMapper.VARCHAR.getJavaSimpleType();
			} else if (field.getType().equals("INT")) {
				simpleType = TypeMapper.INT.getJavaSimpleType();
			} else if (field.getType().equals("TIMESTAMP")) {
				simpleType = TypeMapper.TIMESTAMP.getJavaSimpleType();
				map.put("TIMESTAMP", "import " + TypeMapper.TIMESTAMP.getJavaTypeName() + ";\n");
			} else if (field.getType().equals("DATETIME")) {
				simpleType = TypeMapper.DATETIME.getJavaSimpleType();
				map.put("DATETIME", "import " + TypeMapper.DATETIME.getJavaTypeName() + ";\n");
			} else if (field.getType().equals("TEXT")) {
				simpleType = TypeMapper.TEXT.getJavaSimpleType();
			} else if (field.getType().equals("INT UNSIGNED")) {
				simpleType = TypeMapper.INT_UNSIGNED.getJavaSimpleType();
			} else if (field.getType().equalsIgnoreCase("decimal")) {
				simpleType = TypeMapper.DECIMAL.getJavaSimpleType();
			}
			
			
			clazz.append("	private " + simpleType + " " + field.getFieldName() + ";	//" + field.getNote() + "\n");
			String param = field.getFieldName();
			method.append("    public void set" + toUpperCaseFirstOne(param) + "(" + simpleType + " " + param
					+ ") {this." + param + " = " + param + ";}\n\n");

			method.append("    public " + simpleType + " get" + toUpperCaseFirstOne(param) + "() {return this." + param
					+ ";}\n\n");
		}

		for (String key : map.keySet()) {
			String value = map.get(key);
			aimport.append(value);
		}

		clazz.append("\n");
		method.append("}");
		all.append(aimport.toString() + "\n" + clazz.toString()+method.toString());

		return all.toString();

	}
	
	
	/**
	 * 首字母大写
	 * @param s
	 * @return
	 */
	private String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
	/**
	 * 删除以单字母开头的，形成符合规范的表名
	 * @param s
	 * @return
	 */
	private String conversionTableNames(String s) {
		if(s.charAt(1)=='_'){
			s = s.substring(2,s.length());
		}
		String[] array = s.split("_");
		String relust = "";
		for(int i=0;i<array.length;i++){
			array[i] = toUpperCaseFirstOne(array[i]);
			relust = relust+array[i];
		}
		
		return relust;
	}
	
	
	
}
