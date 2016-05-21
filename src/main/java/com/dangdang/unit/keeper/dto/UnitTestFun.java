
package com.dangdang.unit.keeper.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "testfun")
public class UnitTestFun {

	private List<UnitTestCase> testcases;

	private List<UnitAction> actions;

	public List<UnitTestCase> getTestcases() {
		return testcases;
	}

	public void setTestcases(List<UnitTestCase> testcases) {
		this.testcases = testcases;
	}

	public List<UnitAction> getActions() {
		return actions;
	}

	public void setActions(List<UnitAction> actions) {
		this.actions = actions;
	}

}
