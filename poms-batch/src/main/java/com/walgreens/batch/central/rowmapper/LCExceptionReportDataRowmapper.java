/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.ExceptionCSVFileReportDataBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * @author CTS
 *
 */
public class LCExceptionReportDataRowmapper implements RowMapper<ExceptionCSVFileReportDataBean>{

	public ExceptionCSVFileReportDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExceptionCSVFileReportDataBean dataBean = new ExceptionCSVFileReportDataBean();
			dataBean.setStore(CommonUtil.bigDecimalToLong((rs.getString("STORE"))));
			dataBean.setOrderID(CommonUtil.bigDecimalToLong((rs.getString("ORDER_ID"))));
			dataBean.setProductID(CommonUtil.bigDecimalToLong((rs.getString("PRODUCT_ID"))));
			dataBean.setProductDescription(rs.getString("PRODUCT_DESCRIPTION"));
			dataBean.setExceptionDate(rs.getTimestamp("EXCEPTION_DATE"));
			dataBean.setExceptionType((String)(rs.getString("EXCEPTION_TYPE")));
			dataBean.setExceptionDescription(rs.getString("EXCEPTION_DESCRIPTION"));
			dataBean.setRemarks(rs.getString("REMARKS"));
			dataBean.setDatetimeCreated(rs.getTimestamp("DATE_TIME_CREATED"));
			return dataBean;
		}


}
