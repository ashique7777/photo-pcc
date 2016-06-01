/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * <p>
 * This is a custom item processor class implementing spring itemProcessor.
 * This class will read the data of sales report bean from reader and create a skeleton of Report CSV file
 * to which data will be populated later.
 * </p>
 * 
 * {@link SalesReportByProductItemProcessor} is a business implementation class for {@link ItemProcessor}
 * This class is used to process the date fetch from reader
 *  @author CTS
 * @since v1.0
 */
public class SalesReportByProductItemProcessor implements
ItemProcessor<SalesReportByProductBean, SalesReportByProductBean> {

	@Autowired
	private ReportBOFactory reportBOFactory;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportByProductItemProcessor.class);


	/**
	 * Method to create skeleton of sales adoc report csv file using salesReport data bean
	 * 
	 * @param SalesReportByProductBean -- Data bean which contains filter criteria and report type details
	 * @return SalesReportByProductBean -- Data bean which contains filter criteria and report type details
	 * @throws PhotoOmniException - Custom Exception
	 */
	@Override
	public SalesReportByProductBean process(SalesReportByProductBean item)
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into SalesReportByProductItemProcessor.process() method --- >");
		}
		SalesReportByProductBean salesReportByProductBean = item;
		try{
			if(null != salesReportByProductBean){
				ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
				salesReportByProductBean = (SalesReportByProductBean) reportsUtilBO
						.createCSVFile(salesReportByProductBean,salesReportByProductBean.getReportType());
			}
		}catch (Exception e) {
			LOGGER.error(" Error occoured at SalesReportByProductItemProcessor.process() method --- > " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(" Exiting from SalesReportByProductItemProcessor.process() method -- >");
			}
		}
		return salesReportByProductBean;
	}

}
