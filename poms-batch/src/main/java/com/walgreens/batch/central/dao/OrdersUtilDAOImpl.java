package com.walgreens.batch.central.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.walgreens.batch.query.EmailReportQuery;
import com.walgreens.batch.query.OrderUtilQuery;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
/**
 * <p>
 * A utility class for DAO transactions
 * </p>
 *
 * @author CTS
 * @since v1.0
 */
@Component
public class OrdersUtilDAOImpl implements OrdersUtilDAO {

	/**
	 * database object for DAO transactions
	 */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersUtilDAOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.batch.central.dao.OrdersUtilDAO#getReportData(reportName)
	 */
	@Override
	public List<Map<String, Object>> getReportData(String reportName) throws PhotoOmniException {
		LOGGER.info("Ennerted into OrdersUtilDAOImpl.getReportData() method",reportName);
		List<Map<String, Object>> data;
		try{
			data = jdbcTemplate.queryForList(EmailReportQuery.getEmailReportData(),
					reportName);
		}catch (DataAccessException e) {
			LOGGER.error(" Error occoured while fetching email report data in OrdersUtilDAOImpl.getReportData() method "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}catch (Exception e) {
			LOGGER.error(" Error occoured in OrdersUtilDAOImpl.getReportData() method "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			LOGGER.info(" Exiting from OrdersUtilDAOImpl.getReportData() method");
		}
		return  data;
	}
	
	/**
	 * This method handles database transaction to get EMAIL id from OM_REPORT_EMAIL_CONFIG table.
	 * @param reportConfigName contains reports config name.
	 * @return machineList
	 */
	public StringBuilder getFromEmailId(String reportConfigName)  throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getFromEmailId method of OrdersUtilDAOImpl ");
		}
		StringBuilder emailId = new StringBuilder();
		try {
			String sqlQuery = OrderUtilQuery.getFromEmailId().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Sql to get the from EMAIL-ID : "+ sqlQuery);
			}
			List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sqlQuery, new Object[]{reportConfigName});
			for (Map<String, Object> row : rows) {
				if (!CommonUtil.isNull(row.get("EMAIL_ID"))) {
					emailId.append(row.get("EMAIL_ID"));
					emailId.append(",");
				}
			}
			/*Deleting the last "," char*/
			if (emailId.toString().contains(",")) {
				int lastIndex = emailId.toString().lastIndexOf(",");
				emailId.deleteCharAt(lastIndex);
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getFromEmailId method of OrdersUtilDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at getFromEmailId method of OrdersUtilDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getFromEmailId method of OrdersUtilDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getFromEmailId method of OrdersUtilDAOImpl ");
			}
		}
		return emailId;
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
