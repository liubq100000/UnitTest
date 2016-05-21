
package com.dangdang.unit.keeper.verification;

import net.javacrumbs.jsonunit.JsonAssert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * JSON格式数据验证
 * 
 * @author liubq
 */
public class JSONVerifier implements IVerifier {
	//日志
	private static final Logger logger = LoggerFactory.getLogger(JSONVerifier.class);
	
	/**
	 * 验证
	 */
	public void valid(String expJsonText, String actJsonText) throws Exception {
		String expectValueStr = expJsonText == null ? "" : expJsonText.trim();
		logger.info("expJsonText:\r\n{}", expectValueStr);
		String actValueStr = actJsonText == null ? "" : actJsonText.trim();
		logger.info("actJsonText:\r\n{}", actValueStr);
		JsonAssert.assertJsonEquals(expectValueStr, actValueStr);
	}
}
