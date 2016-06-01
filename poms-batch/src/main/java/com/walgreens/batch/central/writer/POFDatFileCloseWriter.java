package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.POFDatFileDataBean;
import com.walgreens.batch.central.utility.PayOnFulfillmentQry;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * This class is used to call for closing DAT file.
 * 
 * @author CTS
 * @version 1.1
 */
public class POFDatFileCloseWriter implements ItemWriter<POFDatFileDataBean> {
	

	/**
	 * lCDailyReportPrefDataBean
	 */
	POFDatFileDataBean pofDatFileDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFDatFileCloseWriter.class);
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	

	
	
	/**
     * This method Update with Status CD as I  in VC REP table
     * @param items contains item data.
     * @throws PhotoOmniException - Custom Exception.
     */
	public void write(final List<? extends POFDatFileDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of POFDatFileCloseWriter ");
		}
		try {
			String statusInd = PhotoOmniConstants.POF_STATUS_IND_I;
			String updateOrderVCRepQry =PayOnFulfillmentQry.updateOrderVCRepForDatFile().toString();
			int datUpdate = jdbcTemplate.update(updateOrderVCRepQry, new Object[]{statusInd});
			if(datUpdate > 0){
				LOGGER.info(" Number of DAT updation in  Write method of POFDATFileCustomWriter "+ datUpdate);	
			}
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of POFDatFileCloseWriter - " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of POFDatFileCloseWriter ");
			}
		}

		
	}


}
