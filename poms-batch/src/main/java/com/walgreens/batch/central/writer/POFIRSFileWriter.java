package com.walgreens.batch.central.writer;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.POFIrsFileDataBean;
import com.walgreens.common.exception.PhotoOmniException;

import org.springframework.batch.item.ExecutionContext;

public class POFIRSFileWriter implements ItemWriter<POFIrsFileDataBean>{
	private StepExecution stepExecution;
    private static final Logger logger = LoggerFactory.getLogger(POFIRSFileWriter.class);
	

	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution)
	{
		this.stepExecution = stepExecution;
	}
	
	/**
	 * Method to set Bean to Execution Context
	 * @param List of RoyaltySalesReportPrefDataBean
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@Override
	public void write(List<? extends POFIrsFileDataBean> items)
			throws PhotoOmniException {
		if (logger.isDebugEnabled()) {
			logger.debug(" Entering write method of POFIRSFileWriter ");
		}
		try{
		ExecutionContext executionContext = this.stepExecution.getExecutionContext();
		POFIrsFileDataBean pofIrsFileDataBean = items.get(0);
		executionContext.put("refPofIRSFileKey", pofIrsFileDataBean);
		} catch (Exception e) {
				logger.error(" Error occoured at write method of POFIRSFileWriter - " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (logger.isDebugEnabled()) {
					logger.debug(" Exiting write method of POFIRSFileWriter ");
				}
			}
	}

}
