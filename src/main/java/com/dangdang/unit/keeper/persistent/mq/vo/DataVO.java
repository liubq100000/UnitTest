
package com.dangdang.unit.keeper.persistent.mq.vo;

import com.dangdang.unit.keeper.dto.UnitAttribute;
import com.dangdang.unit.keeper.dto.UnitRow;

public class DataVO extends TopicVO {

	private String key;

	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTopicKey() {
		return "TopicVO [group=" + this.getGroup() + ", instanceName=" + this.getInstanceName() + ", topic=" + this.getTopic() + ", tags=" + this.getTags() + "]";
	}

	/**
	 * 构建数据对象
	 * 
	 * @param uRow
	 * 
	 * @return 数据对象
	 */
	public static DataVO assembleTopicVO(UnitRow uRow) {
		//		String group = row String instanceName, String topic, String tags
		DataVO data = new DataVO();
		if (uRow.getAttributes() != null) {
			for (UnitAttribute att : uRow.getAttributes()) {
				if ("group".equals(att.getName())) {
					data.setGroup(att.getValue());
				}
				else if ("instanceName".equals(att.getName())) {
					data.setInstanceName(att.getValue());
				}
				else if ("topic".equals(att.getName())) {
					data.setTopic(att.getValue());
				}
				else if ("tags".equals(att.getName())) {
					data.setTags(att.getValue());
				}
				else if ("key".equals(att.getName())) {
					data.setKey(att.getValue());
				}
			}
		}
		data.setValue(uRow.getValue());
		return data;

	}

}
