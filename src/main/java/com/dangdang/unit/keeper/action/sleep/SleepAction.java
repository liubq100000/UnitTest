
package com.dangdang.unit.keeper.action.sleep;

import com.dangdang.unit.keeper.action.IAction;
import com.dangdang.unit.keeper.action.sleep.vo.UkSleepActionParam;
import com.dangdang.unit.keeper.dto.UnitAction;
import com.dangdang.unit.keeper.util.xml.UkJaxbUtils;

public class SleepAction implements IAction {

	public void action(UnitAction action) {
		UkSleepActionParam param = null;
		try {
			param = UkJaxbUtils.xml2Object2(action.getInnerXml(), UkSleepActionParam.class);
		}
		catch (Exception ex) {
			throw new RuntimeException(" xml 结构不合法，xml:" + action.getInnerXml());
		}
		if (param == null) {
			throw new RuntimeException(" xml 结构转化为对象是空 ，xml:" + action.getInnerXml());
		}
		doAction(param);
	}

	public void doAction(UkSleepActionParam param) {
		int time = 1000 * 60;
		try {
			time = Integer.valueOf(param.getParam());
			Thread.sleep(time);
		}
		catch (Exception e) {
			try {
				Thread.sleep(time);
			}
			catch (Exception ex) {
			}
		}
	}

}
