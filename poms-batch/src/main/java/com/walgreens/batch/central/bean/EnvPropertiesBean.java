/**
 * 
 */
package com.walgreens.batch.central.bean;

/**
 * @author CTS
 *
 */
public class EnvPropertiesBean {
	
	Integer envPropId;
	String envName;
	String propName;
	String propType;
	String propValue;
	/**
	 * @return the envPropId
	 */
	public Integer getEnvPropId() {
		return envPropId;
	}
	/**
	 * @param envPropId the envPropId to set
	 */
	public void setEnvPropId(Integer envPropId) {
		this.envPropId = envPropId;
	}
	/**
	 * @return the envName
	 */
	public String getEnvName() {
		return envName;
	}
	/**
	 * @param envName the envName to set
	 */
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	/**
	 * @return the propName
	 */
	public String getPropName() {
		return propName;
	}
	/**
	 * @param propName the propName to set
	 */
	public void setPropName(String propName) {
		this.propName = propName;
	}
	/**
	 * @return the propType
	 */
	public String getPropType() {
		return propType;
	}
	/**
	 * @param propType the propType to set
	 */
	public void setPropType(String propType) {
		this.propType = propType;
	}
	/**
	 * @return the propValue
	 */
	public String getPropValue() {
		return propValue;
	}
	/**
	 * @param propValue the propValue to set
	 */
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	
	

}
