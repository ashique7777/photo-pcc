/**
 * DbPartitionPurgeBOFactory.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 18 December 2015
 *  
 **/
package com.walgreens.batch.central.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bo.DbPartitionPurgeBO;

/**
 * 
 * @author CTS
 * @version 1.1 December 18, 2015.
 * 
 */
@Component
@Scope("singleton")
public class DbPartitionPurgeBOFactory {
	
	@Autowired
	@Qualifier("DbPartitionPurgeBO")
	private DbPartitionPurgeBO dbPartitionPurgeBO;

	/**
	 * @return the dbPartitionPurgeBO
	 */
	public DbPartitionPurgeBO getDbPartitionPurgeBO() {
		return dbPartitionPurgeBO;
	}

	/**
	 * @param dbPartitionPurgeBO the dbPartitionPurgeBO to set
	 */
	public void setDbPartitionPurgeBO(DbPartitionPurgeBO dbPartitionPurgeBO) {
		this.dbPartitionPurgeBO = dbPartitionPurgeBO;
	}
	
	

}
