package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.POFDatFileDataBean;
import com.walgreens.common.exception.PhotoOmniException;

public class POFDatFileWriter implements ItemWriter<POFDatFileDataBean> {
	private StepExecution stepExecution;
    private static final Logger logger = LoggerFactory.getLogger(POFDatFileWriter.class);
	

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
	public void write(List<? extends POFDatFileDataBean> items)
			throws PhotoOmniException {
		if (logger.isDebugEnabled()) {
			logger.debug(" Entering write method of POFDatFileWriter ");
		}
		try{
			
		ExecutionContext executionContext = this.stepExecution.getExecutionContext();
		POFDatFileDataBean pofDatFileDataBean = items.get(0);
		executionContext.put("refPofDATFileKey", pofDatFileDataBean);
		} catch (Exception e) {
				logger.error(" Error occoured at write method of POFDatFileWriter - " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (logger.isDebugEnabled()) {
					logger.debug(" Exiting write method of POFDatFileWriter ");
				}
			}
	}

}
