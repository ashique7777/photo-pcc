/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.AdhocAndDailyCSVFileReportDataBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * @author CTS
 *
 */
public class LCAdhocAndDailyReportRowmapper implements RowMapper<AdhocAndDailyCSVFileReportDataBean>{

	public AdhocAndDailyCSVFileReportDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		AdhocAndDailyCSVFileReportDataBean adhocCSVFileReportDataBean = new AdhocAndDailyCSVFileReportDataBean();
			adhocCSVFileReportDataBean.setOrderDate(rs.getTimestamp("ORDERDATE"));
			adhocCSVFileReportDataBean.setQuantity(CommonUtil.bigDecimalToLong(rs.getString("QUANTITY")));
			adhocCSVFileReportDataBean.setProvider(rs.getString("PROVIDER"));
			adhocCSVFileReportDataBean.setLocationType(rs.getString("LOCATIONTYPE"));
			adhocCSVFileReportDataBean.setLocationNumb(CommonUtil.bigDecimalToLong(rs.getString("LOCATIONNUMBER")));
			adhocCSVFileReportDataBean.setCalRetailPrice(CommonUtil.bigDecimalToDouble(rs.getString("CALCULATEDRETAILPRICE")));
			adhocCSVFileReportDataBean.setDiscountApplied(CommonUtil.bigDecimalToDouble(rs.getString("DISCOUNTAPPLIED")));
			adhocCSVFileReportDataBean.setEmployeeDiscount((String) (rs.getString("EMPLOYEEDISCOUNT")));
			adhocCSVFileReportDataBean.setNetSale(CommonUtil.bigDecimalToDouble(rs.getString("NETSALE")));
			adhocCSVFileReportDataBean.setProductDescription((String) (rs.getString("PRODUCTDESCRIPTION")));
			adhocCSVFileReportDataBean.setuPC(rs.getString("UPC"));
			adhocCSVFileReportDataBean.setwIC(rs.getString("WIC"));
			adhocCSVFileReportDataBean.setLicenseContOrTempId(rs.getString("LICECONTORTEMPID"));
			adhocCSVFileReportDataBean.setEnvelopeNumber(CommonUtil.bigDecimalToLong(rs.getString("ENVELOPENUMBER")));
			adhocCSVFileReportDataBean.setOriginalRetailPrice(CommonUtil.bigDecimalToDouble(rs.getString("ORIGINALRETAILPRICE")));
			adhocCSVFileReportDataBean.setOrderStatus(rs.getString("ORDERSTATUS"));
			return adhocCSVFileReportDataBean;
		}


}
