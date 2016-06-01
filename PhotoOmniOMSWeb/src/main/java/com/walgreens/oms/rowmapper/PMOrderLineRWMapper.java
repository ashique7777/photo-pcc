/**
 * 
 */
package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.PMOrderLineBean;

/**
 * @author CTS
 *
 */
public class PMOrderLineRWMapper implements RowMapper<PMOrderLineBean> {

	@Override
	public PMOrderLineBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		PMOrderLineBean itemBean = new PMOrderLineBean();
		itemBean.setOrderId(rs.getInt("ORDER_ID"));		
		itemBean.setOrderLineId(rs.getInt("ORDER_LINE_ID"));
		itemBean.setProductId(rs.getInt("PRODUCT_ID"));		
		itemBean.setQuantity(rs.getInt("QUANTITY"));
		itemBean.setFinalPrice(rs.getInt("FINAL_PRICE"));		
		itemBean.setSetsQty(rs.getInt("SETS_QTY"));
		return itemBean;
	}

}
