package com.walgreens.batch.central.bean;

/**
 * @author CTS
 *
 */

public class OldStoreDetailBean {
	
	private long sysLocationId;
	private long locationNbr;
	private long districtNbr;
	private String addressStateCode;
	private long sysPriceLevelId;
	
	/**
	 * @return
	 */
	public long getSysLocationId() {
		return sysLocationId;
	}
	/**
	 * @param sysLocationId
	 */
	public void setSysLocationId(long sysLocationId) {
		this.sysLocationId = sysLocationId;
	}
	/**
	 * @return
	 */
	public long getLocationNbr() {
		return locationNbr;
	}
	/**
	 * @param locationNbr
	 */
	public void setLocationNbr(long locationNbr) {
		this.locationNbr = locationNbr;
	}
	/**
	 * @return
	 */
	public long getDistrictNbr() {
		return districtNbr;
	}
	/**
	 * @param districtNbr
	 */
	public void setDistrictNbr(long districtNbr) {
		this.districtNbr = districtNbr;
	}
	/**
	 * @return
	 */
	public String getAddressStateCode() {
		return addressStateCode;
	}
	/**
	 * @param addressStateCode
	 */
	public void setAddressStateCode(String addressStateCode) {
		this.addressStateCode = addressStateCode;
	}
	/**
	 * @return
	 */
	public long getSysPriceLevelId() {
		return sysPriceLevelId;
	}
	/**
	 * @param sysPriceLevelId
	 */
	public void setSysPriceLevelId(long sysPriceLevelId) {
		this.sysPriceLevelId = sysPriceLevelId;
	}
	
	

}
