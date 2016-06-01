package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

public class PosReconciliationTransferBean {

	private PosOrderExceptionDataBean posOrderExceptionDataBean;
	private OrderHistoryBean orderHistoryBean;
	private PosTransactionDetails PosTransactionDetails;
	private PosCostCalCulationBean posCostCalCulationBean;
	
	private String orderSoldDttm;
	private String orderPlaceDttm;
	private long owingLocId;
	private String finalOrderStatus;
	private long sysOrderId;
	private int calenderId;
	private int printsReturnedQty;
	private String pmStatus;
	
	public String getFinalOrderStatus() {
		return finalOrderStatus;
	}

	public void setFinalOrderStatus(String finalOrderStatus) {
		this.finalOrderStatus = finalOrderStatus;
	}

	public long getSysOrderId() {
		return sysOrderId;
	}

	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}

	public PosCostCalCulationBean getPosCostCalCulationBean() {
		return posCostCalCulationBean;
	}

	public void setPosCostCalCulationBean(
			PosCostCalCulationBean posCostCalCulationBean) {
		this.posCostCalCulationBean = posCostCalCulationBean;
	}

	public PosOrderExceptionDataBean getPosOrderExceptionDataBean() {
		return posOrderExceptionDataBean;
	}

	public void setPosOrderExceptionDataBean(
			PosOrderExceptionDataBean posOrderExceptionDataBean) {
		this.posOrderExceptionDataBean = posOrderExceptionDataBean;
	}

	public OrderHistoryBean getOrderHistoryBean() {
		return orderHistoryBean;
	}

	public void setOrderHistoryBean(OrderHistoryBean orderHistoryBean) {
		this.orderHistoryBean = orderHistoryBean;
	}

	public PosTransactionDetails getPosTransactionDetails() {
		return PosTransactionDetails;
	}

	public void setPosTransactionDetails(PosTransactionDetails posTransactionDetails) {
		PosTransactionDetails = posTransactionDetails;
	}

	public int getCalenderId() {
		return calenderId;
	}

	public void setCalenderId(int calenderId) {
		this.calenderId = calenderId;
	}

	public int getPrintsReturnedQty() {
		return printsReturnedQty;
	}

	public void setPrintsReturnedQty(int printsReturnedQty) {
		this.printsReturnedQty = printsReturnedQty;
	}

	public String getPmStatus() {
		return pmStatus;
	}

	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}

	public String getOrderSoldDttm() {
		return orderSoldDttm;
	}

	public void setOrderSoldDttm(String orderSoldDttm) {
		this.orderSoldDttm = orderSoldDttm;
	}

	public String getOrderPlaceDttm() {
		return orderPlaceDttm;
	}

	public void setOrderPlaceDttm(String orderPlaceDttm) {
		this.orderPlaceDttm = orderPlaceDttm;
	}

	public long getOwingLocId() {
		return owingLocId;
	}

	public void setOwingLocId(long owingLocId) {
		this.owingLocId = owingLocId;
	}

}
