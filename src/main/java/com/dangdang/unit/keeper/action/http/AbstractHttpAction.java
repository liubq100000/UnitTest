
package com.dangdang.unit.keeper.action.http;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.unit.keeper.action.IAction;
import com.dangdang.unit.keeper.action.http.vo.UkHttpParser;
import com.dangdang.unit.keeper.action.http.vo.UkHttpResponse;
import com.dangdang.unit.keeper.action.http.vo.UkHttpParam;
import com.dangdang.unit.keeper.dto.UnitAction;
import com.dangdang.unit.keeper.verification.IVerifier;
import com.squareup.okhttp.Response;

/**
 * HTTP请求的公共基础类
 * 
 * @author liubq
 */
public abstract class AbstractHttpAction implements IAction {

	//日志
	private static final Logger logger = LoggerFactory.getLogger(AbstractHttpAction.class);
		
	//正在处理的Action
	private UnitAction nowAction;

	public final void action(UnitAction action) {
		UkHttpParam param = null;
		try {
			param = UkHttpParser.buildAction(action.getInnerXml());
		}
		catch (Exception ex) {
			throw new RuntimeException(" xml 结构不合法，xml:" + action.getInnerXml());
		}
		if (param == null) {
			throw new RuntimeException(" xml 结构转化为对象是空 ，xml:" + action.getInnerXml());
		}
		nowAction = action;
		doAction(param);
	}

	/**
	 * 执行
	 * @param param
	 */
	public abstract void doAction(UkHttpParam param);

	/**
	 * 对比返回值
	 * 
	 * @param okHttpResponse
	 * @param tunitResponse
	 * @throws Exception
	 */
	public void diffResponse(Response okHttpResponse, UkHttpResponse tunitResponse) throws Exception {
		if (okHttpResponse.isSuccessful()) {
			if (tunitResponse != null) {
				String className = tunitResponse.getValidby();
				IVerifier verifier = (IVerifier) Class.forName(className).newInstance();
				try {
					verifier.valid(tunitResponse.getValue(), okHttpResponse.body().string());
				}
				catch (Throwable ex) {
					logger.error("diffResponse",ex);
					throw new Exception("*^_^*Action=[" + nowAction.getName() + "]*^_^*处理异常>> " + ex.getMessage(), ex);
				}
			}
		}
		else {
			throw new IOException("Unexpected code " + okHttpResponse);
		}
	}
}
