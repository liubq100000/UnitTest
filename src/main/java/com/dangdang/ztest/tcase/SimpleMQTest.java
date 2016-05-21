
package com.dangdang.ztest.tcase;

import org.junit.Test;

import com.dangdang.ztest.common.AbstractRocketMQTestCase;

/**
 * 示例测试程序
 * 
 * @author liubq
 */
public class SimpleMQTest extends AbstractRocketMQTestCase {

	/**
	 * 文件位置
	 * 
	 * @return
	 */
	private String getXmlPath() {
		return System.getProperty("user.dir") + "/src/main/resources/simple/Simple_RocketMQ.xml";
	}

	@Test
	public void testRocketMQ() throws Exception {
		AbstractRocketMQTestCase.clearAfterTestCaseEnd = false;
		testByFile(getXmlPath());
	}
}
