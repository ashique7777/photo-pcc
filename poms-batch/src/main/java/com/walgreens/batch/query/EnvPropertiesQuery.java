/**
 * 
 */
package com.walgreens.batch.query;


/**
 * This method will generate the query which is used for pagination by using different properties.
 * @author Cognizant
 *
 */
public class EnvPropertiesQuery {
	
	public static StringBuilder getEnvProperties(String envMode) {		
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ENV_PROPERTIES_ID,PROPERTY_NAME,PROPERTY_TYPE,PROPERTY_VALUE FROM OM_ENV_PROPERTIES WHERE ENV_NAME = ? AND PROPERTY_NAME = ? AND PROPERTY_TYPE = ?");
		return query; 
	}
	
	public static StringBuilder getDBEnvProperties() {		
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ENV_PROPERTIES_ID,PROPERTY_NAME,PROPERTY_TYPE,PROPERTY_VALUE FROM OM_ENV_PROPERTIES WHERE ENV_NAME = ?");
		return query; 
	}


}
