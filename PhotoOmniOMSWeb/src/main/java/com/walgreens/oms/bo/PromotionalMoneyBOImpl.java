/**
 * 
 */
package com.walgreens.oms.bo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.oms.bean.OrderPMDataBean;
import com.walgreens.oms.bean.PMOrderDetailBean;
import com.walgreens.oms.bean.PMOrderLineBean;
import com.walgreens.oms.bean.PMRule;
import com.walgreens.oms.bean.PMRuleDetail;
import com.walgreens.oms.dao.PromotionalMoneyDAO;
import com.walgreens.oms.factory.OmsDAOFactory;

/**
 * @author CTS
 *
 */
@Component("PromotionalMoneyBO")
public class PromotionalMoneyBOImpl implements PromotionalMoneyBO {
	
	@Autowired
	private OmsDAOFactory omsDAOFactory; // Use to call the DAO factory class
	
	/**
	 * logger to log the details.
	 */
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PromotionalMoneyBOImpl.class);
	
	/* (non-Javadoc)
	 * @see com.walgreens.common.bo.PromotionalMoneyBO#calculatePromotionalMoney(long, java.lang.String)
	 * orderPlacedDTTM should be in 'YYYY-MM-DD HH24:MI:SS' format
	 */
	public boolean calculatePromotionalMoney(long sysOrderID, String orderPlacedDTTM){
		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering calculatePromotionalMoney method of PromotionalMoneyBOImpl ");
		}
		
		PromotionalMoneyDAO pmDao = null;			
		pmDao = omsDAOFactory.getPromotionalMoneyDAO();
		boolean isPMValid = false;
		boolean status = true;
		int orderPLUCnt = 0;
		boolean isOrderPLUChecked = false;
		int pmRejectedCount = 0;
		try {
			PMOrderDetailBean orderDetails = pmDao.getOrderPMDetails(sysOrderID, orderPlacedDTTM);
			orderDetails.setOrderId(sysOrderID);
			orderDetails.setOrderPlacedDTTM(orderPlacedDTTM); //conversion not needed
			
			List<OrderPMDataBean> orderPMList = new ArrayList<OrderPMDataBean>();
			List<OrderPMDataBean> orderPmToSaveDB = new ArrayList<OrderPMDataBean>();
			
			// Validate order object whether it is eligible for PM or not
			isPMValid = this.validateOrderPMObj(orderDetails);
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Validation of PM eligibility for the order  "+ sysOrderID + " is : "+isPMValid);
			}
			if(isPMValid){
				//fetch order PM for this order
				orderPMList = pmDao.fetchOrderPM(orderDetails.getOrderId(), orderDetails.getOrderPlacedDTTM());
				
				if (LOGGER.isDebugEnabled()) {
					if(orderPMList.isEmpty()){
						LOGGER.debug("Order is not exist in OM_ORDER_PM table  ");
					}
				}
				
				// Get Product associated with this order
				pmDao.getPMOrderLine(orderDetails);
				
				// Get PM Rule for each product
				for(PMOrderLineBean orderitem :orderDetails.getItemList()){
					
					boolean isPMCalculate = false;
					// Fetching pm rule
					PMRule pmRule = pmDao.getPMRule(orderitem.getProductId(), orderDetails.getOrderPlacedDTTM(),orderDetails.getLocationId());
					
					if(null != pmRule){
						
						//if PM rule isDiscountApplicableCd is 0 them PM is rejected
						if(!pmRule.isDiscountApplicableCd()){
							
							if(!isOrderPLUChecked){
								// Get Order PLU Count
								orderPLUCnt = pmDao.getOrderPLUCnt(orderDetails.getOrderId(), orderDetails.getOrderPlacedDTTM());
								isOrderPLUChecked = true;
							}
							
							if(orderPLUCnt == 0){
								int orderLinePLUCnt = 0;
								orderLinePLUCnt = pmDao.getOrderLinePLUCnt(orderitem.getOrderLineId(), orderDetails.getOrderPlacedDTTM());
								
								if(orderLinePLUCnt == 0){
									isPMCalculate = true;
								}
							}							
						} else{
							isPMCalculate = true;
						}
					}else{
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("PM rule is not available for the product ..." + orderitem.getProductId());
						}
					}
					//get order pm bean
					OrderPMDataBean orderPmBean = this.getOrderPmDataBean(orderPMList, orderitem.getProductId());
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("PM Calculated for the product " + orderitem.getProductId() +  " is "+isPMCalculate );
					}
					if(isPMCalculate){		
											
						// calculate PM starts
						int itemQty = 0;
						if (pmRule.getPmRuleType().equalsIgnoreCase(PhotoOmniConstants.BY_SETS)) {
							itemQty = orderitem.getSetsQty();
						} else {
							itemQty = orderitem.getQuantity();
						}

						double itemAmt = 0;
						List<PMRuleDetail> ruleDetails = pmRule.getRuleDetails();

						if (pmRule.getTierType().equalsIgnoreCase(PhotoOmniConstants.STRAIGHT)) {

							if (pmRule.getPmRuleType().equalsIgnoreCase(PhotoOmniConstants.BY_ORDER)) {
								for (PMRuleDetail rule : ruleDetails) {
									if (itemQty >= rule.getMinTier() && itemQty <= rule.getMaxTier()) {
										//Use Quantity as 1
										itemAmt = rule.getPmAmount();
										break;
									}
								}
							} else {
								for (PMRuleDetail rule : ruleDetails) {
									if (itemQty >= rule.getMinTier() && itemQty <= rule.getMaxTier()) {
										itemAmt = itemQty * rule.getPmAmount();
										break;
									}
								}
							}

						} else if (pmRule.getTierType().equalsIgnoreCase(PhotoOmniConstants.GRADUATED)) {

							if (pmRule.getPmRuleType().equalsIgnoreCase(PhotoOmniConstants.BY_ORDER)) {
								//Use Quantity as 1
								for (PMRuleDetail rule : ruleDetails) {
									if (itemQty >= rule.getMinTier() && itemQty <= rule.getMaxTier()) {
										itemAmt = itemAmt + rule.getPmAmount();
									} else if (itemQty >= rule.getMinTier()) {
										itemAmt = itemAmt + rule.getPmAmount();
									}
								}

							} else {
								for (PMRuleDetail rule : ruleDetails) {
									if (itemQty >= rule.getMinTier() && itemQty <= rule.getMaxTier()) {
										itemAmt = itemAmt + rule.getPmAmount() * ((itemQty - rule.getMinTier()) + 1);
									} else if (itemQty >= rule.getMinTier()) {
										itemAmt = itemAmt + rule.getPmAmount() * ((rule.getMaxTier() - rule.getMinTier()) + 1);
									}
								}
							}
						}
						// calculate PM ends
						
						orderPmBean.setActiveCd(1);
						orderPmBean.setPendingAmt(itemAmt);					
						orderPmBean.setPendingQty(itemQty);
						orderPmBean.setEmpId(orderDetails.getEmpId());
						orderPmBean.setPotentialAmt(itemAmt);
						orderPmBean.setPotentialQty(itemQty);					
						orderPmBean.setPmId(pmRule.getPmId());
						orderPmBean.setEmpId(orderDetails.getEmpId());
						orderPmBean.setOrderPlacedDttm(orderDetails.getOrderPlacedDTTM());
						//if it's a new entry, populate other data
						if(orderPmBean.isInsrtInd()){							
							orderPmBean.setOrderId(orderDetails.getOrderId());							
						}
						//Add to the final list
						orderPmToSaveDB.add(orderPmBean);
					} else {
						pmRejectedCount++;
						//if product not eligible for PM & already exists in DB update with defaults
						// this block not needed as per new rule
						if(orderPmBean.isUpdateInd()){
							orderPmBean.setPmId(pmRule.getPmId());
							orderPmBean.setOrderId(orderDetails.getOrderId());
							orderPmBean.setOrderPlacedDttm(orderDetails.getOrderPlacedDTTM());
							orderPmBean.setActiveCd(0);
							orderPmBean.setProductId(orderitem.getProductId());						
							orderPmBean.setInsrtInd(true);
							//Add to the final list
							orderPmToSaveDB.add(orderPmBean);
						}
						
						
					}
				}//end order item loop
				
				// Add orderPMlist to orderObj
				orderDetails.setOrderPmList(orderPmToSaveDB);
			}
			
			//Check if PM rejected for all products
			if(orderDetails.getItemList().size() == pmRejectedCount){
				isPMValid = false;
			}
			
			// Update pm_status & other detaILS to DB 
			this.updatePMStatusToDB(orderDetails,isPMValid,pmDao);
			
		} catch (Exception e) {			
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Exception occured " + e.getMessage());
			}
			status = false;
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("End of calculatePromotionalMoney method of PromotionalMoneyBOImpl ");
		}
		
		return status;
	}
	
	private void updatePMStatusToDB(PMOrderDetailBean orderDetails,boolean isPMValid,PromotionalMoneyDAO pmDao) throws SQLException{ 
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter into PromotionalMoneyBOImpl.updatePMStatusToDB() method");
		}
		if(!orderDetails.getPmStatus().equals(PhotoOmniConstants.PM_STATUS_T) && !orderDetails.getPmStatus().equals(PhotoOmniConstants.PM_STATUS_R)){
			if(isPMValid && orderDetails.getOrderStatus().equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD)){
				orderDetails.setPmStatus(PhotoOmniConstants.PM_STATUS_E);
				//move Move Pending amount & Qty to Earned amount & Qty columns
				orderDetails.setOrderPmList(this.movePendingToErned(orderDetails.getOrderPmList()));
				//Save orderpmList to DB			
				pmDao.updateOrderPm(orderDetails);
			}else if(isPMValid){
				orderDetails.setPmStatus(PhotoOmniConstants.PM_STATUS_P);
				//Save orderpmList to DB
				pmDao.updateOrderPm(orderDetails);
			} else {
				if(orderDetails.getEmployeeID().equalsIgnoreCase(PhotoOmniConstants.ORDER_INTERNET) || orderDetails.getEmployeeID().equalsIgnoreCase(PhotoOmniConstants.ORDER_KIOSK)){
					// It seems this block will not execute as we are returning false from validation for this scenario
					orderDetails.setPmStatus("X");
				}else{
					orderDetails.setPmStatus(PhotoOmniConstants.PM_STATUS_R);				
				}
				//update om-order-pm table 
				pmDao.rejectOrderPm(orderDetails);
			}

		}
	}
	
	private List<OrderPMDataBean> movePendingToErned(List<OrderPMDataBean> orderPmList){
		for(OrderPMDataBean orderPm: orderPmList){
			orderPm.setEarnedAmt(orderPm.getPendingAmt());
			orderPm.setEarnedQty(orderPm.getPendingQty());
			orderPm.setPendingQty(0);
			orderPm.setPendingAmt(0);
		}
		
		return orderPmList;
	}
	
	private OrderPMDataBean getOrderPmDataBean(List<OrderPMDataBean> omPmBeanList, long productId){
		OrderPMDataBean dataBean = null;
		
			for(OrderPMDataBean omPmBean: omPmBeanList){
				if(omPmBean.getProductId() == productId){
					dataBean = omPmBean;
					break;
				}
			}	
			if(null == dataBean){
				dataBean = new OrderPMDataBean();
				dataBean.setUpdateInd(false);
				dataBean.setInsrtInd(true);
				dataBean.setProductId(productId);
			} 
		
		return dataBean;
		
	}
	
	
	/**
	 * @param orderDetails
	 * @return
	 */
	private boolean validateOrderPMObj(PMOrderDetailBean orderDetails){
		
		// PM calculation will not happen for the following condition 
		if(orderDetails.getPmStatus().equals(PhotoOmniConstants.PM_STATUS_T) || orderDetails.getPmStatus().equals(PhotoOmniConstants.PM_STATUS_R)){
			//PM status is transfered(T) or Rejected(R) previously
			return false;
		}else if(orderDetails.getOrderStatus().equalsIgnoreCase(PhotoOmniConstants.ORDER_CANCEL)){
			//Order status is CANCEL / COMPLETE
			return false;
		}else if(orderDetails.isExpenseInd()){
			//Expense orders are not considered for PM 
			return false;
		}else if(orderDetails.getOrderStatus().equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD) && orderDetails.getSoldAmount() == 0){
			//Order status = SOLD & Amount paid = 0,
			return false;
		}else if(orderDetails.getOrderStatus().equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD) && orderDetails.getSoldAmount() < orderDetails.getFinalPrice()){
			//Order status = SOLD  & amount paid by the customer < calculated price of the order,
			return false;
		} else if(!(orderDetails.getPmEligibleCd() == 1)){
			//Employee PM eligibility code is 0 i.e. not eligible
			return false;
		}else if(orderDetails.getDiscntCardInd() == 1){
			//Discount_applicable_ind = 1
			return false;
		}else if(orderDetails.getEmployeeID().equalsIgnoreCase(PhotoOmniConstants.ORDER_INTERNET) || orderDetails.getEmployeeID().equalsIgnoreCase(PhotoOmniConstants.ORDER_KIOSK)){
			//Label printed employee id is either ‘Kiosk’ or ‘Internet’.
			return false;
		}
		return true;
		
	}
}
