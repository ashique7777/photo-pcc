package com.walgreens.batch.central.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

public class PLUCSVReportItemProcessor implements
		ItemProcessor<PLUReportPrefDataBean, PLUReportPrefDataBean> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PLUCSVReportItemProcessor.class);

	@Autowired
	private OMSBOFactory omsboFactory;

	private String strjobExecutionstepName;
	PLUReportPrefDataBean objPLUReportPrefDataBean;

	/**
	 * Method to create xlsx file
	 * 
	 * @param PLUReportPrefDataBean
	 * @return PLUReportPrefDataBean
	 * @throws PhotoOmniException
	 *             - Custom Exception
	 */
	@Override
	public PLUReportPrefDataBean process(PLUReportPrefDataBean item)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into process method of PLUCSVReportItemProcessor ");
		}
		try {
			String reportType = "";
			objPLUReportPrefDataBean = item;
			if (strjobExecutionstepName.equalsIgnoreCase("DailyPLUJobStep1")) {
				reportType = PhotoOmniConstants.PLU_DAILY;
				objPLUReportPrefDataBean.setReportType(reportType);
			} else {
				reportType = PhotoOmniConstants.PLU_ADHOC;
				objPLUReportPrefDataBean.setReportType(reportType);
			}
			OrderUtilBO orderUtilBO = omsboFactory.getOrderUtilBO();
			objPLUReportPrefDataBean = (PLUReportPrefDataBean) orderUtilBO
					.createCSVFile(objPLUReportPrefDataBean, reportType);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of PLUCSVReportItemProcessor ----> "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of PLUCSVReportItemProcessor ");
			}
		}
		return objPLUReportPrefDataBean;
	}

	/**
	 * Method to get stepExecution from Execution context
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering beforeStep method of PLUCSVReportItemProcessor");
		}
		strjobExecutionstepName = stepExecution.getStepName();
	}
}