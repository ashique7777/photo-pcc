/**
 * KronosCompleteOrderDataProcessor.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 02 September 2015
 *  
 **/
package com.walgreens.batch.central.processor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.KronosDataBean;
import com.walgreens.batch.central.bean.StoreOpenCloseDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This processor class use to process Kronos Complete order.  
 * @author CTS
 * @version 1.1 September 02, 2015
 */
public class KronosCompleteOrderDataProcessor implements ItemProcessor<KronosDataBean, KronosDataBean> {
    /**
     * oMSBOFactory
     */
	@Autowired
	private OMSBOFactory oMSBOFactory;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KronosCompleteOrderDataProcessor.class);
	/**
	 * storeOpenCloseDataBeanList
	 */
	private Map<String, StoreOpenCloseDataBean> storeOpenCloseDataBeanList = null;
	
	/**
	 * This method use for processing Kronos Complete order.
	 * @param item contains kronos complete order data.
	 * @throws PhotoOmniException-Custom Exception.
	 */
	public KronosDataBean process(final KronosDataBean item) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of KronosCompleteOrderDataProcessor ");
		}
		try {
			if (!CommonUtil.isNull(storeOpenCloseDataBeanList) && storeOpenCloseDataBeanList.size() > 0 && !CommonUtil.isNull(item)) {
				final OrderUtilBO orderUtilBo = oMSBOFactory.getOrderUtilBO();
				orderUtilBo.kronosDataFormatting(item, storeOpenCloseDataBeanList);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of KronosCompleteOrderDataProcessor - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of KronosCompleteOrderDataProcessor ");
			}
		}
		return item;
	}
	
	
	/**
	 * This method set the params values to execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@SuppressWarnings("unchecked")
	@BeforeStep
	protected void retriveValue(final StepExecution stepExecution) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of KronosCompleteOrderDataProcessor ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			storeOpenCloseDataBeanList = (Map<String, StoreOpenCloseDataBean>) executionContext.get("storeOpenCloseSlot");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at retriveValue method of KronosCompleteOrderDataProcessor - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of KronosCompleteOrderDataProcessor ");
			}
		}
	}
	
}
