
package com.dangdang.unit.keeper.persistent.mq.tool;

import java.util.List;
import java.util.Map;

import junit.framework.ComparisonFailure;

import com.dangdang.unit.keeper.persistent.mq.vo.TopicVO;
import com.dangdang.unit.keeper.util.UkMapList;
import com.dangdang.unit.keeper.util.UkStringUtils;

/**
 * 判定
 * 
 * @author liubq
 */
public class RocketMQAsserts {
	/**
	 * 判断是否相等
	 * 
	 * @param preMsg 前缀提示语
	 * @param expect 预期的数据
	 * @param act 实际的数据
	 */
	public static void assertEqual(String preMsg, UkMapList<String, String> inExpect, UkMapList<String, String> inAct) {
		UkMapList<String, String> expect = inExpect == null ? new UkMapList<String, String>() : inExpect;
		UkMapList<String, String> act = inAct == null ? new UkMapList<String, String>() : inAct;
		if (expect.size() != act.size()) {
			String expectMsg = UkStringUtils.join(expect.keySet());
			String actMsg = UkStringUtils.join(act.keySet());
			throw new ComparisonFailure(preMsg + "-->个数不同", expectMsg, actMsg);
		}
		String key;
		List<String> actValues;
		List<String> expectValues;
		for (Map.Entry<String, List<String>> entry : expect.entrySet()) {
			key = entry.getKey();
			StringBuilder msg = new StringBuilder();
			msg.append(preMsg).append("-->Key:").append("[").append(key).append("],");
			if (!act.containsKey(key)) {
				msg.append("Key差异");
				throw new ComparisonFailure(msg.toString(), entry.getKey(), "不包含");
			}
			expectValues = entry.getValue();
			actValues = act.get(key);
			if (isEmpty(expectValues) && isEmpty(actValues)) {
				continue;
			}
			else if (!isEmpty(expectValues) && isEmpty(actValues)) {
				msg.append("值个数差异");
				throw new ComparisonFailure(msg.toString(), "非空", "空");
			}
			else if (isEmpty(expectValues) && !isEmpty(actValues)) {
				msg.append("值个数差异");
				throw new ComparisonFailure(msg.toString(), "空", "非空");
			}
			else if (expectValues.size() != actValues.size()) {
				msg.append("值个数差异");
				throw new ComparisonFailure(msg.toString(), String.valueOf(expectValues.size()), String.valueOf(actValues.size()));
			}
			else {
				int len = expectValues.size();
				String expValue;
				String actValue;
				for (int i = 0; i < len; i++) {
					expValue = expectValues.get(i) == null ? "" : expectValues.get(i).trim();
					actValue = actValues.get(i) == null ? "" : actValues.get(i).trim();
					if (!expValue.equals(actValue)) {
						msg.append("index=" + i + " 值差异");
						throw new ComparisonFailure(msg.toString(), expValue, actValue);
					}
				}
			}
		}
	}

	/**
	 * 判空
	 * 
	 * @param data
	 * @return
	 */
	private static boolean isEmpty(List<String> datas) {
		if (datas == null || datas.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判空
	 * 
	 * @param data
	 * @return
	 */
	private static boolean isEmpty(String data) {
		if (data == null || data.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * TOPIC 判断
	 * 
	 * @param data
	 */
	public static void assertTopic(TopicVO data) {
		if (data == null || isEmpty(data.getGroup()) || isEmpty(data.getInstanceName()) || isEmpty(data.getTopic()) || isEmpty(data.getTags())) {
			throw new RuntimeException("RocketMQ 的xml(consumer)数据格式 group,instanceName,topic,tags 字段不能为空！");
		}
	}
}
