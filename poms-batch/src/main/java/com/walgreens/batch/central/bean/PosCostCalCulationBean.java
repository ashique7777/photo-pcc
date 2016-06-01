package com.walgreens.batch.central.bean;

import java.util.List;

public class PosCostCalCulationBean {
	
	
	private long sysOrderId;	
	private double soldAmtOmOrder;
	private List<OrderLineBean> orderLineBeanList;
	private List<LicenceContentPosBean> licenceContentBeanList;
	private List<OrderLineTemplateBean> orderLineTemplateBeanList;

	
	public long getSysOrderId() {
		return sysOrderId;
	}
	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	public double getSoldAmtOmOrder() {
		return soldAmtOmOrder;
	}
	public void setSoldAmtOmOrder(double soldAmtOmOrder) {
		this.soldAmtOmOrder = soldAmtOmOrder;
	}
	public List<OrderLineBean> getOrderLineBeanList() {
		return orderLineBeanList;
	}
	public void setOrderLineBeanList(List<OrderLineBean> orderLineBeanList) {
		this.orderLineBeanList = orderLineBeanList;
	}
	public List<LicenceContentPosBean> getLicenceContentBeanList() {
		return licenceContentBeanList;
	}
	public void setLicenceContentBeanList(
			List<LicenceContentPosBean> licenceContentBeanList) {
		this.licenceContentBeanList = licenceContentBeanList;
	}
	public List<OrderLineTemplateBean> getOrderLineTemplateBeanList() {
		return orderLineTemplateBeanList;
	}
	public void setOrderLineTemplateBeanList(
			List<OrderLineTemplateBean> orderLineTemplateBeanList) {
		this.orderLineTemplateBeanList = orderLineTemplateBeanList;
	}
	
}
