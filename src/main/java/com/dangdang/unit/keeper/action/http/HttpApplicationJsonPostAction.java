
package com.dangdang.unit.keeper.action.http;

import com.dangdang.unit.keeper.action.http.vo.UkHttpRequest;
import com.dangdang.unit.keeper.action.http.vo.UkHttpRequestBody;
import com.dangdang.unit.keeper.action.http.vo.UkHttpParam;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
/**
 * JSON格式POST请求
 * 
 * @author liubq
 */
public class HttpApplicationJsonPostAction extends AbstractHttpAction {
	//格式
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	@Override
	public void doAction(UkHttpParam http) {
		try {
			post(http);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void post(UkHttpParam http) throws Exception {
		UkHttpRequest tunitRequest = http.getRequest();
		UkHttpRequestBody body = tunitRequest.getBody();
		RequestBody requestBody = RequestBody.create(JSON, body.get("content"));
		Request request = new Request.Builder().url(tunitRequest.getUrl()).post(requestBody).build();
		OkHttpClient client = new OkHttpClient();
		Response response = client.newCall(request).execute();
		//对比返回值和预期返回值
		diffResponse(response, http.getResponse());
	}
}
