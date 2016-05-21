
package com.dangdang.unit.keeper.persistent.redis.type;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.ComparisonFailure;

import com.dangdang.unit.keeper.util.UkListUtils;
import com.dangdang.unit.keeper.util.UkMapList;
import com.dangdang.unit.keeper.util.UkMapMap;
import com.dangdang.unit.keeper.util.UkStringUtils;

/**
 * 判断是否相等
 * 
 * @author liubq
 */
public class RedisAsserts {

	/**
	 * 判断是否相等
	 * 
	 * @param preMsg 前缀提示语
	 * @param expect 预期的数据
	 * @param act 实际的数据
	 */
	public static void assertEqual(String preMsg, UkMapMap<String, String, String> inExpect, UkMapMap<String, String, String> inAct) {
		UkMapMap<String, String, String> expect = inExpect == null ? new UkMapMap<String, String, String>() : inExpect;
		UkMapMap<String, String, String> act = inAct == null ? new UkMapMap<String, String, String>() : inAct;
		if (expect.size() != act.size()) {
			String expectMsg = UkStringUtils.join(expect.keySet());
			String actMsg = UkStringUtils.join(act.keySet());
			throw new ComparisonFailure(preMsg + "-->个数不同", expectMsg, actMsg);
		}
		String key;
		Map<String, String> actMap;
		Map<String, String> expectMap;
		for (Map.Entry<String, Map<String, String>> entry : expect.entrySet()) {
			key = entry.getKey();
			StringBuilder msg = new StringBuilder();
			msg.append(preMsg).append("-->Key:").append("[").append(key).append("],");
			if (!act.containsKey(key)) {
				msg.append("Key差异");
				throw new ComparisonFailure(msg.toString(), entry.getKey(), "不包含");
			}
			expectMap = entry.getValue();
			actMap = act.get(key);
			if (isEmpty(expectMap) && isEmpty(actMap)) {
				continue;
			}
			else if (!isEmpty(expectMap) && isEmpty(actMap)) {
				msg.append("Field列表个数差异");
				throw new ComparisonFailure(msg.toString(), "非空", "空");
			}
			else if (isEmpty(expectMap) && !isEmpty(actMap)) {
				msg.append("Field列表个数差异");
				throw new ComparisonFailure(msg.toString(), "空", "非空");
			}
			else if (expectMap.size() != actMap.size()) {
				msg.append("Field列表个数差异");
				String expectMsg = UkStringUtils.join(expectMap.keySet());
				String actMsg = UkStringUtils.join(actMap.keySet());
				throw new ComparisonFailure(msg.toString(), expectMsg, actMsg);
			}
			for (Map.Entry<String, String> fieldEntry : expectMap.entrySet()) {
				String actValue = actMap.get(fieldEntry.getKey());
				actValue = actValue == null ? "" : actValue;
				String expectValue = fieldEntry.getValue() == null ? "" : fieldEntry.getValue();
				if (!expectValue.equals(actValue)) {
					msg.append("Field:[").append(fieldEntry.getKey()).append("]值内容差异");
					throw new ComparisonFailure(msg.toString(), expectValue, actValue);
				}
			}
		}
	}

	/**
	 * 判空
	 * 
	 * @param map
	 * @return
	 */
	private static boolean isEmpty(Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否相等
	 * 
	 * @param preMsg 前缀提示语
	 * @param expect 预期的数据
	 * @param act 实际的数据
	 */
	public static void assertEqualIgnoreSort(String preMsg, UkMapList<String, String> inExpect, UkMapList<String, String> inAct) {
		UkMapList<String, String> expect = inExpect == null ? new UkMapList<String, String>() : inExpect;
		UkMapList<String, String> act = inAct == null ? new UkMapList<String, String>() : inAct;
		if (expect.size() != act.size()) {
			String expectMsg = UkStringUtils.join(expect.keySet());
			String actMsg = UkStringUtils.join(act.keySet());
			throw new ComparisonFailure(preMsg + "-->个数不同", expectMsg, actMsg);
		}
		String key;
		List<String> actList;
		List<String> expectList;
		for (Map.Entry<String, List<String>> entry : expect.entrySet()) {
			key = entry.getKey();
			StringBuilder msg = new StringBuilder();
			msg.append(preMsg).append("-->Key:").append("[").append(key).append("],");
			if (!act.containsKey(key)) {
				msg.append("Key差异");
				throw new ComparisonFailure(msg.toString(), entry.getKey(), "不包含");
			}
			expectList = entry.getValue();
			actList = act.get(key);
			if (UkListUtils.isEmpty(expectList) && UkListUtils.isEmpty(actList)) {
				continue;
			}
			else if (UkListUtils.isNotEmpty(expectList) && UkListUtils.isEmpty(actList)) {
				msg.append("值列表个数差异");
				throw new ComparisonFailure(msg.toString(), "非空", "空");
			}
			else if (UkListUtils.isEmpty(expectList) && UkListUtils.isNotEmpty(actList)) {
				msg.append("值个数差异");
				throw new ComparisonFailure(msg.toString(), "空", "非空");
			}
			else if (expectList.size() != actList.size()) {
				msg.append("值个数差异");
				String expectMsg = UkStringUtils.join(expectList);
				String actMsg = UkStringUtils.join(actList);
				throw new ComparisonFailure(msg.toString(), expectMsg, actMsg);
			}
			//排序
			Collections.sort(expectList);
			Collections.sort(actList);
			int len = expectList.size();
			for (int i = 0; i < len; i++) {
				String actValue = actList.get(i);
				actValue = actValue == null ? "" : actValue;
				String expectValue = expectList.get(i);
				expectValue = expectValue == null ? "" : expectValue;
				if (!expectValue.equals(actValue)) {
					msg.append("值内容差异");
					throw new ComparisonFailure(msg.toString(), expectValue, actValue);
				}
			}
		}
	}
}
