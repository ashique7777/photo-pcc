/**
 * 
 */
package com.walgreens.admin.json.bean;

/**
 * @author CTS
 *
 */
import java.util.ArrayList;
import java.util.List;

public class Store {

	private String regionNumber;
	private String districtNumber;
	private String storeNumber;
	private String storeNbrAddress;
	private String totalRecord;
	private String currrentPage;
	private List<ResponseMachineData> data = new ArrayList<ResponseMachineData>();

	/**
	 * @return the regionNumber
	 */
	public String getRegionNumber() {
		return regionNumber;
	}

	/**
	 * @param regionNumber
	 *            the regionNumber to set
	 */
	public void setRegionNumber(String regionNumber) {
		this.regionNumber = regionNumber;
	}

	/**
	 * @return the districtNumber
	 */
	public String getDistrictNumber() {
		return districtNumber;
	}

	/**
	 * @param districtNumber
	 *            the districtNumber to set
	 */
	public void setDistrictNumber(String districtNumber) {
		this.districtNumber = districtNumber;
	}

	/**
	 * @return the storeNumber
	 */
	public String getStoreNumber() {
		return storeNumber;
	}

	/**
	 * @param storeNumber
	 *            the storeNumber to set
	 */
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	/**
	 * @return the totalRecord
	 */
	public String getTotalRecord() {
		return totalRecord;
	}

	/**
	 * @param totalRecord
	 *            the totalRecord to set
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
	 * @param currrentPage
	 *            the currrentPage to set
	 */
	public void setCurrrentPage(String currrentPage) {
		this.currrentPage = currrentPage;
	}

	/**
	 * @return the data
	 */
	public List<ResponseMachineData> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<ResponseMachineData> data) {
		this.data = data;
	}

	/**
	 * 
	 * @return
	 */
	public String getStoreNbrAddress() {
		return storeNbrAddress;
	}

	/**
	 * 
	 * @param storeNbrAddress
	 */
	public void setStoreNbrAddress(String storeNbrAddress) {
		this.storeNbrAddress = storeNbrAddress;
	}

}
