/**
 * 
 */
package com.walgreens.oms.bo;

/**
 * @author CTS
 *
 */
public interface PromotionalMoneyBO {
	public boolean calculatePromotionalMoney(long sys_order_id, String order_placed_dttm);
}
