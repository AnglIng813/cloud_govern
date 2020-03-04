package com.casic.cloud.hyperloop.autocoding.common;

import java.util.List;

public class Table {
	private String name;
	private String dbName;
	private String classRemark;
	private List<Column> columns;
	private List<Column> pkColumns;
	
	public String getName() {
		return name;
	}
	public String getNameUpper() {
		return name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getClassRemark() {
		return classRemark;
	}

	public void setClassRemark(String classRemark) {
		this.classRemark = classRemark;
	}

	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public List<Column> getPkColumns() {
		return pkColumns;
	}
	public void setPkColumns(List<Column> pkColumns) {
		this.pkColumns = pkColumns;
	}


}
