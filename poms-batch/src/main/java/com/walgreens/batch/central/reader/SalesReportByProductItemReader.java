/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.batch.central.rowmapper.SalesReportByProductRowmapper;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * This is a custom item reader class implementing spring itemReader.
 * This class will read adoc report filter criteria details from the adoc report able
 * using adoc reportId.
 * </p>
 * 
 * {@link SalesReportByProductItemReader} is a business implementation class for {@link ItemReader}
 * This class is used to read the Sales report filter detail from database
 * @author CTS
 * @since v1.0
 */
public class SalesReportByProductItemReader implements
ItemReader<SalesReportByProductBean> {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	int count = 0;

	private Long reportId ;

	@SuppressWarnings("unused")
	private long jobSubmitTime;


	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportByProductItemReader.class);

	/**
	 * Method to set adoc reportId from jobparamters
	 * 
	 * @param reportID
	 */
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public void setJobSubmitTime(long jobSubmitTime) {
		this.jobSubmitTime = jobSubmitTime;
	}

	/**
	 * Method to get sales adoc report filter criteria using reportId
	 * 
	 * @return SalesReportByProductBean -- Data bean which contains filter criteria and report type details
	 * @throws PhotoOmniException -- Custom exception of application
	 */
	@Override
	public SalesReportByProductBean read() throws
	PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into SalesReportByProductItemReader.read() method -- >");	
		}
		SalesReportByProductBean salesReportByProductBean = null;
		if (count == 0) {
			count++;
			try{
				String query = ReportsQuery.getUserPrefQuery(reportId).toString();
				salesReportByProductBean = jdbcTemplate.query(query,
						new SalesReportByProductRowmapper()).get(0);
				if(null != salesReportByProductBean){
					salesReportByProductBean.setSysUserReportPrefId(reportId);
					salesReportByProductBean.setReportType(PhotoOmniConstants.SALES_CUSTOM_REPORT);
					salesReportByProductBean.setFilterMap(
							CommonUtil.getFilterparams(salesReportByProductBean.getFilterState()));
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at SalesReportByProductItemReader.read() method----> " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("Exiting from SalesReportByProductItemReader.read() method--- >");
				}
			}
		} 
		return salesReportByProductBean;
	}
}
