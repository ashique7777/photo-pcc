/**
 * 
 */
package com.walgreens.oms.bo;

import com.walgreens.oms.bean.MBPMDetailsBean;

/**
 * @author CTS
 *
 */


/**
 * This method consists all of the business rules required for MBPM Report
 * Consists of validations, MBPM rule calculations and setting different indicators
 * @author Cognizant
 *
 */
public interface MBPromotionalMoneyBO {
	
	public  boolean calculateMBPromotionalMoney(long sysShoppingCartID ,String orderplaceddttm) throws Exception;
	
	public void validateOrderPMObj(MBPMDetailsBean mbpmBasketDetails);
	
}

