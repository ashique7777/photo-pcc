/**
 * 
 */
package com.walgreens.batch.central.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.batch.central.bean.POFOrderVCRepBean;
import com.walgreens.batch.central.bean.PromotionalMoneyOrderDataBean;
import com.walgreens.batch.central.factory.BatchBO;
import com.walgreens.common.constant.PhotoOmniConstants;

/**
 * @author CTS
 * 
 */
public class PayOnFulfillmentBOImpl implements BatchBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PayOnFulfillmentBOImpl.class);

	@Override
	public void validateVendorPayment(POFOrderVCRepBean pofOrderVCRepBean) {
		LOGGER.debug("Pay On Fulfillment BO Impl validating Vendor Payment..");
		boolean flag = validatePaymentThreshold(pofOrderVCRepBean);
		if (flag) {
			LOGGER.debug("Pay On Fulfillment BO Impl updated by  Flag : " + flag);
			pofOrderVCRepBean.setStatusInd(PhotoOmniConstants.POF_STATUS_IND_D);
		} else {
			LOGGER.debug("Pay On Fulfillment BO Impl updated by Flag : " + flag);
			pofOrderVCRepBean.setStatusInd(PhotoOmniConstants.POF_STATUS_IND_V);
		}
	}

	/**
	 * This method is used for validating vendor Cost Data
	 * 
	 * @param pofOrderVCRepBean
	 * @return
	 */
	private boolean validatePaymentThreshold(POFOrderVCRepBean pofOrderVCRepBean) {
		boolean isValid = false;
		int recalculateCostInd = pofOrderVCRepBean.getRecalculatedCostCD();
		/** Calculate Central Cost **/
		calculateCentralVendorpayment(pofOrderVCRepBean);
		isValid = validation1(pofOrderVCRepBean);
		if (isValid && recalculateCostInd == 1) {
			isValid = validation2(pofOrderVCRepBean);
		}
		return isValid;
	}

	/**
	 * This Method is used for Validating Vendor Payment Cost with Item sold
	 * Amount
	 * 
	 * @param pofOrderVCRepBean
	 * @return
	 */
	private boolean validation1(POFOrderVCRepBean pofOrderVCRepBean) {

		int thresholdValue = pofOrderVCRepBean.getCostThresoldCap();
		double itemSoldAmt = pofOrderVCRepBean.getSoldAmount();
		double vendorPaymentAmt = pofOrderVCRepBean.getVendPaymentAmt();
		if (vendorPaymentAmt <= (thresholdValue * itemSoldAmt) / 100) {
			return true;
		} else
			return false;

	}

	/**
	 * This Method is used for Validating Vendor Payment Cost with Central
	 * Calculated Cost
	 * 
	 * @param pofOrderVCRepBean
	 * @return
	 */
	private boolean validation2(POFOrderVCRepBean pofOrderVCRepBean) {
		double vendorPaymentAmt = pofOrderVCRepBean.getVendPaymentAmt();
		double centralClcCost = pofOrderVCRepBean.getCalcVendPayment();
		if (vendorPaymentAmt <= centralClcCost) {
			return true;
		} else
			return false;

	}

	/**
	 * This Method is used for Calculating Central Cost
	 * 
	 * @param pofOrderVCRepBean
	 */
	private void calculateCentralVendorpayment(
			POFOrderVCRepBean pofOrderVCRepBean) {
		double cost = pofOrderVCRepBean.getVendCost();
		String costType = pofOrderVCRepBean.getVendCostType();
		double shippingCost = pofOrderVCRepBean.getVendShippingCost();
		String shippingCostType = pofOrderVCRepBean.getVendShippingCostType();
		double vendorItemCost = 0.0;
		double calcShoppingCost = 0.0;
		double finalVendorCost = 0.0;

		if (costType.equalsIgnoreCase("P")) {
			vendorItemCost = cost * 1;

		} else if (null != costType
				&& (costType.equalsIgnoreCase("Q") || costType.trim()
						.equals(""))) {
			vendorItemCost = cost * pofOrderVCRepBean.getQuantity();
		}
		if (shippingCostType.equalsIgnoreCase("P")) {

			calcShoppingCost = shippingCost * 1;

		} else if (null != shippingCostType
				&& (shippingCostType.equalsIgnoreCase("Q") || shippingCostType
						.trim().equals(""))) {

			calcShoppingCost = shippingCost * pofOrderVCRepBean.getQuantity();

		}

		finalVendorCost = vendorItemCost + calcShoppingCost;

		if (finalVendorCost == 0) {
			finalVendorCost = 0.667 * pofOrderVCRepBean.getCalculatedPrice();
		}
		// NumberFormat nf=NumberFormat.getInstance();
		// nf.setMaximumFractionDigits(Integer.MAX_VALUE);
		String finalVendorCostString = String.format("%.2f", finalVendorCost);
		finalVendorCost = Double.parseDouble(finalVendorCostString);

		/** Setting calculated Central cost to POF data Bean **/
		pofOrderVCRepBean.setCalcVendPayment(finalVendorCost);

	}

	@Override
	public void calculatePM(PromotionalMoneyOrderDataBean pmOrderDataBean,
			boolean isPMReject) {

	}

}
