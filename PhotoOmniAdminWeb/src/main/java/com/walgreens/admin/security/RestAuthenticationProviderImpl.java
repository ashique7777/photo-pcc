/**
 * 
 */

package com.walgreens.admin.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.admin.factory.AdminBOFactory;
import com.walgreens.admin.service.AdminWebEnvConfigService;
import com.walgreens.common.cache.EvePropBean;
import com.walgreens.common.security.rest.RestAuthenticationProvider;

/**
 * @author CTS
 *
 */
public class RestAuthenticationProviderImpl  extends RestAuthenticationProvider{
	

	/**
	 * logger to log the details.
	 */

	private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationProviderImpl.class);
	@Autowired
	private AdminBOFactory boFactory;
	
	/* @Autowired
	 @Qualifier("envConfig")
	 private ConcurrentMapCacheFactoryBean concurrentMapCacheFactoryBean;*/
	
	@Autowired
	AdminWebEnvConfigService adminWebEnvConfigService;
	
	@Override
	public  String getSecretKey(String propName){
		List<EvePropBean> evePropBeanList = new ArrayList<EvePropBean>();
		
		String secretKey = null;
		//AdminWebEnvConfigService adminWebEnvConfigService = new AdminWebEnvConfigService();
  		evePropBeanList = adminWebEnvConfigService.getEnvProperties();	
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
