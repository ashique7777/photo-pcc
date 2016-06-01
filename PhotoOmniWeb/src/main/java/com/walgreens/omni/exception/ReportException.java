/**
 * ReportException.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
* Modification Log
*-----------------------------------------------------------------------------------------------
*   Ver             Date            Modified By         Description
*-----------------------------------------------------------------------------------------------
*  <1.1>     		06 Feb 2015
*  
**/
package com.walgreens.omni.exception;

/**
 * This class is used custome Exception
 * @author CTS
 * @version 1.1 February 06, 2015
 */
public class ReportException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4456484442513929196L;
	private String message = null;

	public ReportException() {
		super();
	}

	public ReportException(String message) {
		super(message);
		this.message = message;
	}

	public ReportException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
