
/**
 * PSReportItemWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 04 mar 2015
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
import com.walgreens.batch.query.PrintSignsReportQuery;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This writer class update the active flag for the particular report id.
 * @author CTS
 * @version 1.1 March 04, 2015
 */

public class PSReportItemWriter implements ItemWriter<LCAndPSReportPrefDataBean>{
	/**
	 * PSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean pSReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PSReportItemWriter.class);
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
			LOGGER.debug(" Entering write method of PSReportItemWriter ");
		}
		try {
			pSReportPrefDataBean = items.get(0);
			final String strUserPrefTblUpdtQuery = PrintSignsReportQuery.updateQueryUserPRef().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" update Query for Print Sign Report is  : " + strUserPrefTblUpdtQuery);
			}
			final Object[] objUserParms = new Object[] {pSReportPrefDataBean.getReportPrefId()};
			jdbcTemplate.update(strUserPrefTblUpdtQuery, objUserParms);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process write of PSReportItemWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of PSReportItemWriter ");
			}
		}
	}
}
