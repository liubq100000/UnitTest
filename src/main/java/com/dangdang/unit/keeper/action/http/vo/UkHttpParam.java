
package com.dangdang.unit.keeper.action.http.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class UkHttpParam {
	private UkHttpRequest request;

	private UkHttpResponse response;

	public UkHttpRequest getRequest() {
		return request;
	}

	public void setRequest(UkHttpRequest request) {
		this.request = request;
	}

	public UkHttpResponse getResponse() {
		return response;
	}

	public void setResponse(UkHttpResponse response) {
		this.response = response;
	}

}
