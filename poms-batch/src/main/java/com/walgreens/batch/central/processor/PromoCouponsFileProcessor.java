/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.processor;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import com.walgreens.batch.central.bean.PromotionCouponsDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * {@link PromoCouponsFileProcessor} is a business implementation class for {@link ItemProcessor}
 * This class is used to process the Header bean information
 * @author CTS
 * @since v1.0
 */
public class PromoCouponsFileProcessor implements ItemProcessor<PromotionCouponsDataBean, PromotionCouponsDataBean>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PromoCouponsFileProcessor.class);

	private PromotionCouponsDataBean objPromotionCouponsDataBean;

	private Date recvDTTM;

	public void setRecvDTTM(Date recvDTTM) {
		this.recvDTTM = recvDTTM;
	}


	/**
	 * This method to process PromotionCouponsDataBean data.
	 * @param PromotionCouponsDataBean .
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public PromotionCouponsDataBean process(PromotionCouponsDataBean promotionCouponsDataBean)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PromoCouponsFileProcessor.process() --- >");
		}
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Received Date and time is --- > "  + this.recvDTTM);
			}
			objPromotionCouponsDataBean = promotionCouponsDataBean;
			objPromotionCouponsDataBean.setRecvDTTM(recvDTTM);
		}catch (Exception e) {
			LOGGER.error(" Error occoured at PromoCouponsFileProcessor.process() ---->  " + e);
			throw new PhotoOmniException(e);
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting PromoCouponsFileProcessor.process() ---> ");
			}
		}
		return objPromotionCouponsDataBean;
	}
}

