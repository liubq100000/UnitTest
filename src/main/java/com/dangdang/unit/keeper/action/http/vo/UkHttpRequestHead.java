
package com.dangdang.unit.keeper.action.http.vo;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class UkHttpRequestHead {
	private Map<String, String> headAttributes = new HashMap<String, String>();

	public Map<String, String> getHeadAttributes() {
		return headAttributes;
	}

	public void setHeadAttributes(Map<String, String> headAttributes) {
		this.headAttributes = headAttributes;
	}

	public void put(String key, String value) {
		this.headAttributes.put(key, value);
	}
}
