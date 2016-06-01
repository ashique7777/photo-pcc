package com.walgreens.omni.bean;

import java.sql.Timestamp;

public class CanisterChangeBean {
	
	private int sysLocationId;
	private Timestamp canisterStartDttm;
	private Timestamp canisterEndDttm;
	private int canisterCd;
	private String serviceDescription;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updateDttm;
	
	public int getSysLocationId() {
		return sysLocationId;
	}
	public void setSysLocationId(int sysLocationId) {
		this.sysLocationId = sysLocationId;
	}
	public Timestamp getCanisterStartDttm() {
		return canisterStartDttm;
	}
	public void setCanisterStartDttm(Timestamp canisterStartDttm) {
		this.canisterStartDttm = canisterStartDttm;
	}
	public Timestamp getCanisterEndDttm() {
		return canisterEndDttm;
	}
	public void setCanisterEndDttm(Timestamp canisterEndDttm) {
		this.canisterEndDttm = canisterEndDttm;
	}
	public int getCanisterCd() {
		return canisterCd;
	}
	public void setCanisterCd(int canisterCd) {
		this.canisterCd = canisterCd;
	}
	public String getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
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

}
