
package com.dangdang.unit.keeper.action.sleep.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class UkSleepActionParam {

	private String param;

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
