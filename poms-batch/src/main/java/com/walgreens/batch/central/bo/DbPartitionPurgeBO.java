/**
 * DbPartitionPurgeBO.java 
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
package com.walgreens.batch.central.bo;

import java.util.Properties;

/**
 * This interface is used to process DbPartitionPurge related information to DbPartitionPurgeBOImpl class.
 */
public interface DbPartitionPurgeBO {
	
	public String[] dbPartitionPurgeTableName(Properties prop) throws Exception;

}
