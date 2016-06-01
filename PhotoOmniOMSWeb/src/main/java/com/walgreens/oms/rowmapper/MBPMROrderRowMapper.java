/**
 * 
 */
package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OrderDetailData;


/**
 * The following user defined RowMapper is used to get OM shopping cart details based on the query used
 * @author Cognizant
 *
 */
public class MBPMROrderRowMapper implements RowMapper<OrderDetailData>{

	@Override
	public OrderDetailData mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		OrderDetailData mbpmDataBean = new OrderDetailData();

		mbpmDataBean.setShoppingCartId(rs.getLong("SYS_SHOPPING_CART_ID"));
		mbpmDataBean.setPmStatus(rs.getString("PM_STATUS"));
		mbpmDataBean.setOwningLocationID(rs.getInt("OWNING_LOC_ID"));
		mbpmDataBean.setSysOrderId(rs.getInt("SYS_ORDER_ID"));
		mbpmDataBean.setStatus(rs.getString("STATUS"));		
		mbpmDataBean.setCouponInd(rs.getInt("COUPON_CD"));		
		mbpmDataBean.setDiscountCardUsedInd(rs.getInt("DISCOUNT_CARD_USED_CD"));
		mbpmDataBean.setOrderPlacedDttm(rs.getString("ORDER_PLACED_DTTM"));
		mbpmDataBean.setLbldEmployeeId(rs.getInt("LABELED_EMPLOYEE_ID"));
		mbpmDataBean.setFinalPrice(rs.getFloat("FINAL_PRICE"));
		mbpmDataBean.setExpenseInd(rs.getInt("EXPENSE_IND"));
		mbpmDataBean.setEmpId(rs.getLong("empId"));
		mbpmDataBean.setPmEligible(rs.getBoolean("PM_ELIGIBLE_CD"));
		mbpmDataBean.setSoldAmount(rs.getFloat("Sold_Amount"));
		mbpmDataBean.setEmployeeId(rs.getString("EMPLOYEE_ID"));
		
		
		return mbpmDataBean;
	}

}
