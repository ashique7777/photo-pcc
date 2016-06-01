package com.walgreens.omni.bean;

import java.util.List;

public class ProductDetailsBean {

	    private String orderDesc;
	    private String finalPrice;
	    private String promotionDescLineThree;
	    private String discountAmtLineThree;
	    private String promotionDescLineFour;
	    private String discountAmtLineFour;
	    private List<ProductDescBean> prodDescList;
	    
		/**
		 * @return the prodDescList
		 */
		public List<ProductDescBean> getProdDescList() {
			return prodDescList;
		}
		/**
		 * @param prodDescList the prodDescList to set
		 */
		public void setProdDescList(List<ProductDescBean> prodDescList) {
			this.prodDescList = prodDescList;
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
		public String getFinalPrice() {
			return finalPrice;
		}
		/**
		 * @param finalPrice the finalPrice to set
		 */
		public void setFinalPrice(String finalPrice) {
			this.finalPrice = finalPrice;
		}
		
		/**
		 * @return the promotionDescLineThree
		 */
		public String getPromotionDescLineThree() {
			return promotionDescLineThree;
		}
		/**
		 * @param promotionDescLineThree the promotionDescLineThree to set
		 */
		public void setPromotionDescLineThree(String promotionDescLineThree) {
			this.promotionDescLineThree = promotionDescLineThree;
		}
		/**
		 * @return the discountAmtLineThree
		 */
		public String getDiscountAmtLineThree() {
			return discountAmtLineThree;
		}
		/**
		 * @param discountAmtLineThree the discountAmtLineThree to set
		 */
		public void setDiscountAmtLineThree(String discountAmtLineThree) {
			this.discountAmtLineThree = discountAmtLineThree;
		}
		/**
		 * @return the promotionDescLineFour
		 */
		public String getPromotionDescLineFour() {
			return promotionDescLineFour;
		}
		/**
		 * @param promotionDescLineFour the promotionDescLineFour to set
		 */
		public void setPromotionDescLineFour(String promotionDescLineFour) {
			this.promotionDescLineFour = promotionDescLineFour;
		}
		/**
		 * @return the discountAmtLineFour
		 */
		public String getDiscountAmtLineFour() {
			return discountAmtLineFour;
		}
		/**
		 * @param discountAmtLineFour the discountAmtLineFour to set
		 */
		public void setDiscountAmtLineFour(String discountAmtLineFour) {
			this.discountAmtLineFour = discountAmtLineFour;
		}
	    
	    
}
