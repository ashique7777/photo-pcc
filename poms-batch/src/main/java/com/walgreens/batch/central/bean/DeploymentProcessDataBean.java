package com.walgreens.batch.central.bean;

import java.io.Serializable;
/**
 * @author CTS
 */
public class DeploymentProcessDataBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * propertyValue
	 */
	private String propertyValue;
	
	/**
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}
	/**
	 * @param propertyValue the propertyValue to set
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
}
