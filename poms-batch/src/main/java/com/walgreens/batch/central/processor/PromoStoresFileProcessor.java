/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.processor;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import com.walgreens.batch.central.bean.PromotionStoresDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * {@link PromoStoresFileProcessor} is a business implementation class for {@link ItemProcessor}
 * This class is used to process the Header bean information
 * @author CTS
 * @since v1.0
 */
public class PromoStoresFileProcessor implements ItemProcessor<PromotionStoresDataBean, PromotionStoresDataBean> {

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PromoStoresFileProcessor.class);

	private PromotionStoresDataBean objPromotionStoresDataBean;

	private Date recvDTTM;

	public void setRecvDTTM(Date recvDTTM) {
		this.recvDTTM = recvDTTM;
	}

	/**
	 * This method to process PromotionStoresDataBean data.
	 * @param PromotionStoresDataBean .
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public PromotionStoresDataBean process(PromotionStoresDataBean promotionStoresDataBean)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PromoStoresFileProcessor.process() --- >");
		}
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Received Date and time is --- > "  + this.recvDTTM);
			}
			objPromotionStoresDataBean = promotionStoresDataBean;
			objPromotionStoresDataBean.setRecvDTTM(recvDTTM);
		}catch (Exception e) {
			LOGGER.error(" Error occoured at PromoStoresFileProcessor.process() ---->  " + e);
			throw new PhotoOmniException(e);
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting PromoStoresFileProcessor.process() ---> ");
			}
		}
		return objPromotionStoresDataBean;
	}
}

