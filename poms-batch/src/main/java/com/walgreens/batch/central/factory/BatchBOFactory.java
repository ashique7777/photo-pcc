/**
 * 
 */
package com.walgreens.batch.central.factory;

import com.walgreens.batch.central.bo.PayOnFulfillmentBOImpl;
import com.walgreens.common.constant.PhotoOmniConstants;

/**
 * @author CTS
 *
 */
public class BatchBOFactory {
	
	public BatchBO getBO(String type){
		
		if(type.equalsIgnoreCase(PhotoOmniConstants.POF)){
			return new PayOnFulfillmentBOImpl();
		}
		return null;
	}

}
