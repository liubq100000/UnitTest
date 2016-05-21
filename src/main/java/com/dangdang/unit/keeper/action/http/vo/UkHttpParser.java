
package com.dangdang.unit.keeper.action.http.vo;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.dangdang.unit.keeper.util.UkStringUtils;
import com.dangdang.unit.keeper.util.UkXmlUtils;

/**
 * 主要为了兼容，现在已经有人按照旧的格式写了，所以这里还是解析方式吧。
 * 其实可以采用Jackon方式,更加优秀
 * 但是map在那种方式下表达格式略显复杂。
 * 
 * @author liubq
 */
public class UkHttpParser {
	//	<?xml version="1.0" encoding="utf-8"?>
	//	<root>
	//	 <param>
	//	  <request>
	//	   <url>http://127.0.0.1:8080/resteasy/rest/userservice/add</url>
	//	   <body>
	//	    <content>{"name":"linda","code":"623425","mail":"163242@ms.com","age":23,"sex":"female"}</content>
	//	   </body>
	//	  </request>
	//	  <response validby="com.dangdang.unit.keeper.verification.JSONVerifier">{"errCode":"0"}</response>
	//	 </param>
	//	</root>
	/**
	 * 解析
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static UkHttpParam buildAction(String xml) throws Exception {
		Document doc = UkXmlUtils.xml2Doc2(xml);
		Element root = doc.getRootElement();
		Element param = root.getChild("param");
		return buildHttpActionParam(param);
	}

	private static UkHttpParam buildHttpActionParam(Element paramXml) throws Exception {
		UkHttpParam param = new UkHttpParam();
		Element requestXml = paramXml.getChild("request");
		if (requestXml != null) {
			UkHttpRequest request = buildHttpRequest(requestXml);
			if (request != null) {
				param.setRequest(request);
			}
		}
		Element responseXml = paramXml.getChild("response");
		if (responseXml != null) {
			UkHttpResponse response = buildHttpResponse(responseXml);
			if (response != null) {
				param.setResponse(response);
			}
		}
		return param;
	}

	@SuppressWarnings("rawtypes")
	private static UkHttpRequest buildHttpRequest(Element requestXml) throws Exception {
		UkHttpRequest param = new UkHttpRequest();
		//url
		Element urlXml = requestXml.getChild("url");
		String url = urlXml.getText();
		if (UkStringUtils.isEmpty(url)) {
			throw new Exception("url is null");
		}
		else {
			param.setUrl(url);
		}
		//head
		UkHttpRequestHead head = new UkHttpRequestHead();
		Element headXml = requestXml.getChild("head");
		if (headXml != null) {
			List headAttributeXmls = headXml.getChildren();
			if (headAttributeXmls != null && headAttributeXmls.size() > 0) {
				for (Object headAttributeObj : headAttributeXmls) {
					Element headAttributeXml = (Element) headAttributeObj;
					head.put(headAttributeXml.getName(), headAttributeXml.getText());
				}
				param.setHead(head);
			}
		}
		//body
		UkHttpRequestBody body = new UkHttpRequestBody();
		Element bodyXml = requestXml.getChild("body");
		if (bodyXml != null) {
			List bodyAttributeXmls = bodyXml.getChildren();
			if (bodyAttributeXmls != null && bodyAttributeXmls.size() > 0) {
				for (Object bodyAttributeObj : bodyAttributeXmls) {
					Element headAttributeXml = (Element) bodyAttributeObj;
					body.put(headAttributeXml.getName(), headAttributeXml.getText());
				}
				param.setBody(body);
			}
		}
		return param;
	}

	private static UkHttpResponse buildHttpResponse(Element responseXml) throws Exception {
		UkHttpResponse response = new UkHttpResponse();
		response.setValidby(responseXml.getAttributeValue("validby"));
		if (UkStringUtils.isEmpty(response.getValidby())) {
			throw new Exception("valid by cat't be null");
		}
		response.setValue(responseXml.getText());
		return response;
	}

}
