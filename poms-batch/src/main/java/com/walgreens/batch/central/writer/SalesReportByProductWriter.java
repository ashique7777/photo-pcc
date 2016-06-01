/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.exception.PhotoOmniException;

/**
 *  <p>
 * This is a custom item writer class implementing spring itemWriter.
 * This class will update the processed adoc report as inactive using reprotId
 * </p>
 * {@link SalesReportByProductWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to update report status as inactive
 * @author CTS
 * @since v1.0
 */
public class SalesReportByProductWriter implements
ItemWriter<SalesReportByProductBean> {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportByProductWriter.class);

	@Autowired
	private ReportBOFactory reportBOFactory;

	/**
	 * Method to update active status as 0 
	 * after report is successfully generated
	 * 
	 * @param SalesReportByProductBean - Data bean which contains filter criteria and report type details
	 * @throws PhotoOmniException - Custom exception
	 */
	public void write(List<? extends SalesReportByProductBean> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into SalesReportByProductWriter.write() method-- > ");
		}
		SalesReportByProductBean salesReportByProductBean = items.get(0);
		try{
			if(null != salesReportByProductBean){
				String strUserPrefTblUpdtQuery = ReportsQuery.updateQueryUserPRef()
						.toString();

				Object[] objUserParms = new Object[] { salesReportByProductBean.getSysUserReportPrefId() };
				jdbcTemplate.update(strUserPrefTblUpdtQuery, objUserParms);
			}
		}catch (Exception e) {
			LOGGER.error(" Error occoured at SalesReportByProductWriter.write() method ----> " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from SalesReportByProductWriter.write() method -- >");
			}
		}

	}

}
