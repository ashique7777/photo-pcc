package com.walgreens.oms.bean;

import java.sql.Timestamp;

public class OmPosTranByWicUpcBean {
	
	private String businessDate;
	private long sysPosTransByProductId;
	private int locationNbr;
	private int envelopNbr;
	private int orderNbr;
	private String wic;
	private String upc;
	private Long sysStorePosId;
	private int salesUnits;
	private double salesDollers;
	private double costDollers;
	private String transactionTypeCd;
	private Timestamp mssTransmissionDate;
	private String mssTransfarCd;
	private int defaultCostCd;
	private String createUserId;
	private Timestamp createdDttm;
	private String updateUserId;
	private Timestamp updatedDttm;
	/** Changes for JIRA#624 starts here **/
	private String productNbr;
	/** Changes for JIRA#624 ends here **/
	
	public String getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}
	public long getSysPosTransByProductId() {
		return sysPosTransByProductId;
	}
	public void setSysPosTransByProductId(long sysPosTransByProductId) {
		this.sysPosTransByProductId = sysPosTransByProductId;
	}
	public int getLocationNbr() {
		return locationNbr;
	}
	public void setLocationNbr(int locationNbr) {
		this.locationNbr = locationNbr;
	}
	public int getEnvelopNbr() {
		return envelopNbr;
	}
	public void setEnvelopNbr(int envelopNbr) {
		this.envelopNbr = envelopNbr;
	}
	public int getOrderNbr() {
		return orderNbr;
	}
	public void setOrderNbr(int orderNbr) {
		this.orderNbr = orderNbr;
	}
	public String getWic() {
		return wic;
	}
	public void setWic(String wic) {
		this.wic = wic;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public Long getSysStorePosId() {
		return sysStorePosId;
	}
	public void setSysStorePosId(Long sysStorePosId) {
		this.sysStorePosId = sysStorePosId;
	}
	public int getSalesUnits() {
		return salesUnits;
	}
	public void setSalesUnits(int salesUnits) {
		this.salesUnits = salesUnits;
	}
	public double getSalesDollers() {
		return salesDollers;
	}
	public void setSalesDollers(double salesDollers) {
		this.salesDollers = salesDollers;
	}
	public double getCostDollers() {
		return costDollers;
	}
	public void setCostDollers(double costDollers) {
		this.costDollers = costDollers;
	}
	public String getTransactionTypeCd() {
		return transactionTypeCd;
	}
	public void setTransactionTypeCd(String transactionTypeCd) {
		this.transactionTypeCd = transactionTypeCd;
	}
	public Timestamp getMssTransmissionDate() {
		return mssTransmissionDate;
	}
	public void setMssTransmissionDate(Timestamp mssTransmissionDate) {
		this.mssTransmissionDate = mssTransmissionDate;
	}
	public String getMssTransfarCd() {
		return mssTransfarCd;
	}
	public void setMssTransfarCd(String mssTransfarCd) {
		this.mssTransfarCd = mssTransfarCd;
	}
	public int getDefaultCostCd() {
		return defaultCostCd;
	}
	public void setDefaultCostCd(int defaultCostCd) {
		this.defaultCostCd = defaultCostCd;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public Timestamp getCreatedDttm() {
		return createdDttm;
	}
	public void setCreatedDttm(Timestamp createdDttm) {
		this.createdDttm = createdDttm;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Timestamp getUpdatedDttm() {
		return updatedDttm;
	}
	public void setUpdatedDttm(Timestamp updatedDttm) {
		this.updatedDttm = updatedDttm;
	}
	
	/** Changes for JIRA#624 starts here **/
	public String getProductNbr() {
		return productNbr;
	}
	public void setProductNbr(String productNbr) {
		this.productNbr = productNbr;
	}
	/** Changes for JIRA#624 ends here **/

}
