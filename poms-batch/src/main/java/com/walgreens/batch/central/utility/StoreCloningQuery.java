package com.walgreens.batch.central.utility;

import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * @author CTS
 * 
 */

public class StoreCloningQuery {
	
	/**
	 * Method to get select Query from OM_LOCATION table.
	 * @return StringBuffer SQL Select Query.
	 * @since v1.0
	 */
	public String getNewLocationDetail(){
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_LOCATION_ID, LOCATION_NBR, DISTRICT_NBR, ADDRESS_STATE_CODE, SYS_PRICE_LEVEL_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_LOCATION);
		query.append(" WHERE SYS_PRICE_LEVEL_ID = 0");
		return query.toString();
	}
	
	/**
	 * Method to get select Query from OM_LOCATION table.
	 * @return StringBuffer SQL Select Query.
	 * @since v1.0
	 */
	public String getOldLocationDetail(){
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_LOCATION_ID, LOCATION_NBR, DISTRICT_NBR, ADDRESS_STATE_CODE, SYS_PRICE_LEVEL_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_LOCATION);
		query.append(" WHERE DISTRICT_NBR = ? AND SYS_PRICE_LEVEL_ID <> 0 AND ROWNUM<=1");
		return query.toString();
	}
	
	/**
	 * Method to update Query to OM_LOCATION table.
	 * @return StringBuffer SQL Select Query.
	 * @since v1.0
	 */
	public String updateNewLocationDetail(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_LOCATION);
		query.append(" SET SYS_PRICE_LEVEL_ID = ? , UPDATE_DTTM = SYSDATE WHERE SYS_LOCATION_ID = ?");
		return query.toString();
	}

}
