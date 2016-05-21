
package com.dangdang.unit.keeper.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UnitData {

	@XmlAttribute
	private String storeKey;

	public String getStoreKey() {
		return storeKey;
	}

	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}

	@XmlElement(name = "table")
	private List<UnitTable> dataSet;

	public List<UnitTable> getDataSet() {
		return dataSet;
	}

	public void setDataSet(List<UnitTable> dataSet) {
		this.dataSet = dataSet;
	}

}
