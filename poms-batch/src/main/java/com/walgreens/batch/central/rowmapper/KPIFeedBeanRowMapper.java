package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.KPIFeedBean;
import com.walgreens.common.constant.PhotoOmniConstants;

/*
 * Store Numbers : Can have but does not require leading zeros.
 * STAT Id 		 : Up to 8 characters.
 * Sample Value  : Numeric STAT value, up to 13 digits with 2 decimal places. 
 * Sample Size   : Used for weighted average STATS.  Numeric, up to 13 digits plus 2 decimal places.
 * Date/Time     : Date/Time field in format: YYYYMMDDHHMMSS. 
 * For # TIME FRAME=HR, values of HHMMSS have to be valid, but for other time frames, they can be zeros.
 */
public class KPIFeedBeanRowMapper implements RowMapper<KPIFeedBean> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIFeedBeanRowMapper.class);

	Format formatter = new SimpleDateFormat("yyyyMMdd" + "000000");

	@Override
	public KPIFeedBean mapRow(ResultSet rs, int rowNum) throws SQLException {

		KPIFeedBean kpiFeedBean = new KPIFeedBean();
		try {
			kpiFeedBean.setSotreNumber(padZeroPrefix(
					rs.getString("LOCATION_NBR"), 5));
			kpiFeedBean.setStatId(addBlankSpace(rs.getString("KPI_STAT_ID")));
			kpiFeedBean.setSampleValue(formatKpiValues(
					String.valueOf(rs.getObject("KPI_SAMPLE_VALUE")), 12));
			kpiFeedBean.setSampleSize(formatKpiValues(
					String.valueOf(rs.getObject("KPI_SAMPLE_SIZE")), 12));
			kpiFeedBean.setDateTime(formatter.format(rs.getDate("KPI_DATE")));
			kpiFeedBean.setReferenceData("          |");
		} catch (Exception e) {
			LOGGER.error("Error occured while setting value in the KPIFeedBeanRowMapper ---> "
					+ e);
		}
		return kpiFeedBean;
	}

	private String formatKpiValues(String storeNo, int length) {
		String[] split = storeNo.split(("\\."));
		String first = split[0];
		StringBuilder sb = new StringBuilder();
		if (first.length() < length) {
			for (int i = first.length(); i <= length; i++) {
				sb.append("0");
			}
			sb.append(first);
		}
		first = sb.toString();
		String second = "";
		if (split.length > 1) {
			second = split[1];
		} else {
			second = "00";
		}

		sb = new StringBuilder();
		sb.append(second);
		if (second.length() < 2) {
			for (int i = second.length(); i < 2; i++) {
				sb.append("0");
			}
		}
		second = sb.toString();
		sb = new StringBuilder();
		sb.append(first);
		sb.append(".");
		sb.append(second);
		return sb.toString();
	}

	private String padZeroPrefix(String storeNo, int length) {
		StringBuilder sb = new StringBuilder();
		if (storeNo.length() < length) {
			for (int i = storeNo.length(); i < length; i++) {
				sb.append("0");
			}
		}
		sb.append(storeNo);
		return sb.toString();
	}

	private String addBlankSpace(String statId) {
		if (statId.length() < 8) {
			StringBuffer sb = new StringBuffer();
			sb.append(statId);
			sb.append(PhotoOmniConstants.BLANK);
			return sb.toString();
		} else {
			return statId;
		}
	}
}