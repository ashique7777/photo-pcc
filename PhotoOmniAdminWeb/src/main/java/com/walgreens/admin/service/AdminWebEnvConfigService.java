package com.walgreens.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.walgreens.admin.bo.AdminWebEnvConfigBo;
import com.walgreens.admin.factory.AdminBOFactory;
import com.walgreens.common.cache.EnvConfigCache;
import com.walgreens.common.cache.EvePropBean;

@Component("AdminWebEnvConfigService")
public class AdminWebEnvConfigService extends EnvConfigCache {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AdminWebEnvConfigService.class);

	@Autowired
	private AdminBOFactory adminBOFactory;

	@Value( "${env}" )
	public String envMode;
	
	@Override
	@Cacheable(value = "AdminEvePropBeanCache")
	public List<EvePropBean> getEnvProperties() {

		LOGGER.debug("Entering AdminWebEnvConfigService getEnvProperties()");
		LOGGER.debug(" AdminWebEnvConfigService  Environment name : " + envMode);
		List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
		AdminWebEnvConfigBo envConfigBo = adminBOFactory.getAdminWebEnvConfigBo();
		LOGGER.debug(" AdminWebEnvConfigService  Environment After BO facotry : " + envMode);
		evePropBeanList = envConfigBo.getEnvProperties(envMode);
		LOGGER.debug("Exiting EnvConfigServiceImpl getEnvProperties()");
		return evePropBeanList;
	}

	@CacheEvict(value = "EvePropBeanCache", allEntries = true)
	@Override
	public void evictCache(String envMode) {
		LOGGER.debug("Inside AdminWebEnvConfigService evictCache()");
		LOGGER.debug(" AdminWebEnvConfigService  Environment name : " + envMode);
	}

}
