/**
 * 
 */

package com.walgreens.oms.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.common.cache.EvePropBean;
import com.walgreens.common.security.rest.RestAuthenticationProvider;
import com.walgreens.oms.factory.OmsBOFactory;
import com.walgreens.oms.service.OmsWebEnvConfigService;

/**
 * @author mannasa
 *
 */
public class RestAuthenticationProviderImpl  extends RestAuthenticationProvider{
	

	/**
	 * logger to log the details.
	 */

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestAuthenticationProviderImpl.class);
	@Autowired
	private OmsBOFactory boFactory;
	
	/*@Autowired
	 @Qualifier("envConfig")
	 private ConcurrentMapCacheFactoryBean concurrentMapCacheFactoryBean;*/
	
	@Autowired
	OmsWebEnvConfigService omsWebEnvConfigService;
	
	public  String getSecretKey(String propName){
		List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
		String secretKey = null;
		//OmsWebEnvConfigService omsWebEnvConfigService = new OmsWebEnvConfigService();
  		evePropBeanList = omsWebEnvConfigService.getEnvProperties();	
  		for (EvePropBean evePropBean : evePropBeanList) {
  			if(evePropBean.getPropertyName().compareToIgnoreCase(propName) ==0){
  				secretKey = evePropBean.getPropertyValue();
  				break;
  			}
		}
  		LOGGER.debug("In RestAuthenticationProviderImpl.class");
		return secretKey;
	}
	
	
	
	
	
}
