/**
 * 
 */
package com.walgreens.oms.dao;

import java.sql.SQLException;
import java.util.List;

import com.walgreens.oms.bean.OmPromotionalMoneyBean;
import com.walgreens.oms.bean.OrderPMDataBean;
import com.walgreens.oms.bean.PMOrderDetailBean;
import com.walgreens.oms.bean.PMRule;

/**
 * @author CTS
 *
 */
public interface PromotionalMoneyDAO {
	public PMOrderDetailBean getOrderPMDetails(long sys_order_id, String order_placed_dttm);

	public void getPMOrderLine(PMOrderDetailBean orderDetails);

	List<OmPromotionalMoneyBean> getPMStoreRule(long sysProductId,
			String orderPlacedDTTM,long locationId);

	PMRule getPMRule(long sysProductId,
			String orderPlacedDTTM,long locationId);

	List<OrderPMDataBean> fetchOrderPM(long sysOrderID,
			String orderPlacedDTTM);
	public void updatePmStatus(long sysOderId, String oderPlacedDTTM, String pmStatus);

	public void rejectOrderPm(PMOrderDetailBean orderDetails) throws SQLException;

	public void updateOrderPm(PMOrderDetailBean orderDetails) throws SQLException;

	public int getOrderPLUCnt(long sysOderId, String oderPlacedDTTM);

	public int getOrderLinePLUCnt(long sysOderLineId, String oderPlacedDTTM);

}
