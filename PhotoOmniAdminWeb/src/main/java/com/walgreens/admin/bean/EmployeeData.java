/********************************************************************************/
/* Copyright (c) 2015, Walgreens Co.											*/
/* All Rights Reserved.															*/
/*																				*/
/* THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT			*/
/* HOLDERS MAKE NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED,			*/
/* INCLUDING BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY OR				*/
/* FITNESS FOR ANY PARTICULAR PURPOSE OR THAT THE USE OF THE SOFTWARE			*/
/* OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY PATENTS,					*/
/* COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.										*/
/*																				*/
/* COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,				*/
/* SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE				*/
/* SOFTWARE OR DOCUMENTATION.													*/
/*																				*/
/* The name and trademarks of copyright holders may NOT be used in				*/
/* advertising or publicity pertaining to the software without					*/
/* specific, written prior permission. Title to copyright in this				*/
/* software and any associated documentation will at all times remain			*/
/* with copyright holders.														*/
/*																				*/
/* /*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Version             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		18 Feb 2015           CTS
 *  
/********************************************************************************/

package com.walgreens.admin.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeData {

	@JsonProperty("employeeId")
	private String employeeId;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("authenticatorId")
	private String authenticatorId; 
	@JsonProperty("photoPmEligibleInd")
	private String photoPmEligibleInd;
	@JsonProperty("storeEmployeeInd")
	private String storeEmployeeInd;

	/**
	 * Get and Set method for employeeId property.
	 */
	@JsonProperty("employeeId")
	public String getEmployeeId() {
		return employeeId;
	}
	@JsonProperty("employeeId")
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * Get and Set method for firstName property.
	 */
	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}
	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get and Set method for lastName property.
	 */
	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}
	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get and Set method for authenticatorId property.
	 */
	@JsonProperty("authenticatorId")
	public String getAuthenticatorId() {
		return authenticatorId;
	}
	@JsonProperty("authenticatorId")
	public void setAuthenticatorId(String authenticatorId) {
		this.authenticatorId = authenticatorId;
	}

	/**
	 * Get and Set method for photoPmEligibleInd property.
	 */
	@JsonProperty("photoPmEligibleInd")
	public String getPhotoPmEligibleInd() {
		return photoPmEligibleInd;
	}
	@JsonProperty("photoPmEligibleInd")
	public void setPhotoPmEligibleInd(String photoPmEligibleInd) {
		this.photoPmEligibleInd = photoPmEligibleInd;
	}

	/**
	 * Get and Set method for storeEmployeeInd property.
	 */
	@JsonProperty("storeEmployeeInd")
	public String getStoreEmployeeInd() {
		return storeEmployeeInd;
	}
	@JsonProperty("storeEmployeeInd")
	public void setStoreEmployeeInd(String storeEmployeeInd) {
		this.storeEmployeeInd = storeEmployeeInd;
	}
	
	@Override
	public String toString() {
		return "EmployeeData [employeeId=" + employeeId
				+ ",firstName=" + firstName
				+ ", lastName=" + lastName
				+ ", authenticatorId=" + authenticatorId
				+ ", photoPmEligibleInd=" + photoPmEligibleInd
				+ ", storeEmployeeInd=" + storeEmployeeInd
				+ "]";
	}
	
}