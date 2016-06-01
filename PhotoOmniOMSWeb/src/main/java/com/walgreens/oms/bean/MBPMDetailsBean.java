/**
 * 
 */
package com.walgreens.oms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CTS
 * 
 */
public class MBPMDetailsBean {

	private long sysShoppingCartId;
	private String orderPlacedDTTM;
	List<OrderDetailData> orderDataList = new ArrayList<OrderDetailData>();
	private boolean isBasketValid = true;
	private boolean isEmpIdKiosk;
	private float totalFinalPrice;
	private float totalFinalPriceForProration;
	private boolean isCouponValid;
	private boolean allSoldCancl;
	private String pmStatus;
	private boolean isKisokInternet;
	private int validOrderCount;

	public boolean isKisokInternet() {
		return isKisokInternet;
	}

	public void setKisokInternet(boolean isKisokInternet) {
		this.isKisokInternet = isKisokInternet;
	}

	private List<OrderPMDataBean> orderPmList = new ArrayList<OrderPMDataBean>();

	public List<OrderPMDataBean> getOrderPmList() {
		return orderPmList;
	}

	public void setOrderPmList(List<OrderPMDataBean> orderPmList) {
		this.orderPmList = orderPmList;
	}

	public boolean isAllSoldCancl() {
		return allSoldCancl;
	}

	public void setAllSoldCancl(boolean allSoldCancl) {
		this.allSoldCancl = allSoldCancl;
	}

	public String getPmStatus() {
		return pmStatus;
	}

	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}

	/**
	 * @return the totalFinalPrice
	 */
	public float getTotalFinalPrice() {
		return totalFinalPrice;
	}

	/**
	 * @param totalFinalPrice
	 *            the totalFinalPrice to set
	 */
	public void setTotalFinalPrice(float totalFinalPrice) {
		this.totalFinalPrice = totalFinalPrice;
	}

	/**
	 * @return the sysShoppingCartId
	 */
	public long getSysShoppingCartId() {
		return sysShoppingCartId;
	}

	/**
	 * @param sysShoppingCartId
	 *            the sysShoppingCartId to set
	 */
	public void setSysShoppingCartId(long sysShoppingCartId) {
		this.sysShoppingCartId = sysShoppingCartId;
	}

	/**
	 * @return the orderPlacedDTTM
	 */
	public String getOrderPlacedDTTM() {
		return orderPlacedDTTM;
	}

	/**
	 * @param orderPlacedDTTM
	 *            the orderPlacedDTTM to set
	 */
	public void setOrderPlacedDTTM(String orderPlacedDTTM) {
		this.orderPlacedDTTM = orderPlacedDTTM;
	}

	/**
	 * @return the isBasketValid
	 */
	public boolean isBasketValid() {
		return isBasketValid;
	}

	/**
	 * @param isBasketValid
	 *            the isBasketValid to set
	 */
	public void setBasketValid(boolean isBasketValid) {
		this.isBasketValid = isBasketValid;
	}

	/**
	 * @return the isCouponValid
	 */
	public boolean isCouponValid() {
		return isCouponValid;
	}

	/**
	 * @param isCouponValid
	 *            the isCouponValid to set
	 */
	public void setCouponValid(boolean isCouponValid) {
		this.isCouponValid = isCouponValid;
	}

	/**
	 * @return the isEmpIdKiosk
	 */
	public boolean isEmpIdKiosk() {
		return isEmpIdKiosk;
	}

	/**
	 * @param isEmpIdKiosk
	 *            the isEmpIdKiosk to set
	 */
	public void setEmpIdKiosk(boolean isEmpIdKiosk) {
		this.isEmpIdKiosk = isEmpIdKiosk;
	}

	/**
	 * @return the orderDataList
	 */
	public List<OrderDetailData> getOrderDataList() {
		return orderDataList;
	}

	/**
	 * @param orderDataList
	 *            the orderDataList to set
	 */
	public void setOrderDataList(List<OrderDetailData> orderDataList) {
		this.orderDataList = orderDataList;
	}

	/**
	 * @return
	 */
	public float getTotalFinalPriceForProration() {
		return totalFinalPriceForProration;
	}

	/**
	 * @param totalFinalPriceForProration
	 */
	public void setTotalFinalPriceForProration(float totalFinalPriceForProration) {
		this.totalFinalPriceForProration = totalFinalPriceForProration;
	}

	/**
	 * @return validOrderCount
	 */
	public int getValidOrderCount() {
		return validOrderCount;
	}

	/**
	 * @param validOrderCount
	 */
	public void setValidOrderCount(int validOrderCount) {
		this.validOrderCount = validOrderCount;
	}
	
	

}
