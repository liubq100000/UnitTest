
package com.dangdang.unit.keeper.persistent.redis.dto;

public class RedisSortSetRow extends RedisRow {
	public Double score;

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

}
