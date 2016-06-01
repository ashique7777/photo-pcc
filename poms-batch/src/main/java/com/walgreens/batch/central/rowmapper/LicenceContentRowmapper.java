/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.LicenceContentBean;

/**
 * @author CTS
 *
 */
public class LicenceContentRowmapper implements RowMapper<LicenceContentBean>{
	
public LicenceContentBean mapRow(ResultSet rs, int rowNum) throws SQLException {	
		
		
		LicenceContentBean licenceContentBean = new LicenceContentBean();
		
		licenceContentBean.setOrderDate(rs.getString("PF_ORDER_DTTM"));
		licenceContentBean.setQuantity(rs.getString("PF_QUANTITY"));
		licenceContentBean.setProvider(rs.getString("PF_PROVIDER"));
		licenceContentBean.setLocationType(rs.getString("PF_LOCATION_TYPE"));
		licenceContentBean.setLocationNumber(rs.getString("PF_LOCATION_NBR"));
		licenceContentBean.setRetail(rs.getString("PF_ORIGINAL_RETAIL"));
		licenceContentBean.setDiscountApplied(rs.getString("PF_TOTAL_DISCOUNT_AMT"));
		licenceContentBean.setEmployeeDiscount(rs.getString("PF_EMPLOYEE_DISCOUNT"));
		licenceContentBean.setNetSale(rs.getString("PF_NET_SALE"));
		licenceContentBean.setProductID(rs.getString("PF_PRODUCT_ID"));
		licenceContentBean.setUPC(rs.getString("PF_UPC"));
		licenceContentBean.setWIC(rs.getString("PF_WIC"));
		licenceContentBean.setProductDescription(rs.getString("PF_LONG_DESC"));
		licenceContentBean.setTemplateID(rs.getString("PF_LICENSED_CONTENT_ID"));
		licenceContentBean.setEnvelopeNumber(rs.getString("PF_ENVELOPE_NBR"));
		return licenceContentBean;
	}

}
