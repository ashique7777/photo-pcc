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
public class POFDatRowmapper implements RowMapper<POFOrderVCRepBean > {
	
	@Override
	public POFOrderVCRepBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		POFOrderVCRepBean  pofVCRepBean = new POFOrderVCRepBean();
		pofVCRepBean.setLocationNumber(rs.getInt("LOCATION_NBR"));
		pofVCRepBean.setEnvNo(rs.getInt("ENVELOPE_NBR"));
		pofVCRepBean.setMarketVendorNumber(rs.getString("MARKET_VENDOR_NUMBER"));
		pofVCRepBean.setEdiUpc(rs.getString("EDI_UPC"));
		pofVCRepBean.setTimeDoneSoldDttm(rs.getDate("TIME_DONE_SOLD_DTTM"));
		pofVCRepBean.setQuantity(rs.getInt("QUANTITY"));
		pofVCRepBean.setSoldAmount(rs.getDouble("ITEM_SOLD_AMT"));
		pofVCRepBean.setVendPaymentAmt(rs.getDouble("ITEM_VENDOR_COST"));
		pofVCRepBean.setDeptNumber(rs.getString("DEPT_NUMBER"));
		return pofVCRepBean;
		
	}
	

}
