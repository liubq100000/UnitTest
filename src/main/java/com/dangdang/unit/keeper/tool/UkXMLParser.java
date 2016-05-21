
package com.dangdang.unit.keeper.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.dangdang.unit.keeper.dto.UnitAction;
import com.dangdang.unit.keeper.dto.UnitTestCase;
import com.dangdang.unit.keeper.dto.UnitTestFun;
import com.dangdang.unit.keeper.tool.parser.UkXMLActionParser;
import com.dangdang.unit.keeper.tool.parser.UkXMLTestCaseParser;

public final class UkXMLParser {

	public static UkXMLParser getInstance() {
		return new UkXMLParser();
	}

	public UnitTestFun parse(String xmlFile) throws Exception {
		return parse(new File(xmlFile));
	}

	@SuppressWarnings("rawtypes")
	public UnitTestFun parse(File xmlFile) throws Exception {
		SAXBuilder builder = new SAXBuilder(false);
		Document doc = builder.build(xmlFile);
		Element root = doc.getRootElement();
		UnitTestFun fun = new UnitTestFun();

		//每个action
		List actionXmls = root.getChildren("action");
		Set<String> actionNameSet = new HashSet<String>();
		if (actionXmls != null && actionXmls.size() > 0) {
			List<UnitAction> actions = new ArrayList<UnitAction>();
			UkXMLActionParser actionParser = new UkXMLActionParser();
			UnitAction action;
			for (Object actionObj : actionXmls) {
				Element actionXml = (Element) actionObj;
				action = actionParser.buildAction(actionXml);
				if (action != null) {
					actions.add(action);
					if (!actionNameSet.add(action.getName())) {
						throw new Exception("*^_^*^_^* " + action.getName() + "名称重复，请检查xml文件。");
					}
				}
			}
			fun.setActions(actions);
		}

		//每个case
		List<UnitTestCase> testcases = new ArrayList<UnitTestCase>();
		List caseXmls = root.getChildren("testcase");
		if (caseXmls != null && caseXmls.size() > 0) {
			UkXMLTestCaseParser testCaseParser = new UkXMLTestCaseParser();
			UnitTestCase stockTestCase;
			for (Object caseObj : caseXmls) {
				Element caseXml = (Element) caseObj;
				stockTestCase = testCaseParser.buildTestCase(caseXml);
				if (stockTestCase != null) {
					if (!actionNameSet.contains(stockTestCase.getAction())) {
						throw new Exception(stockTestCase.getAction() + " not exists");
					}
					testcases.add(stockTestCase);
				}
			}
			fun.setTestcases(testcases);
		}
		return fun;
	}

}
