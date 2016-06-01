package com.walgreens.batch.central.writer;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.TADataBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class will write the PROPERTY_VALUE fetched from OM_ENV_PROPERTIES to
 * execution context so that it can be fetched in the next step
 * 
 * @author Cognizant
 * 
 */
public class PropertyValueWriter implements ItemWriter<TADataBean>,
		PhotoOmniConstants {

	final private static Logger log = LoggerFactory
			.getLogger(PropertyValueWriter.class);
	private StepExecution stepExecution;

	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public void write(List<? extends TADataBean> items) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Entering into PropertyValueWriter:write()");
		}

		ExecutionContext executionContext = this.stepExecution
				.getExecutionContext();
		TADataBean taDataBean = null;
		if (null != items && items.size() >= 1) {
			taDataBean = items.get(0);
			String propertyValue = taDataBean.getPropValue();
			log.info("propertyValue    " + propertyValue);
			Date chainWidePilotDate = CommonUtil.convertStringToDate(
					propertyValue, DATE_FORMAT_ZERO);
			log.info("chainWidePilotDate    " + chainWidePilotDate);
			executionContext.put("propValue", chainWidePilotDate);
		}
		else{
			log.info("there are no items");
		}
		if (log.isDebugEnabled()) {
			log.debug("Exiting from PropertyValueWriter:write()");
		}

	}

}
