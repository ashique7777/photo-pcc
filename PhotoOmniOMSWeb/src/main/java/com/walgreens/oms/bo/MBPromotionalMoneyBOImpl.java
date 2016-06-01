/**
 * 
 */
package com.walgreens.oms.bo;

/**
 * @author CTS
 *
 */


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.MBPMDetailsBean;
import com.walgreens.oms.bean.OrderDetailData;
import com.walgreens.oms.bean.OrderPMDataBean;
import com.walgreens.oms.bean.PMRule;
import com.walgreens.oms.bean.PMRuleDetail;
import com.walgreens.oms.dao.MBPromotionalMoneyDAO;
import com.walgreens.oms.factory.OmsDAOFactory;

/**
 * This method consists all of the business rules required for MBPM Report
 * Consists of validations, MBPM rule calculations and setting different
 * indicators
 * 
 * @author Cognizant
 * 
 */
@Component("MBPromotionalMoneyBO")
public class MBPromotionalMoneyBOImpl implements MBPromotionalMoneyBO,
		PhotoOmniConstants {

	@Autowired
	private OmsDAOFactory omsDAOFactory;

	final Logger LOGGER = LoggerFactory
			.getLogger(MBPromotionalMoneyBOImpl.class);

	/**
	 * 
	 * @param sysShoppingCartID,orderplaceddttm
	 * @return boolean
	 * @throws PhotoOmniException
	 */
	public boolean calculateMBPromotionalMoney(long sysShoppingCartID,
			String orderplaceddttm) throws Exception {
		MBPromotionalMoneyDAO mbpmDao = omsDAOFactory.getMbpromotionalMoneyDAOImpl();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into CalculateMBPromotionalMoney() of MBPromotionalMoneyBOImpl");
			
		}
		boolean status = true;
		try {
			MBPMDetailsBean mbpmBasketDetails = new MBPMDetailsBean();
			mbpmBasketDetails.setSysShoppingCartId(sysShoppingCartID);
			mbpmBasketDetails.setOrderPlacedDTTM(orderplaceddttm);
			List<OrderDetailData> mbpmOrderDetailData = new ArrayList<OrderDetailData>();
			List<OrderPMDataBean> orderPmToSaveDB = new ArrayList<OrderPMDataBean>();
			// Fetches Order Details
			mbpmOrderDetailData = mbpmDao.getMbpmOrderDetailData(sysShoppingCartID, orderplaceddttm);
			mbpmBasketDetails.setOrderDataList(mbpmOrderDetailData);
			// VALIDATION STARTS HERE
			this.validateOrderPMObj(mbpmBasketDetails);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Validation of MBPM "+ mbpmBasketDetails.isBasketValid());
			}
			if (mbpmBasketDetails.isBasketValid()) {
				List<OrderPMDataBean> orderPMList = new ArrayList<OrderPMDataBean>();
				PMRule pmRuleBean = new PMRule();
				// Fetches MBPM Rule
				pmRuleBean = mbpmDao.getMbpmRuleData(orderplaceddttm, orderplaceddttm);
				if (null != pmRuleBean) {
					if (!pmRuleBean.isDiscountApplicableCd() && (mbpmBasketDetails.isCouponValid())) {
						mbpmBasketDetails.setBasketValid(false);
					}
				} else {
					mbpmBasketDetails.setBasketValid(false);
				}
				if (mbpmBasketDetails.isBasketValid()) {
					float finalPrice = mbpmBasketDetails.getTotalFinalPrice();
					double mbPmAmt = 0;
					if (null!=pmRuleBean.getTierType() && pmRuleBean.getTierType().equalsIgnoreCase(PhotoOmniConstants.STRAIGHT)) {
						for (PMRuleDetail rule : pmRuleBean.getRuleDetails()) {
							if (finalPrice >= rule.getMinTier() && finalPrice <= rule.getMaxTier()) {
								mbPmAmt = rule.getPmAmount();
								break;
							}
						}
					} else if (null!=pmRuleBean.getTierType() && pmRuleBean.getTierType().equalsIgnoreCase(PhotoOmniConstants.GRADUATED)) {
						for (PMRuleDetail rule : pmRuleBean.getRuleDetails()) {
							if (finalPrice >= rule.getMinTier()) {
								mbPmAmt += rule.getPmAmount();
							}
						}
					}
					
					//Roundoff
					mbPmAmt = Math.round(mbPmAmt * 100.0) / 100.0;
					
					double orderPMAmtDistPending = mbPmAmt;
					int noOfValidOrders = mbpmBasketDetails.getValidOrderCount();
					int noOfOrdersPMAmtShared = 0;
					// Loop through orderList
					for (OrderDetailData orderdata : mbpmBasketDetails
							.getOrderDataList()) {
						// fetches sysOrderId
						orderPMList = mbpmDao.getOrderMBPMData(orderdata.getSysOrderId(),orderdata.getOrderPlacedDttm());
						OrderPMDataBean orderPmBean = this.getOrderPmDataBean(orderPMList,orderdata.getSysOrderId());
						orderPmBean.setOrderPlacedDttm(orderdata.getOrderPlacedDttm());
						orderPmBean.setProductId(0);
						
						if (orderdata.isValidProration()) {
							double orderAmount = 0;
							noOfOrdersPMAmtShared++;
							if(noOfValidOrders == noOfOrdersPMAmtShared){
								orderAmount = orderPMAmtDistPending;
							}else{
								orderAmount =  (mbPmAmt * orderdata.getFinalPrice() / mbpmBasketDetails.getTotalFinalPriceForProration());		
								orderAmount = Math.round(orderAmount * 100.0) / 100.0;
								orderPMAmtDistPending = orderPMAmtDistPending - orderAmount;
							}
							orderPmBean.setActiveCd(1);
							orderPmBean.setEmpId(orderdata.getEmpId());
							orderPmBean.setPendingAmt(orderAmount);
							orderPmBean.setPotentialAmt(orderAmount);
							orderPmBean.setPendingQty(1);
							orderPmBean.setPotentialQty(1);
						    orderPmBean.setPmId(pmRuleBean.getPmId());
							if (orderPmBean.isInsrtInd()) {
								orderPmBean.setOrderId(orderdata.getSysOrderId());
							}
							orderPmToSaveDB.add(orderPmBean);
						} else{
							//if order is not eligible
							if(orderPmBean.isUpdateInd()){
								orderPmBean.setPotentialAmt(0);
								orderPmBean.setPotentialQty(0);
								orderPmBean.setEarnedAmt(0);
								orderPmBean.setEarnedQty(0);
							    orderPmBean.setPendingAmt(0);	
							    orderPmBean.setPendingQty(0);
							    orderPmBean.setActiveCd(0);
							    orderPmBean.setEmpId(0);
								orderPmToSaveDB.add(orderPmBean);
							}
						}
					}

				} 
		  }
		// Update pm_status & other detaILS to DB
			if (!mbpmBasketDetails.getPmStatus().equals(PhotoOmniConstants.PM_STATUS_T)
					&& !mbpmBasketDetails.getPmStatus().equals(PhotoOmniConstants.PM_STATUS_R)) {
				if (mbpmBasketDetails.isBasketValid() && mbpmBasketDetails.isAllSoldCancl() ) {
					mbpmBasketDetails.setPmStatus(PhotoOmniConstants.PM_STATUS_E);
					// move Move Pending amount & Qty to Earned amount & Qty
					// columns
					mbpmBasketDetails.setOrderPmList(this.movePendingToErned(orderPmToSaveDB));
					// Save orderpmList to DB
					mbpmDao.updateOrderPm(mbpmBasketDetails);
				} else if (mbpmBasketDetails.isBasketValid()) {
					mbpmBasketDetails.setPmStatus(PhotoOmniConstants.PM_STATUS_P);
					mbpmBasketDetails.setOrderPmList(orderPmToSaveDB);
					// Save orderpmList to DB
					mbpmDao.updateOrderPm(mbpmBasketDetails);
				} else {
					if (mbpmBasketDetails.isKisokInternet()) {
						mbpmBasketDetails.setPmStatus("X");
					} else {
						mbpmBasketDetails.setPmStatus(PhotoOmniConstants.PM_STATUS_R);
					}
					// update om-order-pm table
					mbpmDao.rejectOrderPm(mbpmBasketDetails);
				}
			}
			
		} catch (Exception e) {
			
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Exception occured " + e.getMessage());
			}
			status = false;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("End of CalculateMBPromotionalMoney method of MBPromotionalMoneyBOImpl ");
		}

		return status;
	}
	
	

	/**
	 * @param mbpmBasketDetails
	 * @return boolean
	 */
	public void validateOrderPMObj(MBPMDetailsBean mbpmBasketDetails) {
		mbpmBasketDetails.setPmStatus(mbpmBasketDetails.getOrderDataList().get(0).getPmStatus());
		// PM calculation will not happen for the following condition
		if (mbpmBasketDetails.getPmStatus().equals(PhotoOmniConstants.PM_STATUS_T)
				|| mbpmBasketDetails.getPmStatus().equals(PhotoOmniConstants.PM_STATUS_R)) {
			// PM status is transfered(T) or Rejected(R) previously
			mbpmBasketDetails.setBasketValid(false);
			return;
		}
		
		int noOfOrders = mbpmBasketDetails.getOrderDataList().size();
		float totalFinalPrice = 0;
		float totalFinalPriceForProrate = 0;
		int rejectionCount = 0;
		int soldCancel = 0;
		for (OrderDetailData orderDetails : mbpmBasketDetails.getOrderDataList()) {
			
			//Any one expense order in a basket
			if (orderDetails.getExpenseInd()==1) {
				mbpmBasketDetails.setBasketValid(false);
				return;
			}

			//Employee discount applied for any one order in a basket
			if (orderDetails.getDiscountCardUsedInd() == 1) {
				mbpmBasketDetails.setBasketValid(false);
				return;
			}
			
			//Employee id is kiosk or internet for any one order
			if (orderDetails.getEmployeeId().equalsIgnoreCase(PhotoOmniConstants.ORDER_KIOSK)
					|| orderDetails.getEmployeeId().equalsIgnoreCase(PhotoOmniConstants.ORDER_INTERNET)) {				
				mbpmBasketDetails.setBasketValid(false);
				mbpmBasketDetails.setKisokInternet(true);
				return;
			}
			
			totalFinalPrice = totalFinalPrice + orderDetails.getFinalPrice();

			//Order status is SOLD
			if (orderDetails.getStatus().equalsIgnoreCase(
					PhotoOmniConstants.ORDER_STATUS_SOLD)) {
				soldCancel = soldCancel + 1;
			}
			
			//Employee not eligible for PM makes makes an order not eligible for PM
			if (!orderDetails.isPmEligible()) {
				orderDetails.setValidProration(false);
				rejectionCount = rejectionCount + 1;
				continue;
			}
			
			//Order status is CANCEL makes the order not eligible for PM
			if (orderDetails.getStatus().equalsIgnoreCase(
					PhotoOmniConstants.ORDER_CANCEL)) {
				orderDetails.setValidProration(false);
				rejectionCount = rejectionCount + 1;
				soldCancel = soldCancel + 1;
				continue;
			}

			//Order status SOLD and (sold amount = 0 OR sold amount < final price) makes an order not eligible for PM
			if (orderDetails.getStatus().equalsIgnoreCase(
					PhotoOmniConstants.ORDER_STATUS_SOLD)
					&& (orderDetails.getSoldAmount() == 0 || orderDetails
							.getSoldAmount() < orderDetails.getFinalPrice())) {
				orderDetails.setValidProration(false);
				rejectionCount = rejectionCount + 1;
				continue;
			}
			
			if (orderDetails.getCouponInd() == 1) {
				mbpmBasketDetails.setCouponValid(true);
			}
			
			totalFinalPriceForProrate = totalFinalPriceForProrate + orderDetails.getFinalPrice();
			
		}
		
		if (noOfOrders == rejectionCount) {
			mbpmBasketDetails.setBasketValid(false);
			return;
		}
		
		if (noOfOrders == soldCancel) {
			mbpmBasketDetails.setAllSoldCancl(true);
		}
		
		mbpmBasketDetails.setTotalFinalPrice(totalFinalPrice);
		mbpmBasketDetails.setTotalFinalPriceForProration(totalFinalPriceForProrate);
		mbpmBasketDetails.setValidOrderCount(noOfOrders - rejectionCount);

		return;
	}
	

	/**
	 * @param sysOrderId
	 * @param omPmBeanList
	 * @return dataBean
	 */
	public OrderPMDataBean getOrderPmDataBean(
			List<OrderPMDataBean> omPmBeanList, long sysOrderId) {
		OrderPMDataBean dataBean = null;

		for (OrderPMDataBean omPmBean : omPmBeanList) {
			if (omPmBean.getOrderId() == sysOrderId) {
				dataBean = omPmBean;
				dataBean.setUpdateInd(true);
				dataBean.setInsrtInd(false);
				break;
			}
		}
		if (null == dataBean) {
			dataBean = new OrderPMDataBean();
			dataBean.setUpdateInd(false);
			dataBean.setInsrtInd(true);
			dataBean.setOrderId(sysOrderId);
		}

		return dataBean;

	}
	

	/**
	 * @param orderPmList
	 * @return orderPmList
	 */
	public List<OrderPMDataBean> movePendingToErned(
			List<OrderPMDataBean> orderPmList) {
		for (OrderPMDataBean orderPm : orderPmList) {
			orderPm.setEarnedAmt(orderPm.getPendingAmt());
			orderPm.setEarnedQty(orderPm.getPendingQty());
			orderPm.setPendingQty(0);
			orderPm.setPendingAmt(0);
		}

		return orderPmList;
	}
	
 }
