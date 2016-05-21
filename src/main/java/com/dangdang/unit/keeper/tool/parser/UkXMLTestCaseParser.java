
package com.dangdang.unit.keeper.tool.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.dangdang.unit.keeper.dto.UnitAttribute;
import com.dangdang.unit.keeper.dto.UnitPrepareData;
import com.dangdang.unit.keeper.dto.UnitResultData;
import com.dangdang.unit.keeper.dto.UnitRow;
import com.dangdang.unit.keeper.dto.UnitTable;
import com.dangdang.unit.keeper.dto.UnitTestCase;
import com.dangdang.unit.keeper.util.UkListUtils;
import com.dangdang.unit.keeper.util.UkStringUtils;

public class UkXMLTestCaseParser {

	@SuppressWarnings("rawtypes")
	public UnitTestCase buildTestCase(Element testcaseXml) throws JDOMException, IOException {
		UnitTestCase testCase = new UnitTestCase();
		testCase.setName(testcaseXml.getAttributeValue("name"));
		testCase.setAction(testcaseXml.getAttributeValue("action"));
		//准备数据部分
		Element preparedatasetXml  = testcaseXml.getChild("preparedataset");
		if (preparedatasetXml != null) {
			List preparedataXmls = preparedatasetXml.getChildren("preparedata");
			if (preparedataXmls != null && preparedataXmls.size() > 0) {
				List<UnitPrepareData> prepareDataSet = new ArrayList<UnitPrepareData>();
				UnitPrepareData data;
				for (Object preparedataObj : preparedataXmls) {
					Element preparedataXml = (Element) preparedataObj;
					data = buildPrepareData(preparedataXml);
					if (data != null) {
						prepareDataSet.add(data);
					}
				}
				testCase.setPrepareDataSet(prepareDataSet);
			}
		}

		//验证数据部分
		Element resultdatasetXml = testcaseXml.getChild("resultdataset");
		if (resultdatasetXml != null) {
			List resultdataXmls = resultdatasetXml.getChildren("resultdata");
			if (resultdataXmls != null && resultdataXmls.size() > 0) {
				List<UnitResultData> resultDataSet = new ArrayList<UnitResultData>();
				UnitResultData data;
				for (Object resultdataObj : resultdataXmls) {
					Element resultdataXml = (Element) resultdataObj;
					data = buildResultData(resultdataXml);
					if (data != null) {
						resultDataSet.add(data);
					}
				}
				testCase.setResultDataSet(resultDataSet);
			}
		}
		return testCase;
	}

	private UnitPrepareData buildPrepareData(Element preparedataXml) {
		UnitPrepareData prepareData = new UnitPrepareData();
		prepareData.setStoreKey(preparedataXml.getAttributeValue("storekey"));
		List<UnitTable> dataSet = buildDataSet(preparedataXml);
		prepareData.setDataSet(dataSet);
		return prepareData;
	}

	private UnitResultData buildResultData(Element resultdataXml) {
		UnitResultData resultData = new UnitResultData();
		resultData.setStoreKey(resultdataXml.getAttributeValue("storekey"));
		//DataSet赋值
		List<UnitTable> dataSet = buildDataSet(resultdataXml);
		resultData.setDataSet(dataSet);
		return resultData;
	}

	@SuppressWarnings("rawtypes")
	private List<UnitTable> buildDataSet(Element stockdataXml) {
		//DataSet赋值
		List<UnitTable> dataSet = new ArrayList<UnitTable>();
		Element datasetXml = stockdataXml.getChild("dataset");
		if (datasetXml == null) {
			return dataSet;
		}
		Set<String> set = new HashSet<String>();
		List tableXmls = datasetXml.getChildren();
		UnitTable table;
		for (Object tabObj : tableXmls) {
			Element tableXml = (Element) tabObj;
			table = getTableInfo(tableXml);
			if (table != null) {
				if (!set.add(table.getTableName())) {
					throw new RuntimeException("Table is duplicate In Same DataSet ");
				}
				dataSet.add(table);
			}

		}
		return dataSet;
	}

	@SuppressWarnings("rawtypes")
	private UnitTable getTableInfo(Element tableXml) {
		//卫语句
		if (tableXml == null) {
			return null;
		}
		UnitTable table = new UnitTable();
		//表名
		table.setTableName(tableXml.getName());

		//排序字段
		Attribute orderbyXml = tableXml.getAttribute("orderby");
		if (orderbyXml != null) {
			String orderBy = orderbyXml.getValue();
			if (orderBy != null && orderBy.length() > 0) {
				table.setOrderBy(orderBy);
			}
		}

		//排除列
		Attribute excludedcolumnXml = tableXml.getAttribute("excludedcolumn");
		if (excludedcolumnXml != null) {
			String columns = excludedcolumnXml.getValue();
			if (columns != null && columns.length() > 0) {
				List<String> list = new ArrayList<String>();
				list.addAll(Arrays.asList(columns.split(",")));
				table.setExcludedColumns(list);
			}
		}
		//所有行
		List rowXmls = tableXml.getChildren("row");
		List<UnitRow> rows = new ArrayList<UnitRow>();
		UnitRow row;
		for (Object rowObj : rowXmls) {
			Element rowXml = (Element) rowObj;
			row = getRowInfo(rowXml);
			if (row != null) {
				rows.add(row);
			}

		}
		if (rows != null && rows.size() > 0) {
			table.setRows(rows);
			//包含项，采用第一行的列
			UnitRow firstRow = table.getRows().get(0);
			List<String> includedColumns = new ArrayList<String>();
			for (UnitAttribute attribute : firstRow.getAttributes()) {
				includedColumns.add(attribute.getName());
			}
			table.setIncludedColumns(includedColumns);
		}
		return table;
	}

	@SuppressWarnings("rawtypes")
	private UnitRow getRowInfo(Element rowXml) {
		List<UnitAttribute> attributes = new ArrayList<UnitAttribute>();
		UnitAttribute attribute;
		List attributeXmls = rowXml.getAttributes();
		for (Object attributeObj : attributeXmls) {
			Attribute attributeXml = (Attribute) attributeObj;
			attribute = new UnitAttribute();
			attribute.setName(attributeXml.getName());
			attribute.setValue(attributeXml.getValue());
			attributes.add(attribute);
		}
		UnitRow row = new UnitRow();
		row.setAttributes(attributes);
		row.setValue(rowXml.getText());
		if (UkListUtils.isEmpty(row.getAttributes()) && UkStringUtils.isEmpty(row.getValue())) {
			return null;
		}
		return row;
	}
}
