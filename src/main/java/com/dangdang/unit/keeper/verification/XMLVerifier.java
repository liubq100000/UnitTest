
package com.dangdang.unit.keeper.verification;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.unit.keeper.util.UkXmlUtils;

/**
 * XML格式数据验证
 * 
 * @author liubq
 */
public class XMLVerifier implements IVerifier {
	//日志
	private static final Logger logger = LoggerFactory.getLogger(XMLVerifier.class);

	/** 
	 * 验证
	 */
	public void valid(String expXmlText, String actXmlText) throws Exception {
		String expectValueStr = expXmlText;
		try {
			expectValueStr = UkXmlUtils.formatXmlStr(expXmlText.trim());
			logger.info("expXmlText:\r\n{}", expectValueStr);
		}
		catch (Exception ex) {
			logger.info("expXmlText:\r\n{},非标准的XML格式", expectValueStr, ex);
			throw ex;
		}
		String actValueStr = actXmlText;
		try {
			actValueStr = UkXmlUtils.formatXmlStr(actXmlText.trim());
			logger.info("actXmlText:\r\n{}", actValueStr);
		}
		catch (Exception ex) {
			logger.info("actXmlText:\r\n{},非标准的XML格式", actValueStr, ex);
			throw ex;
		}

		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLAssert.assertXMLEqual(expectValueStr, actValueStr);
	}
}
