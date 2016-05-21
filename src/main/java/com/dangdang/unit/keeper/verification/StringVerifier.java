
package com.dangdang.unit.keeper.verification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringVerifier implements IVerifier {
	//日志
	private static final Logger logger = LoggerFactory.getLogger(StringVerifier.class);
	
	/** 
	 * 验证
	 */
	public void valid(String expText, String actText) throws Exception {
		String expectValueStr = expText == null ? "" : expText.trim();
		logger.info("expStringText:\r\n{}", expectValueStr);
		String actValueStr = actText == null ? "" : actText.trim();
		logger.info("actStringText:\r\n{}", actValueStr);
		org.junit.Assert.assertEquals(expectValueStr, actValueStr);
	}
}
