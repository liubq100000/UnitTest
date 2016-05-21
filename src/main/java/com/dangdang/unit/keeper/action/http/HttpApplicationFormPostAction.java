
package com.dangdang.unit.keeper.action.http;

import java.util.Map;

import com.dangdang.unit.keeper.action.http.vo.UkHttpRequest;
import com.dangdang.unit.keeper.action.http.vo.UkHttpRequestBody;
import com.dangdang.unit.keeper.action.http.vo.UkHttpRequestHead;
import com.dangdang.unit.keeper.action.http.vo.UkHttpParam;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * 表单POST请求
 * 
 * @author liubq
 */
public class HttpApplicationFormPostAction extends AbstractHttpAction {

	public void doAction(UkHttpParam http) {
		try {
			post(http);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void post(UkHttpParam http) throws Exception {
		com.squareup.okhttp.Request.Builder requestBuilder = new Request.Builder();
		UkHttpRequest tunitRequest = http.getRequest();
		UkHttpRequestHead head = tunitRequest.getHead();
		if (head != null) {
			for (Map.Entry<String, String> entry : head.getHeadAttributes().entrySet()) {
				requestBuilder.addHeader(entry.getKey(), entry.getValue());
			}
		}
		FormEncodingBuilder bodyBuilder = new FormEncodingBuilder();
		UkHttpRequestBody body = tunitRequest.getBody();
		for (Map.Entry<String, String> entry : body.getBodyAttributes().entrySet()) {
			bodyBuilder.add(entry.getKey(), entry.getValue());
		}
		RequestBody formBody = bodyBuilder.build();
		Request request = requestBuilder.url(tunitRequest.getUrl()).post(formBody).build();
		OkHttpClient client = new OkHttpClient();
		Response response = client.newCall(request).execute();
		//对比返回值和预期返回值
		diffResponse(response, http.getResponse());
	}
}
