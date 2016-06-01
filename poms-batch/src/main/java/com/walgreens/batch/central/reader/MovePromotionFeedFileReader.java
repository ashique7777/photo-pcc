package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import com.walgreens.batch.central.bean.MovePromotionFeedFileBean;

public class MovePromotionFeedFileReader implements ItemReader<MovePromotionFeedFileBean>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MovePromotionFeedFileReader.class);
	int count = 0;
	MovePromotionFeedFileBean objMovePromotionFeedFileBean;
	private String strLstFileNames;

	public void setStrLstFileNames(String strLstFileNames) {
		this.strLstFileNames = strLstFileNames;
	}

	@Value( "${promotion.inbound.feed.location}" )
	private String strInboundFeedLocation;

	@Value( "${promotion.archival.feed.location}" )
	private String strarchivalFeedLocation;

	public MovePromotionFeedFileBean read() throws Exception,
	UnexpectedInputException, ParseException,
	NonTransientResourceException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into MovePromotionFeedFileReader.read()");
		}
		if(count == 0){
			count++;

			objMovePromotionFeedFileBean = new MovePromotionFeedFileBean();
			objMovePromotionFeedFileBean.setStrLstFileNames(strLstFileNames);
			objMovePromotionFeedFileBean.setInboundFeedLocation(strInboundFeedLocation);
			objMovePromotionFeedFileBean.setArchivalFeedLocation(strarchivalFeedLocation);

			return objMovePromotionFeedFileBean;
		}else{
			return null;
		}
	}
}