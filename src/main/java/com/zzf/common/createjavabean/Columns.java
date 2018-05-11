package com.zzf.common.createjavabean;

public class Columns {
	
	/** 字段名 */
	public String columnName;
	
	/** 格式化后的字段名 */
	public String formatColumnName;

	/** 字段类型 */
	public String dataType;

	/** 字段注释 */
	public String columnComment;

	/** 是否主键 */
	public boolean isKey=false;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getFormatColumnName() {
		return formatColumnName;
	}

	public void setFormatColumnName(String formatColumnName) {
		this.formatColumnName = formatColumnName;
	}

	public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
	
}
