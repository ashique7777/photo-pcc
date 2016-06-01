package com.walgreens.oms.json.bean;

import java.util.List;

public class ExceptionPopupProductDetailsBean {

	    private String orderDesc;
	    private double finalPrice;
	    private List<ExceptionRepProductDtlsDataBean> prodDescList;
	    private List<ExceptionRepOrderLinePromotionDtls> orderLinePromotionList;
	    private List<ExceptionRepOrderPromotionDtls> orderPromotionList;
	    private List<ExceptionRepProductDtlsDataBean> orderLineDtlsFinalList;
	    
		
		
		
		/**
		 * @return the orderLineDtlsFinalList
		 */
		public List<ExceptionRepProductDtlsDataBean> getOrderLineDtlsFinalList() {
			return orderLineDtlsFinalList;
		}
		/**
		 * @param orderLineDtlsFinalList the orderLineDtlsFinalList to set
		 */
		public void setOrderLineDtlsFinalList(
				List<ExceptionRepProductDtlsDataBean> orderLineDtlsFinalList) {
			this.orderLineDtlsFinalList = orderLineDtlsFinalList;
		}
		/**
		 * @return the prodDescList
		 */
		public List<ExceptionRepProductDtlsDataBean> getProdDescList() {
			return prodDescList;
		}
		/**
		 * @param prodDescList the prodDescList to set
		 */
		public void setProdDescList(List<ExceptionRepProductDtlsDataBean> prodDescList) {
			this.prodDescList = prodDescList;
		}
		/**
		 * @return the orderLinePromotionList
		 */
		public List<ExceptionRepOrderLinePromotionDtls> getOrderLinePromotionList() {
			return orderLinePromotionList;
		}
		/**
		 * @param orderLinePromotionList the orderLinePromotionList to set
		 */
		public void setOrderLinePromotionList(
				List<ExceptionRepOrderLinePromotionDtls> orderLinePromotionList) {
			this.orderLinePromotionList = orderLinePromotionList;
		}
		/**
		 * @return the orderPromotionList
		 */
		public List<ExceptionRepOrderPromotionDtls> getOrderPromotionList() {
			return orderPromotionList;
		}
		/**
		 * @param orderPromotionList the orderPromotionList to set
		 */
		public void setOrderPromotionList(
				List<ExceptionRepOrderPromotionDtls> orderPromotionList) {
			this.orderPromotionList = orderPromotionList;
		}
		/**
		 * @return the orderDesc
		 */
		public String getOrderDesc() {
			return orderDesc;
		}
		/**
		 * @param orderDesc the orderDesc to set
		 */
		public void setOrderDesc(String orderDesc) {
			this.orderDesc = orderDesc;
		}
		/**
		 * @return the finalPrice
		 */
		public double getFinalPrice() {
			return finalPrice;
		}
		/**
		 * @param finalPrice the finalPrice to set
		 */
		public void setFinalPrice(double finalPrice) {
			this.finalPrice = finalPrice;
		}
		
		
	    
	    
}
