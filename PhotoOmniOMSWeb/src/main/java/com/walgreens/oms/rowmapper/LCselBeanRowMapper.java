package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.oms.bean.LCSelDataBean;

	public class LCselBeanRowMapper implements RowMapper<LCSelDataBean>{

		@Override
		public LCSelDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			LCSelDataBean lcSelDataBean = new LCSelDataBean();
			
			lcSelDataBean.setOrderlineId(rs.getLong("SYS_ORDER_LINE_ID"));
			lcSelDataBean.setOrderPlacedDttm(rs.getTimestamp("ORDER_PLACED_DTTM"));
			
			return lcSelDataBean;
		}

	}


