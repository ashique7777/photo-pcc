package com.walgreens.oms.bo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.walgreens.common.cache.EvePropBean;
import com.walgreens.oms.dao.OmniWebEnvConfigDAO;
import com.walgreens.oms.factory.OmsDAOFactory;

@Qualifier("OmniWebEnvConfigBo")
@Service()
public class OmniWebEnvConfigBoImpl implements OmniWebEnvConfigBo {

	@Autowired()
	private OmsDAOFactory omsDAOFactory;

	/**
	 * logger to log the details.
	 */

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OmniWebEnvConfigBoImpl.class);

	
	@Override
	public List<EvePropBean> getEnvProperties(String envMode) {
		
		LOGGER.debug("Entering EnvConfigBoImpl getEvePropBean()");
		List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
		OmniWebEnvConfigDAO envConfigDAO = omsDAOFactory.getOmniWebEnvConfigDAO();
		evePropBeanList = envConfigDAO.getEnvProperties(envMode);
		LOGGER.debug("Exiting EnvConfigBoImpl getEvePropBean()");
		return evePropBeanList;
	}
	

}
