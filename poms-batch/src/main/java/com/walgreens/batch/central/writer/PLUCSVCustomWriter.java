package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

public class PLUCSVCustomWriter implements ItemWriter<PLUReportPrefDataBean> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PLUCSVCustomWriter.class);
	private StepExecution stepExecution;

	/**
	 * Method to set Bean to Execution Context
	 * 
	 * @param List
	 *            of PLUReportPrefDataBean
	 * @throws PhotoOmniException
	 *             - Custom Exception.
	 */
	@Override
	public void write(List<? extends PLUReportPrefDataBean> items)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into write method of PLUCSVCustomWriter ");
		}
		try {
			ExecutionContext executionContext = this.stepExecution
					.getExecutionContext();
			PLUReportPrefDataBean objPLUReportPrefDataBean = items.get(0);
			executionContext.put("refDataKey", objPLUReportPrefDataBean);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of PLUCSVCustomWriter ----> "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of PLUCSVCustomWriter ");
			}
		}
	}

	/**
	 * Method to get stepExecution from Execution context
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering beforeStep method of PLUCSVCustomWriter");
		}
		this.stepExecution = stepExecution;
	}
}