package com.walgreens.oms.bean;

public class OmProductDetailBean {
	
	private long sysProductId;
	private String wic;
	private String upc;
	/** Changes for JIRA#624 starts here **/
	private String productNbr;
	/** Changes for JIRA#624 ends here **/
	
	public long getSysProductId() {
		return sysProductId;
	}
	public void setSysProductId(long sysProductId) {
		this.sysProductId = sysProductId;
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
	
	/** Changes for JIRA#624 starts here **/
	public String getProductNbr() {
		return productNbr;
	}
	public void setProductNbr(String productNbr) {
		this.productNbr = productNbr;
	}
	/** Changes for JIRA#624 ends here **/

}
