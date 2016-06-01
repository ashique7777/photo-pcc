package com.walgreens.oms.rowmapper;

	import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.oms.bean.OrderlinePluBean;

	public class OrderLinePluRowMapper implements RowMapper<OrderlinePluBean> {

		@Override
		public OrderlinePluBean mapRow(ResultSet rs, int rowNum) throws SQLException { 
			OrderlinePluBean orderLinePluBean = new OrderlinePluBean(); 
			orderLinePluBean.setOrderPlaceddttm(rs.getString("ORDERPLACEDDTTM"));
			orderLinePluBean.setDiscountAmount(rs.getString("PLU_DISCOUNT_AMOUNT"));
			orderLinePluBean.setSysPluId(Long.parseLong(rs.getString("SYS_PLU_ID")));
			orderLinePluBean.setActiveCd(rs.getLong("ACTIVE_CD"));
			orderLinePluBean.setSysOrderLineid(rs.getLong("SYS_ORDER_LINE_ID"));
			orderLinePluBean.setSysOrderLinePluId(rs.getLong("SYS_OL_PLU_ID"));
			orderLinePluBean.setPluNumber(rs.getString("PLU_NBR"));
			
			return orderLinePluBean;
		}

	}

