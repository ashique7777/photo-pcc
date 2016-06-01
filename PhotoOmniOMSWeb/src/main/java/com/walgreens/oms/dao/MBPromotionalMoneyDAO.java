/**
 * 
 */
package com.walgreens.oms.dao;

import java.sql.SQLException;
import java.util.List;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.MBPMDetailsBean;
import com.walgreens.oms.bean.OrderDetailData;
import com.walgreens.oms.bean.OrderPMDataBean;
import com.walgreens.oms.bean.PMRule;

/**
 * @author CTS
 *
 */

/**
 * Consists of the business rule implementations for MBPM report by executing queries defined
 * @author Cognizant
 *
 */
public interface MBPromotionalMoneyDAO {
	
	public List<OrderDetailData> getMbpmOrderDetailData(long sysShoppingCartID ,String orderPlacedDttm) throws PhotoOmniException ;
	
	public PMRule getMbpmRuleData(String startdate ,String endDate);
	
	public List<OrderPMDataBean> getOrderMBPMData(long sysShoppingCartID ,String orderPlacedDttm) ;

	public void updatePmStatus(long sysOderId, String oderPlacedDTTM, String pmStatus);

	void rejectOrderPm(MBPMDetailsBean mbpmBasketDetails) throws SQLException;

	public void updateOrderPm(MBPMDetailsBean mbpmBasketDetails) throws SQLException;


	
}

