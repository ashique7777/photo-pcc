package com.walgreens.oms.json.bean;

import java.util.List;

public class ExceptionRepEnvNbrLink {
	
	 private ExceptionPopupProductDetailsBean productDetails;
	 private List<EnvHistoryBean> envHistoryList;
	 private ExceptionPopupHeaderBean exceptionPopupHeaderBean;
	 
	/**
	 * @return the exceptionPopupHeaderBean
	 */
	public ExceptionPopupHeaderBean getExceptionPopupHeaderBean() {
		return exceptionPopupHeaderBean;
	}
	/**
	 * @param exceptionPopupHeaderBean the exceptionPopupHeaderBean to set
	 */
	public void setExceptionPopupHeaderBean(
			ExceptionPopupHeaderBean exceptionPopupHeaderBean) {
		this.exceptionPopupHeaderBean = exceptionPopupHeaderBean;
	}
	/**
	 * @return the envHistoryList
	 */
	public List<EnvHistoryBean> getEnvHistoryList() {
		return envHistoryList;
	}
	/**
	 * @param envHistoryList the envHistoryList to set
	 */
	public void setEnvHistoryList(List<EnvHistoryBean> envHistoryList) {
		this.envHistoryList = envHistoryList;
	}
	/**
	 * @return the productDetails
	 */
	public ExceptionPopupProductDetailsBean getProductDetails() {
		return productDetails;
	}
	/**
	 * @param productDetails the productDetails to set
	 */
	public void setProductDetails(ExceptionPopupProductDetailsBean productDetails) {
		this.productDetails = productDetails;
	}
	
	 

}
