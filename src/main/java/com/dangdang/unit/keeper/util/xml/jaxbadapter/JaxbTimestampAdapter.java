
package com.dangdang.unit.keeper.util.xml.jaxbadapter;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.dangdang.unit.keeper.util.UkDateTimeUtils;

/**
 * jaxb 注解 属性值，用于时间 （YMD）格式的转换
 * 
 * @author hugangjs2
 *
 */
public class JaxbTimestampAdapter extends XmlAdapter<String, Timestamp> {

	@Override
	public Timestamp unmarshal(String v) throws Exception {
		return new Timestamp(UkDateTimeUtils.str2Date(v, UkDateTimeUtils.YMDHMS).getTime());
	}

	@Override
	public String marshal(Timestamp v) throws Exception {
		return UkDateTimeUtils.date2str(new Date(v.getTime()), UkDateTimeUtils.YMDHMS);
	}

}
