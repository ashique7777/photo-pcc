package com.walgreens.omni.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.walgreens.common.cache.EnvConfigCache;
import com.walgreens.common.cache.EvePropBean;
import com.walgreens.omni.bo.OmniWebEnvConfigBo;
import com.walgreens.omni.factory.OmniBOFactory;

@Component
public class OmniWebEnvConfigService extends EnvConfigCache {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OmniWebEnvConfigService.class);

	@Autowired
	private OmniBOFactory omniBOFactory;

	@Value( "${env}" )
	public String envMode;
	
	//Set environment mode from Tomcat context

	@Override
	@Cacheable(value = "omniEvePropBeanCache")
	public List<EvePropBean> getEnvProperties() {

		LOGGER.debug("Entering OmniWebEnvConfigService getEnvProperties()");
		LOGGER.debug(" OmniWebEnvConfigService  Environment name : " + envMode);
		List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
		OmniWebEnvConfigBo envConfigBo = omniBOFactory.getOmniWebEnvConfigBo();
		evePropBeanList = envConfigBo.getEnvProperties(envMode);
		LOGGER.debug("Exiting OmniWebEnvConfigService getEnvProperties()");
		return evePropBeanList;
	}

	@CacheEvict(value = "EvePropBeanCache", allEntries = true)
	public void evictCache(String envMode) {
		LOGGER.debug("Inside OmniWebEnvConfigService evictCache()");
		LOGGER.debug(" OmniWebEnvConfigService  Environment name : " + envMode);
	}

}
