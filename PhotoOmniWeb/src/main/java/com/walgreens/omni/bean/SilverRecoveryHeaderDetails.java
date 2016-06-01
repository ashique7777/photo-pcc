package com.walgreens.omni.bean;

import java.sql.Timestamp;

public class SilverRecoveryHeaderDetails {
	
	private Timestamp CanisterStartDttm;
	private Timestamp CanisterEndDttm;
	private String CanisterStatus;
	private String CanisterPrevStatus;
	private int SysLocationId;
	private double silverRecvRolls;
	private double silverRecvPrints;
	private String silverCompany;
	private int printInSqInch;
	private int rollsCount;
	private int printsCount;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updateDttm;
	
	public Timestamp getCanisterStartDttm() {
		return CanisterStartDttm;
	}
	public void setCanisterStartDttm(Timestamp canisterStartDttm) {
		CanisterStartDttm = canisterStartDttm;
	}
	public Timestamp getCanisterEndDttm() {
		return CanisterEndDttm;
	}
	public void setCanisterEndDttm(Timestamp canisterEndDttm) {
		CanisterEndDttm = canisterEndDttm;
	}
	public String getCanisterStatus() {
		return CanisterStatus;
	}
	public void setCanisterStatus(String canisterStatus) {
		CanisterStatus = canisterStatus;
	}
	public int getSysLocationId() {
		return SysLocationId;
	}
	public void setSysLocationId(int sysLocationId) {
		SysLocationId = sysLocationId;
	}
	public double getSilverRecvRolls() {
		return silverRecvRolls;
	}
	public void setSilverRecvRolls(double silverRecvRolls) {
		this.silverRecvRolls = silverRecvRolls;
	}
	public double getSilverRecvPrints() {
		return silverRecvPrints;
	}
	public void setSilverRecvPrints(double silverRecvPrints) {
		this.silverRecvPrints = silverRecvPrints;
	}
	public String getCanisterPrevStatus() {
		return CanisterPrevStatus;
	}
	public void setCanisterPrevStatus(String canisterPrevStatus) {
		CanisterPrevStatus = canisterPrevStatus;
	}
	public String getSilverCompany() {
		return silverCompany;
	}
	public void setSilverCompany(String silverCompany) {
		this.silverCompany = silverCompany;
	}
	public int getPrintInSqInch() {
		return printInSqInch;
	}
	public void setPrintInSqInch(int printInSqInch) {
		this.printInSqInch = printInSqInch;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public Timestamp getCreateDttm() {
		return createDttm;
	}
	public void setCreateDttm(Timestamp createDttm) {
		this.createDttm = createDttm;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Timestamp getUpdateDttm() {
		return updateDttm;
	}
	public void setUpdateDttm(Timestamp updateDttm) {
		this.updateDttm = updateDttm;
	}
	public int getRollsCount() {
		return rollsCount;
	}
	public void setRollsCount(int rollsCount) {
		this.rollsCount = rollsCount;
	}
	public int getPrintsCount() {
		return printsCount;
	}
	public void setPrintsCount(int printsCount) {
		this.printsCount = printsCount;
	}
	
	

}
