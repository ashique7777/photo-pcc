package com.walgreens.batch.central.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.POFDatFileDataBean;
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

public class POFDatFileProcessor implements ItemProcessor<POFDatFileDataBean, POFDatFileDataBean>{
	
	/**
     * oMSBOFactory
     */
	@Autowired
	private OMSBOFactory oMSBOFactory;
	/**
	 * POFOrderVCRepBean
	 */
	POFDatFileDataBean pofDatFileDataBean = null;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFDatFileProcessor.class);
	
	/**
	 * This method use for calling the createDatFile method of orderUtilBO 
	 * @param item
	 * @return POFOrderVCRepBean
	 * @throws PhotoOmniException - Custom Exception
	 */
	public POFDatFileDataBean process(final POFDatFileDataBean item) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of POFDatFileProcessor ");
		}
		try {
			OrderUtilBO orderUtilBO = null;
			pofDatFileDataBean = item;
			orderUtilBO = oMSBOFactory.getOrderUtilBO();
			pofDatFileDataBean = (POFDatFileDataBean) orderUtilBO.createDatOrIrsFile(pofDatFileDataBean,PhotoOmniBatchConstants.DAT_FILE);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of POFDatFileProcessor - " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of POFDatFileProcessor ");
			}
		}
		
		return pofDatFileDataBean;
	}

	

}
