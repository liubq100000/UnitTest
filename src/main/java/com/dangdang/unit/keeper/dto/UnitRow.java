
package com.dangdang.unit.keeper.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UnitRow {

	@XmlElement(name = "column")
	private List<UnitAttribute> attributes;

	public List<UnitAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<UnitAttribute> attributes) {
		this.attributes = attributes;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
