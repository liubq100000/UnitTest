
package com.dangdang.unit.keeper.action.http;

import com.dangdang.unit.keeper.action.http.vo.UkHttpParam;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * GET请求
 * 
 * @author liubq
 */
public class HttpGetAction extends AbstractHttpAction {

	OkHttpClient client = new OkHttpClient();

	public void doAction(UkHttpParam http) {
		try {
			get(http);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void get(UkHttpParam http) throws Exception {
		Request request = new Request.Builder().url(http.getRequest().getUrl()).build();
		Response response = client.newCall(request).execute();
		//对比返回值和预期返回值
		diffResponse(response, http.getResponse());
	}

}
