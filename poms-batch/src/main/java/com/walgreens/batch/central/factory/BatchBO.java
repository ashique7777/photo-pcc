/**
 * 
 */
package com.walgreens.batch.central.factory;

import com.walgreens.batch.central.bean.POFOrderVCRepBean;
import com.walgreens.batch.central.bean.PromotionalMoneyOrderDataBean;

/**
 * @author CTS
 *
 */
public interface BatchBO {
	
	public void calculatePM(PromotionalMoneyOrderDataBean pmOrderDataBean,boolean isPMReject  );
	public void  validateVendorPayment(POFOrderVCRepBean pofOrderVCRepBean);
	
}
