package com.walgreens.oms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bo.PosOrdersBO;
import com.walgreens.oms.factory.OmsBOFactory;
import com.walgreens.oms.json.bean.PosOrderRequest;
import com.walgreens.oms.json.bean.PosOrderResponse;

/**
 * This class is used to call for Pos Order status updates and POS Realtime
 * Order process in central system as per action.
 * 
 * @author CTS
 * 
 */

@RestController
@RequestMapping(value = "/pos", method = RequestMethod.POST)
public class PosRestServiceImpl implements PosRestService {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PosRestServiceImpl.class);

	@Autowired
	private OmsBOFactory omsBOFactory;

	/**
	 * This method is use for Realtime pos order submission at Central
	 * 
	 * @param posJsonRequestMsg
	 * @return PosOrderResponse
	 */
	@RequestMapping(value = "/update", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody PosOrderResponse submittPOSOrder(@RequestBody PosOrderRequest posOrderJsonRequest) {

		PosOrderResponse posOrderResponse = new PosOrderResponse();
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Inside RealTime Service  submittPOSOrder process...");
			}	
			
			PosOrdersBO posOrdersBO = omsBOFactory.getPOSOrdersBo();
			if (posOrderJsonRequest != null) {
				posOrderResponse = posOrdersBO.submittPOSOrder(posOrderJsonRequest);
			}

		} catch (NullPointerException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" NullPointerException occoured at submittPOSOrder() method of RealTimeOrderServiceImpl - ", e);
			}
		} catch (PhotoOmniException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" PhotoOmniException occoured at submittPOSOrder() method of RealTimeOrderServiceImpl - ", e);
			}
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at submittPOSOrder() method of RealTimeOrderServiceImpl - ", e);
			}
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("RealTime Service submittPOSOrder process end...");
			}
		}	
		return posOrderResponse;

	}
}
