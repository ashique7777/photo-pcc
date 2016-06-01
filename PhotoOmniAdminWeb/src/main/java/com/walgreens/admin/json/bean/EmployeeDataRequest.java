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
package com.walgreens.admin.json.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.walgreens.admin.bean.EmployeeData;
import com.walgreens.common.utility.MessageHeader;

public class EmployeeDataRequest {

	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("employeeDetails")
	private List<EmployeeData> employeeDetails;

	/**
	 * @return the messageHeader object
	 */
	@JsonProperty("messageHeader")
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * method to set messageHeader object.
	 */
	@JsonProperty("messageHeader")
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	/**
	 * @return the List of Employee object.
	 */
	@JsonProperty("employeeDetails")
	public List<EmployeeData> getEmployeeDetails() {
		return employeeDetails;
	}

	/**
	 * method to Set Employee object.
	 */
	@JsonProperty("employeeDetails")
	public void setEmployeeDetails(List<EmployeeData> employee) {
		this.employeeDetails = employee;
	}
	
	@Override
	public String toString() {
		return "EmployeeDataRequest [messageHeader=" + messageHeader
				+ ", employeeDetails List=" + employeeDetails + "]";
	}
}
