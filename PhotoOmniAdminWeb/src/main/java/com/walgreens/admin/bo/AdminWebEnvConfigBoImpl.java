package com.walgreens.admin.bo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.walgreens.admin.dao.AdminWebEnvConfigDAO;
import com.walgreens.admin.factory.AdminDAOFactory;
import com.walgreens.common.cache.EvePropBean;


@Qualifier("AdminWebEnvConfigBo")
@Service()
public class AdminWebEnvConfigBoImpl implements AdminWebEnvConfigBo {

	@Autowired()
	private AdminDAOFactory adminDAOFactory ;

	/**
	 * logger to log the details.
	 */

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AdminWebEnvConfigBoImpl.class);

	@Override
	public List<EvePropBean> getEnvProperties(String envMode) {
		
			LOGGER.debug("Entering AdminWebEnvConfigBoImpl getEnvProperties()");
			List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
			AdminWebEnvConfigDAO envConfigDAO = adminDAOFactory.getAdminWebEnvConfigDAO();
			evePropBeanList = envConfigDAO.getEnvProperties(envMode);
			LOGGER.debug("Exiting AdminWebEnvConfigBoImpl getEnvProperties()");
			return evePropBeanList;
		
	}
	

}
