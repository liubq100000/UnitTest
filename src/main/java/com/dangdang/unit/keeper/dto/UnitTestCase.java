
package com.dangdang.unit.keeper.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class UnitTestCase {
	@XmlAttribute
	private String name;

	@XmlAttribute
	private String action;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	List<UnitPrepareData> prepareDataSet;

	public List<UnitPrepareData> getPrepareDataSet() {
		return prepareDataSet;
	}

	public void setPrepareDataSet(List<UnitPrepareData> prepareDataSet) {
		this.prepareDataSet = prepareDataSet;
	}

	List<UnitResultData> resultDataSet;

	public List<UnitResultData> getResultDataSet() {
		return resultDataSet;
	}

	public void setResultDataSet(List<UnitResultData> resultDataSet) {
		this.resultDataSet = resultDataSet;
	}
}
