/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.processor;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import com.walgreens.batch.central.bean.PromotionHeaderDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * {@link PromoHeaderFileProcessor} is a business implementation class for {@link ItemProcessor}
 * This class is used to process the Header bean information
 * @author CTS
 * @since v1.0
 */
public class PromoHeaderFileProcessor implements ItemProcessor<PromotionHeaderDataBean, PromotionHeaderDataBean> {

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PromoHeaderFileProcessor.class);

	private PromotionHeaderDataBean objPromotionHeaderDataBean ;

	private Date recvDTTM;

	public void setRecvDTTM(Date recvDTTM) {
		this.recvDTTM = recvDTTM;
	}

	@SuppressWarnings("unused")
	private long jobSubmitTime;

	public void setJobSubmitTime(long jobSubmitTime) {
		this.jobSubmitTime = jobSubmitTime;
	}

	/**
	 * This method to process promotionHeaderDataBean data.
	 * @param PromotionHeaderDataBean .
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public PromotionHeaderDataBean process(PromotionHeaderDataBean promotionHeaderDataBean)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PromoHeaderFileProcessor.process() --- >");
		}
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Received Date and time is --- > "  + this.recvDTTM);
			}
			objPromotionHeaderDataBean = promotionHeaderDataBean;
			objPromotionHeaderDataBean.setRecvDTTM(recvDTTM);
		}catch (Exception e) {
			LOGGER.error(" Error occoured at PromoHeaderFileProcessor.process() ---->  " + e);
			throw new PhotoOmniException(e);
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting PromoHeaderFileProcessor.process() ---> ");
			}
		}
		return objPromotionHeaderDataBean;
	}
}
