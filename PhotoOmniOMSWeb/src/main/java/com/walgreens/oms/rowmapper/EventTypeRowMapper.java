package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.json.bean.EventData;


public class EventTypeRowMapper implements RowMapper<EventData> {
	@Override
	public EventData mapRow(ResultSet rs, int rowNum) throws SQLException {
		EventData eventTypeBean = new EventData();
		eventTypeBean.setSignsId(CommonUtil.bigDecimalToLong(rs.getString("SYS_EVENT_ID")));
		eventTypeBean.setSignName(rs.getString("EVENT_NAME"));
		return eventTypeBean;
	}

}