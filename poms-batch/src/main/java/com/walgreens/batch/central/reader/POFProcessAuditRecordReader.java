/**
 * 
 */
package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import com.walgreens.batch.central.bean.POFOrderVCRepBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * @author CTS
 * 
 */
public class POFProcessAuditRecordReader implements
		ItemReader<POFOrderVCRepBean> {

	private static final Logger logger = LoggerFactory
			.getLogger(POFProcessAuditRecordReader.class);

	static int count = 0;
	POFOrderVCRepBean pof= new POFOrderVCRepBean();
	public POFOrderVCRepBean read() throws PhotoOmniException {

		if (logger.isDebugEnabled()) {
			logger.debug(" Entering read method of POFProcessAuditRecordItemReader  step four..");
		}
		
		
		try {
			if (count == 0) {
				count++;
				if (logger.isDebugEnabled()) {
					logger.debug(" Exiting read method of POFProcessAuditRecordItemReader ");
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(" Exiting read method of POFProcessAuditRecordItemReader ");
				}
				return null;
			}

		} catch (Exception e) {
			logger.error(" Error occoured at read method of POFProcessAuditRecordItemReader - "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {

			if (logger.isDebugEnabled()) {
				logger.debug(" Exiting read method of POFProcessAuditRecordItemReader ");
			}
		}
		return pof;
	}

}
