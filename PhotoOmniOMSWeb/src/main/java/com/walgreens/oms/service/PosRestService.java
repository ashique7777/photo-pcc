package com.walgreens.oms.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walgreens.oms.json.bean.PosOrderRequest;
import com.walgreens.oms.json.bean.PosOrderResponse;

/**
 * @author CTS Interface is Used to process Pos orders
 * 
 */

public interface PosRestService {

	/**
	 * Method used to process order POS JSON messages
	 * 
	 * @param posJsonRequestMsg
	 * @return PosOrderResponse
	 */
	public @ResponseBody
	PosOrderResponse submittPOSOrder(
			@RequestBody PosOrderRequest posOrderRequest);

}
