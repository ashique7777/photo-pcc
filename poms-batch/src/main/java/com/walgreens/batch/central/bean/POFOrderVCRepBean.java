/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author CTS
 *
 */
public class POFOrderVCRepBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Timestamp originalOrderPlcDttm;
	String orderPlacedDttm;
	long sysPayOnOrderVCId;
	long locId;
	String orderNo;
	int envNo;
	long prodId;
	String ediUpc;
	int quantity;
	long vendorId;
	Date asnRcvDate;
	Date completedDttm;
	Date soldDttm;
	Date reportingDttm;
	double vendPaymentAmt;
	double calculatedPrice;
	double calcVendPayment;
	double prevVendPaymentAmt;
	String statusInd;
	String approveBy;
	Date approveDttm;
	Date ediTransferDt;
	String emailSendInd;
	String createUserId;
	Date createDttm;
	String updatedUserId;
	Date updatedDttm;
	int costThresoldCap;
	int recalculatedCostCD;
	double vendCost;
	String vendCostType;
	double vendShippingCost;
	String vendShippingCostType;
	double itemCost;
	
	
	
	//added for DAT file 
	int locationNumber;
	
	double soldAmount;
	String productDiscription;
	String productWIC;
	String productUPC;
	String locationType;
	String marketVendorNumber;
	 List<VendorBean>  vendorList = new ArrayList<VendorBean>();
	 Date timeDoneSoldDttm;
	 String deptNumber;
	
	//added for IRS File
	long orderId;
	String orderNumber;
	String prodNumber;
	String vendorNumber;
	String envQuantity;
	
	
	
	
	
	/**
	 * @return the envQuantity
	 */
	public String getEnvQuantity() {
		return envQuantity;
	}
	/**
	 * @param envQuantity the envQuantity to set
	 */
	public void setEnvQuantity(String envQuantity) {
		this.envQuantity = envQuantity;
	}
	/**
	 * @return the deptNumber
	 */
	public String getDeptNumber() {
		return deptNumber;
	}
	/**
	 * @param deptNumber the deptNumber to set
	 */
	public void setDeptNumber(String deptNumber) {
		this.deptNumber = deptNumber;
	}
	/**
	 * @return the timeDoneSoldDttm
	 */
	public Date getTimeDoneSoldDttm() {
		return timeDoneSoldDttm;
	}
	/**
	 * @param timeDoneSoldDttm the timeDoneSoldDttm to set
	 */
	public void setTimeDoneSoldDttm(Date timeDoneSoldDttm) {
		this.timeDoneSoldDttm = timeDoneSoldDttm;
	}
	/**
	 * @return the marketVendorNumber
	 */
	public String getMarketVendorNumber() {
		return marketVendorNumber;
	}
	/**
	 * @param marketVendorNumber the marketVendorNumber to set
	 */
	public void setMarketVendorNumber(String marketVendorNumber) {
		this.marketVendorNumber = marketVendorNumber;
	}
	/**
	 * @return the itemCost
	 */
	public double getItemCost() {
		return itemCost;
	}
	/**
	 * @param itemCost the itemCost to set
	 */
	public void setItemCost(double itemCost) {
		this.itemCost = itemCost;
	}
	/**
	 * @return the sysPayOnOrderVCId
	 */
	public long getSysPayOnOrderVCId() {
		return sysPayOnOrderVCId;
	}
	/**
	 * @param sysPayOnOrderVCId the sysPayOnOrderVCId to set
	 */
	public void setSysPayOnOrderVCId(long sysPayOnOrderVCId) {
		this.sysPayOnOrderVCId = sysPayOnOrderVCId;
	}
	/**
	 * @return the locId
	 */
	public long getLocId() {
		return locId;
	}
	/**
	 * @param locId the locId to set
	 */
	public void setLocId(long locId) {
		this.locId = locId;
	}
	/**
	 * @return the prodId
	 */
	public long getProdId() {
		return prodId;
	}
	/**
	 * @param prodId the prodId to set
	 */
	public void setProdId(long prodId) {
		this.prodId = prodId;
	}
	/**
	 * @return the vendorId
	 */
	public long getVendorId() {
		return vendorId;
	}
	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the originalOrderPlcDttm
	 */
	public Timestamp getOriginalOrderPlcDttm() {
		return originalOrderPlcDttm;
	}
	/**
	 * @param originalOrderPlcDttm the originalOrderPlcDttm to set
	 */
	public void setOriginalOrderPlcDttm(Timestamp originalOrderPlcDttm) {
		this.originalOrderPlcDttm = originalOrderPlcDttm;
	}
	/**
	 * @return the prodNumber
	 */
	public String getProdNumber() {
		return prodNumber;
	}
	/**
	 * @param prodNumber the prodNumber to set
	 */
	public void setProdNumber(String prodNumber) {
		this.prodNumber = prodNumber;
	}
	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return the costThresoldCap
	 */
	public int getCostThresoldCap() {
		return costThresoldCap;
	}
	/**
	 * @param costThresoldCap the costThresoldCap to set
	 */
	public void setCostThresoldCap(int costThresoldCap) {
		this.costThresoldCap = costThresoldCap;
	}
	/**
	 * @return the recalculatedCostCD
	 */
	public int getRecalculatedCostCD() {
		return recalculatedCostCD;
	}
	/**
	 * @param recalculatedCostCD the recalculatedCostCD to set
	 */
	public void setRecalculatedCostCD(int recalculatedCostCD) {
		this.recalculatedCostCD = recalculatedCostCD;
	}
	/**
	 * @return the vendCost
	 */
	public double getVendCost() {
		return vendCost;
	}
	/**
	 * @param vendCost the vendCost to set
	 */
	public void setVendCost(double vendCost) {
		this.vendCost = vendCost;
	}
	/**
	 * @return the vendCostType
	 */
	public String getVendCostType() {
		return vendCostType;
	}
	/**
	 * @param vendCostType the vendCostType to set
	 */
	public void setVendCostType(String vendCostType) {
		this.vendCostType = vendCostType;
	}
	/**
	 * @return the vendShippingCost
	 */
	public double getVendShippingCost() {
		return vendShippingCost;
	}
	/**
	 * @param vendShippingCost the vendShippingCost to set
	 */
	public void setVendShippingCost(double vendShippingCost) {
		this.vendShippingCost = vendShippingCost;
	}
	/**
	 * @return the vendShippingCostType
	 */
	public String getVendShippingCostType() {
		return vendShippingCostType;
	}
	/**
	 * @param vendShippingCostType the vendShippingCostType to set
	 */
	public void setVendShippingCostType(String vendShippingCostType) {
		this.vendShippingCostType = vendShippingCostType;
	}
	/**
	 * @return the vendorList
	 */
	public List<VendorBean> getVendorList() {
		return vendorList;
	}
	/**
	 * @param vendorList the vendorList to set
	 */
	public void setVendorList(List<VendorBean> vendorList) {
		this.vendorList = vendorList;
	}
	
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getProductDiscription() {
		return productDiscription;
	}
	public void setProductDiscription(String productDiscription) {
		this.productDiscription = productDiscription;
	}
	public String getProductWIC() {
		return productWIC;
	}
	public void setProductWIC(String productWIC) {
		this.productWIC = productWIC;
	}
	public String getProductUPC() {
		return productUPC;
	}
	public void setProductUPC(String productUPC) {
		this.productUPC = productUPC;
	}
	public int getLocationNumber() {
		return locationNumber;
	}
	public void setLocationNumber(int locationNumber) {
		this.locationNumber = locationNumber;
	}
	public String getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	public double getSoldAmount() {
		return soldAmount;
	}
	public void setSoldAmount(double soldAmount) {
		this.soldAmount = soldAmount;
	}
	

	/**
	 * @return the orderPlacedDttm
	 */
	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	/**
	 * @param orderPlacedDttm the orderPlacedDttm to set
	 */
	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	
	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return the envNo
	 */
	public int getEnvNo() {
		return envNo;
	}
	/**
	 * @param envNo the envNo to set
	 */
	public void setEnvNo(int envNo) {
		this.envNo = envNo;
	}
	
	/**
	 * @return the ediUpc
	 */
	public String getEdiUpc() {
		return ediUpc;
	}
	/**
	 * @param ediUpc the ediUpc to set
	 */
	public void setEdiUpc(String ediUpc) {
		this.ediUpc = ediUpc;
	}
	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * @return the asnRcvDate
	 */
	public Date getAsnRcvDate() {
		return asnRcvDate;
	}
	/**
	 * @param asnRcvDate the asnRcvDate to set
	 */
	public void setAsnRcvDate(Date asnRcvDate) {
		this.asnRcvDate = asnRcvDate;
	}
	/**
	 * @return the completedDttm
	 */
	public Date getCompletedDttm() {
		return completedDttm;
	}
	/**
	 * @param completedDttm the completedDttm to set
	 */
	public void setCompletedDttm(Date completedDttm) {
		this.completedDttm = completedDttm;
	}
	/**
	 * @return the soldDttm
	 */
	public Date getSoldDttm() {
		return soldDttm;
	}
	/**
	 * @param soldDttm the soldDttm to set
	 */
	public void setSoldDttm(Date soldDttm) {
		this.soldDttm = soldDttm;
	}
	/**
	 * @return the reportingDttm
	 */
	public Date getReportingDttm() {
		return reportingDttm;
	}
	/**
	 * @param reportingDttm the reportingDttm to set
	 */
	public void setReportingDttm(Date reportingDttm) {
		this.reportingDttm = reportingDttm;
	}
	/**
	 * @return the vendPaymentAmt
	 */
	public double getVendPaymentAmt() {
		return vendPaymentAmt;
	}
	/**
	 * @param vendPaymentAmt the vendPaymentAmt to set
	 */
	public void setVendPaymentAmt(double vendPaymentAmt) {
		this.vendPaymentAmt = vendPaymentAmt;
	}
	/**
	 * @return the calculatedPrice
	 */
	public double getCalculatedPrice() {
		return calculatedPrice;
	}
	/**
	 * @param calculatedPrice the calculatedPrice to set
	 */
	public void setCalculatedPrice(double calculatedPrice) {
		this.calculatedPrice = calculatedPrice;
	}
	/**
	 * @return the calcVendPayment
	 */
	public double getCalcVendPayment() {
		return calcVendPayment;
	}
	/**
	 * @param calcVendPayment the calcVendPayment to set
	 */
	public void setCalcVendPayment(double calcVendPayment) {
		this.calcVendPayment = calcVendPayment;
	}
	/**
	 * @return the prevVendPaymentAmt
	 */
	public double getPrevVendPaymentAmt() {
		return prevVendPaymentAmt;
	}
	/**
	 * @param prevVendPaymentAmt the prevVendPaymentAmt to set
	 */
	public void setPrevVendPaymentAmt(double prevVendPaymentAmt) {
		this.prevVendPaymentAmt = prevVendPaymentAmt;
	}
	/**
	 * @return the statusInd
	 */
	public String getStatusInd() {
		return statusInd;
	}
	/**
	 * @param statusInd the statusInd to set
	 */
	public void setStatusInd(String statusInd) {
		this.statusInd = statusInd;
	}
	/**
	 * @return the approveBy
	 */
	public String getApproveBy() {
		return approveBy;
	}
	/**
	 * @param approveBy the approveBy to set
	 */
	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}
	/**
	 * @return the approveDttm
	 */
	public Date getApproveDttm() {
		return approveDttm;
	}
	/**
	 * @param approveDttm the approveDttm to set
	 */
	public void setApproveDttm(Date approveDttm) {
		this.approveDttm = approveDttm;
	}
	/**
	 * @return the ediTransferDt
	 */
	public Date getEdiTransferDt() {
		return ediTransferDt;
	}
	/**
	 * @param ediTransferDt the ediTransferDt to set
	 */
	public void setEdiTransferDt(Date ediTransferDt) {
		this.ediTransferDt = ediTransferDt;
	}
	/**
	 * @return the emailSendInd
	 */
	public String getEmailSendInd() {
		return emailSendInd;
	}
	/**
	 * @param emailSendInd the emailSendInd to set
	 */
	public void setEmailSendInd(String emailSendInd) {
		this.emailSendInd = emailSendInd;
	}
	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * @return the createDttm
	 */
	public Date getCreateDttm() {
		return createDttm;
	}
	/**
	 * @param createDttm the createDttm to set
	 */
	public void setCreateDttm(Date createDttm) {
		this.createDttm = createDttm;
	}
	/**
	 * @return the updatedUserId
	 */
	public String getUpdatedUserId() {
		return updatedUserId;
	}
	/**
	 * @param updatedUserId the updatedUserId to set
	 */
	public void setUpdatedUserId(String updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
	/**
	 * @return the updatedDttm
	 */
	public Date getUpdatedDttm() {
		return updatedDttm;
	}
	/**
	 * @param updatedDttm the updatedDttm to set
	 */
	public void setUpdatedDttm(Date updatedDttm) {
		this.updatedDttm = updatedDttm;
	}
	
	

}
