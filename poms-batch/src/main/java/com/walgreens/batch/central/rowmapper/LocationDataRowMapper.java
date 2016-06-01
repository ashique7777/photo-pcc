/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.LocationDataBean;

/**
 * @author CTS
 *
 */
public class LocationDataRowMapper implements RowMapper<LocationDataBean> {

	@Override
	public LocationDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		LocationDataBean locationDataBean = new LocationDataBean();
		locationDataBean.setSysLocId(rs.getString("SYS_LOCATION_ID"));
		locationDataBean.setLocationNumber(rs.getString("LOCATION_NBR"));
		locationDataBean.setIsTwentyFourHourstr(rs.getLong("TWENTY_FOUR_HOUR_CD"));
		if (locationDataBean.getIsTwentyFourHourstr() != 1) {
			/*As no need to fetch data for 24 hours open store*/
			/*Store Open Time*/
			locationDataBean.setStoreOpenTimeSun(rs.getString("BUSINESS_TIME_OPEN_SUN"));
			locationDataBean.setStoreOpenTimeMon(rs.getString("BUSINESS_TIME_OPEN_MON"));
			locationDataBean.setStoreOpenTimeTue(rs.getString("BUSINESS_TIME_OPEN_TUE"));
			locationDataBean.setStoreOpenTimeWed(rs.getString("BUSINESS_TIME_OPEN_WED"));
			locationDataBean.setStoreOpenTimeThus(rs.getString("BUSINESS_TIME_OPEN_THU"));
			locationDataBean.setStoreOpenTimeFri(rs.getString("BUSINESS_TIME_OPEN_FRI"));
			locationDataBean.setStoreOpenTimeSat(rs.getString("BUSINESS_TIME_OPEN_SAT"));
			/*Store close time*/
			locationDataBean.setStoreCloseTimeSun(rs.getString("BUSINESS_TIME_CLOSED_SUN"));
			locationDataBean.setStoreCloseTimeMon(rs.getString("BUSINESS_TIME_CLOSED_MON"));
			locationDataBean.setStoreCloseTimeTue(rs.getString("BUSINESS_TIME_CLOSED_TUE"));
			locationDataBean.setStoreCloseTimeWed(rs.getString("BUSINESS_TIME_CLOSED_WED"));
			locationDataBean.setStoreCloseTimeThus(rs.getString("BUSINESS_TIME_CLOSED_THUS"));
			locationDataBean.setStoreCloseTimeFri(rs.getString("BUSINESS_TIME_CLOSED_FRI"));
			locationDataBean.setStoreCloseTimeSat(rs.getString("BUSINESS_TIME_CLOSED_SAT"));
		}
		
	
		return locationDataBean;
	}
}
