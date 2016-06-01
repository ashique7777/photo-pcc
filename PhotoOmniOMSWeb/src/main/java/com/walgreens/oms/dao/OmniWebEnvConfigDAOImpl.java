package com.walgreens.oms.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.common.cache.EvePropBean;
import com.walgreens.oms.rowmapper.EnvPropBeanRowMapper;

@Qualifier("OmniWebEnvConfigDAO")
@Repository()
public class OmniWebEnvConfigDAOImpl implements OmniWebEnvConfigDAO {
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OmniWebEnvConfigDAOImpl.class);


	@Override
	@Transactional
	public List<EvePropBean> getEnvProperties(String envMode) {
		
		LOGGER.debug("Entering EnvConfigDAOImpl getEvePropBean()");
		List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
		
		String envDetailsQuery = "SELECT SYS_ENV_PROPERTIES_ID,ENV_NAME,PROPERTY_NAME,PROPERTY_TYPE,PROPERTY_VALUE FROM OM_ENV_PROPERTIES WHERE ENV_NAME = ?";
		LOGGER.info(" envDetailsQuery " + envDetailsQuery + " envMode " + envMode);
		try {
			 evePropBeanList =jdbcTemplate.query(envDetailsQuery,
					
					new Object[] { envMode },
			 new EnvPropBeanRowMapper());		
			
		} catch (Exception ex) {
			LOGGER.error("Exception occured in EnvConfigDAOImpl getEvePropBean()" + ex.getMessage());
			LOGGER.error(" envDetailsQuery " + envDetailsQuery + " envMode " + envMode);
		} finally {

		}
		LOGGER.debug("Exiting EnvConfigDAOImpl getEvePropBean()");
		return evePropBeanList;
	}

}
