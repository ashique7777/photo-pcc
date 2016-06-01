package com.walgreens.admin.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.walgreens.admin.rowmapper.AdminEnvPropBeanRowMapper;
import com.walgreens.common.cache.EvePropBean;

@Qualifier("AdminWebEnvConfigDAO")
@Repository()
public class AdminWebEnvConfigDAOImpl implements AdminWebEnvConfigDAO {
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AdminWebEnvConfigDAOImpl.class);

	@Override
	public List<EvePropBean> getEnvProperties(String envMode) {
		
		LOGGER.debug("Entering AdminWebEnvConfigDAOImpl getEnvProperties()");
		List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
		
		String envDetailsQuery = "SELECT SYS_ENV_PROPERTIES_ID,ENV_NAME,PROPERTY_NAME,PROPERTY_TYPE,PROPERTY_VALUE FROM OM_ENV_PROPERTIES WHERE ENV_NAME = ?";
		LOGGER.info(" envDetailsQuery " + envDetailsQuery + " envMode " + envMode);
		try {
			 evePropBeanList =jdbcTemplate.query(envDetailsQuery,
					new Object[] { envMode },
			 new AdminEnvPropBeanRowMapper());		
			
		} catch (Exception ex) {
			LOGGER.error("Exception occured in AdminWebEnvConfigDAOImpl getEnvProperties()" + ex.getMessage());
			LOGGER.error(" envDetailsQuery " + envDetailsQuery + " envMode " + envMode);
		} finally {

		}
		LOGGER.debug("Exiting AdminWebEnvConfigDAOImpl getEnvProperties()");
		return evePropBeanList;
	}


	
}
