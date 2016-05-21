
package com.dangdang.unit.keeper.action.http.vo;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class UkHttpRequestBody {
	private Map<String, String> bodyAttributes = new HashMap<String, String>();

	public Map<String, String> getBodyAttributes() {
		return bodyAttributes;
	}

	public void setBodyAttributes(Map<String, String> bodyAttributes) {
		this.bodyAttributes = bodyAttributes;
	}

	public void put(String key, String value) {
		this.bodyAttributes.put(key, value);
	}

	public String get(String key) {
		return this.bodyAttributes.get(key);
	}

}
