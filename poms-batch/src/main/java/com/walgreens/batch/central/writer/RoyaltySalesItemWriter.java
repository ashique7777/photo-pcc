/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * <p>
 * 	Custom item reader implements Spring itemReader to deactivate the adoc report
 * </p>
 * 
 * <p>
 * This will update the processed adoc report as inactive using reportId
 * </p>
 * {@link RoyaltySalesItemWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to update report status as inactive
 * @author CTS
 * @since v1.0
 */
public class RoyaltySalesItemWriter implements ItemWriter<RoyaltySalesReportPrefDataBean>{
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoyaltySalesItemWriter.class);
	
	@Autowired
	private ReportBOFactory reportBOFactory;

	/**
	 * Method to update active status as 0 for processed adoc report 
	 * based on reportId
	 * 
	 * @param RoyaltySalesReportPrefDataBean
	 * @throws PhotoOmniException
	 */
	public void write(List<? extends RoyaltySalesReportPrefDataBean> items)
			 throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering RoyaltySalesItemWriter.read() method -- >");
		}
		try{
		RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean = items.get(0);
		String strUserPrefTblUpdtQuery = ReportsQuery.updateQueryUserPRef().toString();		 
		Object[] objUserParms = new Object[] {royaltySalesReportPrefDataBean.getSysUserReportPrefId()};
		jdbcTemplate.update(strUserPrefTblUpdtQuery, objUserParms);
	 } catch (Exception e) {
		 LOGGER.error(" Error occoured at RoyaltySalesItemWriter.read() method  ----> " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from RoyaltySalesItemWriter.read() method  -- >");
			}
		}
		
	}
}
