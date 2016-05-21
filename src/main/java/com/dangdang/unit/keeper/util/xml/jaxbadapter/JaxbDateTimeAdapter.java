
package com.dangdang.unit.keeper.util.xml.jaxbadapter;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.dangdang.unit.keeper.util.UkDateTimeUtils;

public class JaxbDateTimeAdapter extends XmlAdapter<String, Date> {

	@Override
	public String marshal(Date date) throws Exception {
		return UkDateTimeUtils.date2str(date, UkDateTimeUtils.YMDHMS);
	}

	@Override
	public Date unmarshal(String str) throws Exception {
		return UkDateTimeUtils.str2Date(str, UkDateTimeUtils.YMDHMS);
	}
}
