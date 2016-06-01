package com.walgreens.batch.central.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.POFIrsFileDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.exception.PhotoOmniException;


/**
* This processor class call the createCSVFile method to create the CSV file.  
* 
* @author CTS
* @version 1.1
*/

public class POFIRSFileProcessor implements ItemProcessor<POFIrsFileDataBean, POFIrsFileDataBean>{
	
	/**
     * oMSBOFactory
     */
	@Autowired
	private OMSBOFactory oMSBOFactory;
	/**
	 * POFOrderVCRepBean
	 */
	POFIrsFileDataBean pofIrsFileDataBean = null;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFIRSFileProcessor.class);
	
	/**
	 * This method use for calling the createIrsFile method of orderUtilBO 
	 * @param item
	 * @return pofIrsFileDataBean
	 * @throws PhotoOmniException - Custom Exception
	 */
	public POFIrsFileDataBean process(final POFIrsFileDataBean item) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of POFIRSFileProcessor ");
		}
		try {
			OrderUtilBO orderUtilBO = null;
			pofIrsFileDataBean = item;
			orderUtilBO = oMSBOFactory.getOrderUtilBO();
			pofIrsFileDataBean = (POFIrsFileDataBean) orderUtilBO.createDatOrIrsFile(pofIrsFileDataBean,PhotoOmniBatchConstants.IRS_FILE);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of POFIRSFileProcessor - " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of POFIRSFileProcessor ");
			}
		}
		
		return pofIrsFileDataBean;
	}

}
