package com.walgreens.oms.bo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.UnclaimedEnvCustorderReqBean;
import com.walgreens.oms.json.bean.AsnOrderRequest;
import com.walgreens.oms.json.bean.AsnOrderResponse;
import com.walgreens.oms.json.bean.UnclaimedEnvCustomer;
import com.walgreens.oms.json.bean.UnclaimedEnvFilter;
import com.walgreens.oms.json.bean.UnclaimedResponse;

public interface OrdersUtilBO {

	AsnOrderResponse updateASNDetails(AsnOrderRequest asnOrderJsonRequest) throws PhotoOmniException;
	
	public @ResponseBody UnclaimedResponse submitUnclaimedEnvRequest(final UnclaimedEnvFilter reqBean) throws PhotoOmniException ;
	
	public @ResponseBody UnclaimedEnvCustomer unclaimedEnvCustOrderRequest(@RequestBody final UnclaimedEnvCustorderReqBean reqBean) throws PhotoOmniException; 
	
}
