
/**
 * LCReportItemWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 12 mar 2015
 *  
 **/
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.query.LicenseContentReportQuery;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This writer class update the active flag for the particular report id.
 * @author CTS
 * @version 1.1 March 12, 2015
 */

public class LCReportItemWriter implements ItemWriter<LCAndPSReportPrefDataBean>{
	/**
	 * PSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean PSReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCReportItemWriter.class);
    /**
     * jdbcTemplate
     */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
   
	/**
	 * This write method update the active flag for the particular report id.
	 * @param items contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(final List<? extends LCAndPSReportPrefDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of LCReportItemWriter ");
		}
		try {
			PSReportPrefDataBean = items.get(0);
			final String strUserPrefTblUpdtQuery = LicenseContentReportQuery.updateQueryUserPRef().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" update Query for Licesnce Content Report is  : " + strUserPrefTblUpdtQuery);
			}
            final Object[] objUserParms = new Object[] {PSReportPrefDataBean.getReportPrefId()};
			jdbcTemplate.update(strUserPrefTblUpdtQuery, objUserParms);
		  } catch (Exception e) {
			LOGGER.error(" Error occoured at process write of LCReportItemWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		  } finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of LCReportItemWriter ");
			}
		  }
	}
}
