package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.POFIrsFileDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.utility.PayOnFulfillmentQry;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class is used to call for closing IRS file.
 * 
 * @author CTS
 * @version 1.1
 */

public class POFIRSFileCloseWriter implements ItemWriter<POFIrsFileDataBean>{
	

    /**
	 * pofIrsFileDataBean
	 */
	POFIrsFileDataBean pofIrsFileDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFIRSFileCloseWriter.class);
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	
	
	/**
     * This method closing the CSV file.
     * @param items contains item data.
     * @throws PhotoOmniException - Custom Exception.
     */
	public void write(final List<? extends POFIrsFileDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of POFIRSFileCloseWriter ");
		}
		try {
			String statusInd = PhotoOmniBatchConstants.POF_STATUS_IND_Y;
			String updateOrderVCRepQry =PayOnFulfillmentQry.updateOrderVCRepForIrsFile().toString();
			jdbcTemplate.update(updateOrderVCRepQry, new Object[]{statusInd});
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of POFIRSFileCloseWriter - " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of POFIRSFileCloseWriter ");
			}
		}

		
	}


}
