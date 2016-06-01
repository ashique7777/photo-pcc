package com.walgreens.oms.bo;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.json.bean.PosOrderRequest;
import com.walgreens.oms.json.bean.PosOrderResponse;

/**
 * @author CTS interface used to process realtime pos orders
 */

public interface PosOrdersBO {

	/**
	 * This method is use for Realtime pos order submission at Central
	 * 
	 * @param posOrderRequest
	 * @return PosOrderResponse
	 * @throws PhotoOmniException
	 */
	public PosOrderResponse submittPOSOrder(PosOrderRequest posOrderRequest)
			throws PhotoOmniException;
}
