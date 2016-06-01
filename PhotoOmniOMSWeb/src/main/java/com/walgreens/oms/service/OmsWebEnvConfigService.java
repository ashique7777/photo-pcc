package com.walgreens.oms.service;

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
import com.walgreens.oms.bo.OmniWebEnvConfigBo;
import com.walgreens.oms.factory.OmsBOFactory;

@Component("OmsWebEnvConfigService")
public class OmsWebEnvConfigService extends EnvConfigCache {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OmsWebEnvConfigService.class);

	@Autowired
	private OmsBOFactory omsBOFactory;

	@Value( "${env}" )
	public String envMode;
	
	@Override
	@Cacheable(value = "EvePropBeanCache")
	public List<EvePropBean> getEnvProperties() {

		LOGGER.debug("Entering EnvConfigServiceImpl getEvePropBean()");
		LOGGER.debug(" EnvConfigServiceImpl  Environment name : " + envMode);
		List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
		OmniWebEnvConfigBo envConfigBo = omsBOFactory.getOmniWebEnvConfigBo();
		evePropBeanList = envConfigBo.getEnvProperties(envMode);
		LOGGER.debug("Exiting EnvConfigServiceImpl getEvePropBean()");
		return evePropBeanList;
	}

	@CacheEvict(value = "EvePropBeanCache", allEntries = true)
	public void evictCache(String envMode) {
		LOGGER.debug("Inside EnvConfigServiceImpl evictCache()");
		LOGGER.debug(" EnvConfigServiceImpl  Environment name : " + envMode);
	}

}
