package com.walgreens.batch.central.bean;

import java.util.List;


public class PosExceptionTransferBean {
	
	private List<PosOrderExceptionDataBean> posOrderExceptionDataBeanList;
	private List<OrderHistoryBean> orderHistoryBeanList;
	private List<WasteQtyBean> wasteQtyBeanList;	
	
	
	public List<PosOrderExceptionDataBean> getPosOrderExceptionDataBeanList() {
		return posOrderExceptionDataBeanList;
	}
	public void setPosOrderExceptionDataBeanList(
			List<PosOrderExceptionDataBean> posOrderExceptionDataBeanList) {
		this.posOrderExceptionDataBeanList = posOrderExceptionDataBeanList;
	}
	public List<OrderHistoryBean> getorderHistoryBeanList() {
		return orderHistoryBeanList;
	}
	public void setOrderHistoryBeanList(List<OrderHistoryBean> orderHistoryBeanList) {
		this.orderHistoryBeanList = orderHistoryBeanList;
	}
	public List<WasteQtyBean> getWasteQtyBeanList() {
		return wasteQtyBeanList;
	}
	public void setWasteQtyBeanList(List<WasteQtyBean> wasteQtyBeanList) {
		this.wasteQtyBeanList = wasteQtyBeanList;
	}
	

}
