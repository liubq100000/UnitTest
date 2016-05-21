
package com.dangdang.unit.keeper.persistent.mq.vo;

public class ConnVO {
	private String ip;

	private String port;

	public ConnVO(String ip, String port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public String getUrl() {
		return getIp() + ":" + getPort();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
