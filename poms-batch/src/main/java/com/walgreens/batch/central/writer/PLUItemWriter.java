package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.exception.PhotoOmniException;

public class PLUItemWriter implements ItemWriter<PLUReportPrefDataBean> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PLUItemWriter.class);
	PLUReportPrefDataBean pluReportPrefDataBean;

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public void write(List<? extends PLUReportPrefDataBean> items)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into write method of PLUItemWriter ");
		}
		try {
			pluReportPrefDataBean = items.get(0);
			String updateUserPrefTblQuery = ReportsQuery.updateQueryUserPRef()
					.toString();
			Object[] params = { pluReportPrefDataBean.getSysUserReportPrefId() };
			jdbcTemplate.update(updateUserPrefTblQuery, params);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of PLUItemWriter ---- > "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of PLUItemWriter ");
			}
		}
	}
}