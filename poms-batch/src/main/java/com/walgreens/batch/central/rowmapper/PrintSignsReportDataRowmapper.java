/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.PrintSignsCSVFileReportDataBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * @author CTS
 *
 */
public class PrintSignsReportDataRowmapper implements RowMapper<PrintSignsCSVFileReportDataBean>{

	public PrintSignsCSVFileReportDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		PrintSignsCSVFileReportDataBean printSignsCSVFileReportDataBean = new PrintSignsCSVFileReportDataBean();
		printSignsCSVFileReportDataBean.setStoreNo((rs.getString("STORE")).toString());
		printSignsCSVFileReportDataBean.setEventName((rs.getString("EVENT_NAME")).toString());
		printSignsCSVFileReportDataBean.setQuantity(CommonUtil.bigDecimalToLong(rs.getString("QUANTITY")));
		return printSignsCSVFileReportDataBean;
		}


}
