
package com.dangdang.unit.keeper.tool.parser;

import org.jdom.Element;

import com.dangdang.unit.keeper.dto.UnitAction;
import com.dangdang.unit.keeper.tool.UkDataSetBuilder;
import com.dangdang.unit.keeper.util.UkStringUtils;
import com.dangdang.unit.keeper.util.UkXmlUtils;

public class UkXMLActionParser {

	public UnitAction buildAction(Element actionXml) throws Exception {
		UnitAction action = new UnitAction();
		action.setName(actionXml.getAttributeValue("name"));
		if (UkStringUtils.isEmpty(action.getName())) {
			throw new Exception("name can't be null");
		}
		action.setClassName(actionXml.getAttributeValue("class"));
		if (UkStringUtils.isEmpty(action.getClassName())) {
			throw new Exception("class can't be null");
		}

		//准备数据部分
		Element paramXml = actionXml.getChild("param");
		String paramXmlStr = UkDataSetBuilder.elementToXml(paramXml);
		if (UkStringUtils.isEmpty(paramXmlStr)) {
			throw new Exception("param can't be null");
		}
		String innerXml = UkXmlUtils.formatXmlStr(paramXmlStr);
		action.setInnerXml(innerXml);
		return action;
	}
}
