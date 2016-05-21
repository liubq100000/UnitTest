package com.dangdang.ztest.tcase;

import java.io.File;

import org.junit.Test;

import com.dangdang.ztest.common.AbstractRedisTestCase;

/**
 * 示例测试程序
 * 
 * @author liubq
 */
public class SimpleRedisTest extends AbstractRedisTestCase {

    @Test
    public void testRedisXml() throws Exception {
	String path = System.getProperty("user.dir") + "/src/main/resources/simple/simple_redis.xml";
	testByFile(new File(path));
    }

}
