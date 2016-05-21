
package com.dangdang.unit.keeper.action.http.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class UkHttpRequest {

	private String url;

	private UkHttpRequestHead head;

	private UkHttpRequestBody body;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UkHttpRequestHead getHead() {
		return head;
	}

	public void setHead(UkHttpRequestHead head) {
		this.head = head;
	}

	public UkHttpRequestBody getBody() {
		return body;
	}

	public void setBody(UkHttpRequestBody body) {
		this.body = body;
	}

}
