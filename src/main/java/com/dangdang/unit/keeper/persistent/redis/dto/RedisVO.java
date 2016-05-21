
package com.dangdang.unit.keeper.persistent.redis.dto;

/**
 * 全部属性对象
 * 
 * @author liubq
 */
public class RedisVO {

	/**
	 * 关键字
	 */
	public static final String KEY = "key";

	/**
	 * 值
	 */
	public static final String VALUE = "value";

	/**
	 * 属性
	 */
	public static final String FIELD = "field";

	/**
	 * 权重
	 */
	public static final String SCORE = "score";

	//关键字
	private String key;

	//属性
	private String field;

	//值
	private String value;

	//权重
	private String score;

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

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
