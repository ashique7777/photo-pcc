package com.walgreens.oms.bean;

import java.io.Serializable;

public class EvePropBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private int sysInvPropertiesId;
	private String envName;
	private String propertyName;
	private String propertyValue;
	private String propertyType;
	
	public int getSysInvPropertiesId() {
		return sysInvPropertiesId;
	}
	public void setSysInvPropertiesId(int sysInvPropertiesId) {
		this.sysInvPropertiesId = sysInvPropertiesId;
	}
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

}
