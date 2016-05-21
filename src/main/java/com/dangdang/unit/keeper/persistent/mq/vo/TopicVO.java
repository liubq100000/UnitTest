
package com.dangdang.unit.keeper.persistent.mq.vo;

public class TopicVO {

	private String group;

	private String instanceName;

	private String topic;

	private String tags;

	public TopicVO() {

	}

	public TopicVO(String group, String instanceName, String topic, String tags) {
		super();
		this.group = group;
		this.instanceName = instanceName;
		this.topic = topic;
		this.tags = tags;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "TopicVO [group=" + group + ", instanceName=" + instanceName + ", topic=" + topic + ", tags=" + tags + "]";
	}

}
