/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	Custom item Processor implements Spring itemProcessor
 * </p>
 * 
 * <p>
 *  Class to create csv report skeleton for pmByWic report 
 * </p>
 * {@link CSVReportItemProcessor} is a business implementation class for {@link ItemProcessor}
 * @author CTS
 * @since v1.0
 */
public class CSVReportItemProcessor implements ItemProcessor<PMBYWICReportPrefDataBean, PMBYWICReportPrefDataBean> {

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVReportItemProcessor.class);

	@Autowired
	private ReportBOFactory objReportBOFactory;
	
	PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean;
	
	private String strjobExecutionstepName;

	/**
	 * Processor method to create skeleton for PmBYWIC report
	 * 
	 * @param PMBYWICReportPrefDataBean -- Bean contains filter criteria
	 * @return PMBYWICReportPrefDataBean -- Bean contains filter criteria and file location
	 * @throws Exception 
	 */
	public PMBYWICReportPrefDataBean process(PMBYWICReportPrefDataBean item)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVReportItemProcessor.process()");
		}
		try {
			String strReportType = "";
			objPMBYWICReportPrefDataBean = item;

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" strjobExecutionstepName in CSVReportItemProcessor.process()" + strjobExecutionstepName);
			}

			if(strjobExecutionstepName.equalsIgnoreCase("PMWICcustomreportJobstep1")){
				strReportType = PhotoOmniBatchConstants.PMBYWIC_CUSTOM;
				objPMBYWICReportPrefDataBean.setReportType(strReportType);
			}else {
				strReportType = PhotoOmniBatchConstants.PMBYWIC_MONTHLY;
				objPMBYWICReportPrefDataBean.setReportType(strReportType);
			}

			ReportsUtilBO objReportsUtilBO = objReportBOFactory.getReportsUtilBO();
			objPMBYWICReportPrefDataBean =  (PMBYWICReportPrefDataBean) objReportsUtilBO.createCSVFile(objPMBYWICReportPrefDataBean, strReportType);

		}catch (Exception e) {
			LOGGER.error(" Error occoured at CSVReportItemProcessor.process() ---> " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From CSVReportItemProcessor.process() ");
			}
		}
		return objPMBYWICReportPrefDataBean;
	}

	/**
	 * Method to set execution context variable
	 * 
	 * @param stepExecution -- Execution context holder
	 */
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVReportItemProcessor.beforeStep()");
		}
		strjobExecutionstepName = stepExecution.getStepName();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting From CSVReportItemProcessor.beforeStep() ");
		}
	}

}
