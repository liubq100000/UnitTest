
package com.dangdang.unit.keeper.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAction {

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String className;

	private String innerXml;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getInnerXml() {
		return innerXml;
	}

	public void setInnerXml(String xml) {
		this.innerXml = xml;
	}

}
