/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.POFOrderVCRepBean;

/**
 * @author CTS
 *
 */
public class POFIrsRowmapper implements RowMapper<POFOrderVCRepBean > {
	
	@Override
	public POFOrderVCRepBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		POFOrderVCRepBean  pofVCRepBean = new POFOrderVCRepBean();
		
		pofVCRepBean.setLocationNumber(rs.getInt("LOCATION_NBR"));
		pofVCRepBean.setLocationType(rs.getString("LOCATION_TYPE"));
		pofVCRepBean.setOrderId(rs.getInt("Order_Id"));
		pofVCRepBean.setProdId(rs.getInt("Product_Id"));
		pofVCRepBean.setQuantity(rs.getInt("Quantity"));
		pofVCRepBean.setEnvQuantity(rs.getString("Envelope_Quantity"));
		pofVCRepBean.setVendorNumber(rs.getString("Vendor_Id"));
		pofVCRepBean.setTimeDoneSoldDttm(rs.getDate("Time_Done_Sold_Dttm"));
		pofVCRepBean.setVendPaymentAmt(rs.getDouble("Item_Vendor_Cost"));
		pofVCRepBean.setSoldAmount(rs.getDouble("Item_Sold_Amt"));
		pofVCRepBean.setEdiUpc(rs.getString("Upc_Edi"));
		pofVCRepBean.setProductDiscription(rs.getString("DESCRIPTION"));
		pofVCRepBean.setProductWIC(rs.getString("WIC"));
		pofVCRepBean.setProductUPC(rs.getString("UPC"));
			
			
		return pofVCRepBean;

	}
}
