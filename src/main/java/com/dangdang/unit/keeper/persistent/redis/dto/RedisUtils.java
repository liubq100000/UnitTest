
package com.dangdang.unit.keeper.persistent.redis.dto;

import com.dangdang.unit.keeper.dto.UnitAttribute;
import com.dangdang.unit.keeper.dto.UnitRow;
import com.dangdang.unit.keeper.util.UkStringUtils;

public class RedisUtils {

	public static String getKey(UnitRow row) {
		if (row == null || row.getAttributes() == null) {
			return null;
		}
		for (UnitAttribute att : row.getAttributes()) {
			if (RedisVO.KEY.equalsIgnoreCase(att.getName())) {
				return att.getValue();
			}
		}
		return null;
	}

	public static RedisSortSetRow getSortSetRow(UnitRow row) {
		RedisSortSetRow rRow = null;
		RedisVO vo = getCommonEntry(row);
		if (vo != null) {
			rRow = new RedisSortSetRow();
			rRow.setKey(vo.getKey());
			rRow.setValue(vo.getValue());
			try {
				rRow.setScore(Double.valueOf(vo.getScore()));
			}
			catch (Exception ex) {
				rRow.setScore(1D);
			}

		}
		return rRow;
	}

	public static RedisRow getRow(UnitRow row) {
		RedisRow rRow = null;
		RedisVO vo = getCommonEntry(row);
		if (vo != null) {
			rRow = new RedisRow();
			rRow.setKey(vo.getKey());
			rRow.setValue(vo.getValue());
		}
		return rRow;
	}

	public static RedisHashRow getHashRow(UnitRow row) {
		RedisHashRow rRow = null;
		RedisVO vo = getCommonEntry(row);
		if (vo != null) {
			rRow = new RedisHashRow();
			rRow.setKey(vo.getKey());
			rRow.setField(vo.getField());
			rRow.setValue(vo.getValue());
		}
		return rRow;
	}

	public static RedisVO getCommonEntry(UnitRow row) {
		if (row == null || row.getAttributes() == null) {
			return null;
		}
		RedisVO entry = new RedisVO();
		for (UnitAttribute att : row.getAttributes()) {
			if (RedisVO.KEY.equalsIgnoreCase(att.getName())) {
				entry.setKey(att.getValue());
			}
			else if (RedisVO.FIELD.equalsIgnoreCase(att.getName())) {
				entry.setField(att.getValue());
			}
			else if (RedisVO.VALUE.equalsIgnoreCase(att.getName())) {
				entry.setValue(att.getValue());
			}
			else if (RedisVO.SCORE.equalsIgnoreCase(att.getName())) {
				entry.setScore(att.getValue());
			}
		}
		if (!UkStringUtils.isEmpty(row.getValue())) {
			entry.setValue(row.getValue());
		}
		return entry;
	}
}
