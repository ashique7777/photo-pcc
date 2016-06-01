package com.walgreens.batch.central.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import com.walgreens.batch.central.bean.PromotionStoresAssocBean;

public class PromoStoresAssocProcessor implements ItemProcessor<PromotionStoresAssocBean, PromotionStoresAssocBean> {

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PromoStoresAssocProcessor.class);
	PromotionStoresAssocBean objPromotionStoresAssocBean;

	@Override
	public PromotionStoresAssocBean process(PromotionStoresAssocBean item)
			throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PromoStoresAssocProcessor.process()");
		}
		return item;
	}
}
