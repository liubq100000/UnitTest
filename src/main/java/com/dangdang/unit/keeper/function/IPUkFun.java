
package com.dangdang.unit.keeper.function;

import java.net.InetAddress;
import java.text.SimpleDateFormat;

public class IPUkFun extends AbstractUkFunction {

	public static final String FUN_NAME = "IP()";

	public static final String FUN_NAME1 = "IP";

	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public FunResult doAction(UkFunctionContext context, String value) {
		String valueStr = value.trim();
		if (FUN_NAME.equalsIgnoreCase(valueStr) || FUN_NAME1.equalsIgnoreCase(valueStr)) {
			//获得本机IP
			String ip;
			try {
				InetAddress addr = InetAddress.getLocalHost();
				ip = addr.getHostAddress();
			}
			catch (Exception ex) {
				ip = "127.0.0.1";
			}
			return new FunResult(ip, false);
		}
		//没有匹配上继续下面测处理
		return new FunResult(value, true);
	}
}
