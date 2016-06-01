/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	Custom item processor implements Spring itemProcessor to which will create 
 * excel file skeleton based on filter criteria.
 * </p>
 * 
 * <p>
 * This will read the royalty data bean data and create skeleton for report excel file.
 * </p>
 * 
 * {@link CSVRoyaltyReportItemProcessor} is a business implementation class for {@link ItemProcessor}
 * This class is used to process the date fetch from reader
 *  @author CTS
 * @since v1.0
 */
public class CSVRoyaltyReportItemProcessor implements ItemProcessor<RoyaltySalesReportPrefDataBean, RoyaltySalesReportPrefDataBean>{

	@Autowired
	private ReportBOFactory reportBOFactory;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVRoyaltyReportItemProcessor.class);

	/**
	 * Method will process the royalty data bean and skeleton for royalty report
	 * using filter details 
	 * 
	 * @param RoyaltySalesReportPrefDataBean  --  Contains filter state and reportId 
	 * @return RoyaltySalesReportPrefDataBean --  Contains filter state and reportId 
	 * @throws PhotoOmniException -- Custom Exception
	 */
	@Override
	public RoyaltySalesReportPrefDataBean process(RoyaltySalesReportPrefDataBean item)
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into  CSVRoyaltyReportItemProcessor.process() method--- >");
		}
		RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean = item;
		try{
			ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
			royaltySalesReportPrefDataBean =  (RoyaltySalesReportPrefDataBean) reportsUtilBO
					.createXlsxFile(royaltySalesReportPrefDataBean, royaltySalesReportPrefDataBean.getReportType());
		}catch (Exception e) {
			LOGGER.error(" Error occoured at CSVRoyaltyReportItemProcessor.process() method --- > " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting CSVRoyaltyReportItemProcessor.process() method --- >");
			}
		}
		return royaltySalesReportPrefDataBean;
	}
}
