package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.CSVFileRoyaltyReportDataBean;

public class RoyaltyReportRowmapper implements RowMapper<CSVFileRoyaltyReportDataBean> {

	@Override
	public CSVFileRoyaltyReportDataBean mapRow(ResultSet result, int rowNum)
			throws SQLException {
		CSVFileRoyaltyReportDataBean csvFileRoyaltyReportDataBean = new CSVFileRoyaltyReportDataBean();
		csvFileRoyaltyReportDataBean = new CSVFileRoyaltyReportDataBean();
		csvFileRoyaltyReportDataBean.setStoreNumber(Long.valueOf(result.getString("STORENUMBER")));
		csvFileRoyaltyReportDataBean.setTemplateId(Long.valueOf(result.getString("TEMPLATEID")));
		csvFileRoyaltyReportDataBean.setNumberOfOrders(result.getLong("NOOFORDERS"));
		csvFileRoyaltyReportDataBean.setNumberOfPrints(result.getLong("NOOFPRINTS"));
		csvFileRoyaltyReportDataBean.setProductName(result.getString("PRODUCTNAME"));
		csvFileRoyaltyReportDataBean.setSoldAmount(result.getDouble("SOLDAMOUNT"));
		csvFileRoyaltyReportDataBean.setVendor(result.getString("VENDOR"));
		csvFileRoyaltyReportDataBean.setOutPutSize(result.getString("OUTPUT_SIZE"));
		csvFileRoyaltyReportDataBean.setTemplateName(result.getString("TEMPLATENAME"));
		csvFileRoyaltyReportDataBean.setProduct(result.getString("PRODUCT"));
		
		return csvFileRoyaltyReportDataBean;
	}

}
