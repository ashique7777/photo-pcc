package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OmProductDetailBean;

public class OmProductDetailBeanRowMapper implements RowMapper<OmProductDetailBean>{

	@Override
	public OmProductDetailBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		OmProductDetailBean omProductDetailBean = new OmProductDetailBean();
		
		omProductDetailBean.setSysProductId(rs.getLong("SYS_PRODUCT_ID"));
		omProductDetailBean.setUpc(rs.getString("UPC"));
		omProductDetailBean.setWic(rs.getString("WIC"));
		/** Changes for JIRA#624 starts here **/
		omProductDetailBean.setProductNbr(rs.getString("PRODUCT_NBR"));
		/** Changes for JIRA#624 ends here **/
		
		return omProductDetailBean;
	}

}
