package com.walgreens.batch.central.bean;

public class 	CSVFileRoyaltyReportDataBean{


	private long storeNumber;
	private long templateId;
	private String productName;
	private String product;
	private String vendor;
	private long numberOfOrders;
	private long numberOfPrints;
	private double soldAmount;
	private String groupBy;
	private String outPutSize; 
	private String templateName;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getOutPutSize() {
		return outPutSize;
	}

	public void setOutPutSize(String outPutSize) {
		this.outPutSize = outPutSize;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public long getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public long getNumberOfOrders() {
		return numberOfOrders;
	}

	public void setNumberOfOrders(long numberOfOrders) {
		this.numberOfOrders = numberOfOrders;
	}

	public long getNumberOfPrints() {
		return numberOfPrints;
	}

	public void setNumberOfPrints(long numberOfPrints) {
		this.numberOfPrints = numberOfPrints;
	}

	public double getSoldAmount() {
		return soldAmount;
	}

	public void setSoldAmount(double soldAmount) {
		this.soldAmount = soldAmount;
	}
}
