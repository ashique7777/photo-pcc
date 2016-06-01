package com.walgreens.batch.central.writer;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	Custom item writer implements Spring itemWriter
 * </p>
 * 
 * <p>
 * Class will set processed report as inactive
 * </p>
 * {@link CSVFileCustomWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to update report status as inactive
 * @author CTS
 * @since v1.0
 */
public class PMByWICItemWriter implements ItemWriter<PMBYWICReportPrefDataBean>{
	PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean;

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PMByWICItemWriter.class);
	/**
	 * jdbcTemplate
	 */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * method will set processed report as inactive
	 * 
	 * @param items -- List of pmBYwic data beans
	 * @throws Exception
	 * */
	public void write(List<? extends PMBYWICReportPrefDataBean> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PMByWICItemWriter.write() >--> ");
		}
		try {
			objPMBYWICReportPrefDataBean = items.get(0);
			String strUserPrefTblUpdtQuery = ReportsQuery.updateQueryUserPRef().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Update Query in PMByWICItemWriter.write() >>-> : " + strUserPrefTblUpdtQuery);
			}
			Object[] objUserParms = new Object[] {objPMBYWICReportPrefDataBean.getSysUserReportPrefId()};
			jdbcTemplate.update(strUserPrefTblUpdtQuery, objUserParms);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at PMByWICItemWriter.write() >--> " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From PMByWICItemWriter.write() >-->");
			}
		}
	}
}