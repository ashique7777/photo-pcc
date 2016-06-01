package com.walgreens.omni.bean;

import java.util.List;

public class ExceptionRepEnvNbrLink {
	
	 private ProductDetailsBean productDetails;
	 private List<EnvHistoryBean> envHistoryList;
	 
	 
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
	public ProductDetailsBean getProductDetails() {
		return productDetails;
	}
	/**
	 * @param productDetails the productDetails to set
	 */
	public void setProductDetails(ProductDetailsBean productDetails) {
		this.productDetails = productDetails;
	}
	
	 

}
