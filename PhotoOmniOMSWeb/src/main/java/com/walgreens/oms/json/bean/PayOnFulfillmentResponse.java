package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.List;






import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.PayOnFulfillmentCSVRespData;

public class PayOnFulfillmentResponse {
	
	private String totalRecord;
	private String currrentPage;
	private String filtertypePay;
	private boolean storePrint;
	private String storeWithAddress;
	
	
	PayOnFulfillmentRespData respData = new PayOnFulfillmentRespData();
	
	private List<PayOnFulfillmentRespData> repByPayOnFulfillmentList = new ArrayList<PayOnFulfillmentRespData>();
	private List<PayOnFulfillmentCSVRespData> csvResponseList = new ArrayList<PayOnFulfillmentCSVRespData>();
	/**
	 * @return the csvResponseList
	 */
	public List<PayOnFulfillmentCSVRespData> getCsvResponseList() {
		return csvResponseList;
	}
	/**
	 * @return the storeWithAddress
	 */
	public String getStoreWithAddress() {
		return storeWithAddress;
	}
	/**
	 * @param storeWithAddress the storeWithAddress to set
	 */
	public void setStoreWithAddress(String storeWithAddress) {
		this.storeWithAddress = storeWithAddress;
	}
	
	/**
	 * @param csvResponseList the csvResponseList to set
	 */
	public void setCsvResponseList(List<PayOnFulfillmentCSVRespData> csvResponseList) {
		this.csvResponseList = csvResponseList;
	}
	
	

	/**
	 * @return the storePrint
	 */
	public boolean isStorePrint() {
		return storePrint;
	}
	/**
	 * @param storePrint the storePrint to set
	 */
	public void setStorePrint(boolean storePrint) {
		this.storePrint = storePrint;
	}



	private List<ProductDetails> productList = new ArrayList<ProductDetails>();
	private MessageHeader messageHeader;
	
		
	/**
	 * @return the totalRecord
	 */
	public String getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * @return the currrentPage
	 */
	public String getCurrrentPage() {
		return currrentPage;
	}
	/**
	 * @param currrentPage the currrentPage to set
	 */
	public void setCurrrentPage(String currrentPage) {
		this.currrentPage = currrentPage;
	}
	/**
	 * @return the filtertypePayl
	 */
	public String getFiltertypePay() {
		return filtertypePay;
	}
	/**
	 * @param filtertypePayl the filtertypePayl to set
	 */
	public void setFiltertypePay(String filtertypePay) {
		this.filtertypePay = filtertypePay;
	}
	/**
	 * @return the respData
	 */
	public PayOnFulfillmentRespData getRespData() {
		return respData;
	}
	/**
	 * @param respData the respData to set
	 */
	public void setRespData(PayOnFulfillmentRespData respData) {
		this.respData = respData;
	}
	/**
	 * @return the repByPayOnFulfillmentList
	 */
	public List<PayOnFulfillmentRespData> getRepByPayOnFulfillmentList() {
		return repByPayOnFulfillmentList;
	}
	/**
	 * @param repByPayOnFulfillmentList the repByPayOnFulfillmentList to set
	 */
	public void setRepByPayOnFulfillmentList(
			List<PayOnFulfillmentRespData> repByPayOnFulfillmentList) {
		this.repByPayOnFulfillmentList = repByPayOnFulfillmentList;
	}
	/**
	 * @return the messageHeader
	 */
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	/**
	 * @return the productList
	 */
	public List<ProductDetails> getProductList() {
		return productList;
	}
	/**
	 * @param productList the productList to set
	 */
	public void setProductList(List<ProductDetails> productList) {
		this.productList = productList;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	

}
