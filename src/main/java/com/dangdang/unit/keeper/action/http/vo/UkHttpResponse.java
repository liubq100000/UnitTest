
package com.dangdang.unit.keeper.action.http.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class UkHttpResponse {

	@XmlAttribute
	private String validby;

	@XmlValue
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValidby() {
		return validby;
	}

	public void setValidby(String validby) {
		this.validby = validby;
	}
}
