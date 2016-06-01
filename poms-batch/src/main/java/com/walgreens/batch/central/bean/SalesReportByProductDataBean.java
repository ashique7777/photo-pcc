package com.walgreens.batch.central.bean;

/**
 * 
 * @author CTS
 * 
 */
public class SalesReportByProductDataBean {

	private long templateId;
	private String category;
	private String description;
	private String outputSize;
	private String vendor;
	private int count;
	private int quantity;
	private double amountPaid;
	private double calculatedRetail;
	private double originalRetail;
	private double orderCost;
	private double totalDiscountAmount;

	/**
	 * @return the templateId
	 */
	public long getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId
	 *            the templateId to set
	 */
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the outputSize
	 */
	public String getOutputSize() {
		return outputSize;
	}

	/**
	 * @param outputSize
	 *            the outputSize to set
	 */
	public void setOutputSize(String outputSize) {
		this.outputSize = outputSize;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the amountPaid
	 */
	public double getAmountPaid() {
		return amountPaid;
	}

	/**
	 * @param amountPaid
	 *            the amountPaid to set
	 */
	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	/**
	 * @return the calculatedRetail
	 */
	public double getCalculatedRetail() {
		return calculatedRetail;
	}

	/**
	 * @param calculatedRetail
	 *            the calculatedRetail to set
	 */
	public void setCalculatedRetail(double calculatedRetail) {
		this.calculatedRetail = calculatedRetail;
	}

	/**
	 * @return the originalRetail
	 */
	public double getOriginalRetail() {
		return originalRetail;
	}

	/**
	 * @param originalRetail
	 *            the originalRetail to set
	 */
	public void setOriginalRetail(double originalRetail) {
		this.originalRetail = originalRetail;
	}

	/**
	 * @return the orderCost
	 */
	public double getOrderCost() {
		return orderCost;
	}

	/**
	 * @param orderCost
	 *            the orderCost to set
	 */
	public void setOrderCost(double orderCost) {
		this.orderCost = orderCost;
	}

	/**
	 * @return the totalDiscountAmount
	 */
	public double getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	/**
	 * @param totalDiscountAmount
	 *            the totalDiscountAmount to set
	 */
	public void setTotalDiscountAmount(double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

}
