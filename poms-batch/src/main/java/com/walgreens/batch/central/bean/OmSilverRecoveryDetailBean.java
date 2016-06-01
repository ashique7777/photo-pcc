package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

/**
 * @author: CTS
 * @date: 08-04-2015
 * @table: OM_SILVER_RECOVERY_DETAIL
 */


public class OmSilverRecoveryDetailBean {
	
	private int sysSilverRecoveryDetailId;
	private int sysLocationId;
	private Timestamp silverCalcDttm;
	private int rollsCount;
	private int printsCount;
	private int printsInSoInch;
	private int silverRecvRolls;
	private int silverRecvPrints;
	private int transferCd;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updatedDttm;
	private String canisterStatus;
	
	/**
	 * @return the sysSilverRecoveryDetailId
	 */
	public int getSysSilverRecoveryDetailId() {
		return sysSilverRecoveryDetailId;
	}
	/**
	 * @param sysSilverRecoveryDetailId the sysSilverRecoveryDetailId to set
	 */
	public void setSysSilverRecoveryDetailId(int sysSilverRecoveryDetailId) {
		this.sysSilverRecoveryDetailId = sysSilverRecoveryDetailId;
	}
	/**
	 * @return the sysLocationId
	 */
	public int getSysLocationId() {
		return sysLocationId;
	}
	/**
	 * @param sysLocationId the sysLocationId to set
	 */
	public void setSysLocationId(int sysLocationId) {
		this.sysLocationId = sysLocationId;
	}
	/**
	 * @return the silverCalcDttm
	 */
	public Timestamp getSilverCalcDttm() {
		return silverCalcDttm;
	}
	/**
	 * @param silverCalcDttm the silverCalcDttm to set
	 */
	public void setSilverCalcDttm(Timestamp silverCalcDttm) {
		this.silverCalcDttm = silverCalcDttm;
	}
	/**
	 * @return the rollsCount
	 */
	public int getRollsCount() {
		return rollsCount;
	}
	/**
	 * @param rollsCount the rollsCount to set
	 */
	public void setRollsCount(int rollsCount) {
		this.rollsCount = rollsCount;
	}
	/**
	 * @return the printsCount
	 */
	public int getPrintsCount() {
		return printsCount;
	}
	/**
	 * @param printsCount the printsCount to set
	 */
	public void setPrintsCount(int printsCount) {
		this.printsCount = printsCount;
	}
	/**
	 * @return the printsInSoInch
	 */
	public int getPrintsInSoInch() {
		return printsInSoInch;
	}
	/**
	 * @param printsInSoInch the printsInSoInch to set
	 */
	public void setPrintsInSoInch(int printsInSoInch) {
		this.printsInSoInch = printsInSoInch;
	}
	/**
	 * @return the silverRecvRolls
	 */
	public int getSilverRecvRolls() {
		return silverRecvRolls;
	}
	/**
	 * @param silverRecvRolls the silverRecvRolls to set
	 */
	public void setSilverRecvRolls(int silverRecvRolls) {
		this.silverRecvRolls = silverRecvRolls;
	}
	/**
	 * @return the silverRecvPrints
	 */
	public int getSilverRecvPrints() {
		return silverRecvPrints;
	}
	/**
	 * @param silverRecvPrints the silverRecvPrints to set
	 */
	public void setSilverRecvPrints(int silverRecvPrints) {
		this.silverRecvPrints = silverRecvPrints;
	}
	/**
	 * @return the transferCd
	 */
	public int getTransferCd() {
		return transferCd;
	}
	/**
	 * @param transferCd the transferCd to set
	 */
	public void setTransferCd(int transferCd) {
		this.transferCd = transferCd;
	}
	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * @return the createDttm
	 */
	public Timestamp getCreateDttm() {
		return createDttm;
	}
	/**
	 * @param createDttm the createDttm to set
	 */
	public void setCreateDttm(Timestamp createDttm) {
		this.createDttm = createDttm;
	}
	/**
	 * @return the updateUserId
	 */
	public String getUpdateUserId() {
		return updateUserId;
	}
	/**
	 * @param updateUserId the updateUserId to set
	 */
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	/**
	 * @return the updatedDttm
	 */
	public Timestamp getUpdatedDttm() {
		return updatedDttm;
	}
	/**
	 * @param updatedDttm the updatedDttm to set
	 */
	public void setUpdatedDttm(Timestamp updatedDttm) {
		this.updatedDttm = updatedDttm;
	}
	public String getCanisterStatus() {
		return canisterStatus;
	}
	public void setCanisterStatus(String canisterStatus) {
		this.canisterStatus = canisterStatus;
	}
	
	
}
