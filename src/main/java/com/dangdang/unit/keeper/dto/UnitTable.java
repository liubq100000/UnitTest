
package com.dangdang.unit.keeper.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UnitTable {

	@XmlAttribute
	private String tableName;

	@XmlAttribute
	private List<String> excludedColumns;

	@XmlAttribute
	private List<String> includedColumns;

	@XmlAttribute
	private String orderBy;

	@XmlElement(name = "row")
	private List<UnitRow> rows;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getIncludedColumns() {
		return includedColumns;
	}

	public void setIncludedColumns(List<String> includedColumns) {
		this.includedColumns = includedColumns;
	}

	public List<String> getExcludedColumns() {
		return excludedColumns;
	}

	public void setExcludedColumns(List<String> excludedColumns) {
		this.excludedColumns = excludedColumns;
	}

	public List<UnitRow> getRows() {
		return rows;
	}

	public void setRows(List<UnitRow> rows) {
		this.rows = rows;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
